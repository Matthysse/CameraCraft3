package de.take_weiland.mods.cameracraft;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class CCPlayerData implements IExtendedEntityProperties {

	public static final String INDENTIFIER = "cameracraft.playerdata";

    public static CCPlayerData get(EntityPlayer player) {
		return (CCPlayerData)player.getExtendedProperties(INDENTIFIER);
	}

    private final EntityPlayer player;
    private long cooldownEnd = 0;

	CCPlayerData(EntityPlayer player) {
        this.player = player;
    }

	public boolean isOnCooldown() {
		return player.worldObj.getTotalWorldTime() - cooldownEnd < 0;
	}
	
	public void setCooldown(int cooldown) {
		cooldownEnd = player.worldObj.getTotalWorldTime() + cooldown;
	}
	
	private static final String COOLDOWN = "cooldown";

	@Override
	public void saveNBTData(NBTTagCompound nbt) {
		nbt.setLong(COOLDOWN, cooldownEnd);
	}

	@Override
	public void loadNBTData(NBTTagCompound nbt) {
        cooldownEnd = nbt.getLong(COOLDOWN);
	}
	
	@Override
	public void init(Entity entity, World world) {
	}

}
