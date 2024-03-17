package xyz.trieye.assignment.deal.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceSimulator {
    private static BigDecimal u = new BigDecimal(0.4);
    private static BigDecimal r = new BigDecimal(0.6);

    public static BigDecimal nextPrice(BigDecimal currentPrice, long secondElapse) {
        BigDecimal mediaValue = new BigDecimal(secondElapse).divide(new BigDecimal(7257600), 10, RoundingMode.HALF_UP);
        BigDecimal firstBranch = mediaValue.multiply(u);
        BigDecimal secondBranch = r.multiply(nextRandom()).multiply(new BigDecimal(Math.sqrt(mediaValue.doubleValue())));
        BigDecimal gapPrice = currentPrice.multiply(firstBranch.add(secondBranch));
        BigDecimal newPrice = currentPrice.add(gapPrice).setScale(2, RoundingMode.HALF_UP);
        if (newPrice.compareTo(BigDecimal.ZERO) == -1) {
            return BigDecimal.ZERO;
        } else {
            return newPrice;
        }
    }

    public static BigDecimal nextRandom() {
        return new BigDecimal(Math.random() - 0.5);
    }
}
