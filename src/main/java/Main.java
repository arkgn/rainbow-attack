import java.io.IOException;

public class Main {

    private static int passwordSize;
    private static int chainSize;
    private static int tableSize;
    private static String hashPath;


    public static void main(String[] args) throws IOException {

        if (args.length > 1 && "--build".equals(args[0])) {
            handleBuildArgs(args);
            RainbowTable table = new RainbowTable(tableSize, chainSize, passwordSize);
            System.out.println("Please wait...");
            table.genTable();
        } else if (args.length > 1 && "--attack".equals(args[0])) {
            handleAttackArgs(args);
            RainbowTable attack = new RainbowTable("rainbowtable.txt");
            RainbowAttack.crack(RainbowAttack.loadHashes(hashPath), attack);
        } else {
            printUsage();
        }

    }


    public static void handleBuildArgs(String[] args) {

        if (args.length != 7) {
            printUsage();
        }

        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "-t":
                    tableSize = Integer.parseInt(args[++i]);
                    break;
                case "-c":
                    chainSize = Integer.parseInt(args[++i]);
                    break;
                case "-l":
                    passwordSize = Integer.parseInt(args[++i]);
                    break;
                default:
                    System.err.println("Unknown argument: " + args[i]);
                    printUsage();
                    return;
            }
        }

    }

    public static void handleAttackArgs(String[] args) {
        if (args.length != 3) {
            printUsage();
        }

        if ("-h".equals(args[1])) {
            hashPath = args[2];
        } else {
            System.err.println("Unknown argument: " + args[1]);
            printUsage();
        }
    }

    private static void printUsage() {
        System.err.println("Invalid or missing arguments.");
        System.out.println("Usage:");
        System.out.println("./RainbowAttck --attack -h <hashFile>");
        System.out.println("./RainbowAttck --build -t <tableSize> -c <chainSize> -l <passwordSize>");
        System.exit(1);
    }
}
