package io.github.vladocc.getbacktohome.statements;

import io.github.vladocc.getbacktohome.HomeInfo;
import io.github.vladocc.getbacktohome.ModConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import squeek.applecore.api.food.FoodValues;
import squeek.applecore.api_impl.AppleCoreAccessorMutatorImpl;

public class TeleportStatement implements Statement {

    public static final float EXHAUSTION_SPRINT = (float) ModConfig.runCost;
    public static final float EXHAUSTION_JUMP = (float) ModConfig.jumpCost;
    public static final float EXHAUSTION_FALL = (float) ModConfig.fallCost;
    public static final float EXHAUSTION_MULTIPLIER = (float) ModConfig.multCost;
    public static final float MOUNT_MULTIPLIER = (float) ModConfig.multMountCost;

    @Override
    public boolean check(EntityPlayer player, HomeInfo home) {
        AppleCoreAccessorMutatorImpl mutator = AppleCoreAccessorMutatorImpl.INSTANCE;
        int flatDistance = Math.abs(home.getX() - player.getPosition().getX()) + Math.abs(home.getZ() - player.getPosition().getZ());
        int jumpHeight = Math.max(0, home.getY() - player.getPosition().getY());
        int fallHeight = Math.max(0, player.getPosition().getY() - home.getY());
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
        boolean result = canTeleport && (!ModConfig.paymentType || exhaustion >= 0);
        if (canTeleport && (!ModConfig.paymentType || exhaustion >= 0)) {
            if (ModConfig.paymentType) {
                player.getFoodStats().setFoodLevel((int) food);
                player.getFoodStats().setFoodSaturationLevel(saturation);
            } else {
                mutator.setExhaustion(player, exhaustion + mutator.getExhaustion(player));
            }
            if (player.isRiding()) {
                if (ModConfig.onMount) {
                    player.getRidingEntity().setPositionAndUpdate(home.getX() + 0.5, home.getY() + 0.5, home.getZ() + 0.5);
                } else {
                    player.dismountRidingEntity();
                }
            }
            player.setPositionAndUpdate(home.getX() + 0.5, home.getY() + 0.5, home.getZ() + 0.5);
            player.sendMessage(new TextComponentString("Home, sweet home!"));
        } else {
            player.sendMessage(new TextComponentString("You are too exhausted to overcome this path").setStyle(new Style().setColor(TextFormatting.RED)));
        }
        return result;
    }

    @Override
    public String onSuccess(EntityPlayer player) {
        return "";
    }

    @Override
    public String onFailure(EntityPlayer player) {
        return "You are too exhausted to overcome this path";
    }
}
