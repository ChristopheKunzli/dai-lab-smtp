package src.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private List<String> victims = new ArrayList<>();
    private List<Email> messages = new ArrayList<>();
    private int numberOfGroups;

    public Configuration(String victimsFile, String messagesFile, int numberOfGroups) {
        this.numberOfGroups = numberOfGroups;
        loadVictims(victimsFile);
        loadMessages(messagesFile);
    }

    public List<String> getVictims() {
        List<String> vic = new ArrayList<>();
        for(int i = 1; i < victims.size();++i){
            vic.add(victims.get(i));
        }
        return vic;
    }

    public String getFakeSender() {
        return victims.getFirst();
    }

    public List<Email> getMessages() {
        return messages;
    }

    public int getNumberOfGroups() {
        return numberOfGroups;
    }

    private void loadVictims(String victimsFile) {
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
        }
    }

    private void loadMessages(String messagesFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(messagesFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid message format: <" + line + ">");
                }
                messages.add(new Email(parts[0], parts[1]));
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
