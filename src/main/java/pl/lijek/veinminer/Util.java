package pl.lijek.veinminer;

import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.util.maths.Vec3i;

public class Util {
    public static final int[] offsetsXForSide = {0, 0, 0, 0, 1, -1};
    public static final int[] offsetsYForSide = {1, -1, 0, 0, 0, 0};
    public static final int[] offsetsZForSide = {0, 0, 1, -1, 0, 0};

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
}
