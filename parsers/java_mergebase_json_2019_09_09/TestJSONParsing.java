// javac -cp ".:json-mergebase-2019.09.09.jar" TestJSONParsing.java && java -classpath ".:json-mergebase-2019.09.09.jar" TestJSONParsing x.json

import com.mergebase.util.Java2Json;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestJSONParsing {
    
    public static boolean isValidJSON(String s) {
        try {
            Object obj = Java2Json.parse(s);
        } catch (Throwable t) {
            if ( (t.toString().contains("Expected whitespace or JSON literal")) || 
            (t.toString().contains("expected number but got")) || 
            (t.toString().contains("expected null literal but")) || 
            (t.toString().contains("expected true/false literal but ran into bad character")) || 
            (t.toString().contains("invalid type: -1")) || 
            (t.toString().contains("number literal cannot be negative sign by itself")) || 
            (t.toString().contains("expected true/false literal but ran of out string")) || 
            (t.toString().contains("expected whitespace or comma or ] but found")) || 
            (t.toString().contains("control characters in string literal must be escaped")) || 
            (t.toString().contains("java.lang.ArrayIndexOutOfBoundsException")) || 
            (t.toString().contains("invalid backslash protected character")) || 
            (t.toString().contains("Expected whitespace or : but got")) || 
            (t.toString().contains("never found literal string terminator")) || 
            (t.toString().contains("json expecting double-quote"))) {
            /* it does not run as it should, but checking for "Never found" does also not work */
                System.out.println(t);
                System.out.println("invalid");
                System.exit(2);
            }
            else {
                System.out.println(t);
            }
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        
        if(args.length == 0) {
            System.out.println("Usage: java TestJSONParsing file.json");            
            System.exit(2);
        }
        
        try {
            String s = new String(Files.readAllBytes(Paths.get(args[0])));
            if(isValidJSON(s)) {
                System.out.println("valid");
                System.exit(0);            
            }
            System.out.println("incomplete1");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("incomplete2");
            System.exit(1);
        }   
    }
}
