package gg.terramc.terraadmin.commands.bench

import gg.terramc.terraadmin.TerraAdmin
import gg.terramc.terraadmin.config.Configs
import gg.terramc.terraadmin.screen.CraftingCommandScreenHandler
import me.lucko.fabric.api.permissions.v0.Permissions
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.ScreenHandlerFactory
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.text.Text
import net.silkmc.silk.commands.command
fun getWorkbenchFactory(): ScreenHandlerFactory {
    return ScreenHandlerFactory { syncId, inv, player ->
        CraftingCommandScreenHandler(syncId, inv, ScreenHandlerContext.create(player.entityWorld, player.blockPos))
    }
}

val WorkbenchCommand = command("workbench") {
    runs {
        val player = source.playerOrThrow

        if (!Permissions.check(player, "terraadmin.workbench")) {
            TerraAdmin.LOGGER.info("[TA] ${player.name.string} tried to run /workbench")
            source.sendMessage(Configs.Language.prefix.append(Configs.Language.noPermission))
            return@runs
        }


        val screen = SimpleNamedScreenHandlerFactory(getWorkbenchFactory(), Text.translatable("block.minecraft.crafting_table"))
        TerraAdmin.LOGGER.info("[TA] ${player.name.string} ran /workbench")

        player.openHandledScreen(screen)
    }
}

val CraftCommand = command("craft") {
    runs {
        val player = source.playerOrThrow

        if (!Permissions.check(player, "terraadmin.workbench")) {
            TerraAdmin.LOGGER.info("[TA] ${player.name.string} tried to run /craft")
            source.sendMessage(Configs.Language.prefix.append(Configs.Language.noPermission))
            return@runs
        }


        val screen = SimpleNamedScreenHandlerFactory(getWorkbenchFactory(), Text.translatable("block.minecraft.crafting_table"))
        TerraAdmin.LOGGER.info("[TA] ${player.name.string} ran /craft")

        player.openHandledScreen(screen)
    }
}