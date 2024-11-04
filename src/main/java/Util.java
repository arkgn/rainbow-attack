import java.util.concurrent.atomic.AtomicInteger;

public final class Util {

    public static void progressBar(AtomicInteger progressCounter, int progressBarLength, int totalPasswords) {
        int progress = (progressCounter.get() * progressBarLength / totalPasswords);
        String bar = "[" + "=".repeat(progress) + " ".repeat(progressBarLength - progress) + "]";
        System.out.print("\rProgress: " + bar + " " + (progressCounter.get() * 100 / totalPasswords) + "%");
    }

    public static String reduce(String hash, int index, int passwordSize) {
        long x;
        if (index % 2 == 0) {
            x = Long.parseUnsignedLong(hash.substring(0, 15), 16);
        } else if (index % 3 == 0) {
            x = Long.parseUnsignedLong(hash.substring(15, 15 * 2), 16);
        } else {
            x= Long.parseUnsignedLong(hash.substring(0, 15), 16) +
                    Long.parseUnsignedLong(hash.substring(15, 15 * 2), 16);
        }

        StringBuilder password = getReduced(index, x,passwordSize);

        return password.toString();
    }

    public static StringBuilder getReduced(int index, long x,int passwordSize) {
        long P = 839299365868340200L;

        long remaining = (x + index) % P;

        StringBuilder password = new StringBuilder(passwordSize);
        String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int alphaLength = ALPHA.length();

        for (int i = 0; i < passwordSize; i++) {
            int alphaIndex = (int) (remaining % alphaLength);
            password.append(ALPHA.charAt(alphaIndex));
            remaining /= alphaLength;
        }
        return password;
    }
}
