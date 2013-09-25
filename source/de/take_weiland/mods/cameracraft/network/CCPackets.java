package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.network.ModPacket;
import de.take_weiland.mods.commons.network.PacketTransport;
import de.take_weiland.mods.commons.network.PacketType;

public enum CCPackets implements PacketType {
	
	CLIENT_ACTION(PacketClientAction.class),
	TAKEN_PHOTO(PacketTakenPhoto.class),
	PHOTO_NAME(PacketPhotoName.class);

	private final Class<? extends ModPacket> clazz;
	
	private CCPackets(Class<? extends ModPacket> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public int packetId() {
		return ordinal();
	}

	@Override
	public Class<? extends ModPacket> packetClass() {
		return clazz;
	}

	@Override
	public PacketTransport transport() {
		return CameraCraft.packetTransport;
	}

	@Override
	public boolean isMultipart() {
		return this == TAKEN_PHOTO;
	}
	
}