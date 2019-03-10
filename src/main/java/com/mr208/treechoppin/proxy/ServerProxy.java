package com.mr208.treechoppin.proxy;

import com.mr208.treechoppin.core.TCConfig;
import com.mr208.treechoppin.core.TreeChoppin;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import com.mr208.treechoppin.common.handler.TreeHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber()
public class ServerProxy extends CommonProxy{
  
  @OnlyIn(Dist.DEDICATED_SERVER)
  public static void InteractWithTree(PlayerInteractEvent interactEvent) {
    int logCount;
    boolean shifting = true;

    if (!TreeChoppin.disableShift) {
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
}
