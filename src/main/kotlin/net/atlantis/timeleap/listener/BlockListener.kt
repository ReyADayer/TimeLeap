package net.atlantis.timeleap.listener

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class BlockListener(private val plugin: JavaPlugin) : Listener {
    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        val previousBlockType = event.block.type
        val previousBlockData = event.block.blockData.clone()
        object : BukkitRunnable() {
            override fun run() {
                setBlock(event.block, previousBlockType, previousBlockData)
            }
        }.runTaskLater(plugin, 1200L)
    }

    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        val previousBlockType = event.blockReplacedState.type
        val previousBlockData = event.blockReplacedState.blockData.clone()
        object : BukkitRunnable() {
            override fun run() {
                setBlock(event.block, previousBlockType, previousBlockData)
            }
        }.runTaskLater(plugin, 1200L)
    }

    @EventHandler
    fun onDeath(event: EntityDeathEvent) {
        val entity = event.entity
        if (entity !is Player) {
            object : BukkitRunnable() {
                override fun run() {
                    val location = entity.location
                    location.world?.spawnEntity(location, entity.type)
                }
            }.runTaskLater(plugin, 1200L)
        }
    }

    private fun setBlock(block: Block, previousBlockType: Material, previousBlockData: BlockData) {
        block.type = previousBlockType
        block.blockData = previousBlockData
    }
}