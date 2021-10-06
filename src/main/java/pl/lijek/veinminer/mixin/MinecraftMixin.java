package pl.lijek.veinminer.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lijek.veinminer.VeinMinerClient;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow private static Minecraft instance;

    @Inject(method = "init", at = @At("RETURN"))
    private void postMcInit(CallbackInfo ci){
        VeinMinerClient.postMcInit();
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(I)V"))
    private void switchMode(CallbackInfo ci){
        VeinMinerClient.changeMode(Mouse.getEventDWheel());
    }

    @Inject(method = "showLevelProgress", at = @At("RETURN"))
    private void switchLevel(Level level, String message, PlayerBase arg1, CallbackInfo ci){
        VeinMinerClient.switchLevel(instance);
    }
}
