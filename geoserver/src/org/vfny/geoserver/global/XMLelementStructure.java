/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

   import org.vfny.geoserver.global.FeatureTypeInfo;
   import org.vfny.geoserver.global.Data;   
   import org.vfny.geoserver.global.NameSpaceInfo;
   import org.xml.sax.helpers.NamespaceSupport;   
   import org.vfny.geoserver.responses.wfs.ASFeatureTransformer.FeatureTranslator;
   import org.geotools.feature.Feature;
   import org.xml.sax.helpers.AttributesImpl;
   import java.util.List;
   import java.util.ArrayList;
   import java.util.LinkedList;
   import java.util.TreeSet;
   import java.util.SortedSet;
   import java.util.HashMap;
   import java.util.HashSet;   
   import java.util.Iterator;

   import java.util.logging.Logger;

/**
 * Parse xpath attributes in schema.xml so that a result set formatter can
 * iterate through the result set in such a way that it can build 
 * XML nested element output. (xpath attributes have been transferred to
 * AttributeTypeInfos accessible from FeatureTypeInfo)
 *
 * @author Peter Barrs, Social Change Online
 * @version $Id$
 */
public class XMLelementStructure {

    private static final Logger LOGGER = Logger.getLogger("org.vfny.geoserver.global");
	
	private Elements head = new Elements();	// start of recursive structure
	private Helper helper = null;
	
	/*
	 * Count of db attributes - this is expected to be the same as the 
	 * count of attributes in the result set.
	 */
	private int dbAttributeCount;
	
	/*
	 * pointer to namespaces declared in catalog.xml via Data catalogue object
	 * 
	 */	
	private Data data;


	
    /**
     * Factory method to create a tree structured list of XML elements
     * and their attributes in a form suitable for nested GML elements
     * from a result set, as a report writer might do.
     *
     * @param ft FeatureTypeInfo containing AttributeTypeInfo with 
     * xapth attriubutes from schema.xml for this feature type
     * @param data Data - pointer to catalogu (of datastores) 
     *
     * @return element tree *or* null if not supported by FeatureTypeInfo
     *
     * @throws IOException 
     *
     * @see JDBCDataStore#buildAttributeType(ResultSet)
     */	
	public static XMLelementStructure createXMLelementStructure(FeatureTypeInfo ft, Data data) {
	
		if (ft == null) {
			return null;
		}
		
		XMLelementStructure structure = new XMLelementStructure();
		
		// create the Elements structure; group xpath attributes in 
		// FeatureTypeInfo into parent + childless elements (BreakGroup)
		// and a list of nesting elements (elements with children)
		//
		// element values and asscoiated attributes are mapped in schema.xml
		// to a database attribute
		
		List atts = ft.getAttributes();
		if (atts == null) {
			// LOGGER.warning("FeatureTypeInfo " + ft.getName() + " does not have attributes!");
			return null; 
		}
		if (!Helper.checkXpaths(ft)) {
			LOGGER.info("schema.xml for FeatureTypeInfo " + ft.getName() + " does not have, or is missing xpath attributes - nested element GML output turned off");
			return null;
		}

		structure.data = data;
		structure.helper = new Helper(ft);
		
		int index = -1;
		Iterator i = atts.iterator();
		while (i.hasNext()) {
			index++;
			
			AttributeTypeInfo att = (AttributeTypeInfo)i.next();
			String name = att.getName();
			String xpath = att.getXpath();
			LOGGER.finer("***** xpath =  " + xpath);
			if (xpath == null) continue;

			structure.addStructure(structure.head, null, "", xpath, name, index);
		}
		structure.dbAttributeCount = ++index;
		
		return structure;
	}
	

	public int getDBattributeCount() {
		return dbAttributeCount;
	}
		
