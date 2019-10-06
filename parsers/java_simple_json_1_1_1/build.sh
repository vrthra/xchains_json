#!/bin/sh
javac -cp ".:json-simple-1.1.1.jar" TestJSONParsing.java
jar cvfm TestJSONParsing.jar META-INF/MANIFEST.MF json-simple-1.1.1.jar TestJSONParsing.class
