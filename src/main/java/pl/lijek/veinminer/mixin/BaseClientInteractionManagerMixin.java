package pl.lijek.veinminer.mixin;

import net.minecraft.client.BaseClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.packet.PacketHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static pl.lijek.veinminer.VeinMiner.breakWithVeinMinePacket;
import static pl.lijek.veinminer.VeinMinerClient.veinMinerMode;
import static pl.lijek.veinminer.VeinMinerClient.veinMiningKeyPressed;

@Mixin(BaseClientInteractionManager.class)
public abstract class BaseClientInteractionManagerMixin {

    @Shadow @Final protected Minecraft minecraft;

    @Unique
    private int blockID = 0;

    @Unique
    private int blockMeta = 0;

    @Inject(method = "method_1716", at = @At("HEAD"), cancellable = true)
    private void beforeBlockRemoved(int x, int y, int z, int i1, CallbackInfoReturnable<Boolean> cir){
        blockID = minecraft.level.getTileId(x, y, z);
        blockMeta = minecraft.level.getTileMeta(x, y, z);
        if(veinMiningKeyPressed){
            Message message = new Message(breakWithVeinMinePacket);
            message.ints = new int[]{x, y, z, i1, blockID, blockMeta, veinMinerMode.toInt()};
            PacketHelper.send(message);
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
