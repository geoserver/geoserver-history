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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;

import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.SelectedChannelType;
import org.vfny.geoserver.wms.responses.ImageUtils;
import org.vfny.geoserver.wms.responses.LegendUtils;
import org.vfny.geoserver.wms.responses.LegendUtils.HAlign;
import org.vfny.geoserver.wms.responses.LegendUtils.VAlign;


/**
 * @author  Simone Giannecchini, GeoSolutions.
 */
@SuppressWarnings("deprecation")
class ColorMapLegendBuilder {
	
	/**
	 * 
	 * @author Simone Giannecchini, GeoSolutions SAS
	 *
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
	
	abstract class Row{
		private final List<Cell> columns= new ArrayList<Cell>();
		
		Row(){
		}
		Row(final List<Cell> columns){
			columns.addAll(columns);
		}
		
		protected Cell get(final int index){
			return columns.get(index);
		}
		
		protected void add(final Cell cell){
			columns.add(cell);
		}
	}
	abstract class Cell{
	
		protected final Color bkgColor;
		protected final double bkgOpacity;
		protected final String text;
		protected final VAlign textVAlign;
		protected final HAlign textHAlign;

		public Cell(final Color bkgColor, final double bkgOpacity, final String text, final VAlign vAlign, final HAlign hAlign) {
			this.bkgOpacity=bkgOpacity;
			this.bkgColor = bkgColor;
			this.text=text;
			this.textVAlign=vAlign;
			this.textHAlign=hAlign;
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
			super(color, opacity, null,null,null);
		}
		
		
		public abstract void draw(final Graphics2D graphics,final Rectangle2D clipBox, final boolean completeBorder);
		
		@Override
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
		            
//		            final int minx=(int) (clipBox.getMinX()+0.5);
//		            final int miny=(int) (clipBox.getMinY()+0.5);
//		            final int maxx=(int) (minx+clipBox.getWidth()+0.5)-1;
//		            final int maxy=(int)(miny+clipBox.getHeight()+0.5)-1;	            	
//	            	graphics.drawLine(minx,maxy,maxx,maxy);
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
	            final int maxx=(int) (minx+clipBox.getWidth()-1+0.5);
	            final int maxy=(int)(miny+clipBox.getHeight()-1+0.5);
	            graphics.drawLine(minx,miny,maxx,maxy);
	            graphics.drawLine(minx,maxy,maxx,miny);
	            
	            graphics.setColor(Color.BLACK);
	            if(!completeBorder)
	            {
		            
//        	
//	            	graphics.drawLine(minx,maxy,maxx,maxy);
	            }
	            else
	            {

		            final int w=(int) (clipBox.getWidth()+0.5)-1;
		            final int h=(int)(clipBox.getHeight()+0.5)-1;	   
	            	graphics.draw(new Rectangle2D.Double(minx,miny,w,h));
	            }

	            //restore bkgColor            
	            graphics.setColor(oldColor);
            }
			
		}
		
	}
	
	class GradientColorManager extends SimpleColorManager{

		@Override
		public Dimension getPreferredDimension(Graphics2D graphics) {
			//twice as much space for the Height to account for the gradient
			return new Dimension(requestedDimension.width,(int) (1.5*requestedDimension.height+0.5));
		}

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
	            		h/2);
	            
	            //gradient paint
	            final Paint oldPaint=graphics.getPaint();
	            final GradientPaint paint=new GradientPaint(
	            		(float)minx,(float)miny,previousColor,
	            		(float)minx,(float)(miny+h/2),bkgColor);
	            
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
            		miny+(leftEdge?0:h/2),
            		w,
            		!leftEdge?h/2:h);
            super.draw(graphics, rectLegend, completeBorder);
            
            
			
		}
		
	}	
	
	/**
	 * 
	 * @author Simone Giannecchini, GeoSolutions SAS
	 *
	 */
	class TextManager extends Cell{
	

		public TextManager(final String text, final VAlign vAlign, final HAlign hAlign) {
			super(backgroundColor, 1.0, text,vAlign,hAlign);
		}
		
