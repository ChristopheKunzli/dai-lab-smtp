package src.client;

import src.utils.Configuration;

public class Client {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage : ./client <victimsList>.txt <messages>.txt <N>");
            return;
        }

        String victimsFile = args[0], messageFile = args[1];
        int groupsCount = Integer.parseInt(args[2]);

        Configuration configuration = new Configuration(victimsFile, messageFile, groupsCount);

    }
}
