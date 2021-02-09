package de.newspicel.projekt.projekt.commands

import de.newspicel.projekt.projekt.Projekt
import de.newspicel.projekt.projekt.api.Command
import de.newspicel.projekt.projekt.api.Data
import de.newspicel.projekt.projekt.api.home.HomeManager
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class DelHomeCommand(javaPlugin: JavaPlugin) : Command(javaPlugin = javaPlugin, commandName = "delhome",  usage = "<name>", minLength = 1, maxLength = 1,
        tabCompleter = TabCompleter { _: CommandSender, _: org.bukkit.command.Command, _: String, _: Array<String> ->
            return@TabCompleter emptyList()
        }) {



    override fun execute(sender: CommandSender, args: Array<String>) {
        if (isPlayer(sender).isPresent) {
            val player: Player = isPlayer(sender).get();
            val homeManager: HomeManager? = JavaPlugin.getPlugin(Projekt::class.java).homeManager
            if (args.size == 1) {
                if (homeManager!!.isExist(player.uniqueId.toString(), args[0])) {
                    homeManager.delHome(player.uniqueId.toString(), args[0])
                    player.sendMessage(Data.prefix + "§aDu hast das Home §9" + args[0] + "§a gelöscht!")
                } else {
                    player.sendMessage(Data.prefix + "§aDas Home §9" + args[0] + "§a gibt es nicht!")
                }
            }
        }
    }

}
