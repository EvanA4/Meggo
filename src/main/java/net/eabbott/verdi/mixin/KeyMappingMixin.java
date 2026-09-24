package net.eabbott.verdi.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.eabbott.verdi.util.input.PlayerMover;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyMapping.class)
public class KeyMappingMixin {
    @Shadow
    @Final
    private String name;

    @Shadow
    protected InputConstants.Key key;

    @Inject(
        method = "<init>(Ljava/lang/String;Lcom/mojang/blaze3d/platform/InputConstants$Type;ILnet/minecraft/client/KeyMapping$Category;I)V",
        at = @At("RETURN")
    )
    public void afterConstructor(final String name, final InputConstants.Type type, final int value, final KeyMapping.Category category, final int order, CallbackInfo ci) {
        PlayerMover.setBinding(name, key);
    }

    @Inject(
        at = @At("HEAD"),
        method = "setKey(Lcom/mojang/blaze3d/platform/InputConstants$Key;)V"
    )
    public void setKey(final InputConstants.Key key, CallbackInfo ci) {
        PlayerMover.setBinding(name, key);
    }
}
