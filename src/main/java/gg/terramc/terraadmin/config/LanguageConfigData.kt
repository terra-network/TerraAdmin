package gg.terramc.terraadmin.config

import kotlinx.serialization.Serializable


class LanguageConfigData {
    companion object {
        val THEME_COLOR = "#6a994e"
    }

    @Serializable
    data class Config (
        var prefix: String = "<$THEME_COLOR>ᴛᴇʀʀᴀ <dark_gray><bold> • </bold></dark_gray>",
        var noPermission: String = "<red>ʏᴏᴜ ᴅᴏɴ'ᴛ ʜᴀᴠᴇ ᴘᴇʀᴍɪꜱꜱɪᴏɴ ᴛᴏ ᴇxᴇᴄᴜᴛᴇ ᴛʜɪꜱ ᴄᴏᴍᴍᴀɴᴅ.</red>",
        var playerNotFound: String = "<red>ᴘʟᴀʏᴇʀ <player> ᴄᴏᴜʟᴅ ɴᴏᴛ ʙᴇ ꜰᴏᴜɴᴅ.",
        var movement: MovementCommands = MovementCommands(),
        var player: PlayerCommands = PlayerCommands()

    )

    @Serializable
    data class MovementCommands (
        var playerNowFlying: String = "<$THEME_COLOR><player>'s ꜰʟʏɪɴɢ: <green>ᴇɴᴀʙʟᴇᴅ</green>",
        var playerNoLongerFlying: String = "<player>'s ꜰʟʏɪɴɢ: <red>ᴅɪꜱᴀʙʟᴇᴅ</red>",
        var nowFlying: String = "<white>ꜰʟʏɪɴɢ: </white><green>ᴇɴᴀʙʟᴇᴅ</green>",
        var noLongerFlying: String = "<white>ꜰʟʏɪɴɢ: </white><red>ᴅɪꜱᴀʙʟᴇᴅ</red>",
        var teleportedTop: String = "<white>ᴛᴇʟᴇᴘᴏʀᴛᴇᴅ ᴛᴏ ᴛʜᴇ ᴛᴏᴘ.</white>",
        var noSafeTop: String = "<red>ᴛʜᴇʀᴇ ᴡᴀꜱ ɴᴏ ꜱᴀꜰᴇ ᴘʟᴀᴄᴇ ᴛᴏ ɢᴏ ᴛᴏ.</red>"
    )

    @Serializable
    data class PlayerCommands (
        var setGamemode: String = "<white>ꜱᴇᴛ ɢᴀᴍᴇᴍᴏᴅᴇ ᴛᴏ</white> <$THEME_COLOR><gamemode><white>.</white>",
        var playerSetGamemode: String = "<white>ꜱᴇᴛ ɢᴀᴍᴇᴍᴏᴅᴇ ᴛᴏ</white> <$THEME_COLOR><gamemode><white> ꜰᴏʀ </white><$THEME_COLOR><player><white>.</white>",
        val invalidGamemode: String = "<red>ɢᴀᴍᴇᴍᴏᴅᴇ <gamemode> ᴅᴏᴇꜱ ɴᴏᴛ ᴇxɪꜱᴛ</red>",
        val gamemodeSurvival: String = "ꜱᴜʀᴠɪᴠᴀʟ",
        val gamemodeCreative: String = "ᴄʀᴇᴀᴛɪᴠᴇ",
        val gamemodeSpectator: String = "ꜱᴘᴇᴄᴛᴀᴛᴏʀ",
        val gamemodeAdventure: String = "ᴀᴅᴠᴇɴᴛᴜʀᴇ",
        val healedPlayer: String = "<white>ʜᴇᴀʟᴇᴅ </white><$THEME_COLOR><player><white>.</white>",
        val fedPlayer: String = "<white>ꜰᴇᴅ </white><$THEME_COLOR><player><white>.</white>"
    )
}