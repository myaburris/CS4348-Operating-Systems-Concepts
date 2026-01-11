Files included in project:
- README file - the current file you're reading; the purpose is to explain what files are included, their purpose, how to compile the project, and any other notes
- Driver.java - Driver program for project that deals with all the user interaction and communicates with the Encryption and Logger files
- Encryption.java - Encryption program for project that uses the Vigenere cipher
- Logger.java - Logger program for project that creates a log to hold the log messages

How to compile project:
- run Driver.java with a single file name for the argument

Notes:
- Each java file works fine on its own, but some of the communcation does not work correctly in the Driver.java file 
   - trying to encrypt/decrypt before giving a password in Driver will give an exception, but done in the Encryption file works as intended
   - the log may not store any log messages from the Driver, but running the Logger.java file independently correctly stores the messages as intended