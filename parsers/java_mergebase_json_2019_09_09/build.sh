#!/bin/sh
javac -cp ".:json-mergebase-2019.09.09.jar" TestJSONParsing.java
jar cvfm TestJSONParsing.jar META-INF/MANIFEST.MF json-mergebase-2019.09.09.jar TestJSONParsing.class
