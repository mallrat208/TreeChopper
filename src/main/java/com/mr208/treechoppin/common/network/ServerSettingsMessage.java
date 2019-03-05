package com.mr208.treechoppin.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.mr208.treechoppin.common.config.ConfigurationHandler;

public class ServerSettingsMessage implements IMessage {

  private boolean m_ReverseShift;
  private boolean m_DisableShift;

  public ServerSettingsMessage() {
  }

  public ServerSettingsMessage(boolean reverseShift, boolean disableShift) {
    m_ReverseShift = reverseShift;
    m_DisableShift = disableShift;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    m_ReverseShift = buf.readBoolean();
    m_DisableShift = buf.readBoolean();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeBoolean(m_ReverseShift);
    buf.writeBoolean(m_DisableShift);
  }

  public static class MsgHandler implements IMessageHandler<ServerSettingsMessage, IMessage> {

    @Override
    public IMessage onMessage(ServerSettingsMessage message, MessageContext ctx) {
      IThreadListener mainThread = Minecraft.getMinecraft();
      mainThread.addScheduledTask(new Runnable() {
        @Override
        public void run() {
          ConfigurationHandler.reverseShift = message.m_ReverseShift;
          ConfigurationHandler.disableShift = message.m_DisableShift;
        }
      });
      return null;
    }
  }
}
