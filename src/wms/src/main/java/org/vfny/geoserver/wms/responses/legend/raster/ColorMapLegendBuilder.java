package org.vfny.geoserver.wms.responses.legend.raster;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.vfny.geoserver.wms.responses.ImageUtils;
import org.vfny.geoserver.wms.responses.LegendUtils;


/**
 * @author  Simone Giannecchini, GeoSolutions.
 */
@SuppressWarnings("deprecation")
class ColorMapLegendBuilder {
	/**
	 * @author  Administrator
	 */
	enum ColorMapType {
		UNIQUE_VALUES, RAMP, CLASSES;
		public static ColorMapType create(final String value) {
			if (value.equalsIgnoreCase("intervals"))
				return CLASSES;
			else if (value.equalsIgnoreCase("ramp")) {
				return RAMP;
			} else if (value.equalsIgnoreCase("values")) {
				return UNIQUE_VALUES;
			} else
				return ColorMapType.valueOf(value);
		}
		public static ColorMapType create(final int value) {
			switch (value) {
			case ColorMap.TYPE_INTERVALS:
				return ColorMapType.CLASSES;
			case ColorMap.TYPE_RAMP:
				return ColorMapType.RAMP;
			case ColorMap.TYPE_VALUES:
				return ColorMapType.UNIQUE_VALUES;
				

			default:
				throw new IllegalArgumentException("Unable to create ColorMapType for value "+value);
			}
		}		
	}
	
	abstract class Cell{
	
		protected final Color bkgColor;
		protected final double bkgOpacity;
		protected final String text;

		public Cell(final Color bkgColor, final double bkgOpacity, final String text) {
			this.bkgOpacity=bkgOpacity;
			this.bkgColor = bkgColor;
			this.text=text;
		}
		
		
		public abstract void draw(final Graphics2D graphics,final Rectangle2D clipBox, final boolean completeBorder);
		
		/**
		 * Retrieves the preferred dimension for this {@link Cell} element within the provided graphics element.
		 * 
		 * @param graphics {@link Graphics2D} object to use for computing the preferred dimension
		 * @return the preferred dimension for this {@link Cell} element within the provided graphics element.
		 */
		public abstract Dimension getPreferredDimension(final Graphics2D graphics);
	}
		
	abstract class ColorManager extends Cell{
		

		public ColorManager(final Color color, final double opacity) {
			super(color, opacity, null);
		}
		
		
		public abstract void draw(final Graphics2D graphics,final Rectangle2D clipBox, final boolean completeBorder);
		
		public Dimension getPreferredDimension(final Graphics2D graphics){
			return new Dimension(requestedDimension);
		}

	}
	
	class SimpleColorManager extends ColorManager{

		public SimpleColorManager(Color color, double opacity) {
			super(color, opacity);
		}

