// SPDX-FileCopyrightText: © 2022-2026 Greg Christiana <maxuser@minescript.net>
// SPDX-License-Identifier: GPL-3.0-only

package net.eabbott.meggo.mixin;

import net.eabbott.meggo.MeggoManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.List;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
  @Shadow
  public abstract double xpos();

  @Shadow
  public abstract double ypos();

  @Inject(
      at = @At("HEAD"),
      method = "onButton(JLnet/minecraft/client/input/MouseButtonInfo;I)V",
      cancellable = true)
  private void onButton(long window, MouseButtonInfo mouseButtonInfo, int action, CallbackInfo ci) {
    Minecraft mc = Minecraft.getInstance();
    boolean isScreenOpen = mc.gui.screen() != null;
    if (MeggoManager.getIsInputLocked() && !isScreenOpen) ci.cancel();

    int button = mouseButtonInfo.button();
    int modifiers = mouseButtonInfo.modifiers();
    MeggoManager.onMouseClick(button, action, modifiers, this.xpos(), this.ypos());
  }

  @Inject(
      at = @At("HEAD"),
      method = "onScroll(JDD)V",
      cancellable = true)
  private void onScroll(final long handle, final double xoffset, final double yoffset, CallbackInfo ci) {
    Minecraft mc = Minecraft.getInstance();
    boolean isScreenOpen = mc.gui.screen() != null;
    if (MeggoManager.getIsInputLocked() && !isScreenOpen) ci.cancel();
  }

  @Inject(
      at = @At("HEAD"),
      method = "onDrop(JLjava/util/List;I)V",
      cancellable = true)
  private void onDrop(final long handle, final List<Path> files, final int failedCount, CallbackInfo ci) {
    Minecraft mc = Minecraft.getInstance();
    boolean isScreenOpen = mc.gui.screen() != null;
    if (MeggoManager.getIsInputLocked() && !isScreenOpen) ci.cancel();
  }

  @Inject(
      at = @At("HEAD"),
      method = "onMove(JDD)V",
      cancellable = true)
  private void onMove(final long handle, final double xpos, final double ypos, CallbackInfo ci) {
    Minecraft mc = Minecraft.getInstance();
    boolean isScreenOpen = mc.gui.screen() != null;
    if (MeggoManager.getIsInputLocked() && !isScreenOpen) ci.cancel();
  }

}
