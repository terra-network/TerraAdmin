package gg.terramc.terraadmin.commands.bench

import gg.terramc.terraadmin.TerraAdmin
import gg.terramc.terraadmin.config.Configs
import gg.terramc.terraadmin.screen.CraftingCommandScreenHandler
import gg.terramc.terraadmin.screen.GrindstoneCommandScreenHandler
import me.lucko.fabric.api.permissions.v0.Permissions
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.ScreenHandlerFactory
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.text.Text
import net.silkmc.silk.commands.command

fun getEnderchestFactory(): ScreenHandlerFactory {
    return ScreenHandlerFactory { syncId, inv, player ->
        GenericContainerScreenHandler.createGeneric9x3(syncId, inv, player.enderChestInventory)
    }
}

val EnderchestCommand = command("enderchest") {
    runs {
        val player = source.playerOrThrow

        if (!Permissions.check(player, "terraadmin.enderchest")) {
            TerraAdmin.LOGGER.info("[TA] ${player.name.string} tried to run /enderchest")
            source.sendMessage(Configs.Language.prefix.append(Configs.Language.noPermission))
            return@runs
        }


        val screen = SimpleNamedScreenHandlerFactory(getEnderchestFactory(), Text.translatable("container.enderchest"))
        TerraAdmin.LOGGER.info("[TA] ${player.name.string} ran /enderchest")
        player.openHandledScreen(screen)
    }
}