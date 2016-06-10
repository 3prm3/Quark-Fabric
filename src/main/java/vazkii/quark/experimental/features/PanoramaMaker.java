/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * 
 * Quark is Open Source and distributed under the
 * [ADD-LICENSE-HERE]
 * 
 * File Created @ [10/06/2016, 18:08:12 (GMT)]
 */
package vazkii.quark.experimental.features;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import com.google.common.collect.ImmutableSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.quark.base.lib.LibObfuscation;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;

public class PanoramaMaker extends Feature {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

	File panoramaDir;
	File currentDir;
	float rotationYaw, rotationPitch;
	int panoramaStep;
	boolean takingPanorama;
	int currentWidth, currentHeight;
	boolean overridenOnce;
	
	boolean overrideMainMenu;
	
	@Override
	public void setupConfig() {
		overrideMainMenu = loadPropBool("Use panorama screenshots on main menu", "", true);
	}
	
	@SubscribeEvent
	public void loadMainMenu(GuiOpenEvent event) {
		if(overrideMainMenu && !overridenOnce && event.getGui() instanceof GuiMainMenu) {
			File mcDir = ModuleLoader.configFile.getParentFile().getParentFile();
			File panoramasDir = new File(mcDir, "/screenshots/panoramas");
			List<File[]> validFiles = new ArrayList();
			
			ImmutableSet<String> set = ImmutableSet.of("panorama_0.png", "panorama_1.png", "panorama_2.png", "panorama_3.png", "panorama_4.png", "panorama_5.png");
			
			if(panoramasDir.exists()) {
				File[] subDirs;

				File mainMenu = new File(panoramasDir, "main_menu");
				if(mainMenu.exists())
					subDirs = new File[] { mainMenu };
				else subDirs = panoramasDir.listFiles((File f) -> f.isDirectory());
				
				for(File f : subDirs)
					if(set.stream().allMatch((String s) -> new File(f, s).exists()))
						validFiles.add(f.listFiles((File f1) -> set.contains(f1.getName())));
			}
			
			if(!validFiles.isEmpty()) {
				File[] files = validFiles.get(new Random().nextInt(validFiles.size()));
				Arrays.sort(files);
				
				Minecraft mc = Minecraft.getMinecraft();
				ResourceLocation[] resources = new ResourceLocation[6];
				
				for(int i = 0; i < resources.length; i++) {
					File f = files[i];
					try {
						DynamicTexture tex = new DynamicTexture(ImageIO.read(f));
						String name = "quark:" + f.getName();
						
						resources[i] = mc.getTextureManager().getDynamicTextureLocation(name, tex);
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
				}
				
				try {
					Field field = ReflectionHelper.findField(GuiMainMenu.class, LibObfuscation.TITLE_PANORAMA_PATHS);
					field.setAccessible(true);
					
					if(Modifier.isFinal(field.getModifiers())) {
						Field modfield = Field.class.getDeclaredField("modifiers");
						modfield.setAccessible(true);
						modfield.setInt(field, field.getModifiers() & ~Modifier.FINAL);
					}

					field.set(null, resources);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			overridenOnce = true;
		}
	}

	@SubscribeEvent
	public void takeScreenshot(ScreenshotEvent event) {
		if(takingPanorama)
			return;

		if(GuiScreen.isCtrlKeyDown() && GuiScreen.isShiftKeyDown()) {
			takingPanorama = true;
			panoramaStep = 0;

			if(panoramaDir == null)
				panoramaDir = new File(event.getScreenshotFile().getParentFile(), "panoramas");
			if(!panoramaDir.exists())
				panoramaDir.mkdirs();

			int i = 0;
			String ts = getTimestamp();
			do {
				if(i == 0)
					currentDir = new File(panoramaDir, ts);
				else currentDir = new File(panoramaDir, ts + "_" + i);
			} while(currentDir.exists());

			currentDir.mkdirs();

			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void renderTick(RenderTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();

		if(takingPanorama) {
			if(event.phase == Phase.START) {
				if(panoramaStep == 0) {
					mc.gameSettings.hideGUI = true;
					currentWidth = mc.displayWidth;
					currentHeight = mc.displayHeight;
					rotationYaw = mc.thePlayer.rotationYaw;
					rotationPitch = mc.thePlayer.rotationPitch;
					mc.resize(256, 256);	
				}

				switch(panoramaStep) {
				case 1:
					mc.thePlayer.rotationYaw = 180;
					mc.thePlayer.rotationPitch = 0;
					break;
				case 2:
					mc.thePlayer.rotationYaw = -90;
					mc.thePlayer.rotationPitch = 0;
					break;
				case 3:
					mc.thePlayer.rotationYaw = 0;
					mc.thePlayer.rotationPitch = 0;
					break;
				case 4:
					mc.thePlayer.rotationYaw = 90;
					mc.thePlayer.rotationPitch = 0;
					break;
				case 5:
					mc.thePlayer.rotationYaw = 180;
					mc.thePlayer.rotationPitch = -90;
					break;
				case 6:
					mc.thePlayer.rotationYaw = 180;
					mc.thePlayer.rotationPitch = 90;
					break;
				}
				mc.thePlayer.prevRotationYaw = mc.thePlayer.rotationYaw;
				mc.thePlayer.prevRotationPitch = mc.thePlayer.rotationPitch;
			} else {
				if(panoramaStep > 0)
					saveScreenshot(currentDir, "panorama_" + (panoramaStep - 1) + ".png", mc.displayWidth, mc.displayHeight, mc.getFramebuffer());
				panoramaStep++;
				if(panoramaStep == 7) {
					mc.gameSettings.hideGUI = false;
					takingPanorama = false;
					
					mc.thePlayer.rotationYaw = rotationYaw;
					mc.thePlayer.rotationPitch = rotationYaw;
					mc.thePlayer.prevRotationYaw = mc.thePlayer.rotationYaw;
					mc.thePlayer.prevRotationPitch = mc.thePlayer.rotationPitch;
					
					mc.resize(currentWidth, currentHeight);
				}
			}
		}
	}

	public static void saveScreenshot(File dir, String screenshotName, int width, int height, Framebuffer buffer) {
		try {
			BufferedImage bufferedimage = ScreenShotHelper.createScreenshot(width, height, buffer);
			File file2 = new File(dir, screenshotName);

			net.minecraftforge.client.event.ScreenshotEvent event = net.minecraftforge.client.ForgeHooksClient.onScreenshot(bufferedimage, file2);
			ImageIO.write(bufferedimage, "png", (File)file2);
		} catch(Exception exception) { }
	}

	private static String getTimestamp() {
		String s = DATE_FORMAT.format(new Date()).toString();
		return s;
	}

	@Override
	public boolean hasSubscriptions() {
		return isClient();
	}

}
