package de.take_weiland.mods.cameracraft.client.render;

import de.take_weiland.mods.cameracraft.client.PhotoDataCache;
import de.take_weiland.mods.cameracraft.entity.EntityPaintable;
import de.take_weiland.mods.cameracraft.entity.EntityPoster;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL;

public class RenderPoster extends Render {

	public static final ResourceLocation vanillaPainting = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");
	
	@Override
	public void doRender(Entity entity, double x, double y, double z, float posYaw, float partialTickTime) {
		EntityPoster poster = (EntityPoster) entity;

		glPushMatrix();
		glTranslated(x, y, z);
		glRotatef(posYaw, 0, 1, 0);
		glEnable(GL_RESCALE_NORMAL);

		float f2 = 1/(16f * 4f);
		glScalef(f2, f2, f2);

		drawPoster(poster);

		glDisable(GL_RESCALE_NORMAL);
		glPopMatrix();
	}

	private void drawPoster(EntityPoster poster) {
		int sizeX = 256; // sizex
		int sizeY = 256; // sizeY

		float f = (float)(-sizeX) / 2.0F;
		float f1 = (float)(-sizeY) / 2.0F;
		float f2 = 0.5F;
		float f3 = 0.75F;
		float f4 = 0.8125F;
		float f5 = 0.0F;
		float f6 = 0.0625F;
		float f7 = 0.75F;
		float f8 = 0.8125F;
		float f9 = 0.001953125F;
		float f10 = 0.001953125F;
		float f11 = 0.7519531F;
		float f12 = 0.7519531F;
		float f13 = 0.0F;
		float f14 = 0.0625F;

		for (int tileX = 0; tileX < sizeX / 64; ++tileX) {
			for (int tileY = 0; tileY < sizeY / 64; ++tileY) {
				float xEnd = f + (float)((tileX + 1) * 64);
				float xStart = f + (float)(tileX * 64);
				float yEnd = f1 + (float)((tileY + 1) * 64);
				float yStart = f1 + (float)(tileY * 64);

				setupLightmap(poster, (xEnd + xStart) / 2.0F, (yEnd + yStart) / 2.0F);

				float uStart = (float)(0 + sizeX - tileX * 64) / 256.0F;
				float uEnd = (float)(0 + sizeX - (tileX + 1) * 64) / 256.0F;
				float vStart = (float)(0 + sizeY - tileY * 64) / 256.0F;
				float vEnd = (float)(0 + sizeY - (tileY + 1) * 64) / 256.0F;
				Tessellator t = Tessellator.instance;
				
				bindTexture(vanillaPainting);
				
				t.startDrawingQuads();
				
				t.setNormal(0.0F, 1.0F, 0.0F);
				t.addVertexWithUV((double)xEnd, (double)yEnd, (double)(-f2), (double)f7, (double)f9);
				t.addVertexWithUV((double)xStart, (double)yEnd, (double)(-f2), (double)f8, (double)f9);
				t.addVertexWithUV((double)xStart, (double)yEnd, (double)f2, (double)f8, (double)f10);
				t.addVertexWithUV((double)xEnd, (double)yEnd, (double)f2, (double)f7, (double)f10);
				
				t.setNormal(0.0F, -1.0F, 0.0F);
				t.addVertexWithUV((double)xEnd, (double)yStart, (double)f2, (double)f7, (double)f9);
				t.addVertexWithUV((double)xStart, (double)yStart, (double)f2, (double)f8, (double)f9);
				t.addVertexWithUV((double)xStart, (double)yStart, (double)(-f2), (double)f8, (double)f10);
				t.addVertexWithUV((double)xEnd, (double)yStart, (double)(-f2), (double)f7, (double)f10);
				
				t.setNormal(-1.0F, 0.0F, 0.0F);
				t.addVertexWithUV((double)xEnd, (double)yEnd, (double)f2, (double)f12, (double)f13);
				t.addVertexWithUV((double)xEnd, (double)yStart, (double)f2, (double)f12, (double)f14);
				t.addVertexWithUV((double)xEnd, (double)yStart, (double)(-f2), (double)f11, (double)f14);
				t.addVertexWithUV((double)xEnd, (double)yEnd, (double)(-f2), (double)f11, (double)f13);

				t.setNormal(1.0F, 0.0F, 0.0F);
				t.addVertexWithUV((double)xStart, (double)yEnd, (double)(-f2), (double)f12, (double)f13);
				t.addVertexWithUV((double)xStart, (double)yStart, (double)(-f2), (double)f12, (double)f14);
				t.addVertexWithUV((double)xStart, (double)yStart, (double)f2, (double)f11, (double)f14);
				t.addVertexWithUV((double)xStart, (double)yEnd, (double)f2, (double)f11, (double)f13);
				
				// back
				t.setNormal(0.0F, 0.0F, 1.0F);
				t.addVertexWithUV((double)xEnd, (double)yEnd, (double)f2, (double)f3, (double)f5);
				t.addVertexWithUV((double)xStart, (double)yEnd, (double)f2, (double)f4, (double)f5);
				t.addVertexWithUV((double)xStart, (double)yStart, (double)f2, (double)f4, (double)f6);
				t.addVertexWithUV((double)xEnd, (double)yStart, (double)f2, (double)f3, (double)f6);

				t.draw();
				
				bindEntityTexture(poster);

				t.startDrawingQuads();

				// front
				t.setNormal(0.0F, 0.0F, -1.0F);
				t.addVertexWithUV((double)xEnd, (double)yStart, (double)(-f2), (double)uEnd, (double)vStart);
				t.addVertexWithUV((double)xStart, (double)yStart, (double)(-f2), (double)uStart, (double)vStart);
				t.addVertexWithUV((double)xStart, (double)yEnd, (double)(-f2), (double)uStart, (double)vEnd);
				t.addVertexWithUV((double)xEnd, (double)yEnd, (double)(-f2), (double)uEnd, (double)vEnd);

				t.draw();

				bindOverloadPicture(poster);

				t.startDrawingQuads();

				// front
				t.setNormal(0.0F, 0.0F, -1.0F);
				t.addVertexWithUV((double)xEnd, (double)yStart, (double)(-f2), (double)uEnd, (double)vStart);
				t.addVertexWithUV((double)xStart, (double)yStart, (double)(-f2), (double)uStart, (double)vStart);
				t.addVertexWithUV((double)xStart, (double)yEnd, (double)(-f2), (double)uStart, (double)vEnd);
				t.addVertexWithUV((double)xEnd, (double)yEnd, (double)(-f2), (double)uEnd, (double)vEnd);

				t.draw();
			}
		}
	}
	
