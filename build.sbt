organization := "tv.cntt"

name := "annovention"

version := "1.8-SNAPSHOT"

autoScalaLibrary := false

crossPaths := false

javacOptions ++= Seq("-source", "1.6", "-target", "1.6", "-Xlint:deprecation")

javacOptions in doc := Seq("-source", "1.6")

libraryDependencies += "org.javassist" % "javassist" % "3.18.1-GA"

//------------------------------------------------------------------------------

// Skip API doc generation to speedup "publish-local" while developing.
// Comment out this line when publishing to Sonatype.
publishArtifact in (Compile, packageDoc) := false
