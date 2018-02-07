package pacifistgroup.anticommandtab;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class Anticommandtab extends JavaPlugin implements Listener {

    String prefix = "§f§l[§6§lPacifistGroup§f§l]§bAntiCommandTab: ";

    ProtocolManager protocolManager;

    FileConfiguration config;

    List<String> plugins = new ArrayList<String>();
    List<String> version = new ArrayList<String>();
    List<String> about = new ArrayList<String>();
    List<String> question = new ArrayList<String>();
    List<String> help = new ArrayList<String>();

    Boolean BlockPlugins, BlockVersion, BlockAbout, BlockQuestionMark, BlockHelp;

    String pluginsDeny, versionDeny, aboutDeny, qmDeny, helpDeny;

    public void onEnable() {

        config = getConfig();

        saveDefaultConfig();

        plugins.add("pl");
        plugins.add("bukkit:pl");
        plugins.add("plugins");
        plugins.add("bukkit:plugins");
        version.add("ver");
        plugins.add("bukkit:ver");
        version.add("version");
        plugins.add("bukkit:version");
        about.add("about");
        plugins.add("bukkit:about");
        question.add("?");
        plugins.add("bukkit:?");
        help.add("help");
        plugins.add("bukkit:help");

        BlockPlugins = config.getBoolean("BlockPlugins");
        BlockVersion = config.getBoolean("BlockVersion");
        BlockAbout = config.getBoolean("BlockAbout");
        BlockQuestionMark = config.getBoolean("BlockQuestionMark");
        BlockHelp = config.getBoolean("BlockHelp");

        pluginsDeny = config.getString("Plugins").replaceAll("&", "§");
        versionDeny = config.getString("Version").replaceAll("&", "§");
        aboutDeny = config.getString("About").replaceAll("&", "§");
        qmDeny = config.getString("QuestionMark").replaceAll("&", "§");
        helpDeny = config.getString("Help").replaceAll("&", "§");

        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.protocolManager.addPacketListener(new PacketAdapter(this,
                ListenerPriority.NORMAL, PacketType.Play.Client.TAB_COMPLETE) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE)
                    try {
                        if (event.getPlayer().hasPermission("pact.bypass"))
                            return;
                        PacketContainer packet = event.getPacket();
                        String message = (String) packet
                                .getSpecificModifier(String.class).read(0)
                                .toLowerCase();

                        if ((message.startsWith("/") && !message.contains(" "))
                                || (message.startsWith("/" + plugins) && !message.contains(" "))
                                || (message.startsWith("/" + version) && !message.contains(" "))
                                || (message.startsWith("/" + about) && !message.contains(" "))
                                || (message.startsWith("/" + question) && !message.contains(" "))
                                || (message.startsWith("/" + help) && !message.contains(" "))){
                            event.setCancelled(true);
                        }
                    } catch (FieldAccessException e) {
                        Anticommandtab.this.getLogger().log(Level.SEVERE,
                                "Couldn't access field.", e);
                    }
            }
        });
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("pact")) {
            if (sender.hasPermission("act.reload")) {
                sender.sendMessage(prefix + "§aReloaded Configuration File");
                this.reloadConfig();
            } else {
                sender.sendMessage(prefix + "§cdon't have permission");
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();

        String[] msg = event.getMessage().split(" ");

        if (!player.hasPermission("pact.bypass")) {

            if (BlockPlugins) {
                for (String Loop : plugins) {
                    if (msg[0].equalsIgnoreCase("/" + Loop)) {
                        player.sendMessage(pluginsDeny.replaceAll("%player",
                                player.getName()));
                        event.setCancelled(true);
                    }
                }
            }

            if (BlockVersion) {
                for (String Loop : version) {
                    if (msg[0].equalsIgnoreCase("/" + Loop)) {
                        player.sendMessage(versionDeny.replaceAll("%player",
                                player.getName()));
                        event.setCancelled(true);
                    }
                }
            }

            if (BlockAbout) {
                for (String Loop : about) {
                    if (msg[0].equalsIgnoreCase("/" + Loop)) {
                        player.sendMessage(aboutDeny.replaceAll("%player",
                                player.getName()));
                        event.setCancelled(true);
                    }
                }
            }

            if (BlockQuestionMark) {
                for (String Loop : question) {
                    if (msg[0].equalsIgnoreCase("/" + Loop)) {
                        player.sendMessage(qmDeny.replaceAll("%player",
                                player.getName()));
                        event.setCancelled(true);
                    }
                }
            }

            if (BlockHelp) {
                for (String Loop : help) {
                    if (msg[0].equalsIgnoreCase("/" + Loop)) {
                        player.sendMessage(helpDeny.replaceAll("%player",
                                player.getName()));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
