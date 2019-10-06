// javac -cp ".:json-simple-1.1.1.jar" TestJSONParsing.java && java -classpath ".:json-simple-1.1.1.jar" TestJSONParsing x.json

import org.stringtree.json.JSONReader;
import org.stringtree.json.JSONValidatingReader;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestJSONParsing {
    
    public static Integer parseException(String ex) {
		try {
                int position = 0;
				String[] tokens = ex.split(" ");
				for (int i = 0; i < tokens.length; i++) { 
					if (tokens[i].equals("position")) {
                        position = Integer.parseInt(tokens[i+1].substring(0, tokens[i+1].length() - 1));
                        System.out.println("position: " + position);
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
            JSONReader reader = new JSONValidatingReader(new ExceptionErrorListener());
            Object result = reader.read(s);
            //System.out.println("result: " + result);
            System.out.println("JSONReader result is " + result + " of class " + result.getClass());
            if (result.toString().equals("true")) {
                return true;
            }
            else {
                if ( /* OR */ ( (result.toString().contains("expected value"))) ) {
                    System.out.println("invalid");
                    System.exit(2);
                }
                return false;
            }
        } catch (Exception e) {
            Integer len = s.length();
            System.out.println("Length:" + len);
            Integer position = parseException(e.toString());
            if ( /* OR */ ( (e.toString().contains("expected value"))/* || 
            (e.toString().contains("Unexpected token"))*/
            /*

            you can not decide between a invalid and incomplete input
            
            "Jd|^S(h-77RJi0*mrS3{'6#fD3jl<JSy=B6*S:p	4I$XSBAVX6%~v_Y(tIi>B0FcL4kF7xO8tF2m_*hr~J1ip*&
            $JIG-B^jpuw3iK5 N6D$-G1+\C]}f3+P
            b'Length:125\nposition: 125\nUnexpected token END OF FILE at position 125.\ninvalid\nincomplete1\n'

            [

            "Jd|^S(h-77RJi0*mrS3{'6#fD3jl<JSy=B6*S:p	4I$XSBAVX6%~v_Y(tIi>B0FcL4kF7xO8tF2m_*hr~J1ip*&
            $JIG-B^jpuw3iK5 N6D$-G1+\C]}f3+P<
            b'Length:126\nposition: 126\nUnexpected token END OF FILE at position 126.\ninvalid\nincomplete1\n'

            [
            
            "Jd|^S(h-77RJi0*mrS3{'6#fD3jl<JSy=B6*S:p	4I$XSBAVX6%~v_Y(tIi>B0FcL4kF7xO8tF2m_*hr~J1ip*&
            $JIG-B^jpuw3iK5 N6D$-G1+\C]}f3+P<

            b'Length:127\nposition: 127\nUnexpected token END OF FILE at position 127.\ninvalid\nincomplete1\n'

            [

            */
            ) /* AND */ /*&& (!e.toString().contains("in numeric value: expected digit"))*/ ) {
                System.out.println(e);
                System.out.println("invalid");
                if (position != len && position != -1) {
                    System.exit(2);
                }
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
