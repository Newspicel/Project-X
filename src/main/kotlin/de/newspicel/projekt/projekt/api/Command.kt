/*
 * © Copyright - Pucto.net | Lars Artmann aka. LartyHD & Julian Haag aka. Newspicel  2018.
 */
package de.newspicel.projekt.projekt.api

import com.google.common.base.Optional
import org.bukkit.Bukkit
import org.bukkit.command.*
import org.bukkit.command.Command
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 26.03.2018 03:41.
 * Last edit 28.03.2018
 */

@Suppress("MemberVisibilityCanBePrivate", "LeakingThis")
abstract class Command(val javaPlugin: JavaPlugin,
                       val commandName: String,
                       val permission: String = "",
                       val usage: String = "",
                       val minLength: Int = 0,
                       val maxLength: Int = 0,
                       tabCompleter: TabCompleter? = null) : CommandExecutor {


    init {
        val command: PluginCommand? = javaPlugin.getCommand(commandName)
        if (command != null) {
            command.permission = permission
            command.tabCompleter = tabCompleter
            command.setExecutor(this)
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<out String>): Boolean {
        if (args.size < minLength || args.size > maxLength) {
            sendUseMessage(sender)
        } else {
            execute(sender, args as Array<String>)
        }
        return true
    }

    abstract fun execute(sender: CommandSender, args: Array<String>)

    fun isPlayer(sender: CommandSender): Optional<Player> {
        return if (sender is Player) {
            Optional.of(sender)
        } else {
            sender.sendMessage("Nur Spieler dürfen diesen Command ausführen")
            Optional.absent();
        }

    }

    fun getTarget(sender: CommandSender, player: Player?): Optional<Player> {
        return if (player != null) {
            Optional.of(player)
        } else {
            sender.sendMessage("§aDer Spieler ist nicht §conline!")
            Optional.absent()
        }
    }

    fun getTarget(sender: CommandSender, uuid: UUID): Optional<Player> {
        return getTarget(sender, Bukkit.getPlayer(uuid))
    }

    fun getTarget(sender: CommandSender, name: String): Optional<Player> {
        return getTarget(sender, Bukkit.getPlayer(name))
    }

    fun sendUseMessage(sender: CommandSender) {
        sender.sendMessage("§cNutze: /$commandName $usage")
    }
}