		@Override
		public void draw(final Graphics2D graphics, final Rectangle2D clipBox, final boolean completeBorder) {
			//bkgColor fill
            if(bkgOpacity>0){
            	// OPAQUE
	            final Color oldColor=graphics.getColor();
	            final Color newColor= new Color(bkgColor.getRed(),bkgColor.getGreen(),bkgColor.getBlue(),(int) (255*bkgOpacity+0.5));
	            graphics.setColor(newColor);
	            graphics.fill(clipBox);
	            //make bkgColor customizable
	            graphics.setColor(Color.BLACK);
	            if(!completeBorder)
	            {
		            
		            final int minx=(int) (clipBox.getMinX()+0.5);
		            final int miny=(int) (clipBox.getMinY()+0.5);
		            final int maxx=(int) (minx+clipBox.getWidth()+0.5)-1;
		            final int maxy=(int)(miny+clipBox.getHeight()+0.5)-1;	            	
	            	graphics.drawLine(minx,maxy,maxx,maxy);
	            }
	            else
	            {
		            
		            final int minx=(int) (clipBox.getMinX()+0.5);
		            final int miny=(int) (clipBox.getMinY()+0.5);
		            final int w=(int) (clipBox.getWidth()+0.5)-1;
		            final int h=(int)(clipBox.getHeight()+0.5)-1;	   
	            	graphics.draw(new Rectangle2D.Double(minx,miny,w,h));
	            }
	            //restore bkgColor            
	            graphics.setColor(oldColor);
            }
            else
            {
            	// TRANSPARENT
	            final Color oldColor=graphics.getColor();
	            
	            //white background
	            graphics.setColor(Color.white);
	            graphics.fill(clipBox);
	            
	            //now the red cross
	            graphics.setColor(Color.RED);
	            final int minx=(int) (clipBox.getMinX()+0.5);
	            final int miny=(int) (clipBox.getMinY()+0.5);
	            final int maxx=(int) (minx+clipBox.getWidth()+0.5);
	            final int maxy=(int)(miny+clipBox.getHeight()+0.5);
	            graphics.drawLine(minx,miny,maxx,maxy);
	            graphics.drawLine(minx,maxy,maxx,miny);
	            
	            graphics.setColor(Color.BLACK);
	            if(!completeBorder)
	            {
	            	graphics.drawLine(minx,maxy,maxx,maxy);
	            }
	            else
	            	graphics.draw(clipBox);

	            //restore bkgColor            
	            graphics.setColor(oldColor);
            }
			
		}
		
	}
	
	class GradientColorManager extends SimpleColorManager{

		private Color previousColor=null;
		private boolean leftEdge;

		public GradientColorManager(Color color, double opacity, final Color previousColor) {
			super(color, opacity);
			this.previousColor=previousColor;
			if(previousColor==null)
				leftEdge=true;
		}

		@Override
		public void draw(final Graphics2D graphics, final Rectangle2D clipBox, final boolean completeBorder) {
			
			// getting clipbox dimensions
            final double minx=clipBox.getMinX();
            final double miny=clipBox.getMinY();
            final double w=clipBox.getWidth();
            final double h=clipBox.getHeight();	 
			
            // GRADIENT
            if(!leftEdge){
	            //rectangle for the gradient
	            final Rectangle2D.Double rectLegend= new Rectangle2D.Double(
	            		minx,
	            		miny,
	            		w,
	            		h/2.0);
	            
	            //gradient paint
	            final Paint oldPaint=graphics.getPaint();
	            final GradientPaint paint=new GradientPaint(
	            		(float)minx,(float)miny,previousColor,
	            		(float)minx,(float)(miny+h/2.0),bkgColor);
	            
	            // do the magic
	            graphics.setPaint(paint);
	            graphics.fill(rectLegend);
	            
	            //restore paint            
	            graphics.setPaint(oldPaint);
	            
	            
	            
            }		
            
            //COLOR BOX
            //careful with handling the leftEdge case
            final Rectangle2D rectLegend  = new Rectangle2D.Double(
            		minx,
            		miny+(leftEdge?0:h/2.0),
            		w,
            		!leftEdge?h/2.0:h);
            super.draw(graphics, rectLegend, completeBorder);
            
            
			
		}
		
	}	
	
	/**
	 * 
	 * @author Simone Giannecchini, GeoSolutions SAS
	 *
	 */
	class TextManager extends Cell{
	

		public TextManager(String text) {
			super(backgroundColor, 1.0, text);
		}
		
		public Dimension getPreferredDimension(final Graphics2D graphics){
			//get old font
			final Font oldFont=graphics.getFont();
			
			//set new font
            graphics.setFont(labelFont);
            //computing label dimension and creating  buffered image on which we can draw the label on it
	        final int labelHeight = (int) Math.ceil(graphics.getFontMetrics().getStringBounds(text, graphics).getHeight());
	        final int labelWidth = (int) Math.ceil(graphics.getFontMetrics().getStringBounds(text, graphics).getWidth());
	        //restore the old font
	        graphics.setFont(oldFont);	
	        return new Dimension(labelWidth,labelHeight);
		}
		
