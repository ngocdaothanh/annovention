package com.impetus.annovention.util;

/**
 * This class extracts Method Parameters and their annotations given a Javassist MethodInfo object
 *
 * @author David Bennett - dbennett455@gmail.com
 *
 */

import java.util.ArrayList;
import java.util.List;

import javassist.bytecode.MethodInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;


public class MethodParameters {

	/**
	 * These returns the method parameter information only if there is one or more parameter annotations.
	 * Otherwise returns an empty list.
	 *
	 * @param methodInfo
	 * @param visible
	 * @param invisible
	 * @return
	 */
	public static List<MethodParameter> getMethodParameters(MethodInfo methodInfo, boolean visible, boolean invisible) {
		List<MethodParameter> ret = new ArrayList<MethodParameter>();

		List<String> types=parameterTypes(methodInfo.getDescriptor());

		//  create method parameter objects
		int i=0;
		for (String type : types) {
			ret.add(new MethodParameter(i++, type));
		}

		// scan the methods parameter annotations
		Annotation[][] aaVisibleA=null;
		Annotation[][] aaInvisibleA=null;

        if (visible) {
        	ParameterAnnotationsAttribute visibleA = (ParameterAnnotationsAttribute) methodInfo.getAttribute(ParameterAnnotationsAttribute.visibleTag);
            if (visibleA != null) aaVisibleA = visibleA.getAnnotations();
        }

        if (invisible) {
        	ParameterAnnotationsAttribute invisibleA = (ParameterAnnotationsAttribute) methodInfo.getAttribute(ParameterAnnotationsAttribute.invisibleTag);
            if (invisibleA != null) aaInvisibleA = invisibleA.getAnnotations();
        }

        int length=0;
        if (aaVisibleA != null) length += aaVisibleA.length;
        if (aaInvisibleA != null) length += aaInvisibleA.length;


        if (length > 0) {
			Annotation[][] aaParameterAnnotations = new Annotation[length][];
			if (aaVisibleA != null)
				System.arraycopy(aaVisibleA, 0, aaParameterAnnotations, 0, aaVisibleA.length);
			if (aaInvisibleA != null)
				System.arraycopy(aaInvisibleA, 0, aaParameterAnnotations, 0, aaInvisibleA.length);
			for (int parameterIndex=0; parameterIndex<aaParameterAnnotations.length; parameterIndex++) {
				List<Annotation> annotations=new ArrayList<Annotation>();
				for (int annotationIndex=0; annotationIndex<aaParameterAnnotations[parameterIndex].length; annotationIndex++) {
					Annotation annotation=aaParameterAnnotations[parameterIndex][annotationIndex];
					if (annotation != null) {
						annotations.add(annotation);
						//System.out.println("method: " + methodInfo.getName() + methodInfo.getDescriptor() + " parameter[" + parameterIndex + "] parameter annotation["+annotationIndex+"]: "+annotation.toString());
					}
				}
				// set the annotations in our array
				ret.get(parameterIndex).setAnnotations(annotations);
			}
        }
		return(ret);
	}


	/**
	 * Return normal parameter type declarations from Java Field Descriptor of parameter.
	 * There is probably some way to generate this from the bytecode in a CfBehaivor,
	 * However,  I was unable to access it from the MethodInfo object.  So instead we
	 * parse the bytecode Field Descriptor returned from MethodInfo.getDescriptor()
	 *
	 * @author David Bennett - dbennett455@gmail.com
	 *
	 * @param descriptor
	 * @return List<String> list of java parameter types
	 */
	public static List<String> parameterTypes(String descriptor) {
		List<String> ret=new ArrayList<String>();
		StringBuffer remainder=null;
		// look for parameter designator
		// get a list of the method types
		if (descriptor != null && descriptor.indexOf("(") > -1) {
			remainder=new StringBuffer(descriptor.substring(
				descriptor.indexOf("(")+1,
				descriptor.indexOf(")")));
		}  else
			// it's just a raw field designator string
			remainder = new StringBuffer(descriptor);
		// loop through descriptor adding all of the found field designators
		while (remainder != null && remainder.length() > 0) {
			String strType="unknown";
			int ad, idx;
			char type;
			// array dim count
			for (ad=0; remainder.charAt(ad) == '['; ad++);
            // decode next type
			idx=ad;
			type = remainder.charAt(idx);
			// object
			if (type == 'L') {
				idx++;
				int typeLen=remainder.indexOf(";");
				strType=remainder.substring(idx, typeLen).replace("/", ".");
				idx+=typeLen;
			// primitive
			} else if ("BCDFIJSZ".indexOf(type) > -1) {
				strType=primType(type);
				idx++;
			}
			strType+=new String(new char[ad]).replace("\0", "[]");
			ret.add(strType);
			idx-=ad;
			while (idx-- > 0) remainder.deleteCharAt(0);
		}
		return ret;
	}

	/**
	 * Return readable java primitive type from bytecode field descriptor character
	 *
	 * @param typ char primitive type
	 * @return String full type
	 */
	public static String primType(char typ) {
		String ret="unknown";
		switch (typ) {
	    case 'B':
	        ret = "byte";
	        break;
	      case 'C':
	        ret = "char";
	        break;
	      case 'D':
	        ret = "double";
	        break;
	      case 'F':
	        ret = "float";
	        break;
	      case 'I':
	        ret = "int";
	        break;
	      case 'J':
	        ret = "long";
	        break;
	      case 'S':
	        ret = "short";
	        break;
	      case 'Z':
	        ret = "boolean";
	        break;
		}
		return ret;
	}


}
