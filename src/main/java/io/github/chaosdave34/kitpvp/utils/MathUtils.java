package io.github.chaosdave34.kitpvp.utils;

public class MathUtils {
    public static int binomialCoefficient(int n, int k) {
        if (k > n - k)
            k = n - k;

        int b = 1;
        for (int i = 1, m = n; i <= k; i++, m--)
            b = b * m / i;

        return b;
    }
}
