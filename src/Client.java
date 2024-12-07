package src;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    private static final String PRANKER = "pranker.pranking@prankService.com";
    private static final String SMTP_ADDRESS = "localhost";
    private static final int SMTP_PORT = 1025;

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage : ./client <victimsList.txt> <messages.txt> <N>");
            return;
        }

        final String victimsFile = args[0], messageFile = args[1];
        final int groupsCount = Integer.parseInt(args[2]);

        Configuration configuration = new Configuration(PRANKER);
        if (!configuration.loadVictims(victimsFile) || !configuration.loadMessages(messageFile)) {
            System.out.println("Error while loading configuration");
            return;
        }

        for (Group group : configuration.formGroups(groupsCount)) {
            try (Socket socket = new Socket(SMTP_ADDRESS, SMTP_PORT);
                 var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                 var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

                System.out.println("connected to server mail at address:" + SMTP_ADDRESS + " port:" + SMTP_PORT);
                read(in);
                write(out, "EHLO test");
                read(in);
                write(out, "MAIL FROM:<" + group.getRealSender() + ">");
                read(in);
                for (String victim : group.getVictims()) {
                    write(out, "RCPT TO:<" + victim + ">");
                    read(in);
                }
                write(out, "DATA");
                read(in);
                write(out, "From:<" + group.getFakeSender() + ">");
                write(out, "To:" + String.join(",", group.getVictims()));

                Email email = configuration.getRandomMessage();

                write(out, "Subject:" + email.getSubject());
                write(out, "Content-Type: text/plain; charset=utf-8" + "\r\n");
                write(out, email.getBody());
                write(out, "");
                write(out, ".");
                read(in);
                write(out, "QUIT");

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void write(BufferedWriter writer, String text) {
        try {
            writer.write(text + "\r\n");
            System.out.println("C:" + text);
            writer.flush();
        } catch (Exception e) {
            System.out.println("Error while talking to server: " + e.getMessage());
        }
    }

    private static void read(BufferedReader reader) {
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("S:" + line);
                if (line.charAt(3) == ' ') {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error while reading server: " + e.getMessage());
        }
    }
}