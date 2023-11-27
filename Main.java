import java.util.*;

// TODO: equivalence testing

public class Main {

    HashMap<String, Integer> distancesMap = new HashMap<>();

    int prevLevel = 0;

    private static Set<String> exploredPermutations = new HashSet<>();

    private static boolean areEquivalent(String[] code, int level) {
        // Convert the code array to a string for easy comparison
        String currentPermutation = Arrays.toString(Arrays.copyOfRange(code, 0, level + 1));

        // Check if the current permutation has been explored before
        if (exploredPermutations.contains(currentPermutation)) {
            return true; // The current permutation is equivalent to a previously explored one
        } else {
            // If not, add the current permutation to the set of explored permutations
            exploredPermutations.add(currentPermutation);
            return false; // The current permutation is not equivalent to any previous one
        }
    }

    public void backtrack(String[] code, List<String>[] candidates, int level, int M, int d) {
        if (level >= M) {
            return;
        }

        if (level < prevLevel) return;
        prevLevel = level;

        try {
            for (String v : candidates[level]) {
                code[level] = v;
                candidates[level + 1] = new ArrayList<>();
                for (String w : candidates[level]) {
                    if (distancesMap.get(w+v) >= d) {
                        candidates[level + 1].add(w);
                    }
                }
                backtrack(code, candidates, level + 1, M, d);
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.out.println("Invalid bound: Value M needs to be raised" + exception);
            System.exit(0);
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

    private void computeDistances(List<String> codewords){
        for (String codeword1 : codewords) {
            for (String codeword2 : codewords) {
                int dist = calculateEditDistance(codeword1, codeword2);
                String key = codeword1 + codeword2;
                distancesMap.put(key, dist);
            }
        }
    }

    public static void main(String[] args) {
        Main test = new Main();

        int n = 8;
        int d = 4;
        int q = 2;
        int M = 10;

        String[] code = new String[M];
        List<String>[] candidates = new List[M + 1];

        List<String> generatedCodewords = test.generateAllCodewords(n, M, q);
        candidates[0] = generatedCodewords;

        test.computeDistances(generatedCodewords);

        test.backtrack(code, candidates, 0, M, d);

        if (Arrays.asList(code).contains(null)) {
            int counter = (int) Arrays.stream(code).filter(Objects::nonNull).count();
            System.out.println("(" + n + ", " + d + ")" + q + " code with more than "
                    + counter + " codewords cannot exist");
        } else {
            System.out.println("A valid code exists: ");
            for (int i = code.length-1; i >= 0; i--) {
                System.out.print(code[i] + " ");
            }
        }
    }
}
