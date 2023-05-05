package org.unittest.unittestnedir;

public class FizzBuzz {
    public String stringFor(final int number) {

        if (number % 15 == 0) {
            return "FizzBuzz";
        } else if (number % 3 == 0) {
            return "Fizz";
        } else if (number % 5 == 0) {
            return "Buzz";
        } else if (number > 100 || number < 0) {
            throw new IllegalArgumentException();
        }

        return String.valueOf(number);
    }
}
