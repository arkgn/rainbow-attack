import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RainbowTableTest {
    public RainbowTable rainbowTable;
    private static final int TABLE_SIZE = 1000000;
    private static final int CHAIN_SIZE = 1000;
    private static final int PASSWORD_SIZE = 6;

    @BeforeEach
    void setUp() {
        rainbowTable = new RainbowTable(TABLE_SIZE, CHAIN_SIZE, PASSWORD_SIZE);
        //rainbowTable.genTable();
    }

    @Test
    void testReductionConsistency() {
        String hash = "0d68d60423721a3742a2bd43b06b06a9a4568fbe6ac86e70f23fcd7c430bb7ef";
        int index = 0;

        String reduced1 = Util.reduce(hash, index, PASSWORD_SIZE);
        String reduced2 = Util.reduce(hash, index, PASSWORD_SIZE);
        System.out.println(reduced2);

        assertEquals(reduced1, reduced2, "Reduction function is consistent!");
    }

    @Test
    void testHashReduceChainConsistency() {
        String password = "hqiwmU";
        String hash = DigestUtils.sha256Hex(password);
        String reduced = Util.reduce(hash, 0, PASSWORD_SIZE);

        System.out.println("Initial password: " + password);
        System.out.println("Initial hash: " + hash);
        System.out.println("Initial reduction: " + reduced);

        for (int i = 1; i < CHAIN_SIZE; i++) {
            hash = DigestUtils.sha256Hex(reduced);
            reduced = Util.reduce(hash, i, PASSWORD_SIZE);

            //System.out.println("Iteration " + i + " - Hash: " + hash + ", Reduction: " + reduced);
        }

        String finalHash = hash;
        String finalReduced = reduced;

        hash = DigestUtils.sha256Hex(password);
        reduced = Util.reduce(hash, 0, PASSWORD_SIZE);
        for (int i = 1; i < CHAIN_SIZE; i++) {
            hash = DigestUtils.sha256Hex(reduced);
            reduced = Util.reduce(hash, i, PASSWORD_SIZE);

        //    System.out.println("@@@@@@@@Iteration " + i + " - Hash: " + hash + ", Reduction: " + reduced);
        }

        //System.out.println("Final reduction comparison - Expected: " + finalReduced + ", Actual: " + reduced);

        assertEquals(finalReduced, Util.reduce(finalHash, CHAIN_SIZE - 1, PASSWORD_SIZE), "Hash-reduction chain is inconsistent!");
    }

//    @Test
//    void attackTest() throws IOException {
//
//        RainbowAttack.crack(List.of("047828a55e4e95f5e22190b15d28f6833026835e4a61df6dd3b2a37841a52196"),rainbowTable);
//    }
}