	private void addStructure(
			Elements treeNode, Element element, String lpath, String rpath,
			String dbAttName, int dbAttIndex) {

		// pop the first structure (element or attribute) off the xpath
		int start = 0;
		// if (rpath.charAt(0) == '/') start++;	
		int finish = rpath.indexOf('/', start+1);
		if (finish == -1) {
			// does an attribute follow ?
			finish = rpath.indexOf('@', start+1);
		}
		if (finish == -1) {
			finish = rpath.length();
		}

		String structure = rpath.substring(start, finish);
		// LOGGER.fine("structure = " + structure);
		
		if (finish == rpath.length()) {
			rpath = null;	// leaf node
		} else {
			rpath = rpath.substring(finish);
		}
		lpath = lpath + structure;
		LOGGER.finer("lpath = " + lpath + ", structure = " + structure);

		// do grandchildren case first ?
		
		if (helper.hasChildren(lpath)) {
		
			LOGGER.finer("has Children");
			
			// find or create a grouping element
			treeNode = treeNode.doGroupElement(structure);
			element = treeNode.groupingElement;
			LOGGER.finer("Grouping Element is " + element.name.get(0));
			
		} else if (structure.startsWith("/")) {
			LOGGER.finer("element, no children");

			if (rpath == null) {
				element = treeNode.doBreakElement(structure, dbAttName, dbAttIndex);
			} else {
				element = treeNode.doBreakElement(structure, null, -1);				
			}
			
		} else if (structure.startsWith("@")) {
			LOGGER.finer("attribute");
			
			element.addLeaf(structure, lpath, dbAttName, dbAttIndex);
			
		} else {
			LOGGER.warning("Could not process xpath " + lpath + rpath +
					"for result set attribute " + dbAttName);
			return;
		}
		
		// repeat for the rest of the path
		if (rpath != null) addStructure(treeNode, element, lpath, rpath, dbAttName, dbAttIndex);
	}


    /**
     * Writes elements and attributes from break element down to the 
     * leaf elements from <breakElement>; Currently treats Elements as 
     * a list rather than a tree - ie will only process the top element
     * at any given nesting level. If breakElement = "", writes from the
     * top down.
     *
     * @param breakElement Write out elements from this element down
     * @param f the GT2 feature to write elements from
     * @param featureTranslator object providing handleAttribute() callback 
     *
     */
	public void writeElements(String breakElement, Feature feature, 
			FeatureTranslator featureTranslator)
	{
		Elements start = head;
		
		if (!(breakElement == null || breakElement == "" )) {

			// determine position to write from
			start = helper.findGroupingElementInList(breakElement, head);
			if (start == null) {

				// breakElement should be a repeated leaf element, write this
				// out directly
				Element leaf = helper.findLeafElement(breakElement, head);
				if (leaf != null) {
					// LOGGER.fine("Writing repeated leaf element" + leaf.getName());
					AttributesImpl SAXatts = leaf.buildSAXatts(feature);
					featureTranslator.handleAttribute(
							leaf.getName(), leaf.getValue(feature),
							SAXatts, true);	
					return;
				} else
					throw new RuntimeException (
							"Could not find element " +	breakElement);
			}
		} else {
			// for debugging only
			breakElement = start.groupingElement.getName();
		}

		// LOGGER.fine ("writing elements from and including " + breakElement);
		
		// write elements
		
		Element element; 
		AttributesImpl SAXatts; 
		
		while (start != null) {

			// write grouping element
			element = start.groupingElement;
			// LOGGER.fine("writing grouping element " + element.getName());
			if (element != null) {
				SAXatts = element.buildSAXatts(feature);
				featureTranslator.handleAttribute(
					element.getName(), null, SAXatts, false);						
					// element.getName(), element.getValue(feature), SAXatts, false);
			}
			
			// write leaf element under the grouping element
			if (start.breakElements != null) {
				Iterator i = start.breakElements.iterator();
				while (i.hasNext()) {
					element = (Element)i.next();
					// LOGGER.fine("writing leaf element " + element.getName());
					SAXatts = element.buildSAXatts(feature);
					featureTranslator.handleAttribute(
							element.getName(), element.getValue(feature),
							SAXatts, true);
				}
			}
			
			// do it again ...
			Iterator i = start.nestingElements.iterator();
			if (i.hasNext()) {
				 // treat as list - only look at top element in this level					
				start = (Elements) i.next();  
			} else {
				start = null;
			}
		}
	}

