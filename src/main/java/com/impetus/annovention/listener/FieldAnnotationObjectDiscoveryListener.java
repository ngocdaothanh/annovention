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
package com.impetus.annovention.listener;

import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.Annotation;


/**
 * The Interface FieldAnnotationDiscoveryListener.
 *
 * @author animesh.kumar
 * @author dbennett
 */
public interface FieldAnnotationObjectDiscoveryListener extends AnnotationDiscoveryListener {

    /**
     * Gets called by the Discoverer with class-name of the class, field-name of the field
     * where annotation is found.
     *
     * @param clazz
     * @param field
     * @param annotation
     */
    void discovered(ClassFile clazz, FieldInfo field, Annotation annotation);
}
