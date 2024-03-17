package xyz.trieye.assignment.deal.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToolsTest {

    @Test
    void extendWithSpaces() {
        String s = Tools.extendWithSpaces("1234", 10, true);
        Assertions.assertThat(s).isEqualTo("      1234");
        s = Tools.extendWithSpaces("1234", 10, false);
        Assertions.assertThat(s).isEqualTo("1234      ");
    }
}