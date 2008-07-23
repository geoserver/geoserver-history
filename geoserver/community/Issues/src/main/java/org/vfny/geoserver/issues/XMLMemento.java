/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.vfny.geoserver.issues;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class represents the default implementation of the
 * <code>IMemento</code> interface.
 * <p>
 * This class is not intended to be extended by clients.
 * </p>
 * This class was adapted by Q.Anderson to be used in geoserver to enable
 * xml based persistance of any memento passed to the issues service. Methods
 * to save and load were added for files and more importantly strings.
 *
 * @see IMemento
 */
public final class XMLMemento implements IMemento {
    private Document factory;

    private Element element;

    /**
     * Creates a <code>Document</code> from the <code>Reader</code>
     * and returns a memento on the first <code>Element</code> for reading
     * the document.
     * <p>
     * Same as calling createReadRoot(reader, null)
     * </p>
     * 
     * @param reader the <code>Reader</code> used to create the memento's document
     * @return a memento on the first <code>Element</code> for reading the document
     * @throws <code>WorkbenchException</code> if IO problems, invalid format, or no element.
     */
    public static XMLMemento createReadRoot(Reader reader)
            throws Exception {
        return createReadRoot(reader, null);
    }

    /**
     * Creates a <code>Document</code> from the <code>Reader</code>
     * and returns a memento on the first <code>Element</code> for reading
     * the document.
     * 
     * @param reader the <code>Reader</code> used to create the memento's document
     * @param baseDir the directory used to resolve relative file names
     *      in the XML document. This directory must exist and include the
     *      trailing separator. The directory format, including the separators,
     *      must be valid for the platform. Can be <code>null</code> if not
     *      needed.
     * @return a memento on the first <code>Element</code> for reading the document
     * @throws <code>WorkbenchException</code> if IO problems, invalid format, or no element.
     */
    public static XMLMemento createReadRoot(Reader reader, String baseDir)
            throws Exception {
        String errorMessage = null;
        Exception exception = null;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            InputSource source = new InputSource(reader);
            if (baseDir != null)
                source.setSystemId(baseDir);
            Document document = parser.parse(source);
            NodeList list = document.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node instanceof Element)
                    return new XMLMemento(document, (Element) node);
            }
        } catch (ParserConfigurationException e) {
            exception = e;
            errorMessage = e.getMessage(); //$NON-NLS-1$
        } catch (IOException e) {
            exception = e;
            errorMessage = e.getMessage(); //$NON-NLS-1$
        } catch (SAXException e) {
            exception = e;
            errorMessage = e.getMessage(); //$NON-NLS-1$
        }

        String problemText = null;
        if (exception != null)
            problemText = exception.getMessage();
        if (problemText == null || problemText.length() == 0)
            problemText = errorMessage != null ? errorMessage
                    : "XMLMemento.noElement"; //$NON-NLS-1$
        throw new Exception(problemText, exception);
    }

    /**
     * Returns a root memento for writing a document.
     * 
     * @param type the element node type to create on the document
     * @return the root memento for writing a document
     */
    public static XMLMemento createWriteRoot(String type) {
        Document document;
        try {
            document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().newDocument();
            Element element = document.createElement(type);
            document.appendChild(element);
            return new XMLMemento(document, element);
        } catch (ParserConfigurationException e) {
            throw new Error(e);
        }
    }

    /**
     * Creates a memento for the specified document and element.
     * <p>
     * Clients should use <code>createReadRoot</code> and
     * <code>createWriteRoot</code> to create the initial
     * memento on a document.
     * </p>
     * 
     * @param document the document for the memento
     * @param element the element node for the memento
     */
    public XMLMemento(Document document, Element element) {
        super();
        this.factory = document;
        this.element = element;
    }

    /* (non-Javadoc)
     * Method declared in IMemento.
     */
    public IMemento createChild(String type) {
        Element child = factory.createElement(type);
        element.appendChild(child);
        return new XMLMemento(factory, child);
    }

    /* (non-Javadoc)
     * Method declared in IMemento.
     */
    public IMemento createChild(String type, String id) {
        Element child = factory.createElement(type);
        child.setAttribute(TAG_ID, id == null ? "" : id); //$NON-NLS-1$
        element.appendChild(child);
        return new XMLMemento(factory, child);
    }

    /* (non-Javadoc)
     * Method declared in IMemento.
     */
    public IMemento copyChild(IMemento child) {
        Element childElement = ((XMLMemento) child).element;
        Element newElement = (Element) factory.importNode(childElement, true);
        element.appendChild(newElement);
        return new XMLMemento(factory, newElement);
    }

    /* (non-Javadoc)
     * Method declared in IMemento.
     */
    public IMemento getChild(String type) {

        // Get the nodes.
        NodeList nodes = element.getChildNodes();
        int size = nodes.getLength();
        if (size == 0)
            return null;

        // Find the first node which is a child of this node.
        for (int nX = 0; nX < size; nX++) {
            Node node = nodes.item(nX);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (element.getNodeName().equals(type))
                    return new XMLMemento(factory, element);
            }
        }

        // A child was not found.
        return null;
    }

    /* (non-Javadoc)
     * Method declared in IMemento.
     */
    public IMemento[] getChildren(String type) {

        // Get the nodes.
        NodeList nodes = element.getChildNodes();
        int size = nodes.getLength();
        if (size == 0)
            return new IMemento[0];

        // Extract each node with given type.
        ArrayList list = new ArrayList(size);
        for (int nX = 0; nX < size; nX++) {
            Node node = nodes.item(nX);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (element.getNodeName().equals(type))
                    list.add(element);
            }
        }

        // Create a memento for each node.
        size = list.size();
        IMemento[] results = new IMemento[size];
        for (int x = 0; x < size; x++) {
            results[x] = new XMLMemento(factory, (Element) list.get(x));
        }
        return results;
    }

    /* (non-Javadoc)
     * Method declared in IMemento.
     */
    public Float getFloat(String key) {
        Attr attr = element.getAttributeNode(key);
        if (attr == null)
            return null;
        String strValue = attr.getValue();
        try {
            return new Float(strValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /* (non-Javadoc)
     * Method declared in IMemento.
     */
    public String getID() {
        return element.getAttribute(TAG_ID);
    }

    /* (non-Javadoc)
     * Method declared in IMemento.
     */
    public Integer getInteger(String key) {
        Attr attr = element.getAttributeNode(key);
        if (attr == null)
            return null;
        String strValue = attr.getValue();
        try {
            return new Integer(strValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /* (non-Javadoc)
     * Method declared in IMemento.
     */
    public String getString(String key) {
        Attr attr = element.getAttributeNode(key);
        if (attr == null)
            return null;
        return attr.getValue();
    }

    /* (non-Javadoc)
     * Method declared in IMemento.
     */
    public String getTextData() {
        Text textNode = getTextNode();
        if (textNode != null) {
            return textNode.getData();
        } else {
            return null;
        }
    }

    /**
     * Returns the Text node of the memento. Each memento is allowed only 
     * one Text node.
     * 
     * @return the Text node of the memento, or <code>null</code> if
     * the memento has no Text node.
     */
    private Text getTextNode() {
        // Get the nodes.
        NodeList nodes = element.getChildNodes();
        int size = nodes.getLength();
        if (size == 0)
            return null;
        for (int nX = 0; nX < size; nX++) {
            Node node = nodes.item(nX);
            if (node instanceof Text) {
                return (Text) node;
            }
        }
        // a Text node was not found
        return null;
    }

    /**
     * Places the element's attributes into the document.
     */
    private void putElement(Element element) {
        NamedNodeMap nodeMap = element.getAttributes();
        int size = nodeMap.getLength();
        for (int i = 0; i < size; i++) {
            Attr attr = (Attr) nodeMap.item(i);
            putString(attr.getName(), attr.getValue());
        }

        NodeList nodes = element.getChildNodes();
        size = nodes.getLength();
        for (int i = 0; i < size; i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                XMLMemento child = (XMLMemento) createChild(node.getNodeName());
                child.putElement((Element) node);
            }
        }
    }

    /* (non-Javadoc)
     * Method declared in IMemento.
     */
    public void putFloat(String key, float f) {
        element.setAttribute(key, String.valueOf(f));
    }

    /* (non-Javadoc)
     * Method declared in IMemento.
     */
    public void putInteger(String key, int n) {
        element.setAttribute(key, String.valueOf(n));
    }

    /* (non-Javadoc)
     * Method declared in IMemento.
     */
    public void putMemento(IMemento memento) {
        putElement(((XMLMemento) memento).element);
    }

    /* (non-Javadoc)
     * Method declared in IMemento.
     */
    public void putString(String key, String value) {
        if (value == null)
            return;
        element.setAttribute(key, value);
    }

    /* (non-Javadoc)
     * Method declared in IMemento.
     */
    public void putTextData(String data) {
        Text textNode = getTextNode();
        if (textNode == null) {
            textNode = factory.createTextNode(data);
            element.appendChild(textNode);
        } else {
            textNode.setData(data);
        }
    }

    /**
     * Saves this memento's document current values to the
     * specified writer. 
     *  
     * @param writer the writer used to save the memento's document
     * @throws IOException if there is a problem serializing the document to the stream.
     */
    public void save(Writer writer) throws IOException {
        Result result = new StreamResult(writer);
        Source source = new DOMSource(factory);
        try {
            Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
            transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            throw (IOException) (new IOException().initCause(e));
        } catch (TransformerException e) {
            throw (IOException) (new IOException().initCause(e));
        }
    }
    
    /**
     * Save this Memento to a Writer.
     */
    public void save(OutputStream os) throws IOException {
        Result result = new StreamResult(os);
        Source source = new DOMSource(factory);
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
            transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
            transformer.transform(source, result);
        } catch (Exception e) {
            throw (IOException) (new IOException().initCause(e));
        }
    }

    /**
     * Saves the memento to the given file.
     *
     * @param filename java.lang.String
     * @exception java.io.IOException
     */
    public void saveToFile(String filename) throws IOException {
        Writer w = null;
        try {
            w = new FileWriter(filename);
            save(w);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e.getLocalizedMessage());
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (Exception e) { }
            }
        }
    }

    public String saveToString() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        save(out);
        return out.toString("UTF-8");
    }
    
    /**
     * Loads a memento from the given filename.
     *
     * @param filename java.lang.String
     * @return org.eclipse.ui.IMemento
     * @exception java.io.IOException
     */
    public static IMemento loadMemento(String filename) throws Exception {
        return XMLMemento.createReadRoot(new FileReader(filename));
    }

    /**
     * Loads a memento from the given filename.
     *
     * @param url java.net.URL
     * @return org.eclipse.ui.IMemento
     * @exception java.io.IOException
     */
    public static IMemento loadMemento(URL url) throws Exception {
        return XMLMemento.createReadRoot(new InputStreamReader(url.openStream()));
    }
    
    /**
     * Loads a memento from the given string.
     *
     * @param url java.net.URL
     * @return org.eclipse.ui.IMemento
     * @exception java.io.IOException
     */
    public static IMemento loadMementoFromString(String mementoString) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(mementoString.getBytes());
        return XMLMemento.createReadRoot(new InputStreamReader(in));
    }
}
