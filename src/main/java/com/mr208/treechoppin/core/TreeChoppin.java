package com.mr208.treechoppin.core;

import com.mr208.treechoppin.common.config.ConfigurationHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import com.mr208.treechoppin.common.command.TCHCommand;
import com.mr208.treechoppin.common.handler.EventHandler;
import com.mr208.treechoppin.common.network.ClientSettingsMessage;
import com.mr208.treechoppin.common.network.ServerSettingsMessage;
import com.mr208.treechoppin.proxy.CommonProxy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.mr208.treechoppin.core.TreeChoppin.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION, dependencies = MOD_DEPENDENCIES, acceptableRemoteVersions = "*")

public class TreeChoppin
{

  public static final String MOD_ID = "treechoppin";
  public static final String MOD_NAME = "Tree Choppin";
  public static final String MOD_VERSION = "1.0.0";
  public static final String MOD_DEPENDENCIES = "required-after:forge@[14.23.2.2611,)";
  public static SimpleNetworkWrapper m_Network;
  
  public static Map<Item, Boolean> registeredAxes = new HashMap<>();
  public static Set<Block> registeredLogs = new HashSet<>();
  public static Set<Block> registeredLeaves = new HashSet<>();
  
  @SidedProxy(serverSide = "com.mr208.treechoppin.proxy.ServerProxy", clientSide = "com.mr208.treechoppin.proxy.CommonProxy")
  private static CommonProxy commonProxy;

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {

    m_Network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
    m_Network.registerMessage(ServerSettingsMessage.MsgHandler.class, ServerSettingsMessage.class, 0, Side.CLIENT);
    m_Network.registerMessage(ClientSettingsMessage.MsgHandler.class, ClientSettingsMessage.class, 1, Side.SERVER);
  }

  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(commonProxy);
    MinecraftForge.EVENT_BUS.register(new EventHandler());
  }

  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    
    for(String axe:ConfigurationHandler.axeTypesDamageable)
    {
      Item temp = Item.REGISTRY.getObject(new ResourceLocation(axe));
      if(temp!=Items.AIR)
        registeredAxes.put(temp, true);
    }
  
    for(String axe:ConfigurationHandler.axeTypesUndamageable)
    {
      Item temp = Item.REGISTRY.getObject(new ResourceLocation(axe));
      if(temp!=Items.AIR)
        registeredAxes.put(temp, false);
    }
    
    
    for(String log:ConfigurationHandler.blockWhiteList)
    {
      Block temp = Block.REGISTRY.getObject(new ResourceLocation(log));
      if(temp!=Blocks.AIR)
        registeredLogs.add(temp);
    }
  
    for(String log:ConfigurationHandler.leafWhiteList)
    {
      Block temp = Block.REGISTRY.getObject(new ResourceLocation(log));
      if(temp!=Blocks.AIR)
        registeredLeaves.add(temp);
    }
  }

  @Mod.EventHandler
  public void serverStarting(FMLServerStartingEvent event) {
    event.registerServerCommand(new TCHCommand());
  }
}
