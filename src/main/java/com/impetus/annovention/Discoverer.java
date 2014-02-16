/*
 * Copyright 2010 Impetus Infotech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.impetus.annovention;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;

import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;
import com.impetus.annovention.listener.ClassAnnotationObjectDiscoveryListener;
import com.impetus.annovention.listener.FieldAnnotationDiscoveryListener;
import com.impetus.annovention.listener.FieldAnnotationObjectDiscoveryListener;
import com.impetus.annovention.listener.MethodAnnotationDiscoveryListener;
import com.impetus.annovention.listener.MethodAnnotationObjectDiscoveryListener;
import com.impetus.annovention.listener.MethodParameterAnnotationDiscoveryListener;
import com.impetus.annovention.listener.MethodParameterAnnotationObjectDiscoveryListener;
import com.impetus.annovention.resource.ClassFileIterator;
import com.impetus.annovention.resource.JarFileIterator;
import com.impetus.annovention.resource.ResourceIterator;
import com.impetus.annovention.util.MethodParameter;
import com.impetus.annovention.util.MethodParameters;

/**
 * Base annotation discoverer.
 *
 * @author animesh.kumar
 */
public abstract class Discoverer {

    /** map to hold ClassAnnotation listeners */
    private final Map<String, Set<ClassAnnotationDiscoveryListener>> classAnnotationListeners =
        new HashMap<String, Set<ClassAnnotationDiscoveryListener>>();

    /** map to hold FieldAnnotation listeners */
    private final Map<String, Set<FieldAnnotationDiscoveryListener>> fieldAnnotationListeners =
        new HashMap<String, Set<FieldAnnotationDiscoveryListener>>();

    /** map to hold MethodAnnotation listeners */
    private final Map<String, Set<MethodAnnotationDiscoveryListener>> methodAnnotationListeners =
        new HashMap<String, Set<MethodAnnotationDiscoveryListener>>();

    /** map to hold MethodParameterAnnotation listeners */
    private final Map<String, Set<MethodParameterAnnotationDiscoveryListener>> methodParameterAnnotationListeners =
        new HashMap<String, Set<MethodParameterAnnotationDiscoveryListener>>();

    /** map to hold ClassAnnotationObject listeners */
    private static final Map<String, Set<ClassAnnotationObjectDiscoveryListener>> classAnnotationObjectListeners =
    	new HashMap<String, Set<ClassAnnotationObjectDiscoveryListener>>();

    /** map to hold FieldAnnotationObject listeners */
    private static final Map<String, Set<FieldAnnotationObjectDiscoveryListener>> fieldAnnotationObjectListeners =
    	new HashMap<String, Set<FieldAnnotationObjectDiscoveryListener>>();

    /** map to hold MethodAnnotationObject listeners */
    private static final Map<String, Set<MethodAnnotationObjectDiscoveryListener>> methodAnnotationObjectListeners =
    	new HashMap<String, Set<MethodAnnotationObjectDiscoveryListener>>();

    /** map to hold MethodAnnotationObject listeners */
    private static final Map<String, Set<MethodParameterAnnotationObjectDiscoveryListener>> methodParameterAnnotationObjectListeners =
    	new HashMap<String, Set<MethodParameterAnnotationObjectDiscoveryListener>>();

    /**
     * Instantiates a new Discoverer.
     */
    public Discoverer() {
    }

    /**
     * Adds ClassAnnotationDiscoveryListener
     *
     * @param listener
     */
    public final void addAnnotationListener (ClassAnnotationDiscoveryListener listener) {
        addAnnotationListener (classAnnotationListeners, listener, listener.supportedAnnotations());
    }

    /**
     * Adds FieldAnnotationDiscoveryListener
     *
     * @param listener
     */
    public final void addAnnotationListener (FieldAnnotationDiscoveryListener listener) {
        addAnnotationListener (fieldAnnotationListeners, listener, listener.supportedAnnotations());
    }

