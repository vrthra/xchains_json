import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.leastfixedpoint.json.*;

public class TestJSONParsing {
    public static boolean isValidJSON(String s) {
        try {
            Object obj = JSONReader.readFrom(s);
            //System.out.println(obj);
            //System.out.println(obj.getClass().getSimpleName());
            return true;
        } catch (Exception e) {
            /*
            endless loop or takes very long ...

            b"com.leastfixedpoint.json.JSONSyntaxError: Invalid input parsing 'null' (line 0)\ninvalid\n"
            [71303nulc
            b"com.leastfixedpoint.json.JSONSyntaxError: Invalid input parsing 'null' (line 0)\ninvalid\n"
            [71303nul&
            b"com.leastfixedpoint.json.JSONSyntaxError: Invalid input parsing 'null' (line 0)\ninvalid\n"
            [71303nul#

            */
            if ((e.toString().contains("Invalid character")) || 
            (e.toString().contains("Unexpected")) || 
            (e.toString().contains("No digits found")) || 
            /*(e.toString().contains("Expected string map key")) || */ /* here lies the problem */
            (e.toString().contains("Invalid comment")) || 
            (e.toString().contains("Invalid input parsing")) || 
            (e.toString().contains("NumberFormatException ")) || 
            (e.toString().contains("Invalid string escape"))) {
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
            String s = new String(Files.readAllBytes(Paths.get(args[0])));
            if(isValidJSON(s)) {
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
