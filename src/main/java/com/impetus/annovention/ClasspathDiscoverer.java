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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * The Class ClasspathReader.
 * 
 * @author animesh.kumar
 */
public class ClasspathDiscoverer extends Discoverer {

    /** The filter. */
    private Filter filter;

    /**
     * Instantiates a new classpath reader.
     */
    public ClasspathDiscoverer() {
        filter = new FilterImpl();
    }

    /**
     * Uses java.class.path system-property to fetch URLs
     * 
     * @return the URL[]
     */
    @SuppressWarnings("deprecation")
    @Override
    public final URL[] findResources() {
        URL[] ret = getUrlsForCurrentClasspath();
        if (ret.length == 0) ret = getUrlsForSystemClasspath();
        return ret;
    }

    /* @see com.impetus.annovention.Discoverer#getFilter() */
    public final Filter getFilter() {
        return filter;
    }

    /**
     * @param filter
     */
    public final void setFilter(Filter filter) {
        this.filter = filter;
    }

    //-------------------------------------------------------------------------

    // See http://code.google.com/p/reflections/source/browse/trunk/reflections/src/main/java/org/reflections/util/ClasspathHelper.java?r=103
    private URL[] getUrlsForCurrentClasspath() {
        List<URL> list = new ArrayList<URL>();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        while (loader != null) {
            if (loader instanceof URLClassLoader) {
                URL[]     urlArray = ((URLClassLoader) loader).getURLs();
                List<URL> urlList  = Arrays.asList(urlArray);
                list.addAll(urlList);
            }
            loader = loader.getParent();
        }
        return list.toArray(new URL[list.size()]);
    }
    
    private URL[] getUrlsForSystemClasspath() {
        List<URL> list = new ArrayList<URL>();
        String classpath = System.getProperty("java.class.path");
        StringTokenizer tokenizer = new StringTokenizer(classpath,
                File.pathSeparator);

        while (tokenizer.hasMoreTokens()) {
            String path = tokenizer.nextToken();

            File fp = new File(path);
            if (!fp.exists())
                throw new RuntimeException(
                        "File in java.class.path does not exist: " + fp);
            try {
                list.add(fp.toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        return list.toArray(new URL[list.size()]);
    }
}
