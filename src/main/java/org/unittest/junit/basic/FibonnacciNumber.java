package org.unittest.junit.basic;

public class FibonnacciNumber {
    public int find(final int order) {

        if (order <= 0) {
            throw new IllegalArgumentException();
        } else if (order == 1 || order == 2) {
            return 1;
        }

        return find(order - 2) + find(order - 1);
    }
}
