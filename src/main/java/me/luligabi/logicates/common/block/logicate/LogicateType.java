package me.luligabi.logicates.common.block.logicate;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public enum LogicateType {

    SINGLE_INPUT(Text.translatable("logicateType.logicates.single_input")),
    DUAL_INPUT(Text.translatable("logicateType.logicates.dual_input")),
    MISCELLANEOUS(Text.translatable("logicateType.logicates.misc"));

    LogicateType(MutableText text) {
        this.text = text;
    }

    public MutableText getText() {
        return text;
    }

    private final MutableText text;

}