// SPDX-FileCopyrightText: © 2022-2026 Greg Christiana <maxuser@minescript.net>
// SPDX-License-Identifier: GPL-3.0-only

package net.eabbott.meggo.mixin;

import net.eabbott.meggo.MeggoManager;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
  private static int KEY_ACTION_DOWN = 1;
  private static int ENTER_KEY = 257;
  private static int secondaryEnterKeyCode = 335;

  @Inject(
      at = @At("HEAD"),
      method = "keyPress(JILnet/minecraft/client/input/KeyEvent;)V",
      cancellable = true)
  private void keyPress(long window, int action, KeyEvent event, CallbackInfo ci) {
    int key = event.key();
    int scanCode = event.scancode();
    int modifiers = event.modifiers();
    MeggoManager.onKeyboardEvent(key, scanCode, action, modifiers);
    var screen = Minecraft.getInstance().gui.screen();
    if (screen == null) {
      MeggoManager.onKeyInput(key);
    } else if (
        (key == ENTER_KEY || key == secondaryEnterKeyCode)
        && action == KEY_ACTION_DOWN
        && MeggoManager.onKeyboardKeyPressed(screen, key)) {
      ci.cancel();
    }
  }
}
