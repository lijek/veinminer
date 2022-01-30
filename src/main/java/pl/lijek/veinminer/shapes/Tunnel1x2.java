package pl.lijek.veinminer.shapes;

import net.minecraft.level.Level;
import net.minecraft.util.maths.Vec3i;
import pl.lijek.veinminer.util.BlockAddAction;
import pl.lijek.veinminer.util.BlockData;
import pl.lijek.veinminer.util.Util;

import static pl.lijek.veinminer.VeinMiner.config;

public class Tunnel1x2 extends Shape{
    public Tunnel1x2(Level level, Vec3i originVec3i, int hitSide, int playerFacing) {
        super(level, originVec3i, hitSide, playerFacing);
    }

    @Override
    public void selectBlocks(int x, int y, int z) {

        if(hitSide < 2) {
            int dx = Util.directionXMultiplier[playerFacing];
            int dy = Util.offsetsYForSide[hitSide];
            int dz = Util.directionZMultiplier[playerFacing];
            for (int i = 0; i < config.maxDistance; i++) {
                BlockData target = new BlockData(x, y + (i * dy), z);
                BlockAddAction action = addBlock(target);
                if (action.cancel)
                    return;

                target = new BlockData(x + dx, y + (i * dy), z + dz);
                action = addBlock(target);
                if (action.cancel)
                    return;
            }
        }else {
            int dx = Util.offsetsXForSide[hitSide];
            int dz = Util.offsetsZForSide[hitSide];
            for (int i = 0; i < config.maxDistance; i++) {
                BlockData target = new BlockData(x + (dx * i), y, z + (dz * i));
                BlockAddAction action = addBlock(target);
                if (action.cancel)
                    return;

                target = new BlockData(x + (dx * i), y + 1, z + (dz * i));
                action = addBlock(target);
                if (action.cancel)
                    return;
            }
        }
    }
}
