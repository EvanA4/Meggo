package net.eabbott.meggo.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.eabbott.meggo.Meggo;
import net.eabbott.meggo.util.input.PlayerMover;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyMapping.class)
public class KeyMappingMixin {
    @Shadow
    @Final
    private String name;

    @Shadow
    protected InputConstants.Key key;

    @Shadow
    private int clickCount;

    @Inject(
        method = "<init>(Ljava/lang/String;Lcom/mojang/blaze3d/platform/InputConstants$Type;ILnet/minecraft/client/KeyMapping$Category;I)V",
        at = @At("RETURN")
    )
    public void afterConstructor(final String name, final InputConstants.Type type, final int value, final KeyMapping.Category category, final int order, CallbackInfo ci) {
        Meggo.LOGGER.info(String.format("Setting binding %s -> %s", name, key.getName()));
        PlayerMover.setBinding(name, key);
    }

    @Inject(
        at = @At("HEAD"),
        method = "setKey(Lcom/mojang/blaze3d/platform/InputConstants$Key;)V"
    )
    public void setKey(final InputConstants.Key key, CallbackInfo ci) {
        Meggo.LOGGER.info(String.format("Setting binding %s -> %s", name, key.getName()));
        PlayerMover.setBinding(name, key);
    }

    @Inject(
        at = @At("HEAD"),
        method = "consumeClick()Z"
    )
    public void consumeClick(CallbackInfoReturnable<Boolean> ci) {
        if (this.clickCount > 0) {
            Meggo.LOGGER.info(String.format("Consuming click for %s", name));
        }
    }
}
