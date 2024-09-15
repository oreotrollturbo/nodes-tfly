package org.oreo.nodesTfly.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.oreo.nodesTfly.NodesTfly
import phonon.nodes.Nodes

class TflyCommand(private val plugin : NodesTfly) : CommandExecutor,TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, p2: String, args: Array<out String>?): Boolean {

        if (args?.isNotEmpty() == true && sender.isOp) {
            when (args[0]) {
                "enable" ->{
                    plugin.tflyEnabled = true;
                    sender.sendMessage("§2Tfly has been enabled for everyone :)")
                }
                "disable" ->{
                    plugin.tflyEnabled = false;
                    sender.sendMessage("§2Tfly has been disabled for everyone :(")
                }
                else -> sender.sendMessage("§cSubcommand not recognised please use /tfly <enable|disable>")
            }
            return true
        }

        if (sender !is Player) {
            sender.sendMessage("§cConsole cant use this command")
            return true
        }

        if (!plugin.isPlayerAllowedToTfly(sender)) {
            sender.sendMessage("§cYou are not allowed to use this command")
            return true
        }

        if (!plugin.tflyEnabled){
            sender.sendMessage("§cTfly is disabled by staff :(")
            return true
        }

        if (!isInTheirTown(sender)) {
            sender.sendMessage("§cYou are not in your town")
            return true
        }

        sender.allowFlight = !sender.allowFlight

        if (sender.allowFlight){
            sender.sendMessage("§2Town fly is enabled")
        } else {
            sender.sendMessage("§2Town fly is disabled")
        }

        return true
    }

    private fun isInTheirTown(player: Player) : Boolean{

        val townTerritory =  Nodes.getResident(player)?.town?.home

        val playerTerritory = Nodes.getTerritoryFromChunk(player.chunk)?.id

        return townTerritory == playerTerritory
    }

    override fun onTabComplete(
        commandSender : CommandSender,
        p1: Command,
        p2: String,
        p3: Array<out String>?
    ): List<String> {
        val completions: ArrayList<String> = ArrayList()

        if (commandSender !is Player) {
            return emptyList()
        }

        val sender = commandSender

        if (!sender.isOp) {
            return emptyList()
        }

        completions.add("enable")
        completions.add("disable")

        return completions
    }
}