	private void setupLightmap(EntityPoster screen, float par2, float par3) {
		int x = MathHelper.floor_double(screen.posX);
		int y = MathHelper.floor_double(screen.posY + (par3 / 64.0F));
		int z = MathHelper.floor_double(screen.posZ);

		switch (screen.hangingDirection) {
		case 2:
			x = MathHelper.floor_double(screen.posX + (par2 / 64.0F));
			break;
		case 1:
			z = MathHelper.floor_double(screen.posZ - (par2 / 64.0F));
			break;
		case 0:
			x = MathHelper.floor_double(screen.posX - (par2 / 64.0F));
			break;
		case 3:
			z = MathHelper.floor_double(screen.posZ + (par2 / 64.0F));
			break;
		}

		int lightLevel = renderManager.worldObj.getLightBrightnessForSkyBlocks(x, y, z, 0);
		int i1 = lightLevel % 65536;
		int j1 = lightLevel / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i1, j1);
		glColor3f(1.0F, 1.0F, 1.0F);
	}

	@Override
	protected void bindEntityTexture(Entity entity) {
		PhotoDataCache.bindTexture(((EntityPoster)entity).getPhotoId());
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}

	protected void bindOverloadPicture(Entity entity){
		Minecraft.getMinecraft().getTextureManager().bindTexture(Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("cameracraft.overlay",((EntityPaintable) entity).getDynamicTexture()));
	}
}
