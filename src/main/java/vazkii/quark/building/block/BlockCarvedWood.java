/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [30/03/2016, 18:42:47 (GMT)]
 */
package vazkii.quark.building.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import vazkii.quark.base.block.BlockMetaVariants;

public class BlockCarvedWood extends BlockMetaVariants {

	public BlockCarvedWood() {
		super("carved_wood", Material.WOOD, Variants.class);
		setHardness(2.0F);
		setResistance(5.0F);
		setSoundType(SoundType.WOOD);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

	public enum Variants implements EnumBase {
		CARVED_OAK_WOOD,
		CARVED_SPRUCE_WOOD,
		CARVED_BIRCH_WOOD,
		CARVED_JUNGLE_WOOD,
		CARVED_ACACIA_WOOD,
		CARVED_DARK_OAK_WOOD
	}

}
