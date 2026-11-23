package com.kleo.modmenu;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.forgespi.language.IModInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModMenuScreen extends Screen {
    private static final int SKY = 0xFF7FD4FF;
    private static final int NAVY = 0xFF0B2250;
    private static final int TEXT = 0xFFE6F7FF;

    private enum Tab { MODS, CONFIG, ALL }

    private Tab tab = Tab.MODS;
    private final List<String> lines = new ArrayList<>();
    private int x0, y0, boxW, boxH;

    public ModMenuScreen() {
        super(Component.literal("Mod Menu"));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        boxW = Math.min(420, width - 40);
        boxH = Math.min(320, height - 40);
        x0 = width / 2 - boxW / 2;
        y0 = height / 2 - boxH / 2;

        loadLines();

        int tabW = (boxW - 36) / 3;
        addTab("Mods", Tab.MODS, x0 + 12, y0 + 38, tabW);
        addTab("Config", Tab.CONFIG, x0 + 12 + tabW + 6, y0 + 38, tabW);
        addTab("All", Tab.ALL, x0 + 12 + (tabW + 6) * 2, y0 + 38, tabW);

        addRenderableWidget(Button.builder(Component.literal("Close"), b -> onClose())
                .pos(width / 2 - 50, y0 + boxH - 28)
                .size(100, 20)
                .build());
    }

    private void addTab(String label, Tab t, int x, int y, int w) {
        Button btn = Button.builder(Component.literal(label), b -> {
                    tab = t;
                    rebuildWidgets();
                })
                .pos(x, y)
                .size(w, 20)
                .build();
        btn.active = tab != t;
        addRenderableWidget(btn);
    }

    private void loadLines() {
        lines.clear();
        if (tab != Tab.CONFIG) {
            for (IModInfo mod : ModList.get().getMods()) {
                lines.add(mod.getDisplayName() + " " + mod.getVersion());
            }
        }
        if (tab != Tab.MODS) {
            File[] files = FMLPaths.CONFIGDIR.get().toFile().listFiles();
            if (files != null) {
                for (File f : files) {
                    lines.add("[cfg] " + f.getName());
                }
            }
        }
        Collections.sort(lines, String.CASE_INSENSITIVE_ORDER);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        renderBackground(poseStack);

        // antenna at ears
        fill(poseStack, width / 2 - 2, y0 - 26, width / 2 + 2, y0, SKY);
        fill(poseStack, width / 2 - 6, y0 - 32, width / 2 + 6, y0 - 26, SKY);
        fill(poseStack, x0 - 12, y0 + boxH / 2 - 30, x0, y0 + boxH / 2 + 30, SKY);
        fill(poseStack, x0 + boxW, y0 + boxH / 2 - 30, x0 + boxW + 12, y0 + boxH / 2 + 30, SKY);

        // border at box
        fill(poseStack, x0 - 3, y0 - 3, x0 + boxW + 3, y0 + boxH + 3, SKY);
        fill(poseStack, x0, y0, x0 + boxW, y0 + boxH, NAVY);
        fill(poseStack, x0, y0, x0 + boxW, y0 + 30, 0x557FD4FF);

        drawString(poseStack, font, Component.literal("MOD MENU // BOT-01"), x0 + 12, y0 + 11, SKY);

        // list
        int listY = y0 + 70;
        int rowH = 12;
        int maxRows = (boxH - 110) / rowH;
        int shown = Math.min(lines.size(), maxRows);
        for (int i = 0; i < shown; i++) {
            String text = font.plainSubstrByWidth(lines.get(i), boxW - 28);
            drawString(poseStack, font, Component.literal(text), x0 + 14, listY + i * rowH, TEXT);
        }
        if (lines.isEmpty()) {
            drawString(poseStack, font, Component.literal("Walang laman."), x0 + 14, listY, SKY);
        } else if (lines.size() > shown) {
            drawString(poseStack, font, Component.literal("+" + (lines.size() - shown) + " pa..."), x0 + 14, listY + shown * rowH, SKY);
        }

        super.render(poseStack, mouseX, mouseY, partialTick);
    }
             }
