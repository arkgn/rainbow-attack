import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HashTest {


    /**
     * I remember that in Java 11- the following unit test was taking approximately 30s.
     * Since java 17+, it only takes 7s (+-)!
     *
     */
    @Test
    public void testHashingPerformance() {
        int numPasswords = 100000;
        int chainSize = 1000;
        List<String> passwords = new ArrayList<>();
        for (int i = 0; i < numPasswords; i++) {
            passwords.add("password" + i);
        }

        long startTime = System.currentTimeMillis();
        passwords.parallelStream().forEach(password -> {
            String hash = DigestUtils.sha256Hex(password);

            for (int i = 1; i < chainSize; i++) {
                hash = DigestUtils.sha256Hex(hash);
            }
        });

        long endTime = System.currentTimeMillis();
        long durationInMillis = (endTime - startTime) ;

        System.out.println("Duration: " + durationInMillis + " ms");
        assertTrue(durationInMillis <= 37000, "Hashing process took longer than expected!");
    }

    /**
     * You can use this to generate hash.txt to test an attack.
     */
    @Test
    void generatePasswordHash() {

        //password size that you want to attack.
        int passwordLength = 6;
        List<String> passwords = new ArrayList<>(PasswordGenerator.genPasswords(passwordLength,100));
        passwords.forEach(password -> {
            String hash = DigestUtils.sha256Hex(password)+System.lineSeparator();
            try {
                Files.write(Path.of("hash.txt"),hash.getBytes(), StandardOpenOption.CREATE,StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
