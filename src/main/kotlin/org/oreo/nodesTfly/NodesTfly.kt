package org.oreo.nodesTfly

import net.luckperms.api.LuckPerms
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.oreo.nodesTfly.commands.TflyCommand
import org.oreo.nodesTfly.listeners.PlayerChangeNode
import org.oreo.nodesTfly.listeners.PlayerShootListener
import phonon.nodes.Nodes

class NodesTfly : JavaPlugin() {

    private val allowedGroups : List<String> = config.getStringList("allowed-groups")

    var tflyEnabled = false

    val isTflying = ArrayList<Player>()

    var nodesInstance : Nodes? = null

    override fun onEnable() {
        val provider = Bukkit.getServicesManager().getRegistration(
            LuckPerms::class.java
        )
        if (provider != null) {
            val api: LuckPerms = provider.provider
        }

        nodesInstance = Bukkit.getServicesManager().load(Nodes::class.java)
        if (nodesInstance == null) {
            // Handle error: service not registered
            logger.severe("Nodes not detected!")
        }

        server.pluginManager.registerEvents(PlayerChangeNode(this,nodesInstance!!),this)
        server.pluginManager.registerEvents(PlayerShootListener(this),this)

        getCommand("tfly")!!.setExecutor(TflyCommand(this,nodesInstance!!))

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
