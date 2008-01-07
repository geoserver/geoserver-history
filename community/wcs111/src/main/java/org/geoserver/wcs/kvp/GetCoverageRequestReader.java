/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs.kvp;

import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.InvalidParameterValue;
import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.MissingParameterValue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.opengis.ows.v1_1_0.BoundingBoxType;
import net.opengis.wcs.v1_1_1.AxisSubsetType;
import net.opengis.wcs.v1_1_1.DomainSubsetType;
import net.opengis.wcs.v1_1_1.FieldSubsetType;
import net.opengis.wcs.v1_1_1.GetCoverageType;
import net.opengis.wcs.v1_1_1.OutputType;
import net.opengis.wcs.v1_1_1.RangeSubsetType;
import net.opengis.wcs.v1_1_1.TimeSequenceType;
import net.opengis.wcs.v1_1_1.Wcs111Factory;

import org.geoserver.ows.kvp.EMFKvpRequestReader;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.global.CoverageDimension;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.WcsException.WcsExceptionCode;

public class GetCoverageRequestReader extends EMFKvpRequestReader {

    Data catalog;

    public GetCoverageRequestReader(Data catalog) {
        super(GetCoverageType.class, Wcs111Factory.eINSTANCE);
        this.catalog = catalog;
    }

    @Override
    public Object read(Object request, Map kvp, Map rawKvp) throws Exception {
        GetCoverageType getCoverage = (GetCoverageType) super.read(request, kvp, rawKvp);

        // grab coverage info to perform further checks
        if (getCoverage.getIdentifier() == null)
            throw new WcsException("identifier parameter is mandatory", MissingParameterValue,
                    "identifier");
        CoverageInfo info = catalog.getCoverageInfo(getCoverage.getIdentifier().getValue());

        // build the domain subset
        getCoverage.setDomainSubset(parseDomainSubset(info, kvp));

        // check the range subset (validity of fields and so on)
        checkRangeSubset(info, getCoverage.getRangeSubset());

        // build output element
        getCoverage.setOutput(parseOutputElement(info, kvp));

        return getCoverage;
    }

    private void checkRangeSubset(CoverageInfo info, RangeSubsetType rangeSubset) {
        // quick escape if no range subset has been specified (it's legal)
        if(rangeSubset == null)
            return;
        
        if (rangeSubset.getFieldSubset().size() > 1) {
            throw new WcsException("Multi field coverages are not supported yet",
                    InvalidParameterValue, "RangeSubset");
        }

        // check field identifier
        FieldSubsetType field = (FieldSubsetType) rangeSubset.getFieldSubset().get(0);
        final String fieldId = field.getIdentifier().getValue();
        if (!fieldId.equalsIgnoreCase(info.getLabel()))
            throw new WcsException("Unknown field " + fieldId, InvalidParameterValue, "RangeSubset");

        // check axis
        if (field.getAxisSubset().size() > 1) {
            throw new WcsException("Multi axis coverages are not supported yet",
                    InvalidParameterValue, "RangeSubset");
        }
        else if(field.getAxisSubset().size() == 0)
            return;
        
        AxisSubsetType axisSubset = (AxisSubsetType) field.getAxisSubset().get(0);
        final String axisId = axisSubset.getIdentifier();
        if (!axisId.equalsIgnoreCase("Bands"))
            throw new WcsException("Unknown axis " + axisId + " in field " + fieldId,
                    InvalidParameterValue, "RangeSubset");

        // prepare a support structure to quickly get the band index of a key
        // (and remember we replaced spaces with underscores in the keys to
        // avoid issues
        // with the kvp parsing of indentifiers that include spaces)
        CoverageDimension[] dimensions = info.getDimensions();
        Set<String> dimensionMap = new HashSet<String>();
        for (int i = 0; i < dimensions.length; i++) {
            String keyName = dimensions[i].getName().replace(' ', '_');
            dimensionMap.add(keyName);
        }

        // check keys
        List keys = axisSubset.getKey();
        int[] bands = new int[keys.size()];
        for (int j = 0; j < bands.length; j++) {
            final String key = (String) keys.get(j);
            if (!dimensionMap.contains(key))
                throw new WcsException("Unknown field/axis/key combination " + fieldId + "/"
                        + axisSubset.getIdentifier() + "/" + key);
        }
    }

    private DomainSubsetType parseDomainSubset(CoverageInfo info, Map kvp) {
        final DomainSubsetType domainSubset = Wcs111Factory.eINSTANCE.createDomainSubsetType();

        // either bbox or timesequence must be there
        BoundingBoxType bbox = (BoundingBoxType) kvp.get("BoundingBox");
        TimeSequenceType timeSequence = (TimeSequenceType) kvp.get("TemporalSubset");
        if (timeSequence == null && bbox == null)
            throw new WcsException(
                    "Bounding box cannot be null, TimeSequence has not been specified",
                    WcsExceptionCode.MissingParameterValue, "BoundingBox");

        domainSubset.setBoundingBox(bbox);
        domainSubset.setTemporalSubset(timeSequence);

        return domainSubset;
    }

