package org.geoserver.sldservice.resource;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerException;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.filter.FilterTransformer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDParser;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;
import org.vfny.geoserver.config.ConfigRequests;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.global.Data;

import com.noelios.restlet.ext.servlet.ServletCall;
import com.noelios.restlet.http.HttpCall;
import com.noelios.restlet.http.HttpRequest;

/**
 * @author kappu
 * 
 */
public class BaseResource extends Resource {
	protected Data dt;
	protected String baseUrl;

	public BaseResource(Context con, Request req, Response resp, Data data) {
		super(con, req, resp);
		this.dt = data;
		baseUrl = this.getRequest().getResourceRef().toString();
		if(baseUrl.charAt(baseUrl.length()-1)=='/')baseUrl=baseUrl.substring(0, baseUrl.length()-1);
	}

	protected StyledLayerDescriptor loadFile(File file) throws Exception {

		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools
				.getDefaultHints());
		StyleFactory factory = CommonFactoryFinder.getStyleFactory(GeoTools
				.getDefaultHints());
		SLDParser stylereader = new SLDParser(factory, file);

		StyledLayerDescriptor newSLD = stylereader.parseSLD();
		// TODO: handle exceptions
		if (newSLD == null) {
			// exceptional!
			throw (IOException) new IOException("SLD import returned null"); //
		}
		return newSLD;
	}

	/**
	 * return HttpServletRequest
	 * 
	 * @return
	 */
	protected HttpServletRequest getHttpRequest() {
		Request req = this.getRequest();
		if (req instanceof HttpRequest) {
			HttpRequest httpRequest = (HttpRequest) req;
			HttpCall httpCall = httpRequest.getHttpCall();
			if (httpCall instanceof ServletCall) {
				HttpServletRequest httpServletRequest = ((ServletCall) httpCall)
						.getRequest();
				return httpServletRequest;
			}
		}
		return null;
	}

	/**
	 * Get geoserver DataConfig
	 * 
	 * @return
	 */
	protected DataConfig getDataConfig() {
		return ConfigRequests.getDataConfig(this.getHttpRequest());
	}

	/**
	 * 
	 * @param style
	 * @return a string with json userStyle representation
	 */
	protected String jsonUserStyle(Style style) {
		String userStyleSz;
		userStyleSz = "{'name':'" + style.getName() + "','title':'"
				+ style.getTitle() + "','abstract':'" + style.getAbstract()
				+ "','featureTypeStyles':[";
		FeatureTypeStyle[] ftStyleA = style.getFeatureTypeStyles();
		String fTypeStyleSz;
		FeatureTypeStyle fTypeStyle = ftStyleA[0];
		fTypeStyleSz = "{'name':'" + fTypeStyle.getName() + "','link':'"
				+ this.baseUrl + "/0','id':'0'}";
		userStyleSz += fTypeStyleSz;

		for (int i = 1; i < ftStyleA.length; i++) {
			fTypeStyle = ftStyleA[i];
			fTypeStyleSz = "{'name':'" + fTypeStyle.getName() + "','link':'"
					+ this.baseUrl + "/" + i + "','id':,'" + i + "'}";
			userStyleSz += "," + fTypeStyleSz;
		}
		userStyleSz += "]}";
		return userStyleSz;
	}

	/**
	 * 
	 * @param fTStyle
	 * @return a string with json featureTypeStyle representation
	 */
	protected String jsonFeatureTypeStyle(FeatureTypeStyle fTStyle) {
		String featureTypeStyleSz;
		featureTypeStyleSz = "{'name':'" + fTStyle.getName() + "','title':'"
				+ fTStyle.getTitle() + "','abstract':'" + fTStyle.getAbstract()
				+ "','featureTypeName':'" + fTStyle.getFeatureTypeName()
				+ "',ruleslength:'" + fTStyle.getRules().length + "'}";
		return featureTypeStyleSz;
	}

	/**
	 * 
	 * @param rule
	 * @return a string with json Rule representation
	 */
	protected String jsonRuleCLQ(Rule rule) {

		String ruleSz;
		Filter filter;
		Symbolizer[] symbolizerA;
		
		
		ruleSz = "{'name':'" + rule.getName() + "','title':'" + rule.getTitle()
				+ "','abstract':'" + rule.getAbstract() + "'";
		if (rule.getMaxScaleDenominator() != Double.POSITIVE_INFINITY)
			ruleSz += ",'maxScaleDenominator':'"
					+ rule.getMaxScaleDenominator() + "'";
		if (rule.getMinScaleDenominator() != 0.0)
			ruleSz += ",'minScaleDenominator':'"
					+ rule.getMinScaleDenominator() + "'";
		if (rule.getFilter()!=null){
			filter = rule.getFilter();
			ruleSz+=",'filter':"+this.jsonFilter(filter);
		}if (rule.getSymbolizers()!=null){
			symbolizerA=rule.getSymbolizers();
			
			ruleSz+=",'Symbolyzer':[";
			ruleSz+=this.jsonSybolizer(symbolizerA[0]);
			for(int i =1;i<symbolizerA.length;i++){
				ruleSz+=","+this.jsonSybolizer(symbolizerA[i]);
			}
			ruleSz+="]";
		}
		
		ruleSz += "}";

		return ruleSz;

	}
	/**
	 * 
	 * @param rule
	 * @return a string with json Rule representation
	 */
	protected String jsonRule(Rule rule) {

		String ruleSz=null;
		String xmlRule;
		XMLSerializer xmlS = new XMLSerializer();
				
		SLDTransformer transform = new SLDTransformer();
		transform.setIndentation(2);
		try {
			xmlRule = transform.transform( rule );
			xmlS.setRemoveNamespacePrefixFromElements(true);
			xmlS.setSkipNamespaces(true);
			JSONObject json = (JSONObject)   xmlS.read( xmlRule );
			ruleSz = json.toString();
			
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return ruleSz;

	}
	protected String jsonFilter(Filter filter) {
		String filterSz = null;
		String xmlFilter;
		XMLSerializer xmlS = new XMLSerializer();
		
		FilterTransformer transform = new FilterTransformer();
		transform.setIndentation(2);
		try {

			
			xmlFilter = transform.transform( filter );
			xmlS.setRemoveNamespacePrefixFromElements(true);
			xmlS.setSkipNamespaces(true);
			JSONObject json = (JSONObject)   xmlS.read( xmlFilter );
			filterSz = json.toString();
			
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return filterSz;
		
	}
	protected String jsonSybolizer(Symbolizer symbolizer) {
		String symbolizerSz = null;
		String xmlSymbolizer;
		XMLSerializer xmlS = new XMLSerializer();
		
		SLDTransformer transform = new SLDTransformer();
		transform.setIndentation(2);
		try {

			xmlSymbolizer = transform.transform( symbolizer );
			xmlS.setRemoveNamespacePrefixFromElements(true);
			xmlS.setSkipNamespaces(true);
			JSONObject json = (JSONObject)   xmlS.read( xmlSymbolizer );
			symbolizerSz = json.toString();
			
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return symbolizerSz;
		
	}
}
