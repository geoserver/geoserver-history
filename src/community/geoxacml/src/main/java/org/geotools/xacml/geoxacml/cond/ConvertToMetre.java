/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */


package org.geotools.xacml.geoxacml.cond;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.xacml.EvaluationCtx;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.attr.DoubleAttribute;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.cond.EvaluationResult;
/*
 * LENGTH
To convert from 							to 					Multiply by
ångström(Å) 								meter (m) 			1.0 	E-10
ångström(Å) 								nanometer (nm) 		1.0 	E-01
astronomical unit (ua) 						meter (m) 			1.495 979   	E+11
chain (based on U.S. survey foot) (ch) 7   	meter (m) 			2.011 684 	E+01
fathom (based on U.S. survey foot) 7   		meter (m) 			1.828 804 	E+00
fermi 										meter (m) 			1.0 	E-15
fermi 										femtometer (fm) 	1.0 	E+00
foot (ft) 									meter (m) 			3.048 	E-01
foot (U.S. survey) (ft) 7   				meter (m) 			3.048 006 	E-01
inch (in) 									meter (m) 			2.54 	E-02
inch (in) 									centimeter (cm) 	2.54 	E+00
kayser(K) 									reciprocal meter (m-1)   	1 	E+02
light year (l. y.) 18   					meter (m) 			9.460 73 	E+15
microinch 									meter (m) 			2.54 	E-08
microinch 									micrometer (μm) 	2.54 	E-02
micron (μ) 									meter (m) 			1.0 	E-06
micron (μ) 									micrometer (μm) 	1.0 	E+00
mil (0.001 in) 								meter (m) 			2.54 	E-05
mil (0.001 in) 								millimeter (mm) 	2.54 	E-02
mile (mi) 									meter (m) 			1.609 344 	E+03
mile (mi) 									kilometer (km) 		1.609 344 	E+00
mile (based on U.S. survey foot) (mi) 7   	meter (m) 			1.609 347 	E+03
mile (based on U.S. survey foot) (mi) 7   	kilometer (km) 		1.609 347 	E+00
mile, nautical 20   						meter (m) 			1.852 	E+03
parsec (pc) 								meter (m) 			3.085 678 	E+16
pica (computer) (1/6 in) 					meter (m) 			4.233 333 	E-03
pica (computer) (1/6 in) 					millimeter (mm) 	4.233 333 	E+00
pica (printer's) 							meter (m) 			4.217 518 	E-03
pica (printer's) 							millimeter (mm) 	4.217 518 	E+00
point (computer) (1/72 in) 					meter (m) 			3.527 778 	E-04
point (computer) (1/72 in) 					millimeter (mm) 	3.527 778 	E-01
point (printer's) 							meter (m) 			3.514 598 	E-04
point (printer's) 							millimeter (mm) 	3.514 598 	E-01
rod (based on U.S. survey foot) (rd) 7   	meter (m) 			5.029 210 	E+00
yard (yd) 									meter (m) 			9.144 	E-01

 * 
 */

/**
 * Converts untis to metres
 * 
 * @author Christian Mueller
 *
 */
public class ConvertToMetre extends ConvertFunction {

	public static final String NAME= NAME_PREFIX+"convert-to-metre";

	private static Map<String,Double> UnitToMetre;
	
	static {
		UnitToMetre = new HashMap<String,Double>();
		UnitToMetre.put("ångström",1.0E-10);
		UnitToMetre.put("Å",1.0E-10);
		UnitToMetre.put("ua",1.495979E+11);
		UnitToMetre.put("chain",2.011684E+01);
		UnitToMetre.put("fathom",1.828804E+00);
		UnitToMetre.put("fermi",1.0E-15);
		UnitToMetre.put("ft",3.048E-01);
		UnitToMetre.put("ft 7",3.048006E-01);
		UnitToMetre.put("in",2.54E-02);
		UnitToMetre.put("K",1E+02);
		UnitToMetre.put("light year",9.46073E+15);
		UnitToMetre.put("microinch",2.54E-08);
		UnitToMetre.put("micron",1.0E-06);
		UnitToMetre.put("μ",1.0E-06);
		UnitToMetre.put("mil",2.54E-05);
		UnitToMetre.put("mi",1.609344E+03);
		UnitToMetre.put("mi 7",1.609347E+03);
		UnitToMetre.put("mile nautical",1.852E+03);
		UnitToMetre.put("pc",3.085678E+16);
		
		UnitToMetre.put("pica computer",4.233333E-03);
		UnitToMetre.put("pica printer",4.217518E-03);
		UnitToMetre.put("point computer",3527778E-04);
		UnitToMetre.put("point printer",3.514598E-04);
		UnitToMetre.put("rd 7",5.029210E+00);
		UnitToMetre.put("yd",9.144E-01);
	}

    public ConvertToMetre() {        
        super(NAME);
              
    }

	
	public EvaluationResult evaluate(List inputs, EvaluationCtx context) {
		
		   AttributeValue [] argValues = new AttributeValue[inputs.size()];
           EvaluationResult result = evalArgs(inputs, context, argValues);
           if (result != null)
               return result;

                      
           DoubleAttribute value =(DoubleAttribute)(argValues[0]);
           StringAttribute unit =(StringAttribute)(argValues[1]);
           
           
           Double multiplyBy= UnitToMetre.get(unit.getValue());
           if (multiplyBy==null) {
        	   exceptionError(new Exception("Unit" + unit + " not supported"));
           }
           double resultValue =value.getValue()*multiplyBy;



           return new EvaluationResult(new DoubleAttribute(resultValue));		
	}

}
