package net.eabbott.meggo.mixin;

import net.eabbott.meggo.Meggo;
import net.eabbott.meggo.MeggoManager;
import net.eabbott.meggo.util.input.PlayerMover;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Inject(
            method = "setScreen(Lnet/minecraft/client/gui/screens/Screen;)V",
            at = @At("HEAD")
    )
    private void setScreen(Screen screen, CallbackInfo ci) {
        if (screen instanceof PauseScreen) {
            MeggoManager.interruptAllTasks();
        } else if (screen instanceof AbstractContainerScreen<?>) {
            PlayerMover.setCurrentContainer(((AbstractContainerScreen<?>) screen).getMenu().containerId);
        }
    }
}