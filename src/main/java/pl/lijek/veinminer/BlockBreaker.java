package pl.lijek.veinminer;

import lombok.RequiredArgsConstructor;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.Item;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.Pickaxe;
import net.minecraft.item.tool.Shears;
import net.minecraft.item.tool.Shovel;
import net.minecraft.item.tool.Sword;
import net.minecraft.level.Level;
import net.minecraft.stat.Stats;
import net.minecraft.util.maths.Vec3i;
import pl.lijek.veinminer.mixin.BlockBaseInvoker;
import pl.lijek.veinminer.util.BlockData;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.block.BlockBase.LEAVES;
import static net.minecraft.block.BlockBase.SNOW;

@RequiredArgsConstructor
public class BlockBreaker {
    private final PlayerBase player;
    private final Level level;
    private final List<BlockData> blocksToBreak;
    private final List<ItemInstance> drops = new ArrayList<>();

    public void breakBlocks(){
        for(Vec3i block : blocksToBreak){

            int id = level.getTileId(block.x, block.y, block.z);
            int meta = level.getTileMeta(block.x, block.y, block.z);

            BlockBase minedBlock = BlockBase.BY_ID[id];
            if(minedBlock == null)
                continue;

            if(isHoldingTool(player) && !player.canRemoveBlock(minedBlock))
                continue;
            if(useHeldItem(player, block.x, block.y, block.z, id))
                break;


            minedBlock.activate(level, block.x, block.y, block.z, meta);
            level.setTile(block.x, block.y, block.z, 0);
            level.playLevelEvent(2001, block.x, block.y, block.z, id + meta * 256);
            player.increaseStat(Stats.mineBlock[id], 1);
            addDrop(minedBlock, meta);
        }
    }

    private boolean isHoldingTool(PlayerBase player){
        ItemInstance itemInstance = player.getHeldItem();
        if (itemInstance == null) return false;
        ItemBase item = itemInstance.getType();
        if(item == null) return false;
        return item instanceof Pickaxe ||
                item instanceof Shears ||
                item instanceof Shovel ||
                item instanceof Sword;
    }

    private void addDrop(ItemInstance drop){
        int index = -1;
        for (int i = 0; i < drops.size(); i++) {
            if (drops.get(i).isDamageAndIDIdentical(drop)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            drops.add(drop);
        } else {
            int quantity = drop.count;
            drop = drops.get(index);
            drop.count += quantity;
        }
    }

    protected void addDrop(BlockBase block, int meta){
        int id = block.getDropId(meta, level.rand);
        int count = block.getDropCount(level.rand);
        int dropMeta = ((BlockBaseInvoker)block).invokeDroppedMeta(meta);

        if(block == LEAVES &&
                (player.getHeldItem() != null && player.getHeldItem().itemId == ItemBase.shears.id)) {
            id = LEAVES.id;
            count = 1;
            dropMeta = meta & 3;
        }else if(block == SNOW){
            count = 1;
        }
        if(id <= 0 || count <= 0)
            return;

        addDrop(new ItemInstance(id, count, dropMeta));
    }

    protected boolean useHeldItem(PlayerBase player, int x, int y, int z, int blockID){
        if(!VeinMiner.config.damageTool)
            return false;
        ItemInstance heldItem = player.getHeldItem();
        if (heldItem != null) {
            heldItem.postMine(blockID, x, y, z, player);
            if (heldItem.count == 0) {
                heldItem.unusedEmptyMethod1(player);
                player.breakHeldItem();
                return true;
            }
        }
        return false;
    }

    public void spawnDrops(){
        for (ItemInstance drop : drops) {
            if(drop.count > drop.getMaxStackSize()){
                for (int i = 0; i < drop.count / drop.getMaxStackSize(); i++) {
                    spawnItemEntity(new ItemInstance(drop.itemId, drop.getMaxStackSize(), drop.getDamage()));
                }
                if(drop.count % drop.getMaxStackSize() > 0)
                    spawnItemEntity(new ItemInstance(drop.itemId, drop.count % drop.getMaxStackSize(), drop.getDamage()));
            }else
                spawnItemEntity(drop);
        }
    }

    private void spawnItemEntity(ItemInstance itemInstance){
        float randomMultiplier = 0.7F;
        double offsetX = 0.0d;//(double)(level.rand.nextFloat() * randomMultiplier) + (double)(1.0F - randomMultiplier) * 0.5D;
        double offsetY = 1.0d;//(double)(level.rand.nextFloat() * randomMultiplier) + (double)(1.0F - randomMultiplier) * 0.5D;
        double offsetZ = 0.0d;//(double)(level.rand.nextFloat() * randomMultiplier) + (double)(1.0F - randomMultiplier) * 0.5D;
        Item item = new Item(level, player.x + offsetX, player.y + offsetY, player.z + offsetZ, itemInstance);
        item.pickupDelay = 10;
        level.spawnEntity(item);
    }
}
