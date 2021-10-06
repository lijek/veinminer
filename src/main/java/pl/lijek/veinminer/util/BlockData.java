package pl.lijek.veinminer.util;

import net.minecraft.util.maths.Vec3i;

public class BlockData extends Vec3i {
    public int id, meta;

    public BlockData(int x, int y, int z) {
        super(x, y, z);
        id = 0;
        meta = 0;
    }

    public BlockData(Vec3i xyz) {
        super(xyz.x, xyz.y, xyz.z);
        id = 0;
        meta = 0;
    }

    public BlockData(int x, int y, int z, int id, int meta) {
        super(x, y, z);
        this.id = id;
        this.meta = meta;
    }

    public BlockData(Vec3i xyz, int id, int meta) {
        super(xyz.x, xyz.y, xyz.z);
        this.id = id;
        this.meta = meta;
    }
}
