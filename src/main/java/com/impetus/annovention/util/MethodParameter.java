package com.impetus.annovention.util;

/**
 * This class represents a Method Parameter and it's annotations (if any)
 *
 * @author David Bennett - dbennett455@gmail.com
 */

import java.util.List;

import javassist.bytecode.annotation.Annotation;

public class MethodParameter {

	private int index=0;
	private String type=null;
	private List<Annotation> annotations;

	public MethodParameter(int index, String type) {
		super();
		this.index = index;
		this.type = type;
	}

	public MethodParameter setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
		return this;
	}

	public int getIndex() {
		return index;
	}

	public String getType() {
		return type;
	}

	public List<Annotation> getAnnotations() {
		return annotations;
	}

}
