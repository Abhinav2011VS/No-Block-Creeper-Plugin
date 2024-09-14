package net.abhinav.nbcp;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.entity.Creeper;

public class NoBlockCreeperPlugin extends JavaPlugin implements Listener {

    private boolean visualEffect;
    private double explosionPower;
    private boolean preventCreeperExplosions;

    @Override
    public void onEnable() {
        saveDefaultConfig();  // Create config file if it doesn't exist
        loadConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("NoBlockCreeperPlugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("NoBlockCreeperPlugin disabled!");
    }

    private void loadConfig() {
        FileConfiguration config = getConfig();
        visualEffect = config.getBoolean("visualEffect", true);
        explosionPower = config.getDouble("explosionPower", 4.0);
        preventCreeperExplosions = config.getBoolean("preventCreeperExplosions", true);
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        if (event.getEntity() instanceof Creeper && preventCreeperExplosions) {
            handleExplosion(event);
        }
    }

    private void handleExplosion(ExplosionPrimeEvent event) {
        event.setCancelled(true);

        // Set explosion power if needed
        event.setRadius((float) explosionPower);

        if (visualEffect) {
            event.getEntity().getWorld().spawnParticle(Particle.EXPLOSION, event.getEntity().getLocation(), 1);
        }
    }
}
