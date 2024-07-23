package gg.terramc.terraadmin.events

import gg.terramc.terraadmin.config.Configs
import gg.terramc.terraadmin.persistence.ServerPlayersData
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun registerMessageEvent() {
    ServerMessageEvents.ALLOW_CHAT_MESSAGE.register { _, p, _ ->
        val playerData = ServerPlayersData.get(p)
        if (playerData.mutedTil == null) return@register true
        if (!playerData.mutedTil!!.isBefore(ZonedDateTime.now())) {
            val dura = ZonedDateTime.now().until(playerData.mutedTil!!, ChronoUnit.SECONDS)
            p.sendMessage(
                Configs.Language.prefix.append(
                    Configs.Language.player.areMuted(dura.toDuration(
                        DurationUnit.SECONDS).toString())))
            return@register false
        }
        return@register true
    }
}