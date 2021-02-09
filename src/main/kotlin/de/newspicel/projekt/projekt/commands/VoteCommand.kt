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

class VoteCommand(javaPlugin: JavaPlugin) : Command(javaPlugin,
        commandName = "vote",
        usage = "",
        minLength = 1,
        maxLength = 1,
        tabCompleter = TabCompleter { _: CommandSender, _: org.bukkit.command.Command, _: String, strings: Array<String> ->
            return@TabCompleter when (strings.size) {
                1 -> listOf("day", "night", "rain", "sun")
                else -> emptyList()
            }
        }) {

    var run = false
    var hashMap: HashMap<Player, Boolean> = HashMap()

    override fun execute(sender: CommandSender, args: Array<String>) {
        if (isPlayer(sender).isPresent) {
            val player: Player = isPlayer(sender).get()
            when (args[0]) {
                "day" -> {
                    if (run){
                        player.sendMessage(Data.prefix + "§aEs läuft derzeit schon eine Abstimmung")
                        return
                    }
                    vote("Tag", player)

                }
                "night" -> {
                    if (run){
                        player.sendMessage(Data.prefix + "§aEs läuft derzeit schon eine Abstimmung")
                        return
                    }
                    vote("Nacht", player)

                }
                "rain" -> {
                    if (run){
                        player.sendMessage(Data.prefix + "§aEs läuft derzeit schon eine Abstimmung")
                        return
                    }
                    vote("Regen", player)

                }
                "sun" -> {
                    if (run){
                        player.sendMessage(Data.prefix + "§aEs läuft derzeit schon eine Abstimmung")
                        return
                    }
                    vote("Sonne", player)
                }
                "yes" -> {
                    hashMap[player] = true
                    player.sendMessage(Data.prefix + "§aDu hast mit §2Ja §aabgestimmt!")
                }
                "no" -> {
                    hashMap[player] = false
                    player.sendMessage(Data.prefix + "§aDu hast mit §4Nein §aabgestimmt!")
                }
                else -> {
                    sendUseMessage(sender)
                }
            }

        }
    }

    private fun vote(message: String, playerss: Player) {

        if(Bukkit.getOnlinePlayers().size == 1){
            Bukkit.broadcastMessage(Data.prefix + "§aDie Abstimmung wurde sofort mit Ja abgestimmt da nur ein Spieler auf dem §aServer ist!")
            when (message.toLowerCase()) {
                "tag" -> {
                    Bukkit.getScheduler().runTask(javaPlugin, Runnable {
                        playerss.world.time /= 24000 + 1000
                    })
                }
                "nacht" -> {
                    Bukkit.getScheduler().runTask(javaPlugin, Runnable {
                        playerss.world.time /= 24000 + 13000
                    })
                }
                "regen" -> {
                    Bukkit.getScheduler().runTask(javaPlugin, Runnable {
                        playerss.world.isThundering = true
                        playerss.world.setStorm(true)
                    })
                }
                "sonne" -> {
                    Bukkit.getScheduler().runTask(javaPlugin, Runnable {
                        playerss.world.isThundering = false
                        playerss.world.setStorm(false)
                    })
                }
            }
            return
        }

        run = true


        Bukkit.getOnlinePlayers().forEach { players ->
            run {
                players.sendMessage(Data.prefix + "§aDer Spieler §9" + playerss.name + " §ahat eine Umfrage gestartet!")
                players.sendMessage(Data.prefix + "§aDer Spieler hat eine abstimmung für $message gestartet!")
                players.spigot().sendMessage()

                val yes = TextComponent()
                yes.text = Data.prefix + "§2[Ja] "
                yes.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote yes")
                yes.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("§aIch stimme mit §2Ja §a ab!").create())

                val no = TextComponent()
                no.text = " §4[Nein]"
                no.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote no")
                no.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("§aIch stimme mit §4Nein §a ab!").create())
                players.spigot().sendMessage(yes, no)
                players.sendMessage(Data.prefix + "Die Abstimmung läuft ab jetzt eine Minute!")
            }


        }

        Thread {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            for (i in 60 downTo 1) {
                when (i) {
                    30 -> Bukkit.broadcastMessage(Data.prefix + "§aNoch 30 Sekunden zum Abstimmen")
                    15 -> Bukkit.broadcastMessage(Data.prefix + "§aNoch 15 Sekunden zum Abstimmen")
                    10 -> Bukkit.broadcastMessage(Data.prefix + "§aNoch 10 Sekunden zum Abstimmen")
                    5 -> Bukkit.broadcastMessage(Data.prefix + "§aNoch 5 Sekunden zum Abstimmen")
                    4 -> Bukkit.broadcastMessage(Data.prefix + "§aNoch 4 Sekunden zum Abstimmen")
                    3 -> Bukkit.broadcastMessage(Data.prefix + "§aNoch 3 Sekunden zum Abstimmen")
                    2 -> Bukkit.broadcastMessage(Data.prefix + "§aNoch 2 Sekunden zum Abstimmen")
                    1 -> Bukkit.broadcastMessage(Data.prefix + "§aNoch eine Sekunde zum Abstimmen")
                }
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }

            val yes:ArrayList<Player> = ArrayList()
            val no:ArrayList<Player> = ArrayList()

            hashMap.forEach { (player, what) ->
                run {
                    if (what) {
                        yes.add(player)
                    } else {
                        no.add(player)
                    }
                }
            }

            val playersint = Bukkit.getOnlinePlayers().size
            val playershalf = playersint - playersint
            if (yes.size >= playershalf){
                Bukkit.broadcastMessage(Data.prefix + "§aDie Abstimmung wurde mit §2Ja §abeendet!")
                when (message.toLowerCase()) {
                    "tag" -> {
                        Bukkit.getScheduler().runTask(javaPlugin, Runnable {
                            playerss.world.time /= 24000 + 1000
                        })
                    }
                    "nacht" -> {
                        Bukkit.getScheduler().runTask(javaPlugin, Runnable {
                            playerss.world.time /= 24000 + 13000
                        })
                    }
                    "regen" -> {
                        Bukkit.getScheduler().runTask(javaPlugin, Runnable {
                            playerss.world.isThundering = true
                            playerss.world.setStorm(true)
                        })
                    }
                    "sonne" -> {
                        Bukkit.getScheduler().runTask(javaPlugin, Runnable {
                            playerss.world.isThundering = false
                            playerss.world.setStorm(false)
                        })
                    }
                }

            }else{
                Bukkit.broadcastMessage(Data.prefix + "§aDie Abstimmung wurde mit §4Nein §abeendet!")
            }
            run = false
        }.start()



    }
}
