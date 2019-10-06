#!/bin/sh
javac -cp ".:actson-1.2.0.jar" TestJSONParsing.java
jar cvfm TestJSONParsing.jar META-INF/MANIFEST.MF actson-1.2.0.jar TestJSONParsing.class