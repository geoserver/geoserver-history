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
package org.geoserver.wps.jts;

import java.awt.RenderingHints.Key;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.geotools.data.Parameter;
import org.geotools.feature.NameImpl;
import org.geotools.process.Process;
import org.geotools.process.ProcessException;
import org.geotools.process.ProcessFactory;
import org.geotools.util.Converters;
import org.geotools.util.SimpleInternationalString;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

import es.unex.sextante.gui.core.ProgressMonitor;

/**
 * Grabbed from Geotools and generalized a bit, should go back into GeoTools
 * once improved enough. ProcessFactory for classes exposing simple processes as
 * static methods
 * 
 * @since 2.7
 */
public class StaticMethodsProcessFactory<T> implements ProcessFactory {
	Class<T> targetClass;
	String namespace;
	InternationalString title;

	public StaticMethodsProcessFactory(Class<T> targetClass,
			InternationalString title, String namespace) {
		this.targetClass = targetClass;
		this.namespace = namespace;
		this.title = title;
	}

	public Process create(Name name) {
		return new ProcessInvocation(name.getLocalPart());
	}

	public InternationalString getTitle() {
		return title;
	}

	public InternationalString getDescription(Name name) {
		DescribeProcess info = getProcessDescription(name);
		if (info != null) {
			return new SimpleInternationalString(info.description());
		} else {
			return null;
		}
	}

	/**
	 * Finds the DescribeProcess description for the specified name
	 * 
	 * @param name
	 * @return
	 */
	DescribeProcess getProcessDescription(Name name) {
		Method method = method(name.getLocalPart());
		if (method == null) {
			return null;
		}
		DescribeProcess info = method.getAnnotation(DescribeProcess.class);
		return info;
	}

	public Method method(String name) {
		for (Method method : targetClass.getMethods()) {
			if (name.equalsIgnoreCase(method.getName())) {
				DescribeProcess dp = method
						.getAnnotation(DescribeProcess.class);
				if (dp != null) {
					return method;
				}
			}
		}
		return null;
	}

	public Set<Name> getNames() {
		// look for the methods that have the DescribeProcess annotation. use
		// a linkedHashSet to make sure we don't report duplicate names
		Set<Name> names = new LinkedHashSet<Name>();
		for (Method method : targetClass.getMethods()) {
			DescribeProcess dp = method.getAnnotation(DescribeProcess.class);
			if (dp != null) {
				Name name = new NameImpl(namespace, method.getName());
				if (names.contains(name)) {
					throw new IllegalStateException(
							targetClass.getName()
									+ " has two methods named "
									+ method.getName()
									+ ", both annotated with DescribeProcess, this is an ambiguity. "
									+ "Please a different name");
				}
				names.add(name);
			}
		}
		return names;
	}

