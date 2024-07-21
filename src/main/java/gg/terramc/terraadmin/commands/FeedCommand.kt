package gg.terramc.terraadmin.commands

import gg.terramc.terraadmin.TerraAdmin
import gg.terramc.terraadmin.config.Configs
import me.lucko.fabric.api.permissions.v0.Permissions
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.silkmc.silk.commands.command

val FeedCommand = command("feed") {
    argument<String>("target") { target ->
        suggestList { TerraAdmin.currentServer?.playerNames?.toList() }
        runs {

            if (source.player !== null && !Permissions.check(source.player!!, "terraadmin.feed")) {
                source.sendMessage(Configs.Language.prefix.append(Configs.Language.noPermission))
                TerraAdmin.LOGGER.info("[TA] ${source.player!!.name.string} tried to run /feed.")
                return@runs
            }

            val targetPlayer = source.server.playerManager.getPlayer(target())
            if (targetPlayer === null) {
                source.sendMessage(Configs.Language.prefix.append(Configs.Language.playerNotFound(target())))
                return@runs
            }

            targetPlayer.hungerManager.foodLevel = 20;
            source.sendMessage(Configs.Language.prefix.append(Configs.Language.player.fedPlayer(targetPlayer)))

        }

    }
}