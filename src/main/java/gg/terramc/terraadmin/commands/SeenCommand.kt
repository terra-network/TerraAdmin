package gg.terramc.terraadmin.commands

import gg.terramc.terraadmin.TerraAdmin
import gg.terramc.terraadmin.config.Configs
import me.lucko.fabric.api.permissions.v0.Permissions
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentBuilder
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.network.ServerPlayerInteractionManager
import net.minecraft.text.HoverEvent
import net.minecraft.text.Text
import net.minecraft.world.GameMode
import net.silkmc.silk.commands.command
import kotlin.math.roundToInt

val SeenCommand = command("seen") {
    argument<String>("target") { target ->
        suggestList { TerraAdmin.currentServer?.playerNames?.toList() }
        runs {

            if (source.player !== null && !Permissions.check(source.player!!, "terraadmin.seen")) {
                source.sendMessage(Configs.Language.prefix.append(Configs.Language.noPermission))
                TerraAdmin.LOGGER.info("[TA] ${source.player!!.name.string} tried to run /seen.")
                return@runs
            }

            val targetPlayer = source.server.playerManager.getPlayer(target())
            if (targetPlayer === null) {
                source.sendMessage(TerraAdmin.PREFIX.append(
                    Component.text("This player does not exist.").color(
                        NamedTextColor.RED)))
                return@runs
            }

            val line1 = Component.text("Information About ").color(NamedTextColor.YELLOW).append(Component.text(targetPlayer.name.string)).appendNewline()
            val line2Hover = Component.text("Click to teleport").color(NamedTextColor.GREEN).asHoverEvent()
            val line2Click = ClickEvent.runCommand("/tp ${targetPlayer.x} ${targetPlayer.y} ${targetPlayer.z}")
            val line2 = Component.text("Current Position: ").color(NamedTextColor.YELLOW).append(Component.text("${targetPlayer.x.roundToInt()}, ${targetPlayer.y.roundToInt()}, ${targetPlayer.z.roundToInt()}").hoverEvent(line2Hover).clickEvent(line2Click)).appendNewline()

            val gamemodeLine = Component.text("Current Gamemode: ").color(NamedTextColor.YELLOW).append(Component.text(
                targetPlayer.interactionManager.gameMode.getName()
            )).appendNewline()

            val healHover = Component.text("Click to heal").color(NamedTextColor.GREEN).asHoverEvent()
            val healClick = ClickEvent.runCommand("/heal ${targetPlayer.name.string}")
            val killHover = Component.text("Click to kill").color(NamedTextColor.RED).asHoverEvent()
            val killClick = ClickEvent.runCommand("/kill ${targetPlayer.name.string}")
            val healthLine = Component.text("Current Health: ").color(NamedTextColor.YELLOW).append(Component.text(
                "${targetPlayer.health.roundToInt()}/${targetPlayer.maxHealth.roundToInt()} "
            )).append(Component.text("[♥] ").color(NamedTextColor.GREEN).hoverEvent(healHover).clickEvent(healClick)).append(Component.text("[☠]").color(NamedTextColor.RED).hoverEvent(killHover).clickEvent(killClick)).appendNewline()
            val feedHover = Component.text("Click to feed").color(NamedTextColor.GREEN)
            val feedClick = ClickEvent.runCommand("/feed ${targetPlayer.name.string}")
            val feedLine = Component.text("Current Hunger: ").color(NamedTextColor.YELLOW).append(Component.text("${targetPlayer.hungerManager.foodLevel}/20").hoverEvent(feedHover).clickEvent(feedClick))
            source.sendMessage(line1.append(line2).append(gamemodeLine).append(healthLine).append(feedLine))
        }
    }
}