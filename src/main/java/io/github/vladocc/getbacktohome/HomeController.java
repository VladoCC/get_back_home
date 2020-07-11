package com.example.examplemod;

import com.example.examplemod.statements.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;

/**
 * Created by Voyager on 06.05.2018.
 */
public class HomeController {

    public static final float EXHAUSTION_SPRINT = (float) ModConfig.runCost;
    public static final float EXHAUSTION_JUMP = (float) ModConfig.jumpCost;
    public static final float EXHAUSTION_FALL = (float) ModConfig.fallCost;
    public static final float EXHAUSTION_MULTIPLIER = (float) ModConfig.multCost;
    public static final float MOUNT_MULTIPLIER = (float) ModConfig.multMountCost;

    private static final ArrayList<Statement> teleportStatements = new ArrayList<>();

    public static void setHome(Entity entity, double x, double y, double z, int world){
        if (entity instanceof EntityPlayer) {
            HomeInfo info = entity.getCapability(HomeProvider.HOME_INFO, null);
            info.setHome((int) x, (int) y, (int) z, world);
            entity.sendMessage(new TextComponentString("Welcome to your new home, " + entity.getName() + "!"));
        }
    }

    public static void setHome(Entity entity){
        BlockPos pos = entity.getPosition();
        setHome(entity, pos.getX(), pos.getY(), pos.getZ(), entity.dimension);
    }

    public static void setHomeOnBed(Entity entity){
        EntityPlayer player = (EntityPlayer) entity;
        BlockPos pos = player.bedLocation;
        setHome(entity, pos.getX(), pos.getY() + 1, pos.getZ(), entity.dimension);
    }

    public static void moveBackHome(Entity entity){
        if (entity instanceof EntityPlayer) {
            for (Statement statement : teleportStatements) {
                if (!statement.call((EntityPlayer) entity)) {
                    return;
                }
            }
        }
    }

    public static void init() {
        teleportStatements.add(new DimensionalStatement());
        teleportStatements.add(new HomeExistenceStatement());
        teleportStatements.add(new InFallStatement());
        teleportStatements.add(new DelayStatement());
        teleportStatements.add(new MobStatement());
        teleportStatements.add(new TeleportStatement());
    }
}
