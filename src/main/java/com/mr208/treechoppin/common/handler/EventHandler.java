package com.mr208.treechoppin.common.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import com.mr208.treechoppin.common.config.ConfigurationHandler;
import com.mr208.treechoppin.common.network.ServerSettingsMessage;
import com.mr208.treechoppin.core.TreeChoppin;

public class EventHandler {

  @SubscribeEvent
  public void OnServerConnect(PlayerEvent.PlayerLoggedInEvent loggedInEvent) {
    TreeChoppin.m_Network.sendTo(new ServerSettingsMessage(ConfigurationHandler.reverseShift, ConfigurationHandler.disableShift), (EntityPlayerMP) loggedInEvent.player);
  }
}