		@Override
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
			if(fontAntiAliasing)
				graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			
			//Halign==center vAlign==bottom
            final double minx=clipBox.getMinX();
            final double miny=clipBox.getMinY();
            final double w=clipBox.getWidth();
            final double h=clipBox.getHeight();
            final Dimension dimension=getPreferredDimension(graphics);
            //where do we draw?
			final int xText;
			switch (hAlign) {
			case CENTERED:
				xText=(int)(minx+(w-dimension.getWidth())/2.0+0.5);
				break;
			case LEFT:
				xText=(int)(minx+0.5);
				break;
			case RIGHT:
				xText=(int)(minx+(w-dimension.getWidth())+0.5);
				break;
			case JUSTIFIED:
				throw new UnsupportedOperationException("Unsupported");
			default:
				throw new IllegalStateException("Unsupported horizontal alignment " +hAlign);
			}
			
			final int yText ;
			switch (vAlign) {
			case BOTTOM:
				yText=(int)(miny+h-graphics.getFontMetrics().getDescent()+0.5) ;
				break;
			case TOP:
				yText=(int)(miny+graphics.getFontMetrics().getHeight()+0.5) ;
				break;
			case MIDDLE:
				yText=(int)(miny+(h+graphics.getFontMetrics().getHeight())/2+0.5) ;
				break;
			default:
				throw new IllegalStateException("Unsupported vertical alignment " +vAlign);
			}
			//draw
			graphics.drawString(text, xText,yText); 
			
