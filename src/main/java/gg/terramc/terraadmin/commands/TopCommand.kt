package gg.terramc.terraadmin.commands

import gg.terramc.terraadmin.TerraAdmin
import me.lucko.fabric.api.permissions.v0.Permissions
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.chunk.Chunk
import net.silkmc.silk.commands.command
import kotlin.math.min


val TopCommand = command("top") {
    runs {
        if (source.player === null) return@runs

        if (!Permissions.check(source.player!!, "terraadmin.top")) {

            source.sendMessage(TerraAdmin.PREFIX.append(Component.text("You don't have permission to execute this command.").color(NamedTextColor.RED)))
            TerraAdmin.LOGGER.info("[TA] ${source.player!!.name.string} tried to run /top.")
            return@runs
        }

        val newY: Int?
        val newX = source.player!!.x.toInt()
        val newZ = source.player!!.z.toInt()

        val targetXZ = BlockPos(newX, 0, newZ)

        val chunk = source.world.getChunk(targetXZ)
        newY = getTop(chunk, newX, newZ)

        if (newY === null) {
            source.sendMessage(TerraAdmin.PREFIX.append(Component.text("Could not find a safe top.").color(NamedTextColor.RED)))
            TerraAdmin.LOGGER.info("[TA] ${source.player!!.name.string} tried to run /top but there was no safe tp.")
            return@runs
        }

        source.player!!.teleport(source.world, newX.toDouble(), newY.toDouble(), newZ.toDouble(), source.player!!.getHeadYaw(), source.player!!.pitch)
        TerraAdmin.LOGGER.info("[TA] ${source.player!!.name.string} ran /top.")
        source.sendMessage(TerraAdmin.PREFIX.append(Component.text("Teleporting to top.").color(NamedTextColor.GREEN)))
    }
}

fun getTop(chunk: Chunk, x: Int, z: Int): Int? {
    val maxY = calculateMaxY(chunk)
    val bottomY = chunk.bottomY;
    if (maxY <= bottomY) return null

    val mutablePos = BlockPos.Mutable(x, maxY, z);
    var isAir1 = chunk.getBlockState(mutablePos).isAir // Block at head level
    var isAir2 = chunk.getBlockState(mutablePos.move(Direction.DOWN)).isAir // Block at feet level
    var isAir3: Boolean // Block below feet

    while (mutablePos.y > bottomY) {
        isAir3 = chunk.getBlockState(mutablePos.move(Direction.DOWN)).isAir
        if (!isAir3 && isAir2 && isAir1) { // If there is a floor block and space for player body+head
            return mutablePos.y + 1
        }

        isAir1 = isAir2
        isAir2 = isAir3
    }

    return null
}

fun calculateMaxY(chunk: Chunk): Int {
    val maxY = chunk.topY;
    val sections = chunk.sectionArray;
    val maxSectionIndex = (sections.count() - 1).coerceAtMost(maxY shr 4)

    for (index in maxSectionIndex downTo 0) {
        if (!sections[index].isEmpty) {
            return min((index shl 4 + 15), maxY)
        }
    }

    return Int.MAX_VALUE
}