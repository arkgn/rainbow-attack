import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RainbowTable {

    private int tableSize;
    private int chainSize;
    private int passwordSize;
    private ConcurrentHashMap<String, String> table;

    RainbowTable(int tableSize, int chainSize, int passwordSize) {
        this.tableSize = tableSize;
        this.chainSize = chainSize;
        this.passwordSize = passwordSize;
        this.table = new ConcurrentHashMap<>(tableSize);
    }

    public void genTable() {

        HashSet<String> passwords = PasswordGenerator.genPasswords(passwordSize, tableSize);
        long startTime = System.currentTimeMillis();

        AtomicInteger progressCounter = new AtomicInteger();
        int totalPasswords = passwords.size();
        int progressBarLength = 50;

        passwords.parallelStream().forEach((password) -> {

            String hash = DigestUtils.sha256Hex(password);
            String reduced = Util.reduce(hash, 0, passwordSize);

            for (int i = 1; i < chainSize; i++) {
                hash = DigestUtils.sha256Hex(reduced);
                reduced = Util.reduce(hash, i, passwordSize);
            }

            table.put(reduced, password);

            if (progressCounter.get() % 100000 == 0 ) {
                Util.progressBar(progressCounter, progressBarLength, totalPasswords);
            }
            progressCounter.incrementAndGet();

        });

        //we have to call it one last time for 100%
        Util.progressBar(progressCounter, progressBarLength, totalPasswords);

        System.out.println("\ngenerated in : " + (System.currentTimeMillis() - startTime) / 1000 + "s");

        Path mOutputPath = Path.of("rainbowtable.txt");

        writeToFile(mOutputPath);


    }

    private void writeToFile(Path mOutputPath) {
        try {
            String header = tableSize + ";" + chainSize + ";" + passwordSize;

            //We need tableSize,chainSize and passwordSize on load
            Files.write(mOutputPath, List.of(header), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            Files.write(mOutputPath, () -> table.entrySet().stream()
                    .<CharSequence>map(e -> e.getKey() + ";" + e.getValue())
                    .iterator(), StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.out.println("Something bad happened while writing the rainbow table to rainbowtable.twt");
        }
    }


    RainbowTable(String filePath) {
        Path path = Path.of(filePath);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            // Read first line for table parameters
            String firstLine = reader.readLine();
            String[] sizes = firstLine.split(";");

            if (sizes.length != 3) {
                throw new IOException("Invalid format in the first line. Expected 3 values.");
            }

            tableSize = Integer.parseInt(sizes[0]);
            chainSize = Integer.parseInt(sizes[1]);
            passwordSize = Integer.parseInt(sizes[2]);
            table = new ConcurrentHashMap<>(tableSize);

            // Read the table entries
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    String hash = parts[0].trim();
                    String password = parts[1].trim();
                    this.table.put(hash, password);
                }
            }
        } catch (IOException e) {
            System.out.println("Please generate a rainbow table before running an attack.");
            System.exit(1);
        }
    }


    public ConcurrentHashMap<String, String> getTable() {
        return table;
    }


    public int getChainSize() {
        return chainSize;
    }

    public int getPasswordSize() {
        return passwordSize;
    }
}