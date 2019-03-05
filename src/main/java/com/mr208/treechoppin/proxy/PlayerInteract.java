package com.mr208.treechoppin.proxy;

import net.minecraft.util.math.BlockPos;

class PlayerInteract {

  public BlockPos m_BlockPos; // Interact block position
  public float m_LogCount;
  public int m_AxeDurability;

  public PlayerInteract(BlockPos blockPos, float logCount, int axeDurability) {
    m_BlockPos = blockPos;
    m_LogCount = logCount;
    m_AxeDurability = axeDurability;
  }
}
