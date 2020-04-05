package net.atlantis.timeleap

import net.atlantis.timeleap.listener.BlockListener
import org.bukkit.plugin.java.JavaPlugin

class TimeLeap : JavaPlugin() {
    override fun onEnable() {
        server.pluginManager.registerEvents(BlockListener(this), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}