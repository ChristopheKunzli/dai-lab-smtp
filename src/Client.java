package src;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 6) {
            System.out.println("Usage : ./client <victimsList.txt> <messages.txt> <N> <prankerEmail> <smtpAddress> <smtpPort>");
            return;
        }

        final String victimsFile = args[0], messageFile = args[1];
        final int groupsCount = Integer.parseInt(args[2]);
        final String prankerEmail = args[3];
        final String smtpAddress = args[4];
        final int smtpPort = Integer.parseInt(args[5]);

        Configuration configuration = new Configuration(prankerEmail);
        if (!configuration.loadVictims(victimsFile) || !configuration.loadMessages(messageFile)) {
            System.out.println("Error while loading configuration");
            return;
        }
        Thread.sleep(1000);

        for (Group group : configuration.formGroups(groupsCount)) {
            try (Socket socket = new Socket(smtpAddress, smtpPort);
                 var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                 var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

                System.out.println("connected to server mail at address:" + smtpAddress + " port:" + smtpPort);
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