package com.example.examplemod.statements;

import com.example.examplemod.HomeInfo;
import com.example.examplemod.ModConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class DelayStatement implements Statement {
    @Override
    public boolean check(EntityPlayer player, HomeInfo home) {
        int time = 0;
        if (ModConfig.delaying > 0){
            time = 3000;
            String action = "wait";
            if (ModConfig.delaying == 2){
                action = "stay still";
            }
            player.sendMessage(new TextComponentString("You need to " + action + " for 3 seconds before teleportation").setStyle(new Style().setColor(TextFormatting.RED)));
        }
        BlockPos pos = player.getPosition();
        double startX = pos.getX();
        double startY = pos.getY();
        double startZ = pos.getZ();
        int startDim = player.getEntityWorld().getWorldType().getId();
        float startHP = ((EntityPlayer) player).getHealth();
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pos = player.getPosition();
        double finalX = pos.getX();
        double finalY = pos.getY();
        double finalZ = pos.getZ();
        int finalDim = player.getEntityWorld().getWorldType().getId();
        float finalHP = ((EntityPlayer) player).getHealth();
        boolean still = (startX == finalX) && (startY == finalY) && (startZ == finalZ) && (startDim == finalDim) && (finalHP >= startHP);
        return ModConfig.delaying < 2 || still;
    }

    @Override
    public String onSuccess(EntityPlayer player) {
        return null;
    }

    @Override
    public String onFailure(EntityPlayer player) {
        return "You moved! Teleportation canceled";
    }
}
