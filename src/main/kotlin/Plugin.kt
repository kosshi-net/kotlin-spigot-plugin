import events.ChatDriver
import events.DestructionDriver
import events.MobDriver

import org.bukkit.scheduler.BukkitScheduler
import commands.EchoCmd
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.command.CommandExecutor
import org.bukkit.entity.Player


class Plugin : JavaPlugin() {

	override fun onEnable() {

		var scheduler = getServer().getScheduler();

		server.pluginManager.registerEvents(ChatDriver(), this)
		server.pluginManager.registerEvents(DestructionDriver(), this)
		server.pluginManager.registerEvents(MobDriver(), this)

		getCommand("echo")?.setExecutor( CommandExecutor {
			sender, command, label, args ->
			sender.sendMessage("§bHello!")
			true
		})

		getCommand("nick")?.setExecutor( CommandExecutor {
			sender, command, label, args ->
			if(sender is Player)
				sender.setDisplayName(args[0])
			true
		})

		scheduler.scheduleSyncRepeatingTask(this, Runnable () {
			var world = Bukkit.getServer().getWorld("World");
			world?.setFullTime( world.getFullTime()+1 )
		}, 0L, 3L);


		Bukkit.broadcastMessage("Plugin Reloaded")
	}
}