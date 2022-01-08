package pl.lijek.veinminer.shapes;

import net.minecraft.level.Level;
import net.minecraft.util.maths.Vec3i;
import pl.lijek.veinminer.util.BlockAddAction;
import pl.lijek.veinminer.util.BlockData;
import pl.lijek.veinminer.util.Util;

import static pl.lijek.veinminer.VeinMiner.maxDistance;

public class Tunnel1x1 extends Shape{
    public Tunnel1x1(Level level, Vec3i originVec3i, int hitSide, int playerFacing) {
        super(level, originVec3i, hitSide, playerFacing);
    }

    @Override
    public void selectBlocks(int x, int y, int z) {
        int dx = Util.offsetsXForSide[hitSide];
        int dy = Util.offsetsYForSide[hitSide];
        int dz = Util.offsetsZForSide[hitSide];

        for (int i = 0; i < maxDistance; i++) {
            BlockData target = new BlockData(x + (dx * i), y + (dy * i), z + (dz * i));
            BlockAddAction action = addBlock(target);
            if(action.skipPass)
                continue;
            if(action.cancel)
                return;
        }
    }
}
