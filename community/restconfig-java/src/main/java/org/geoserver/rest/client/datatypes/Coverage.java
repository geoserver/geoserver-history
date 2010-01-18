package org.geoserver.rest.client.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Coverage {

	protected String description;

	protected boolean enabled;

	protected String href;

	protected Keywords keywords;

	protected LatLonBoundingBox latLonBoundingBox;

	protected String name;

	protected Namespace namespace;

	protected NativeBoundingBox nativeBoundingBox;

	protected String nativeCRS;

	protected String nativeFormat;

	protected String nativeName;

	protected String srs;

	protected CoverageStore store;

	protected String title;

	public Coverage() {

	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return the href
	 */
	public String getHref() {
		return this.href;
	}

	/**
	 * @return the keywords
	 */
	public Keywords getKeywords() {
		return this.keywords;
	}

	/**
	 * @return the latLonBoundingBox
	 */
	public LatLonBoundingBox getLatLonBoundingBox() {
		return this.latLonBoundingBox;
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
	 * @return the boundingBox
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
	 * @return the nativeFormat
	 */
	public String getNativeFormat() {
		return this.nativeFormat;
	}

	/**
	 * @return the nativeName
	 */
	public String getNativeName() {
		return this.nativeName;
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
	public CoverageStore getStore() {
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
	 * @param description the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
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
	 * @param keywords the keywords to set
	 */
	public void setKeywords(final Keywords keywords) {
		this.keywords = keywords;
	}

	/**
	 * @param latLonBoundingBox the latLonBoundingBox to set
	 */
	public void setLatLonBoundingBox(final LatLonBoundingBox latLonBoundingBox) {
		this.latLonBoundingBox = latLonBoundingBox;
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
	 * @param boundingBox the boundingBox to set
	 */
	public void setNativeBoundingBox(final NativeBoundingBox boundingBox) {
		this.nativeBoundingBox = boundingBox;
	}

	/**
	 * @param nativeCRS the nativeCRS to set
	 */
	public void setNativeCRS(final String nativeCRS) {
		this.nativeCRS = nativeCRS;
	}

	/**
	 * @param nativeFormat the nativeFormat to set
	 */
	public void setNativeFormat(final String nativeFormat) {
		this.nativeFormat = nativeFormat;
	}

	/**
	 * @param nativeName the nativeName to set
	 */
	public void setNativeName(final String nativeName) {
		this.nativeName = nativeName;
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
	public void setStore(final CoverageStore store) {
		this.store = store;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}
}
