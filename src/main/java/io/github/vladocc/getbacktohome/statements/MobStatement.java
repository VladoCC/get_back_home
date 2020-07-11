package com.example.examplemod.statements;

import com.example.examplemod.HomeInfo;
import com.example.examplemod.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class MobStatement implements Statement {
    @Override
    public boolean check(EntityPlayer player, HomeInfo home) {
        List<Entity> entities = player.getEntityWorld().getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(5.0));
        boolean noMobs = true;
        if (!ModConfig.nearMonster){
            for (Entity mob : entities){
                if (mob instanceof IMob){
                    noMobs = false;
                    break;
                }
            }
        }
        return noMobs;
    }

    @Override
    public String onSuccess(EntityPlayer player) {
        return "";
    }

    @Override
    public String onFailure(EntityPlayer player) {
        return "You can not teleport, there are monsters nearby";
    }
}
