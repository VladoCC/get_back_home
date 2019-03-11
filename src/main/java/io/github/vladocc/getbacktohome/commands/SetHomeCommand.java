package io.github.vladocc.getbacktohome.commands;

import io.github.vladocc.getbacktohome.HomeController;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

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
