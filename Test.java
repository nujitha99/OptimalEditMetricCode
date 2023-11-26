import java.math.BigInteger;
import java.util.*;

public class Test {

    public void backtrack(String[] code, List<String>[] candidates, int level, int M, int d) {
        try {
            for (String v : candidates[level]) {
                code[level] = v;
                candidates[level + 1] = new ArrayList<>();
                for (String w : candidates[level]) {
                    // Calculate the hamming distance first
                    int ham = hammingDistance(BigInteger.valueOf(Long.parseLong(w)),
                            BigInteger.valueOf(Long.parseLong(v)));
                    if (ham >= d) {
                        candidates[level + 1].add(w);
                    } else {
                        if (calculateDistance(w, v) >= d) {
                            candidates[level + 1].add(w);
                        }
                    }
                }
                if (level < M) {
                    backtrack(code, candidates, level + 1, M, d);
                } else {
                    for (String word : code) {
                        System.out.print(word + " ");
                    }
                    System.out.println();
                }
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.out.println("Invalid bound: Value M needs to be raised" + exception);
            System.exit(0);
        }

    }

    private int hammingDistance(BigInteger num1, BigInteger num2) {
        return num1.xor(num2).bitCount();
    }

    private List<String> generateAllCodewords(int n, int M, int q) {
        List<String> codewords = new ArrayList<>();
        generateCodewords("", n, M, q, codewords);
        return codewords;
    }

    private void generateCodewords(String current, int n, int M, int q, List<String> codewords) {
        if (current.length() == n) {
            codewords.add(current);
        } else {
            for (int i = 0; i < q; i++) {
                generateCodewords(current + i, n, M, q, codewords);
            }
        }
    }

    private int calculateDistance(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i][j - 1], Math.min(dp[i - 1][j], dp[i - 1][j - 1]));
                }
            }
        }

        return dp[m][n];
    }

    public static void main(String[] args) {
        Test test = new Test();

        int n = 8;
        int d = 5;
        int q = 2;
        int M = 4;

        String[] code = new String[M];
        List<String>[] candidates = new List[M + 1];

        // Initialize the first level of candidates with set of words
        candidates[0] = test.generateAllCodewords(n, M, q);

        test.backtrack(code, candidates, 0, M, d);

        if (Arrays.asList(code).contains(null)) {
            int counter = 0;
            for (String string : code)
                if (string != null)
                    counter++;

            System.out.println("(" + n + ", " + d + ")_" + q + " code with more than "
                    + counter + " codewords cannot exist");
        } else {
            // Display the generated codes
            System.out.println("A valid code exists: ");
            for (int i = code.length-1; i >= 0; i--) {
                System.out.print(code[i] + " ");
            }
//            for (String word : code) {
//                System.out.print(word + " ");
//            }
        }

    }

}
