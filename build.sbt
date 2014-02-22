organization := "tv.cntt"

name := "annovention"

version := "1.8-SNAPSHOT"

// Remove Scala dependency
autoScalaLibrary := false

// Remove Scala version in output paths and artifacts
crossPaths := false

// "1.5" causes NoSuchMethodError when calling the final methods in the abstract
// Discoverer class when this lib is used with Java 6
javacOptions ++= Seq("-source", "1.6", "-target", "1.6", "-Xlint:deprecation")

javacOptions in doc := Seq("-source", "1.6")

libraryDependencies += "org.javassist" % "javassist" % "3.18.1-GA"

//------------------------------------------------------------------------------

// Skip API doc generation to speedup "publish-local" while developing.
// Comment out this line when publishing to Sonatype.
publishArtifact in (Compile, packageDoc) := false
