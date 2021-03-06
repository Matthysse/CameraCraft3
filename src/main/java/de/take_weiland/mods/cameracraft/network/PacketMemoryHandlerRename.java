package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.cameracraft.api.photo.NamedPhotoStorage;
import de.take_weiland.mods.cameracraft.item.ItemPhotoStorages;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author Intektor
 */
public class PacketMemoryHandlerRename implements Packet {

    public final String name;
    public final int index;
    public final int containerID;
    public final int side;

    public PacketMemoryHandlerRename(int index, String name, int containerID, int side) {
        this.name = name;
        this.index = index;
        this.containerID = containerID;
        this.side = side;
    }

    public PacketMemoryHandlerRename(MCDataInput input) {
        name = input.readString();
        index = input.readInt();
        containerID = input.readInt();
        side = input.readInt();
    }

    @Override
    public void writeTo(MCDataOutput out) {
        out.writeString(name);
        out.writeInt(index);
        out.writeInt(containerID);
        out.writeInt(side);
    }

    public void handle(EntityPlayer player) {
        if(player.openContainer.windowId == containerID) {
            Slot slot = (Slot)player.openContainer.inventorySlots.get(side);
            ItemStack stack = slot.getStack();
            ItemPhotoStorages itemStorage = (ItemPhotoStorages) stack.getItem();
            NamedPhotoStorage storage = (NamedPhotoStorage) itemStorage.getPhotoStorage(stack);
            storage.setName(index, name);
        }
    }
}
