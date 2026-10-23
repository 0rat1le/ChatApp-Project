/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package whatsapp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class WhatsApp {

    private String registeredUsername;
    private String registeredPassword;
    private String registeredCellPhone;
    private String firstName;
    private String lastName;

    private ArrayList<Message> sentMessages = new ArrayList<>();
    private ArrayList<Message> storedMessages = new ArrayList<>();
    private int messageCounter = 0;

    public WhatsApp() {
    }

    public WhatsApp(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

       
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();

        WhatsApp userLogin = new WhatsApp(firstName, lastName);

        System.out.print("Enter Username: ");
        String username = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        System.out.print("Enter Cell Phone Number: ");
        String cellPhone = scanner.nextLine();

        String registrationMessage = userLogin.registerUser(username, password, cellPhone);
        System.out.println("\n" + registrationMessage);

        if (registrationMessage.contains("incorrectly formatted")
                || registrationMessage.contains("not correctly formatted")
                || registrationMessage.contains("not correctly formatted;")) {
            scanner.close();
            return;
        }

        
        System.out.println("\n=== LOGIN ===");
        System.out.print("Enter Username: ");
        String loginUsername = scanner.nextLine();

        System.out.print("Enter Password: ");
        String loginPassword = scanner.nextLine();

        boolean loginSuccess = userLogin.loginUser(loginUsername, loginPassword);
        System.out.println("\n" + userLogin.returnLoginStatus(loginSuccess));

        if (!loginSuccess) {
            scanner.close();
            return;
        }

        
        System.out.println("\nWelcome to QuickChat.");

        boolean running = true;

        while (running) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Enter option number: ");

            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    System.out.print("How many messages would you like to enter? ");
                    int totalMessages = 0;

                    try {
                        totalMessages = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number entered.");
                        break;
                    }

                    if (totalMessages <= 0) {
                        System.out.println("Number of messages must be greater than 0.");
                        break;
                    }

                    for (int i = 0; i < totalMessages; i++) {
                        System.out.println("\n--- Message " + (i + 1) + " of " + totalMessages + " ---");

                        
                        String recipient;
                        while (true) {
                            System.out.print("Enter recipient cell number: ");
                            recipient = scanner.nextLine().trim();

                            if (userLogin.checkRecipientCell(recipient)) {
                                break;
                            } else {
                                System.out.println("Recipient cell number is invalid. Ensure it contains an international code and is valid.");
                            }
                        }

                        
                        String messageText;
                        while (true) {
                            System.out.print("Enter your message (max 250 characters): ");
                            messageText = scanner.nextLine();

                            if (messageText.length() > 250) {
                                System.out.println("Please enter a message of less than 250 characters.");
                            } else {
                                System.out.println("Message sent");
                                break;
                            }
                        }

                        String messageID = userLogin.generateRandomMessageId();

                        if (!userLogin.checkMessageID(messageID)) {
                            System.out.println("Message ID is invalid.");
                            continue;
                        }

                        int messageNumber = userLogin.getNextMessageNumber();
                        String messageHash = userLogin.createMessageHash(messageID, messageNumber, messageText);

                        Message newMessage = new Message(messageID, messageNumber, recipient, messageText, messageHash);

                        String action = userLogin.SentMessage(scanner);

                        if ("SEND".equals(action)) {
                            userLogin.sentMessages.add(newMessage);
                            System.out.println("Message successfully sent");
                            userLogin.displayMessageDetails(newMessage);
                        } else if ("DISREGARD".equals(action)) {
                            System.out.println("Press 0 to delete the message");
                            String deleteChoice = scanner.nextLine().trim();

                            if ("0".equals(deleteChoice)) {
                                System.out.println("Message deleted");
                            } else {
                                System.out.println("Message disregarded");
                            }
                        } else if ("STORE".equals(action)) {
                            userLogin.storeMessage(newMessage);
                            userLogin.sentMessages.add(newMessage);
                            System.out.println("Message successfully stored");
                        }
                    }

                    System.out.println("\nAll messages processed.");
                    System.out.println("Total messages sent: " + userLogin.returnTotalMessagess());
                    System.out.println("\nMessages sent while the program is running:");
                    System.out.println(userLogin.printMessages());

                    break;

                case "2":
                    System.out.println("Coming Soon.");
                    break;

                case "3":
                    running = false;
                    System.out.println("Exiting... Goodbye.");
                    break;

                default:
                    System.out.println("Invalid option. Please select 1, 2 or 3.");
            }
        }

        scanner.close();
    }
   
    public boolean checkUserName(String username) {
        if (username == null) return false;
        if (!username.contains("_") || username.length() > 5) return false;
        return username.matches("^[A-Za-z0-9_]+$");
    }

    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }

        return hasUppercase && hasDigit && hasSpecialChar;
    }

    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) return false;

        String cellRegex = "^\\+\\d{10,13}$";
        return cellPhoneNumber.matches(cellRegex);
    }

    public String registerUser(String username, String password, String cellPhone) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cellPhone)) {
            return "Cell phone number is not correctly formatted; include the international code as '+' followed by 10-13 digits.";
        }

        this.registeredUsername = username;
        this.registeredPassword = password;
        this.registeredCellPhone = cellPhone;

        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
    }

    public boolean loginUser(String username, String password) {
        if (username == null || password == null) return false;
        return username.equals(this.registeredUsername) && password.equals(this.registeredPassword);
    }

    public String returnLoginStatus(boolean isLoggedIn) {
        if (isLoggedIn) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    
    public boolean checkMessageID(String messageId) {
        if (messageId == null) {
            return false;
        }

        return messageId.length() >= 10;
    }

    public boolean checkRecipientCell(String recipient) {
        if (recipient == null) {
            return false;
        }

        String value = recipient.trim();

        if (value.isEmpty()) {
            return false;
        }

        if (value.startsWith("+")) {
            return value.matches("^\\+[0-9]{10,13}$");
        }

        return value.matches("^[0-9]{13}$");
    }

    public String createMessageHash(String messageId, int messageNumber, String message) {
        String firstTwoDigits = "";

        if (messageId != null && messageId.length() >= 2) {
            firstTwoDigits = messageId.substring(0, 2);
        } else {
            firstTwoDigits = "00";
        }

        String[] words = message.trim().split("\\s+");
        String firstWord = "";
        String lastWord = "";

        if (words.length > 0) {
            firstWord = words[0].toUpperCase();
            lastWord = words[words.length - 1].toUpperCase();
        }

        return firstTwoDigits + ":" + messageNumber + ":" + firstWord + lastWord;
    }

    public String SentMessage(Scanner scanner) {
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            System.out.print("Enter 1, 2 or 3: ");

            String option = scanner.nextLine().trim();

            if ("1".equals(option)) {
                return "SEND";
            } else if ("2".equals(option)) {
                return "DISREGARD";
            } else if ("3".equals(option)) {
                return "STORE";
            } else {
                System.out.println("Invalid option. Please choose 1, 2 or 3.");
            }
        }
    }

    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent yet.";
        }

        StringBuilder output = new StringBuilder();

        for (Message msg : sentMessages) {
            output.append("Message ID: ").append(msg.getMessageId()).append("\n");
            output.append("Message Hash: ").append(msg.getHash()).append("\n");
            output.append("Recipient: ").append(msg.getRecipient()).append("\n");
            output.append("Message: ").append(msg.getMessageText()).append("\n\n");
        }

        return output.toString();
    }

    public int returnTotalMessagess() {
        return sentMessages.size();
    }

    public String generateRandomMessageId() {
        Random random = new Random();
        long randomId = 1000000000L + (long) (random.nextDouble() * 9000000000L);
        return String.valueOf(randomId).substring(0, 10);
    }

    public int getNextMessageNumber() {
        int value = messageCounter;
        messageCounter++;
        return value;
    }

    public void storeMessage(Message message) {
        storedMessages.add(message);
        writeStoredMessagesToJson();
    }

    private void writeStoredMessagesToJson() {
        File file = new File("messages.json");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("[");
            for (int i = 0; i < storedMessages.size(); i++) {
                Message msg = storedMessages.get(i);

                writer.write("{");
                writer.write("\"messageId\":\"" + escapeJson(msg.getMessageId()) + "\",");
                writer.write("\"messageNumber\":" + msg.getMessageNumber() + ",");
                writer.write("\"recipient\":\"" + escapeJson(msg.getRecipient()) + "\",");
                writer.write("\"message\":\"" + escapeJson(msg.getMessageText()) + "\",");
                writer.write("\"hash\":\"" + escapeJson(msg.getHash()) + "\"");
                writer.write("}");

                if (i < storedMessages.size() - 1) {
                    writer.write(",\n");
                }
            }
            writer.write("]");
        } catch (IOException e) {
            System.out.println("Could not store message in JSON file.");
        }
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    public void displayMessageDetails(Message message) {
        System.out.println("Message ID: " + message.getMessageId());
        System.out.println("Message Hash: " + message.getHash());
        System.out.println("Recipient: " + message.getRecipient());
        System.out.println("Message: " + message.getMessageText());
    }

    public static class Message {
        private final String messageId;
        private final int messageNumber;
        private final String recipient;
        private final String messageText;
        private final String hash;

        public Message(String messageId, int messageNumber, String recipient, String messageText, String hash) {
            this.messageId = messageId;
            this.messageNumber = messageNumber;
            this.recipient = recipient;
            this.messageText = messageText;
            this.hash = hash;
        }

        public String getMessageId() {
            return messageId;
        }

        public int getMessageNumber() {
            return messageNumber;
        }

        public String getRecipient() {
            return recipient;
        }

        public String getMessageText() {
            return messageText;
        }

        public String getHash() {
            return hash;
        }
    }
}

