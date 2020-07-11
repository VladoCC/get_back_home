package com.example.examplemod.statements;

import com.example.examplemod.HomeInfo;
import com.example.examplemod.ModConfig;
import net.minecraft.entity.player.EntityPlayer;

public class InFallStatement implements Statement {
    @Override
    public boolean check(EntityPlayer player, HomeInfo home) {
        return player.motionY >= -0.2 || ModConfig.teleportInFall;
    }

    @Override
    public String onSuccess(EntityPlayer player) {
        return "";
    }

    @Override
    public String onFailure(EntityPlayer player) {
        return "You can not teleport while you are falling";
    }
}
