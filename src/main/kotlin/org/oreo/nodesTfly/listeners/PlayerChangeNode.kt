package org.oreo.nodesTfly.listeners

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.oreo.nodesTfly.NodesTfly
import org.spigotmc.event.entity.EntityMountEvent
import phonon.nodes.Nodes
import phonon.nodes.objects.Territory
import java.util.*


class PlayerChangeNode(private val plugin : NodesTfly) : Listener {

    @EventHandler
    fun onPlayerEnterTown(e: PlayerMoveEvent) {

        val player = e.player

        if (!plugin.isPlayerAllowedToTfly(player) || !player.isFlying) {
            return
        }

        val world = Objects.requireNonNull(e.to).world

        val fromX = e.from.blockX
        val fromZ = e.from.blockZ

        val toX = e.to.blockX
        val toZ = e.to.blockZ


        // check if player chunk changed
        checkNotNull(world)
        val fromTerritory: Territory? = Nodes.getTerritoryFromBlock(fromX, fromZ)

        val toTerritory: Territory? = Nodes.getTerritoryFromBlock(toX, toZ)

        if (fromTerritory != toTerritory){
            player.allowFlight = false
            player.sendMessage("§cYou exited your town's territory")
        }
    }

    @EventHandler
    fun playerMounted(e:EntityMountEvent){
        val player = e.entity

        if (player !is Player || player.isOp || !player.isFlying){
            return
        }

        player.allowFlight = false
    }


}