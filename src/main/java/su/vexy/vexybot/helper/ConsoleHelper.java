package su.vexy.vexybot.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper implements Helper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() {
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }
}
