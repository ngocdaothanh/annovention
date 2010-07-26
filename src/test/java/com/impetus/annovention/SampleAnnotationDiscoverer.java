package com.impetus.annovention;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;
import com.impetus.annovention.listener.FieldAnnotationDiscoveryListener;
import com.impetus.annovention.listener.MethodAnnotationDiscoveryListener;

public class SampleAnnotationDiscoverer {

	public static void main (String args []) {
		// get a classpath discovered instance
		Discoverer discoverer = new ClasspathDiscoverer();
		
		// add class annotation listener
		discoverer.addAnnotationListener(new MyClassAnnotationDiscoveryListener());
		// add field annotation listener
		discoverer.addAnnotationListener(new MyFieldAnnotationDiscoveryListener());
		// add method annotation listener
		discoverer.addAnnotationListener(new MyMethodAnnotationDiscoveryListener());
		
		// fire it
		discoverer.discover();
	}
	
	
	/**
	 * Dummy ClassAnnotation listener
	 * 
	 * @author animesh.kumar
	 *
	 */
	static class MyClassAnnotationDiscoveryListener implements ClassAnnotationDiscoveryListener {
		
		private static Log log = LogFactory.getLog(MyClassAnnotationDiscoveryListener.class);
		
		@Override
		public void discovered(String clazz, String annotation) {
			log.info("Discovered Class(" + clazz + ") with Annotation(" + annotation + ")");
		}

		@Override
		public String[] supportedAnnotations() {
			// Listens for @Entity and @Table annotations. 
			return new String[] {Entity.class.getName(), Table.class.getName()};
		}
	}
	
	/**
	 * Dummy FieldAnnotation listener
	 * 
	 * @author animesh.kumar
	 *
	 */
	static class MyFieldAnnotationDiscoveryListener implements FieldAnnotationDiscoveryListener {
		
		private static Log log = LogFactory.getLog(MyFieldAnnotationDiscoveryListener.class);
		
		@Override
		public void discovered(String clazz, String field, String annotation) {
			log.info("Discovered Field(" + clazz + "." + field + ") with Annotation(" + annotation + ")");
		}

		@Override
		public String[] supportedAnnotations() {
			// Listens for @Id and @Column annotations. 
			return new String[] {Id.class.getName(), Column.class.getName()};
		}
	}	
	
	
	/**
	 * Dummy FieldAnnotation listener
	 * 
	 * @author animesh.kumar
	 *
	 */
	static class MyMethodAnnotationDiscoveryListener implements MethodAnnotationDiscoveryListener{
		
		private static Log log = LogFactory.getLog(MyMethodAnnotationDiscoveryListener.class);
		
		@Override
		public void discovered(String clazz, String method, String annotation) {
			log.info("Discovered Method(" + clazz + "." + method + ") with Annotation(" + annotation + ")");
		}

		@Override
		public String[] supportedAnnotations() {
			// Listens for @PrePersist, @PreRemove and @PostPersist annotations. 
			return new String[] {PrePersist.class.getName(), PostPersist.class.getName(), PreRemove.class.getName()};
		}
	}		
}
