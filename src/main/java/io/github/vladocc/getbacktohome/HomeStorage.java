package io.github.vladocc.getbacktohome;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Created by Voyager on 06.05.2018.
 */
public class HomeStorage implements Capability.IStorage<HomeInfo> {
    @Nullable
    @Override
    public NBTBase writeNBT(Capability<HomeInfo> capability, HomeInfo instance, EnumFacing side) {
        return new NBTTagIntArray(instance.getHome());
    }

    @Override
    public void readNBT(Capability<HomeInfo> capability, HomeInfo instance, EnumFacing side, NBTBase nbt) {
        int[] nbtArray = ((NBTTagIntArray) nbt).getIntArray();
        instance.setHome(nbtArray);
        if (nbtArray[0] == 0 && nbtArray[1] == 0 && nbtArray[2] == 0) {
            instance.setCreated(false);
        }
    }
}
