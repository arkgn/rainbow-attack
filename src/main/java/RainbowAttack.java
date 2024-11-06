import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RainbowAttack {

    private static final ConcurrentHashMap<String,String> foundHash = new ConcurrentHashMap<>();
    static void crack(List<String> hashes, RainbowTable table) throws IOException {
        System.out.println("Attacking hash...");
        long start = System.currentTimeMillis();

        AtomicInteger progressCounter = new AtomicInteger();
        int totalHashes = hashes.size();
        int progressBarLength = 50;

        hashes.parallelStream().forEach((hash_) -> {
            for (int i = 1; i < table.getChainSize(); i++) {

                String hash = hash_;
                String reduced = Util.reduce(hash, table.getChainSize() - i - 1, table.getPasswordSize());


                for (int j = table.getChainSize() - i; j < table.getChainSize(); j++) {
                    hash = DigestUtils.sha256Hex(reduced);
                    reduced = Util.reduce(hash, j, table.getPasswordSize());
                }
                checkHash(table, hash_, reduced);

            }

            if (progressCounter.get() % 10 == 0 ) {
                Util.progressBar(progressCounter, progressBarLength, totalHashes);
            }
            progressCounter.incrementAndGet();
        });

        writeFoundHash();

        Util.progressBar(progressCounter, progressBarLength, totalHashes);
        System.out.println("\nAttack finished in : " + ((System.currentTimeMillis() - start) / 1000) + "s");
    }

    private static void checkHash(RainbowTable table, String hash_, String reduced) {
        String potential = table.getTable().get(reduced);
        if (potential != null) {
            String hashCheck;
            // Hash the potential password through the entire chain to see if we get the original hash
            for (int j = 0; j <= table.getChainSize(); j++) {
                hashCheck = DigestUtils.sha256Hex(potential);
                if (hashCheck.equals(hash_)) {
                    // We have found the password hash
                    foundHash.put(hash_,potential);
                    break;
                }
                // Reduce the hash to move to the next step of the chain
                potential = Util.reduce(hashCheck, j, table.getPasswordSize());
            }
        }
    }


    private static void writeFoundHash() throws IOException {

        Files.write(Path.of("found.txt"), () -> RainbowAttack.foundHash.entrySet().stream()
                .<CharSequence>map(e -> e.getKey() + ";" + e.getValue())
                .iterator(), StandardOpenOption.CREATE);
    }

    //According to the modalities, we must be able to attack about 100 hashes, we can therefore put all them in memory.
    static List<String> loadHashes(String path) {
        try {
            return new ArrayList<>(Files.readAllLines(Path.of(path)));
        }catch (Exception e){
            System.out.println("Please put the hash file you want to attack in this directory.");
            System.out.println("Note: file should be in txt format and each line should be a new hash.");
            System.exit(1);
            return null;
        }
    }

}