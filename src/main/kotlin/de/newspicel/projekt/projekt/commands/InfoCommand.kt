package de.newspicel.projekt.projekt.commands

import de.newspicel.projekt.projekt.api.Command
import de.newspicel.projekt.projekt.api.Data
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class InfoCommand(javaPlugin: JavaPlugin): Command(javaPlugin = javaPlugin, commandName = "info",  usage = "", minLength = 0, maxLength = 0){

    override fun execute(sender: CommandSender, args: Array<String>) {
        if (isPlayer(sender).isPresent) {
            val player:Player = isPlayer(sender).get()
            player.sendMessage(Data.prefix + "Herzlich Willkommen auf dem Project X Server.")
            player.sendMessage(Data.prefix + "Diese Befehle kannst du benutzen:")
            player.sendMessage(Data.prefix + "Teleport with Request: /tpa")
            player.sendMessage(Data.prefix + "Homesystem: /sethome /home /listhome /delhome")
            player.sendMessage(Data.prefix + "Das Treesystem toggeln: /sm toggle")
            player.sendMessage(Data.prefix + "Um für Regen, Sonne, Tag oder Nacht zu voten: /vote")
        }
    }
}
