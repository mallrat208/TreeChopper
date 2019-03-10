package com.mr208.treechoppin.core;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.TagsProvider;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import com.mr208.treechoppin.common.network.ClientSettingsMessage;
import com.mr208.treechoppin.common.network.ServerSettingsMessage;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.mr208.treechoppin.core.TreeChoppin.*;

@Mod(MOD_ID)
public class TreeChoppin
{
  public static final String MOD_ID = "treechoppin";
  
  private static final String PROTOCOL_VERSION =Integer.toString(1);
  public static SimpleChannel channel = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MOD_ID, "main_channel"))
          .clientAcceptedVersions(PROTOCOL_VERSION::equals)
          .serverAcceptedVersions(PROTOCOL_VERSION::equals)
          .networkProtocolVersion(()-> PROTOCOL_VERSION)
          .simpleChannel();
  
  public static Map<Item, Boolean> registeredAxes = new HashMap<>();
  public static Set<Block> registeredLogs = new HashSet<>();
  public static Set<Block> registeredLeaves = new HashSet<>();
  
  public static boolean plantSapling;
  public static boolean decayLeaves;
  public static boolean disableShift;
  public static boolean reverseShift;
  
  public TreeChoppin()
  {
    ModLoadingContext.get().registerConfig(Type.COMMON, TCConfig.SPEC);
  
    CommentedFileConfig configData = CommentedFileConfig.builder(FMLPaths.CONFIGDIR.get().resolve("treechoppin.toml"))
            .sync()
            .autosave()
            .writingMode(WritingMode.REPLACE)
            .build();
    
    configData.load();
    
    TCConfig.SPEC.setConfig(configData);
    
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
  }
  
  public void setup(FMLCommonSetupEvent event) {
    
    int pktID = 0;
    
    channel.registerMessage(pktID++, ServerSettingsMessage.class, ServerSettingsMessage::encode, ServerSettingsMessage::decode, ServerSettingsMessage.Handler::handle);
    channel.registerMessage(pktID++,  ClientSettingsMessage.class, ClientSettingsMessage::encode, ClientSettingsMessage::decode, ClientSettingsMessage.Handler::handle);
  }
  
  public void loadComplete(FMLLoadCompleteEvent event) {
    reverseShift = TCConfig.options.reverseShift.get();
    disableShift = TCConfig.options.disableShift.get();
    plantSapling = TCConfig.options.plantSapling.get();
    decayLeaves = TCConfig.options.decayLeaves.get();
    
    for(String axe:TCConfig.axes.axesDamageable.get())
    {
      Item temp = ForgeRegistries.ITEMS.getValue(new ResourceLocation(axe));
      if(temp!=Items.AIR)
        registeredAxes.put(temp, true);
    }
  
    for(String axe:TCConfig.axes.axesUndamagable.get())
    {
      Item temp = ForgeRegistries.ITEMS.getValue(new ResourceLocation(axe));
      if(temp!=Items.AIR)
        registeredAxes.put(temp, false);
    }
    
    registeredLogs.addAll(BlockTags.LOGS.getAllElements());
    for(String log:TCConfig.logs.logBlocks.get())
    {
      Block temp = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(log));
      if(temp!=Blocks.AIR && !registeredLogs.contains(temp))
        registeredLogs.add(temp);
    }
    
    registeredLogs.addAll(BlockTags.LEAVES.getAllElements());
    for(String log:TCConfig.leaves.leaves.get())
    {
      Block temp = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(log));
      if(temp!=Blocks.AIR && !registeredLeaves.contains(temp))
        registeredLeaves.add(temp);
    }
  }
}
