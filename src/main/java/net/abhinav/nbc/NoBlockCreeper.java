package net.abhinav.nbc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Creeper;

public class NoBlockCreeper extends JavaPlugin implements Listener {

    private boolean preventGrief;
    private boolean visualEffect;
    private float explosionPower;

    @Override
    public void onEnable() {
        // Load the configuration from config.yml
        saveDefaultConfig();
        loadConfig();

        // Register the event listener
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("NoBlockCreeperPlugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("NoBlockCreeperPlugin disabled.");
    }

    private void loadConfig() {
        // Load values from config.yml
        preventGrief = getConfig().getBoolean("preventCreeperExplosions", true);
        visualEffect = getConfig().getBoolean("visualEffect", true);
        explosionPower = (float) getConfig().getDouble("explosionPower", 4.0);
    }

    private void saveConfigSetting() {
        // Save the current values to config.yml
        getConfig().set("preventCreeperExplosions", preventGrief);
        getConfig().set("visualEffect", visualEffect);
        getConfig().set("explosionPower", explosionPower);
        saveConfig();  // Save the file
    }

    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Creeper) {
            if (preventGrief) {
                // Prevent block destruction by clearing the block list
                event.blockList().clear();
            }
            if (visualEffect) {
                // Optionally show the explosion effect (without destroying blocks)
                event.setCancelled(false); // Ensure the explosion itself is still processed.
                event.getEntity().getWorld().createExplosion(event.getLocation(), explosionPower, false, false);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("togglecreepergrief")) {
            // Toggle the preventGrief setting
            preventGrief = !preventGrief;

            // Save the updated config setting
            saveConfigSetting();

            // Inform the sender of the new state
            if (sender instanceof org.bukkit.entity.Player) {
                sender.sendMessage("Creeper grief prevention is now " + (preventGrief ? "enabled" : "disabled"));
            } else {
                getLogger().info("Creeper grief prevention is now " + (preventGrief ? "enabled" : "disabled"));
            }

            return true;
        }
        return false;
    }
}
