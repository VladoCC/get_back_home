package io.github.vladocc.getbacktohome.statements;

import io.github.vladocc.getbacktohome.HomeInfo;
import io.github.vladocc.getbacktohome.ModConfig;
import net.minecraft.entity.player.EntityPlayer;

public class HomeExistenceStatement implements Statement {
    @Override
    public boolean check(EntityPlayer player, HomeInfo home) {
        return home != null && home.isCreated();
    }

    @Override
    public String onSuccess(EntityPlayer player) {
        return "";
    }

    @Override
    public String onFailure(EntityPlayer player) {
        String ways = "";
        if (ModConfig.homeType == 0){
            ways = "your bed";
        } else if (ModConfig.homeType == 1){
            ways = "/createhome command";
        } else if (ModConfig.homeType == 2){
            ways = "your bed or /createhome command";
        }
        return "Use " + ways + " first";
    }
}
