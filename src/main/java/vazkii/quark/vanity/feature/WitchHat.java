/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * 
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [05/06/2016, 20:23:23 (GMT)]
 */
package vazkii.quark.vanity.feature;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.quark.base.module.Feature;
import vazkii.quark.vanity.item.ItemWitchHat;

public class WitchHat extends Feature {

	public static Item witch_hat;
	
	public boolean halveDamage;
	public double dropRate;
	public double lootingBoost;

	@Override
	public void setupConfig() {
		halveDamage = loadPropBool("Halve witch damage", "", true);
		dropRate = loadPropDouble("Drop Chance from witches", "", 0.025);
		lootingBoost = loadPropDouble("Drop Chance boost per looting level", "", 0.01);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		witch_hat = new ItemWitchHat();
	}
	
	@SubscribeEvent
	public void onDrops(LivingDropsEvent event) {
		if(event.getEntityLiving() instanceof EntityWitch && Math.random() < (dropRate + lootingBoost * event.getLootingLevel()))
			event.getDrops().add(new EntityItem(event.getEntity().worldObj, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, new ItemStack(witch_hat)));
	}
	
	@SubscribeEvent
	public void onDamage(LivingHurtEvent event) {
		if(halveDamage && event.getSource().getEntity() != null && event.getSource().getEntity() instanceof EntityWitch) {
			ItemStack hat = event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD);
			if(hat != null && hat.getItem() == witch_hat)
				event.setAmount(event.getAmount() / 2);
		}
	}
	
	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
}
