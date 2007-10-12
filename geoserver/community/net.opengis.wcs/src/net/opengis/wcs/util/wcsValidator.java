/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.util;

import java.math.BigDecimal;

import java.util.List;
import java.util.Map;

import javax.xml.datatype.Duration;

import net.opengis.wcs.*;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.eclipse.emf.ecore.xml.type.util.XMLTypeUtil;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see net.opengis.wcs.wcsPackage
 * @generated
 */
public class wcsValidator extends EObjectValidator {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final wcsValidator INSTANCE = new wcsValidator();

    /**
     * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.Diagnostic#getSource()
     * @see org.eclipse.emf.common.util.Diagnostic#getCode()
     * @generated
     */
    public static final String DIAGNOSTIC_SOURCE = "net.opengis.wcs";

    /**
     * A constant with a fixed name that can be used as the base value for additional hand written constants.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

    /**
     * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

    /**
     * The cached base package validator.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected XMLTypeValidator xmlTypeValidator;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public wcsValidator() {
        super();
        xmlTypeValidator = XMLTypeValidator.INSTANCE;
    }

    /**
     * Returns the package of this validator switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EPackage getEPackage() {
      return wcsPackage.eINSTANCE;
    }

    /**
     * Calls <code>validateXXX</code> for the corresponding classifier of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map context) {
        switch (classifierID) {
            case wcsPackage.AVAILABLE_KEYS_TYPE:
                return validateAvailableKeysType((AvailableKeysType)value, diagnostics, context);
            case wcsPackage.AXIS_SUBSET_TYPE:
                return validateAxisSubsetType((AxisSubsetType)value, diagnostics, context);
            case wcsPackage.AXIS_TYPE:
                return validateAxisType((AxisType)value, diagnostics, context);
            case wcsPackage.CAPABILITIES_TYPE:
                return validateCapabilitiesType((CapabilitiesType)value, diagnostics, context);
            case wcsPackage.CONTENTS_TYPE:
                return validateContentsType((ContentsType)value, diagnostics, context);
            case wcsPackage.COVERAGE_DESCRIPTIONS_TYPE:
                return validateCoverageDescriptionsType((CoverageDescriptionsType)value, diagnostics, context);
            case wcsPackage.COVERAGE_DESCRIPTION_TYPE:
                return validateCoverageDescriptionType((CoverageDescriptionType)value, diagnostics, context);
            case wcsPackage.COVERAGE_DOMAIN_TYPE:
                return validateCoverageDomainType((CoverageDomainType)value, diagnostics, context);
            case wcsPackage.COVERAGE_SUMMARY_TYPE:
                return validateCoverageSummaryType((CoverageSummaryType)value, diagnostics, context);
            case wcsPackage.DESCRIBE_COVERAGE_TYPE:
                return validateDescribeCoverageType((DescribeCoverageType)value, diagnostics, context);
            case wcsPackage.DOCUMENT_ROOT:
                return validateDocumentRoot((DocumentRoot)value, diagnostics, context);
            case wcsPackage.DOMAIN_SUBSET_TYPE:
                return validateDomainSubsetType((DomainSubsetType)value, diagnostics, context);
            case wcsPackage.FIELD_SUBSET_TYPE:
                return validateFieldSubsetType((FieldSubsetType)value, diagnostics, context);
            case wcsPackage.FIELD_TYPE:
                return validateFieldType((FieldType)value, diagnostics, context);
            case wcsPackage.GET_CAPABILITIES_TYPE:
                return validateGetCapabilitiesType((GetCapabilitiesType)value, diagnostics, context);
            case wcsPackage.GET_COVERAGE_TYPE:
                return validateGetCoverageType((GetCoverageType)value, diagnostics, context);
            case wcsPackage.GRID_CRS_TYPE:
                return validateGridCrsType((GridCrsType)value, diagnostics, context);
            case wcsPackage.IMAGE_CRS_REF_TYPE:
                return validateImageCRSRefType((ImageCRSRefType)value, diagnostics, context);
            case wcsPackage.OUTPUT_TYPE:
                return validateOutputType((OutputType)value, diagnostics, context);
            case wcsPackage.RANGE_SUBSET_TYPE:
                return validateRangeSubsetType((RangeSubsetType)value, diagnostics, context);
            case wcsPackage.RANGE_TYPE:
                return validateRangeType((RangeType)value, diagnostics, context);
            case wcsPackage.REQUEST_BASE_TYPE:
                return validateRequestBaseType((RequestBaseType)value, diagnostics, context);
            case wcsPackage.SPATIAL_DOMAIN_TYPE:
                return validateSpatialDomainType((SpatialDomainType)value, diagnostics, context);
            case wcsPackage.TIME_PERIOD_TYPE:
                return validateTimePeriodType((TimePeriodType)value, diagnostics, context);
            case wcsPackage.TIME_SEQUENCE_TYPE:
                return validateTimeSequenceType((TimeSequenceType)value, diagnostics, context);
            case wcsPackage.IDENTIFIER_TYPE:
                return validateIdentifierType((String)value, diagnostics, context);
            case wcsPackage.TIME_DURATION_TYPE:
                return validateTimeDurationType(value, diagnostics, context);
            default: 
                return true;
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAvailableKeysType(AvailableKeysType availableKeysType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(availableKeysType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAxisSubsetType(AxisSubsetType axisSubsetType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(axisSubsetType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAxisType(AxisType axisType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(axisType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCapabilitiesType(CapabilitiesType capabilitiesType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(capabilitiesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateContentsType(ContentsType contentsType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(contentsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCoverageDescriptionsType(CoverageDescriptionsType coverageDescriptionsType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(coverageDescriptionsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCoverageDescriptionType(CoverageDescriptionType coverageDescriptionType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(coverageDescriptionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCoverageDomainType(CoverageDomainType coverageDomainType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(coverageDomainType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCoverageSummaryType(CoverageSummaryType coverageSummaryType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(coverageSummaryType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDescribeCoverageType(DescribeCoverageType describeCoverageType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(describeCoverageType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDocumentRoot(DocumentRoot documentRoot, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(documentRoot, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDomainSubsetType(DomainSubsetType domainSubsetType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(domainSubsetType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFieldSubsetType(FieldSubsetType fieldSubsetType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(fieldSubsetType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFieldType(FieldType fieldType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(fieldType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGetCapabilitiesType(GetCapabilitiesType getCapabilitiesType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(getCapabilitiesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGetCoverageType(GetCoverageType getCoverageType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(getCoverageType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGridCrsType(GridCrsType gridCrsType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(gridCrsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateImageCRSRefType(ImageCRSRefType imageCRSRefType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(imageCRSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOutputType(OutputType outputType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(outputType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRangeSubsetType(RangeSubsetType rangeSubsetType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(rangeSubsetType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRangeType(RangeType rangeType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(rangeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRequestBaseType(RequestBaseType requestBaseType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(requestBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSpatialDomainType(SpatialDomainType spatialDomainType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(spatialDomainType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimePeriodType(TimePeriodType timePeriodType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(timePeriodType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeSequenceType(TimeSequenceType timeSequenceType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(timeSequenceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIdentifierType(String identifierType, DiagnosticChain diagnostics, Map context) {
        boolean result = validateIdentifierType_Pattern(identifierType, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateIdentifierType_Pattern
     */
    public static final  PatternMatcher [][] IDENTIFIER_TYPE__PATTERN__VALUES =
        new PatternMatcher [][] {
            new PatternMatcher [] {
                XMLTypeUtil.createPatternMatcher(".+")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Identifier Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIdentifierType_Pattern(String identifierType, DiagnosticChain diagnostics, Map context) {
        return validatePattern(wcsPackage.Literals.IDENTIFIER_TYPE, identifierType, IDENTIFIER_TYPE__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeDurationType(Object timeDurationType, DiagnosticChain diagnostics, Map context) {
        boolean result = validateTimeDurationType_MemberTypes(timeDurationType, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Time Duration Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeDurationType_MemberTypes(Object timeDurationType, DiagnosticChain diagnostics, Map context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (XMLTypePackage.Literals.DURATION.isInstance(timeDurationType)) {
                if (xmlTypeValidator.validateDuration((Duration)timeDurationType, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.DECIMAL.isInstance(timeDurationType)) {
                if (xmlTypeValidator.validateDecimal((BigDecimal)timeDurationType, tempDiagnostics, context)) return true;
            }
            List children = tempDiagnostics.getChildren();
            for (int i = 0; i < children.size(); i++) {
                diagnostics.add((Diagnostic)children.get(i));
            }
        }
        else {
            if (XMLTypePackage.Literals.DURATION.isInstance(timeDurationType)) {
                if (xmlTypeValidator.validateDuration((Duration)timeDurationType, null, context)) return true;
            }
            if (XMLTypePackage.Literals.DECIMAL.isInstance(timeDurationType)) {
                if (xmlTypeValidator.validateDecimal((BigDecimal)timeDurationType, null, context)) return true;
            }
        }
        return false;
    }

} //wcsValidator
