import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Logger {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Logger <logFileName>");
            System.exit(1);
        }

        String logFileName = args[0];

        try (Scanner scanner = new Scanner(System.in);
             PrintWriter logWriter = new PrintWriter(new FileWriter(logFileName, true))) {

            String input;
            String action = "";
            String message = "";
            while (true) {
                input = scanner.nextLine();
                if (input.equals("QUIT")) {
                    break;
                } else {
                    //seperate the action word from the rest of the message
                    int i = 0;
                    for (; input.charAt(i) != ' '; i++) {
                        action = action + input.charAt(i);
                    }
                    message = input.substring(i + 1);

                    //write action word and message into log file
                    logWriter.println(formatLogMessage(action, message));
                    logWriter.flush();

                    //reset action word
                    action = "";
                }
            }
        } catch (IOException ex) {
            System.out.println("Error writing to the log file: " + ex.getMessage());
        }
    }

    private static String formatLogMessage(String action, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timestamp = dateFormat.format(new Date());
        return timestamp + " [" + action + "] " + message;
    }
}
