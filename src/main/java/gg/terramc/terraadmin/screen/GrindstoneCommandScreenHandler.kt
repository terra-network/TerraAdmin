package gg.terramc.terraadmin.screen

import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.CraftingScreenHandler
import net.minecraft.screen.GrindstoneScreenHandler
import net.minecraft.screen.ScreenHandlerContext

class GrindstoneCommandScreenHandler(syncId: Int, inv: PlayerInventory, ctx: ScreenHandlerContext) : GrindstoneScreenHandler(syncId, inv, ctx) {
    override fun canUse(player: PlayerEntity): Boolean {
        return Permissions.check(player, "terraadmin.grindstone")
    }
}