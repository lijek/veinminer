package pl.lijek.veinminer;

import net.minecraft.level.Level;
import net.minecraft.util.maths.Vec3i;
import net.modificationstation.stationapi.api.registry.Identifier;
import pl.lijek.veinminer.shapes.*;
import uk.co.benjiweber.expressions.function.QuadFunction;

import java.util.Locale;

public enum VeinMinerMode {
    NORMAL(NormalShape::new, NormalShape.class),
    TUNNEL1X1(Tunnel1x1::new, Tunnel1x1.class),
    TUNNEL1X2(Tunnel1x2::new, Tunnel1x2.class),
    TUNNEL2X2(Tunnel2x2::new, Tunnel2x2.class),
    TUNNEL3X3(Tunnel3x3::new, Tunnel3x3.class),
    STAIRS_DOWN(StairsDown::new, StairsDown.class),
    STAIRS_UP(StairsUp::new, StairsUp.class);

    private final QuadFunction<Level, Vec3i, Integer, Integer, Shape> shapeCreator;
    public final Class<? extends Shape> shapeClass;

    VeinMinerMode(){
        shapeCreator = NormalShape::new;
        this.shapeClass = Shape.class;
    }

    VeinMinerMode(QuadFunction<Level, Vec3i, Integer, Integer, Shape> shapeCreator){
        this.shapeCreator = shapeCreator;
        this.shapeClass = Shape.class;
    }

    VeinMinerMode(QuadFunction<Level, Vec3i, Integer, Integer, Shape> shapeCreator, Class<? extends Shape> shapeClass){
        this.shapeCreator = shapeCreator;
        this.shapeClass = shapeClass;
    }

    public Shape getShape(Level level, Vec3i origin, int hitSide, int playerFacing){
        return shapeCreator.apply(level, origin, hitSide, playerFacing);
    }

    public int toInt(){
        return this.ordinal();
    }

    public String getTranslationKey(){
        return "mode." + Identifier.of(VeinMiner.MODID, this.name()).toString().toLowerCase(Locale.ROOT);
    }

    public VeinMinerMode getPrevious(){
        int current = toInt();
        if(current - 1 < 0)
            return VeinMinerMode.values()[current + VeinMinerMode.values().length - 1];
        else
            return VeinMinerMode.values()[current - 1];
    }

    public VeinMinerMode getNext(){
        int current = toInt();
        if(current + 1 >= VeinMinerMode.values().length)
            return VeinMinerMode.values()[current - VeinMinerMode.values().length + 1];
        else
            return VeinMinerMode.values()[current + 1];
    }
}
