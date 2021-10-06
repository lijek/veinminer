package pl.lijek.veinminer.shapes;

import net.minecraft.level.Level;
import net.minecraft.util.maths.Vec3i;
import pl.lijek.veinminer.util.BlockAddAction;
import pl.lijek.veinminer.util.BlockData;

public class NormalShape extends Shape {

    public NormalShape(Level level, Vec3i origin) {
        super(level, origin);
    }

    @Override
    public void selectBlocks(int x, int y, int z) {
        int d = getConnectionDistance();
        for (int dx = -d; dx <= d; dx++) {
            for (int dy = -d; dy <= d; dy++) {
                for (int dz = -d; dz <= d; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0)
                        continue;

                    BlockData target = new BlockData(x + dx, y + dy, z + dz);
                    BlockAddAction action = addBlock(target);
                    if(action.skipPass)
                        continue;
                    else if(action.cancel)
                        return;
                    selectBlocks(target.x, target.y, target.z);
                }
            }
        }
    }
}
