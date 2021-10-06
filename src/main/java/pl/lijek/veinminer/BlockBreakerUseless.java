package pl.lijek.veinminer;

import lombok.RequiredArgsConstructor;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.Item;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.stat.Stats;
import net.minecraft.util.maths.Vec3i;
import pl.lijek.veinminer.mixin.BlockBaseInvoker;
import pl.lijek.veinminer.util.BlockAddAction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static net.minecraft.block.BlockBase.LEAVES;
import static net.minecraft.block.BlockBase.SNOW;
import static pl.lijek.veinminer.util.BlockAddAction.*;
import static pl.lijek.veinminer.VeinMiner.damageTool;
import static pl.lijek.veinminer.VeinMiner.maxDistanceNORMAL;

@RequiredArgsConstructor
public class BlockBreakerUseless {
    private final List<ItemInstance> drops = new ArrayList<>();
    public final Vec3i origin;
    public final Level level;
    public final PlayerBase player;
    public final int side;
    public final int targetID;
    public final int targetMeta;

    public int maxDistance = maxDistanceNORMAL;

    public BlockAddAction breakBlock(Vec3i target, BiFunction<Vec3i, Vec3i, Integer> measureDistance){

        int id = level.getTileId(target.x, target.y, target.z);
        int meta = level.getTileMeta(target.x, target.y, target.z);

        BlockBase targetBlock = BlockBase.BY_ID[targetID];
        BlockBase minedBlock = BlockBase.BY_ID[id];

        if ((id == 73 || id == 74) && (targetID == 73 || targetID == 74))
            id = targetID;

        if(!(id == targetID && (meta == 0 || meta == targetMeta)))
            return SKIP;

        int distance = measureDistance.apply(origin, target);
        if(!(maxDistanceNORMAL == -1 || distance <= maxDistance))
            return SKIP;

        if(!player.canRemoveBlock(minedBlock))
            return SKIP;
        if(useHeldItem(player, target.x, target.y, target.z, id))
            return CANCEL;

        minedBlock.activate(level, target.x, target.y, target.z, meta);
        level.setTile(target.x, target.y, target.z, 0);
        player.increaseStat(Stats.mineBlock[id], 1);
        addDrop(minedBlock, meta);

        return PASS;
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
        if(!damageTool)
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

    public void spawnDrop(){
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
        double offsetX = (double)(level.rand.nextFloat() * randomMultiplier) + (double)(1.0F - randomMultiplier) * 0.5D;
        double offsetY = (double)(level.rand.nextFloat() * randomMultiplier) + (double)(1.0F - randomMultiplier) * 0.5D;
        double offsetZ = (double)(level.rand.nextFloat() * randomMultiplier) + (double)(1.0F - randomMultiplier) * 0.5D;
        Item item = new Item(level, (double)origin.x + offsetX, (double)origin.y + offsetY, (double)origin.z + offsetZ, itemInstance);
        item.pickupDelay = 10;
        level.spawnEntity(item);
    }
}
