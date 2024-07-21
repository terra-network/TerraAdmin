package gg.terramc.terraadmin.config

import gg.terramc.terraadmin.TerraAdmin

object Configs {
    private val BASE_PATH = "./config/${TerraAdmin.MOD_ID.lowercase()}/"
    val Language = LanguageConfig.load("${BASE_PATH}language.toml")
}