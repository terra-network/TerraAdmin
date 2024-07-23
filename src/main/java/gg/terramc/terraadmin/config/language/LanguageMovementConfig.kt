package gg.terramc.terraadmin.config.language

import gg.terramc.terraadmin.config.LanguageConfig
import gg.terramc.terraadmin.config.LanguageConfigData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minecraft.server.network.ServerPlayerEntity

class LanguageMovementConfig(private val mm: MiniMessage, private val data: LanguageConfigData.MovementCommands) {

    val nowFlying = mm.deserialize(data.nowFlying)
    val noLongerFlying = mm.deserialize(data.noLongerFlying)
    val playerNowFlying: (player: ServerPlayerEntity) -> Component = { player ->
        LanguageConfig.insertPlayer(data.playerNowFlying, player)
    }
    val playerNoLongerFlying: (player: ServerPlayerEntity) -> Component = { player ->
        LanguageConfig.insertPlayer(data.playerNoLongerFlying, player)
    }
    val teleportedTop = mm.deserialize(data.teleportedTop)
    val noSafeTop = mm.deserialize(data.noSafeTop)
    val teleportedPlayerHere: (player: ServerPlayerEntity) -> Component = { player ->
        LanguageConfig.insertPlayer(data.teleportedPlayerHere, player)
    }

}