    private OutputType parseOutputElement(CoverageInfo info, Map kvp) throws Exception {
        final OutputType output = Wcs111Factory.eINSTANCE.createOutputType();
        output.setGridCRS(Wcs111Factory.eINSTANCE.createGridCrsType());

        // check and set store
        Boolean store = (Boolean) kvp.get("store");
        if (store != null)
            output.setStore(store.booleanValue());
        if (output.isStore())
            throw new WcsException("store is not supported", InvalidParameterValue, "store");

        // check and set format
        String format = (String) kvp.get("format");
        if (format == null)
            throw new WcsException("format parameter is mandatory", MissingParameterValue, "format");
        String declaredFormat = getDeclaredFormat(info.getSupportedFormats(), format);
        if (declaredFormat == null)
            throw new WcsException("format " + format + " is not supported for this coverage",
                    InvalidParameterValue, "format");
        output.setFormat(declaredFormat);

        // check and set the grid base crs
        String gridBaseCrs = (String) kvp.get("gridBaseCrs");
        if (gridBaseCrs != null) {
            // make sure the requested is among the supported ones, by making a
            // code level
            // comparison (to avoid assuming epsg:xxxx and
            // http://www.opengis.net/gml/srs/epsg.xml#xxx are different ones.
            // We'll also consider the urn one comparable, allowing eventual
            // axis flip on the
            // geographic crs
            String actualCRS = null;
            final String gridBaseCrsCode = extractCode(gridBaseCrs);
            for (Iterator it = info.getResponseCRSs().iterator(); it.hasNext();) {
                final String responseCRS = (String) it.next();
                final String code = extractCode(responseCRS);
                if (code.equalsIgnoreCase(gridBaseCrsCode)) {
                    actualCRS = responseCRS;
                }
            }
            if (actualCRS == null)
                throw new WcsException("CRS " + gridBaseCrs
                        + " is not among the supported ones for coverage " + info.getName(),
                        WcsExceptionCode.InvalidParameterValue, "GridBaseCrs");
            output.getGridCRS().setGridBaseCRS(gridBaseCrs);
        } else {
            String code = GML2EncodingUtils.epsgCode(info.getCrs());
            output.getGridCRS().setGridBaseCRS("urn:x-ogc:def:crs:EPSG:" + code);
        }

        // check and set grid type
        String gridType = (String) kvp.get("gridType");
        if (gridType != null)
            output.getGridCRS().setGridType(gridType);

        // check and set grid cs
        String gridCS = (String) kvp.get("gridCS");
        if (gridCS != null)
            output.getGridCRS().setGridCS(gridCS);

        // check and set grid origin
        CoordinateReferenceSystem crs = CRS.decode(output.getGridCRS().getGridBaseCRS());
        final int crsDimension = crs.getCoordinateSystem().getDimension();
        double[] gridOrigin = (double[]) kvp.get("GridOrigin");
        if (gridOrigin != null) {
            // make sure the origin dimension matches the output crs dimension

            if (crsDimension != gridOrigin.length)
                throw new WcsException("Grid origin size (" + gridOrigin.length
                        + ") inconsistent with crs dimension (" + crsDimension + ")",
                        WcsExceptionCode.InvalidParameterValue, "GridOrigin");
            output.getGridCRS().setGridOrigin(gridOrigin);
        } else {
            double[] defaultOrigins = new double[crsDimension];
            Arrays.fill(defaultOrigins, 0);
            output.getGridCRS().setGridOrigin(defaultOrigins);
        }

        // check and set grid offsets
        double[] gridOffsets = (double[]) kvp.get("GridOffsets");
        if (gridOffsets != null) {
            // make sure the origin dimension matches the output crs dimension
            if (crsDimension != gridOffsets.length)
                throw new WcsException("Grid offsets size (" + gridOffsets.length
                        + ") inconsistent with crs dimension (" + crsDimension + ")",
                        WcsExceptionCode.InvalidParameterValue, "GridOffsets");
            output.getGridCRS().setGridOffsets(gridOffsets);
        } else {
            double[] defaultOffsets = new double[crsDimension];
            Arrays.fill(defaultOffsets, 0);
            output.getGridCRS().setGridOffsets(defaultOffsets);
        }

        return output;
    }

    /**
     * Extracts only the final part of an EPSG code allowing for a specification
     * independent comparison (that is, it removes the EPSG:, urn:xxx:,
     * http://... prefixes)
     * 
     * @param srsName
     * @return
     */
    private String extractCode(String srsName) {
        if (srsName.startsWith("http://www.opengis.net/gml/srs/epsg.xml#"))
            return srsName.substring(40);
        else if (srsName.startsWith("urn:"))
            return srsName.substring(srsName.lastIndexOf(':') + 1);
        else if (srsName.startsWith("EPSG:"))
            return srsName.substring(5);
        else
            return srsName;
    }

    /**
     * Checks if the supported format string list contains the specified format,
     * doing a case insensitive search. If found the declared output format name
     * is returned, otherwise null is returned.
     * 
     * @param supportedFormats
     * @param format
     * @return
     */
    private String getDeclaredFormat(List supportedFormats, String format) {
        for (Iterator it = supportedFormats.iterator(); it.hasNext();) {
            String sf = (String) it.next();
            if (sf.equalsIgnoreCase(format))
                return sf;
        }
        return null;
    }

}
