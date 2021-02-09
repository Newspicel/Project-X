/*
 * © Copyright - Pucto.net | Lars Artmann aka. LartyHD & Julian Haag aka. Newspicel  2018.
 */

package de.newspicel.projekt.projekt.commands


import de.newspicel.projekt.projekt.api.Command
import de.newspicel.projekt.projekt.api.Data
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.stream.Collectors


class TpaCommand(javaPlugin: JavaPlugin) : Command(javaPlugin,
        commandName = "Tpa",
        usage = "<Spieler>",
        minLength = 1,
        maxLength = 2,
        tabCompleter = TabCompleter { _: CommandSender, _: org.bukkit.command.Command, _: String, strings: Array<String> ->
            return@TabCompleter when (strings.size) {
                1 -> Bukkit.getOnlinePlayers().stream().map { it.name }.collect(Collectors.toList<String>())
                2 -> Bukkit.getOnlinePlayers().stream().map { it.name }.collect(Collectors.toList<String>())
                else -> emptyList()
            }
        }) {

    private val requests: MutableMap<Player, Player>

    init {
        this.requests = HashMap()
    }

    @Suppress("LABEL_NAME_CLASH")
    override fun execute(sender: org.bukkit.command.CommandSender, args: Array<String>) {
        if (isPlayer(sender).isPresent) {
            var player: Player = isPlayer(sender).get();
            if (args.size == 1) {
                if (isPlayer(sender).isPresent) {
                    var target: Player = getTarget(sender, args[0]).get();
                    if (target == player) {
                        sender.sendMessage(Data.prefix + "§aDu darfts dir nicht selber eine teleportation's Anfrage gesenden")
                        return
                    }
                    player.sendMessage(Data.prefix + "§aDu hast dem Spieler §9" + target.name + " §aeine teleportation's Anfrage gesendet!")
                    val textComponent = TextComponent()
                    textComponent.text = Data.prefix + "§aDarf sich §9" + player.name + " §azu dir telepotieren? §2[ACCEPT§2]"
                    textComponent.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa accept " + player.name)
                    textComponent.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("§aJa er darf sich zu mir teleportieren!").create())
                    target.spigot().sendMessage(textComponent)
                    this.requests[target] = player
                    Thread {
                        try {
                            Thread.sleep(120000)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

                        this.requests.remove(target)
                    }.start()
                }
            } else if (args.size == 2 && args[0].equals("accept", ignoreCase = true)) {
                if (isPlayer(sender).isPresent) {
                    val target: Player = getTarget(sender, args[1]).get()
                    if (this.requests[player] === target) {
                        this.requests.remove(player)
                        target.sendMessage(Data.prefix + "§aDer Spieler §9" + player.name + " §ahat deine teleportation's Anfrage angenommen!")
                        target.sendMessage(Data.prefix + "§aDu wirst zu §9" + player.name + " §ateleportiert!")
                        player.sendMessage(Data.prefix + "§aDer Spieler §9" + target.name + " §awird zu dir teleportiert!")
                        target.teleport(player)
                        target.sendMessage(Data.prefix + "§aDu hast dich zu §9" + player.name + " §a teleportiert!")
                    } else {
                        sender.sendMessage(Data.prefix + "§aDer Spieler hat dir keine Anfrage gesendt")
                    }
                }
            }
        }
    }
}
