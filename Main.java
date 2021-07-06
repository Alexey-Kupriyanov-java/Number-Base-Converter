package converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Scanner;

public class Main {

    public static final String DIGITS = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) {
        // write your code here
        Scanner scanner = new Scanner(System.in);
        String answer;
        int sourceBase;
        int targetBase;

        while (true) {
            System.out.print("Enter two numbers in format: {source base} {target base}? " +
                    "(To quit type /exit) ");
            answer = scanner.nextLine();

            if (answer.equals("/exit")) {
                return;
            }

            sourceBase = Integer.parseInt(answer.split(" ")[0]);
            targetBase = Integer.parseInt(answer.split(" ")[1]);

            while (true) {
                System.out.printf("Enter number in base %d to convert to base %d " +
                        "(To go back type /back) ", sourceBase, targetBase);
                answer = scanner.nextLine();

                if (answer.equals("/back")) {
                    break;
                }

                System.out.print("Conversion result: ");
                System.out.println(fromDecimal(toDecimal(answer, sourceBase), targetBase, 5));

            }
        }
    }

    static String toDecimal(String number, int radix) {
        BigDecimal result = BigDecimal.ZERO;

        String[] array = number.split("\\.");

        for (int i = 0; i < array[0].length(); i++) {
            int val = DIGITS.indexOf(array[0].charAt(i));
            result = result.add(BigDecimal.valueOf(val * Math.pow(radix, array[0].length() - 1 - i)));
        }

        if (array.length == 2) {
            for (int i = 0; i < array[1].length(); i++) {
                int val = DIGITS.indexOf(array[1].charAt(i));
                result = result.add(BigDecimal.valueOf(val * Math.pow(radix, (i + 1) * -1)));
            }
        } else {
            result = result.setScale(0);
        }

        return result.toString();
    }

    static String fromDecimal(String number, int radix, int scale) {
        StringBuilder result = new StringBuilder();

        String[] array = number.split("\\.");
        BigInteger integer = new BigDecimal(array[0]).toBigInteger();

        do {
            result.append(DIGITS.charAt(integer.divideAndRemainder(BigInteger.valueOf(radix))[1].intValue()));
            integer = integer.divide(BigInteger.valueOf(radix));
        } while (integer.compareTo(BigInteger.ZERO) > 0);
        result = result.reverse();

        if (array.length == 2) {
            result.append(".");
            BigDecimal remainder = new BigDecimal("0." + array[1]);
            for (int i = 0; i < scale; i++) {
                BigDecimal val = remainder.multiply(BigDecimal.valueOf(radix)).setScale(scale, RoundingMode.UP);
                remainder = val.remainder(BigDecimal.ONE);
                result.append(DIGITS.charAt(val.setScale(0, RoundingMode.DOWN).intValue()));
            }
        }

        return result.toString();
    }
}
