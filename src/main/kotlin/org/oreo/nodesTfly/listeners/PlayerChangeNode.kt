package org.oreo.nodesTfly.listeners

import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Boat
import org.bukkit.entity.Camel
import org.bukkit.entity.Donkey
import org.bukkit.entity.Horse
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Llama
import org.bukkit.entity.Mule
import org.bukkit.entity.Pig
import org.bukkit.entity.Player
import org.bukkit.entity.Strider
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.oreo.nodesTfly.NodesTfly
import org.oreo.nodesTfly.java.GetNodesInfo
import org.spigotmc.event.entity.EntityMountEvent
import phonon.nodes.Nodes
import phonon.nodes.objects.Territory
import java.util.*


class PlayerChangeNode(private val plugin : NodesTfly) : Listener {

    private val debuffDuration = plugin.config.getInt("fly-abuse-debuff-duration")

    @EventHandler
    fun onPlayerEnterTown(e: PlayerMoveEvent) {

        val player = e.player

        if (!plugin.isPlayerAllowedToTfly(player) || !player.isFlying || player.isOp) {
            return
        }

        if (GetNodesInfo.isWarOn()){
            player.allowFlight = false
            player.sendMessage("§cWar is enabled")
            return
        }

        val world = Objects.requireNonNull(e.to).world

        val fromX = e.from.blockX
        val fromZ = e.from.blockZ

        val chunkFrom = world.getChunkAt(fromX,fromZ)

        val toX = e.to.blockX
        val toZ = e.to.blockZ

        val chunkTo = world.getChunkAt(toX,toZ)

        if (chunkFrom == chunkTo){
            return
        }

        unleashEntities(player)

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

    private fun unleashEntities(player: Player){

        for (entity in player.getNearbyEntities(5.0, 5.0, 5.0)) {

            if (entity !is LivingEntity){
                return
            }

            if (entity is Horse || entity is Mule || entity is Donkey || entity is Llama || entity is Pig ||
                entity is ArmorStand || entity is Strider || entity is Camel) {

                if (entity.isLeashed && entity.leashHolder == player) {
                    entity.setLeashHolder(null)

                    player.allowFlight = false

                    player.addPotionEffect(PotionEffect(PotionEffectType.POISON, debuffDuration * 20,1))
                    player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, debuffDuration * 20,3))
                    player.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, debuffDuration * 20, 87))

                    sendTitle(player, "§cYou thought bitch","§eNo abusing for u",
                        10, 65, 20)
                }
            }
        }
    }


    private fun sendTitle(player: Player, title: String?, subtitle: String?, fadeIn: Int, stay: Int, fadeOut: Int) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut)
    }

}