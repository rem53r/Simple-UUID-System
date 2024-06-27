import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

public class Main {

    // raw
    public static final String UrlWithHwids = "";
    public static String uuid;

    // get hwid
    public static String getHWID() throws IOException {
        String command = "wmic csproduct get uuid";
        Process process = Runtime.getRuntime().exec(command);
        Scanner scanner = new Scanner(process.getInputStream());

        StringBuilder output = new StringBuilder();
        while (scanner.hasNext()) {
            output.append(scanner.next());
        }

        String uuid = output.toString().trim();
        if (uuid.contains("UUID")) {
            uuid = uuid.replace("UUID", "").trim();
        }

        return uuid;
    }

    public static int checkUUID(String uuid) throws IOException {
        URL url = new URL(UrlWithHwids);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equalsIgnoreCase(uuid)) {
                    return lineNumber;
                }
                lineNumber++;
            }
        }
        return -1;
    }

    public static String UID(boolean exit) throws IOException {
        try {
            uuid = getHWID();
        } catch (IOException e) {
            uuid = "Unknown";
        }

        int lineNumber;
        try {
            lineNumber = checkUUID(uuid);
        } catch (IOException e) {
            lineNumber = -1;
        }

        // if hwid not found in the db = exit
        if (exit) {
            if (lineNumber == -1) {
                System.out.println("HWID not found in the database. HWID: " + getHWID());
                System.exit(0);
            }
        }

        return String.valueOf(lineNumber);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("UID: " + UID(true) + " | HWID: " + getHWID());
    }

}