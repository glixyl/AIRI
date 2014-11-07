package com.arisux.airi;

import java.util.Arrays;

import com.arisux.airi.Settings.Setting;
import com.arisux.airi.api.obj3dapi.Obj3DAPI;
import com.arisux.airi.api.remapping.RemappingAPI;
import com.arisux.airi.api.updater.Updater;
import com.arisux.airi.api.updater.UpdaterAPI;
import com.arisux.airi.api.window.WindowAPI;
import com.arisux.airi.coremod.GlobalSettings;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = Properties.MODID, name = Properties.MODID, version = Properties.VERSION)
public class AIRI
{
	@SideOnly(Side.CLIENT)
	public WindowAPI windowapi;
	@SideOnly(Side.CLIENT)
	public UpdaterAPI updaterapi;
	@SideOnly(Side.CLIENT)
	public Obj3DAPI obj3dAPI;
	
	public static boolean ASM_INITIALIZED = false;

	public static Logger logger = new Logger();
	public RemappingAPI remappingApi;
	public Properties properties = new Properties();
	public SpawnSystem spawnsystem;
	public ServerTickHandler serverTickHandler;
	public ClientTickHandler clientTickHandler;
	public Settings settings;
	public FMLEventHandler fmlEvents;
	public Updater updater;
	@SideOnly(Side.CLIENT)
	public GlobalSettings global;

	@Mod.Instance(Properties.MODID)
	public static AIRI INSTANCE;

	public static AIRI instance()
	{
		return INSTANCE;
	}

	private void setModMetadataInfo(ModMetadata meta)
	{
		meta.autogenerated = false;
		meta.modId = Properties.MODID;
		meta.name = Properties.MODID;
		meta.version = Properties.VERSION;
		meta.credits = "The Minecraft Community";
		meta.authorList = Arrays.asList("Ri5ux");
		meta.description = "N/A";
		meta.url = properties.SERVER_ADDRESS + "/page/mods/airi/";
		meta.updateUrl = "";
		meta.screenshots = new String[0];
		meta.logoFile = "";
	}

	public static class Logger
	{
		public void info(String info, Object...args)
		{
			System.out.println(String.format("[AIRI/INFO] %s", String.format(info, args)));
		}

		public void bug(String info, Object...args)
		{
			System.out.println(String.format("[AIRI/BUG] %s. This should not happen, report it.", String.format(info, args)));
		}

		public void warning(String warning, Object...args)
		{
			System.out.println(String.format("[AIRI/WARNING] %s", String.format(warning, args)));
		}
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger.info("AIRI - Copyright(C) 2013-2015 Arisux");

		this.setModMetadataInfo(event.getModMetadata());

		(settings = new Settings()).preInit(event);
		(spawnsystem = new SpawnSystem()).initialize();
		(serverTickHandler = new ServerTickHandler()).initialize();
		(fmlEvents = new FMLEventHandler()).initialize();
		(remappingApi = new RemappingAPI()).initialize();
	}

	@SideOnly(Side.CLIENT)
	@Mod.EventHandler
	public void preInitClient(FMLPreInitializationEvent event)
	{
		this.global = new GlobalSettings();
		this.windowapi = new WindowAPI();
		this.updaterapi = new UpdaterAPI();
		this.obj3dAPI = new Obj3DAPI();
	}

	@SideOnly(Side.CLIENT)
	@Mod.EventHandler
	public void postInitClient(FMLPostInitializationEvent event)
	{
		(clientTickHandler = new ClientTickHandler()).postInitialize();
		(updater = updaterapi.createNewUpdater(Properties.MODID, Properties.VERSION, properties.URL_LATEST, properties.URL_MODPAGE, properties.URL_CHANGELOG)).initialize();
	}

	@Mod.EventHandler
	public void onLoadMissingMapping(FMLMissingMappingsEvent event)
	{
		(remappingApi).onLoadMissingMapping(event);
	}

	public void disableNetworking(String reason)
	{
		AIRI.logger.warning("Networking was automatically disabled. " + reason);
		AIRI.instance().settings.propertyList.get(Setting.NETWORKING).set(false);
	}
	
	public static void setASMInitialized(boolean b)
	{
		ASM_INITIALIZED = b;
	}
}