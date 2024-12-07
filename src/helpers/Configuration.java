package helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Configures a prank campaign.
 */
public class Configuration {
    private List<String> victims;//list of email addresses
    private List<Email> messages;//list of email content (subject + body)
    private final String sender;//email address of the pranker

    public Configuration(String sender) {
        this.sender = sender;
        victims = new ArrayList<>();
        messages = new ArrayList<>();
    }

    /**
     * Returns a random message from the list of messages.
     *
     * @return chosen message
     */
    public Email getRandomMessage() {
        return messages.get((int) (Math.random() * messages.size()));
    }

    /**
     * Forms groups of victims. Each group has at least 2 victims.
     *
     * @param groupsCount number of groups to form
     * @return an array of groups
     */
    public Group[] formGroups(int groupsCount) {
        if (groupsCount < 1) {
            throw new IllegalArgumentException("Number of groups must be at least 1");
        }

        int victimsPerGroup = victims.size() / groupsCount;
        int remainingVictims = victims.size() % groupsCount;

        if (victimsPerGroup < 2) {
            throw new IllegalArgumentException("Number of victims per group must be at least 2");
        }

        Group[] groups = new Group[groupsCount];
        int victimIndex = 0;

        for (int i = 0; i < groupsCount; ++i) {
            int groupSize = victimsPerGroup + (remainingVictims > 0 ? 1 : 0);
            List<String> groupVictims = new ArrayList<>();
            for (int j = 0; j < groupSize; ++j) {
                groupVictims.add(victims.get(victimIndex++));
            }
            groups[i] = new Group(groupVictims, sender);
            --remainingVictims;
        }

        return groups;
    }

    /**
     * Loads the list of victims email address from a file. Each victim occupies one line.
     *
     * @param victimsFile path to the file containing the victims
     * @return true if the victims were loaded successfully, false otherwise
     */
    public boolean loadVictims(String victimsFile) {
        victims = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(victimsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains("@")) {
                    throw new IllegalArgumentException("Invalid email address<" + line + ">");
                }
                victims.add(line);
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Loads messages from a file. Each message occupies one line. The subject and the body are separated by a semicolon.
     *
     * @param messagesFile path to the file containing the messages
     * @return true if the messages were loaded successfully, false otherwise
     */
    public boolean loadMessages(String messagesFile) {
        messages = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(messagesFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into subject and body
                String[] parts = line.split(";");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid message format: <" + line + ">");
                }
                // Replace <br> with real newline character
                parts[1] = parts[1].replaceAll("<br>", "\r\n");
                messages.add(new Email(parts[0], parts[1]));
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
            return false;
        }
        return true;
    }
}
