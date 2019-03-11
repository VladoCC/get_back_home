package io.github.vladocc.getbacktohome;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import squeek.applecore.api.food.FoodValues;
import squeek.applecore.api_impl.AppleCoreAccessorMutatorImpl;

import java.util.List;

/**
 * Created by Voyager on 06.05.2018.
 */
public class HomeController {

    public static final float EXHAUSTION_SPRINT = (float) ModConfig.runCost;
    public static final float EXHAUSTION_JUMP = (float) ModConfig.jumpCost;
    public static final float EXHAUSTION_FALL = (float) ModConfig.fallCost;
    public static final float EXHAUSTION_MULTIPLIER = (float) ModConfig.multCost;
    public static final float MOUNT_MULTIPLIER = (float) ModConfig.multMountCost;

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
            HomeInfo info = entity.getCapability(HomeProvider.HOME_INFO, null);
            System.out.println(entity.dimension + " " + info.getDim());
            if (entity.dimension == info.getDim()){
                if (info != null && info.isCreated()) {
                    if (entity.motionY >= -0.2 || ModConfig.teleportInFall) {
                        int time = 0;
                        if (ModConfig.delaying > 0){
                            time = 3000;
                            String action = "wait";
                            if (ModConfig.delaying == 2){
                                action = "stay still";
                            }
                            entity.sendMessage(new TextComponentString("You need to " + action + " for 3 seconds before teleportation").setStyle(new Style().setColor(TextFormatting.RED)));
                        }
                        BlockPos pos = entity.getPosition();
                        double startX = pos.getX();
                        double startY = pos.getY();
                        double startZ = pos.getZ();
                        int startDim = entity.getEntityWorld().getWorldType().getId();
                        float startHP = ((EntityPlayer) entity).getHealth();
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        pos = entity.getPosition();
                        double finalX = pos.getX();
                        double finalY = pos.getY();
                        double finalZ = pos.getZ();
                        int finalDim = entity.getEntityWorld().getWorldType().getId();
                        float finalHP = ((EntityPlayer) entity).getHealth();
                        boolean still = (startX == finalX) && (startY == finalY) && (startZ == finalZ) && (startDim == finalDim) && (finalHP >= startHP);

                        List<Entity> entities = entity.getEntityWorld().getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().grow(5.0));
                        boolean noMobs = true;
                        if (!ModConfig.nearMonster){
                            for (Entity mob : entities){
                                if (mob instanceof IMob){
                                    noMobs = false;
                                    break;
                                }
                            }
                        }
                        if (noMobs) {
                            if (ModConfig.delaying < 2 || still) {
                                EntityPlayer player = (EntityPlayer) entity;

                                AppleCoreAccessorMutatorImpl mutator = AppleCoreAccessorMutatorImpl.INSTANCE;
                                int flatDistance = Math.abs(info.getX() - player.getPosition().getX()) + Math.abs(info.getZ() - player.getPosition().getZ());
                                int jumpHeight = Math.max(0, info.getY() - player.getPosition().getY());
                                int fallHeight = Math.max(0, player.getPosition().getY() - info.getY());
                                float exhaustion = flatDistance * EXHAUSTION_SPRINT + jumpHeight * EXHAUSTION_JUMP + fallHeight * EXHAUSTION_FALL;
                                exhaustion *= EXHAUSTION_MULTIPLIER;
                                if (player.isRiding() && ModConfig.onMount){
                                    exhaustion *= MOUNT_MULTIPLIER;
                                }
                                float maxExhaustion = mutator.getMaxExhaustion(player);
                                float food = player.getFoodStats().getFoodLevel() * maxExhaustion;
                                float saturation = player.getFoodStats().getSaturationLevel() * maxExhaustion;
                                boolean canTeleport = !ModConfig.paymentType;
                                if (ModConfig.paymentType) {
                                    if (saturation >= exhaustion) {
                                        saturation -= exhaustion;
                                        exhaustion = 0;
                                        canTeleport = true;
                                    } else {
                                        exhaustion -= saturation;
                                        saturation = 0;
                                    }
                                    if (!canTeleport && food >= exhaustion) {
                                        food -= exhaustion;
                                        exhaustion = 0;
                                        canTeleport = true;
                                    } else {
                                        exhaustion -= food;
                                        food = 0;
                                    }
                                    if (!canTeleport) {
                                        for (int i = 0; i < 9; i++) {
                                            ItemStack stack = player.inventory.mainInventory.get(i);
                                            if (stack.getItem() instanceof ItemFood && stack.getCount() > 0) {
                                                ItemFood item = (ItemFood) stack.getItem();
                                                FoodValues values = mutator.getFoodValuesForPlayer(stack, player);
                                                float itemFood = values.hunger * (1 + values.saturationModifier) * maxExhaustion;
                                                float stackFood = itemFood * stack.getCount();
                                                if (stackFood >= exhaustion) {
                                                    int count = (int) Math.ceil(exhaustion / itemFood);
                                                    exhaustion -= itemFood * count;
                                                    stack.setCount(stack.getCount() - count);
                                                    canTeleport = true;
                                                    break;
                                                } else {
                                                    exhaustion -= stackFood;
                                                    stack.setCount(0);
                                                }
                                            }
                                        }
                                    }
                                }

                                if (canTeleport && (!ModConfig.paymentType || exhaustion >= 0)) {
                                    if (ModConfig.paymentType) {
                                        player.getFoodStats().setFoodLevel((int) food);
                                        player.getFoodStats().setFoodSaturationLevel(saturation);
                                    } else {
                                        mutator.setExhaustion(player, exhaustion + mutator.getExhaustion(player));
                                    }
                                    if (player.isRiding()) {
                                        if (ModConfig.onMount) {
                                            player.getRidingEntity().setPositionAndUpdate(info.getX() + 0.5, info.getY() + 0.5, info.getZ() + 0.5);
                                        } else {
                                            player.dismountRidingEntity();
                                        }
                                    }
                                    player.setPositionAndUpdate(info.getX() + 0.5, info.getY() + 0.5, info.getZ() + 0.5);
                                    player.sendMessage(new TextComponentString("Home, sweet home!"));
                                } else {
                                    player.sendMessage(new TextComponentString("You are too exhausted to overcome this path").setStyle(new Style().setColor(TextFormatting.RED)));
                                }
                            } else {
                                entity.sendMessage(new TextComponentString("You moved! Teleportation canceled").setStyle(new Style().setColor(TextFormatting.RED)));
                            }
                        } else {
                            entity.sendMessage(new TextComponentString("You can not teleport, there are monsters nearby").setStyle(new Style().setColor(TextFormatting.RED)));
                        }
                    } else {
                        entity.sendMessage(new TextComponentString("You can not teleport while you are falling").setStyle(new Style().setColor(TextFormatting.RED)));
                    }
                } else {
                    String ways = "";
                    if (ModConfig.homeType == 0){
                        ways = "your bed";
                    } else if (ModConfig.homeType == 1){
                        ways = "/createhome command";
                    } else if (ModConfig.homeType == 2){
                        ways = "your bed or /createhome command";
                    }
                    entity.sendMessage(new TextComponentString("Use " + ways + " first").setStyle(new Style().setColor(TextFormatting.RED)));
                }
            } else {
                entity.sendMessage(new TextComponentString("You can not teleport interdimensionally").setStyle(new Style().setColor(TextFormatting.RED)));
            }
        }
    }
}
