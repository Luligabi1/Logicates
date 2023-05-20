package me.luligabi.logicates.common.misc.packet;

import me.luligabi.logicates.common.misc.screenhandler.KeypadLogicateScreenHandler;

public class ServerPlayReceiverRegistry {

    public static void init() {
        KeypadLogicateScreenHandler.initPacketReceivers();
    }

}