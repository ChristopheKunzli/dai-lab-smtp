package helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Handles sending an email to a group of victims.
 */
public class Handler implements Runnable {
    // Group of victims, first victim is used as fake sender
    private final Group group;
    private final Email email;

    // SMTP server address and port
    private final String smtpAddress;
    private final int smtpPort;

    public Handler(Group group, Email email, String smtpAddress, int smtpPort) {
        this.group = group;
        this.email = email;
        this.smtpAddress = smtpAddress;
        this.smtpPort = smtpPort;
    }

    @Override
    public void run() {

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

            write(out, "Subject:" + email.subject());
            write(out, "Content-Type: text/plain; charset=utf-8" + "\r\n");
            write(out, email.body());
            write(out, "");
            write(out, ".");
            read(in);
            write(out, "QUIT");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Write some text to the server
     *
     * @param writer writer linked to socket
     * @param text   text to write
     */
    private static void write(BufferedWriter writer, String text) {
        try {
            writer.write(text + "\r\n");
            System.out.println("C:" + text);
            writer.flush();
        } catch (Exception e) {
            System.out.println("Error while talking to server: " + e.getMessage());
        }
    }

    /**
     * Read some text from the server
     *
     * @param reader reader linked to socket
     */
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
