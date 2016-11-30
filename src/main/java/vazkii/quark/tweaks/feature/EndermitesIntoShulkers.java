package vazkii.quark.tweaks.feature;

import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.quark.base.module.Feature;
import vazkii.quark.tweaks.ai.AIFormShulker;

public class EndermitesIntoShulkers extends Feature {

	public static int chance = 100;

	@Override
	public void setupConfig() {
		chance = loadPropInt("Transform Chance", "The chance (1 in X) for an Endermite to turn into a Shulker.\nThe higher, the lower the chance. The chance for s Silverfish to bury is 10, for reference.", chance); 
	}
	
	@SubscribeEvent
	public void onEnterChunk(EnteringChunk event) {
		if(event.getEntity() instanceof EntityEndermite) {
			EntityEndermite endermite = (EntityEndermite) event.getEntity();
			for(EntityAITaskEntry task : endermite.tasks.taskEntries)
				if(task.action instanceof AIFormShulker)
					return;
			
			endermite.tasks.addTask(2, new AIFormShulker(endermite));
		}
	}
	
	@Override	
	public boolean hasSubscriptions() {
		return true;
	}
}
