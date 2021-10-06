package pl.lijek.veinminer.util;

import net.minecraft.block.BlockBase;
import net.minecraft.util.maths.Vec3i;
import pl.lijek.veinminer.shapes.Shape;

public abstract class BlockMatcher {

    public abstract boolean matchBlocks(int originalID, int originalMeta, BlockBase originalBlock,
                                        int targetID, int targetMeta, BlockBase targetBlock);

    public boolean matchBlocks(Shape shape, Vec3i target){
        int targetID = shape.level.getTileId(target.x, target.y, target.z);
        if(targetID == 0)
            return false;
        int targetMeta = shape.level.getTileMeta(target.x, target.y, target.z);
        BlockBase targetBlock = BlockBase.BY_ID[targetID];
        return matchBlocks(shape.origin.id, shape.origin.meta, shape.originBlock, targetID, targetMeta, targetBlock);
    }

    public static BlockMatcher ID_META = new BlockMatcher() {
        @Override
        public boolean matchBlocks(int originalID, int originalMeta, BlockBase originalBlock, int targetID, int targetMeta, BlockBase targetBlock) {
            if ((originalID == 73 || originalID == 74) && (targetID == 73 || targetID == 74))
                originalID = targetID;

            return originalID == targetID && (originalMeta == 0 || originalMeta == targetMeta);
        }
    };
}
