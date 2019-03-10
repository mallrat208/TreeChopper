package com.mr208.treechoppin.common.network;


import com.mr208.treechoppin.core.TreeChoppin;
import net.minecraft.network.PacketBuffer;

import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientSettingsMessage {

  private boolean m_ReverseShift;
  private boolean m_DisableShift;

  public ClientSettingsMessage(boolean reverseShift, boolean disableShift) {
    m_ReverseShift = reverseShift;
    m_DisableShift = disableShift;
  }
  
  public static void encode(ClientSettingsMessage msg, PacketBuffer buf)
  {
    buf.writeBoolean(msg.m_ReverseShift);
    buf.writeBoolean(msg.m_DisableShift);
  }
  
  public static ClientSettingsMessage decode(PacketBuffer buf)
  {
    return new ClientSettingsMessage(buf.readBoolean(), buf.readBoolean());
  }
  
  public static class Handler
  {
    public static void handle(final ClientSettingsMessage msg, Supplier<NetworkEvent.Context> ctx)
    {
      ctx.get().enqueueWork(()-> {
        TreeChoppin.reverseShift = msg.m_ReverseShift;
        TreeChoppin.disableShift = msg.m_DisableShift;
      });
      
      ctx.get().setPacketHandled(true);
    }
  }
}
