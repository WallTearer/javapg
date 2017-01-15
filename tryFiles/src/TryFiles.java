import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Created by Kirill on 13-Jan-17.
 */
public class TryFiles {
    private static final String FILE_ENCODING = "UTF-8";
    private static final String FILE_DIR = "." + File.separator + "tmp" + File.separator;

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        if (!ensureDirExists()) {
            return;
        }

        Scanner consoleScanner = new Scanner(System.in);

        System.out.print("Provide file name: ");
        String fileName = consoleScanner.nextLine().trim();

        PrintWriter fileWriter;
        String filePath = FILE_DIR + fileName;
        try {
            fileWriter = new PrintWriter(filePath, FILE_ENCODING);
        } catch (IOException ex) {
            System.out.println("Failed to open a file for writing because of exception: " + ex);
            return;
        }

        System.out.print("Provide content that will be written to the file: ");
        String content = consoleScanner.nextLine();

        byte times = 0;
        do {
            System.out.printf("Provide number of times this content should be written to the file (between 1 and 20): ");
            if (!consoleScanner.hasNextByte()) {
                consoleScanner.next();
                System.out.println("What you've provided is not a byte number, please try again");
                continue;
            }

            times = consoleScanner.nextByte();
            if (times < 1 || times > 20) {
                times = 0;
                System.out.println("We expect times to be >= 1 and <= 20, so please try again");
            }
        } while (times == 0);
        consoleScanner.nextLine(); // ensuring that we passed the whole line

        for (int i=0; i<times; i++) {
            fileWriter.println(content);
        }
        fileWriter.close();

        System.out.printf("Content was written %d times to the file %s%n", times, filePath);

        String seeContents;
        do {
            System.out.print("Do you want to see contents of the created file? Y/N: ");
            seeContents = consoleScanner.nextLine();
            if (!seeContents.equalsIgnoreCase("Y") && !seeContents.equalsIgnoreCase("N")) {
                System.out.println("Please answer with Y or N");
                seeContents = null;
            }
        } while (seeContents == null);

        if (seeContents.equalsIgnoreCase("Y")) {
            Scanner fileReader;
            try {
                fileReader = new Scanner(Paths.get(filePath), FILE_ENCODING);
            } catch (IOException ex) {
                System.out.println("Failed to open the file for reading because of exception: " + ex);
                return;
            }
            while (fileReader.hasNextLine()) {
                System.out.println(fileReader.nextLine());
            }
            fileReader.close();
        }

        System.out.println("Done.");
    }

    private static boolean ensureDirExists() {
        if (!Files.exists(Paths.get(FILE_DIR))) {
            try {
                Files.createDirectory(Paths.get(FILE_DIR));
            } catch (IOException ex) {
                System.out.println("Failed to create a temp directory because of exception: " + ex);
                return false;
            }
        }
        return true;
    }
}
