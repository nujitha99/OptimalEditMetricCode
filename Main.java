import java.util.*;

// TODO: equivalence testing

public class Main {

    HashMap<String, Integer> distancesMap = new HashMap<>();

    private void backtrack(String[] code, List<String>[] candidates, int level, int M, int d) {

        for (String v : candidates[level]) {

            code[level] = v;
            candidates[level + 1] = new ArrayList<>();

            for (int i = 1; i < candidates[level].size(); i++) {
                String w = candidates[level].get(i);
                // Restrict words based on start symbol
                int x = Math.abs(M / q) + 1;
                if (String.valueOf(v.charAt(0)).equals("1") && (level+1 < (x))) continue;
                if (distancesMap.get(w + v) >= d) {
                    candidates[level + 1].add(w);
                }
            }

            // Backtrack immediately if not enough candidates are available
            if (candidates[level+1].size() < M - (level+1)) {
                backtrack(code, candidates, level + 1, M, d);
            }

            if (level < M) {
                backtrack(code, candidates, level + 1, M, d);
            } else {
                for (String codew : code) {
                    System.out.print(codew + " ");
                }
                System.out.println();
            }
        }

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

    // Calculate edit distance
    private int calculateEditDistance(String s1, String s2) {
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

    // Pre-compute the edit distances and store in a map
    private void computeDistances(List<String> codewords) {
        for (String codeword1 : codewords) {
            for (String codeword2 : codewords) {
                int dist = calculateEditDistance(codeword1, codeword2);
                String key = codeword1 + codeword2;
                distancesMap.put(key, dist);
            }
        }
    }

    // Prints the summary
    private void checkGeneratedCodes(String[] code) {
        if (Arrays.asList(code).contains(null)) {
            int counter = (int) Arrays.stream(code).filter(Objects::nonNull).count();
            System.out.println("(" + n + ", " + d + ")" + q + " code with more than "
                    + counter + " codewords cannot exist");
        } else {
            System.out.println("A valid code exists: ");
            for (String word : code) {
                System.out.print(word + " ");
            }
        }
        System.exit(0);
    }

    static int n = 3;
    static int d = 2;
    static int q = 2;
    static int M = 4;

    public static void main(String[] args) {
        Main test = new Main();

        String[] code = new String[M];
        List<String>[] candidates = new List[M + 1];

        List<String> generatedCodewords = test.generateAllCodewords(n, M, q);
        candidates[0] = generatedCodewords;

        test.computeDistances(generatedCodewords);

        test.backtrack(code, candidates, 0, M, d);

        test.checkGeneratedCodes(code);
    }
}
