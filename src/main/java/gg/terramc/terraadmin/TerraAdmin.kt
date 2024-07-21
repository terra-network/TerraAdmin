package gg.terramc.terraadmin

import gg.terramc.terraadmin.commands.FlyCommand
import gg.terramc.terraadmin.commands.SeenCommand
import gg.terramc.terraadmin.commands.TopCommand
import gg.terramc.terraadmin.commands.bench.EnderchestCommand
import gg.terramc.terraadmin.commands.bench.GrindstoneCommand
import gg.terramc.terraadmin.commands.bench.WorkbenchCommand
import gg.terramc.terraadmin.commands.registerGamemodes
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minecraft.server.MinecraftServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TerraAdmin : ModInitializer {

    companion object {
        val MOD_ID = TerraAdmin::class.java.simpleName.lowercase()
        val PREFIX = Component.text("ᴛᴇʀʀᴀ").color(TextColor.color(0x6a994e)).append(Component.text(" • ").decoration(TextDecoration.BOLD, true).color(NamedTextColor.DARK_GRAY))
        val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
        var currentServer: MinecraftServer? = null
    }
    override fun onInitialize() {
        LOGGER.info("[INIT] TerraAdmin 1.0-SNAPSHOT")
        registerGamemodes()
        FlyCommand
        TopCommand
        WorkbenchCommand
        GrindstoneCommand
        EnderchestCommand
        SeenCommand

        ServerLifecycleEvents.SERVER_STARTING.register{ server ->
            currentServer = server;
        }

        ServerLifecycleEvents.SERVER_STOPPING.register {
            currentServer = null
        }
    }

}

