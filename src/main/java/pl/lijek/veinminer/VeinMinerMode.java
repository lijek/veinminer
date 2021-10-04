package pl.lijek.veinminer;

import net.modificationstation.stationapi.api.registry.Identifier;
import uk.co.benjiweber.expressions.function.QuadConsumer;

import java.util.Locale;

public enum VeinMinerMode {
    NORMAL(MineFunctions::mineNormal), CUBIC(MineFunctions::mineCubic), TUNNEL1X1(MineFunctions::mineTunnel1x1), TUNNEL1X2(MineFunctions::mineTunnel1x2),
    TUNNEL2X2, TUNNEL3X3, STAIRS_DOWN, STAIRS_UP;

    QuadConsumer<Integer, Integer, Integer, BlockBreaker> mineConsumer;

    VeinMinerMode(QuadConsumer<Integer, Integer, Integer, BlockBreaker> lambda){
        mineConsumer = lambda;
    }

    VeinMinerMode(){
        mineConsumer = (x, y, z, breaker) -> {};
    }

    public void mine(int x, int y, int z, BlockBreaker breaker){
        mineConsumer.accept(x, y, z, breaker);
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
