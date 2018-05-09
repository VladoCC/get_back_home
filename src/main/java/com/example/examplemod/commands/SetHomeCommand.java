package com.example.examplemod.commands;

import com.example.examplemod.HomeController;
import com.example.examplemod.HomeInfo;
import com.example.examplemod.HomeProvider;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Voyager on 06.05.2018.
 */
public class SetHomeCommand implements ICommand {

    ArrayList<String> allias = new ArrayList<>();

    public SetHomeCommand() {
        allias.add("sethome");
    }

    @Override
    public String getName() {
        return "createhome";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "createhome";
    }

    @Override
    public List<String> getAliases() {
        return allias;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Entity entity = sender.getCommandSenderEntity();
        HomeController.setHome(entity);
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return new ArrayList<>();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
