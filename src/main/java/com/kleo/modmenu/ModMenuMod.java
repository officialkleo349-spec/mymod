package com.kleo.modmenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("kleo_modmenu")
public class ModMenuMod {
    public ModMenuMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof PauseScreen) {
            Button button = Button.builder(Component.literal("KLEO Mods"),
                            b -> Minecraft.getInstance().setScreen(new ModMenuScreen()))
                    .pos(10, 10)
                    .size(110, 20)
                    .build();
            event.addListener(button);
        }
    }
}
