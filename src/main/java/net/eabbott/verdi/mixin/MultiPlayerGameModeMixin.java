// SPDX-FileCopyrightText: © 2022-2026 Greg Christiana <maxuser@minescript.net>
// SPDX-License-Identifier: GPL-3.0-only

package net.eabbott.verdi.mixin;

import net.eabbott.verdi.VerdiShell;
import net.eabbott.verdi.util.VerdiChat;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
  @Inject(
      at = @At("HEAD"),
      method =
          "handleContainerInput(IIILnet/minecraft/world/inventory/ContainerInput;Lnet/minecraft/world/entity/player/Player;)V",
      cancellable = true)
  public void handleContainerInput(
        final int containerId, final int slotNum, final int buttonNum, final ContainerInput containerInput, final Player player, CallbackInfo ci
  ) {
      VerdiChat.send(
          "Calling handleContainerInput %d %d %d %s %s", containerId, slotNum, buttonNum, containerInput, player.getPlainTextName()
      );
  }
}
