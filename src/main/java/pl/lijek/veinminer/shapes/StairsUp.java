package pl.lijek.veinminer.shapes;

import net.minecraft.level.Level;
import net.minecraft.util.maths.Vec3i;
import pl.lijek.veinminer.util.BlockAddAction;
import pl.lijek.veinminer.util.BlockData;
import pl.lijek.veinminer.util.Util;

import static pl.lijek.veinminer.VeinMiner.maxDistance;

public class StairsUp extends Shape{
    public StairsUp(Level level, Vec3i originVec3i, int hitSide, int playerFacing) {
        super(level, originVec3i, hitSide, playerFacing);
    }

    @Override
    public void selectBlocks(int x, int y, int z) {
        int dx, dz;
        if(hitSide < 2){
            dx = Util.offsetsXForSide[hitSide];
            dz = Util.offsetsZForSide[hitSide];
        }else{
            dx = Util.directionXMultiplier[playerFacing];
            dz = Util.directionZMultiplier[playerFacing];
        }

        for (int i = 0; i < maxDistance; i++) {
            BlockData target = new BlockData(x + (dx * i), y + (i), z + (dz * i));
            BlockAddAction action = addBlock(target);
            if(action.cancel)
                return;

            target = new BlockData(x + (dx * i), y + (i) + 1, z + (dz * i));
            action = addBlock(target);
            if(action.cancel)
                return;

            target = new BlockData(x + (dx * i), y + (i) + 2, z + (dz * i));
            action = addBlock(target);
            if(action.cancel)
                return;
        }
    }
}
