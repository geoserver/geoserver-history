/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.wps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.wps.InputParameterValues.ParameterType;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * Allows the user to edit a complex input parameter providing a variety of
 * different editors depending on the parameter type
 * 
 * @author Andrea Aime - OpenGeo
 */
@SuppressWarnings("serial")
public class ComplexInputPanel extends Panel {

	private DropDownChoice typeChoice;
	PropertyModel valueModel;
	List<String> mimeTypes;

	public ComplexInputPanel(String id, InputParameterValues pv, int valueIndex) {
		super(id);
		setOutputMarkupId(true);
		setModel(new PropertyModel(pv, "values[" + valueIndex + "]"));
		valueModel = new PropertyModel(getModel(), "value");
		mimeTypes = pv.getSupportedMime();

		List<ParameterType> ptypes = new ArrayList<ParameterType>(Arrays
				.asList(ParameterType.values()));
		ptypes.remove(ParameterType.LITERAL);
		typeChoice = new DropDownChoice("type", new PropertyModel(
				getModelObject(), "type"), ptypes);
		add(typeChoice);

		updateEditor();

		typeChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				updateEditor();
				target.addComponent(ComplexInputPanel.this);
			}

		});
	}

	void updateEditor() {
		// remove the old editor
		if (get("editor") != null) {
			remove("editor");
		}

		// reset the previous value
		valueModel.setObject(null);

		ParameterType pt = (ParameterType) typeChoice.getModelObject();
		if (pt == ParameterType.TEXT) {
			// data as plain text
			Fragment f = new Fragment("editor", "text", this);
			DropDownChoice mimeChoice = new DropDownChoice("mime",
					new PropertyModel(getModel(), "mime"), mimeTypes);
			f.add(mimeChoice);

			f.add(new TextArea("textarea", valueModel));
			add(f);
		} else if (pt == ParameterType.VECTOR_LAYER) {
			// an internal vector layer
			valueModel.setObject(new VectorLayerConfiguration());
			Fragment f = new Fragment("editor", "vectorLayer", this);
			DropDownChoice layer = new DropDownChoice("layer",
					new PropertyModel(valueModel, "layerName"),
					getVectorLayerNames());
			f.add(layer);
			add(f);
		} else if (pt == ParameterType.RASTER_LAYER) {
			// an internal raster layer
			valueModel.setObject(new RasterLayerConfiguration());

			Fragment f = new Fragment("editor", "rasterLayer", this);
			final DropDownChoice layer = new DropDownChoice("layer",
					new PropertyModel(valueModel, "layerName"),
					getRasterLayerNames());
			f.add(layer);
			add(f);

			// we need to update the raster own bounding box as wcs requests
			// mandate a spatial extent (why oh why???)
			layer.add(new AjaxFormComponentUpdatingBehavior("onchange") {

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					String name = layer.getModelObjectAsString();
					LayerInfo li = GeoServerApplication.get().getCatalog()
							.getLayerByName(name);
					ReferencedEnvelope spatialDomain = li.getResource()
							.getNativeBoundingBox();
					((RasterLayerConfiguration) valueModel.getObject())
							.setSpatialDomain(spatialDomain);
				}
			});
		} else if (pt == ParameterType.REFERENCE) {
			// an external reference
			valueModel.setObject(new ReferenceConfiguration());

			Fragment f = new Fragment("editor", "reference", this);
			final DropDownChoice method = new DropDownChoice("method",
					new PropertyModel(valueModel, "method"), Arrays.asList(
							ReferenceConfiguration.Method.GET,
							ReferenceConfiguration.Method.POST));
			f.add(method);

			DropDownChoice mimeChoice = new DropDownChoice("mime",
					new PropertyModel(valueModel, "mime"), mimeTypes);
			f.add(mimeChoice);

			f.add(new TextField("url", new PropertyModel(valueModel, "url"))
					.setRequired(true));
			final TextArea body = new TextArea("body", new PropertyModel(
					valueModel, "body"));
			add(body);

			final WebMarkupContainer bodyContainer = new WebMarkupContainer(
					"bodyContainer");
			f.add(bodyContainer);
			bodyContainer.setOutputMarkupId(true);
			bodyContainer.add(body);
			bodyContainer.setVisible(false);

			method.add(new AjaxFormComponentUpdatingBehavior("onchange") {

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					boolean post = method.getModelObject() == ReferenceConfiguration.Method.POST;
					bodyContainer.setVisible(post);
					body.setRequired(post);
					target.addComponent(ComplexInputPanel.this);
				}
			});

			add(f);
		} else {
			error("Unsupported parameter type");
		}
	}

	List<String> getVectorLayerNames() {
		Catalog catalog = GeoServerApplication.get().getCatalog();

		List<String> result = new ArrayList<String>();
		for (LayerInfo li : catalog.getLayers()) {
			if (li.getResource() instanceof FeatureTypeInfo) {
				result.add(li.getResource().getPrefixedName());
			}
		}
		return result;
	}

	List<String> getRasterLayerNames() {
		Catalog catalog = GeoServerApplication.get().getCatalog();

		List<String> result = new ArrayList<String>();
		for (LayerInfo li : catalog.getLayers()) {
			if (li.getResource() instanceof CoverageInfo) {
				result.add(li.getResource().getPrefixedName());
			}
		}
		return result;
	}
}
