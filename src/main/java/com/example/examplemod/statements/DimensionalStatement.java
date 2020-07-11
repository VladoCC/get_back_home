package com.example.examplemod.statements;

import com.example.examplemod.HomeInfo;
import com.example.examplemod.HomeProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class DimensionalStatement implements Statement {

    @Override
    public boolean check(EntityPlayer player, HomeInfo home) {
        return player.dimension == home.getDim();
    }

    @Override
    public String onSuccess(EntityPlayer player) {
        return "";
    }

    @Override
    public String onFailure(EntityPlayer player) {
        return "You can not teleport interdimensionally";
    }
}
