package xyz.trieye.assignment.deal.helper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import xyz.trieye.assignment.deal.common.Tools;

import java.math.BigDecimal;
import java.text.DecimalFormat;

class PriceSimulatorTest {

    @Test
    void nextPrice() {
        try (MockedStatic<PriceSimulator> mockedStatic = Mockito.mockStatic(PriceSimulator.class)) {
            mockedStatic.when(PriceSimulator::nextRandom).thenReturn(new BigDecimal(0.6));
            mockedStatic.when(() -> PriceSimulator.nextPrice(new BigDecimal(100), 86400L)).thenCallRealMethod();
            BigDecimal bigDecimal = PriceSimulator.nextPrice(new BigDecimal(100), 86400L);
            String formatted = Tools.priceFormat.format(bigDecimal);
            Assertions.assertThat(formatted).isEqualTo("104.404");
        }
    }
}