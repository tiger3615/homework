package xyz.trieye.assignment.deal.common;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Tools {
    public static DecimalFormat priceFormat = new DecimalFormat("#.###");

    private static NormalDistribution distribution = new NormalDistribution(0, 1);

    public static double cdf(double top) {
        return distribution.cumulativeProbability(top);
    }


    public static BigDecimal calculateD1(BigDecimal stockCurrentPrice,
                                         BigDecimal optionStrikePrice,
                                         BigDecimal riskFreeInterestRate,
                                         BigDecimal deviation,
                                         int years) {
        double left = Math.log(stockCurrentPrice.divide(optionStrikePrice, 10, BigDecimal.ROUND_HALF_UP).doubleValue());
        BigDecimal right = deviation.pow(2).divide(new BigDecimal(2)).add(riskFreeInterestRate).multiply(new BigDecimal(years));
        BigDecimal bottom = deviation.multiply(new BigDecimal(Math.sqrt(years)));
        return right.add(new BigDecimal(left)).divide(bottom, 10, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal calculateD2(BigDecimal d1,
                                         BigDecimal deviation,
                                         int years) {
        return d1.subtract(deviation.multiply(new BigDecimal(Math.sqrt(years))));
    }

    public static BigDecimal calculateCallOptionPrice(BigDecimal stockCurrentPrice,
                                                      BigDecimal optionStrikePrice,
                                                      BigDecimal riskFreeInterestRate,
                                                      BigDecimal deviation,
                                                      int years) {
        BigDecimal d1 = calculateD1(stockCurrentPrice, optionStrikePrice, riskFreeInterestRate, deviation, years);
        BigDecimal left = stockCurrentPrice.multiply(new BigDecimal(cdf(d1.doubleValue())));
        Double right1 = Math.pow(Math.E, riskFreeInterestRate.multiply(new BigDecimal(years)).negate().doubleValue());
        BigDecimal right2 = optionStrikePrice.multiply(new BigDecimal(right1));
        BigDecimal d2 = calculateD2(d1, deviation, years);
        BigDecimal right = right2.multiply(new BigDecimal(cdf(d2.doubleValue())));
        return left.subtract(right);
    }

    public static BigDecimal calculatePutOptionPrice(BigDecimal stockCurrentPrice,
                                                     BigDecimal optionStrikePrice,
                                                     BigDecimal riskFreeInterestRate,
                                                     BigDecimal deviation,
                                                     int years) {
        BigDecimal d1 = calculateD1(stockCurrentPrice, optionStrikePrice, riskFreeInterestRate, deviation, years);
        double left1 = Math.pow(Math.E, riskFreeInterestRate.multiply(new BigDecimal(years)).negate().doubleValue());
        BigDecimal left2 = optionStrikePrice.multiply(new BigDecimal(left1));
        BigDecimal d2 = calculateD2(d1, deviation, years);
        BigDecimal left = left2.multiply(new BigDecimal(cdf(d2.negate().doubleValue())));
        BigDecimal right = stockCurrentPrice.multiply(new BigDecimal(cdf(d1.negate().doubleValue())));
        return left.subtract(right);
    }

    public static String extendWithSpaces(String input, int size, boolean fillLeft) {
        char[] newChars = new char[size];
        char[] inputChar = input.toCharArray();
        if (fillLeft) {
            int lastSpacePosition = size - input.length();
            for (int i = 0; i < size; i++) {
                if (i < lastSpacePosition) {
                    newChars[i] = ' ';
                } else {
                    newChars[i] = inputChar[i - lastSpacePosition];
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (i < input.length()) {
                    newChars[i] = inputChar[i];
                } else {
                    newChars[i] = ' ';
                }
            }
        }
        return new String(newChars);
    }
}
