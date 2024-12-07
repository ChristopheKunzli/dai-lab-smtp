package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private List<String> victims;
    private List<Email> messages;
    private String sender;

    public Configuration(String sender) {
        this.sender = sender;
        victims = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public List<Email> getMessages() {
        return messages;
    }

    public Email getRandomMessage() {
        return messages.get((int) (Math.random() * messages.size()));
    }

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

    public boolean loadVictims(String victimsFile) {
        victims = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(victimsFile))) {
            String line = br.readLine();
            while (line != null) {
                if (!line.contains("@")) {
                    throw new IllegalArgumentException("Invalid email address<" + line + ">");
                }
                victims.add(line);
                line = br.readLine();
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean loadMessages(String messagesFile) {
        messages = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(messagesFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid message format: <" + line + ">");
                }
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
