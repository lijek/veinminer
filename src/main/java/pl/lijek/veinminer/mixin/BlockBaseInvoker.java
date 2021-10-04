package pl.lijek.veinminer.mixin;

import net.minecraft.block.BlockBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockBase.class)
public interface BlockBaseInvoker {
    @Invoker("droppedMeta")
    int invokeDroppedMeta(int blockMeta);
}
