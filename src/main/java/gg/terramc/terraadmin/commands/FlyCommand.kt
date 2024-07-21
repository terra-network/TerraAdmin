package gg.terramc.terraadmin.commands

import gg.terramc.terraadmin.TerraAdmin
import me.lucko.fabric.api.permissions.v0.Permissions
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.silkmc.silk.commands.command

fun executeFly(executor: ServerPlayerEntity, server: MinecraftServer, player: String?) {

    if (!Permissions.check(executor, "terraadmin.fly")) {
        TerraAdmin.LOGGER.info("[TA] ${executor.name.string} tried to run /fly.")
        executor.sendMessage(TerraAdmin.PREFIX.append(Component.text("You don't have permission to execute this command.").color(NamedTextColor.RED)))
        return
    }

    val playerEntity: ServerPlayerEntity? = when (player) {
        null -> executor
        else -> server.playerManager.getPlayer(player)
    }

    if (playerEntity === null) {
        TerraAdmin.LOGGER.info("[TA] ${executor.name.string} tried to run /fly ${player} but the player didn't exist.")
        executor.sendMessage(TerraAdmin.PREFIX.append(Component.text("Player $player does not exist").color(NamedTextColor.RED)))
        return
    }

    if (playerEntity !== executor && !Permissions.check(executor, "terraadmin.fly.others")) {
        TerraAdmin.LOGGER.info("[TA] ${executor.name.string} tried to run /fly ${playerEntity.name.string}.")
        executor.sendMessage(TerraAdmin.PREFIX.append(Component.text("You don't have permission to execute this command.").color(NamedTextColor.RED)))
        return
    }

    if (playerEntity.abilities.allowFlying) {
        playerEntity.abilities.allowFlying = false
        playerEntity.abilities.flying = false
        playerEntity.sendAbilitiesUpdate()
        if (playerEntity != executor) {
            TerraAdmin.LOGGER.info("[TA] ${executor.name.string} disabled flight for ${playerEntity.name.string}.")
            executor.sendMessage(TerraAdmin.PREFIX.append(Component.text("${playerEntity.name.string} can no longer fly.").color(NamedTextColor.RED)))
            return;
        }
        TerraAdmin.LOGGER.info("[TA] ${executor.name.string} disabled flight.")
        executor.sendMessage(TerraAdmin.PREFIX.append(Component.text("You can no longer fly.").color(NamedTextColor.RED)))
        return
    }
    playerEntity.abilities.allowFlying = true
    playerEntity.abilities.flying = true
    playerEntity.sendAbilitiesUpdate()
    if (playerEntity != executor) {
        TerraAdmin.LOGGER.info("[TA] ${executor.name.string} enabled flight for ${playerEntity.name.string}.")
        executor.sendMessage(TerraAdmin.PREFIX.append(Component.text("${playerEntity.name.string} can now fly.").color(NamedTextColor.GREEN)))
        return
    }
    TerraAdmin.LOGGER.info("[TA] ${executor.name.string} enabled flight.")
    executor.sendMessage(TerraAdmin.PREFIX.append(Component.text("You can now fly.").color(NamedTextColor.GREEN)))
    return
}

val FlyCommand = command("fly") {
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
            executeFly(source.player!!, source.server, player())
        }
    }

    runs {
        if (source.player === null) return@runs

        executeFly(source.player!!, source.server, null)
    }
}