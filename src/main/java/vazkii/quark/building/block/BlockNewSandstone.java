/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [06/06/2016, 23:10:28 (GMT)]
 */
package vazkii.quark.building.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import vazkii.quark.base.block.BlockMetaVariants;

public class BlockNewSandstone extends BlockMetaVariants {

	public BlockNewSandstone() {
		super("sandstone_new", Material.ROCK, Variants.class);
		setHardness(0.8F);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

	public static enum Variants implements EnumBase {
		SANDSTONE_SMOOTH(false, true),
		SANDSTONE_BRICKS(true, true),
		RED_SANDSTONE_SMOOTH(false, true),
		RED_SANDSTONE_BRICKS(true, true);

		private Variants(boolean stairs, boolean slabs) {
			this.stairs = stairs;
			this.slabs = slabs;
		}
		public final boolean stairs, slabs;
	}

}
