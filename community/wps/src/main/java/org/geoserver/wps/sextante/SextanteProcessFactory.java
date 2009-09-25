/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wps.sextante;

import static org.geoserver.wps.sextante.SextanteProcessFactoryConstants.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.geotools.data.Parameter;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.process.Process;
import org.geotools.process.ProcessFactory;
import org.geotools.text.Text;
import org.jfree.chart.ChartPanel;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

import es.unex.sextante.additionalInfo.AdditionalInfo;
import es.unex.sextante.additionalInfo.AdditionalInfoBoolean;
import es.unex.sextante.additionalInfo.AdditionalInfoFixedTable;
import es.unex.sextante.additionalInfo.AdditionalInfoMultipleInput;
import es.unex.sextante.additionalInfo.AdditionalInfoNumericalValue;
import es.unex.sextante.additionalInfo.AdditionalInfoRasterLayer;
import es.unex.sextante.additionalInfo.AdditionalInfoString;
import es.unex.sextante.additionalInfo.AdditionalInfoTableField;
import es.unex.sextante.additionalInfo.AdditionalInfoVectorLayer;
import es.unex.sextante.core.GeoAlgorithm;
import es.unex.sextante.core.OutputObjectsSet;
import es.unex.sextante.core.ParametersSet;
import es.unex.sextante.core.Sextante;
import es.unex.sextante.dataObjects.IRasterLayer;
import es.unex.sextante.dataObjects.ITable;
import es.unex.sextante.dataObjects.IVectorLayer;
import es.unex.sextante.exceptions.NullParameterAdditionalInfoException;
import es.unex.sextante.outputs.Output;
import es.unex.sextante.outputs.OutputChart;
import es.unex.sextante.outputs.OutputRasterLayer;
import es.unex.sextante.outputs.OutputTable;
import es.unex.sextante.outputs.OutputText;
import es.unex.sextante.outputs.OutputVectorLayer;
import es.unex.sextante.parameters.ParameterBoolean;
import es.unex.sextante.parameters.ParameterFixedTable;
import es.unex.sextante.parameters.ParameterMultipleInput;
import es.unex.sextante.parameters.ParameterNumericalValue;
import es.unex.sextante.parameters.ParameterRasterLayer;
import es.unex.sextante.parameters.ParameterString;
import es.unex.sextante.parameters.ParameterTableField;
import es.unex.sextante.parameters.ParameterVectorLayer;

/**
 * A process factory that wraps a SEXTANTE algorithm and can be used to get information about it and
 * create the corresponding process
 * 
 * @author volaya
 * 
 */
public class SextanteProcessFactory implements ProcessFactory {
    public static final String SEXTANTE_NAMESPACE = "sxt";
    
    private Set<Name> names = new HashSet<Name>();

    /**
     * Constructs a process factory based on the full SEXTANTE algorithms set
     */
    public SextanteProcessFactory() {
        Sextante.initialize();
        
        Set<Name> result = new HashSet<Name>();
        for (String name : (Set<String>) Sextante.getAlgorithms().keySet()) {
            result.add(new NameImpl(SEXTANTE_NAMESPACE, name));
        }
        names = Collections.unmodifiableSet(result);
    }

    public Set<Name> getNames() {
        return names; 
    }
    
    void checkName(Name name) {
        if(name == null)
            throw new NullPointerException("Process name cannot be null");
        if(!names.contains(name))
            throw new IllegalArgumentException("Unknown process '" + name + "'");
    }

    /**
     * Creates a geotools process which wraps a SEXTANTE geoalgorithm
     * 
     * @param alg
     *            the SEXTANTE geoalgorithm to wrap
     * @throws IllegalArgumentException
     */
    public Process create(Name name) throws IllegalArgumentException {
        checkName(name);
        try {
            return new SextanteProcess(Sextante.getAlgorithmFromCommandLineName(name.getLocalPart()));
        } catch (Exception e) {
            throw new RuntimeException("Error occurred cloning the prototype "
                    + "algorithm... this should not happen", e);
        }

    }

    public InternationalString getDescription(Name name) {
        checkName(name);
        return Text.text(Sextante.getAlgorithmFromCommandLineName(name.getLocalPart()).getName());

    }

    public InternationalString getTitle(Name name) {
        return getDescription(name);
    }

    public String getName(Name name) {
        checkName(name);
        GeoAlgorithm algorithm = Sextante.getAlgorithmFromCommandLineName(name.getLocalPart());
        String sClass = algorithm.getClass().getName();
        int iLast = sClass.lastIndexOf(".");
        String sCommandName = sClass.substring(iLast + 1, sClass.length() - "Algorithm".length());
        return "Sextante" + sCommandName;

    }

    public boolean supportsProgress(Name name) {
        checkName(name);
        GeoAlgorithm algorithm = Sextante.getAlgorithmFromCommandLineName(name.getLocalPart());
        return algorithm.isDeterminatedProcess();
    }

    public String getVersion(Name name) {
        checkName(name);
        return "1.0.0";
    }

    public Map<String, Parameter<?>> getParameterInfo(Name name) {
        checkName(name);
        GeoAlgorithm algorithm = Sextante.getAlgorithmFromCommandLineName(name.getLocalPart());

        ParametersSet paramSet = algorithm.getParameters();
        Map<String, Parameter<?>> paramInfo = new HashMap<String, Parameter<?>>();

        for (int i = 0; i < paramSet.getNumberOfParameters(); i++) {
            es.unex.sextante.parameters.Parameter param = paramSet.getParameter(i);
            paramInfo.put(param.getParameterName(), new Parameter(param.getParameterName(),
                    mapToGeoTools(param.getParameterClass()), Text.text(param
                            .getParameterDescription()),
                    Text.text(param.getParameterDescription()), getAdditionalInfoMap(param)));
        }

        return paramInfo;

    }

