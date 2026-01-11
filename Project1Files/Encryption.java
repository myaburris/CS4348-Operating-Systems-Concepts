import java.util.Scanner;

public class Encryption {
    private static String passkey = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();
            String[] parts = input.split(" ", 2);
            String command = parts[0];
            String argument = parts.length > 1 ? parts[1] : "";

            String response = processCommand(command, argument);
            System.out.println(response);

            if (command.equals("QUIT")) {
                break;
            }
        }

        scanner.close();
    }

    private static String processCommand(String command, String argument) {
        switch (command) {
            case "PASSKEY":
                passkey = argument;
                return "RESULT";
            case "ENCRYPT":
                if (passkey.equals(null)) {
                    return "ERROR Password not set";
                }
                String encrypted = encrypt(argument);
                return "RESULT " + encrypted;
            case "DECRYPT":
                if (passkey.equals(null)) {
                    return "ERROR Password not set";
                }
                String decrypted = decrypt(argument);
                return "RESULT " + decrypted;
            case "QUIT":
                return "RESULT";
            default:
                return "ERROR Unknown command: " + command;
        }
    }

    private static String encrypt(String text) {
        String res = "";
        text = text.toUpperCase();
        for (int i = 0, j = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            if (c < 'A' || c > 'Z')
                continue;
            res += (char) ((c + passkey.charAt(j) - 2 * 'A') % 26 + 'A');
            j = ++j % passkey.length();
        }
        return res;
    }

    private static String decrypt(String text) {
        String res = "";
        text = text.toUpperCase();
        for (int i = 0, j = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            if (c < 'A' || c > 'Z')
                continue;
            res += (char) ((c - passkey.charAt(j) + 26) % 26 + 'A');
            j = ++j % passkey.length();
        }
        return res;
    }
}
