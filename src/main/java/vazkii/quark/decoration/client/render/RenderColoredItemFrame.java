/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * 
 * Quark is Open Source and distributed under the
 * [ADD-LICENSE-HERE]
 * 
 * File Created @ [20/06/2016, 00:14:07 (GMT)]
 */
package vazkii.quark.decoration.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.decoration.entity.EntityColoredItemFrame;

// Basically a copy of RenderItemFrame
@SideOnly(Side.CLIENT)
public class RenderColoredItemFrame extends Render<EntityColoredItemFrame> {
	private static final ResourceLocation MAP_BACKGROUND_TEXTURES = new ResourceLocation("textures/map/map_background.png");
	private final Minecraft mc = Minecraft.getMinecraft();
	
	public static final IRenderFactory FACTORY = (RenderManager manager) -> new RenderColoredItemFrame(manager);
	
	private RenderItem itemRenderer;

	public RenderColoredItemFrame(RenderManager renderManagerIn) {
		super(renderManagerIn);
		this.itemRenderer = Minecraft.getMinecraft().getRenderItem();
	}

	@Override
	public void doRender(EntityColoredItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		BlockPos blockpos = entity.getHangingPosition();
		double d0 = (double)blockpos.getX() - entity.posX + x;
		double d1 = (double)blockpos.getY() - entity.posY + y;
		double d2 = (double)blockpos.getZ() - entity.posZ + z;
		GlStateManager.translate(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
		GlStateManager.rotate(180.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
		this.renderManager.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
		ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
		
		IBakedModel ibakedmodel1, ibakedmodel2;

		if (entity.getDisplayedItem() != null && entity.getDisplayedItem().getItem() == Items.FILLED_MAP)
		{
			ibakedmodel1 = modelmanager.getModel(vazkii.quark.base.handler.ModelHandler.resourceLocations.get("colored_item_frame_map_wood"));
			ibakedmodel2 = modelmanager.getModel(vazkii.quark.base.handler.ModelHandler.resourceLocations.get("colored_item_frame_map"));
		}
		else
		{
			ibakedmodel1 = modelmanager.getModel(vazkii.quark.base.handler.ModelHandler.resourceLocations.get("colored_item_frame_wood"));
			ibakedmodel2 = modelmanager.getModel(vazkii.quark.base.handler.ModelHandler.resourceLocations.get("colored_item_frame_normal"));
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);

		if (this.renderOutlines)
		{
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

		blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(ibakedmodel1, 1.0F, 1.0F, 1.0F, 1.0F);

		int color = ItemDye.DYE_COLORS[15 - entity.getColor()];
		float r = (float) ((color >> 16) & 0xFF) / 255F;
		float g = (float) ((color >> 8) & 0xFF) / 255F;
		float b = (float) (color & 0xFF) / 255F;
		
		blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(ibakedmodel2, 1.0F, r, g, b);
		
		if (this.renderOutlines)
		{
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.popMatrix();
		GlStateManager.translate(0.0F, 0.0F, 0.4375F);
		this.renderItem(entity);
		GlStateManager.popMatrix();
		this.renderName(entity, x + (double)((float)entity.facingDirection.getFrontOffsetX() * 0.3F), y - 0.25D, z + (double)((float)entity.facingDirection.getFrontOffsetZ() * 0.3F));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityColoredItemFrame entity) {
		return null;
	}

	private void renderItem(EntityColoredItemFrame itemFrame) {
		ItemStack itemstack = itemFrame.getDisplayedItem();

		if (itemstack != null)
		{
			EntityItem entityitem = new EntityItem(itemFrame.worldObj, 0.0D, 0.0D, 0.0D, itemstack);
			Item item = entityitem.getEntityItem().getItem();
			entityitem.getEntityItem().stackSize = 1;
			entityitem.hoverStart = 0.0F;
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			int i = itemFrame.getRotation();

			if (item instanceof net.minecraft.item.ItemMap)
			{
				i = i % 4 * 2;
			}

			GlStateManager.rotate((float)i * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);

			//            net.minecraftforge.client.event.RenderItemInFrameEvent event = new net.minecraftforge.client.event.RenderItemInFrameEvent(itemFrame, this);
			//            if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
			//            {
			if (item instanceof net.minecraft.item.ItemMap)
			{
				this.renderManager.renderEngine.bindTexture(MAP_BACKGROUND_TEXTURES);
				GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
				float f = 0.0078125F;
				GlStateManager.scale(f, f, f);
				GlStateManager.translate(-64.0F, -64.0F, 0.0F);
				MapData mapdata = Items.FILLED_MAP.getMapData(entityitem.getEntityItem(), itemFrame.worldObj);
				GlStateManager.translate(0.0F, 0.0F, -1.0F);

				if (mapdata != null)
				{
					this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, true);
				}
			}
			else
			{
				GlStateManager.scale(0.5F, 0.5F, 0.5F);

				if (!this.itemRenderer.shouldRenderItemIn3D(entityitem.getEntityItem()) || item instanceof ItemSkull)
				{
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				}

				GlStateManager.pushAttrib();
				RenderHelper.enableStandardItemLighting();
				this.itemRenderer.renderItem(entityitem.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);
				RenderHelper.disableStandardItemLighting();
				GlStateManager.popAttrib();
			}
			//            }

			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
	}

	@Override
	protected void renderName(EntityColoredItemFrame entity, double x, double y, double z) {
		if (Minecraft.isGuiEnabled() && entity.getDisplayedItem() != null && entity.getDisplayedItem().hasDisplayName() && this.renderManager.pointedEntity == entity)
		{
			double d0 = entity.getDistanceSqToEntity(this.renderManager.renderViewEntity);
			float f = entity.isSneaking() ? 32.0F : 64.0F;

			if (d0 < (double)(f * f))
			{
				String s = entity.getDisplayedItem().getDisplayName();
				this.renderLivingLabel(entity, s, x, y, z, 64);
			}
		}
	}
}