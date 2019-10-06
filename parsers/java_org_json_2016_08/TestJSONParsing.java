// javac -cp ".:json-20160810.jar" TestJSONParsing.java && java -classpath ".:json-20160810.jar" TestJSONParsing x.json

import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

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

    public static boolean isValidJSON(String s) {
        try {
            int idxArray  = s.indexOf('[');
            int idxObject = s.indexOf('{');

            System.out.println("String length: " + s.length());

            if (idxArray < 0 && idxObject < 0) {
                System.out.println("Not an array or object");
                System.out.println("invalid");
                System.exit(2);
            }
            else if (idxObject >= 0 && (idxArray < 0 || idxObject < idxArray)) {
                JSONObject obj = new JSONObject(s);
                System.out.println(obj);
                System.out.println(obj.getClass().getSimpleName());
                return true;
            }
            else {
                JSONArray obj = new JSONArray(s);
                System.out.println(obj);
                System.out.println(obj.getClass().getSimpleName());
                return true;
            }
        } catch (JSONException e) {
            /* not decidable between valid and incomplete
            
            (venv) Gordons-MacBook-Pro:java_org_json_2016_08 gordon$ java -jar TestJSONParsing.jar ../../incomplete.json 
            ../../incomplete.json
            String length: 7
            org.json.JSONException: Expected a ',' or ']' at 8 [character 1 line 3]
            invalid
            (venv) Gordons-MacBook-Pro:java_org_json_2016_08 gordon$ java -jar TestJSONParsing.jar ../../invalid.json 
            ../../invalid.json
            String length: 9
            org.json.JSONException: Expected a ',' or ']' at 10 [character 1 line 3]
            invalid
            (venv) Gordons-MacBook-Pro:java_org_json_2016_08 gordon$ 

            
            */
            if ( /* OR */ ( (e.toString().contains("Expected")) || (e.toString().contains("Missing value"))) /* AND */ /*&& (!e.toString().contains("in numeric value: expected digit"))*/ ) {
                System.out.println(e);
                System.out.println("invalid");
                System.exit(2);
            }
            else {
                System.out.println(e);
            }
            return false;
        }
        return false;
    }

    public static void main(String[] args) {

        if(args.length == 0) {
            System.out.println("Usage: java TestJSONParsing file.json");
            System.exit(2);
        }

        try {
            String s = new String(Files.readAllBytes(Paths.get(args[0])));
            System.out.println(args[0]);
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
