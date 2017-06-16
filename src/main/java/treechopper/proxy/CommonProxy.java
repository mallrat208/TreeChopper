package treechopper.proxy;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import treechopper.common.handler.TreeHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class PlayerInteract {

    public PlayerInteract(BlockPos blockPos, float logCount) {
        m_BlockPos = blockPos;
        m_LogCount = logCount;
    }

    public BlockPos m_BlockPos;
    public float m_LogCount;
};

public class CommonProxy {

    @SubscribeEvent
    public void InteractWithTree(PlayerInteractEvent interactEvent) {

        if (interactEvent.getSide().isServer()) { // Server - Singleplayer/LAN

            treeHandler = new TreeHandler();
            int logCount;

            if (CheckWoodenBlock(interactEvent.getWorld(), interactEvent.getPos())) {
                logCount = treeHandler.AnalyzeTree(interactEvent.getWorld(), interactEvent.getPos(), interactEvent.getEntityPlayer());

                m_PlayerSpeed.put(interactEvent.getEntityPlayer().getPersistentID(), new PlayerInteract(interactEvent.getPos(), logCount));
            } else {
                m_PlayerSpeed.remove(interactEvent.getEntityPlayer().getPersistentID());
            }
        }
    }

    @SubscribeEvent
    public void BreakingBlock(PlayerEvent.BreakSpeed breakSpeed) {

        if (breakSpeed.getEntityPlayer().getServer() == null) { // Server - Singleplayer/LAN

            if (m_PlayerSpeed.containsKey(breakSpeed.getEntityPlayer().getPersistentID())) {

                BlockPos blockPos = m_PlayerSpeed.get(breakSpeed.getEntityPlayer().getPersistentID()).m_BlockPos;

                if (blockPos.equals(breakSpeed.getPos())) {
                    breakSpeed.setNewSpeed(breakSpeed.getOriginalSpeed() / (m_PlayerSpeed.get(breakSpeed.getEntityPlayer().getPersistentID()).m_LogCount / 2.0f));
                } else {
                    breakSpeed.setNewSpeed(breakSpeed.getOriginalSpeed());
                }
            }

            System.out.println(breakSpeed.getNewSpeed());
        }
    }

    @SubscribeEvent
    public void DestroyWoodBlock(BlockEvent.BreakEvent breakEvent) {

        if (m_PlayerSpeed.containsKey(breakEvent.getPlayer().getPersistentID())) {

            BlockPos blockPos = m_PlayerSpeed.get(breakEvent.getPlayer().getPersistentID()).m_BlockPos;

            if (blockPos.equals(breakEvent.getPos())) {
                treeHandler.DestroyTree(breakEvent.getWorld(), breakEvent.getPlayer());
            }
        }
    }

    protected boolean CheckWoodenBlock(World world, BlockPos blockPos) {

        if (!world.getBlockState(blockPos).getBlock().isWood(world, blockPos)) {
            return false;
        }

        return true;
    }

    protected static Map<UUID, PlayerInteract> m_PlayerSpeed = new HashMap<>();
    private TreeHandler treeHandler;
}