    /**
     * Closes grouping element tags, working from the grouping element at
     * the end of the list back to the grouping element given by 
     * break element. Leaf elements within a grouping elements already have
     * their tags closed.
     *
     * @param breakElement close tags upto and including this element
     * @param featureTranslator object providing handleAttribute() callback 
     *
     */
	public void closeTags(String breakElement, FeatureTranslator ft) {

		// todo - figure out a caching or flattening strategy, 
		// don't need/want to recompute each row
		
		Elements node = head;
		
		if (! (breakElement == null || breakElement == "" )) {

			// determine position to write from
			node = helper.findGroupingElementInList(breakElement, node);
			if (node == null)
				// not an open tag - will happen with repeated leaf nodes
				// probably needs a tidy up - shouldn't need to do this
				return;
		} else {
			// for debugging only
			breakElement = node.groupingElement.getName();
		}

		// LOGGER.fine ("Closing elements from end up to and including " + breakElement);
		closeTag(node, ft);
	}
	
	private void closeTag(Elements node, FeatureTranslator ft ) {
		
		// recurse to end of list, close tags on the way back
		
		if (node.nestingElements.size() > 0) {

			Iterator i = node.nestingElements.iterator();
			if (i.hasNext()) {
				 // treat as list - only look at top element in this level					
				Elements nextNode = (Elements) i.next();
				closeTag(nextNode, ft);
				if (node.groupingElement != null) {
					// LOGGER.fine("closing element " + node.groupingElement.getName());
					ft.closeTag(node.groupingElement.getName());
				}
			}
		} else {
			if (node.groupingElement != null) {
				// LOGGER.fine("closing element " + node.groupingElement.getName());
				ft.closeTag(node.groupingElement.getName());
			}
		}	
	}

	
    /**
     * Works through the Elements nodes to figure out in which group a change
     * has happened. If a goupingElement has a fid attribute, then we only
     * need to look at that fid attribute to detect a group change, otherwise we
     * have to look at all the database attribute values in the group element AND
     * its break (leaf) elements and their attributes.
     * <p>
     * Assumes the result set rows is ordered as per the XML element enumertaion
     * <br>
     * Assumes .equals() can be legimately applied to database attribute values
     * <br>
     * Treats Elements as a list; that is, only looks at the first element in
     * nesting elements; this is to be revisited, when we look at merging 
     * feature results.
     * </p>
     * 
     * @param feature The Feature providing the "current" state 
     * @param previousValue Array of objects providing the "previous" state. 
     *
     * @return the group element name of the group in which a changed value was
     * detected 
     */
	public String findBreak(Feature feature, Object[] previousValue) {

		Elements node = head;
		Elements lastNode = null;
		String breakElement = null;
		
		while (node != null && breakElement == null) {
			
			// LOGGER.fine("examining element group " + node.groupingElement.getName() + " for change");
			breakElement = node.checkForBreak(feature, previousValue);

			if (breakElement != null) {
				// LOGGER.fine("found break at element " + breakElement);
				return breakElement;
			} else {
				Iterator i = node.nestingElements.iterator();
				if (i.hasNext()) {
					 // treat as list - only look at top element in this level
					lastNode = node;
					node = (Elements) i.next();
				} else {
					node = null;
				}
			}
		}
		LOGGER.fine("no change detected - report as top level element change");
		return head.groupingElement.getName();
	}

