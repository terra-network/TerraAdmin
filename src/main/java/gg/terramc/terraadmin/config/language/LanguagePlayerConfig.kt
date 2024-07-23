package gg.terramc.terraadmin.config.language

import gg.terramc.terraadmin.config.LanguageConfig
import gg.terramc.terraadmin.config.LanguageConfigData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.GameMode

class LanguagePlayerConfig(private val mm: MiniMessage, private val data: LanguageConfigData.PlayerCommands) {
    var setGamemode: (gameMode: GameMode) -> Component = { gameMode ->
        LanguageConfig.insertGamemode(data.setGamemode, gamemodeToString(gameMode))
    }
    var playerSetGamemode: (gameMode: GameMode, player: ServerPlayerEntity) -> Component = { gameMode, player ->
        LanguageConfig.insertPlayerAndGamemode(data.playerSetGamemode, gamemodeToString(gameMode), player)
    }
    var invalidGamemode: (gameMode: String?) -> Component = { gameMode ->
        LanguageConfig.insertGamemode(data.invalidGamemode, gameMode)
    }

    var healedPlayer: (player: ServerPlayerEntity) -> Component = { player ->
        LanguageConfig.insertPlayer(data.healedPlayer, player)
    }
    var fedPlayer: (player: ServerPlayerEntity) -> Component = { player ->
        LanguageConfig.insertPlayer(data.fedPlayer, player)
    }

    var mutedPlayer: (player: ServerPlayerEntity, duration: String) -> Component = { player, duration ->
        LanguageConfig.insertPlayerAndDuration(data.mutedPlayer, player, duration)
    }

    var gotMuted: (duration: String) -> Component = { duration ->
        LanguageConfig.insertDuration(data.gotMuted, duration)
    }

    var areMuted: (duration: String) -> Component = { duration ->
        LanguageConfig.insertDuration(data.areMuted, duration)
    }

    var unmutedPlayer: (player: ServerPlayerEntity) -> Component = { player ->
        LanguageConfig.insertPlayer(data.unmutedPlayer, player)
    }

    var gotUnmuted = mm.deserialize(data.gotUnmuted)

    private fun gamemodeToString(gameMode: GameMode): String {
        return when (gameMode) {
            GameMode.SURVIVAL -> data.gamemodeSurvival
            GameMode.SPECTATOR -> data.gamemodeSpectator
            GameMode.CREATIVE -> data.gamemodeCreative
            GameMode.ADVENTURE -> data.gamemodeAdventure
        }
    }
}