    /**
     * Map Sextante common types into GeoTools common types
     * 
     * @param parameterClass
     * @return
     */
    protected Class mapToGeoTools(Class parameterClass) {
        if (IVectorLayer.class.isAssignableFrom(parameterClass)) {
            return FeatureCollection.class;
        } else if (IRasterLayer.class.isAssignableFrom(parameterClass)) {
            return GridCoverage.class;
        } else if (ITable.class.isAssignableFrom(parameterClass)) {
            return FeatureCollection.class;
        } else {
            return parameterClass;
        }
    }

    /**
     * Returns a map with additional info about a given parameter. It takes a SEXTANTE parameter and
     * produces a map suitable to be added to a GeoTools parameter
     * 
     * @param param
     *            the parameter to take the additional information from
     * @return a Map with additional info about the parameter. Keys used to identify each element
     *         are defined in {@see SextanteProcessFactoryConstants}
     */
    private Map getAdditionalInfoMap(es.unex.sextante.parameters.Parameter param) {

        HashMap map = new HashMap();

        AdditionalInfo ai;
        try {
            ai = param.getParameterAdditionalInfo();
        } catch (NullParameterAdditionalInfoException e) {
            // we return an empty map if could not access the additional information
            return map;

        }

        if (param instanceof ParameterRasterLayer) {
            AdditionalInfoRasterLayer airl = (AdditionalInfoRasterLayer) ai;
            map.put(PARAMETER_MANDATORY, airl.getIsMandatory());
        }
        if (param instanceof ParameterVectorLayer) {
            AdditionalInfoVectorLayer aivl = (AdditionalInfoVectorLayer) ai;
            map.put(PARAMETER_MANDATORY, aivl.getIsMandatory());
            map.put(SHAPE_TYPE, aivl.getShapeType());
        }
        if (param instanceof ParameterVectorLayer) {
            AdditionalInfoVectorLayer aiv = (AdditionalInfoVectorLayer) ai;
            map.put(PARAMETER_MANDATORY, aiv.getIsMandatory());
        }
        if (param instanceof ParameterString) {
            AdditionalInfoString ais = (AdditionalInfoString) ai;
            map.put(DEFAULT_STRING_VALUE, ais.getDefaultString());
        }
        if (param instanceof ParameterNumericalValue) {
            AdditionalInfoNumericalValue ainv = (AdditionalInfoNumericalValue) ai;
            map.put(DEFAULT_NUMERICAL_VALUE, ainv.getDefaultValue());
            map.put(MAX_NUMERICAL_VALUE, ainv.getMaxValue());
            map.put(MIN_NUMERICAL_VALUE, ainv.getMinValue());
            map.put(NUMERICAL_VALUE_TYPE, ainv.getType());
        }
        if (param instanceof ParameterBoolean) {
            AdditionalInfoBoolean aib = (AdditionalInfoBoolean) ai;
            map.put(DEFAULT_BOOLEAN_VALUE, aib.getDefaultValue());
        }
        if (param instanceof ParameterMultipleInput) {
            AdditionalInfoMultipleInput aimi = (AdditionalInfoMultipleInput) ai;
            map.put(MULTIPLE_INPUT_TYPE, aimi.getDataType());
            map.put(PARAMETER_MANDATORY, aimi.getIsMandatory());
        }
        if (param instanceof ParameterFixedTable) {
            AdditionalInfoFixedTable aift = (AdditionalInfoFixedTable) ai;
            map.put(FIXED_TABLE_NUM_COLS, aift.getColsCount());
            map.put(FIXED_TABLE_NUM_ROWS, aift.getRowsCount());
            map.put(FIXED_TABLE_FIXED_NUM_ROWS, aift.isNumberOfRowsFixed());
        }
        if (param instanceof ParameterTableField) {
            AdditionalInfoTableField aitf = (AdditionalInfoTableField) ai;
            map.put(PARENT_PARAMETER_NAME, aitf.getParentParameterName());
        }

        return map;

    }

    public Map<String, Parameter<?>> getResultInfo(Name name, Map<String, Object> inputs)
            throws IllegalArgumentException {
        checkName(name);
        GeoAlgorithm algorithm = Sextante.getAlgorithmFromCommandLineName(name.getLocalPart());

        Class outputClass = null;
        OutputObjectsSet ooset = algorithm.getOutputObjects();
        Map<String, Parameter<?>> outputInfo = new HashMap<String, Parameter<?>>();

        for (int i = 0; i < ooset.getOutputObjectsCount(); i++) {
            Output output = ooset.getOutput(i);

            if (output instanceof OutputVectorLayer) {
                outputClass = FeatureCollection.class;
            } else if (output instanceof OutputRasterLayer) {
                outputClass = GridCoverage.class;
            }
            if (output instanceof OutputTable) {
                outputClass = FeatureCollection.class;
            }
            if (output instanceof OutputChart) {
                outputClass = ChartPanel.class;
            }
            if (output instanceof OutputText) {
                outputClass = String.class;
            }

            outputInfo.put(output.getName(), new Parameter(output.getName(), outputClass, Text
                    .text(output.getDescription()), Text.text(output.getDescription())));
        }

        return outputInfo;

    }

    @Override
    public String toString() {
        return "SextanteFactory";
    }
}
