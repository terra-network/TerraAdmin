package gg.terramc.terraadmin.commands

import gg.terramc.terraadmin.TerraAdmin
import gg.terramc.terraadmin.config.Configs
import net.silkmc.silk.commands.command

val TPHereCommand = command("tphere") {
    argument<String>("target") { target ->
        suggestList { TerraAdmin.currentServer?.playerNames?.toList() }
        runs {
            if (source.player == null) {
                source.sendFailure(Configs.Language.prefix.append(Configs.Language.mustBePlayer))
                return@runs
            }
        }
    }
}