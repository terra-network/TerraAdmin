package gg.terramc.terraadmin.config

import gg.terramc.terraadmin.TerraAdmin
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.exists

object Configs {
    private val BASE_PATH = "./config/${TerraAdmin.MOD_ID.lowercase()}/"
    val Language = LanguageConfig.load("${BASE_PATH}language.toml")
}

fun verifyConfigFolders() {
    if (Path("./config/terraadmin").exists()) return
    if (Path("./config").exists()) {
        Path("./config/terraadmin").createDirectory()
        return
    }
    Path("./config").createDirectory()
    Path("./config/terraadmin").createDirectory()
    return
}