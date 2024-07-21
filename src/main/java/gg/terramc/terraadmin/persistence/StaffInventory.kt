package gg.terramc.terraadmin.persistence

import gg.terramc.terraadmin.TerraAdmin
import net.minecraft.entity.LivingEntity
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtType
import net.minecraft.nbt.NbtTypes
import net.minecraft.server.MinecraftServer
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import java.io.IOException
import java.util.*
import java.util.function.Function


class StaffInventory : PersistentState() {
    var totalSwitches = 0
    var players = mutableMapOf<UUID, StaffMemberInventory>()


    override fun writeNbt(nbt: NbtCompound): NbtCompound {
        nbt.putInt("totalStaffSwitches", totalSwitches)
        val playersNbt = NbtCompound()

        players.forEach { (uuid, data) ->
            val playerNbt = NbtCompound()

            playerNbt.putInt("timesSwitched", data.timesSwitched)
            playerNbt.put("inventory", data.inventory)

            playersNbt.put(uuid.toString(), playerNbt)
        }

        nbt.put("players", playersNbt)

        return nbt
    }

    companion object {
        private fun createFromNbt(tag: NbtCompound): StaffInventory {
            val state = StaffInventory()
            state.totalSwitches = tag.getInt("totalStaffSwitches")

            val playersNbt = tag.getCompound("players")
            playersNbt.keys.forEach { key ->
                val staffInventory = StaffMemberInventory()

                staffInventory.timesSwitched = playersNbt.getCompound(key).getInt("timesSwitched")
                val inventory = playersNbt.getList("inventory", NbtList().nbtType.hashCode())

                staffInventory.inventory = inventory

                val uuid = UUID.fromString(key)
                state.players[uuid] = staffInventory
            }

            return state
        }

        fun getPlayersInventory(player: LivingEntity): StaffMemberInventory {
            val serverState = getServerState(player.world.server!!) ?: throw IOException("Missing server state?")
            return serverState.players.computeIfAbsent(player.uuid) { StaffMemberInventory() }
        }

        private fun getServerState(server: MinecraftServer): StaffInventory? {
            val stateManager = server.getWorld(World.OVERWORLD)?.persistentStateManager ?: return null
            val state = stateManager.getOrCreate({ nbt -> createFromNbt(nbt) }, { StaffInventory() }, TerraAdmin.MOD_ID) ?: return null
            state.markDirty()

            return state
        }
    }

}