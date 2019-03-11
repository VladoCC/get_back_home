package io.github.vladocc.getbacktohome;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Voyager on 06.05.2018.
 */
public class HomeProvider implements ICapabilitySerializable<NBTBase> {

    @CapabilityInject(HomeInfo.class)
    public static final Capability<HomeInfo> HOME_INFO = null;
    private HomeInfo instance = HOME_INFO.getDefaultInstance();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == HOME_INFO;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == HOME_INFO? HOME_INFO.<T> cast(this.instance) : null;
    }

    @Override
    public NBTBase serializeNBT() {
        return HOME_INFO.getStorage().writeNBT(HOME_INFO, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        HOME_INFO.getStorage().readNBT(HOME_INFO, this.instance, null, nbt);
    }
}
