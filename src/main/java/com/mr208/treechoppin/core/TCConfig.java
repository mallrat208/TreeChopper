package com.mr208.treechoppin.core;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber
public class TCConfig
{
	protected static final ForgeConfigSpec.Builder BUILDER = new Builder();
	
	public static final Logs logs = new Logs();
	public static final Leaves leaves = new Leaves();
	public static final Axes axes = new Axes();
	public static final Options options = new Options();
	
	public static class Logs
	{
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> logBlocks;
		
		private ArrayList<String> logsDef = Lists.newArrayList();
		
		Logs()
		{
			BUILDER.push("logs");
			logBlocks = BUILDER.defineList("logs",logsDef, entry -> entry instanceof String);
			BUILDER.pop();
		}
	}
	
	public static class Leaves
	{
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> leaves;
		
		private ArrayList<String> leavesDef = Lists.newArrayList();
		
		Leaves()
		{
			BUILDER.push("leaves");
			leaves = BUILDER.defineList("leaves", leavesDef, entry -> entry instanceof String);
			BUILDER.pop();
		}
	}
	
	public static class Axes
	{
		public final ForgeConfigSpec.ConfigValue<List<? extends  String>> axesDamageable;
		
		public final ForgeConfigSpec.ConfigValue<List<? extends  String>> axesUndamagable;
		
		private ArrayList<String> axesDamageableDef = Lists.newArrayList("minecraft:wooden_axe",
				"minecraft:stone_axe",
				"minecraft:iron_axe",
				"minecraft:diamond_axe",
				"natura:netherquartz_axe",
				"natura:bloodwood_axe",
				"natura:fusewood_axe",
				"natura:darkwood_axe");
		
		private ArrayList<String> axesUndamageableDef = Lists.newArrayList("techguns:chainsaw",
				"mekanism:atomicdisassembler");
		
		
		Axes()
		{
			BUILDER.push("axes");
			axesDamageable = BUILDER.defineList("axesDamageable", axesDamageableDef, entry -> entry instanceof String);
			axesUndamagable = BUILDER.define("axesUndamageable", axesUndamageableDef,entry -> entry instanceof String);
			BUILDER.pop();
		}
	}
	
	public static class Options
	{
		public final ForgeConfigSpec.BooleanValue disableShift;
		public final ForgeConfigSpec.BooleanValue reverseShift;
		public final ForgeConfigSpec.BooleanValue plantSapling;
		public final ForgeConfigSpec.BooleanValue decayLeaves;
		
		Options()
		{
			BUILDER.push("options");
			disableShift = BUILDER.define("disableShift", false);
			reverseShift = BUILDER.define("reverseShift", false);
			plantSapling = BUILDER.define("plantSapling", true);
			decayLeaves = BUILDER.define("decayLeaves", true);
			BUILDER.pop();
		}
	}
	
	protected static final ForgeConfigSpec SPEC = BUILDER.build();
}
