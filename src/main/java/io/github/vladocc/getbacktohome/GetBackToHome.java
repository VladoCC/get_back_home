package io.github.vladocc.getbacktohome;

import io.github.vladocc.getbacktohome.commands.DeleteHomeCommand;
import io.github.vladocc.getbacktohome.commands.MoveBackCommand;
import io.github.vladocc.getbacktohome.commands.SetHomeCommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Logger;
import squeek.applecore.api.hunger.ExhaustionEvent;

import java.util.concurrent.Callable;

@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION, dependencies = "required-after:applecore;after:spiceoflife;")
public class GetBackToHome
{
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        CapabilityManager.INSTANCE.register(HomeInfo.class, new HomeStorage(), HomeInfo::new);

        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        if (ModConfig.homeType > 0) {
            event.registerServerCommand(new SetHomeCommand());
        }
        event.registerServerCommand(new MoveBackCommand());
        event.registerServerCommand(new DeleteHomeCommand());
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event){
        EntityPlayer player = event.getEntityPlayer();
        HomeInfo home = player.getCapability(HomeProvider.HOME_INFO, null);
        HomeInfo oldHome = event.getOriginal().getCapability(HomeProvider.HOME_INFO, null);
        home.setHome(oldHome.getHome());
    }

    @SubscribeEvent
    public void onExhausted(ExhaustionEvent.Exhausted event){
        if (event.player.getFoodStats().getSaturationLevel() <= 0 && event.player.getFoodStats().getFoodLevel() <= 0){
            event.deltaExhaustion = 0;
            event.deltaHunger = 0;
            event.deltaSaturation = 0;
        }
    }

    @SubscribeEvent
    public void worldTickEvent(TickEvent.WorldTickEvent event) {
        if (ModConfig.homeType != 1){
            if (!event.world.isRemote && event.phase == TickEvent.Phase.END) {
                // This is called every tick, do something every 20 ticks
                if (event.world.playerEntities.size() > 0) {
                    for (EntityPlayer player : event.world.playerEntities) {
                        if (player.isPlayerSleeping() && player.getSleepTimer() == 0){
                            HomeController.setHomeOnBed(player);
                        }
                    }
                }
            }
        }
    }
}
