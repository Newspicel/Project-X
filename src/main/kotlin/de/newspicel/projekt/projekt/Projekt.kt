package de.newspicel.projekt.projekt

import de.newspicel.projekt.projekt.api.home.HomeManager
import de.newspicel.projekt.projekt.commands.*
import de.newspicel.projekt.projekt.listener.ManagementListener
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.plugin.java.JavaPlugin

class Projekt : JavaPlugin() {

    var homeManager: HomeManager? = null

    override fun onEnable() {
        TpaCommand(this)
        VoteCommand(this)
        SetHomeCommand(this)
        ListHomeCommand(this)
        HomeCommand(this)
        DelHomeCommand(this)
        GameModeCommand(this)
        InfoCommand(this)
        ManagementListener(this)
        homeManager = HomeManager(this)

        Bukkit.getWorlds().forEach { worlds ->
            run {
                worlds.setGameRule(GameRule.KEEP_INVENTORY, true)
                worlds.setGameRule(GameRule.MOB_GRIEFING, false)
                worlds.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true)
                worlds.setGameRule(GameRule.DISABLE_ELYTRA_MOVEMENT_CHECK, true)

            }
        }
    }

    override fun onDisable() {

    }


}
