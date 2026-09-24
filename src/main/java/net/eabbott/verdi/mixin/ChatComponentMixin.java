// SPDX-FileCopyrightText: © 2022-2026 Greg Christiana <maxuser@minescript.net>
// SPDX-License-Identifier: GPL-3.0-only

package net.eabbott.verdi.mixin;

import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.eabbott.verdi.MeggoManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
  @Inject(
      at = @At("HEAD"),
      method = "addClientSystemMessage(Lnet/minecraft/network/chat/Component;)V",
      cancellable = true)
  private void addClientSystemMessage(Component message, CallbackInfo ci) {
    if (MeggoManager.onClientChatReceived(message)) {
      ci.cancel();
    }
  }

  @Inject(
      at = @At("HEAD"),
      method =
          "addPlayerMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;)V",
      cancellable = true)
  private void addPlayerMessage(
      Component message, MessageSignature signature, GuiMessageTag tag, CallbackInfo ci) {
    if (MeggoManager.onClientChatReceived(message)) {
      ci.cancel();
    }
  }
}
