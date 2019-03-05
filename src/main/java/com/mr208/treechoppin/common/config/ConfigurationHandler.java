package com.mr208.treechoppin.common.config;

import net.minecraftforge.common.config.Config;
import com.mr208.treechoppin.core.TreeChoppin;

@Config(modid = TreeChoppin.MOD_ID)
public class ConfigurationHandler {
  
  @Config.Name("Decay Leaves")
  @Config.Comment("Cut down whole tree - wooden blocks and leaves")
  public static boolean decayLeaves = true;
  
  @Config.Name("Reverse shift")
  @Config.Comment("Reverse shift function - Mod works with shift pressing")
  public static boolean reverseShift = false;
  
  @Config.Name("Disable shift")
  @Config.Comment("Disable shift function - Always chop trees regardless of shift pressing")
  public static boolean disableShift = false;
  
  @Config.Name("Plant sapling")
  @Config.Comment("Automatic sapling plant on tree chop")
  public static boolean plantSapling = false;

  @Config.Name("Whitelisted Axes - Undamageable")
  @Config.Comment({"Axes to be used for Choppin Trees. These will not take extra damage","Use the Item's Registry Name"})
  public static String[] axeTypesUndamageable = new String[] {
          "techguns:chainsaw",
          "mekanism:atomicdisassembler"
  };
  
  @Config.Name("Whitelisted Axes - Damageable")
  @Config.Comment({"Axes to be used for Choppin Trees. These will take extra damage when used","Use the Item's Registry Name"})
  public static String[] axeTypesDamageable = new String[]{
          "minecraft:wooden_axe",
          "minecraft:stone_axe",
          "minecraft:iron_axe",
          "minecraft:diamond_axe",
          "natura:netherquartz_axe",
          "natura:bloodwood_axe",
          "natura:fusewood_axe",
          "natura:darkwood_axe"
         };
  
  @Config.Name("Whitelisted blocks")
  @Config.Comment({"Put here allowed log blocks","Use the block's Registry Name"})
  public static String[] blockWhiteList = new String[]{
          "minecraft:log",
          "minecraft:log2",
          "harvestcraft:pamcinnamon",
          "harvestcraft:pammaple",
          "harvestcraft:pampaperbark",
          "biomesoplenty:log_0",
          "biomesoplenty:log_1",
          "biomesoplenty:log_2",
          "biomesoplenty:log_3",
          "biomesoplenty:log_4",
          "natura:overworld_logs",
          "natura:overworld_logs2",
          "natura:nether_logs",
          "natura:redwood_logs"
  };
  
  @Config.Name("Whitelisted leaves")
  @Config.Comment({"Put here allowed special blocks - e.g. fruit, ","Use the Block's Registry Name"})
  public static String[] leafWhiteList =  new String[]{
          "harvestcraft:pamdate",
          "harvestcraft:pampistachio",
          "harvestcraft:pampapaya",
          "harvestcraft:pamwalnut",
          "harvestcraft:pamcherry",
          "harvestcraft:pamfig",
          "harvestcraft:pamdragonfruit",
          "harvestcraft:pamapple",
          "harvestcraft:pamlemon",
          "harvestcraft:pampear",
          "harvestcraft:pamolive",
          "harvestcraft:pamgrapefruit",
          "harvestcraft:pampomegranate",
          "harvestcraft:pamcashew",
          "harvestcraft:pamvanilla",
          "harvestcraft:pamstarfruit",
          "harvestcraft:pambanana",
          "harvestcraft:pamplum",
          "harvestcraft:pamavocadu",
          "harvestcraft:pampecan",
          "harvestcraft:pampistachio",
          "harvestcraft:pamlime",
          "harvestcraft:pampeppercorn",
          "harvestcraft:pamalmond",
          "harvestcraft:pamgooseberry",
          "harvestcraft:pampeach",
          "harvestcraft:pamchestnut",
          "harvestcraft:pamcoconut",
          "harvestcraft:pammango",
          "harvestcraft:pamapricot",
          "harvestcraft:pamorange",
          "harvestcraft:pampersimmon",
          "harvestcraft:pamnutmeg",
          "harvestcraft:pamdurian",
          "biomesoplenty:leaves_0",
          "biomesoplenty:leaves_1",
          "biomesoplenty:leaves_2",
          "biomesoplenty:leaves_3",
          "biomesoplenty:leaves_4",
          "biomesoplenty:leaves_5",
          "natura:nether_leaves",
          "natura:nether_leaves2",
          "natura:redwood_leaves",
          "natura:overworld_leaves",
          "natura:overworld_leaves2"
  };
  

  public static void setPlantSap(boolean plantSap) {
    ConfigurationHandler.plantSapling = plantSap;
  }

  public static void setReverseShi(boolean reverseShi) {
    ConfigurationHandler.reverseShift = reverseShi;
  }

  public static void setDisableShi(boolean disableShi) {
    ConfigurationHandler.disableShift = disableShi;
  }

  public static void setDecayLea(boolean decayLea) {
    ConfigurationHandler.decayLeaves = decayLea;
  }
}