		public void draw(final Graphics2D graphics,final Rectangle2D clipBox ,final boolean completeBorder){
			
			//save old font
			final Font oldFont=graphics.getFont();
			
			//set font and font color and the antialising
			graphics.setColor(labelFontColor);
			graphics.setFont(labelFont);
			graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			
			//Halign==center vAlign==bottom
            final double minx=clipBox.getMinX();
            final double miny=clipBox.getMinY();
            final double w=clipBox.getWidth(); 
            final Dimension dimension=getPreferredDimension(graphics);
            //where do we draw?
			final int xText=(int)(minx+(w-dimension.getWidth())/2.0+0.5);
			final int yText=(int)(miny+graphics.getFontMetrics().getAscent()+0.5) ;
			//draw
			graphics.drawString(text, xText,yText); 
			
	        //restore the old font
	        graphics.setFont(oldFont);
			
		}
		
		
	}
	 abstract class ColorMapEntryLegendBuilder {		
		
		protected ColorMapEntryLegendBuilder(final List<ColorMapEntry> mapEntries,ColorManager colorManager,
				TextManager labelManager, TextManager ruleManager) {
			this.colorManager = colorManager;
			this.labelManager = labelManager;
			this.ruleManager = ruleManager;
			colorMapEntriesSubset.addAll(mapEntries);
			
		}
		
		protected ColorMapEntryLegendBuilder(final List<ColorMapEntry> mapEntries) {
			colorMapEntriesSubset.addAll(mapEntries);
			
		}

		public abstract boolean hasLabel();

		protected final ArrayList<ColorMapEntry> colorMapEntriesSubset = new ArrayList<ColorMapEntry>();
		
		protected ColorManager colorManager;
		
		protected TextManager labelManager;
		
		protected TextManager ruleManager;

		protected boolean hasLabel;



		public abstract BufferedImage getLegend() ;
		
		public TextManager getRuleManager() {
			return ruleManager;
		}
		
		public TextManager getLabelManager()  {
			return labelManager;
		}
		public ColorManager getColorManager()  {
			return colorManager;
		}
	}
	 class SingleColorMapEntryLegendBuilder extends ColorMapEntryLegendBuilder{
		
		public SingleColorMapEntryLegendBuilder(List<ColorMapEntry> cMapEntries) {
			super(cMapEntries);
			final ColorMapEntry currentCME = cMapEntries.get(0);
			Color color = LegendUtils.color(currentCME);
			final double opacity = LegendUtils.getOpacity(currentCME);
			color=new Color(color.getRed(),color.getGreen(),color.getBlue(),(int) (255*opacity));
			this.colorManager= new SimpleColorManager(color,opacity);
			

			final String label = currentCME.getLabel();
			final double quantity = LegendUtils.getQuantity(currentCME);
			final String symbol = " = "; 
            String rule = Double.toString(quantity)+" "+symbol+" x";

            this.ruleManager= new TextManager(rule);
            		
            // add the label the label to the rule so that we draw all text just once 
            if(label!=null)
            {
            	
            	hasLabel=true;
            	this.labelManager= new TextManager(label);
            }
		}