	/**
	*
	* Load the namespaces corresponding to the prefixes scanned from the 
	* xpaths in schema.xml
	* 
	* need to pass in some namespace related object from ASFeatureTransformer
	*  
	*/
	public void loadGlobalNamespaces(HashSet allAttPrefixes, NamespaceSupport nsSupport) {

		NameSpaceInfo nsi;
		String uri;
		
		Iterator i = helper.prefixList.iterator();
		while ( i.hasNext()) {
			String prefix = (String) i.next();
			if (!allAttPrefixes.contains(prefix)) {
				allAttPrefixes.add(prefix);
				nsi = data.getNameSpace(prefix);
				if (nsi != null) {
					uri = nsi.getURI();
				} else {
					uri = "http://missing.namespace.in.catalog.xml";
				}
            	nsSupport.declarePrefix(prefix, uri);
			}
		}
	}
	
	private class Element {

		// represents an element; index 0 pertains to the element,
		// indices 1..n pertain to its attributes

		private ArrayList name = new ArrayList();
		private ArrayList rsAttName = new ArrayList();		// column in result set
		private ArrayList rsAttIndex = new ArrayList(); 	// position of attribute in result set 
		private int fidIndex = -1;
		boolean suppressFID = false;

		private void addParentElement(String elementName) {
			
			// a parent element is not associated with a db attribute
			// (it could have attributes though - added subsequently through addLeaf )
			
			if (name.size() == 0) {
				elementName = elementName.replaceFirst("/","");
				name.add(elementName);
				rsAttName.add("");
				rsAttIndex.add(new Integer(0));
				LOGGER.finer("added grouping element " + elementName);
			} else {
				// throw an exception ?
				LOGGER.warning("Error adding parent XML element " + elementName + " to processing tree");
			}
		}
		
		private void addLeaf( 
				String leafName, String xpath, String dbAttName, int attIndex) {

			boolean isAttribute = false;
			
			if (leafName.startsWith("@")) {
				isAttribute = true;
				leafName = leafName.replaceFirst("@","");
			} else {
				leafName = leafName.replaceFirst("/","");
			}
			name.add(leafName);
			rsAttName.add(dbAttName);
			rsAttIndex.add(new Integer(attIndex));			
			
			if (isAttribute) {
				if (leafName.equals("fid") || leafName.equals("gml:id")) {
					fidIndex = name.size()-1;
					Integer maxOccurs = (Integer)helper.fidElementList.get(xpath);
					if (maxOccurs != null) {
						if (maxOccurs.intValue() == 0 ) {
							suppressFID = true;
							LOGGER.finer("suppress = true");
						}
					}
				}
			}
			LOGGER.finer("Added leaf " + leafName + ", " + dbAttName + ", " + attIndex);
		}

		private String getName() {
			return (String)name.get(0);
		}
		 
		private Object getValue(Feature f) {
			if ( ((Integer)rsAttIndex.get(0)).intValue() > -1 ) {
				return f.getAttribute( ((Integer)rsAttIndex.get(0)).intValue() );
			} else {
				return null;
			}
		}
		
		private AttributesImpl buildSAXatts(Feature f) {
			
			AttributesImpl SAXatts = new AttributesImpl();
			String attVal;
			String attName;
		
			// attributes have index values 1..n, 0 gives the element
			for (int i = 1; i < name.size(); i++) {

				attName = (String)name.get(i);
				if (attName.equals("fid") || attName.equals("gml:id") ) {
					if (suppressFID) continue;
				}
				
				Object o = f.getAttribute( ((Integer)rsAttIndex.get(i)).intValue() );
				if (o == null) 
					attVal = "";
				else
					attVal = o.toString();
					
				// LOGGER.fine( (String)name.get(i) + ":" + attVal);
				SAXatts.addAttribute("", "", attName, "", attVal);
			}
			return SAXatts;
		}
		
		public String toString() {
			String names = "";
			
			Iterator i = name.iterator();
			while (i.hasNext()) {
				names = names + (String) i.next() + ", ";
			}
			return names;
		}		
	}

		
	private class Elements {

