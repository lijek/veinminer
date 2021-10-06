package pl.lijek.veinminer;

import net.minecraft.level.Level;
import net.minecraft.util.maths.Vec3i;
import net.modificationstation.stationapi.api.registry.Identifier;
import pl.lijek.veinminer.shapes.NormalShape;
import pl.lijek.veinminer.shapes.Shape;

import java.util.Locale;
import java.util.function.BiFunction;

public enum VeinMinerMode {
    NORMAL(NormalShape::new), TUNNEL1X1, TUNNEL1X2,
    TUNNEL2X2, TUNNEL3X3, STAIRS_DOWN, STAIRS_UP;

    private final BiFunction<Level, Vec3i, Shape> shapeCreator;

    VeinMinerMode(){
        shapeCreator = NormalShape::new;
    }

    VeinMinerMode(BiFunction<Level, Vec3i, Shape> shapeCreator){
        this.shapeCreator = shapeCreator;
    }

    public Shape getShape(Level level, Vec3i origin){
        return shapeCreator.apply(level, origin);
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
