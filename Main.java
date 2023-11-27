import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main {

    public void backtrack(String[] code, List<String>[] candidates, int level, int M, int d) {
        if (level >= M) {
//            StringBuilder result = new StringBuilder();
//            for (int i = code.length - 1; i >= 0; i--) {
//                result.append(code[i]).append(" ");
//            }
//            System.out.println(result.toString());
            return;
        }

        try {
            for (String v : candidates[level]) {
                code[level] = v;
                candidates[level + 1] = new ArrayList<>();
                for (String w : candidates[level]) {
                    int ham = hammingDistance(Long.parseLong(w), Long.parseLong(v));
                    if (ham >= d) {
                        candidates[level + 1].add(w);
                    } else {
                        if (calculateDistance(w, v) >= d) {
                            candidates[level + 1].add(w);
                        }
                    }
                }
                backtrack(code, candidates, level + 1, M, d);
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.out.println("Invalid bound: Value M needs to be raised" + exception);
            System.exit(0);
        }
    }

    private int hammingDistance(long num1, long num2) {
        return Long.bitCount(num1 ^ num2);
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
            StringBuilder builder = new StringBuilder(current);
            for (int i = 0; i < q; i++) {
                generateCodewords(builder.append(i).toString(), n, M, q, codewords);
                builder.setLength(builder.length() - 1);
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
        Main test = new Main();

        int n = 8;
        int d = 5;
        int q = 2;
        int M = 4;

        String[] code = new String[M];
        List<String>[] candidates = new List[M + 1];

        candidates[0] = test.generateAllCodewords(n, M, q);

        test.backtrack(code, candidates, 0, M, d);

        if (Arrays.asList(code).contains(null)) {
            int counter = (int) Arrays.stream(code).filter(Objects::nonNull).count();
            System.out.println("(" + n + ", " + d + ")_" + q + " code with more than "
                    + counter + " codewords cannot exist");
        } else {
            System.out.println("A valid code exists: ");
        }
    }
}
