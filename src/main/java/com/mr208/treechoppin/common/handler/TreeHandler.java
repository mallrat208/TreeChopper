package com.mr208.treechoppin.common.handler;

import com.mr208.treechoppin.core.TreeChoppin;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import com.mr208.treechoppin.common.tree.Tree;

import java.util.*;

public class TreeHandler {

  private static Map<UUID, Tree> m_Trees = new HashMap<>();
  private Tree tree;

  private static <T> T getLastElement(final Iterable<T> elements) {
    final Iterator<T> itr = elements.iterator();
    T lastElement = itr.next();

    while (itr.hasNext()) {
      lastElement = itr.next();
    }

    return lastElement;
  }

  public int AnalyzeTree(World world, BlockPos blockPos, EntityPlayer entityPlayer) {

    Queue<BlockPos> queuedBlocks = new LinkedList<>();
    Set<BlockPos> tmpBlocks = new HashSet<>();
    Set<BlockPos> checkedBlocks = new HashSet<>();
    BlockPos currentPos;
    Block logBlock = world.getBlockState(blockPos).getBlock();
    tree = new Tree();

    queuedBlocks.add(blockPos);
    tree.InsertWood(blockPos);

    while (!queuedBlocks.isEmpty()) {

      currentPos = queuedBlocks.remove();
      checkedBlocks.add(currentPos);

      tmpBlocks.addAll(LookAroundBlock(logBlock, currentPos, world, checkedBlocks));
      queuedBlocks.addAll(tmpBlocks);
      checkedBlocks.addAll(tmpBlocks);
      tmpBlocks.clear();
    }

    Set<BlockPos> tmpLeaves = new HashSet<>();
    tmpLeaves.addAll(tree.GetM_Leaves());

    for (BlockPos blockPos1 : tmpLeaves) {
      checkedBlocks.add(blockPos1);
      LookAroundBlock(null, blockPos1, world, checkedBlocks);
    }

    tree.setM_Position(blockPos);
    m_Trees.put(entityPlayer.getUniqueID(), tree);

    return tree.GetLogCount();
  }

  private Queue<BlockPos> LookAroundBlock(Block logBlock, BlockPos currentPos, World world, Set<BlockPos> checkedBlocks) {

    Queue<BlockPos> queuedBlocks = new LinkedList<>();
    BlockPos tmpPos;

    for (int i = -1; i <= 1; i++) {
      tmpPos = new BlockPos(currentPos.getX() + 1, currentPos.getY() + i, currentPos.getZ());
      if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
        queuedBlocks.add(tmpPos);
      }

      tmpPos = new BlockPos(currentPos.getX(), currentPos.getY() + i, currentPos.getZ() + 1);
      if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
        queuedBlocks.add(tmpPos);
      }

      tmpPos = new BlockPos(currentPos.getX() - 1, currentPos.getY() + i, currentPos.getZ());
      if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
        queuedBlocks.add(tmpPos);
      }

      tmpPos = new BlockPos(currentPos.getX(), currentPos.getY() + i, currentPos.getZ() - 1);
      if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
        queuedBlocks.add(tmpPos);
      }

      tmpPos = new BlockPos(currentPos.getX() + 1, currentPos.getY() + i, currentPos.getZ() + 1);
      if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
        queuedBlocks.add(tmpPos);
      }

      tmpPos = new BlockPos(currentPos.getX() - 1, currentPos.getY() + i, currentPos.getZ() - 1);
      if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
        queuedBlocks.add(tmpPos);
      }

      tmpPos = new BlockPos(currentPos.getX() - 1, currentPos.getY() + i, currentPos.getZ() + 1);
      if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
        queuedBlocks.add(tmpPos);
      }

      tmpPos = new BlockPos(currentPos.getX() + 1, currentPos.getY() + i, currentPos.getZ() - 1);
      if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
        queuedBlocks.add(tmpPos);
      }

      tmpPos = new BlockPos(currentPos.getX(), currentPos.getY() + i, currentPos.getZ());
      if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
        queuedBlocks.add(tmpPos);
      }
    }

    return queuedBlocks;
  }

  private boolean CheckBlock(World world, BlockPos blockPos, Set<BlockPos> checkedBlocks, Block originBlock) {

    if (checkedBlocks.contains(blockPos)) {
      return false;
    }

    if (world.getBlockState(blockPos).getBlock() != originBlock) {

      if (TreeChoppin.plantSapling && (world.getBlockState(blockPos).getMaterial() == Material.LEAVES) && tree.GetM_Leaves().isEmpty()) {
        tree.InsertLeaf(blockPos);
      }

      if (TreeChoppin.decayLeaves && TreeChoppin.registeredLeaves.contains(world.getBlockState(blockPos).getBlock())) {
        tree.InsertLeaf(blockPos);

        return false;
      }

      if (TreeChoppin.decayLeaves && (world.getBlockState(blockPos).getMaterial() == Material.LEAVES)) {
        tree.InsertLeaf(blockPos);

        return false;
      } else {
        return false;
      }
    }

    tree.InsertWood(blockPos);

    return true;
  }

  public void DestroyTree(IWorld world, EntityPlayer entityPlayer) {

    int soundReduced = 0;

    if (m_Trees.containsKey(entityPlayer.getUniqueID())) {

      Tree tmpTree = m_Trees.get(entityPlayer.getUniqueID());

      for (BlockPos blockPos : tmpTree.GetM_Wood()) {

        if (soundReduced <= 1) {
          world.destroyBlock(blockPos, true);
        } else {
          world.getBlockState(blockPos).dropBlockAsItem(world.getWorld(), blockPos, 0);
        }

        world.removeBlock(blockPos);

        soundReduced++;
      }

      if (TreeChoppin.plantSapling && !tmpTree.GetM_Leaves().isEmpty()) {

        BlockPos tmpPosition = getLastElement(tmpTree.GetM_Leaves());
        PlantSapling(world.getWorld(), tmpPosition, tmpTree.getM_Position());
      }

      soundReduced = 0;

      if (TreeChoppin.decayLeaves) {

        for (BlockPos blockPos : tmpTree.GetM_Leaves()) {

          if (soundReduced <= 1) {
            world.destroyBlock(blockPos, true);
          } else {
            world.getBlockState(blockPos).dropBlockAsItem(world.getWorld(), blockPos, 1);
          }

          world.removeBlock(blockPos);

          soundReduced++;
        }
      }
    }
  }

  private void PlantSapling(World world, BlockPos blockPos, BlockPos originPos) {

    Set<ItemStack> leafDrop = new HashSet<>();
    BlockPos plantPos1 = new BlockPos(originPos.getX() - 1, originPos.getY(), originPos.getZ() - 1);
    int counter = 0;

    while (leafDrop.isEmpty() && counter <= 100) {
      NonNullList<ItemStack> tmpList = NonNullList.create();
      world.getBlockState(blockPos).getDrops(tmpList, world, blockPos,  3);
      leafDrop.addAll(tmpList);

      counter++;
    }

    if (leafDrop.isEmpty()) {
      return;
    }

    FakePlayer fakePlayer = FakePlayerFactory.getMinecraft((WorldServer) world);
    fakePlayer.setHeldItem(EnumHand.MAIN_HAND, leafDrop.iterator().next());

    for (ItemStack itemStack : leafDrop) {
      itemStack.onItemUse(new ItemUseContext(fakePlayer, itemStack, plantPos1, EnumFacing.NORTH, 0,0,0 ));
    }
  }
}
