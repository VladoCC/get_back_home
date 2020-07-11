package io.github.vladocc.getbacktohome.statements;

import io.github.vladocc.getbacktohome.HomeInfo;
import io.github.vladocc.getbacktohome.ModConfig;
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
