#!/bin/sh
javac -cp ".:gson-2.8.6.jar" TestJSONParsing.java
jar cvfm TestJSONParsing.jar META-INF/MANIFEST.MF gson-2.8.6.jar TestJSONParsing.class