		// tree of XML elements; groupingElement may be null if there is no 
		// element nesting, although there is probably not much  reason to 
		// supply a schema.xml with xpath attributes in that case.
		//
		// Either groupingElement or breakElements may be null, but not both

		
		private Element		groupingElement = null;	
		private ArrayList	breakElements = new ArrayList();	// of Element
		/*
		 * A linked list is used to support having elements of different types
		 * at the same level - however this structure is not yet supported in
		 * output, it would probably operate on multiple result sets,
		 * otherwise the result sets become huge because elements at the
		 * the same level are not related to each other, only the parent.
		 */
		private LinkedList 	nestingElements = new LinkedList();	// of Elements (tree)

		
		/**
		 * finds or creates an Elements and GroupElement of groupName
		 *
		 * @param groupName The grouping element to find or create 
		 *
		 * @return Elements object with groupingElement of groupName
		 */	
		private Elements doGroupElement(String groupName) {

			groupName = groupName.replaceFirst("/","");
			if (groupingElement == null) {
				// this is for the initial case
				groupingElement = new Element();
				groupingElement.addParentElement(groupName);
				return this;
			}
			
			// LOGGER.fine("this Elements groupingElement.name(0) = " + groupingElement.name.get(0));
			if (groupingElement.name.get(0).equals(groupName)) {
				return this;
			}
			
			// LOGGER.fine("walking through linked Elements");
			Iterator i = nestingElements.iterator();
			while (i.hasNext()) {
				Elements es = (Elements) i.next();
				if (es.groupingElement.name.get(0).equals(groupName)) {
					return es;
				}
			}
			
			// have a new nesting element (element that nests other elements)
			// LOGGER.fine("creating new nesting element - adding new group element");
			Elements es = new Elements();
			es.groupingElement = new Element();			
			es.groupingElement.addParentElement(groupName);
			nestingElements.add(es);
			return es;
		}

		/**
		 * finds or creates a BreakGroup Element within this Elements
		 *
		 * @param breakName The BreakGroup.Element to find or create 
		 *
		 * @return BreakGroup.Element object with groupingElement of groupName
		 */		
		private Element doBreakElement(String breakName, String dbAttName, int attIndex) {
			// searches within this Elements only 
			breakName = breakName.replaceFirst("/","");
			Iterator i = breakElements.iterator();
			while (i.hasNext()) {
				Element e = (Element) i.next();
				if (e.name.get(0).equals(breakName)) {
					return e;
				}
			}
			//not found - create it
			Element e = addBreakElement(breakName, dbAttName, attIndex);
			return e;
		}

