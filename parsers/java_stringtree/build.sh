#!/bin/sh
javac -cp ".:stringtree-json-2.0.9.jar" TestJSONParsing.java
jar cvfm TestJSONParsing.jar META-INF/MANIFEST.MF stringtree-json-2.0.9.jar TestJSONParsing.class
