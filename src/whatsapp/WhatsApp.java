/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt
 * to change this license
 */
package whatsapp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WhatsApp {

    private String registeredUsername;
    private String registeredPassword;
    private String registeredCellPhone;
    private String firstName;
    private String lastName;

    private final List<Message> sentMessages = new ArrayList<>();
    private final List<Message> disregardedMessages = new ArrayList<>();
    private final List<Message> storedMessages = new ArrayList<>();
    private final List<Message> allMessages = new ArrayList<>();
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
        WhatsApp app = new WhatsApp();

        System.out.print("Enter First Name: ");
        app.firstName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        app.lastName = scanner.nextLine();

        System.out.print("Enter Username: ");
        String username = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        System.out.print("Enter Cell Phone Number: ");
        String cellPhone = scanner.nextLine();

        String registrationMessage =
                app.registerUser(username, password, cellPhone);

        System.out.println("\n" + registrationMessage);

        if (!registrationMessage.startsWith(
                "Username successfully captured.")) {
            scanner.close();
            return;
        }

        System.out.println("\n=== LOGIN ===");
        System.out.print("Enter Username: ");
        String loginUsername = scanner.nextLine();

        System.out.print("Enter Password: ");
        String loginPassword = scanner.nextLine();

        boolean loginSuccessful =
                app.loginUser(loginUsername, loginPassword);

        System.out.println(
                "\n" + app.returnLoginStatus(loginSuccessful)
        );

        if (!loginSuccessful) {
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
            System.out.println("4) Stored Messages");

            System.out.print("Enter option number: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    app.sendMessages(scanner);
                    break;

                case "2":
                    System.out.println(app.printMessages());
                    break;

                case "3":
                    running = false;
                    System.out.println("Exiting... Goodbye.");
                    break;

                case "4":
                    app.storedMessagesMenu(scanner);
                    break;

                default:
                    System.out.println(
                            "Invalid option. Please select 1, 2, 3 or 4."
                    );
            }
        }

        scanner.close();
    }


    public boolean checkUserName(String username) {
        if (username == null) {
            return false;
        }

        return username.contains("_")
                && username.length() <= 5
                && username.matches("^[A-Za-z0-9_]+$");
    }

    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecialCharacter = false;

        for (char character : password.toCharArray()) {
            if (Character.isUpperCase(character)) {
                hasUppercase = true;
            } else if (Character.isDigit(character)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(character)) {
                hasSpecialCharacter = true;
            }
        }

        return hasUppercase
                && hasDigit
                && hasSpecialCharacter;
    }

    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) {
            return false;
        }

        return cellPhoneNumber.matches("^\\+\\d{10,13}$");
    }

    public String registerUser(
            String username,
            String password,
            String cellPhone
    ) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; "
                    + "please ensure that your username contains "
                    + "an underscore and is no more than five "
                    + "characters in length.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; "
                    + "please ensure that the password contains "
                    + "at least eight characters, a capital letter, "
                    + "a number, and a special character.";
        }

        if (!checkCellPhoneNumber(cellPhone)) {
            return "Cell phone number is not correctly formatted; "
                    + "include the international code as '+' "
                    + "followed by 10-13 digits.";
        }

        registeredUsername = username;
        registeredPassword = password;
        registeredCellPhone = cellPhone;

        return "Username successfully captured.\n"
                + "Password successfully captured.\n"
                + "Cell phone number successfully added.";
    }

    public boolean loginUser(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        return username.equals(registeredUsername)
                && password.equals(registeredPassword);
    }

    public String returnLoginStatus(boolean isLoggedIn) {
        if (isLoggedIn) {
            return "Welcome "
                    + firstName
                    + ", "
                    + lastName
                    + " it is great to see you again.";
        }

        return "Username or password incorrect, please try again.";
    }

    
    private void sendMessages(Scanner scanner) {
        System.out.print(
                "How many messages would you like to enter? "
        );

        int numberOfMessages;

        try {
            numberOfMessages = Integer.parseInt(
                    scanner.nextLine().trim()
            );
        } catch (NumberFormatException exception) {
            System.out.println("Invalid number entered.");
            return;
        }

        if (numberOfMessages <= 0) {
            System.out.println(
                    "Number of messages must be greater than 0."
            );
            return;
        }

        for (int i = 0; i < numberOfMessages; i++) {
            System.out.println(
                    "\n--- Message "
                            + (i + 1)
                            + " of "
                            + numberOfMessages
                            + " ---"
            );

            String recipient;

            while (true) {
                System.out.print(
                        "Enter recipient cell number: "
                );

                recipient = scanner.nextLine().trim();

                if (checkRecipientCell(recipient)) {
                    break;
                }

                System.out.println(
                        "Recipient cell number is invalid. "
                                + "Ensure it contains an international "
                                + "code and is valid."
                );
            }

            String messageText;

            while (true) {
                System.out.print(
                        "Enter your message (max 250 characters): "
                );

                messageText = scanner.nextLine();

                if (messageText.length() > 250) {
                    System.out.println(
                            "Please enter a message of less "
                                    + "than 250 characters."
                    );
                } else {
                    System.out.println("Message sent");
                    break;
                }
            }

            String messageID =
                    generateRandomMessageId();

            if (!checkMessageID(messageID)) {
                System.out.println("Message ID is invalid.");
                continue;
            }

            int messageNumber =
                    getNextMessageNumber();

            String messageHash = createMessageHash(
                    messageID,
                    messageNumber,
                    messageText
            );

            Message message = new Message(
                    messageID,
                    messageNumber,
                    registeredCellPhone,
                    recipient,
                    messageText,
                    messageHash
            );

            allMessages.add(message);

            String action = SentMessage(scanner);

            if ("SEND".equals(action)) {
                sentMessages.add(message);

                System.out.println(
                        "Message successfully sent"
                );

                displayMessageDetails(message);

            } else if ("DISREGARD".equals(action)) {
                disregardedMessages.add(message);

                System.out.println(
                        "Press 0 to delete the message"
                );

                String deleteChoice =
                        scanner.nextLine().trim();

                if ("0".equals(deleteChoice)) {
                    deleteMessageByHash(
                            message.getHash()
                    );

                    System.out.println(
                            "Message deleted"
                    );
                } else {
                    System.out.println(
                            "Message disregarded"
                    );
                }

            } else if ("STORE".equals(action)) {
                storeMessage(message);

                System.out.println(
                        "Message successfully stored"
                );
            }
        }

        System.out.println("\nAll messages processed.");
        System.out.println(
                "Total messages sent: "
                        + returnTotalMessagess()
        );
        System.out.println(
                "\nMessages sent while the program is running:"
        );
        System.out.println(printMessages());
    }

    public boolean checkMessageID(String messageId) {
        if (messageId == null) {
            return false;
        }

        return messageId.length() == 10
                && messageId.matches("\\d{10}");
    }

    public boolean checkRecipientCell(String recipient) {
        if (recipient == null) {
            return false;
        }

        recipient = recipient.trim();

        return recipient.matches("^\\+\\d{10,13}$");
    }

    public String createMessageHash(
            String messageId,
            int messageNumber,
            String message
    ) {
        String firstTwoDigits =
                messageId.substring(0, 2);

        String[] words =
                message.trim().split("\\s+");

        String firstWord = "";
        String lastWord = "";

        if (words.length > 0) {
            firstWord = words[0];
            lastWord = words[words.length - 1];
        }

        return (
                firstTwoDigits
                        + ":"
                        + messageNumber
                        + ":"
                        + firstWord
                        + lastWord
        ).toUpperCase();
    }

    public String SentMessage(Scanner scanner) {
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println(
                    "3) Store Message to send later"
            );
            System.out.print("Enter 1, 2 or 3: ");

            String option =
                    scanner.nextLine().trim();

            if ("1".equals(option)) {
                return "SEND";
            }

            if ("2".equals(option)) {
                return "DISREGARD";
            }

            if ("3".equals(option)) {
                return "STORE";
            }

            System.out.println(
                    "Invalid option. "
                            + "Please choose 1, 2 or 3."
            );
        }
    }

    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent yet.";
        }

        StringBuilder output =
                new StringBuilder();

        for (Message message : sentMessages) {
            output.append(formatMessage(message))
                    .append("\n\n");
        }

        return output.toString();
    }

    public int returnTotalMessagess() {
        return sentMessages.size();
    }

    public String generateRandomMessageId() {
        Random random = new Random();
        long messageId =
                1000000000L
                        + (long)
                        (random.nextDouble()
                                * 9000000000L);

        return String.valueOf(messageId);
    }

    public int getNextMessageNumber() {
        int number = messageCounter;
        messageCounter++;
        return number;
    }

    public void displayMessageDetails(Message message) {
        System.out.println(formatMessage(message));
    }

    private String formatMessage(Message message) {
        return "Message ID: "
                + message.getMessageId()
                + "\n"
                + "Message Hash: "
                + message.getHash()
                + "\n"
                + "Recipient: "
                + message.getRecipient()
                + "\n"
                + "Message: "
                + message.getMessageText();
    }

   
    public void storeMessage(Message message) {
        storedMessages.add(message);
        writeStoredMessagesToJson();
    }

    private void writeStoredMessagesToJson() {
        File file = new File("messages.json");

        try (FileWriter writer =
                     new FileWriter(file)) {
            writer.write("[\n");

            for (int i = 0;
                 i < storedMessages.size();
                 i++) {
                Message message =
                        storedMessages.get(i);

                writer.write("{");
                writer.write(
                        "\"messageId\":\""
                                + escapeJson(
                                message.getMessageId()
                        )
                                + "\","
                );
                writer.write(
                        "\"messageNumber\":"
                                + message.getMessageNumber()
                                + ","
                );
                writer.write(
                        "\"sender\":\""
                                + escapeJson(
                                message.getSender()
                        )
                                + "\","
                );
                writer.write(
                        "\"recipient\":\""
                                + escapeJson(
                                message.getRecipient()
                        )
                                + "\","
                );
                writer.write(
                        "\"message\":\""
                                + escapeJson(
                                message.getMessageText()
                        )
                                + "\","
                );
                writer.write(
                        "\"hash\":\""
                                + escapeJson(
                                message.getHash()
                        )
                                + "\""
                );
                writer.write("}");

                if (i < storedMessages.size() - 1) {
                    writer.write(",\n");
                }
            }

            writer.write("\n]");
        } catch (IOException exception) {
            System.out.println(
                    "Could not store message in JSON file."
            );
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

 

    private void storedMessagesMenu(Scanner scanner) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n--- Stored Messages ---");
            System.out.println(
                    "1) Display senders and recipients"
            );
            System.out.println(
                    "2) Display longest stored message"
            );
            System.out.println(
                    "3) Search by message ID"
            );
            System.out.println(
                    "4) Search stored messages by recipient"
            );
            System.out.println(
                    "5) Search sent messages by recipient"
            );
            System.out.println(
                    "6) Delete a message by hash"
            );
            System.out.println(
                    "7) Display stored-messages report"
            );
            System.out.println(
                    "8) Return to main menu"
            );

            System.out.print("Choose an option: ");
            String option =
                    scanner.nextLine().trim();

            switch (option) {
                case "1":
                    displayStoredSendersAndRecipients();
                    break;

                case "2":
                    displayLongestStoredMessage();
                    break;

                case "3":
                    searchByMessageId(scanner);
                    break;

                case "4":
                    searchByRecipient(
                            scanner,
                            storedMessages,
                            "stored"
                    );
                    break;

                case "5":
                    searchByRecipient(
                            scanner,
                            sentMessages,
                            "sent"
                    );
                    break;

                case "6":
                    System.out.print(
                            "Enter message hash: "
                    );
                    deleteMessageByHash(
                            scanner.nextLine().trim()
                    );
                    break;

                case "7":
                    displayStoredMessagesReport();
                    break;

                case "8":
                    inMenu = false;
                    break;

                default:
                    System.out.println(
                            "Invalid option."
                    );
            }
        }
    }

    private void displayStoredSendersAndRecipients() {
        if (storedMessages.isEmpty()) {
            System.out.println(
                    "There are no stored messages."
            );
            return;
        }

        for (Message message : storedMessages) {
            System.out.println(
                    "Sender: " + message.getSender()
            );
            System.out.println(
                    "Recipient: " + message.getRecipient()
            );
            System.out.println();
        }
    }

    private void displayLongestStoredMessage() {
        if (storedMessages.isEmpty()) {
            System.out.println(
                    "There are no stored messages."
            );
            return;
        }

        Message longest =
                storedMessages.get(0);

        for (Message message : storedMessages) {
            if (message.getMessageText().length()
                    > longest.getMessageText().length()) {
                longest = message;
            }
        }

        System.out.println(
                "Longest stored message:"
        );

        System.out.println(formatMessage(longest));
    }

    private void searchByMessageId(Scanner scanner) {
        System.out.print("Enter message ID: ");
        String searchedId =
                scanner.nextLine().trim();

        for (Message message : allMessages) {
            if (message.getMessageId()
                    .equals(searchedId)) {
                System.out.println(
                        "Recipient: "
                                + message.getRecipient()
                );
                System.out.println(
                        "Message: "
                                + message.getMessageText()
                );
                return;
            }
        }

        System.out.println("Message ID not found.");
    }

    private void searchByRecipient(
            Scanner scanner,
            List<Message> messages,
            String messageType
    ) {
        System.out.print(
                "Enter recipient cell number: "
        );

        String recipient =
                scanner.nextLine().trim();

        boolean found = false;

        for (Message message : messages) {
            if (message.getRecipient().equals(recipient)) {
                System.out.println(formatMessage(message));
                found = true;
            }
        }

        if (!found) {
            System.out.println(
                    "No "
                            + messageType
                            + " messages found for that recipient."
            );
        }
    }

    private void displayStoredMessagesReport() {
        if (storedMessages.isEmpty()) {
            System.out.println(
                    "There are no stored messages."
            );
            return;
        }

        System.out.println(
                "--- Stored Messages Report ---"
        );

        for (Message message : storedMessages) {
            System.out.println(formatMessage(message));
            System.out.println();
        }
    }

    private void deleteMessageByHash(String hash) {
        boolean deleted =
                storedMessages.removeIf(
                        message ->
                                message.getHash().equals(hash)
                );

        deleted =
                sentMessages.removeIf(
                        message ->
                                message.getHash().equals(hash)
                )
                        || deleted;

        deleted =
                disregardedMessages.removeIf(
                        message ->
                                message.getHash().equals(hash)
                )
                        || deleted;

        deleted =
                allMessages.removeIf(
                        message ->
                                message.getHash().equals(hash)
                )
                        || deleted;

        if (deleted) {
            writeStoredMessagesToJson();
            System.out.println(
                    "Message successfully deleted."
            );
        } else {
            System.out.println(
                    "No message was found with that hash."
            );
        }
    }

   
    public static class Message {
        private final String messageId;
        private final int messageNumber;
        private final String sender;
        private final String recipient;
        private final String messageText;
        private final String hash;

        public Message(
                String messageId,
                int messageNumber,
                String sender,
                String recipient,
                String messageText,
                String hash
        ) {
            this.messageId = messageId;
            this.messageNumber = messageNumber;
            this.sender = sender;
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

        public String getSender() {
            return sender;
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
} 


