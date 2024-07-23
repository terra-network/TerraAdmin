package gg.terramc.terraadmin.config

import com.akuleshov7.ktoml.Toml
import gg.terramc.terraadmin.config.language.LanguageMovementConfig
import gg.terramc.terraadmin.config.language.LanguagePlayerConfig
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.minecraft.server.network.ServerPlayerEntity
import java.io.File
import kotlin.io.path.*

class LanguageConfig(private val path: String, private val data: LanguageConfigData.Config) {
    companion object {
        private val mm = MiniMessage.miniMessage()
        private val defaults = LanguageConfigData.Config()

        fun load(path: String): LanguageConfig {
            val file = File(path)
            if (!file.exists()) {
                val defaultString = Toml.encodeToString<LanguageConfigData.Config>(defaults)
                verifyConfigFolders()

                val newFile = Path(path).createFile()
                newFile.writeText(defaultString, Charsets.UTF_8)

                return LanguageConfig(path, defaults)
            }

            val content = file.readText(Charsets.UTF_8)
            val decoded = Toml.decodeFromString<LanguageConfigData.Config>(content)
            val config = LanguageConfig(path, decoded)

            config.save()

            return config
        }

        fun insertPlayer(data: String, player: ServerPlayerEntity): Component {
            return mm.deserialize(data, Placeholder.unparsed("player", player.name.string))
        }
        fun insertPlayer(data: String, player: String?): Component {
            return mm.deserialize(data, Placeholder.unparsed("player", player.orEmpty()))
        }
        fun insertGamemode(data: String, gameMode: String?): Component {
            return mm.deserialize(data, Placeholder.unparsed("gamemode", gameMode.orEmpty()))
        }

        fun insertPlayerAndGamemode(data: String, gameMode: String, player: ServerPlayerEntity): Component {
            return mm.deserialize(data, Placeholder.unparsed("gamemode", gameMode), Placeholder.unparsed("player", player.name.string))
        }

        fun insertPlayerAndDuration(data: String, player: ServerPlayerEntity, duration: String): Component {
            return mm.deserialize(data, Placeholder.unparsed("player", player.name.string), Placeholder.unparsed("duration", duration))
        }

        fun insertDuration(data: String, duration: String): Component {
            return mm.deserialize(data, Placeholder.unparsed("duration", duration))
        }
    }

    val prefix = mm.deserialize(data.prefix)
    val noPermission = mm.deserialize(data.noPermission)
    val playerNotFound: (player: String?) -> Component = { player ->
        insertPlayer(data.playerNotFound, player)
    }
    val mustBePlayer = mm.deserialize(data.mustBePlayer)
    val movement = LanguageMovementConfig(mm, data.movement)
    val player = LanguagePlayerConfig(mm, data.player)

    fun save() {
        val config = Toml.encodeToString<LanguageConfigData.Config>(data)
        Path(path).writeText(config)
    }
}