package me.luligabi.logicates.common.block.logicate;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public interface Logicatable {

    LogicateType getLogicateType();

    List<MutableText> getLogicateTooltip();

    default void appendLogicateTooltip(List<Text> tooltip) {
        tooltip.add(getLogicateType().getText().formatted(Formatting.BLUE));
        tooltip.add(Text.empty());
        getLogicateTooltip().forEach(text -> tooltip.add(text.formatted(Formatting.GRAY)));
    }
}
