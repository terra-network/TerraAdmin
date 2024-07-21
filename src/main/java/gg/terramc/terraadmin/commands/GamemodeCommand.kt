package gg.terramc.terraadmin.commands

import gg.terramc.terraadmin.TerraAdmin
import gg.terramc.terraadmin.config.Configs
import gg.terramc.terraadmin.config.LanguageConfig
import me.lucko.fabric.api.permissions.v0.Permissions
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.GameMode
import net.silkmc.silk.commands.command

fun setGamemodeCommand(executor: ServerPlayerEntity, server: MinecraftServer, gamemode: String, player: String?) {

    if (!Permissions.check(executor, "terraadmin.gamemode")) {
        TerraAdmin.LOGGER.info("[TA] ${executor.name.string} tried to change gamemodes.")
        executor.sendMessage(Configs.Language.prefix.append(Configs.Language.noPermission))
        return
    }

    val gamemodeF: GameMode? = when (gamemode) {
        "creative" -> GameMode.CREATIVE
        "survival" -> GameMode.SURVIVAL
        "spectator" -> GameMode.SPECTATOR
        "adventure" -> GameMode.ADVENTURE
        else -> null
    }

    if (gamemodeF === null) {
        TerraAdmin.LOGGER.info("[TA] ${executor.name.string} tried to set gamemode to $gamemode but $gamemode does not exist.")
        executor.sendMessage(Configs.Language.prefix.append(Configs.Language.player.invalidGamemode(gamemode)))
        return
    }

    val playerEntity: ServerPlayerEntity? = when (player) {
        null -> executor
        else -> server.playerManager.getPlayer(player)
    }

    if (playerEntity == null) {
        TerraAdmin.LOGGER.info("[TA] ${executor.name.string} tried to set $player's gamemode to ${gamemodeF.getName()} but $player does not exist.")
        executor.sendMessage(Configs.Language.prefix.append(Configs.Language.playerNotFound(player)))
        return
    }

    if (playerEntity !== executor) {
        if (!Permissions.check(executor, "terraadmin.gamemode.others")) {
            TerraAdmin.LOGGER.info("[TA] ${executor.name.string} tried to set ${playerEntity.name.string}'s gamemode to ${gamemodeF.getName()}.")
            executor.sendMessage(Configs.Language.prefix.append(Configs.Language.noPermission))
            return
        }
    }

    playerEntity.changeGameMode(gamemodeF)
    if (playerEntity === executor) {
        TerraAdmin.LOGGER.info("[TA] ${executor.name.string} set their gamemode to ${gamemodeF.getName()}.")
        executor.sendMessage(Configs.Language.prefix.append(Configs.Language.player.setGamemode(gamemodeF)))
    } else {
        TerraAdmin.LOGGER.info("[TA] ${executor.name.string} set ${playerEntity.name.string}'s gamemode to ${gamemodeF.getName()}.")
        executor.sendMessage(Configs.Language.prefix.append(Configs.Language.player.playerSetGamemode(gamemodeF, playerEntity)))
    }
    return

}

val CreativeCommand = command("creative") {
    argument<String>("player") { player ->
        suggestList {
            var playerNameList: List<String> = listOf();
            if (TerraAdmin.currentServer !== null) {
                playerNameList = TerraAdmin.currentServer!!.playerNames.asList()
            }

            playerNameList
        }
        runs {
            if (source.player === null) return@runs
            setGamemodeCommand(source.player!!, source.server, "creative", player())
            return@runs
        }
    }
    runs {
        if (source.player === null) return@runs
        setGamemodeCommand(source.player!!, source.server, "creative", null)
        return@runs
    }
}

val ShortCreativeCommand = command("gmc") {
    argument<String>("player") { player ->
        suggestList {
            var playerNameList: List<String> = listOf();
            if (TerraAdmin.currentServer !== null) {
                playerNameList = TerraAdmin.currentServer!!.playerNames.asList()
            }

            playerNameList
        }
        runs {
            if (source.player === null) return@runs
            setGamemodeCommand(source.player!!, source.server, "creative", player())
            return@runs
        }
    }
    runs {
        if (source.player === null) return@runs
        setGamemodeCommand(source.player!!, source.server, "creative", null)
        return@runs
    }
}

val SurvivalCommand = command("survival") {
    argument<String>("player") { player ->
        suggestList {
            var playerNameList: List<String> = listOf();
            if (TerraAdmin.currentServer !== null) {
                playerNameList = TerraAdmin.currentServer!!.playerNames.asList()
            }

            playerNameList
        }
        runs {
            if (source.player === null) return@runs
            setGamemodeCommand(source.player!!, source.server, "survival", player())
            return@runs
        }
    }
    runs {
        if (source.player === null) return@runs
        setGamemodeCommand(source.player!!, source.server, "survival", null)
        return@runs
    }
}

val ShortSurvivalCommand = command("gms") {
    argument<String>("player") { player ->
        suggestList {
            var playerNameList: List<String> = listOf();
            if (TerraAdmin.currentServer !== null) {
                playerNameList = TerraAdmin.currentServer!!.playerNames.asList()
            }

            playerNameList
        }
        runs {
            if (source.player === null) return@runs
            setGamemodeCommand(source.player!!, source.server, "survival", player())
            return@runs
        }
    }
    runs {
        if (source.player === null) return@runs
        setGamemodeCommand(source.player!!, source.server, "survival", null)
        return@runs
    }
}

val GamemodeCommand = command("gamemode") {
    argument<String>("toMode") { toMode ->
        suggestList { listOf("creative", "survival", "spectator", "adventure") }
        runs {
            if (source.player === null) return@runs
            setGamemodeCommand(source.player!!, source.server, toMode(), null)
            return@runs
        }
        argument<String>("player") { player ->
            suggestList {
                var playerNameList: List<String> = listOf();
                if (TerraAdmin.currentServer !== null) {
                    playerNameList = TerraAdmin.currentServer!!.playerNames.asList()
                }

                playerNameList
            }
            runs {
                if (source.player === null) return@runs
                setGamemodeCommand(source.player!!, source.server, toMode(), player())

                return@runs
            }
        }
    }
}


fun registerGamemodes() {
    CreativeCommand
    ShortCreativeCommand
    GamemodeCommand
}