    /**
     * Adds MethodAnnotationDiscoveryListener
     *
     * @param listener
     */
    public final void addAnnotationListener (MethodAnnotationDiscoveryListener listener) {
        addAnnotationListener (methodAnnotationListeners, listener, listener.supportedAnnotations());
    }

    /**
     * Adds MethodParameterAnnotationDiscoveryListener
     *
     * @param listener
     */
    public final void addAnnotationListener (MethodParameterAnnotationDiscoveryListener listener) {
        addAnnotationListener (methodParameterAnnotationListeners, listener, listener.supportedAnnotations());
    }

    /**
     * Adds ClassAnnotationObjectDiscoveryListener
     *
     * @param listener
     */
    public final void addAnnotationListener (ClassAnnotationObjectDiscoveryListener listener) {
    	addAnnotationListener (classAnnotationObjectListeners, listener, listener.supportedAnnotations());
    }

    /**
     * Adds FieldAnnotationObjectDiscoveryListener
     *
     * @param listener
     */
    public final void addAnnotationListener (FieldAnnotationObjectDiscoveryListener listener) {
    	addAnnotationListener (fieldAnnotationObjectListeners, listener, listener.supportedAnnotations());
    }

    /**
     * Adds MethodAnnotationObjectDiscoveryListener
     *
     * @param listener
     */
    public final void addAnnotationListener (MethodAnnotationObjectDiscoveryListener listener) {
    	addAnnotationListener (methodAnnotationObjectListeners, listener, listener.supportedAnnotations());
    }

    /**
     * Adds MethodParameterAnnotationObjectDiscoveryListener
     *
     * @param listener
     */
    public final void addAnnotationListener (MethodParameterAnnotationObjectDiscoveryListener listener) {
    	addAnnotationListener (methodParameterAnnotationObjectListeners, listener, listener.supportedAnnotations());
    }

    /**
     * Helper class to find supported annotations of a listener and register them
     *
     * @param <L>
     * @param map
     * @param listener
     * @param annotations
     */
    private <L> void addAnnotationListener (Map<String, Set<L>> map, L listener, String... annotations) {
        // throw exception if the listener doesn't support any annotations. what's the point of
        // registering then?
        if (null == annotations || annotations.length == 0) {
            throw new IllegalArgumentException(listener.getClass() + " has no supporting Annotations. Check method supportedAnnotations");
        }

        for (String annotation : annotations) {
            Set<L> listeners = map.get(annotation);
            if (null == listeners) {
                listeners = new HashSet<L>();
                map.put(annotation, listeners);
            }
            listeners.add(listener);
        }
    }

    /**
     * Gets the filter implementation.
     *
     * @return the filter
     */
    public abstract Filter getFilter();

    /**
     * Finds resources to scan for
     */
    public abstract URL[] findResources();


    /**
     * legacy discover method without parameter annotation scanner
     */
    public final void discover(boolean classes, boolean fields, boolean methods, boolean visible, boolean invisible) {
    	discover(classes, fields, methods, false, visible, invisible);
    }

    /**
     * that's my buddy! this is where all the discovery starts.
     */
    public final void discover(boolean classes, boolean fields, boolean methods, boolean parameters, boolean visible, boolean invisible) {
        URL[] resources = findResources();
        for (URL resource : resources) {
            try {
                ResourceIterator itr = getResourceIterator(resource, getFilter());
                if (itr != null) {
                    InputStream is = null;
                    while ((is = itr.next()) != null) {
                        // make a data input stream
                        DataInputStream dstream = new DataInputStream(new BufferedInputStream(is));
                        try {
                            // get java-assist class file
                            ClassFile classFile = new ClassFile(dstream);

                            // discover class-level annotations
                            if (classes) discoverAndIntimateForClassAnnotations (classFile, visible, invisible);
                            // discover field annotations
                            if (fields)  discoverAndIntimateForFieldAnnotations (classFile, visible, invisible);
                            // discover method annotations
                            if (methods || parameters) discoverAndIntimateForMethodAnnotations(classFile, methods, parameters, visible, invisible);
                        } finally {
                             dstream.close();
                             is.close();
                        }
                    }
                }
            } catch (IOException e) {
                // TODO: Do something with this exception
                e.printStackTrace();
            }
        }
    }

