package de.take_weiland.mods.cameracraft.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import de.take_weiland.mods.cameracraft.CCSounds;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.Environment;
import de.take_weiland.mods.cameracraft.client.gui.GuiPhotoName;
import de.take_weiland.mods.cameracraft.client.gui.GuiViewPhoto;
import de.take_weiland.mods.cameracraft.client.render.RenderPoster;
import de.take_weiland.mods.cameracraft.entity.EntityPoster;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ForkJoinPool;

public class EnvironmentClient implements Environment {

	public static final ResourceLocation CONTROLS = new ResourceLocation("cameracraft", "textures/gui/controls.png");
	
	private PhotoTicker photoTicker;
	
	@Override
	public void preInit() {
		photoTicker = new PhotoTicker();
		FMLCommonHandler.instance().bus().register(photoTicker);
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);

		RenderingRegistry.registerEntityRenderingHandler(EntityPoster.class, new RenderPoster());
	}

	@Override
	public void handleStandardPhotoRequest(int transferId) {
		photoTicker.schedulePhoto(transferId);
	}

	@Override
	public void handleClientPhotoData(final long photoId, final InputStream in) {
		CameraCraft.executor.execute(() -> {
            try {
                PhotoDataCache.injectReceivedPhoto(photoId, in);
            } catch (IOException e) {
                CrashReport cr = new CrashReport("Receiving CameraCraft photodata", e);
                cr.makeCategory("Photo being received").addCrashSection("photoId", photoId);
                throw new ReportedException(cr);
            }
        });
	}

	@Override
	public void handleClientPhotoData(long photoId, BufferedImage img) {
		ForkJoinPool.commonPool().execute(() -> PhotoDataCache.injectReceivedPhoto(photoId, img));
	}

	@Override
	public void displayNamePhotoGui(String oldName) {
		mc().displayGuiScreen(new GuiPhotoName(oldName, input -> {
            // TODO Auto-generated method stub
        }));
	}
	
	@Override
	public void displayPhotoGui(long photoId, String displayName, boolean canRename) {
		mc().displayGuiScreen(new GuiViewPhoto(photoId, displayName, canRename));
	}

	@Override
	public void spawnAlkalineBubbleFX(double x, double y, double z, double motionX, double motionY, double motionZ) {
		mc().effectRenderer.addEffect(new EntityAlkalineBubbleFX(mc().theWorld, x, y, z, motionX, motionY, motionZ));
	}

    @SubscribeEvent
	public void onSoundLoad(SoundLoadEvent event) {
		for (CCSounds sound : CCSounds.values()) {
			sound.register(event.manager);
		}
	}

	@SubscribeEvent
	public void connectionOpened(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		PhotoDataCache.invalidate();
	}

	@SubscribeEvent
	public void connectionClosed(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
		PhotoDataCache.invalidate();
	}

    private static Minecraft mc() {
        return Minecraft.getMinecraft();
    }

}
