organization := "tv.cntt"

name := "annovention"

version := "1.5-SNAPSHOT"

autoScalaLibrary := false

crossPaths := false

// http://www.scala-sbt.org/release/docs/Detailed-Topics/Java-Sources
// Avoid problem when Xitrum is built with Java 7 but the projects that use Xitrum
// are run with Java 5
// java.lang.UnsupportedClassVersionError: xitrum/annotation/First : Unsupported major.minor version 51.0
// javacOptions ++= Seq("-source", "1.5", "-target", "1.5", "-Xlint:deprecation")

libraryDependencies += "org.javassist" % "javassist" % "3.18.1-GA"

//------------------------------------------------------------------------------

// Skip API doc generation to speedup "publish-local" while developing.
// Comment out this line when publishing to Sonatype.
publishArtifact in (Compile, packageDoc) := false