	        //restore the old font
	        graphics.setFont(oldFont);
			
		}
		
		
	}
	 abstract class ColorMapEntryLegendBuilder extends Row{		
		
		protected ColorMapEntryLegendBuilder() {
			super();
		}


		protected ColorMapEntryLegendBuilder(List<Cell> columns) {
			super(columns);
			
		}


		protected ColorMapEntryLegendBuilder(ColorManager colorManager,
				TextManager labelManager, TextManager ruleManager) {
			super(Arrays.asList(colorManager,ruleManager,labelManager));
			
		}

		
		public boolean hasLabel() {
			return hasLabel;
		}
		protected boolean hasLabel;



		public Cell getRuleManager() {
			return get(1);
		}
		
		public Cell getLabelManager()  {
			return get(2);
		}
		public Cell getColorManager()  {
			return get(0);
		}
	}
	 class SingleColorMapEntryLegendBuilder extends ColorMapEntryLegendBuilder{
		
		public SingleColorMapEntryLegendBuilder(List<ColorMapEntry> cMapEntries, HAlign hAlign, VAlign vAling) {
			
			final ColorMapEntry currentCME = cMapEntries.get(0);
			Color color = LegendUtils.color(currentCME);
			final double opacity = LegendUtils.getOpacity(currentCME);
			color=new Color(color.getRed(),color.getGreen(),color.getBlue(),(int) (255*opacity));
			super.add( new SimpleColorManager(color,opacity));
			

			final String label = currentCME.getLabel();
			final double quantity = LegendUtils.getQuantity(currentCME);
			final String symbol = " = "; 
            String rule = Double.toString(quantity)+" "+symbol+" x";

            super.add( new TextManager(rule, vAling, hAlign));
            		
            // add the label the label to the rule so that we draw all text just once 
            if(label!=null)
            {
            	
            	hasLabel=true;
            	super.add( new TextManager(label, vAling, hAlign));
            }
            else
            	super.add( null);
		}



		
	}
	 class RampColorMapEntryLegendBuilder extends ColorMapEntryLegendBuilder{

		public RampColorMapEntryLegendBuilder(List<ColorMapEntry> mapEntries, HAlign hAlign, VAlign vAling) {
			
			final ColorMapEntry previousCME = mapEntries.get(0);
			final ColorMapEntry currentCME = mapEntries.get(1);
			boolean leftEdge;
			if(previousCME==null)
				leftEdge=true;
			else
				leftEdge=false;
		
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
			super.add( new GradientColorManager(color,opacity,previousColor));
			
			
			String label = currentCME.getLabel();
			double quantity = LegendUtils.getQuantity(currentCME);
			
			String symbol = leftEdge?" > ":" = "; 
            String rule = leftEdge?
            		Double.toString(quantity)+" "+symbol+" x":
            			Double.toString(quantity)+" "+symbol+" x";
            		

    		super.add(  new TextManager(rule, vAling, hAlign));
            		
            // add the label the label to the rule so that we draw all text just once 
            if(label!=null)
            {
            	
            	hasLabel=true;
            	super.add( new TextManager(label, vAling, hAlign));
            }
            else
            	super.add( null);
    		
		}
		
	}
	 
	 class ClassesEntryLegendBuilder extends ColorMapEntryLegendBuilder{

			public ClassesEntryLegendBuilder(List<ColorMapEntry> mapEntries, HAlign hAlign,VAlign vAling) {

				final ColorMapEntry previousCME = mapEntries.get(0);
				final ColorMapEntry currentCME = mapEntries.get(1);
				boolean leftEdge;
				if(previousCME==null)
					leftEdge=true;
				else
					leftEdge=false;
			
				Color color = LegendUtils.color(currentCME);
				final double opacity = LegendUtils.getOpacity(currentCME);
				color=new Color(color.getRed(),color.getGreen(),color.getBlue(),(int) (255*opacity));
				super.add( new SimpleColorManager(color,opacity));


				
				String label =currentCME.getLabel();
				double quantity1 = leftEdge?LegendUtils.getQuantity(currentCME):LegendUtils.getQuantity(previousCME);
				double quantity2 = LegendUtils.getQuantity(currentCME);
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

        		super.add( new TextManager(rule, vAling, hAlign));
	            		
	            // add the label the label to the rule so that we draw all text just once 
	            if(label!=null)
	            {
	            	
	            	hasLabel=true;
	            	super.add( new TextManager(label, vAling, hAlign));
	            }
	            else
	            	super.add( null);
	            
			}

			


			
			
			
		}	 
	
	private ColorMapType colorMapType;
	
	private boolean extended=false;

	private boolean transparent;

	private Dimension requestedDimension;

	private Map<String,?> additionalOptions;

	private Color backgroundColor;

	private Font labelFont;

	private Color labelFontColor;

	private ColorMapEntry previousCMapEntry;

	private final Queue<ColorMapEntryLegendBuilder> bodyRows= new LinkedList<ColorMapEntryLegendBuilder>();
	
	private final List<Cell> footerRows  = new ArrayList<Cell>();

	private HAlign hAlign=HAlign.LEFT;
	
	private VAlign vAlign=VAlign.BOTTOM;
	
	private double vMarginPercentage=LegendUtils.marginFactor;
	
	private double hMarginPercentage=LegendUtils.marginFactor;
	
	private double rowMarginPercentage=LegendUtils.rowPaddingFactor;
	
	private double columnMarginPercentage=LegendUtils.columnPaddingFactor;
	
	private boolean borderColor=false;
	
	private boolean borderLabel=false;
	
	private boolean borderRule=false;

	private double dx;

	private double dy;

	private double margin;

	private double rowH;

	private double colorW;

	private double ruleW;

	private double labelW;

	private double footerW;

	private String grayChannelName="1";

	private boolean fontAntiAliasing=true;

	private boolean forceRule=false;

	
	

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
				element=new SingleColorMapEntryLegendBuilder(Arrays.asList(cEntry), hAlign, vAlign);
				break;
			case RAMP:
				element=new RampColorMapEntryLegendBuilder(Arrays.asList(previousCMapEntry,cEntry), hAlign, vAlign);	
				break;
			case CLASSES:
				element=new ClassesEntryLegendBuilder(Arrays.asList(previousCMapEntry,cEntry), hAlign, vAlign);
				break;
				default:
					throw new IllegalArgumentException("Unrecognized colormap type");

		}
		
		//add to the table, we can use matrix algebra knowning that W==3 for this matrix
    	bodyRows.add(element);	//SHOULD BE REMOVED

    	
   	
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
		
		init();
		//now build the individuals legends
		
		//
		// header
		//
		

		//
		// body
		//
		final Queue<BufferedImage> body=createBody();


		//
		// footer
		//
		final Queue<BufferedImage> footer= createFooter();
		body.addAll(footer);
		
		//now merge them
		return mergeRows(body);
	}

	private void init() {

		//
		// check options
		//
		checkAdditionalOptions();
		
		//
		// create a sample image for computing dimensions of text strings
		//
        BufferedImage image = ImageUtils.createImage(1, 1, (IndexColorModel)null, transparent);
        final Map<Key, Object> hintsMap = new HashMap<Key, Object>();
        Graphics2D graphics = ImageUtils.prepareTransparency(transparent, backgroundColor, image, hintsMap);
        
        //elements used to compute maximum dimensions for rows and columns
        rowH=Double.NEGATIVE_INFINITY;
        colorW=Double.NEGATIVE_INFINITY;
        ruleW=Double.NEGATIVE_INFINITY;
        labelW=Double.NEGATIVE_INFINITY;
       


        //
        //BODY
        //
        //cycle over all the body elements
		cycleBodyRows(graphics);
		
		//
		//FOOTER
		//
        //set footer strings
		final String bandNameString = "Band selection is "+this.grayChannelName;
		footerRows.add(new TextManager(bandNameString,VAlign.BOTTOM,HAlign.LEFT));
        //set footer strings
		final String colorMapTypeString = "ColorMap type is "+this.colorMapType.toString();
		footerRows.add(new TextManager(colorMapTypeString,VAlign.BOTTOM,HAlign.LEFT));
		// extended colors or not
		final String extendedCMapString = "ColorMap is "+(this.extended?"":"not")+" extended";    
		footerRows.add(new TextManager(extendedCMapString,VAlign.BOTTOM,HAlign.LEFT));
		cycleFooterRows(graphics);

		
        //
        // compute dimensions
        //
		//final dimension are different between ramp and others since ramp does not have margin for rows
		final double maxW=Math.max(Math.max(Math.max(colorW,ruleW),labelW),footerW);
		dx=maxW*columnMarginPercentage;
		dy=colorMapType==ColorMapType.RAMP?0:rowH*rowMarginPercentage;
		
		final double mx=maxW*hMarginPercentage;
		final double my=rowH*vMarginPercentage;
		margin=Math.max(mx, my);
		
	}

	private void checkAdditionalOptions() {
		
        fontAntiAliasing = false;
        if (additionalOptions.get("fontAntiAliasing") instanceof String) {
            String aaVal = (String)additionalOptions.get("fontAntiAliasing");
            if (aaVal.equalsIgnoreCase("on") || aaVal.equalsIgnoreCase("true") ||
                    aaVal.equalsIgnoreCase("yes") || aaVal.equalsIgnoreCase("1")) {
                fontAntiAliasing = true;
            }
        }
        
        
        if (additionalOptions.get("dx") instanceof String) {
        	columnMarginPercentage=Double.parseDouble((String) additionalOptions.get("dx"));
            
        }
        
        if (additionalOptions.get("dy") instanceof String) {
        	rowMarginPercentage=Double.parseDouble((String) additionalOptions.get("dy"));
            
        }
        

        if (additionalOptions.get("mx") instanceof String) {
        	hMarginPercentage=Double.parseDouble((String) additionalOptions.get("mx"));
            
        }
        
        if (additionalOptions.get("my") instanceof String) {
        	vMarginPercentage=Double.parseDouble((String) additionalOptions.get("my"));
            
        }
        
        
        if (additionalOptions.get("borderColor") instanceof String) {
        	borderColor=Boolean.parseBoolean((String) additionalOptions.get("borderColor"));
            
        }
        
        if (additionalOptions.get("borderRule") instanceof String) {
        	borderRule=Boolean.parseBoolean((String) additionalOptions.get("borderRule"));
            
        }
        
        if (additionalOptions.get("borderLabel") instanceof String) {
        	borderLabel=Boolean.parseBoolean((String) additionalOptions.get("borderLabel"));
            
        }
        
        
        if (additionalOptions.get("forceRule") instanceof String) {
        	forceRule=Boolean.parseBoolean((String) additionalOptions.get("forceRule"));
            
        }
        
        
        
	}

	private void cycleFooterRows(Graphics2D graphics) {
		int numRows=this.footerRows.size(),i=0;
		footerW=Double.NEGATIVE_INFINITY;
		for(i=0;i<numRows;i++){
			
			//
			//row number i
			//
			
			// color element
			final Cell cell= this.footerRows.get(i);
			final Dimension cellDim=cell.getPreferredDimension(graphics);
			rowH=Math.max(rowH, cellDim.getHeight());
			footerW=Math.max(footerW, cellDim.getWidth());
			
			
		}
		
	}

	/**
	 * @param graphics
	 */
	private void cycleBodyRows(Graphics2D graphics) {
		for(ColorMapEntryLegendBuilder row:bodyRows){
			
			//
			//row number i
			//
			
			// color element
			final Cell cm= row.getColorManager();
			final Dimension colorDim=cm.getPreferredDimension(graphics);
			rowH=Math.max(rowH, colorDim.getHeight());
			colorW=Math.max(colorW, colorDim.getWidth());
			
			// rule
			if(forceRule){
				final Cell ruleM= row.getRuleManager();
				final Dimension ruleDim=ruleM.getPreferredDimension(graphics);
				rowH=Math.max(rowH, ruleDim.getHeight());
				ruleW=Math.max(ruleW, ruleDim.getWidth());
			}
			
			// label
			final Cell labelM= row.getLabelManager();
			if(labelM==null)
				continue;
			final Dimension labelDim=labelM.getPreferredDimension(graphics);
			rowH=Math.max(rowH, labelDim.getHeight());
			labelW=Math.max(labelW, labelDim.getWidth());
		}
	}

	private Queue<BufferedImage> createFooter() {
		
		// creating a backbuffer image on which we should draw the bkgColor for this colormap element
        final BufferedImage image = ImageUtils.createImage(1, 1, (IndexColorModel)null, transparent);
        final Map<Key, Object> hintsMap = new HashMap<Key, Object>();
        final Graphics2D graphics = ImageUtils.prepareTransparency(transparent, backgroundColor, image, hintsMap);
        
        //list where we store the rows for the footer
        final Queue<BufferedImage> queue= new LinkedList<BufferedImage>();
		//the height is already fixed
		final int rowHeight=(int)Math.round(rowH);
		final int rowWidth=(int)Math.round(footerW);
		final Rectangle clipboxA=new Rectangle(0,0,rowWidth,rowHeight);
        //
        // footer
        //
        //
		//draw the various bodyCells
		for(Cell cell:footerRows){
			
//			//get dim
//			final Dimension dim= cell.getPreferredDimension(graphics);
//			final int rowWidth=(int)Math.round(dim.getWidth());
//			final Rectangle clipboxA=new Rectangle(0,0,rowWidth,rowHeight);
			

			//draw it
	        final BufferedImage colorCellLegend = new BufferedImage(rowWidth, rowHeight, BufferedImage.TYPE_INT_ARGB);	
	        Graphics2D rlg = colorCellLegend.createGraphics();
	        rlg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        cell.draw(rlg, clipboxA,borderColor);
	        rlg.dispose(); 
	        
	        queue.add(colorCellLegend);

		}

        graphics.dispose();
        
        
		return queue;//mergeRows(queue);
	}

	private Queue<BufferedImage> createBody() {


		final Queue<BufferedImage> queue= new LinkedList<BufferedImage>();
		
		//
		// draw the various elements
		//
		//create the boxes for drawing later
		final int rowHeight=(int)Math.round(rowH);
		final int colorWidth=(int)Math.round(colorW);
		final int ruleWidth=(int)Math.round(ruleW);
		final int labelWidth=(int)Math.round(labelW);
		final Rectangle clipboxA=new Rectangle(0,0,colorWidth,rowHeight);
		final Rectangle clipboxB=new Rectangle(0,0,ruleWidth,rowHeight);
		final Rectangle clipboxC=new Rectangle(0,0,labelWidth,rowHeight);
		

		
        //
        // Body
        //
        //
		//draw the various bodyCells
		for(ColorMapEntryLegendBuilder row:bodyRows){
			
			//
			//row number i
			//
			//get element for color default behavior
			final Cell colorCell= row.getColorManager();
			//draw it
	        final BufferedImage colorCellLegend = new BufferedImage(colorWidth, rowHeight, BufferedImage.TYPE_INT_ARGB);	
	        Graphics2D rlg = colorCellLegend.createGraphics();
	        rlg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        rlg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	        colorCell.draw(rlg, clipboxA,borderColor);
	        rlg.dispose(); 
	        
	        BufferedImage ruleCellLegend=null;
	        if(forceRule){
				//get element for rule
				final Cell ruleCell= row.getRuleManager();
				//draw it
		        ruleCellLegend = new BufferedImage(ruleWidth, rowHeight, BufferedImage.TYPE_INT_ARGB);	
		        rlg = ruleCellLegend.createGraphics();
		        rlg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		        rlg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		        ruleCell.draw(rlg, clipboxB,borderRule);
		        rlg.dispose(); 
	        }

	        
			//get element for label
			final Cell labelCell= row.getLabelManager();
			//draw it
	        final BufferedImage labelCellLegend = new BufferedImage(labelWidth, rowHeight, BufferedImage.TYPE_INT_ARGB);	
	        rlg = labelCellLegend.createGraphics();
	        rlg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        rlg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);	        
	        labelCell.draw(rlg, clipboxC,borderLabel);
	        rlg.dispose(); 
	       
	        //
            // merge the bodyCells for this row
	        //
	        //

	        final Map<Key, Object> hintsMap = new HashMap<Key, Object>();
	        queue.add(LegendUtils.mergeBufferedImages(colorCellLegend,ruleCellLegend,labelCellLegend, hintsMap,transparent,backgroundColor,dx));    
			

	       
			
		}		
		
		
		//return the list of legends
		return queue;//mergeRows(queue);
	}

	private BufferedImage mergeRows(Queue<BufferedImage> legendsQueue) {
         
         //create the final image
         
         // I am doing a straight cast since I know that I built this
			// dimension object by using the widths and heights of the various
			// bufferedimages for the various bkgColor map entries.
		final Dimension finalDimension = new Dimension();
		final int numRows = legendsQueue.size();
		finalDimension.setSize(Math.max(footerW,colorW + ruleW + labelW) + 2 * dx + 2* margin, rowH * numRows + 2 * margin + (numRows - 1) * dy);
		
		
         final int totalWidth=(int) finalDimension.getWidth();
		 final int totalHeight=(int) finalDimension.getHeight();
		 final BufferedImage finalLegend = ImageUtils.createImage(totalWidth, totalHeight, (IndexColorModel)null, transparent);
		 final Map<Key, Object> hintsMap = new HashMap<Key, Object>();
         Graphics2D finalGraphics = ImageUtils.prepareTransparency(transparent, backgroundColor, finalLegend, hintsMap);
         hintsMap.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         finalGraphics.setRenderingHints(hintsMap);

         int topOfRow = (int) (margin+0.5);
         for (int i = 0; i < numRows; i++) {
			final BufferedImage img = legendsQueue.remove();

			// draw the image
			finalGraphics.drawImage(img, (int) (margin+0.5), topOfRow, null);
			topOfRow += img.getHeight()+dy;
			
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
	public Map<String, ?> getAdditionalOptions() {
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

	public void setBand(SelectedChannelType grayChannel) {
		if(grayChannel!=null)
			this.grayChannelName=grayChannel.getChannelName();
		
		if(grayChannelName==null)
			this.grayChannelName="1";
		
	}


}

