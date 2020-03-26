package commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


object EchoCmd : CommandExecutor {
	override fun onCommand(sender: CommandSender, cmd: Command, lbl: String, args: Array<out String>): Boolean {
		if(sender is Player){
			sender.sendMessage("Hello")
		}
		return true
	}
}