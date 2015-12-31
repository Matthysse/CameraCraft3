package de.take_weiland.mods.cameracraft;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.db.DatabaseImpl;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.item.MiscItemType;
import de.take_weiland.mods.commons.SaveWorldsEvent;
import de.take_weiland.mods.commons.util.Scheduler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.world.WorldEvent;

public final class CCEventHandler implements Scheduler.Task {

    public static final CCEventHandler INSTANCE = new CCEventHandler();

    static volatile DatabaseImpl currentDb;

    private CCEventHandler() {}

    @SubscribeEvent(priority = EventPriority.HIGHEST) // highest so that DB is available during normal world load handlers
    public void onWorldLoad(WorldEvent.Load event) {
        if (CameraCraft.serverStartingUp && event.world.provider.dimensionId == 0) {
            setDatabase(new DatabaseImpl(DimensionManager.getCurrentSaveRootDirectory().toPath().resolve("cameracraft")));
        }
    }

    static void setDatabase(DatabaseImpl db) {
        DatabaseImpl old = currentDb;
        currentDb = db;

        if (old != null) {
            old.save();
        }
    }

    @Override
    public boolean execute() {
        DatabaseImpl db = CameraCraft.currentDatabase();
        if (db != null) {
            db.requestCleanup();
        }
        return true;
    }

    @SubscribeEvent
    public void saveWorlds(SaveWorldsEvent event) {
        DatabaseImpl db = CameraCraft.currentDatabase();
        if (db != null) {
            db.save();
        }
    }

	@SubscribeEvent
	public void onEntityConstruct(EntityEvent.EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			event.entity.registerExtendedProperties(CCPlayerData.INDENTIFIER, new CCPlayerData((EntityPlayer) event.entity));
		}
	}
	
	@SubscribeEvent
	public void onBucketUse(FillBucketEvent event) {
		int x = event.target.blockX;
		int y = event.target.blockY;
		int z = event.target.blockZ;
		if (event.world.getBlock(x, y, z) == CCBlock.alkaline && event.world.getBlockMetadata(x, y, z) == 0) {
			event.world.setBlockToAir(x, y, z);
			event.setResult(Event.Result.ALLOW);
			event.result = CCItem.misc.getStack(MiscItemType.ALKALINE_BUCKET);
		}
	}

	@SubscribeEvent
	public void onEntityInteract(EntityInteractEvent event) {
		Entity entity = event.target;
		if(event.entityPlayer != null) {
			CCPlayerData.get(event.entityPlayer).setLastClickedEntityID(entity.getEntityId());
		}
	}
}
