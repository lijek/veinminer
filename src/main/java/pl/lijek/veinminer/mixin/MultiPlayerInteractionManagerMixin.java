package pl.lijek.veinminer.mixin;

import net.minecraft.block.BlockBase;
import net.minecraft.client.MultiPlayerClientInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static pl.lijek.veinminer.VeinMinerClient.*;

@Mixin(MultiPlayerClientInteractionManager.class)
public class MultiPlayerInteractionManagerMixin {

    @Shadow private float field_2611;

    @Shadow private float field_2612;

    @Shadow private float field_2613;

    @Shadow private int field_2614;

    @Inject(method = "method_1707", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/packet/AbstractPacket;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void beforeSendBlockRemoved1(int x, int y, int z, int side, CallbackInfo ci){
        int blockID = level.getTileId(x, y, z);
        if(blockID > 0 && BlockBase.BY_ID[blockID].getHardness(localPlayer) >= 1.0F)
            if(veinMiningKeyPressed){
                sendVeinMinePacket(x, y, z, side);
                ci.cancel();
            }
    }

    @Inject(method = "method_1721", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/packet/AbstractPacket;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void beforeSendBlockRemoved(int x, int y, int z, int side, CallbackInfo ci){
        if(veinMiningKeyPressed){
            sendVeinMinePacket(x, y, z, side);
            this.field_2611 = 0.0F;
            this.field_2612 = 0.0F;
            this.field_2613 = 0.0F;
            this.field_2614 = 5;
            ci.cancel();
        }
    }
}
