package de.newspicel.projekt.projekt.commands

import de.newspicel.projekt.projekt.Projekt
import de.newspicel.projekt.projekt.api.Command
import de.newspicel.projekt.projekt.api.Data
import de.newspicel.projekt.projekt.api.home.Home
import de.newspicel.projekt.projekt.api.home.HomeManager
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class SetHomeCommand(javaPlugin: JavaPlugin) : Command(javaPlugin = javaPlugin, commandName = "sethome",  usage = "<name>", minLength = 1, maxLength = 1,
        tabCompleter = TabCompleter { _: CommandSender, _: org.bukkit.command.Command, _: String, _: Array<String> ->
            return@TabCompleter emptyList()
        }) {

    private val homeManager: HomeManager? = JavaPlugin.getPlugin(Projekt::class.java).homeManager

    override fun execute(sender: CommandSender, args: Array<String>) {
        if (isPlayer(sender).isPresent) {
            var player: Player = isPlayer(sender).get();
            if (args.size == 1) {
                val homeManager: HomeManager? = JavaPlugin.getPlugin(Projekt::class.java).homeManager
                if (!homeManager!!.isExist(player.uniqueId.toString(), args[0])) {
                    homeManager.addHome(Home(player.uniqueId.toString(), args[0], player.location.x, player.location.y, player.location.z, player.location.yaw, player.location.pitch, player.location.world?.name))
                    player.sendMessage(Data.prefix + "§aDu hast das Home §9" + args[0] + "§a erstellt!")
                } else {
                    player.sendMessage(Data.prefix + "§aDas Home §9" + args[0] + "§a gibt es bereits!")
                }
            }
        }
    }
}