    /**
     * Discovers Class Annotations
     *
     * @param classFile
     */
    private void discoverAndIntimateForClassAnnotations (ClassFile classFile, boolean visible, boolean invisible) {
        Set<Annotation> annotations = new HashSet<Annotation>();

        if (visible) {
            AnnotationsAttribute visibleA = (AnnotationsAttribute) classFile.getAttribute(AnnotationsAttribute.visibleTag);
            if (visibleA != null) annotations.addAll(Arrays.asList(visibleA.getAnnotations()));
        }

        if (invisible) {
            AnnotationsAttribute invisibleA = (AnnotationsAttribute) classFile.getAttribute(AnnotationsAttribute.invisibleTag);
            if (invisibleA != null) annotations.addAll(Arrays.asList(invisibleA.getAnnotations()));
        }

        // now tell listeners
        for (Annotation annotation : annotations) {
        	// String versions of listeners
			Set<ClassAnnotationDiscoveryListener> listeners = classAnnotationListeners.get(annotation.getTypeName());
			if (null != listeners) {
    			for (ClassAnnotationDiscoveryListener listener : listeners) {
    				listener.discovered(classFile.getName(), annotation.getTypeName());
    			}
			}
			// Object versions of listeners
			Set<ClassAnnotationObjectDiscoveryListener> olisteners = classAnnotationObjectListeners.get(annotation.getTypeName());
			if (null != olisteners) {
				for (ClassAnnotationObjectDiscoveryListener listener : olisteners) {
					listener.discovered(classFile, annotation);
				}
			}
        }
    }

    /**
     * Discovers Field Annotations
     *
     * @param classFile
     */
    private void discoverAndIntimateForFieldAnnotations (ClassFile classFile, boolean visible, boolean invisible) {
        @SuppressWarnings("unchecked")
        List<FieldInfo> fields = classFile.getFields();
        if (fields == null) {
            return;
        }

        for (FieldInfo fieldInfo : fields) {
            Set<Annotation> annotations = new HashSet<Annotation>();

            if (visible) {
                AnnotationsAttribute visibleA = (AnnotationsAttribute) fieldInfo.getAttribute(AnnotationsAttribute.visibleTag);
                if (visibleA != null) annotations.addAll(Arrays.asList(visibleA.getAnnotations()));
            }

            if (invisible) {
                AnnotationsAttribute invisibleA = (AnnotationsAttribute) fieldInfo.getAttribute(AnnotationsAttribute.invisibleTag);
                if (invisibleA != null) annotations.addAll(Arrays.asList(invisibleA.getAnnotations()));
            }

            // now tell listeners
            for (Annotation annotation : annotations) {
            	// String versions of listeners
				Set<FieldAnnotationDiscoveryListener> listeners = fieldAnnotationListeners.get(annotation.getTypeName());
				if (null != listeners) {
					for (FieldAnnotationDiscoveryListener listener : listeners) {
						listener.discovered(classFile.getName(), fieldInfo.getName(), annotation.getTypeName());
					}
				}
				// Object versions of listeners
				Set<FieldAnnotationObjectDiscoveryListener> olisteners = fieldAnnotationObjectListeners.get(annotation.getTypeName());
				if (null != olisteners) {
					for (FieldAnnotationObjectDiscoveryListener listener : olisteners) {
						listener.discovered(classFile, fieldInfo, annotation);
					}
				}
            }
        }
    }