		private Element addBreakElement(String leafName, String dbAttName, int attIndex) {
			LOGGER.finer("adding break element");
			Element e = new Element();
			e.addLeaf(leafName, "", dbAttName, attIndex);
			breakElements.add(e);			
			return e;
		}

		
		/*
		 * See if there was a change in database attribute values in this Elements
		 * (groupingElement and breakElements).
		 * 
		 * @param feature Feature with current state
		 * @param previousValue array of previous value object states
		 * 
		 * @return grouping element name or null if no change detected
		 */ 
		private String checkForBreak(Feature f, Object[] previousValue) {

			if (groupingElement != null) {
 
				int fidIndex = groupingElement.fidIndex;
				if (fidIndex > -1) {
					
					int rsAttIndex = ((Integer)groupingElement.rsAttIndex.get(fidIndex)).intValue();
					
					// only have to look at the fid attribute for this Elements
/*				
					LOGGER.fine("rsAttIndex = " + rsAttIndex + " db att name = " + groupingElement.rsAttName.get(fidIndex) + " schema name = " + groupingElement.name.get(fidIndex));
					LOGGER.fine("current fid = " + f.getAttribute(rsAttIndex));
					if (previousValue[rsAttIndex] != null) {
						LOGGER.fine("previous fid = " + previousValue[rsAttIndex]);
					} else {
						LOGGER.fine("previous fid is null");
					}
*/					
	               	if (previousValue[rsAttIndex] == null ||
	               			!f.getAttribute(rsAttIndex).equals(previousValue[rsAttIndex])) {
	               		return groupingElement.getName();
	               	}

					// if this is the terminal Elements - iterate through the 
					// leaf elements looking for those that may repeat (currently must
					// have a fid or gml:id XML attribute)

	               	if (nestingElements.isEmpty()) {
	               		Iterator i = breakElements.iterator();
	               		while (i.hasNext()) {
	               			Element e = (Element)i.next();
	               			if (e.fidIndex > -1) {
		               			// LOGGER.fine("inspecting " + e.getName() + " for change");
	               				rsAttIndex = ((Integer)e.rsAttIndex.get(fidIndex)).intValue();
	               				// LOGGER.fine("previousValue[rsAttIndex] = " + previousValue[rsAttIndex]);
	               				// LOGGER.fine("f.getAttribute(rsAttIndex) = " + f.getAttribute(rsAttIndex));	               				
	        	               	if (previousValue[rsAttIndex] == null ||
	        	               			!f.getAttribute(rsAttIndex).equals(previousValue[rsAttIndex])) {
	        	               		return e.getName();
	        	               	}
	               			}
	               		}
	               	}
	               		
	               	return null;

				}
			}
			
			// todo
			// case where no fid or gml:id optimisation
			// check all element value and element attribute values
			// no change?
			// for each break element, while no change
			// check all element value and element attribute values
			return null;
		}
	}
	
	private static class Helper {
		
		TreeSet elementList = new TreeSet();
		HashMap fidElementList = new HashMap();
		HashSet prefixList = new HashSet();
		

		private static boolean checkXpaths(FeatureTypeInfo ft) {

			boolean isOK = false;
			
			List atts = ft.getAttributes();
			
			Iterator i = atts.iterator();
			while (i.hasNext()) {
				AttributeTypeInfo att = (AttributeTypeInfo)i.next();
				if (att.getXpath() == null || att.getXpath().equals("") ) {
					return false;
				}
				isOK = true;
			}
			return isOK;
		}
		
		// String xpath;
		
		private Helper(FeatureTypeInfo ft) {

			// todo : fold into one loop 

			// compile a list of elements
			
			String member;
			
			List atts = ft.getAttributes();
			
			Iterator i = atts.iterator();
			while (i.hasNext()) {
				AttributeTypeInfo att = (AttributeTypeInfo)i.next();
				member = att.getXpath();
				// don't want attributes, but do want enclosing element
				if (member.indexOf('@') > -1 ) {
					member = member.substring(0, member.indexOf('@'));
				}
				elementList.add(member);
			}
			LOGGER.finer("elementList = " + elementList );

			// compile a list of elements having fid || gmld:id atts
			
			i = atts.iterator();
			while (i.hasNext()) {
				AttributeTypeInfo att = (AttributeTypeInfo)i.next();
				member = att.getXpath();
				int maxOccurs = att.getMaxOccurs();
				// don't want attributes, but do want enclosing element
				if ( member.indexOf("@fid") > -1 || member.indexOf("@gml:id") > -1 ) {
					fidElementList.put(member, new Integer(maxOccurs));					
				}
			}
			LOGGER.finer("fidElementList = " + fidElementList );

			// compile a list of namespace prefixes
			
			String prefix;
			
			i = atts.iterator();
			while (i.hasNext()) {
				AttributeTypeInfo att = (AttributeTypeInfo)i.next();
				member = att.getXpath();
				int maxOccurs = att.getMaxOccurs();
				if ( maxOccurs ==  0 ) continue;
					
				int colon = member.indexOf(':');
				// extract all prefixes in this xpath
				while (colon > -1 ) {
					
					int at = member.indexOf('@');
					prefix = null;
					if (at > -1 && at < colon) {
						// attribute prefix
						prefix = member.substring(at+1, colon);
					} else {

						// find / closest to :
						int slash = member.indexOf('/');
						int prevSlash = -1;
						while (slash > -1 && slash < colon) {
							prevSlash = slash;
							slash = member.indexOf('/', slash+1);
						}
						slash = prevSlash;
						if (slash > -1 && slash < colon) {
							// element prefix
							prefix = member.substring(slash+1, colon);
						}
					}
				
					if ( prefix != null && !prefix.equals("gml") )
						prefixList.add(prefix);	// gml is a default prefix
					
					member = member.replaceAll(prefix+":","");
					colon = member.indexOf(':');
				}
			}
			LOGGER.finer("prefixList = " + prefixList );			
		}
		
