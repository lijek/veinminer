package pl.lijek.veinminer.mixin;

import net.minecraft.client.BaseClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.SinglePlayerClientInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static pl.lijek.veinminer.VeinMinerClient.*;

@Mixin(SinglePlayerClientInteractionManager.class)
public abstract class SinglePlayerInteractionManagerMixin extends BaseClientInteractionManager {

    public SinglePlayerInteractionManagerMixin(Minecraft minecraft) {
        super(minecraft);
    }

    @Inject(method = "method_1716", at = @At("HEAD"), cancellable = true)
    private void beforeBlockRemoved(int x, int y, int z, int i1, CallbackInfoReturnable<Boolean> cir){
        if(veinMiningKeyPressed){
            sendVeinMinePacket(x, y, z, i1);
            cir.cancel();
        }
    }

    /*@Inject(method = "method_1716", at = @At("RETURN"))
    private void afterBlockRemoved(int x, int y, int z, int i1, CallbackInfoReturnable<Boolean> cir){
        if(cir.getReturnValue() && veinMiningKeyPressed){
            Message message = new Message(breakWithVeinMinePacket);
            message.ints = new int[]{x, y, z, i1, blockID, blockMeta, veinMinerMode.toInt()};
            PacketHelper.send(message);
        }
    }*/
}
