package pl.lijek.veinminer.shapes;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Log;
import net.minecraft.block.Plant;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.Vec3f;
import net.minecraft.util.maths.Vec3i;
import org.lwjgl.opengl.GL11;
import pl.lijek.veinminer.util.BlockAddAction;
import pl.lijek.veinminer.util.BlockData;
import pl.lijek.veinminer.util.BlockMatcher;
import pl.lijek.veinminer.util.Util;

import java.util.ArrayList;
import java.util.List;

import static pl.lijek.veinminer.VeinMiner.*;
import static pl.lijek.veinminer.util.BlockAddAction.*;

public abstract class Shape {
    public Level level;
    public BlockData origin;
    public BlockBase originBlock;
    public List<BlockData> blocks = new ArrayList<>();
    protected List<BlockData> scannedBlocks = new ArrayList<>();
    public int hitSide;
    public int playerFacing;

    private static boolean crappyDebug = false;

    public Shape(Level level, Vec3i originVec3i, int hitSide, int playerFacing){
        this.playerFacing = playerFacing;
        this.level = level;
        changeOrigin(originVec3i.x, originVec3i.y, originVec3i.z, hitSide);
        blocks.add(origin);
        selectBlocks(origin.x, origin.y, origin.z);
    }

    protected int getDistance(Vec3i target) {
        return Util.getSphericalDistance(origin, target);
    }

    public abstract void selectBlocks(int x, int y, int z);

    protected BlockAddAction addBlock(BlockData target){
        if(blocks.size() >= config.maxBlocksToMine) {
            if(crappyDebug)
                LOGGER.info("CANCELLED: MAX BLOCK COUNT EXCEEDED");
            return CANCEL;
        }

        if(scannedBlocks.contains(target)) {
            if(crappyDebug)
                LOGGER.info("SKIPPED: THIS BLOCK WAS SCANNED");
            return SKIP;
        }

        if(origin.equals(target)) {
            if(crappyDebug)
                LOGGER.info("SKIPPED: THIS BLOCK EQUALS TARGET");
            return SKIP;
        }

        if (!BlockMatcher.ID_META.matchBlocks(this, target)) {
            if(crappyDebug)
                LOGGER.info("SKIPPED: THIS BLOCK DOESN'T MATCH ORIGIN");
            return SKIP;
        }

        if (!(config.maxDistance == -1 || getDistance(target) <= config.maxDistance)) {
            if(crappyDebug)
                LOGGER.info("SKIPPED: THIS BLOCK IS OUT OF maxDistance");
            return SKIP;
        }

        if(!blocks.contains(target))
            blocks.add(target);
        scannedBlocks.add(target);

        return PASS;
    }

    public void changeOrigin(int x, int y, int z, int hitSide){
        this.origin = new BlockData(x, y, z, level.getTileId(x, y, z), level.getTileMeta(x, y, z));
        this.originBlock = BlockBase.BY_ID[origin.id];
        this.hitSide = hitSide;
    }

    public void changeOrigin(BlockData origin){
        this.origin = origin;
    }

    protected int getConnectionDistance(){
        if(originBlock instanceof Plant)
            return 3;
        else if(originBlock instanceof Log)
            return 2;
        return 1;
    }

    public void render(double fixX, double fixY, double fixZ){
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        for(Vec3i block : blocks){
            Box box = Box.create(block.x, block.y, block.z, block.x + 1.0D, block.y + 1.0D, block.z + 1.0D).method_98(-fixX, -fixY, -fixZ);
            Util.drawCuboid(GL11.GL_LINE_LOOP, box);
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public void reset(int x, int y, int z, int hitSide){
        blocks = new ArrayList<>();
        scannedBlocks = new ArrayList<>();
        changeOrigin(x, y, z, hitSide);
        blocks.add(origin);
        selectBlocks(x, y, z);
    }
}