		/**
		 * Return false if the xpath occurs as a substring, meaning that
		 * the xpath does not parent other elements; xpaths for XML attributes
		 * return true; 
		 *
		 * @param xpath The xpath to test 
		 *
		 * @return true if leaf, false otherwise
		 */	
		private static boolean isAttribute(String xpath) {
			if ( xpath.indexOf('@') != -1 ) {
				return true;
			} else {
				return false;
			}
		}
		
		/**
		 * Return false if the xpath occurs as a substring, meaning that
		 * the xpath does not parent other elements; xpaths for XML attributes
		 * return true; 
		 *
		 * @param xpath The xpath to test 
		 *
		 * @return true if leaf, false otherwise
		 */	
		private boolean isLeaf(String xpath) {
			
			String member = null;
			
			if (xpath.indexOf('@') != -1 ) {
				return true;	// XML attribute
			}
			
			Iterator i = elementList.tailSet(xpath).iterator();	// xpaths >= xpath
			while (i.hasNext()) {
				member = (String) i.next();

				if (member.equals(xpath))
					continue;
				
				if (! member.startsWith(xpath))
					// not related - elementList ordering implies no more matches
					// this should only happend if there is no enclosing element
					return true;

				if (member.indexOf('@') != -1)
					continue; 		// ignore attributes

				return false; 		// has descendant XML elements
			}
			
			return true;			// nothing else to match against
		}

		/**
		 * Return true if xpath has a child element. xpaths for XML attributes
		 * return false; 
		 *
		 * @param xpath The xpath to test 
		 *
		 * @return true if has child element, false otherwise
		 */	
		private boolean hasChildren(String xpath) {
			
			String member = null;
			
			if (isLeaf(xpath)) 
				return false;
			
			Iterator i = elementList.tailSet(xpath).iterator();	// xpaths >= xpath
			while (i.hasNext()) {
				member = (String) i.next();

				if (member.equals(xpath))
					continue;

				if (member.matches( "^" + xpath + "/.+"))
					return true;	
			}
			return false;			// nothing else to match against
		}
		
		private Elements findGroupingElementInList(String breakElement, Elements node) {

			// treats tree as List - ie only looks at first element in
			// nestingElements
			
			// todo : save results in a cache
			
			while (node != null && 
					!node.groupingElement.name.get(0).equals(breakElement) ) {
				Iterator i = node.nestingElements.iterator();
				if (i.hasNext()) {
					 // treat as list - only look at top element in this level
					node = (Elements) i.next();  
				} else {
					node = null;
				}
			}
			return node;
		}

		private Element findLeafElement(String breakElement, Elements node) {

			// todo : save results in a cache
		
			while (!node.nestingElements.isEmpty()) {
			
				Iterator i = node.nestingElements.iterator();
				if (i.hasNext()) {
					 // treat as list - only look at top element in this level
					node = (Elements) i.next();
				}
			}
			
			// see if breakElement is a repeating leaf element
			Iterator i = node.breakElements.iterator();
			while ( i.hasNext() ) {
				Element e = (Element)i.next();
				if (e.getName().equals(breakElement))
					return e; 
			}
			return null;
		}
		
	}
}
