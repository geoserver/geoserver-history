package org.geoserver.rest.client.datatypes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FeatureType {

	@XmlElement(name="abstract")
	protected String abstractStr;

	private Attributes attributes;

	protected boolean enabled;

	protected String href;

	protected LatLonBoundingBox latLonBoundingBox;

	protected int maxFeatures;

	protected String name;

	protected Namespace namespace;

	protected NativeBoundingBox nativeBoundingBox;

	protected String nativeCRS;

	protected String nativeName;

	protected int numDecimals;

	protected String projectionPolicy;

	protected String srs;

	protected DataStore store;

	protected String title;

	/**
	 * @return the attributes
	 */
	public Attributes getAttributes() {
		return this.attributes;
	}

	/**
	 * @return the href
	 */
	public String getHref() {
		return this.href;
	}

	/**
	 * @return the latLonBoundingBox
	 */
	public LatLonBoundingBox getLatLonBoundingBox() {
		return this.latLonBoundingBox;
	}

	/**
	 * @return the maxFeatures
	 */
	public int getMaxFeatures() {
		return this.maxFeatures;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the namespace
	 */
	public Namespace getNamespace() {
		return this.namespace;
	}

	/**
	 * @return the nativeBoundingBox
	 */
	public NativeBoundingBox getNativeBoundingBox() {
		return this.nativeBoundingBox;
	}

	/**
	 * @return the nativeCRS
	 */
	public String getNativeCRS() {
		return this.nativeCRS;
	}

	/**
	 * @return the nativeName
	 */
	public String getNativeName() {
		return this.nativeName;
	}

	/**
	 * @return the numDecimals
	 */
	public int getNumDecimals() {
		return this.numDecimals;
	}

	/**
	 * @return the projectionPolicy
	 */
	public String getProjectionPolicy() {
		return this.projectionPolicy;
	}

	/**
	 * @return the srs
	 */
	public String getSrs() {
		return this.srs;
	}

	/**
	 * @return the store
	 */
	public DataStore getStore() {
		return this.store;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * @param abstractStr the abstractStr to set
	 */
	public void setAbstractStr(final String abstractStr) {
		this.abstractStr = abstractStr;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(final Attributes attributes) {
		this.attributes = attributes;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @param href the href to set
	 */
	public void setHref(final String href) {
		this.href = href;
	}

	/**
	 * @param latLonBoundingBox the latLonBoundingBox to set
	 */
	public void setLatLonBoundingBox(final LatLonBoundingBox latLonBoundingBox) {
		this.latLonBoundingBox = latLonBoundingBox;
	}

	/**
	 * @param maxFeatures the maxFeatures to set
	 */
	public void setMaxFeatures(final int maxFeatures) {
		this.maxFeatures = maxFeatures;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(final Namespace namespace) {
		this.namespace = namespace;
	}

	/**
	 * @param nativeBoundingBox the nativeBoundingBox to set
	 */
	public void setNativeBoundingBox(final NativeBoundingBox nativeBoundingBox) {
		this.nativeBoundingBox = nativeBoundingBox;
	}

	/**
	 * @param nativeCRS the nativeCRS to set
	 */
	public void setNativeCRS(final String nativeCRS) {
		this.nativeCRS = nativeCRS;
	}

	/**
	 * @param nativeName the nativeName to set
	 */
	public void setNativeName(final String nativeName) {
		this.nativeName = nativeName;
	}

	/**
	 * @param numDecimals the numDecimals to set
	 */
	public void setNumDecimals(final int numDecimals) {
		this.numDecimals = numDecimals;
	}

	/**
	 * @param projectionPolicy the projectionPolicy to set
	 */
	public void setProjectionPolicy(final String projectionPolicy) {
		this.projectionPolicy = projectionPolicy;
	}

	/**
	 * @param srs the srs to set
	 */
	public void setSrs(final String srs) {
		this.srs = srs;
	}

	/**
	 * @param store the store to set
	 */
	public void setStore(final DataStore store) {
		this.store = store;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}
}
