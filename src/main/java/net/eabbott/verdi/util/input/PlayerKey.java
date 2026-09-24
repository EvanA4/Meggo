package net.eabbott.verdi.util.input;

public enum PlayerKey {
    HOTBAR_1("key.hotbar.1"),
    HOTBAR_2("key.hotbar.2"),
    HOTBAR_3("key.hotbar.3"),
    HOTBAR_4("key.hotbar.4"),
    HOTBAR_5("key.hotbar.5"),
    HOTBAR_6("key.hotbar.6"),
    HOTBAR_7("key.hotbar.7"),
    HOTBAR_8("key.hotbar.8"),
    HOTBAR_9("key.hotbar.9"),
    FORWARD("key.forward"),
    LEFT("key.left"),
    BACK("key.back"),
    RIGHT("key.right"),
    JUMP("key.jump"),
    SNEAK("key.sneak"),
    SPRINT("key.sprint"),
    INVENTORY("key.inventory"),
    SWAP_OFFHAND("key.swapOffhand"),
    DROP("key.drop"),
    USE("key.use"),
    ATTACK("key.attack"),
    PICK_ITEM("key.pickItem"),
    CHAT("key.chat"),
    PLAYER_LIST("key.playerlist"),
    COMMAND("key.command"),
    FRIENDS("key.friends"),
    SOCIAL_INTERACTIONS("key.socialInteractions"),
    SCREENSHOT("key.screenshot"),
    TOGGLE_PERSPECTIVE("key.togglePerspective"),
    SMOOTH_CAMERA("key.smoothCamera"),
    FULLSCREEN("key.fullscreen"),
    ADVANCEMENTS("key.advancements"),
    QUICK_ACTIONS("key.quickActions"),
    TOGGLE_GUI("key.toggleGui"),
    TOGGLE_SPECTATOR_SHADER_EFFECTS("key.toggleSpectatorShaderEffects"),
    SAVE_TOOLBAR_ACTIVATOR("key.saveToolbarActivator"),
    LOAD_TOOLBAR_ACTIVATOR("key.loadToolbarActivator"),
    SPECTATOR_OUTLINES("key.spectatorOutlines"),
    SPECTATOR_HOTBAR("key.spectatorHotbar"),
    DEBUG_OVERLAY("key.debug.overlay"),
    DEBUG_MODIFIER("key.debug.modifier"),
    DEBUG_CRASH("key.debug.crash"),
    DEBUG_RELOAD_CHUNK("key.debug.reloadChunk"),
    DEBUG_SHOW_HITBOXES("key.debug.showHitboxes"),
    DEBUG_CLEAR_CHAT("key.debug.clearChat"),
    DEBUG_SHOW_CHUNK_BORDERS("key.debug.showChunkBorders"),
    DEBUG_SHOW_ADVANCED_TOOLTIPS("key.debug.showAdvancedTooltips"),
    DEBUG_COPY_RECREATE_COMMAND("key.debug.copyRecreateCommand"),
    DEBUG_SPECTATE("key.debug.spectate"),
    DEBUG_SWITCH_GAME_MODE("key.debug.switchGameMode"),
    DEBUG_DEBUG_OPTIONS("key.debug.debugOptions"),
    DEBUG_FOCUS_PAUSE("key.debug.focusPause"),
    DEBUG_DUMP_DYNAMIC_TEXTURES("key.debug.dumpDynamicTextures"),
    DEBUG_RELOAD_RESOURCE_PACKS("key.debug.reloadResourcePacks"),
    DEBUG_PROFILING("key.debug.profiling"),
    DEBUG_COPY_LOCATION("key.debug.copyLocation"),
    DEBUG_DUMP_VERSION("key.debug.dumpVersion"),
    DEBUG_PROFILING_CHART("key.debug.profilingChart"),
    DEBUG_FPS_CHARTS("key.debug.fpsCharts"),
    DEBUG_NETWORK_CHARTS("key.debug.networkCharts"),
    DEBUG_LIGHTMAP_TEXTURE("key.debug.lightmapTexture"),
    ;

    private final String value;

    PlayerKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}