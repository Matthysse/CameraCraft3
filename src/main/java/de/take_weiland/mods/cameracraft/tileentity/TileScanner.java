package de.take_weiland.mods.cameracraft.tileentity;

import de.take_weiland.mods.cameracraft.api.PhotoStorageProvider;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageItem;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.blocks.MachineType;
import de.take_weiland.mods.commons.meta.HasSubtypes;
import de.take_weiland.mods.commons.tileentity.TileEntityInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileScanner extends TileEntityInventory implements PhotoStorageProvider {

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		Item item = stack.getItem();
		return item instanceof PhotoStorageItem && ((PhotoStorageItem) item).canBeScanned(stack);
	}

	@Override
	public String getDefaultName() {
		return HasSubtypes.name(CCBlock.machines, MachineType.SCANNER);
	}

	@Override
	public PhotoStorage getPhotoStorage() {
		ItemStack storage = getPhotoStorageItem();
		return storage == null ? null : ((PhotoStorageItem) storage.getItem()).getPhotoStorage(storage);
	}
	
	private ItemStack getPhotoStorageItem() {
		ItemStack item = storage[0];
		return item != null && item.getItem() instanceof PhotoStorageItem ? item : null;
	}

}
