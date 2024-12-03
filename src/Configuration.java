package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private List<String> victims;
    private List<Email> messages;
    private final int numberOfGroups;

    public Configuration(int numberOfGroups) {
        this.numberOfGroups = numberOfGroups;
        victims = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public List<String> getVictims() {
        List<String> victimsWithoutFakeSender = new ArrayList<>();
        for (int i = 1; i < victims.size(); ++i) {
            victimsWithoutFakeSender.add(victims.get(i));
        }
        return victimsWithoutFakeSender;
    }

    public String getFakeSender() {
        return victims.getFirst();
    }

    public List<Email> getMessages() {
        return messages;
    }

    public Email getRandomMessage() {
        return messages.get((int) (Math.random() * messages.size()));
    }

    public int getNumberOfGroups() {
        return numberOfGroups;
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
