package de.newspicel.projekt.projekt.commands

import de.newspicel.projekt.projekt.Projekt
import de.newspicel.projekt.projekt.api.Command
import de.newspicel.projekt.projekt.api.Data
import de.newspicel.projekt.projekt.api.home.HomeManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class HomeCommand(javaPlugin: JavaPlugin) : Command(javaPlugin = javaPlugin, commandName = "home",  usage = "<name>", minLength = 1, maxLength = 1,
            tabCompleter = TabCompleter { sender: CommandSender, _: org.bukkit.command.Command, _: String, strings: Array<String> ->
            return@TabCompleter when (strings.size) {
                1 -> JavaPlugin.getPlugin(Projekt::class.java).homeManager!!.toStringArray((sender as Player).uniqueId.toString())
                else -> emptyList()
            }
        }){



    override fun execute(sender: CommandSender, args: Array<String>) {
        if (isPlayer(sender).isPresent) {
            var player: Player = isPlayer(sender).get();
            if (args.size == 1) {
                val homeManager: HomeManager? = JavaPlugin.getPlugin(Projekt::class.java).homeManager
                if (homeManager!!.isExist(player.uniqueId.toString(), args[0])) {
                    val fromPlayerAndName = homeManager.getFromPlayerAndName(player.uniqueId.toString(), args[0])
                    val first = fromPlayerAndName.homes.first()
                    player.teleport(Location(Bukkit.getWorld(first.world), first.x, first.y, first.z, first.yaw, first.pitch))
                    player.sendMessage(Data.prefix + "§aDu hast dich zu deinem Home §9" + args[0] + "§a teleportiert!")
                } else {
                    player.sendMessage(Data.prefix + "§aDas Home §9" + args[0] + "§a gibt es nicht!")
                }
            }
        }
    }
}
