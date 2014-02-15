organization := "tv.cntt"

name := "annovention"

version := "1.6-SNAPSHOT"

autoScalaLibrary := false

crossPaths := false

javacOptions ++= Seq("-source", "1.5", "-target", "1.5", "-Xlint:deprecation")

libraryDependencies += "org.javassist" % "javassist" % "3.18.1-GA"

//------------------------------------------------------------------------------

// Skip API doc generation to speedup "publish-local" while developing.
// Comment out this line when publishing to Sonatype.
publishArtifact in (Compile, packageDoc) := false
