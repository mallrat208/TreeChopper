package com.mr208.treechoppin.proxy;

import com.mr208.treechoppin.core.TCConfig;
import com.mr208.treechoppin.core.TreeChoppin;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import com.mr208.treechoppin.common.handler.TreeHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

;

@EventBusSubscriber()
public class CommonProxy {

  public static Map<UUID, Boolean> m_PlayerPrintNames = new HashMap<>();
  protected static Map<UUID, PlayerInteract> m_PlayerData = new HashMap<>();
  protected static TreeHandler treeHandler;

  @SubscribeEvent
  public static void InteractWithTree(PlayerInteractEvent interactEvent) {

    int logCount;
    boolean shifting = true;

    if (!TCConfig.options.disableShift.get()) {
      if (interactEvent.getEntityPlayer().isSneaking() && !TreeChoppin.reverseShift) {
        shifting = false;
      }

      if (!interactEvent.getEntityPlayer().isSneaking() && TreeChoppin.reverseShift) {
        shifting = false;
      }
    }

    if (CheckWoodenBlock(interactEvent.getWorld(), interactEvent.getPos()) && CheckItemInHand(interactEvent.getEntityPlayer()) && shifting) {

      int axeDurability = interactEvent.getEntityPlayer().getHeldItemMainhand().getMaxDamage() - interactEvent.getEntityPlayer().getHeldItemMainhand().getDamage();

      if (m_PlayerData.containsKey(interactEvent.getEntityPlayer().getUniqueID()) &&
              m_PlayerData.get(interactEvent.getEntityPlayer().getUniqueID()).m_BlockPos.equals(interactEvent.getPos()) &&
              m_PlayerData.get(interactEvent.getEntityPlayer().getUniqueID()).m_AxeDurability == axeDurability) {
        return;
      }

      treeHandler = new TreeHandler();
      logCount = treeHandler.AnalyzeTree(interactEvent.getWorld(), interactEvent.getPos(), interactEvent.getEntityPlayer());

            /*System.out.println("Max damage: " + interactEvent.getEntityPlayer().getHeldItemMainhand().getMaxDamage());
            System.out.println("Item damage: " + interactEvent.getEntityPlayer().getHeldItemMainhand().getItemDamage());*/

      if (interactEvent.getEntityPlayer().getHeldItemMainhand().isDamageable() && axeDurability < logCount) {
        m_PlayerData.remove(interactEvent.getEntityPlayer().getUniqueID());
        return;
      }

      if (logCount > 1) {
        m_PlayerData.put(interactEvent.getEntityPlayer().getUniqueID(), new PlayerInteract(interactEvent.getPos(), logCount, axeDurability));
      }
    } else {
      m_PlayerData.remove(interactEvent.getEntityPlayer().getUniqueID());
    }
  }

  @SubscribeEvent
  public static void BreakingBlock(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed breakSpeed) {

    if (m_PlayerData.containsKey(breakSpeed.getEntityPlayer().getUniqueID())) {

      BlockPos blockPos = m_PlayerData.get(breakSpeed.getEntityPlayer().getUniqueID()).m_BlockPos;

      if (blockPos.equals(breakSpeed.getPos())) {
        breakSpeed.setNewSpeed(breakSpeed.getOriginalSpeed() / (m_PlayerData.get(breakSpeed.getEntityPlayer().getUniqueID()).m_LogCount / 2.0f));
      } else {
        breakSpeed.setNewSpeed(breakSpeed.getOriginalSpeed());
      }
    }
  }

  @SubscribeEvent
  public static void DestroyWoodBlock(BlockEvent.BreakEvent breakEvent) {

    if (m_PlayerData.containsKey(breakEvent.getPlayer().getUniqueID())) {

      BlockPos blockPos = m_PlayerData.get(breakEvent.getPlayer().getUniqueID()).m_BlockPos;

      if (blockPos.equals(breakEvent.getPos())) {
        treeHandler.DestroyTree(breakEvent.getWorld(), breakEvent.getPlayer());

        if (!breakEvent.getPlayer().isCreative() && breakEvent.getPlayer().getHeldItemMainhand().isDamageable() && TreeChoppin.registeredAxes.getOrDefault(breakEvent.getPlayer().getHeldItemMainhand().getItem(), false)) {
          int extraDamage = (int)(m_PlayerData.get(breakEvent.getPlayer().getUniqueID()).m_LogCount * 1.5);
          breakEvent.getPlayer().getHeldItemMainhand().damageItem(extraDamage, breakEvent.getPlayer());
        }
      }
    }
  }

  protected static boolean CheckWoodenBlock(World world, BlockPos blockPos) {

    if(TreeChoppin.registeredLogs.contains(world.getBlockState(blockPos).getBlock()))
      return true;
    
    return world.getBlockState(blockPos).getMaterial() == Material.WOOD;
  }

  protected static boolean CheckItemInHand(EntityPlayer entityPlayer) {

    if (entityPlayer.getHeldItemMainhand().isEmpty()) {
      return false;
    }
    
    if(TreeChoppin.registeredAxes.containsKey(entityPlayer.getHeldItemMainhand().getItem()))
      return true;

    boolean test;

    try {
      ItemAxe tmp = (ItemAxe) entityPlayer.getHeldItemMainhand().getItem();
      test = true;
    } catch (Exception e) {
      test = false;
    }

    return test;
  }
}