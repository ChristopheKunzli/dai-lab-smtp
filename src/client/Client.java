package src.client;

import src.utils.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

public class Client {
    private static final String pranker = "pranker.pranking@prankService.com";
    private static final String smtpAdress = "localhost";
    private static final int smtpPort = 1025;

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage : ./client <victimsList.txt> <messages.txt> <N>");
            return;
        }

        String victimsFile = args[0], messageFile = args[1];
        int groupsCount = Integer.parseInt(args[2]);

        Configuration configuration = new Configuration(victimsFile, messageFile, groupsCount);

        List<Email> messages = configuration.getMessages();

        try (Socket socket = new Socket(smtpAdress, smtpPort);
             var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

            System.out.println("connected to server mail at address:" + smtpAdress + " port:" + smtpPort);

            read(in);
            write(out, "EHLO test");
            read(in);
            write(out, "MAIL FROM:<" + pranker + ">");
            read(in);
            for (String victim : configuration.getVictims()) {
                write(out, "RCPT TO:<" + victim + ">");
                read(in);
            }
            write(out, "DATA");
            read(in);
            write(out, "From:<" + configuration.getFakeSender() + ">");
            read(in);
            write(out, "To:");
            read(in);
            Email email = messages.get(new Random().nextInt() % messages.size());
            write(out, "Subject:" + email.getSubject());
            read(in);
            write(out, "Content-Type: text/plain; charset=utf-");
            read(in);
            write(out, email.getBody());
            read(in);
            write(out, ".");
            read(in);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void write(BufferedWriter writer, String text) {
        try {
            Thread.sleep(2000);
            writer.write(text + "\n");
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
            }
        } catch (Exception e) {
            System.out.println("Error while reading server: " + e.getMessage());
        }
    }
}