    /**
     * Discovers Method Annotations
     *
     * @param classFile
     */
    private void discoverAndIntimateForMethodAnnotations(ClassFile classFile, boolean methods, boolean parameters, boolean visible, boolean invisible) {
        @SuppressWarnings("unchecked")
        List<MethodInfo> methodInfos = classFile.getMethods();

        if (methodInfos == null) {
            return;
        }

        for (MethodInfo methodInfo : methodInfos) {

        	// if method annotations were requested
        	if (methods) {

                Set<Annotation> annotations = new HashSet<Annotation>();

                if (visible) {
                    AnnotationsAttribute visibleA = (AnnotationsAttribute) methodInfo.getAttribute(AnnotationsAttribute.visibleTag);
                    if (visibleA != null) annotations.addAll(Arrays.asList(visibleA.getAnnotations()));
                }

                if (invisible) {
                    AnnotationsAttribute invisibleA = (AnnotationsAttribute) methodInfo.getAttribute(AnnotationsAttribute.invisibleTag);
                    if (invisibleA != null) annotations.addAll(Arrays.asList(invisibleA.getAnnotations()));
                }

                // now tell method listeners
                for (Annotation annotation : annotations) {
                	// String versions of listeners
    				Set<MethodAnnotationDiscoveryListener> listeners = methodAnnotationListeners.get(annotation.getTypeName());
    				if (null != listeners) {
    					for (MethodAnnotationDiscoveryListener listener : listeners) {
    						listener.discovered(classFile.getName(), methodInfo.getName(), annotation.getTypeName());
    					}
    				}
    				// Object versions of listeners
    				Set<MethodAnnotationObjectDiscoveryListener> olisteners = methodAnnotationObjectListeners.get(annotation.getTypeName());
    				if (null != olisteners) {
    					for (MethodAnnotationObjectDiscoveryListener listener : olisteners) {
    						listener.discovered(classFile, methodInfo, annotation);
    					}
    				}
                }
        	}

        	// if parameter annotations were requested
            if (parameters) {
                // look at the method - complexity is in util.MethodParameters
                List<MethodParameter> methodParameters=
                		MethodParameters.getMethodParameters(methodInfo, visible, invisible);
                for (MethodParameter methodParameter : methodParameters) {
                	List<Annotation> annotations=methodParameter.getAnnotations();
                	if (annotations != null && annotations.size() > 0) for (Annotation annotation : annotations){
                    	// String versions of listeners
        				Set<MethodParameterAnnotationDiscoveryListener> listeners = methodParameterAnnotationListeners.get(annotation.getTypeName());
        				if (null != listeners) {
        					for (MethodParameterAnnotationDiscoveryListener listener : listeners) {
        						listener.discovered(classFile.getName(), methodInfo.getName(),
        								methodParameter.getIndex(), methodParameter.getType(), annotation.getTypeName());
        					}
        				}
        				// Object versions of listeners
        				Set<MethodParameterAnnotationObjectDiscoveryListener> olisteners = methodParameterAnnotationObjectListeners.get(annotation.getTypeName());
        				if (null != olisteners) {
        					for (MethodParameterAnnotationObjectDiscoveryListener listener : olisteners) {
        						listener.discovered(classFile, methodInfo, methodParameter, annotation);
        					}
        				}
                	}

                }
            }
        }
    }


    /**
     * Gets the Resource iterator for URL with Filter.
     *
     * @param url
     * @param filter
     * @return
     * @throws IOException
     */
    private ResourceIterator getResourceIterator(URL url, Filter filter) throws IOException {
        String urlString = url.toString();
        if (urlString.endsWith("!/")) {
            urlString = urlString.substring(4);
            urlString = urlString.substring(0, urlString.length() - 2);
            url = new URL(urlString);
        }

        if (!urlString.endsWith("/")) {
            return new JarFileIterator(url.openStream(), filter);
        } else {

            if (!url.getProtocol().equals("file")) {
                throw new IOException("Unable to understand protocol: " + url.getProtocol());
            }

            String filePath = URLDecoder.decode(url.getPath(), "UTF-8");
            File f = new File(filePath);
            if (!f.exists()) return null;

            if (f.isDirectory()) {
                return new ClassFileIterator(f, filter);
            } else {
                return new JarFileIterator(url.openStream(), filter);
            }
        }
    }


}
