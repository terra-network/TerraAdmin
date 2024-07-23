package gg.terramc.terraadmin.commands

import net.kyori.adventure.text.Component
import net.silkmc.silk.commands.command
import kotlin.time.Duration
import kotlin.time.DurationUnit
import gg.terramc.terraadmin.TerraAdmin
import gg.terramc.terraadmin.config.Configs
import gg.terramc.terraadmin.persistence.ServerPlayersData
import me.lucko.fabric.api.permissions.v0.Permissions
import java.time.ZonedDateTime
import kotlin.time.toDuration

val MuteCommand = command("mute") {
    argument<String>("target") { target ->
        suggestList { TerraAdmin.currentServer?.playerNames?.toList() }
        argument<String>("duration") { duration ->
            runs {

                if (source.player !== null && !Permissions.check(source.player!!, "terraadmin.mute")) {
                    source.sendMessage(Configs.Language.prefix.append(Configs.Language.noPermission))
                    TerraAdmin.LOGGER.info("[TA] ${source.player!!.name.string} tried to run /mute.")
                    return@runs
                }

                val targetPlayer = source.server.playerManager.getPlayer(target())
                if (targetPlayer === null) {
                    source.sendMessage(Configs.Language.prefix.append(Configs.Language.playerNotFound(target())))
                    return@runs
                }


                val dur = Duration.parseOrNull(duration())
                val dura = dur?.toLong(DurationUnit.SECONDS)
                if (dura === null) {
                    source.sendMessage(Configs.Language.prefix)
                    return@runs
                }

                val expiresAt = ZonedDateTime.now().plusSeconds(dura)

                val playerData = ServerPlayersData.get(targetPlayer)
                playerData.mutedTil = expiresAt

                source.sendMessage(Configs.Language.prefix.append(Configs.Language.player.mutedPlayer(targetPlayer, dura.toDuration(DurationUnit.SECONDS).toString())))
                targetPlayer.sendMessage(Configs.Language.prefix.append(Configs.Language.player.gotMuted(dura.toDuration(DurationUnit.SECONDS).toString())))
                return@runs
            }
        }
    }
}

val UnmuteCommand = command("unmute") {
    argument<String>("target") { target ->
        suggestList { TerraAdmin.currentServer?.playerNames?.toList() }
        runs {

            if (source.player !== null && !Permissions.check(source.player!!, "terraadmin.mute")) {
                source.sendMessage(Configs.Language.prefix.append(Configs.Language.noPermission))
                TerraAdmin.LOGGER.info("[TA] ${source.player!!.name.string} tried to run /unmute.")
                return@runs
            }

            val targetPlayer = source.server.playerManager.getPlayer(target())
            if (targetPlayer === null) {
                source.sendMessage(Configs.Language.prefix.append(Configs.Language.playerNotFound(target())))
                return@runs
            }


            val playerData = ServerPlayersData.get(targetPlayer)
            playerData.mutedTil = null

            source.sendMessage(Configs.Language.prefix.append(Configs.Language.player.unmutedPlayer(targetPlayer)))
            targetPlayer.sendMessage(Configs.Language.prefix.append(Configs.Language.player.gotUnmuted))
        }
    }
}