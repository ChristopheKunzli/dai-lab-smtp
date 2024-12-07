package src;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private List<String> victims;//Victims of the group, the first one is the fake sender
    private String sender;//Real sender

    public Group(List<String> emails, String sender) {
        if (emails == null || emails.size() < 2) {
            throw new IllegalArgumentException("A group must have at least 2 memebers");
        }
        this.victims = new ArrayList<>(emails);
        this.sender = sender;
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

    public String getRealSender() {
        return sender;
    }
}
