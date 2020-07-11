package com.example.examplemod.statements;

import com.example.examplemod.HomeInfo;
import com.example.examplemod.HomeProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public interface Statement {

    default boolean call(EntityPlayer player) {
        HomeInfo home = player.getCapability(HomeProvider.HOME_INFO, null);
        boolean result = check(player, home);
        String message = result? onSuccess(player) : onFailure(player);
        if (message != null && message.length() > 0) {
            player.sendMessage(new TextComponentString(message).setStyle(new Style().setColor(TextFormatting.RED)));
        }
        return result;
    }

    boolean check(EntityPlayer player, HomeInfo home);

    String onSuccess(EntityPlayer player);

    String onFailure(EntityPlayer player);
}
