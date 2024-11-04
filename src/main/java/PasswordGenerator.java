import java.util.HashSet;
import java.util.Random;

public class PasswordGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static HashSet<String> genPasswords(int passwordLength, int nbrPasswd) {
        HashSet<String> passwords = new HashSet<>(nbrPasswd);
        Random random = new Random();
        StringBuilder passwordBuilder = new StringBuilder(passwordLength);

        for (int i = 0; i < nbrPasswd; i++) {
            passwordBuilder.setLength(0);

            for (int j = 0; j < passwordLength; j++) {
                int randomIndex = random.nextInt(CHARACTERS.length());
                passwordBuilder.append(CHARACTERS.charAt(randomIndex));
            }

            passwords.add(passwordBuilder.toString());
        }

        return passwords;
    }

}
