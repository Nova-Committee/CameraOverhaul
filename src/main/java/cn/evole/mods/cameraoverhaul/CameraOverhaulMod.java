package cn.evole.mods.cameraoverhaul;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
		name = Const.MOD_NAME,
		modid = Const.MOD_ID,
		acceptedMinecraftVersions = "[1.12,]",
		useMetadata = true
		//, dependencies = Const.DEPENDENCIES
)
public class CameraOverhaulMod {

	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		Const.configPath = event.getModConfigurationDirectory().toPath();
	}

	@Mod.EventHandler
	public static void init(FMLInitializationEvent event) {
	}

}
