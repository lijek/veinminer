package pl.lijek.veinminer.mixin;

import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lijek.veinminer.VeinMinerClient;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Inject(method = "scrollInHotbar", at = @At("HEAD"), cancellable = true)
    private void switchMode(int mouseWheelDelta, CallbackInfo ci){
        if(VeinMinerClient.changeMode(mouseWheelDelta))
            ci.cancel();
    }
}
