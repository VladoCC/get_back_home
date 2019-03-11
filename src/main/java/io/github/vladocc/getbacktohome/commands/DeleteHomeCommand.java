package io.github.vladocc.getbacktohome.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Voyager on 07.05.2018.
 */
public class DeleteHomeCommand implements ICommand {

    ArrayList<String> allias = new ArrayList<>();

    public DeleteHomeCommand() {
        allias.add("removehome");
    }

    @Override
    public String getName() {
        return "deletehome";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "deletehome";
    }

    @Override
    public List<String> getAliases() {
        return allias;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

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
