package gg.terramc.terraadmin.persistence

import gg.terramc.terraadmin.TerraAdmin
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import java.io.IOException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone
import java.util.UUID

class ServerPlayersData : PersistentState() {
    companion object {
        val datePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(TimeZone.getTimeZone("America/Los_Angeles").toZoneId())

        private fun getServerState(server: MinecraftServer): ServerPlayersData? {
            val stateManager = server.getWorld(World.OVERWORLD)?.persistentStateManager ?: return null
            val state = stateManager.getOrCreate({ nbt -> createFromNbt(nbt) }, { ServerPlayersData() }, TerraAdmin.MOD_ID) ?: return null
            state.markDirty()

            return state
        }

        fun get(player: ServerPlayerEntity): PlayerData {
            val serverState = getServerState(player.world.server!!) ?: throw IOException("Missing server state?")
            return serverState.players.computeIfAbsent(player.uuid) { PlayerData() }
        }

        private fun createFromNbt(tag: NbtCompound): ServerPlayersData {
            val state = ServerPlayersData()

            val playersNbt = tag.getCompound("players")

            playersNbt.keys.forEach { key ->
                val playerData = PlayerData()
                val mutedTil = tag.getString("mutedTil")

                if (mutedTil.isNotEmpty()) playerData.mutedTil = ZonedDateTime.parse(tag.getString("mutedTil"), datePattern)

                val uuid = UUID.fromString(key)
                state.players[uuid] = playerData
            }

            return state
        }
    }

    var players = mutableMapOf<UUID, PlayerData>()
    var mutedTil: ZonedDateTime? = null

    override fun writeNbt(nbt: NbtCompound): NbtCompound {

        val playersNbt = NbtCompound()

        players.forEach { (uuid, data) ->
            val playerNbt = NbtCompound()

            if (data.mutedTil !== null) playerNbt.putString("mutedTil", data.mutedTil!!.format(datePattern))
            playersNbt.put(uuid.toString(), playerNbt)
        }

        nbt.put("players", playersNbt)

        return nbt
    }
}