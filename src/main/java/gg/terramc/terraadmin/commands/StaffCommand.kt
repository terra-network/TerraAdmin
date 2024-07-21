package gg.terramc.terraadmin.commands

import gg.terramc.terraadmin.TerraAdmin
import gg.terramc.terraadmin.persistence.StaffInventory
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minecraft.nbt.NbtList
import net.minecraft.text.Text
import net.silkmc.silk.commands.command

val StaffCommand = command("staff") {
    runs {
        val player = source.player
        if (player === null) {
            source.sendMessage(Text.of("You must be a player to run this command."))
            return@runs
        }

        val serverState = StaffInventory.getPlayersInventory(player)

        if (serverState.inventory.isNotEmpty()) {
            player.sendMessage(Component.text("Disabling Staff Mode").color(NamedTextColor.RED))
            player.inventory.clear()

            player.inventory.readNbt(serverState.inventory)
            serverState.inventory = NbtList()
        } else {
            player.sendMessage(Component.text("Enabling Staff Mode").color(NamedTextColor.GREEN))
            player.inventory.writeNbt(serverState.inventory)
            player.inventory.clear()
            serverState.timesSwitched++
        }


        player.sendMessage(Component.text("Switched " + serverState.timesSwitched + " Times").color(NamedTextColor.RED))
    }
}