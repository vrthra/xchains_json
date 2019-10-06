import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.File;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

public class TestJSONParsing {
    
    public static boolean isValidJSON(byte[] bytes) {
        try {        
            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);
            JsonNode rootNode = mapper.readTree(bytes);
            return rootNode != null;
        } catch (Exception e) {
            if ( /* OR */ ((e.toString().contains("Unexpected character")) || 
            (e.toString().contains("Unexpected character escape")) || 
            (e.toString().contains("Unrecognized character escape")) || 
            (e.toString().contains("Unrecognized token")) || 
            (e.toString().contains("Illegal character")) || 
            (e.toString().contains("Illegal unquoted character")) || 
            (e.toString().contains("Unexpected close marker")) || 
            (e.toString().contains("Unexpected end-of-input in null")) || 
            (e.toString().contains("Unexpected end-of-input in a Number value")) || 
            (e.toString().contains("Invalid numeric value: Leading zeroes not allowed"))) /* AND */ /*&& (!e.toString().contains("in numeric value: expected digit"))*/ ) {
                System.out.println(e);
                System.out.println("invalid");
                System.exit(2);
            }
            else {
                System.out.println(e);
            }
            return false;
        }
    }

    public static void main(String[] args) {
        
        if(args.length == 0) {
            System.out.println("Usage: java TestJSONParsing file.json");            
            System.exit(2);
        }
        
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(args[0]));
            if(isValidJSON(bytes)) {
                System.out.println("valid");
                System.exit(0);
            }
            else {
                System.out.println("incomplete1");
                System.exit(1);
            }
        } catch (IOException e) {
            System.out.println("incomplete2");
            System.exit(1);
        }   
    }
}
