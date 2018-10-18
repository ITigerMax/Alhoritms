package com.l4g.armstrong;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ArmstrongNumbers {
    private static long myNumber = Long.MAX_VALUE;
    private static Set<Long> armstrongNumbers = new HashSet<>();

    public static void main(String[] args) {
        long t1 = System.currentTimeMillis()/1000;

        long[] armstrong = getNumbers(myNumber);
        long t2 = System.currentTimeMillis()/1000;

        System.out.println(Arrays.toString(armstrong));
        System.out.println();
        System.out.println(t2 - t1 + " sec");
    }

    public static long[] getNumbers(long N) {
        long[] result = null;
        int index = 0;

        if (N > 0) {
            getAllArmstrongNumbers(N);
            result = new long[armstrongNumbers.size()];
            for(long temp : armstrongNumbers){
                result[index++] = temp;
    
            }

            Arrays.sort(result);
        }

        return result;

    }

    private static void getAllArmstrongNumbers(long N){
        N = toCurrentOrder(N);
        int currentLengthOfN = getDigitQuantity(N);
        if (N > 0 && N < 10){

            for (int i = 1; i < N; i++) {
                armstrongNumbers.add((long)i);
            }

        }else if (N > 10) {

            for (int i = 1; i < 10; i++) {
                armstrongNumbers.add((long) i);
            }

            for (long number = 37; number <= N; number++) {
                char[] digits = String.valueOf(number).toCharArray();
                for (int charIndex = 0; charIndex < digits.length - 1; charIndex++) {
                    if (digits[charIndex] > digits[charIndex + 1]){
                        digits[charIndex + 1] = digits[charIndex];
                    }
                }
                try {
                    number = Long.parseLong(String.valueOf(digits));
                    long invariant = number;
                    long result = getProbablyArmstrongNumber(number);
                    if (result != 0 &&isArmstrong(result)){
                        armstrongNumbers.add(result);
                    }
                    int currentNumberLength = getDigitQuantity(number);

                    if (currentNumberLength <= currentLengthOfN){
                        for (int i = currentNumberLength; i < currentLengthOfN; i++) {
                            invariant *= 10;
                            result = getProbablyArmstrongNumber(invariant);
                            if (result != 0 &&isArmstrong(result)){
                                armstrongNumbers.add(result);
                            }
                            i = getDigitQuantity(result);
                        }
                    }
                } catch (NumberFormatException e) {
                    return;
                }
            }

        }

    }

    private static long getProbablyArmstrongNumber(long N){

        int power = getDigitQuantity(N);
        long result = 0;
        while (N > 0){
            int digit = (int)(N % 10);
            N /= 10;
            result += pow(digit, power);
        }
        return result;
    }

    private static long toCurrentOrder(long N){

        int currentLengthOfN = getDigitQuantity(N);
        int quantityOfZero = 0;

        boolean hasZero = false;

        char[] digits = String.valueOf(N).toCharArray();
        Arrays.sort(digits);

        for (char c : digits) {
            if (c == '0'){
                quantityOfZero++;
                hasZero = true;
            }else break;
        }

        if (hasZero){
            digits = Arrays.copyOfRange(digits, quantityOfZero, digits.length);
        }
        long result = Long.parseLong(String.valueOf(digits));
        int resultLength = getDigitQuantity(result);
        if (resultLength < currentLengthOfN){
            for (int i = resultLength; i < currentLengthOfN; i++) {
                result *= 10;
            }
            result -= 1;
        }

        return result;
    }

    private static long pow(int digit, int power){
        long result = digit;
        for (int i = 0; i < power - 1; i++) {
            result *= digit;
        }
        return result;
    }

    private static int getDigitQuantity(long N){
        return (int) Math.log10(N) + 1;
    }

    private static boolean isArmstrong(long N){
        boolean isArmstrong = false;
        long result = getProbablyArmstrongNumber(N);

        if (result == N) {
            isArmstrong = true;
        }

        return isArmstrong;
    }
}