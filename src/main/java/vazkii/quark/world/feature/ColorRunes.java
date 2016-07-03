/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * 
 * Quark is Open Source and distributed under the
 * [ADD-LICENSE-HERE]
 * 
 * File Created @ [19/06/2016, 01:13:02 (GMT)]
 */
package vazkii.quark.world.feature;

import java.awt.Color;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.quark.base.handler.ICustomEnchantColor;
import vazkii.quark.base.handler.ItemNBTHelper;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.world.feature.AncientTomes.EnchantTomeFunction;
import vazkii.quark.world.item.ItemRune;

public class ColorRunes extends Feature {

	public static final String TAG_RUNE_ATTACHED = "Quark:RuneAttached";
	public static final String TAG_RUNE_COLOR = "Quark:RuneColor";

	private static ItemStack targetStack;

	public static Item rune;

	int dungeonWeight, netherFortressWeight, jungleTempleWeight, desertTempleWeight, itemQuality;

	@Override
	public void setupConfig() {
		dungeonWeight = loadPropInt("Dungeon loot weight", "", 20);
		netherFortressWeight = loadPropInt("Nether Fortress loot weight", "", 15);
		jungleTempleWeight = loadPropInt("Jungle Temple loot weight", "", 15);
		desertTempleWeight = loadPropInt("Desert Temple loot weight", "", 15);
		itemQuality = loadPropInt("Item quality for loot", "", 0);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		rune = new ItemRune();
	}

	@SubscribeEvent
	public void onLootTableLoad(LootTableLoadEvent event) {
		LootFunction[] funcs = new LootFunction[] { new SetMetadata(new LootCondition[0], new RandomValueRange(0, 15)) };

		if(event.getName().equals(LootTableList.CHESTS_SIMPLE_DUNGEON))
			event.getTable().getPool("main").addEntry(new LootEntryItem(rune, dungeonWeight, itemQuality, funcs, new LootCondition[0], "quark:rune"));
		else if(event.getName().equals(LootTableList.CHESTS_NETHER_BRIDGE))
			event.getTable().getPool("main").addEntry(new LootEntryItem(rune, netherFortressWeight, itemQuality, funcs, new LootCondition[0], "quark:rune"));
		else if(event.getName().equals(LootTableList.CHESTS_JUNGLE_TEMPLE))
			event.getTable().getPool("main").addEntry(new LootEntryItem(rune, jungleTempleWeight, itemQuality, funcs, new LootCondition[0], "quark:rune"));
		else if(event.getName().equals(LootTableList.CHESTS_DESERT_PYRAMID))
			event.getTable().getPool("main").addEntry(new LootEntryItem(rune, desertTempleWeight, itemQuality, funcs, new LootCondition[0], "quark:rune"));
	}

	@SubscribeEvent
	public void onAnvilUpdate(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();

		if (left != null && right != null && left.isItemEnchanted() && right.getItem() == rune) {
			ItemStack out = left.copy();
			ItemNBTHelper.setBoolean(out, TAG_RUNE_ATTACHED, true);
			ItemNBTHelper.setInt(out, TAG_RUNE_COLOR, right.getItemDamage());
			event.setOutput(out);
			event.setCost(15);
		}
	}

	@Override
	public boolean hasSubscriptions() {
		return true;
	}

	// Called from ASM. See ClassTransformer
	public static void setTargetStack(ItemStack stack) {
		targetStack = stack;
	}

	// Called from ASM. See ClassTransformer
	public static int getColor() {
		if(!ModuleLoader.isFeatureEnabled(ColorRunes.class) || (!doesStackHaveRune(targetStack) && targetStack != null && !(targetStack.getItem() instanceof ICustomEnchantColor)))
			return 0xff8040cc;

		return getColorFromStack(targetStack);
	}

	// Called from ASM. See ClassTransformer
	public static void applyColor(float f1, float f2, float f3, float f4) {
		if(!ModuleLoader.isFeatureEnabled(ColorRunes.class) || !doesStackHaveRune(targetStack)) {
			GlStateManager.color(f1, f2, f3, f4);
			return;
		}

		int color = getColorFromStack(targetStack);
		float r = (float) ((color >> 16) & 0xFF) / 255F;
		float g = (float) ((color >> 8) & 0xFF) / 255F;
		float b = (float) (color & 0xFF) / 255F;

		GlStateManager.color(r, g, b, 1F);
	}

	public static int getColorFromStack(ItemStack stack) {
		if(stack == null)
			return 0xFFFFFF;

		int retColor = 0xFFFFFF;
		boolean truncate = true;

		if (stack.getItem() instanceof ICustomEnchantColor) {
			int color = ((ICustomEnchantColor) stack.getItem()).getEnchantEffectColor(stack);
			truncate = ((ICustomEnchantColor) stack.getItem()).shouldTruncateColorBrightness(stack);
			retColor = 0xFF000000 | color;
		} else if (doesStackHaveRune(stack)) {
			int color = ItemDye.DYE_COLORS[15 - Math.min(15, ItemNBTHelper.getInt(targetStack, TAG_RUNE_COLOR, 0))];
			retColor = 0xFF000000 | color;
		}

		if(truncate) {
			int r = (retColor >> 16) & 0xFF;
			int g = (retColor >> 8) & 0xFF;
			int b = retColor & 0xFF;

			int t = r + g + b;
			if (t > 396) {
				float mul = 396F / (float) t;
				r = (int) ((float) r * mul);
				g = (int) ((float) g * mul);
				b = (int) ((float) b * mul);

				retColor = (0xFF << 24) + (r << 16) + (g << 8) + b;
			}
		}

		return retColor;
	}

	public static boolean doesStackHaveRune(ItemStack stack) {
		return stack != null && stack.hasTagCompound() && ItemNBTHelper.getBoolean(stack, TAG_RUNE_ATTACHED, false);
	}

}
