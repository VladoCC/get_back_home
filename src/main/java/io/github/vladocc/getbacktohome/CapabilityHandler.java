package io.github.vladocc.getbacktohome;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Voyager on 06.05.2018.
 */
public class CapabilityHandler {

    public static final ResourceLocation HOME_INFO = new ResourceLocation(ModInfo.MODID, "home");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if (!(event.getObject() instanceof EntityPlayer)) return;

        event.addCapability(HOME_INFO, new HomeProvider());
    }

}
