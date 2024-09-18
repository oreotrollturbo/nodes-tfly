package org.oreo.nodesTfly.listeners

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.oreo.nodesTfly.NodesTfly


class PlayerShootListener(private val plugin : NodesTfly) : org.bukkit.event.Listener {

    @EventHandler
    fun onPlayerDamage(e: EntityDamageByEntityEvent){

        val damager = e.damager

        if (damager !is Player || plugin.isTflying.contains(damager)){
            return
        }

        e.isCancelled = true

        damager.sendMessage("§cYou cannot damage players while using Tfly")

    }

    @EventHandler
    fun onPlayerDamage(e: PlayerInteractEvent){

        val player = e.player

        val act = e.action

        if (isShooting(act,player)){
            e.isCancelled = true
        }
    }

    fun isShooting(act: Action , player: Player) : Boolean{

        val holdingItem = player.inventory.itemInMainHand

        return (act == Action.LEFT_CLICK_AIR || act == Action.LEFT_CLICK_BLOCK) && holdingItem.type == Material.WARPED_FUNGUS_ON_A_STICK
    }

}