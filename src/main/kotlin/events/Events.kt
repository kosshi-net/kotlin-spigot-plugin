package events

import org.bukkit.entity.Player
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.ChatColor

import org.bukkit.event.server.ServerListPingEvent

import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.entity.EntityType

import org.bukkit.util.Vector

import kotlin.random.Random
import org.bukkit.Material
import org.bukkit.GameMode
import org.bukkit.inventory.ItemStack

import org.bukkit.event.block.BlockBurnEvent
import org.bukkit.event.block.BlockIgniteEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockFadeEvent




class ChatDriver : Listener {
	@EventHandler
	fun playerJoin(event: PlayerJoinEvent) {
		val player: Player = event.player
		event.joinMessage = "${player.displayName} joined"
	}

	@EventHandler
	fun chatEvent(e: AsyncPlayerChatEvent){
		if( e.player.isOp() )
			e.setFormat("${ChatColor.RED}%s${ChatColor.RESET}: %s")
		else
			e.setFormat("${ChatColor.GRAY}%s${ChatColor.RESET}: %s")
	}

	@EventHandler
	fun pingEvent(e: ServerListPingEvent){
		e.setMotd(
			"${ChatColor.GOLD}${ChatColor.ITALIC}Rautasaari ${ChatColor.RESET}${ChatColor.GRAY}2020" +
			"\n${ChatColor.DARK_GRAY}Suomen toisiksi ainut Minecraft-palvelin"
		)
	}
}



class MobDriver : Listener {

	@EventHandler
	fun spawnEvent(e: CreatureSpawnEvent ) {

		if( e.getEntityType()  == EntityType.ENDERMAN 
		&&  e.getSpawnReason() == SpawnReason.NATURAL 
		){
			e.setCancelled(true)
		}



	}
}



class DestructionDriver : Listener {


	@EventHandler
	fun blockBreakEvent(e: BlockBreakEvent ) {
		var block = e.getBlock();
		var world = block.getWorld();
		var player = e.getPlayer();

		if( player.getGameMode() != GameMode.SURVIVAL ) return;

		if( block.getType() == Material.IRON_ORE ){
			block.setType( Material.AIR );
			world.dropItemNaturally( block.getLocation(), ItemStack(Material.IRON_NUGGET, 1)  )
		}

		if( block.getType() == Material.GOLD_ORE ){
			block.setType( Material.AIR );
			world.dropItemNaturally( block.getLocation(), ItemStack(Material.GOLD_NUGGET, 1)  )
		}

	}


	@EventHandler
	fun blockFadeEvent(e: BlockFadeEvent ) {
		var block = e.getBlock();
		var world = block.getWorld();

		if( block.getType() == Material.FIRE ){
			var l1 = e.getBlock().getLocation();

			l1.y -= 1;
			l1.x -= 1;
			l1.z -= 1;

			for( x in (0..2) )
			for( z in (0..2) ){
				

				val l2 = l1.clone();
				l2.x += x;
				l2.z += z;

				var b2 = world.getBlockAt(l2);
				if(b2.getType() != Material.GRASS_BLOCK) continue;
				b2.setType(Material.DIRT);
				if( Random.nextFloat() < 0.9 ) continue;
				l2.y += 1;
				b2 = world.getBlockAt(l2);
				if(b2.getType() != Material.AIR) continue;
				b2.setType( Material.FIRE );
				
			}

		}

	}

	@EventHandler
	fun blockIgniteEvent(e: BlockIgniteEvent ) {
		

		var world = e.getBlock().getWorld();
		var loc1 = e.getBlock().getLocation();
		loc1.y -= 1;

		var block = world.getBlockAt(loc1);

		//Bukkit.broadcastMessage("Ignited ${block.getType()}")
		if(block.getType() == Material.GRASS_BLOCK){
			block.setType(Material.DIRT);
		}

	}


	@EventHandler
	fun blockBurnEvent(e: BlockBurnEvent ) {
		var block = e.getBlock();
		var world = block.getWorld();

				
		var loc = block.getLocation().toVector()

		if( Random.nextFloat() > 0.8 ) return;

		if( Random.nextFloat() > 0.5 ) {
			world.spawnFallingBlock(
				loc.toLocation(world), 
				block.getType(),
				0
			).setDropItem(false)
		} else {
			world.spawnFallingBlock(
				loc.toLocation(world), 
				Material.FIRE,
				0
			)	
		}

		var l1 = block.getLocation();
		l1.x -= 1;
		l1.y -= 1;
		l1.z -= 1;

		for( x in (0..2) )
		for( y in (0..2) )
		for( z in (0..2) ){
			val l2 = l1.clone();
			l2.x += x;
			l2.x += y;
			l2.z += z;
			var b2 = world.getBlockAt(l2);
			if(b2.getType() == Material.AIR) continue;
			if( Random.nextFloat() > 0.8 ) continue;

			world.spawnFallingBlock(
				l2, 
				b2.getType(),
				0
			).setDropItem(false);

			b2.setType(Material.AIR);



		}
		// Collapse nearby


	}

	@EventHandler
	fun explosionEvent(e: EntityExplodeEvent) {
		//Bukkit.broadcastMessage("Kaboom!!")

		e.setYield(0.0f);

		var blocks = e.blockList();
		var world = e.getEntity().getWorld()

		var origin = e.getEntity().getLocation().toVector()

		for(block in blocks){
			if(block.getType() == Material.TNT) continue
			if(block.getType() == Material.GRASS_BLOCK)
				block.setType(Material.DIRT);

			var loc= block.getLocation().toVector()

			var vec = loc.clone()
			vec.subtract(origin).normalize()
			vec.multiply(0.2 + Random.nextFloat()*0.5)

			vec.y += 0.8

			//var entity = world.spawnEntity(loc, EntityType.FALLING_BLOCK)
			var entity = world.spawnFallingBlock(
				loc.toLocation(world), 
				block.getType(),
				0
			)
			entity.setDropItem(false)
			entity.setVelocity( vec )
			//Bukkit.broadcastMessage("${block.getType()} ${loc.x} ${loc.y} ${loc.z} blown up, created entity ${entity}")

		}
		for( i in (0..20) ){
			var vel = Vector(
				Random.nextFloat() - 0.5,
				Random.nextFloat() - 0.5,
				Random.nextFloat() - 0.5
			);
			vel.normalize();
			vel.multiply(0.5 + Random.nextFloat()*0.5)
			var entity = world.spawnFallingBlock(
				origin.toLocation(world), 
				Material.FIRE,
				0
			)	
			entity.setVelocity( vel )
		}
	}
}

