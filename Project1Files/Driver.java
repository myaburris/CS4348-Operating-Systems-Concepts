import java.io.*;
import java.util.*;

public class Driver
{
	public static void main(String[] args)
	{
		if (args.length != 1) {
            System.out.println("Usage: java Driver <logFileName>");
            System.exit(1);
        }

		try
		{
		//process for Logger
		Process logger = Runtime.getRuntime().exec("java Logger");
		InputStream inStreamL = logger.getInputStream();
		OutputStream outStreamL = logger.getOutputStream();
		Scanner fromLogger = new Scanner(inStreamL); 
		PrintStream toLogger = new PrintStream(outStreamL);

		//process for Encryption
		Process encryption = Runtime.getRuntime().exec("java Encryption");
		InputStream inStreamE = encryption.getInputStream();
		OutputStream outStreamE = encryption.getOutputStream();
		Scanner fromEncryption = new Scanner(inStreamE); 
		PrintStream toEncryption = new PrintStream(outStreamE);

		//variables
		Scanner scanner = new Scanner(System.in);
		String command = "";
		String argument = "";
		ArrayList<String> encryptHistory = new ArrayList<String>();
		ArrayList<String> decryptHistory = new ArrayList<String>();
		String choice;
		String password = null;
		String usingHistory;

        while (true) {
            System.out.println("Select a command:");
            System.out.println("1. set password");
            System.out.println("2. encrypt string");
            System.out.println("3. decrypt string");
            System.out.println("4. show history");
            System.out.println("5. quit");

            choice = scanner.nextLine();

            switch (choice) {
                case "1":
					//ask user if they are making a new password or not
					System.out.println("Take password from history? (y/n)");
					usingHistory = scanner.nextLine();

                    if (usingHistory.equals("n")) {
						//make new password
                        System.out.print("Enter a new password: ");
                        password = scanner.nextLine();

						//store into history
						encryptHistory.add(password);
                    } else {
						//prompt for a history string selection and get their input
                        System.out.println("Select a string from history:");
						if(encryptHistory.size() == 0){
							System.out.println("No history available");
						}
                        for (int i = 0; i < encryptHistory.size(); i++) {
                            System.out.println((i + 1) + ". " + encryptHistory.get(i));
                        }
                        System.out.print("Enter the number corresponding to the string you want to use OR enter -1 if you would like to use a new string: ");
                        int historyChoice = Integer.parseInt(scanner.nextLine()) - 1;

						//get string according to their choice
						if (historyChoice ==-2){
							//make new password
							System.out.print("Enter a new password: ");
							password = scanner.nextLine();

							//store into history
							encryptHistory.add(password);
						} else {
							password = encryptHistory.get(historyChoice);
						}
                    }
					//set up command to be sent
                    command = "PASSKEY";
                    argument = password;
                    break;
                case "2":
					//ask user if they will use a string from history
					System.out.println("Encrypt string from history? (y/n)");
					usingHistory = scanner.nextLine();	

					String toEncrypt;
					String toDecrypt;

                    if (usingHistory.equals("n")) {
						//ask user for a new string to encrypt
                        System.out.print("Enter a string to encrypt: ");
                        toEncrypt = scanner.nextLine();

						//add the string to the history
                        encryptHistory.add(toEncrypt);

						//set argument for command
                        argument = toEncrypt;
                    } else {
						//prompt a menu to display the current history and get their input
                        System.out.println("Select a string from history:");
						if(encryptHistory.size() == 0){
							System.out.println("No history available");
						}
                        for (int i = 0; i < encryptHistory.size(); i++) {
                            System.out.println((i + 1) + ". " + encryptHistory.get(i));
                        }
						System.out.print("Enter the number corresponding to the string you want to use OR enter -1 if you would like to use a new string: ");
                        int historyChoice = Integer.parseInt(scanner.nextLine()) - 1;
						
						//get string according to their choice
						if (historyChoice == -2){
							//ask user for a new string to encrypt
							System.out.print("Enter a string to encrypt: ");
							toEncrypt = scanner.nextLine();

							//add the string to the history
							encryptHistory.add(toEncrypt);
							
							//set the argument
							argument = encryptHistory.get(encryptHistory.size() - 1);
						} else {
							//get string from history
							toEncrypt = encryptHistory.get(historyChoice);

							//set the argument
							argument = encryptHistory.get(historyChoice);
						}
                    }
					//set up command to be sent
					command = "ENCRYPT";
                    break;
                case "3":
                    //ask user if they will use a string from history
					System.out.println("Decrypt string from history? (y/n)");
					usingHistory = scanner.nextLine();	

                    if (usingHistory.equals("n")) {
						//ask user for a new string to decrypt
                        System.out.print("Enter a string to decrypt: ");
                        toDecrypt = scanner.nextLine();

						//add the string to the history
                        decryptHistory.add(toDecrypt);

						//set argument for command
                        argument = toDecrypt;
                    } else {
						//prompt a menu to display the current history and get their input
                        System.out.println("Select a string from history:");
						if(decryptHistory.size() == 0){
							System.out.println("No history available");
						}
                        for (int i = 0; i < decryptHistory.size(); i++) {
                            System.out.println((i + 1) + ". " + decryptHistory.get(i));
                        }
						System.out.print("Enter the number corresponding to the string you want to use OR enter -1 if you would like to use a new string: ");
                        int historyChoice = Integer.parseInt(scanner.nextLine()) - 1;
						
						//get string according to their choice
						if (historyChoice == -2){
							//ask user for a new string to decrypt
							System.out.print("Enter a string to decrypt: ");
							toDecrypt = scanner.nextLine();

							//add the string to the history
							decryptHistory.add(toDecrypt);

							//set the argument
							argument = decryptHistory.get(decryptHistory.size() - 1);
						} else {
							//get string from history
							toDecrypt = decryptHistory.get(historyChoice);

							//set up argument for command
                        	argument = decryptHistory.get(historyChoice);
						}
                    }
					//set up command to be sent
					command = "DECRYPT";
                    break;
                case "4":
					//display the encryption history
					System.out.println("Showing encryption history:");
					if(encryptHistory.size() == 0){
						System.out.println("No encryption history available");
					}
                    for (int i = 0; i < encryptHistory.size(); i++) {
                        System.out.println((i + 1) + ". " + encryptHistory.get(i));
					}

					//display the decryption history
					System.out.println("Showing decryption history:");
					if(decryptHistory.size() == 0){
						System.out.println("No decryption history available");
					}
                    for (int i = 0; i < decryptHistory.size(); i++) {
                        System.out.println((i + 1) + ". " + decryptHistory.get(i));
					}
                    break;
                case "5":
                    command = "QUIT";
                    argument = "";
                    break;
                default:
                    System.out.println("Invalid command. Please try again.");
                    
            }
			// Send the command to the encryption program
			toEncryption.println(command + " " + argument);
			toEncryption.flush();

			// Get the result from the encryption program and display them
			String result = fromEncryption.nextLine();
			if (choice.equals("1") || choice.equals("2") || choice.equals("3")){
				System.out.println(result);
			}
			

			// send command and result to logger
			toLogger.println("START Logging Start.");
			toLogger.println(command + " " + argument);
			toLogger.println(result);
			toLogger.flush();

			//exit while loop on QUIT command
            if (command.equals("QUIT")) {
				toLogger.println("END Logging End.");
				toLogger.flush();
                break;
            }
        }

        scanner.close();
		fromEncryption.close();
		fromLogger.close();
		
		logger.waitFor();
		encryption.waitFor();
		}
		catch(IOException ex)
		{
			System.out.println("Unable to run Logger or Encryption program");
		}
		catch(InterruptedException ex)
		{
			System.out.println("Unexpected Termination");
		}
	}
}