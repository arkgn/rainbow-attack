import java.io.IOException;

public class Main {

    private static int passwordSize;
    private static int chainSize;
    private static int tableSize;
    private static String mode = "";
    private static String hashPath;

    public static void main(String[] args) throws IOException {

        if (args.length >1 && "--build".equals(args[0])){
            handleBuildArgs(args);

        }else if (args.length >1 && "--attack".equals(args[0])){
            handleAttackArgs(args);
        }else {
            System.out.println("Usage:");
            System.out.println("  ./RainbowAttck --attack -h <hashFile>");
            System.out.println("  ./RainbowAttck --build -t <tableSize> -c <chainSize> -l <passwordSize>");
        }

        switch (mode) {
            case "build":
                RainbowTable table = new RainbowTable(tableSize, chainSize, passwordSize);
                System.out.println("Please wait...");
                table.genTable();
                break;

            case "attack":
                RainbowTable attack = RainbowTable.loadTable("rainbowtable.txt");
                RainbowAttack.crack(RainbowAttack.loadHashes(hashPath),attack);
                break;
        }

    }


    public static void handleBuildArgs(String[] args) {

        if (args.length == 7 && "--build".equals(args[0])) {
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
                        System.out.println("Unknown argument: " + args[i]);
                        System.out.println("Usage: ./RainbowAttck --build -t <tableSize> -c <chainSize> -l <passwordSize>");
                        return;
                }
            }
            mode = "build";
        } else {
            System.out.println("Invalid or missing arguments.");
            System.out.println("Usage: ./RainbowAttck --build -t <tableSize> -c <chainSize> -l <passwordSize>");
            System.out.println("Usage: ./RainbowAttck --attack -h <hashFile>");
        }


    }

    public static void handleAttackArgs(String[] args) {
        if (args.length != 3 || !"--attack".equals(args[0])) {
            printUsage();
            return;
        }

        if ("-h".equals(args[1])) {
            hashPath = args[2];
            mode = "attack";
        } else {
            System.out.println("Unknown argument: " + args[1]);
            printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("Invalid or missing arguments.");
        System.out.println("Usage:");
        System.out.println("  ./RainbowAttck --attack -h <hashFile>");
        System.out.println("  ./RainbowAttck --build -t <tableSize> -c <chainSize> -l <passwordSize>");
    }
}
