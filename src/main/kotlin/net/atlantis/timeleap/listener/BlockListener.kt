package net.atlantis.timeleap.listener

import io.reactivex.Observable
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
import java.util.concurrent.TimeUnit

class BlockListener(private val plugin: JavaPlugin) : Listener {
    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        val previousBlockType = event.block.type
        val previousBlockData = event.block.blockData.clone()
        Observable.interval(1, TimeUnit.SECONDS)
                .take(60)
                .doOnComplete {
                    object : BukkitRunnable() {
                        override fun run() {
                            setBlock(event.block, previousBlockType, previousBlockData)
                        }
                    }.runTaskLater(plugin, 1)
                }
                .subscribe()
    }

    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        val previousBlockType = event.blockReplacedState.type
        val previousBlockData = event.blockReplacedState.blockData.clone()
        Observable.interval(1, TimeUnit.SECONDS)
                .take(60)
                .doOnComplete {
                    object : BukkitRunnable() {
                        override fun run() {
                            setBlock(event.block, previousBlockType, previousBlockData)
                        }
                    }.runTaskLater(plugin, 1)
                }
                .subscribe()
    }

    @EventHandler
    fun onDeath(event: EntityDeathEvent) {
        val entity = event.entity
        if (entity !is Player) {
            Observable.interval(1, TimeUnit.SECONDS)
                    .take(60)
                    .doOnComplete {
                        object : BukkitRunnable() {
                            override fun run() {
                                val location = entity.location
                                location.world?.spawnEntity(location, entity.type)
                            }
                        }.runTaskLater(plugin, 1)
                    }
                    .subscribe()
        }
    }

    private fun setBlock(block: Block, previousBlockType: Material, previousBlockData: BlockData) {
        block.type = previousBlockType
        block.blockData = previousBlockData
    }
}