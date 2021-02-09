package de.newspicel.projekt.projekt.commands

import de.newspicel.projekt.projekt.Projekt
import de.newspicel.projekt.projekt.api.Command
import de.newspicel.projekt.projekt.api.Data
import de.newspicel.projekt.projekt.api.home.HomeManager
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class ListHomeCommand(javaPlugin: JavaPlugin) : Command(javaPlugin = javaPlugin, commandName = "listhome",  usage = "<name>", minLength = 0, maxLength = 0,
        tabCompleter = TabCompleter { _: CommandSender, _: org.bukkit.command.Command, _: String, _: Array<String> ->
            return@TabCompleter emptyList()
        }) {




    override fun execute(sender: CommandSender, args: Array<String>) {
        if (isPlayer(sender).isPresent) {
            val player: Player = isPlayer(sender).get()
            val homeManager: HomeManager? = JavaPlugin.getPlugin(Projekt::class.java).homeManager
            if (homeManager?.toStringArray(player.uniqueId.toString())?.isNotEmpty()!!) {
                val stringBuilder = StringBuilder()
                homeManager.toStringArray(player.uniqueId.toString()).forEach { s -> stringBuilder.append(s).append(", ") }
                player.sendMessage(Data.prefix + "§aDu besitzt diese Homes: " + stringBuilder.toString())
            } else {
                player.sendMessage(Data.prefix + "§aDu besitzt keine Homes")
            }
        }
    }
}
