package events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.ChatColor
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.entity.Player

class ChatEvent : Listener {
	@EventHandler
	fun chatEvent(e: AsyncPlayerChatEvent){
		if( e.player.isOp() )
			e.setFormat("${ChatColor.RED}%s${ChatColor.RESET}: %s")
		else
			e.setFormat("${ChatColor.GRAY}%s${ChatColor.RESET}: %s")
	}
}