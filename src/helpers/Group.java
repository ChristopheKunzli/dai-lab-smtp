package helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a group of victims.
 */
public class Group {
    private final List<String> victims;//Victims of the group, the first one is the fake sender
    private final String sender;//Real sender

    public Group(List<String> victimMailAddresses, String sender) {
        if (victimMailAddresses == null || victimMailAddresses.size() < 2) {
            throw new IllegalArgumentException("A group must have at least 2 memebers");
        }
        this.victims = new ArrayList<>(victimMailAddresses);
        this.sender = sender;
    }

    /**
     * Returns the list of victims without the fake sender.
     *
     * @return list of victims
     */
    public List<String> getVictims() {
        List<String> victimsWithoutFakeSender = new ArrayList<>();
        for (int i = 1; i < victims.size(); ++i) {
            victimsWithoutFakeSender.add(victims.get(i));
        }
        return victimsWithoutFakeSender;
    }

    /**
     * Returns the fake sender of the group.
     *
     * @return fake sender
     */
    public String getFakeSender() {
        return victims.getFirst();
    }

    /**
     * Returns the real sender of the group.
     *
     * @return real sender
     */
    public String getRealSender() {
        return sender;
    }
}
