package de.newspicel.projekt.projekt.listener

import de.newspicel.projekt.projekt.api.Data
import javafx.util.Duration.seconds
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.CreatureSpawner
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.lang.reflect.Type
import java.util.*


class ManagementListener(javaPlugin: JavaPlugin) : Listener {

    private val javaPlugin: JavaPlugin

    init {
        Bukkit.getPluginManager().registerEvents(this, javaPlugin)
        this.javaPlugin = javaPlugin
    }

    @EventHandler
    fun onChatEvent(playerChatEvent: AsyncPlayerChatEvent) {
        var cancel = false
        if (playerChatEvent.message.contains("%")) {
            cancel = true
        }

        if (playerChatEvent.message.contains("§")) {
            cancel = true
        }


        playerChatEvent.message = playerChatEvent.message.replace("&", "§")


        if (cancel) {
            playerChatEvent.player.sendMessage(Data.noPermissions)
            playerChatEvent.isCancelled = true
        }

        playerChatEvent.format = playerChatEvent.player.displayName + " §8» §r§7" + playerChatEvent.message
    }

    @EventHandler
    fun onJoinEvent(playerJoinEvent: PlayerJoinEvent) {
        playerJoinEvent.joinMessage = Data.prefix + "§aDer Spieler §9" + playerJoinEvent.player.displayName + " §ahat das Spiel betreten!"

        val addAttachment = playerJoinEvent.player.addAttachment(javaPlugin)

        addAttachment.setPermission("smoothtimber.*", true)

        playerJoinEvent.player.sendMessage(Data.prefix + "Mit /info kannst du Informationen aufrufen.")

    }

    @EventHandler
    fun onQuitEvent(playerQuitEvent: PlayerQuitEvent) {
        playerQuitEvent.quitMessage = Data.prefix + "§aDer Spieler §9" + playerQuitEvent.player.displayName + " §ahat das Spiel verlassen!"
    }

    @EventHandler
    fun onBedEvent(playerBedEnterEvent: PlayerBedEnterEvent) {
        //playerBedEnterEvent.isCancelled = true
        playerBedEnterEvent.player.bedSpawnLocation = playerBedEnterEvent.bed.location
        playerBedEnterEvent.player.sendMessage(Data.prefix + "§aDu hast das Bett betreten und §adie Zeit auf Tag gestellt!")
        Bukkit.broadcastMessage(Data.prefix + "§aDer Spieler §9" + playerBedEnterEvent.player.displayName + " §ahat ein Bett betreten und die Zeit auf Tag gestellt!")
        playerBedEnterEvent.player.world.time /= 24000 + 1000
    }

    @EventHandler
    fun onBreakSpawner(blockBreak: BlockBreakEvent) {
        if (blockBreak.block.type == Material.SPAWNER) {
            val location = blockBreak.block.location
            val creatureSpawner = blockBreak.block.state as CreatureSpawner

            blockBreak.expToDrop = 0

            val spawnerItem:ItemStack = blockBreak.block.state.data.toItemStack(1)
            val spawnerMeta = spawnerItem.itemMeta
            spawnerMeta!!.setDisplayName("Spawner")

            val lore = ArrayList<String>()
            lore.add(creatureSpawner.creatureTypeName.toUpperCase(Locale.GERMAN))
            spawnerMeta.lore = lore
            spawnerItem.itemMeta = spawnerMeta

            location.world!!.dropItemNaturally(location, spawnerItem)
        }
}

    private fun setGameMode(player: Player, mode: String) {
        when (mode) {
            "0", "s", "survival" -> player.gameMode = GameMode.SURVIVAL
            "1", "c", "creative" -> player.gameMode = GameMode.CREATIVE
            "2", "a", "adventure" -> player.gameMode = GameMode.ADVENTURE
            "3", "sp", "spectator" -> player.gameMode = GameMode.SPECTATOR
        }
    }

}