		@Override
		public BufferedImage getLegend() {

			// TODO OPACITY IS MISSING, I would add something an hatch to show that an element is transparent
			
			////
			//
			// creating a backbuffer image on which we should draw the bkgColor for this colormap element
			//
			////
			final int width=(int) requestedDimension.getWidth();
			final int height=(int) requestedDimension.getHeight();
            final BufferedImage image = ImageUtils.createImage(width, height, (IndexColorModel)null, transparent);
            final Map<Key, Object> hintsMap = new HashMap<Key, Object>();
            final Graphics2D graphics = ImageUtils.prepareTransparency(transparent, backgroundColor, image, hintsMap);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            ////
            //
            // prepare the padding and set up the dimensions
            //
            ////
            final float hpad = (width * LegendUtils.hpaddingFactor);
            final float vpad = (height * LegendUtils.vpaddingFactor);
            final float wLegend = width - (2 * hpad);
            final float hLegend = height - (2 * vpad);
            //rectangle for the legend
            final Rectangle2D.Double rectLegend= new Rectangle2D.Double(hpad,vpad,wLegend,hLegend);
            colorManager.draw(graphics, rectLegend,true);
            
 	       
	        ////
	        //
            // DRAW the rule 
	        //
	        //
	        ////
            //this is a traditional 'regular-old' label.  Just figure the
	        //size and act accordingly.
            Dimension dimensions = ruleManager.getPreferredDimension(graphics);
	        graphics.dispose();
	        //now draw
	        final BufferedImage renderedRule = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_ARGB);	
	        Graphics2D rlg = renderedRule.createGraphics();
	        ruleManager.draw(rlg, new Rectangle(0,0,renderedRule.getWidth(),renderedRule.getHeight()),false);
	        rlg.dispose(); 
	        
