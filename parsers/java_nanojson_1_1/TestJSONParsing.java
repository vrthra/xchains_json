import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.File;
import java.util.Map;

import com.grack.nanojson.JsonParser;

public class TestJSONParsing {

    public static Integer parseException(String ex) {
		try {
                int position = 0;
				String[] tokens = ex.split(" ");
				for (int i = 0; i < tokens.length; i++) { 
					if (tokens[i].equals("char")) {
                        position = Integer.parseInt(tokens[i+1]);
                        System.out.println("char: " + position);
                        //System.out.println("position: " + Integer.parseInt(tokens[i+1].split(".")[0]));
                        return position;
					}
				}
		}
		catch (Exception e) {
            System.out.println("ParseError:" + e);
        }
        return -1;
	}

    public static boolean isValidJSON(byte[] b) {
        try {
            Object o = JsonParser.any().from(new ByteArrayInputStream(b));
            if (new String(b).trim().equals("null"))
                return o == null;
            return o != null;
        } catch (Exception e) {
            String s = new String(b);
            Integer len = s.length();
            System.out.println("length:" + len);
            Integer echar = parseException(e.toString());
            System.out.println("chr:" + echar);
            if ( /* OR */ ((e.toString().contains("Unexpected token")) || 
            (e.toString().contains("Expected JSON value, got")) || 
            (e.toString().contains("Unexpected character")) || 
            (e.toString().contains("Numbers may not start with")) ||
            (e.toString().contains("Malformed number")) || 
            (e.toString().contains("Strings may not contain control characters")) || 
            (e.toString().contains("Expected STRING, got")) ||
            (e.toString().contains("Invalid escape")) || 
            (e.toString().contains("EOF encountered in the middle of a string escape")) || 
            /*(e.toString().contains("Expected a comma or end of the array instead of STRING")) || */
            (e.toString().contains("Expected COLON, got")) || 
            (e.toString().contains("Expected unicode hex escape character"))
            // still problems with braces
            ) /* AND */ && (!e.toString().contains("STRING, got EOF")) && (!e.toString().contains("JSON value, got EOF")) && (!e.toString().contains("COLON, got EOF")) && (!e.toString().contains("instead of"))) {
                System.out.println(e);
                System.out.println("invalid");
                System.exit(2);
            }
            else if (e.toString().contains("Expected a comma or end of the array instead of")) {
                System.out.println(e);
                System.out.println("invalid");
                if (echar != len && echar != -1 && len != 1) {
                    System.exit(2);
                }
            }
            else {
                System.out.println("no match");
                System.out.println(e);
            }
            return false;
        }
    }

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Usage: java TestJSONParsing file.json");
            System.exit(2);
        }

        try {
            byte[] b = Files.readAllBytes(Paths.get(args[0]));
            if(isValidJSON(b)) {
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
