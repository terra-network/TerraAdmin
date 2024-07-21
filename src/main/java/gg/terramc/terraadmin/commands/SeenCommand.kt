package gg.terramc.terraadmin.commands

import gg.terramc.terraadmin.TerraAdmin
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
            val targetPlayer = source.server.playerManager.getPlayer(target())
            if (targetPlayer === null) {
                source.sendMessage(TerraAdmin.PREFIX.append(
                    Component.text("This player does not exist.").color(
                        NamedTextColor.RED)))
                return@runs
            }

            val line1 = Component.text("Information About ").color(NamedTextColor.YELLOW).append(targetPlayer.displayName).appendNewline()
            val line2Hover = Component.text("Click to teleport").color(NamedTextColor.GREEN).asHoverEvent()
            val line2Click = ClickEvent.runCommand("/tp ${targetPlayer.x} ${targetPlayer.y} ${targetPlayer.z}")
            val line2 = Component.text("Current Position: ").color(NamedTextColor.YELLOW).append(Component.text("${targetPlayer.x.roundToInt()}, ${targetPlayer.y.roundToInt()}, ${targetPlayer.z.roundToInt()}").hoverEvent(line2Hover).clickEvent(line2Click)).appendNewline()

            val gamemodeLine = Component.text("Current Gamemode: ").color(NamedTextColor.YELLOW).append(Component.text(
                targetPlayer.interactionManager.gameMode.getName()
            )).appendNewline()
            val healthLine = Component.text("Current Health: ").color(NamedTextColor.YELLOW).append(Component.text(
                "${targetPlayer.health}/${targetPlayer.maxHealth}"
            )).appendNewline()
            source.sendMessage(line1.append(line2).append(gamemodeLine))
        }
    }
}