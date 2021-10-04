package pl.lijek.veinminer;

import net.minecraft.util.maths.Vec3i;

public class MineFunctions {

    public static void mineNormal(int x, int y, int z, BlockBreaker breaker){
        int d = 1;
        for (int dx = -d; dx <= d; dx++) {
            for (int dy = -d; dy <= d; dy++) {
                for (int dz = -d; dz <= d; dz++) {
                    if(dx == 0 && dy == 0 && dz == 0)
                        continue;

                    Vec3i target = new Vec3i(x + dx, y + dy, z + dz);
                    BlockBreakAction action = breaker.breakBlock(target, Util::getSphericalDistance);
                    if(action.skipPass)
                        continue;
                    else if(action.cancel)
                        return;
                    mineNormal(target.x, target.y, target.z, breaker);
                }
            }
        }
    }

    public static void mineCubic(int x, int y, int z, BlockBreaker breaker){
        int d = 1;
        for (int dx = -d; dx <= d; dx++) {
            for (int dy = -d; dy <= d; dy++) {
                for (int dz = -d; dz <= d; dz++) {
                    if(dx == 0 && dy == 0 && dz == 0)
                        continue;

                    Vec3i target = new Vec3i(x + dx, y + dy, z + dz);
                    BlockBreakAction action = breaker.breakBlock(target, Util::getDistance);
                    if(action.skipPass)
                        continue;
                    else if(action.cancel)
                        return;

                    mineCubic(target.x, target.y, target.z, breaker);
                }
            }
        }
    }

    public static void mineTunnel1x1(int x, int y, int z, BlockBreaker breaker){
        int dx = Util.offsetsXForSide[breaker.side];
        int dy = Util.offsetsYForSide[breaker.side];
        int dz = Util.offsetsZForSide[breaker.side];

        for (int i = 0; i < breaker.maxDistance; i++) {
            Vec3i target = new Vec3i(x + (dx * i), y + (dy * i), z + (dz * i));
            BlockBreakAction action = breaker.breakBlock(target, Util::getDistance);
            if(action.cancel)
                return;
        }
    }

    public static void mineTunnel1x2(int x, int y, int z, BlockBreaker breaker){
        int dx = Util.offsetsXForSide[breaker.side];
        int dy = Util.offsetsYForSide[breaker.side];
        int dz = Util.offsetsZForSide[breaker.side];

        for (int i = 0; i < breaker.maxDistance; i++) {
            Vec3i target = new Vec3i(x + (dx * i), y + (dy * i), z + (dz * i));
            BlockBreakAction action = breaker.breakBlock(target, Util::getDistance);
            if(action.cancel)
                return;

            target = new Vec3i(x + (dx * i), y + (dy * i) + 1, z + (dz * i));
            action = breaker.breakBlock(target, Util::getDistance);
            if(action.cancel)
                return;
        }
    }
}
