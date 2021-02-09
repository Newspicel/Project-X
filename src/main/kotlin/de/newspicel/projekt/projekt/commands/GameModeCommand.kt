package de.newspicel.projekt.projekt.commands

import de.newspicel.projekt.projekt.api.Command
import de.newspicel.projekt.projekt.api.Data
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.stream.Collectors

class GameModeCommand(javaPlugin: JavaPlugin) : Command(javaPlugin,
        commandName = "GameMode",
        usage = "<Mode> [Player]",
        minLength = 1,
        maxLength = 2,
        tabCompleter = TabCompleter { _: CommandSender, _: org.bukkit.command.Command, _: String, strings: Array<String> ->
            return@TabCompleter when (strings.size) {
                1 -> listOf("0", "1", "2", "3", "s", "c", "a", "sp", "survival", "creative", "adventure", "spectator")
                2 -> Bukkit.getOnlinePlayers().stream().map { it.name }.collect(Collectors.toList<String>())
                else -> emptyList()
            }
        }) {

    override fun execute(sender: CommandSender, args: Array<String>) {
        if (isPlayer(sender).isPresent) {
            val player: Player = isPlayer(sender).get();
            if (player.isOp) {
                when (args.size) {
                    1 -> {
                        val gameMode = player.gameMode;
                        setGameMode(player, args[0])
                        if (gameMode != player.gameMode)
                            player.sendMessage(Data.prefix + "§aDu hast deinen GameMode zu §9" + player.gameMode + " §ageändert!")
                    }
                    2 -> {
                        if (getTarget(sender, args[1]).isPresent) {
                            val target: Player = getTarget(sender, args[1]).get()
                            setGameMode(target, args[0])
                            target.sendMessage(Data.prefix + "§aDein GameMode hat sich zu §9" + target.gameMode + " §ageändert!")
                            player.sendMessage(Data.prefix + "§aDu hast den Gamemode des Spielers §9" + target.name + " §azu §9" + target.gameMode + " §ageändert!")
                        }
                    }
                }
            }else{
                player.sendMessage(Data.noPermissions)
            }
        }
    }

    private fun setGameMode(player: Player, mode: String) {
        when (mode) {
            "0", "s", "survival" -> player.gameMode = GameMode.SURVIVAL
            "1", "c", "creative" -> player.gameMode = GameMode.CREATIVE
            "2", "a", "adventure" -> player.gameMode = GameMode.ADVENTURE
            "3", "sp", "spectator" -> player.gameMode = GameMode.SPECTATOR
            else -> sendUseMessage(player)
        }
    }
}
