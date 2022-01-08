package pl.lijek.veinminer.shapes;

import net.minecraft.level.Level;
import net.minecraft.util.maths.Vec3i;
import pl.lijek.veinminer.util.BlockAddAction;
import pl.lijek.veinminer.util.BlockData;
import pl.lijek.veinminer.util.Util;

import static pl.lijek.veinminer.VeinMiner.maxDistance;

public class StairsDown extends Shape{
    public StairsDown(Level level, Vec3i originVec3i, int hitSide, int playerFacing) {
        super(level, originVec3i, hitSide, playerFacing);
    }

    @Override
    public void selectBlocks(int x, int y, int z) {
        int dx = Util.offsetsXForSide[hitSide];
        int dy = Util.offsetsYForSide[hitSide];
        int dz = Util.offsetsZForSide[hitSide];

        for (int i = 0; i < maxDistance; i++) {
            BlockData target = new BlockData(x + (dx * i), y + (i * -1), z + (dz * i));
            BlockAddAction action = addBlock(target);
            if(action.cancel)
                return;

            target = new BlockData(x + (dx * i), y + (i * -1) + 1, z + (dz * i));
            action = addBlock(target);
            if(action.cancel)
                return;

            target = new BlockData(x + (dx * i), y + (i * -1) + 2, z + (dz * i));
            action = addBlock(target);
            if(action.cancel)
                return;
        }
    }
}
