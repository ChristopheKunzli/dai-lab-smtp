
import helpers.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    public static void main(String[] args) {
        if (args.length != 6) {
            System.out.println("Usage : ./client <victimsList.txt> <messages.txt> <N> <prankerEmail> <address> <port>");
            return;
        }

        // Parse arguments
        final String victimsFile = args[0], messageFile = args[1];
        final int groupsCount = Integer.parseInt(args[2]);
        final String prankerEmail = args[3];
        final String smtpAddress = args[4];
        final int smtpPort = Integer.parseInt(args[5]);

        // Load configuration
        Configuration configuration = new Configuration(prankerEmail);
        if (!configuration.loadVictims(victimsFile) || !configuration.loadMessages(messageFile)) {
            System.out.println("Error while loading configuration");
            return;
        }

        // Run the prank campaign on virtual threads, one thread per group
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        for (Group group : configuration.formGroups(groupsCount)) {
            executor.execute(new Handler(group, configuration.getRandomMessage(), smtpAddress, smtpPort));
        }

        executor.close();
    }
}