package me.luligabi.logicates.common.misc.screenhandler;


import me.luligabi.logicates.common.Logicates;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

@SuppressWarnings("deprecation")
public class ScreenHandlingRegistry {

    public static final ScreenHandlerType<TimerLogicateScreenHandler> TIMER_LOGICATE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(Logicates.id("timer_logicate"), TimerLogicateScreenHandler::new);

    public static final ScreenHandlerType<KeypadLogicateScreenHandler> KEYPAD_LOGICATE_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(Logicates.id("keypad_logicate"), KeypadLogicateScreenHandler::new);

    public static final ScreenHandlerType<LogicateFabricatorScreenHandler> LOGICATE_FABRICATOR_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(Logicates.id("logicate_fabricator"), LogicateFabricatorScreenHandler::new);


    public static void init() {
        // NO-OP
    }

    private ScreenHandlingRegistry() {
        // NO-OP
    }
}