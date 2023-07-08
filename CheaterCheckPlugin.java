import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CheaterCheckPlugin extends JavaPlugin implements Listener, CommandExecutor {
    private List<Player> cheaters;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("CheaterCheck").setExecutor(this);
        getCommand("uncheat").setExecutor(this);
        cheaters = new ArrayList<>();
    }

    @Override
    public void onDisable() {
        cheaters.clear();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("CheaterCheck")) {
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /CheaterCheck <player>");
                return true;
            }

            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }

            cheaters.add(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0, false, false));
            player.sendMessage(ChatColor.RED + "Join Discord VC");
            sender.sendMessage(ChatColor.GREEN + "Player " + player.getName() + " has been marked as a cheater.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("uncheat")) {
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /uncheat <player>");
                return true;
            }

            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }

            cheaters.remove(player);
            player.removePotionEffect(PotionEffectType.BLINDNESS);
            player.sendMessage(ChatColor.GREEN + "You are no longer marked as a cheater.");
            sender.sendMessage(ChatColor.GREEN + "Player " + player.getName() + " has been uncheated.");
            return true;
        }

        return false;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (cheaters.contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (cheaters.contains(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0, false, false));
            player.sendMessage(ChatColor.RED + "Join Discord VC");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        cheaters.remove(player);
    }
}
