import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import de.undercouch.actson.JsonEvent;
import de.undercouch.actson.JsonParser;

public class TestJSONParsing {
    public static boolean isValidJSON(byte[] bytes) {
        JsonParser parser = new JsonParser();
        int i = 0;
        int event;
        try {
            do {
                while ((event = parser.nextEvent()) == JsonEvent.NEED_MORE_INPUT) {
                    i += parser.getFeeder().feed(bytes, i, bytes.length - i);
                    System.out.println("Event: " + event);
                    System.out.println("i: " + i);
                    if (i == bytes.length) {
                        parser.getFeeder().done();
                    }
                }
                if (event == JsonEvent.ERROR) {
                    return false;
                }
            } while (event != JsonEvent.EOF);
            return true;
        } catch (Exception e) {
            /*

            endless loop

            b"java.lang.IllegalArgumentException: Unexpected character 'g' at java.io.BufferedReader@721e0f4f\ninvalid\n"
            [5/i
            b"java.lang.IllegalArgumentException: Unexpected character 'i' at java.io.BufferedReader@721e0f4f\ninvalid\n"
            [5//
            b"java.lang.IllegalArgumentException: Unexpected character '/' at java.io.BufferedReader@721e0f4f\ninvalid\n"
            [5/2
            b"java.lang.IllegalArgumentException: Unexpected character '2' at java.io.BufferedReader@721e0f4f\ninvalid\n"
            [5/


            */
            if ((e.toString().contains("bla"))/* || (e.toString().contains("Invalid")) || (e.toString().contains("Comments"))  || (e.toString().contains("Stream")) || (e.toString().contains("Trailing"))*/) {
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
        if (args.length == 0) {
            System.out.println("Usage: java TestJSONParsing file.json");
            System.exit(2);
        }

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(args[0]));
            if (isValidJSON(bytes)) {
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
