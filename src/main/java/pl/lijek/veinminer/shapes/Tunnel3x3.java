package pl.lijek.veinminer.shapes;

import net.minecraft.level.Level;
import net.minecraft.util.maths.Vec3i;
import pl.lijek.veinminer.util.BlockAddAction;
import pl.lijek.veinminer.util.BlockData;
import pl.lijek.veinminer.util.Util;

import static pl.lijek.veinminer.VeinMiner.maxDistance;

public class Tunnel3x3 extends Shape {
    public Tunnel3x3(Level level, Vec3i originVec3i, int hitSide, int playerFacing) {
        super(level, originVec3i, hitSide, playerFacing);
    }

    @Override
    public void selectBlocks(int x, int y, int z) {

        if(hitSide < 2) {
            int dx = Util.directionXMultiplier[playerFacing];
            int dy = Util.offsetsYForSide[hitSide];
            int dz = Util.directionZMultiplier[playerFacing];
            for (int i = 0; i < maxDistance; i++) {
                //środek
                BlockData target = new BlockData(x, y + (dy * i), z);
                BlockAddAction action = addBlock(target);
                if (action.cancel)
                    return;

                //środek góra
                target = new BlockData(x + dx, y + (dy * i), z + dz);
                action = addBlock(target);
                if (action.cancel)
                    return;

                //środek dół
                target = new BlockData(x - dx, y + (dy * i), z - dz);
                action = addBlock(target);
                if (action.cancel)
                    return;

                //prawo
                target = new BlockData(x + dz, y + (dy * i), z + dx);
                action = addBlock(target);
                if (action.cancel)
                    return;

                //prawo góra
                target = new BlockData(x + dx + dz, y + (dy * i), z + dz + dx);
                action = addBlock(target);
                if (action.cancel)
                    return;

                //prawo dół
                target = new BlockData(x - dx + dz, y + (dy * i), z - dz + dx);
                action = addBlock(target);
                if (action.cancel)
                    return;

                //lewo
                target = new BlockData(x - dz, y + (dy * i), z - dx);
                action = addBlock(target);
                if (action.cancel)
                    return;

                //lewo góra
                target = new BlockData(x + dx - dz, y + (dy * i), z + dz - dx);
                action = addBlock(target);
                if (action.cancel)
                    return;

                //lewo dół
                target = new BlockData(x - dx - dz, y + (dy * i), z - dz - dx);
                action = addBlock(target);
                if (action.cancel)
                    return;
            }
        }else {
            int dx = Util.offsetsXForSide[hitSide];
            int dz = Util.offsetsZForSide[hitSide];
            for (int i = 0; i < maxDistance; i++) {
                //środek
                BlockData target = new BlockData(x + (dx * i), y, z + (dz * i));
                BlockAddAction action = addBlock(target);
                if (action.cancel)
                    return;

                //środek góra
                target = new BlockData(x + (dx * i), y + 1, z + (dz * i));
                action = addBlock(target);
                if (action.cancel)
                    return;

                //środek dół
                target = new BlockData(x + (dx * i), y - 1, z + (dz * i));
                action = addBlock(target);
                if (action.cancel)
                    return;

                //lewo
                target = new BlockData(x + (dx * i) - dz, y, z + (dz * i) - dx);
                action = addBlock(target);
                if (action.cancel)
                    return;

                //lewo góra
                target = new BlockData(x + (dx * i) - dz, y + 1, z + (dz * i) - dx);
                action = addBlock(target);
                if (action.cancel)
                    return;

                //lewo dół
                target = new BlockData(x + (dx * i) - dz, y - 1, z + (dz * i) - dx);
                action = addBlock(target);
                if (action.cancel)
                    return;

                //prawo
                target = new BlockData(x + (dx * i) + dz, y, z + (dz * i) + dx);
                action = addBlock(target);
                if (action.cancel)
                    return;

                //prawo góra
                target = new BlockData(x + (dx * i) + dz, y + 1, z + (dz * i) + dx);
                action = addBlock(target);
                if (action.cancel)
                    return;

                //prawo dół
                target = new BlockData(x + (dx * i) + dz, y - 1, z + (dz * i) + dx);
                action = addBlock(target);
                if (action.cancel)
                    return;
            }
        }
    }
}