	        ////
	        //
            // DRAW the label 
	        //
	        //
	        ////
            //this is a traditional 'regular-old' label.  Just figure the
	        //size and act accordingly.
            dimensions = labelManager.getPreferredDimension(graphics);
	        graphics.dispose();
	        //now draw
	        final BufferedImage renderedLabel = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_ARGB);	
	        rlg = renderedLabel.createGraphics();
	        labelManager.draw(rlg, new Rectangle(0,0,renderedLabel.getWidth(),renderedLabel.getHeight()),false);
	        rlg.dispose();   
            
            
	        ////
	        //
            // merge
	        //
	        ////
            return LegendUtils.mergeBufferedImages(image, hintsMap, graphics,renderedRule,renderedLabel, transparent,backgroundColor,true);
		}


		@Override
		public boolean hasLabel() {
			return hasLabel;
		}
		
	}
	 class RampColorMapEntryLegendBuilder extends ColorMapEntryLegendBuilder{

		private boolean leftEdge;
		private double quantity;
		public RampColorMapEntryLegendBuilder(List<ColorMapEntry> mapEntries) {
			super(mapEntries);
			
			final ColorMapEntry previousCME = mapEntries.get(0);
			final ColorMapEntry currentCME = mapEntries.get(1);
			if(previousCME==null)
				this.leftEdge=true;
			else
				this.leftEdge=false;
		
			Color previousColor;
			if(!leftEdge){
				previousColor=LegendUtils.color(previousCME);
				final double opacity=LegendUtils.getOpacity(previousCME);
				previousColor=new Color(previousColor.getRed(),previousColor.getGreen(),previousColor.getBlue(),(int) (255*opacity+0.5));
			}
			else
				previousColor=null;
			
			Color color = LegendUtils.color(currentCME);
			double opacity = LegendUtils.getOpacity(currentCME);	
			color=new Color(color.getRed(),color.getGreen(),color.getBlue(),(int) (255*opacity));
			this.colorManager= new GradientColorManager(color,opacity,previousColor);
			
			
			String label = currentCME.getLabel();
			this.quantity=LegendUtils.getQuantity(currentCME);
			
			String symbol = leftEdge?" > ":" = "; 
            String rule = leftEdge?
            		Double.toString(quantity)+" "+symbol+" x":
            			Double.toString(quantity)+" "+symbol+" x";
            		

            this.ruleManager= new TextManager(rule);
            		
            // add the label the label to the rule so that we draw all text just once 
            if(label!=null)
            {
            	
            	hasLabel=true;
            	this.labelManager= new TextManager(label);
            }
    		
		}

		/**
		 * 
		 */
		@Override
		public BufferedImage getLegend() {

	        ////
	        //
            // Preparation
	        //
	        //
	        ////
			// getting the requested dimensions
			final int requestedWidth=(int) requestedDimension.getWidth();
			final int requestedHeight=(int) requestedDimension.getHeight();
			
			//fake image from which we can get the font characteristics
            BufferedImage image = ImageUtils.createImage(1, 1, (IndexColorModel)null, transparent);
            final Map<Key, Object> hintsMap = new HashMap<Key, Object>();
            Graphics2D graphics = ImageUtils.prepareTransparency(transparent, backgroundColor, image, hintsMap);
           
 	       
	        ////
	        //
            // DRAW the rule 
	        //
	        //
	        ////
            //this is a traditional 'regular-old' label.  Just figure the
	        //size and act accordingly.
            Dimension dimensions = ruleManager.getPreferredDimension(graphics);
	        graphics.dispose();
	        //now draw
	        final BufferedImage renderedRule = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_ARGB);	
	        Graphics2D rlg = renderedRule.createGraphics();
	        ruleManager.draw(rlg, new Rectangle(0,0,renderedRule.getWidth(),renderedRule.getHeight()),false);
	        rlg.dispose(); 
	        
	        ////
	        //
            // DRAW the label 
	        //
	        //
	        ////
            //this is a traditional 'regular-old' label.  Just figure the
	        //size and act accordingly.
            dimensions = labelManager.getPreferredDimension(graphics);
	        graphics.dispose();
	        //now draw
	        final BufferedImage renderedLabel = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_ARGB);	
	        rlg = renderedLabel.createGraphics();
	        labelManager.draw(rlg, new Rectangle(0,0,renderedLabel.getWidth(),renderedLabel.getHeight()),false);
	        rlg.dispose();   
            
            
	        ////
	        //
	        // DRAW the bkgColor label
	        //
	        //
	        ////
            // prepare the padding and set up the dimensions
            //we only have some horizontal padding since we want the various elements to touch each other
            final float hpad = (requestedWidth * LegendUtils.hpaddingFactor);
            //give it some horizontal padding to separate the other labels 
            final int wLegend =(int)( requestedWidth - (2 * hpad)+0.5);
            final int hLegend = (int)(Math.max(!leftEdge?dimensions.height*2:dimensions.height,requestedHeight)+.5);
            
	        //notice that if are at the left edge, we should not draw anything 
            image = ImageUtils.createImage(requestedWidth, hLegend, (IndexColorModel)null, transparent);
            graphics = ImageUtils.prepareTransparency(transparent, backgroundColor, image, hintsMap);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);            
            
            colorManager.draw(graphics, new Rectangle2D.Double(0,0,wLegend,hLegend), false);
            
	        ////
	        //
            // merge
	        //
	        ////
            return LegendUtils.mergeBufferedImages(image, hintsMap, graphics,renderedRule,renderedLabel, transparent,backgroundColor,false);
		}

		@Override
		public boolean hasLabel() {
			return hasLabel;
		}
		
	}
	 
	 class ClassesEntryLegendBuilder extends ColorMapEntryLegendBuilder{

			private double quantity1;
			private boolean leftEdge;
			private double quantity2;
			public ClassesEntryLegendBuilder(List<ColorMapEntry> mapEntries) {
				super(mapEntries);
				final ColorMapEntry previousCME = mapEntries.get(0);
				final ColorMapEntry currentCME = mapEntries.get(1);
				if(previousCME==null)
					this.leftEdge=true;
				else
					this.leftEdge=false;
			
				Color color = LegendUtils.color(currentCME);
				final double opacity = LegendUtils.getOpacity(currentCME);
				color=new Color(color.getRed(),color.getGreen(),color.getBlue(),(int) (255*opacity));
				this.colorManager= new SimpleColorManager(color,opacity);


				
				String label =currentCME.getLabel();
				this.quantity1=leftEdge?LegendUtils.getQuantity(currentCME):LegendUtils.getQuantity(previousCME);
				this.quantity2=LegendUtils.getQuantity(currentCME);
	            String symbol1=null,symbol2=null;
        		if(leftEdge)
        			symbol1=" < ";
        		else
        		{
        			symbol1=" <= "; 
        			symbol2=" < "; 
        		}
	            String rule=leftEdge?
	            		Double.toString(quantity1)+" "+symbol1+" x":
	            			Double.toString(quantity1)+" "+symbol1+" x "+symbol2+" "+ Double.toString(quantity2)	;

	            this.ruleManager= new TextManager(rule);
	            		
	            // add the label the label to the rule so that we draw all text just once 
	            if(label!=null)
	            {
	            	
	            	hasLabel=true;
	            	this.labelManager= new TextManager(label);
	            }
	            
			}

			@Override
			public BufferedImage getLegend() {
				
				////
				//
				// creating a backbuffer image on which we should draw the bkgColor for this colormap element
				//
				////
				final int width=(int) requestedDimension.getWidth();
				final int height=(int) requestedDimension.getHeight();
	            final BufferedImage image = ImageUtils.createImage(width, height, (IndexColorModel)null, transparent);
	            final Map<Key, Object> hintsMap = new HashMap<Key, Object>();
	            final Graphics2D graphics = ImageUtils.prepareTransparency(transparent, backgroundColor, image, hintsMap);
	            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	            
	            ////
	            //
	            // prepare the padding and set up the dimensions
	            //
	            ////
	            final float hpad = (width * LegendUtils.hpaddingFactor);
	            final float vpad = (height * LegendUtils.vpaddingFactor);
	            final float wLegend = width - (2 * hpad);
	            final float hLegend = height - (2 * vpad);
	            //rectangle for the legend
	            final Rectangle2D.Double rectLegend= new Rectangle2D.Double(hpad,vpad,wLegend,hLegend);
	            colorManager.draw(graphics, rectLegend,true);
	       
		        ////
		        //
	            // DRAW the rule 
		        //
		        //
		        ////
	            //this is a traditional 'regular-old' label.  Just figure the
		        //size and act accordingly.
	            Dimension dimensions = ruleManager.getPreferredDimension(graphics);
		        graphics.dispose();
		        //now draw
		        final BufferedImage renderedRule = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_ARGB);	
		        Graphics2D rlg = renderedRule.createGraphics();
		        ruleManager.draw(rlg, new Rectangle(0,0,renderedRule.getWidth(),renderedRule.getHeight()),false);
		        rlg.dispose(); 
		        
		        ////
		        //
	            // DRAW the label 
		        //
		        //
		        ////
	            //this is a traditional 'regular-old' label.  Just figure the
		        //size and act accordingly.
	            dimensions = labelManager.getPreferredDimension(graphics);
		        graphics.dispose();
		        //now draw
		        final BufferedImage renderedLabel = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_ARGB);	
		        rlg = renderedLabel.createGraphics();
		        labelManager.draw(rlg, new Rectangle(0,0,renderedLabel.getWidth(),renderedLabel.getHeight()),false);
		        rlg.dispose();    
	            
	            
		        ////
		        //
	            // merge
		        //
		        ////
	            return LegendUtils.mergeBufferedImages(image, hintsMap, graphics,
	            		renderedRule,renderedLabel,transparent,backgroundColor,true);
			}



			
			@Override
			public boolean hasLabel() {
				return hasLabel;
			}
			
		}	 
	
	private ColorMapType colorMapType;
	
	private boolean extended=false;

	private boolean transparent;

	private Dimension requestedDimension;

	private Map<String,List<String>> additionalOptions;

	private Color backgroundColor;

	private Font labelFont;

	private Color labelFontColor;

	private ColorMapEntry previousCMapEntry;

	private final Queue<ColorMapEntryLegendBuilder> colorMapEntryLegendBuilders= new LinkedList<ColorMapEntryLegendBuilder>();

	private final List<Cell> cells  = new ArrayList<Cell>();
	
	

	/**
	 * @return
	 * @uml.property  name="colorMapType"
	 */
	public ColorMapType getColorMapType() {
		return colorMapType;
	}

	/**
	 * @return
	 * @uml.property  name="extended"
	 */
	public boolean isExtended() {
		return extended;
	}

	/**
	 * @param extended
	 * @uml.property  name="extended"
	 */
	public void setExtended(boolean extended) {
		this.extended = extended;
	}

	public void setColorMapType(int type) {
		this.colorMapType=ColorMapType.create(type);
		
	}



	public void addColorMapEntry(final ColorMapEntry cEntry) {
		
		
		//build a ColorMapEntryLegendBuilder for the specified colorMapEntry
		final ColorMapEntryLegendBuilder element;
		switch(colorMapType){
			case UNIQUE_VALUES:
				element=new SingleColorMapEntryLegendBuilder(Arrays.asList(cEntry));
				break;
			case RAMP:
				element=new RampColorMapEntryLegendBuilder(Arrays.asList(previousCMapEntry,cEntry));	
				break;
			case CLASSES:
				element=new ClassesEntryLegendBuilder(Arrays.asList(previousCMapEntry,cEntry));
				break;
				default:
					throw new IllegalArgumentException("Unrecognized colormap type");

		}
		
		//add to the table, we can use matrix algebra knowning that W==3 for this matrix
    	colorMapEntryLegendBuilders.add(element);	//SHOULD BE REMOVED
    	cells.add(element.getColorManager());
    	cells.add(element.getRuleManager());
    	cells.add(element.getLabelManager());
    	
   	
		//set last used element
		previousCMapEntry=cEntry;			
			
		
	}

	/**
	 * @param dimension
	 * @uml.property  name="requestedDimension"
	 */
	public void setRequestedDimension(Dimension dimension) {
		this.requestedDimension=(Dimension) dimension.clone();
		
	}

	/**
	 * @param transparent
	 * @uml.property  name="transparent"
	 */
	public void setTransparent(boolean transparent) {
		this.transparent=transparent;
		
	}



	public BufferedImage getLegend() {
		
		//now build the individuals legends
		final Dimension finalDimension= new Dimension();
		final Queue<BufferedImage> legendsQueue=createLegends(finalDimension);
		
		//now merge them
		return mergeLegends(legendsQueue,finalDimension);
	}

	private Queue<BufferedImage> createLegends(
			final Dimension finalDimension) {
		
		//
		// create a sample image for computing dimensions
		//
        BufferedImage image = ImageUtils.createImage(1, 1, (IndexColorModel)null, transparent);
        final Map<Key, Object> hintsMap = new HashMap<Key, Object>();
        Graphics2D graphics = ImageUtils.prepareTransparency(transparent, backgroundColor, image, hintsMap);
        
        double rowH=Double.NEGATIVE_INFINITY;
        double colorW=Double.NEGATIVE_INFINITY;
        double ruleW=Double.NEGATIVE_INFINITY;
        double labelW=Double.NEGATIVE_INFINITY;
        //
        // compute dimensions
        //
		final Queue<BufferedImage> queue= new LinkedList<BufferedImage>();
		final int numRows=this.cells.size()/3;
		for(int i=0;i<numRows;i++){
			
			//
			//row number i
			//
			
			// color element
			final Cell cm= this.cells.get(i);
			final Dimension colorDim=cm.getPreferredDimension(graphics);
			rowH=Math.max(rowH, colorDim.getHeight());
			colorW=Math.max(colorW, colorDim.getWidth());
			
			// rule
			final Cell ruleM= this.cells.get(i);
			final Dimension ruleDim=ruleM.getPreferredDimension(graphics);
			rowH=Math.max(rowH, ruleDim.getHeight());
			ruleW=Math.max(ruleW, ruleDim.getWidth());
			
			
			// label
			final Cell labelM= this.cells.get(i);
			if(labelM==null)
				continue;
			final Dimension labelDim=labelM.getPreferredDimension(graphics);
			rowH=Math.max(rowH, labelDim.getHeight());
			labelW=Math.max(labelW, labelDim.getWidth());
		}
		
		
		
		
		
		for(ColorMapEntryLegendBuilder legendBuilder:this.colorMapEntryLegendBuilders)
		{
			//get the legend
			final BufferedImage legend= legendBuilder.getLegend();
			queue.add(legend);
			
			//set width and height of the final legend by using the larger width and by summing the new height
			final int currentWidth=(int) finalDimension.getWidth();
			finalDimension.setSize(currentWidth>legend.getWidth()?currentWidth:legend.getWidth(), finalDimension.getHeight()+legend.getHeight());
		}
		
		//return the list of legends
		return queue;
	}

	private BufferedImage mergeLegends(Queue<BufferedImage> legendsQueue, Dimension finalDimension) {
         final Map<Key, Object> hintsMap = new HashMap<Key, Object>();
         //create the final image
         
         // I am doing a straight cast since I know that I built this
			// dimension object by using the widths and heights of the various
			// bufferedimages for the various bkgColor map entries.
         final int totalWidth=(int) finalDimension.getWidth();
		 final int totalHeight=(int) finalDimension.getHeight();
		 final BufferedImage finalLegend = ImageUtils.createImage(totalWidth, totalHeight, (IndexColorModel)null, transparent);
         Graphics2D finalGraphics = ImageUtils.prepareTransparency(transparent, backgroundColor, finalLegend, hintsMap);
         hintsMap.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
         finalGraphics.setRenderingHints(hintsMap);

         int topOfRow = 0;
         final int size=legendsQueue.size();
         for (int i = 0; i < size; i++) {
			final BufferedImage img = legendsQueue.remove();

			// draw the image
			finalGraphics.drawImage(img, 0, topOfRow, null);
			topOfRow += img.getHeight();
		}
         
         finalGraphics.dispose();
         return finalLegend;
	}

	/**
	 * @param legendOptions
	 * @uml.property  name="additionalOptions"
	 */
	public void setAdditionalOptions(Map<String,List<String>> legendOptions) {
		this.additionalOptions= new HashMap<String,List<String>>(legendOptions);
		
	}

	/**
	 * @return
	 * @uml.property  name="requestedDimension"
	 */
	public Object getRequestedDimension() {
		return requestedDimension;
	}

	/**
	 * @return
	 * @uml.property  name="transparent"
	 */
	public boolean isTransparent() {
		return transparent;
	}

	/**
	 * @return
	 * @uml.property  name="additionalOptions"
	 */
	public Map<String, List<String>> getAdditionalOptions() {
		return Collections.unmodifiableMap(additionalOptions);
	}

	/**
	 * @param colorMapType
	 * @uml.property  name="colorMapType"
	 */
	public void setColorMapType(ColorMapType colorMapType) {
		this.colorMapType = colorMapType;
	}

	/**
	 * @return
	 * @uml.property  name="backgroundColor"
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * @param backGroundColor
	 * @uml.property  name="backgroundColor"
	 */
	public void setBackgroundColor(Color backGroundColor) {
		this.backgroundColor = backGroundColor;
	}

	/**
	 * @param labelFont
	 * @uml.property  name="labelFont"
	 */
	public void setLabelFont(Font labelFont) {
		this.labelFont=labelFont;
		
	}

	/**
	 * @param labelFontColor
	 * @uml.property  name="labelFontColor"
	 */
	public void setLabelFontColor(Color labelFontColor) {
		this.labelFontColor=labelFontColor;
		
	}


}

