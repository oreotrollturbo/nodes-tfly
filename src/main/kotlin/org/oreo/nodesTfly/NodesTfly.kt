package org.oreo.nodesTfly

import net.luckperms.api.LuckPerms
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.oreo.nodesTfly.commands.TflyCommand
import org.oreo.nodesTfly.listeners.PlayerChangeNode
import org.oreo.nodesTfly.listeners.PlayerShootListener

class NodesTfly : JavaPlugin() {

    val allowedGroups : List<String> = config.getStringList("allowed-groups")

    var tflyEnabled = false

    val isTflying = ArrayList<Player>()

    override fun onEnable() {
        val provider = Bukkit.getServicesManager().getRegistration(
            LuckPerms::class.java
        )
        if (provider != null) {
            val api: LuckPerms = provider.provider
        }

        server.pluginManager.registerEvents(PlayerChangeNode(this),this)
        server.pluginManager.registerEvents(PlayerShootListener(this),this)

        getCommand("tfly")!!.setExecutor(TflyCommand(this))

        saveDefaultConfig()
    }

    fun isPlayerAllowedToTfly(player: Player): Boolean {
        for (group in allowedGroups){
            if (player.hasPermission("group.$group")){
                return true
            }
        }
        return false
    }

}
