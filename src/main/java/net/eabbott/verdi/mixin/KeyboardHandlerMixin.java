// SPDX-FileCopyrightText: © 2022-2026 Greg Christiana <maxuser@minescript.net>
// SPDX-License-Identifier: GPL-3.0-only

package net.eabbott.verdi.mixin;

import net.eabbott.verdi.MeggoManager;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
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
    Minecraft mc = Minecraft.getInstance();
    boolean isEscapeKey = event.isEscape();
    boolean isF5 = mc.options.keyTogglePerspective.matches(event);
    boolean isChatOpened = mc.gui.screen() instanceof ChatScreen;
    if (MeggoManager.getIsInputLocked() && !isChatOpened && !isEscapeKey && !isF5) ci.cancel();

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
