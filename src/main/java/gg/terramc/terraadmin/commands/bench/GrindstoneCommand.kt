package gg.terramc.terraadmin.commands.bench

import gg.terramc.terraadmin.TerraAdmin
import gg.terramc.terraadmin.screen.CraftingCommandScreenHandler
import me.lucko.fabric.api.permissions.v0.Permissions
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.ScreenHandlerFactory
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.text.Text
import net.silkmc.silk.commands.command
fun getFactory(): ScreenHandlerFactory {
    return ScreenHandlerFactory { syncId, inv, player ->
        CraftingCommandScreenHandler(syncId, inv, ScreenHandlerContext.create(player.entityWorld, player.blockPos))
    }
}

val WorkbenchCommand = command("workbench") {
    runs {
        val player = source.playerOrThrow

        if (!Permissions.check(player, "terraadmin.workbench")) {
            source.sendMessage(
                TerraAdmin.PREFIX.append(
                    Component.text("You don't have permission to execute this command.").color(
                        NamedTextColor.RED)))
            return@runs
        }


        val screen = SimpleNamedScreenHandlerFactory(getFactory(), Text.translatable("block.minecraft.crafting_table"))

        player.openHandledScreen(screen)
    }
}

val CraftCommand = command("craft") {
    runs {
        val player = source.playerOrThrow

        if (!Permissions.check(player, "terraadmin.workbench")) {
            source.sendMessage(
                TerraAdmin.PREFIX.append(
                    Component.text("You don't have permission to execute this command.").color(
                        NamedTextColor.RED)))
            return@runs
        }


        val screen = SimpleNamedScreenHandlerFactory(getFactory(), Text.translatable("block.minecraft.crafting_table"))

        player.openHandledScreen(screen)
    }
}