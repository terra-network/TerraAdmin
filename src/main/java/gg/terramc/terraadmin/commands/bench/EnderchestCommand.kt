package gg.terramc.terraadmin.commands.bench

import gg.terramc.terraadmin.TerraAdmin
import gg.terramc.terraadmin.screen.CraftingCommandScreenHandler
import gg.terramc.terraadmin.screen.GrindstoneCommandScreenHandler
import me.lucko.fabric.api.permissions.v0.Permissions
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.ScreenHandlerFactory
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.text.Text
import net.silkmc.silk.commands.command

fun getGrindstoneFactory(): ScreenHandlerFactory {
    return ScreenHandlerFactory { syncId, inv, player ->
        GrindstoneCommandScreenHandler(syncId, inv, ScreenHandlerContext.create(player.entityWorld, player.blockPos))
    }
}

val GrindstoneCommand = command("grindstone") {
    runs {
        val player = source.playerOrThrow

        if (!Permissions.check(player, "terraadmin.grindstone")) {
            source.sendMessage(
                TerraAdmin.PREFIX.append(
                    Component.text("You don't have permission to execute this command.").color(
                        NamedTextColor.RED)))
            return@runs
        }


        val screen = SimpleNamedScreenHandlerFactory(getGrindstoneFactory(), Text.translatable("block.minecraft.grindstone"))

        player.openHandledScreen(screen)
    }
}