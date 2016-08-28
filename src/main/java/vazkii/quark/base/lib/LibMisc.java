/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [18/03/2016, 21:39:39 (GMT)]
 */
package vazkii.quark.base.lib;

import java.util.Arrays;
import java.util.List;

public final class LibMisc {

	// Mod Constants
	public static final String MOD_ID = "Quark";
	public static final String MOD_NAME = MOD_ID;
	public static final String BUILD = "GRADLE:BUILD";
	public static final String VERSION = "GRADLE:VERSION-" + BUILD;
	public static final String DEPENDENCIES = "required-after:Forge@[12.17.0.1909,);required-after:AutoRegLib;";
	public static final String RESOURCE_NAMESPACE = MOD_ID.toLowerCase();
	public static final String PREFIX_MOD = RESOURCE_NAMESPACE + ":";

	// Proxy Constants
	public static final String PROXY_COMMON = "vazkii.quark.base.proxy.CommonProxy";
	public static final String PROXY_CLIENT = "vazkii.quark.base.proxy.ClientProxy";
	public static final String GUI_FACTORY = "vazkii.quark.base.handler.GuiFactory";

	// Oredict Dyes
	public static final List<String> OREDICT_DYES = Arrays.asList(new String[]{
			"dyeBlack",
			"dyeRed",
			"dyeGreen",
			"dyeBrown",
			"dyeBlue",
			"dyePurple",
			"dyeCyan",
			"dyeLightGray",
			"dyeGray",
			"dyePink",
			"dyeLime",
			"dyeYellow",
			"dyeLightBlue",
			"dyeMagenta",
			"dyeOrange",
			"dyeWhite"
	});
	
}
