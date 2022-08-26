package me.luligabi.logicates.common.block.logicate;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public enum LogicateType {

    SINGLE_INPUT(new TranslatableText("logicateType.logicates.single_input")),
    DUAL_INPUT(new TranslatableText("logicateType.logicates.dual_input")),
    INPUTLESS(new TranslatableText("logicateType.logicates.inputless"));

    LogicateType(MutableText text) {
        this.text = text;
    }

    public MutableText getText() {
        return text;
    }

    private final MutableText text;

}