	public Map<String, Parameter<?>> getParameterInfo(Name name) {
		// build the parameter descriptions by using the DescribeParameter
		// annotations
		Method method = method(name.getLocalPart());
		Map<String, Parameter<?>> input = new LinkedHashMap<String, Parameter<?>>();
		Annotation[][] params = method.getParameterAnnotations();
		Class<?>[] paramTypes = method.getParameterTypes();
		for (int i = 0; i < paramTypes.length; i++) {
			Parameter<?> param = paramInfo(i, paramTypes[i], params[i]);
			input.put(param.key, param);
		}
		return input;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Parameter<?>> getResultInfo(Name name,
			Map<String, Object> parameters) throws IllegalArgumentException {
		Method method = method(name.getLocalPart());
		// look for the DescribeResult annotations (the result could be a
		// key/value
		// map holding multiple results)
		Map<String, Parameter<?>> result = new LinkedHashMap<String, Parameter<?>>();
		for (Annotation annotation : method.getAnnotations()) {
			if (annotation instanceof DescribeResult) {
				DescribeResult info = (DescribeResult) annotation;
				// see if a type has been declared, otherwise use the annotation
				Class resultType = info.type();
				if (Object.class.equals(resultType)) {
					resultType = method.getReturnType();
				}
				Parameter<?> resultParam = new Parameter(info.name(),
						resultType, info.name(), info.description());
				result.put(resultParam.key, resultParam);
			}
		}
		// if annotation is not found, return a generic description using
		// the method return type
		if (result.isEmpty()) {
			if (!Void.class.equals(method.getReturnType())) {
				Parameter<?> VALUE = new Parameter("result", method
						.getReturnType(), "Process result",
						"No description is available");
				result.put(VALUE.key, VALUE);
			}
		}
		return result;
	}

	public InternationalString getTitle(Name name) {
		DescribeProcess info = getProcessDescription(name);
		if (info != null) {
			return new SimpleInternationalString(info.description());
		} else {
			return null;
		}
	}

	public String getVersion(Name name) {
		DescribeProcess info = getProcessDescription(name);
		if (info != null) {
			return info.version();
		} else {
			return null;
		}
	}

	public boolean supportsProgress(Name name) {
		return false;
	}

	public boolean isAvailable() {
		return true;
	}

	public Map<Key, ?> getImplementationHints() {
		return null;
	}

	@SuppressWarnings("unchecked")
	Parameter<?> paramInfo(int i, Class<?> type, Annotation[] paramAnnotations) {
		DescribeParameter info = null;
		for (Annotation annotation : paramAnnotations) {
			if (annotation instanceof DescribeParameter) {
				info = (DescribeParameter) annotation;
				break;
			}
		}
		// handle collection type and multiplicity
		boolean collection = Collection.class.isAssignableFrom(type);
		int min = 1;
		int max = 1;
		if (collection) {
			if (info != null) {
				type = info.collectionType();
				if (type == null) {
					type = Object.class;
				}
				min = info.min() > -1 ? info.min() : 0;
				max = info.max() > -1 ? info.max() : Integer.MAX_VALUE;
			} else {
				type = Object.class;
				min = 0;
				max = Integer.MAX_VALUE;
			}
		} else if (type.isArray()) {
			if (info != null) {
				min = info.min() > -1 ? info.min() : 0;
				max = info.max() > -1 ? info.max() : Integer.MAX_VALUE;
			} else {
				min = 0;
				max = Integer.MAX_VALUE;
			}
			type = type.getComponentType();
		} else {
			if (info != null) {
				if (info.min() > 1) {
					throw new IllegalArgumentException(
							"The non collection parameter at index " + i
									+ " cannot have a min multiplicity > 1");
				}
				min = info.min() > -1 ? info.min() : 1;
				if (info.max() > 1) {
					throw new IllegalArgumentException(
							"The non collection parameter at index " + i
									+ " cannot have a max multiplicity > 1");
				}
				max = info.max() > -1 ? info.max() : 1;
			}
		}
		if (min > max) {
			throw new IllegalArgumentException(
					"Min occurrences > max occurrences for parameter at index "
							+ i);
		}
		// finally build the parameter
		if (info != null) {
			return new Parameter(info.name(), type,
					new SimpleInternationalString(info.name()),
					new SimpleInternationalString(info.description()), min > 0,
					min, max, null, null);
		} else {
			return new Parameter("arg" + i, type,
					new SimpleInternationalString("Argument " + i),
					new SimpleInternationalString("Input " + type.getName()
							+ " value"), min > 0, min, max, null, null);
		}
	}

	/**
	 * Executes the method as a process
	 */
	class ProcessInvocation implements Process {

		Method method;

		public ProcessInvocation(String name) {
			method = method(name);
			if (method == null) {
				throw new IllegalArgumentException("Could not find method "
						+ name + " in class " + targetClass);
			}
		}

		@SuppressWarnings("unchecked")
		public Map<String, Object> execute(Map<String, Object> input,
				ProgressListener monitor) throws ProcessException {

			// build the array of arguments we'll use to invoke the method
			Class<?>[] paramTypes = method.getParameterTypes();
			Annotation[][] annotations = method.getParameterAnnotations();
			Object args[] = new Object[paramTypes.length];
			for (int i = 0; i < args.length; i++) {
				if (ProgressMonitor.class.equals(paramTypes[i])) {
					// pass in the monitor
					args[i] = monitor;
				} else {
					// find the corresponding argument in the input
					// map and set it
					Parameter p = paramInfo(i, paramTypes[i], annotations[i]);
					Object value = input.get(p.key);
					
					// this takes care of array/collection conversions among
					// others
					args[i] = Converters.convert(value, paramTypes[i]);
					// check the conversion was successful
					if(args[i] == null && value != null) {
						throw new ProcessException("Could not convert " + value 
								+ " to target type " + paramTypes[i].getName());
					}
					
					// check multiplicity is respected
					if(p.minOccurs > 0 && value == null) {
						throw new ProcessException("Parameter " + p.key + " is missing but has min multiplicity > 0");
					} else if(p.maxOccurs > 1) {
						int size = -1;
						if(paramTypes[i].isArray()) {
							size = Array.getLength(args[i]);
						} else {
							size = ((Collection) args[i]).size();
						}
						if(size < p.minOccurs) {
							throw new ProcessException("Parameter " + p.key + " has " + size 
									+ " elements but min occurrences is " + p.minOccurs);
						}
						if(size > p.maxOccurs) {
							throw new ProcessException("Parameter " + p.key + " has " + size 
									+ " elements but max occurrences is " + p.maxOccurs);
						}
					}
				}
			}

			// invoke and grab result
			Object value = null;
			try {
				value = method.invoke(null, args);
			} catch (Throwable t) {
				// report the exception and exit
				if (monitor != null) {
					monitor.exceptionOccurred(t);
				}
				throw new ProcessException(t);
			}

			// build up the result
			if (value instanceof Object[]) {
				// handle the case the implementor used a positional array for
				// returning multiple outputs
				Object values[] = (Object[]) value;
				Map<String, Object> result = new LinkedHashMap<String, Object>();
				int i = 0;
				for (Annotation annotation : method.getAnnotations()) {
					if (i >= values.length)
						break; // no more values to encode
					Object obj = values[i];

					if (annotation instanceof DescribeResult) {
						DescribeResult info = (DescribeResult) annotation;
						if (info.type().isInstance(obj)) {
							result.put(info.name(), obj);
						} else {
							throw new IllegalArgumentException(method.getName()
									+ " unable to encode result " + obj
									+ " as " + info.type());
						}
					}
				}
				return result;
			} else if (value instanceof Map) {
				// handle the case where a map was used instead
				return (Map<String, Object>) value;
			} else if (!Void.class.equals(method.getReturnType())) {
				// handle the single result case
				Map<String, Object> result = new LinkedHashMap<String, Object>();
				DescribeResult dr = method.getAnnotation(DescribeResult.class);
				if (dr != null) {
					result.put(dr.name(), value);
				} else {
					result.put("result", value);
				}
				return result;
			}
			// handle the case where the method returns void
			return null;
		}
	}

}
