package pl.lijek.veinminer.util;

import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.Vec3i;

import static org.lwjgl.opengl.GL11.*;

public class Util {
    public static final int[] offsetsXForSide = {0, 0, 0, 0, 1, -1};
    public static final int[] offsetsYForSide = {1, -1, 0, 0, 0, 0};
    public static final int[] offsetsZForSide = {0, 0, 1, -1, 0, 0};

    public static final int[] directionXMultiplier = {0, -1, 0, 1};
    public static final int[] directionZMultiplier = {1, 0, -1, 0};

    public static int getSphericalDistance(Vec3i a, Vec3i b)
    {
        return (int) Math.round(Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.z - b.z, 2) + Math.pow(a.y - b.y, 2)));
    }

    public static int getDistance(Vec3i a, Vec3i b)
    {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y) + Math.abs(a.z - b.z);
    }

    public static String translate(String key){
        return TranslationStorage.getInstance().translate(key);
    }

    public static void drawCuboid(int glMode, Box box){
        //top
        glBegin(glMode);
        glVertex3d(box.minX, box.minY, box.minZ);
        glVertex3d(box.maxX, box.minY, box.minZ);
        glVertex3d(box.maxX, box.minY, box.maxZ);
        glVertex3d(box.minX, box.minY, box.maxZ);
        glEnd();
        //bottom
        glBegin(glMode);
        glVertex3d(box.minX, box.maxY, box.minZ);
        glVertex3d(box.maxX, box.maxY, box.minZ);
        glVertex3d(box.maxX, box.maxY, box.maxZ);
        glVertex3d(box.minX, box.maxY, box.maxZ);
        glEnd();
        //front
        glBegin(glMode);
        glVertex3d(box.minX, box.minY, box.minZ);
        glVertex3d(box.maxX, box.minY, box.minZ);
        glVertex3d(box.maxX, box.maxY, box.minZ);
        glVertex3d(box.minX, box.maxY, box.minZ);
        glEnd();
        //back
        glBegin(glMode);
        glVertex3d(box.minX, box.minY, box.maxZ);
        glVertex3d(box.maxX, box.minY, box.maxZ);
        glVertex3d(box.maxX, box.maxY, box.maxZ);
        glVertex3d(box.minX, box.maxY, box.maxZ);
        glEnd();
        //right
        glBegin(glMode);
        glVertex3d(box.minX, box.minY, box.minZ);
        glVertex3d(box.minX, box.maxY, box.minZ);
        glVertex3d(box.minX, box.maxY, box.maxZ);
        glVertex3d(box.minX, box.minY, box.maxZ);
        glEnd();
        //left
        glBegin(glMode);
        glVertex3d(box.maxX, box.minY, box.minZ);
        glVertex3d(box.maxX, box.maxY, box.minZ);
        glVertex3d(box.maxX, box.maxY, box.maxZ);
        glVertex3d(box.maxX, box.minY, box.maxZ);
        glEnd();
    }
}
