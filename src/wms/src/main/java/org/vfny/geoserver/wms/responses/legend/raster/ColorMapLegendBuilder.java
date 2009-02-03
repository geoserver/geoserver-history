package org.vfny.geoserver.wms.responses.legend.raster;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
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
	
	abstract class ColorManager{
		
		protected final Color color;
		protected final double opacity;

		public ColorManager(final Color color, final double opacity) {
			this.opacity=opacity;
			this.color = color;
		}
		
		
		public abstract void draw(final Graphics2D graphics,final Rectangle2D clipBox, final boolean completeBorder);

	}
	
	class SimpleColorManager extends ColorManager{

		public SimpleColorManager(Color color, double opacity) {
			super(color, opacity);
		}

		@Override
		public void draw(final Graphics2D graphics, final Rectangle2D clipBox, final boolean completeBorder) {
			//color fill
            if(opacity>0){
            	// OPAQUE
	            final Color oldColor=graphics.getColor();
	            final Color newColor= new Color(color.getRed(),color.getGreen(),color.getBlue(),(int) (255*opacity+0.5));
	            graphics.setColor(newColor);
	            graphics.fill(clipBox);
	            //make color customizable
	            graphics.setColor(Color.BLACK);
	            if(!completeBorder)
	            {
		            
		            final int minx=(int) (clipBox.getMinX()+0.5);
		            final int miny=(int) (clipBox.getMinY()+0.5);
		            final int maxx=(int) (minx+clipBox.getWidth()+0.5);
		            final int maxy=(int)(miny+clipBox.getHeight()+0.5);	            	
	            	graphics.drawLine(minx,maxy,maxx,maxy);
	            }
	            else
	            	graphics.draw(clipBox);
	            //restore color            
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

	            //restore color            
	            graphics.setColor(oldColor);
            }
			
		}
		
	}
	
	class GradientColorManager extends ColorManager{

		public GradientColorManager(Color color, double opacity) {
			super(color, opacity);
		}

		@Override
		public void draw(final Graphics2D graphics, final Rectangle2D clipBox, final boolean completeBorder) {
			//color fill
            if(opacity>0){
            	// OPAQUE
	            final Color oldColor=graphics.getColor();
	            final Color newColor= new Color(color.getRed(),color.getGreen(),color.getBlue(),(int) (255*opacity+0.5));
	            graphics.setColor(newColor);
	            graphics.fill(clipBox);
	            //make color customizable
	            graphics.setColor(Color.BLACK);
	            if(!completeBorder)
	            {
		            
		            final int minx=(int) (clipBox.getMinX()+0.5);
		            final int miny=(int) (clipBox.getMinY()+0.5);
		            final int maxx=(int) (minx+clipBox.getWidth()+0.5);
		            final int maxy=(int)(miny+clipBox.getHeight()+0.5);	            	
	            	graphics.drawLine(minx,maxy,maxx,maxy);
	            }
	            else
	            	graphics.draw(clipBox);
	            //restore color            
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

	            //restore color            
	            graphics.setColor(oldColor);
            }
			
		}
		
	}	
	
	/**
	 * 
	 * @author Simone Giannecchini, GeoSolutions SAS
	 *
	 */
	class TextManager{

		private final String text;
		private Dimension dimension;
		

		public TextManager(String text) {
			this.text = text;
		}
		
		public Dimension computeDimension(final Graphics2D graphics){
			if(dimension==null)
			{
				final Font oldFont=graphics.getFont();
	            graphics.setFont(labelFont);
	            //computing label dimension and creating  buffered image on which we can draw the label on it
		        final int labelHeight = (int) Math.ceil(graphics.getFontMetrics().getStringBounds(text, graphics).getHeight());
		        final int labelWidth = (int) Math.ceil(graphics.getFontMetrics().getStringBounds(text, graphics).getWidth());
		        graphics.dispose();
		        //restore the old font
		        graphics.setFont(oldFont);	
		        dimension=new Dimension(labelWidth,labelHeight);
	        }
	        return dimension;
		}
		
		public void draw(final Graphics2D graphics,final Rectangle2D textBox){
			//get old font
			final Font oldFont=graphics.getFont();
			//set font and font color
			graphics.setColor(labelFontColor);
			graphics.setFont(labelFont);
			graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			//Halign==center vAlign==bottom
			final int xText=(int)(textBox.getMinX()+(textBox.getWidth()-dimension.getWidth())/2.0+0.5);
			final int yText=(int)(textBox.getMinY()+graphics.getFontMetrics().getAscent()+0.5) ;
			graphics.drawString(text, xText,yText); 
	        //restore the old font
	        graphics.setFont(oldFont);
			
		}
		
		
	}
	 abstract class ColorMapEntryLegendBuilder {		
		
		public abstract boolean isHasLabel();

		protected final ArrayList<ColorMapEntry> colorMapEntriesSubset = new ArrayList<ColorMapEntry>();

		public ColorMapEntryLegendBuilder(final List<ColorMapEntry> mapEntries) {
			colorMapEntriesSubset.addAll(mapEntries);
		}

		public abstract BufferedImage getLegend() ;
		
	}
	 class SingleColorMapEntryLegendBuilder extends ColorMapEntryLegendBuilder{
		
		private boolean hasLabel;
		private TextManager labelManager;
		private SimpleColorManager colorManager;

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
            // add the label the label to the rule so that we draw all text just once 
            if(label!=null)
            {
            	rule+=" "+label;
            	hasLabel=true;
            }
            this.labelManager= new TextManager(rule);
		}


		@Override
		public BufferedImage getLegend() {

			// TODO OPACITY IS MISSING, I would add something an hatch to show that an element is transparent
			
			////
			//
			// creating a backbuffer image on which we should draw the color for this colormap element
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
            // DRAW the text 
	        //
	        //
	        ////
            //this is a traditional 'regular-old' label.  Just figure the
	        //size and act accordingly.
            final Dimension dimensions = labelManager.computeDimension(graphics);
	        graphics.dispose();
	        //now draw
	        final BufferedImage renderedLabel = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_ARGB);	
	        final Graphics2D rlg = renderedLabel.createGraphics();
	        labelManager.draw(rlg, new Rectangle(0,0,renderedLabel.getWidth(),renderedLabel.getHeight()));
	        rlg.dispose(); 
            
            
	        ////
	        //
            // merge
	        //
	        ////
            return LegendUtils.mergeBufferedImages(image, hintsMap, graphics,
					renderedLabel,transparent,backgroundColor,true);
		}


		@Override
		public boolean isHasLabel() {
			return hasLabel;
		}
		
	}
	 class RampColorMapEntryLegendBuilder extends ColorMapEntryLegendBuilder{

		private Color previousColor;
		private boolean leftEdge;
		private double opacity;
		private double quantity;
		private Color color;
		private TextManager labelManager;
		private boolean hasLabel;

		public RampColorMapEntryLegendBuilder(List<ColorMapEntry> mapEntries) {
			super(mapEntries);
			
			final ColorMapEntry previousCME = mapEntries.get(0);
			final ColorMapEntry currentCME = mapEntries.get(1);
			if(previousCME==null)
				this.leftEdge=true;
			else
				this.leftEdge=false;
		
			if(!leftEdge){
				this.previousColor=LegendUtils.color(previousCME);
				final double opacity=LegendUtils.getOpacity(previousCME);
				this.previousColor=new Color(previousColor.getRed(),previousColor.getGreen(),previousColor.getBlue(),(int) (255*opacity+0.5));
			}
			else
				previousColor=null;
			
			this.color=LegendUtils.color(currentCME);
			this.opacity=LegendUtils.getOpacity(currentCME);	
			this.color=new Color(color.getRed(),color.getGreen(),color.getBlue(),(int) (255*opacity));
			
			
			
			String label = currentCME.getLabel();
			this.quantity=LegendUtils.getQuantity(currentCME);
			
			String symbol = leftEdge?" > ":" = "; 
            String rule = leftEdge?
            		Double.toString(quantity)+" "+symbol+" x":
            			Double.toString(quantity)+" "+symbol+" x";
            		
            // add the label the label to the rule so that we draw all text just once 
            if(label!=null)
            {
            	rule+=" "+label;
            	hasLabel=true;
            }
            this.labelManager= new TextManager(rule);
    		
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
            // DRAW the text 
	        //
	        //
	        ////
            //this is a traditional 'regular-old' label.  Just figure the
	        //size and act accordingly.
            final Dimension dimensions = labelManager.computeDimension(graphics);
	        graphics.dispose();
	        //now draw
	        final BufferedImage renderedLabel = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_ARGB);	
	        final Graphics2D rlg = renderedLabel.createGraphics();
	        labelManager.draw(rlg, new Rectangle(0,0,renderedLabel.getWidth(),renderedLabel.getHeight()));
	        rlg.dispose();     
	        
	       
            
            
	        ////
	        //
	        // DRAW the color label
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
            
            // GRADIENT
            if(!leftEdge){
	            //rectangle for the gradient
	            final Rectangle2D.Double rectLegend= new Rectangle2D.Double(hpad,0,wLegend,hLegend/2.0);
	            
	            //gradient paint
	            final Paint oldPaint=graphics.getPaint();
	            final GradientPaint paint= new GradientPaint(0,0,previousColor,0,(int)(hLegend/2.0),color);
	            graphics.setPaint(paint);
	            graphics.fill(rectLegend);
	            
	            //restore paint            
	            graphics.setPaint(oldPaint);
	            
            }
           
            ////
            //
            // prepare the padding and set up the dimensions
            //
            ////
            //rectangle for the legend
            final Rectangle2D rectLegend  = new Rectangle2D.Double(hpad,!leftEdge?(int)(hLegend/2.0):0,wLegend,!leftEdge?(int)(hLegend/2.0):hLegend);
            
            //color fill
            if(opacity>0){
            	// OPAQUE
	            final Color oldColor=graphics.getColor();
	            final Color newColor= new Color(color.getRed(),color.getGreen(),color.getBlue(),(int) (255*opacity+0.5));
	            graphics.setColor(newColor);
	            graphics.fill(rectLegend);
	            //make color customizable
	            graphics.setColor(Color.BLACK);
	            final int minx=(int) (rectLegend.getMinX()+0.5);
	            final int miny=(int) (rectLegend.getMinY()+0.5);
	            final int maxx=(int) (minx+rectLegend.getWidth()+0.5)-1;
	            final int maxy=(int)(miny+rectLegend.getHeight()+0.5)-1;
	            graphics.drawLine(minx,maxy,maxx,maxy);
	            //restore color            
	            graphics.setColor(oldColor);
            }
            else
            {
            	// TRANSPARENT
	            final Color oldColor=graphics.getColor();
	            
	            //white background
	            graphics.setColor(Color.white);
	            graphics.fill(rectLegend);
	            
	            //now the red cross
	            graphics.setColor(Color.RED);
	            final int minx=(int) (rectLegend.getMinX()+0.5);
	            final int miny=(int) (rectLegend.getMinY()+0.5);
	            final int maxx=(int) (minx+rectLegend.getWidth()+0.5)-1;
	            final int maxy=(int)(miny+rectLegend.getHeight()+0.5)-1;
	            graphics.drawLine(minx,miny,maxx,maxy);
	            graphics.drawLine(minx,maxy,maxx,miny);
	            
	            graphics.setColor(Color.BLACK);
	            graphics.drawLine(minx,maxy,maxx,maxy);

	            //restore color            
	            graphics.setColor(oldColor);
            }
            
            
	        ////
	        //
            // merge
	        //
	        ////
            return LegendUtils.mergeBufferedImages(image, hintsMap, graphics,
					renderedLabel,transparent,backgroundColor,false);
		}

		@Override
		public boolean isHasLabel() {
			return hasLabel;
		}
		
	}
	 
	 class ClassesEntryLegendBuilder extends ColorMapEntryLegendBuilder{

			private double quantity1;
			private boolean leftEdge;
			private double quantity2;
			private boolean hasLabel;
			private TextManager labelManager;
			private SimpleColorManager colorManager;
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

	            		
	            // add the label the label to the rule so that we draw all text just once 
	            if(label!=null)
	            {
	            	rule+=" "+label;
	            	hasLabel=true;
	            }
	            this.labelManager= new TextManager(rule);
			}

			@Override
			public BufferedImage getLegend() {
				// TODO OPACITY IS MISSING, I would add something an hatch to show that an element is transparent
				
				////
				//
				// creating a backbuffer image on which we should draw the color for this colormap element
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
	            // DRAW the text 
		        //
		        //
		        ////
	            //this is a traditional 'regular-old' label.  Just figure the
		        //size and act accordingly.
	            final Dimension dimensions = labelManager.computeDimension(graphics);
		        graphics.dispose();
		        //now draw
		        final BufferedImage renderedLabel = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_ARGB);	
		        final Graphics2D rlg = renderedLabel.createGraphics();
		        labelManager.draw(rlg, new Rectangle(0,0,renderedLabel.getWidth(),renderedLabel.getHeight()));
		        rlg.dispose();    
	            
	            
		        ////
		        //
	            // merge
		        //
		        ////
	            return LegendUtils.mergeBufferedImages(image, hintsMap, graphics,
						renderedLabel,transparent,backgroundColor,true);
			}



			
			@Override
			public boolean isHasLabel() {
				return hasLabel;
			}
			
		}	 
	
	private ColorMapType colorMapType;
	
	private boolean extended=false;

	private boolean transparent;

	private Dimension requestedDimension;

	private Map additionalOptions;

	private Color backgroundColor;

	private Font labelFont;

	private Color labelFontColor;
	
	private boolean needsRules=false;
	
	private Dimension maxRuleDim=new Dimension(0,0);
	
	private Dimension maxLabelDim=new Dimension(0,0);

	private ColorMapEntry previousCMapEntry;

	private final Queue<ColorMapEntryLegendBuilder> colorMapEntryLegendBuilders= new LinkedList<ColorMapEntryLegendBuilder>();
	
	

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
		//build a ColorMapEntryLegendBuilder

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
    	colorMapEntryLegendBuilders.add(element);				
		//set last element
		previousCMapEntry=cEntry;			

		
		//
		// parse the relevant elements for the single elements in order to evaluate dimensions 
		//
			
		
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
		final Queue<BufferedImage> legendsQueue=createLegends(this.colorMapEntryLegendBuilders,finalDimension);
		
		//now merge them
		return mergeLegends(legendsQueue,finalDimension);
	}

	private Queue<BufferedImage> createLegends(
			Queue<ColorMapEntryLegendBuilder> cmeBuilders, final Dimension finalDimension) {
		final Queue<BufferedImage> queue= new LinkedList<BufferedImage>();
		
		for(ColorMapEntryLegendBuilder legendBuilder:cmeBuilders)
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
			// bufferedimages for the various color map entries.
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

//	
//	private Queue<ColorMapEntryLegendBuilder> parseColorMapEntries() {
//
//		
//		// parsing the various color map entries in order to build a queue of
//		// color map entry legend builders which are responsible for building
//		// the individual legends for each color map entry.
//
//		final int size=colorMapEntries.size();
//		//create a queue of ColorMapEntryLegendBuilder which will build single piece of the overall legend
//		final Queue<ColorMapEntryLegendBuilder> queue= new LinkedList<ColorMapEntryLegendBuilder>();
//		ColorMapEntry previousCMapEntry=null;
//		for(int i=0;i<size;i++)
//		{
//			final ColorMapEntry cEntry=colorMapEntries.get(i);
//			final ColorMapEntryLegendBuilder element;
//			switch(colorMapType){
//				case UNIQUE_VALUES:
//					element=new SingleColorMapEntryLegendBuilder(Arrays.asList(cEntry));
//					break;
//				case RAMP:
//					element=new RampColorMapEntryLegendBuilder(Arrays.asList(previousCMapEntry,cEntry));	
//					break;
//				case CLASSES:
//					element=new ClassesEntryLegendBuilder(Arrays.asList(previousCMapEntry,cEntry));
//					break;
//					default:
//						throw new IllegalArgumentException("Unrecognized colormap type");
//
//			}
//			
//
//			queue.add(element);				
//			//set last element
//			previousCMapEntry=cEntry;
//		}
//		
//		return queue;
//	}

	/**
	 * @param legendOptions
	 * @uml.property  name="additionalOptions"
	 */
	public void setAdditionalOptions(Map legendOptions) {
		this.additionalOptions= new HashMap(legendOptions);
		
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
	public Map getAdditionalOptions() {
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

