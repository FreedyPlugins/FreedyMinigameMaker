// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.commands;

import org.bukkit.inventory.PlayerInventory;
import java.util.List;
import freedy.freedyminigamemaker.MiniGame;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.command.CommandExecutor;

public class MinigameCommand implements CommandExecutor
{
    MiniGames miniGames;
    private final FreedyMinigameMaker plugin;
    
    public MinigameCommand(final FreedyMinigameMaker plugin) {
        this.plugin = plugin;
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c\ud50c\ub808\uc774\uc5b4\ub9cc \uc2e4\ud589\ud560 \uc218 \uc788\uc2b5\ub2c8\ub2e4");
            return true;
        }
        final Player player = (Player)sender;
        final Location playerLocation = player.getLocation();
        if (args.length != 0) {
            final String s = args[0];
            switch (s) {
                case "gui": {
                    if (args.length == 2) {
                        this.miniGames.getNoneGame().openInv(player, args[1]);
                        break;
                    }
                    if (args.length == 3) {
                        this.miniGames.getNoneGame().openInv(Bukkit.getPlayer(args[2]), args[1]);
                        break;
                    }
                    player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg gui <\uba54\ub274\uc774\ub984> [\ud50c\ub808\uc774\uc5b4]");
                    break;
                }
                case "join": {
                    if (args.length == 2) {
                        this.miniGames.get(args[1]).add(player);
                        break;
                    }
                    if (args.length == 3) {
                        this.miniGames.get(args[1]).add(Bukkit.getPlayer(args[2]));
                        break;
                    }
                    player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg join <\uac8c\uc784\uc774\ub984> [\ud50c\ub808\uc774\uc5b4]");
                    break;
                }
                case "joinAll": {
                    if (!player.hasPermission("freedyminigamemaker.admin")) {
                        break;
                    }
                    if (args.length == 2) {
                        for (final Player p : Bukkit.getOnlinePlayers()) {
                            this.miniGames.get(args[1]).add(p);
                        }
                        break;
                    }
                    player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg joinAll <\uac8c\uc784\uc774\ub984>");
                    break;
                }
                case "quitAll": {
                    if (!player.hasPermission("freedyminigamemaker.admin")) {
                        break;
                    }
                    if (args.length == 2) {
                        this.miniGames.get(args[1]).shutDown();
                        break;
                    }
                    player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg quitAll <\uac8c\uc784\uc774\ub984>");
                    break;
                }
                case "quit": {
                    if (this.miniGames.isJoined(player)) {
                        final MiniGame miniGame = this.miniGames.getJoined(player);
                        miniGame.remove(player);
                        miniGame.stop();
                        break;
                    }
                    player.sendMessage("§c\ucc38\uc5ec\uc911\uc778 \ubbf8\ub2c8\uac8c\uc784\uc774 \uc5c6\uc2b5\ub2c8\ub2e4");
                    break;
                }
                case "create": {
                    if (!player.hasPermission("freedyminigamemaker.admin")) {
                        player.sendMessage("§c\uad8c\ud55c\uc774 \uc5c6\uc2b5\ub2c8\ub2e4.");
                        break;
                    }
                    if (args.length != 6) {
                        player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg create <\uac8c\uc784\uc774\ub984> <\ucd5c\ub300\uc778\uc6d0> <\uc2dc\uc791\uc778\uc6d0> <\uc2dc\uc791\ub300\uae30\ucd08> <\uc885\ub8cc\ub300\uae30\ucd08>");
                        break;
                    }
                    if (args[1].length() <= 16) {
                        this.miniGames.get(args[1]).create(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                        this.miniGames.add(args[1]);
                        player.sendMessage("§6\uac8c\uc784 " + args[1] + "\uc774 \uc0dd\uc131\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                        break;
                    }
                    player.sendMessage("§\uac8c\uc784\uc774\ub984\uc740 \ucd5c\ub300 16\uc790 \uae4c\uc9c0 \ubc16\uc5d0 \uc548\ub429\ub2c8\ub2e4.");
                    break;
                }
                case "remove": {
                    if (!player.hasPermission("freedyminigamemaker.admin")) {
                        player.sendMessage("§c\uad8c\ud55c\uc774 \uc5c6\uc2b5\ub2c8\ub2e4.");
                        break;
                    }
                    if (args.length == 2) {
                        this.miniGames.getEditor(args[1]).remove(args[1]);
                        this.miniGames.remove(args[1]);
                        player.sendMessage("§6\uac8c\uc784 " + args[1] + "\uc774 \uc0ad\uc81c\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                        break;
                    }
                    player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg remove <\uac8c\uc784\uc774\ub984>");
                    break;
                }
                case "set": {
                    if (!player.hasPermission("freedyminigamemaker.admin")) {
                        player.sendMessage("§c\uad8c\ud55c\uc774 \uc5c6\uc2b5\ub2c8\ub2e4.");
                        break;
                    }
                    if (args.length > 2) {
                        final String s2 = args[2];
                        switch (s2) {
                            case "wait": {
                                this.miniGames.getEditor(args[1]).setLocation("waitLocation", playerLocation.getWorld().getName(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), playerLocation.getYaw(), playerLocation.getPitch());
                                player.sendMessage("§6\uc704\uce58\uac00 " + args[1] + " \uac8c\uc784\uc5d0 \ub300\uae30 \uc704\uce58\uc5d0 \uc800\uc7a5 \ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                break;
                            }
                            case "start": {
                                String team = "default";
                                if (args.length == 4) {
                                    team = args[3];
                                }
                                this.miniGames.getEditor(args[1]).addLocation(team + "StartLocation", playerLocation.getWorld().getName(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), playerLocation.getYaw(), playerLocation.getPitch());
                                player.sendMessage("§6\uc704\uce58\uac00 " + args[1] + " \uac8c\uc784\uc5d0 " + team + " \uc2dc\uc791\uc704\uce58\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                break;
                            }
                            case "end": {
                                this.miniGames.getEditor(args[1]).setLocation("endLocation", playerLocation.getWorld().getName(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), playerLocation.getYaw(), playerLocation.getPitch());
                                player.sendMessage("§6\uc704\uce58\uac00 " + args[1] + " \uac8c\uc784\uc5d0 \uc885\ub8cc \uc704\uce58\uc5d0 \uc800\uc7a5 \ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                break;
                            }
                            case "addBlock": {
                                if (args.length == 4) {
                                    final List<String> allowedBlocks = (List<String>)this.plugin.getConfig().getStringList("miniGames." + args[1] + ".allowedBlocks");
                                    allowedBlocks.add(args[3]);
                                    this.plugin.getConfig().set("miniGames." + args[1] + ".allowedBlocks", (Object)allowedBlocks);
                                    this.plugin.saveConfig();
                                    player.sendMessage("§6" + args[3] + "\uc774 " + args[1] + "\uac8c\uc784\uc5d0 \ub79c\ub364 \ube14\ub7ed \ub9ac\uc2a4\ud2b8\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                    break;
                                }
                                player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> addBlock <\ube14\ub7ed\uc774\ub984>");
                                player.sendMessage("\ube14\ub7ed\ubaa9\ub85d: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html");
                                break;
                            }
                            case "addDropItem": {
                                if (args.length == 5) {
                                    final List<String> dropItemList = (List<String>)this.plugin.getConfig().getStringList("miniGames." + args[1] + ".dropItems.dropList");
                                    dropItemList.add(args[3]);
                                    this.plugin.getConfig().set("miniGames." + args[1] + ".dropItems.dropList", (Object)dropItemList);
                                    this.plugin.getConfig().set("miniGames." + args[1] + ".dropItems.drop." + args[3], (Object)Integer.parseInt(args[4]));
                                    this.plugin.saveConfig();
                                    player.sendMessage("§6" + args[3] + " \ube14\ub7ed\uc774 " + args[1] + "\uac8c\uc784\uc5d0 " + args[4] + " \ub9cc\ud07c \ub5a8\uafd4\uc9d0\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                    break;
                                }
                                player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> addDropItem <\ube14\ub7ed\uc774\ub984> <\uc544\uc774\ud15c\uc218>");
                                player.sendMessage("\ube14\ub7ed\ubaa9\ub85d: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html");
                                break;
                            }
                            case "gameType": {
                                if (args.length == 4) {
                                    this.plugin.getConfig().set("miniGames." + args[1] + ".gameType", (Object)args[3]);
                                    this.plugin.saveConfig();
                                    player.sendMessage("§6" + args[3] + "\uc774 " + args[1] + "\uac8c\uc784\uc5d0 \ud0c0\uc785\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                    break;
                                }
                                player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> gameType <hideAndSeek|zombieMode>");
                                break;
                            }
                            case "teamMode": {
                                if (args.length == 4) {
                                    this.plugin.getConfig().set("miniGames." + args[1] + ".teamMode", (Object)Boolean.parseBoolean(args[3]));
                                    this.plugin.saveConfig();
                                    player.sendMessage("§6" + args[3] + "\uc774 " + args[1] + "\uac8c\uc784\uc5d0 \ud2f0\ubc0d\uc5ec\ubd80\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                    break;
                                }
                                player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> teamMode <true|false>");
                                break;
                            }
                            case "maxHealth": {
                                if (args.length >= 4) {
                                    String team;
                                    if (args.length == 5) {
                                        team = args[4];
                                    }
                                    else {
                                        team = "default";
                                    }
                                    this.plugin.getConfig().set("miniGames." + args[1] + "." + team + "TeamStartMaxHeart", (Object)Double.parseDouble(args[3]));
                                    this.plugin.saveConfig();
                                    player.sendMessage("§6\ucd5c\ub300\uccb4\ub825\uc774 " + args[1] + " \uac8c\uc784\uc5d0 " + team + "\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                    break;
                                }
                                player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> maxHealth <\uccb4\ub825> [\ud300\uc774\ub984]");
                                break;
                            }
                            case "worldBoarder": {
                                if (args.length >= 4) {
                                    final String s3 = args[3];
                                    switch (s3) {
                                        case "setLocation": {
                                            this.miniGames.getEditor(args[1]).setLocation("worldBoarder.location", playerLocation.getWorld().getName(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
                                            player.sendMessage("§6\uc704\uce58\uac00 " + args[1] + " \uc6d4\ub4dc \ubcf4\ub354 \uc911\uc2ec \uc704\uce58\uc5d0 \uc800\uc7a5 \ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                            break;
                                        }
                                        case "enable": {
                                            if (args.length == 5) {
                                                this.plugin.getConfig().set("miniGames." + args[1] + ".worldBoarder.enable", (Object)Boolean.parseBoolean(args[4]));
                                                this.plugin.saveConfig();
                                                player.sendMessage("§6" + args[4] + "\uc774 " + args[1] + "\uac8c\uc784\uc5d0 \ucc38\uc5ec\uc2dc \uc6d4\ub4dc \ubcf4\ub354 \uc124\uc815 \uc5ec\ubd80\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                                break;
                                            }
                                            player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> worldBoarder enable <true|false>");
                                            break;
                                        }
                                        case "sizePerPlayer": {
                                            if (args.length == 5) {
                                                this.plugin.getConfig().set("miniGames." + args[1] + ".worldBoarder.sizePerPlayer", (Object)Integer.parseInt(args[4]));
                                                this.plugin.saveConfig();
                                                player.sendMessage("§6" + args[4] + "\uc774 " + args[1] + "\uac8c\uc784\uc5d0 \ud50c\ub808\uc774\uc5b4\uc218\uc758\ube44\ub840\ud55c\uc6d4\ub4dc\ubcf4\ub354\ud06c\uae30\ubc30\uc218\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                                break;
                                            }
                                            player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> worldBoarder sizePerPlayer <\ud50c\ub808\uc774\uc5b4\uc218\uc758\ube44\ub840\ud55c\uc6d4\ub4dc\ubcf4\ub354\ud06c\uae30\ubc30\uc218>");
                                            break;
                                        }
                                        case "outDamage": {
                                            if (args.length == 5) {
                                                this.plugin.getConfig().set("miniGames." + args[1] + ".worldBoarder.outDamage", (Object)Integer.parseInt(args[4]));
                                                this.plugin.saveConfig();
                                                player.sendMessage("§6" + args[4] + "\uc774 " + args[1] + "\uac8c\uc784\uc5d0 \uc6d4\ub4dc \ubcf4\ub354 \ubc14\uae65 \ub370\ubbf8\uc9c0\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                                break;
                                            }
                                            player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> worldBoarder outDamage <\ud50c\ub808\uc774\uc5b4\uc218\uc758\ube44\ub840\ud55c\uc6d4\ub4dc\ubcf4\ub354\ud06c\uae30\ubc30\uc218>");
                                            break;
                                        }
                                        case "minSize": {
                                            if (args.length == 5) {
                                                this.plugin.getConfig().set("miniGames." + args[1] + ".worldBoarder.minSize", (Object)Integer.parseInt(args[4]));
                                                this.plugin.saveConfig();
                                                player.sendMessage("§6" + args[4] + "\uc774 " + args[1] + "\uac8c\uc784\uc5d0 \uc6d4\ub4dc \ubcf4\ub354 \ucd5c\uc18c \uc0ac\uc774\uc988\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                                break;
                                            }
                                            player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> worldBoarder minSize <\ucd5c\uc18c\uc0ac\uc774\uc988>");
                                            break;
                                        }
                                        case "speed": {
                                            if (args.length == 5) {
                                                this.plugin.getConfig().set("miniGames." + args[1] + ".worldBoarder.speed", (Object)Integer.parseInt(args[4]));
                                                this.plugin.saveConfig();
                                                player.sendMessage("§6" + args[4] + "\uc774 " + args[1] + "\uac8c\uc784\uc5d0 \uc6d4\ub4dc \ubcf4\ub354 \uac10\uc18c \uc18d\ub3c4\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                                break;
                                            }
                                            player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> worldBoarder minSize <\uac10\uc18c\uc18d\ub3c4>");
                                            break;
                                        }
                                    }
                                    break;
                                }
                                player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> worldBoarder <enable|sizePerPlayer|setLocation|outDamage|minSize|speed> ...");
                                break;
                            }
                            case "scoreBoard": {
                                if (args.length >= 4) {
                                    final String s4 = args[3];
                                    switch (s4) {
                                        case "enable": {
                                            if (args.length == 5) {
                                                this.plugin.getConfig().set("miniGames." + args[1] + ".scoreBoardEnable", (Object)Boolean.parseBoolean(args[4]));
                                                this.plugin.saveConfig();
                                                player.sendMessage("§6" + args[4] + "\uc774 " + args[1] + "\uac8c\uc784\uc5d0 \uc2a4\ucf54\uc5b4 \ubcf4\ub4dc \uc0ac\uc6a9 \uc5ec\ubd80\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                                break;
                                            }
                                            player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> scoreBoard enable <true|false>");
                                            break;
                                        }
                                        case "addMsg": {
                                            if (args.length == 5) {
                                                this.miniGames.getEditor(args[1]).addMessage("scoreBoard", ChatColor.translateAlternateColorCodes('&', args[4].replace("{spc}", " ")));
                                                player.sendMessage("§6\uba54\uc0c8\uc9c0\uac00 " + args[1] + " \uc2a4\ucf54\uc5b4\ubcf4\ub4dc \ubaa9\ub85d\uc5d0 \ucd94\uac00 \ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                                break;
                                            }
                                            player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> scoreBoard addMsg <\uba54\uc138\uc9c0>");
                                            player.sendMessage("§c\ucc38\uace0: <\uba85\ub839\uc904> \uc785\ub825\ub780\uc5d0\ub294 \uacf5\ubc31\uc744 {spc}\uc73c\ub85c \ub123\uc73c\uc138\uc694");
                                            break;
                                        }
                                        case "setTitle": {
                                            if (args.length == 5) {
                                                this.plugin.getConfig().set("miniGames." + args[1] + ".scoreBoardTitle", (Object)ChatColor.translateAlternateColorCodes('&', args[4].replace("{spc}", " ")));
                                                this.plugin.saveConfig();
                                                player.sendMessage("§6\uc2a4\ucf54\uc5b4\ubcf4\ub4dc \uc81c\ubaa9\uc774 " + args[1] + " \uac8c\uc784\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                                break;
                                            }
                                            player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> scoreBoard setTitle <\uba54\uc138\uc9c0>");
                                            player.sendMessage("§c\ucc38\uace0: <\uba85\ub839\uc904> \uc785\ub825\ub780\uc5d0\ub294 \uacf5\ubc31\uc744 {spc}\uc73c\ub85c \ub123\uc73c\uc138\uc694");
                                            break;
                                        }
                                    }
                                    break;
                                }
                                player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> scoreBoard <enable|addMsg|setTitle> ...");
                                break;
                            }
                            case "kit": {
                                if (args.length == 5) {
                                    final PlayerInventory inventory = player.getInventory();
                                    for (int i = 0; i < inventory.getSize(); ++i) {
                                        this.plugin.getConfig().set("kits." + args[4] + ".items." + i, (Object)inventory.getItem(i));
                                    }
                                    final List<String> inventoryList = (List<String>)this.plugin.getConfig().getStringList("kitList");
                                    if (!inventoryList.contains(args[4])) {
                                        inventoryList.add(args[4]);
                                    }
                                    this.plugin.getConfig().set("kitList", (Object)inventoryList);
                                    this.plugin.saveConfig();
                                    player.sendMessage("§6\ub2f9\uc2e0\uc758 \uc778\ubca4\ud1a0\ub9ac\uc5d0 \uc787\ub294 \uc544\uc774\ud15c\ub4e4\uc774 \ud0b7\uc5d0 " + args[4] + " \uc73c\ub85c \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                    break;
                                }
                                player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> kit create <\ud0b7\uc774\ub984>");
                                break;
                            }
                            case "item": {
                                if (args.length == 4) {
                                    final PlayerInventory inventory = player.getInventory();
                                    this.plugin.getConfig().set("items." + args[3], (Object)inventory.getItemInMainHand());
                                    final List<String> itemList = (List<String>)this.plugin.getConfig().getStringList("itemList");
                                    if (!itemList.contains(args[3])) {
                                        itemList.add(args[3]);
                                    }
                                    this.plugin.getConfig().set("itemList", (Object)itemList);
                                    this.plugin.saveConfig();
                                    player.sendMessage("§6\ub2f9\uc2e0\uc774 \ub4e4\uace0 \uc787\ub294 \uc544\uc774\ud15c\uc774 " + args[3] + " \uc73c\ub85c \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                    break;
                                }
                                player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> item <\ucee4\uc2a4\ud140\uc544\uc774\ud15c\uc774\ub984>");
                                break;
                            }
                            case "inv": {
                                if (args.length >= 4) {
                                    final String s5 = args[3];
                                    switch (s5) {
                                        case "addItem": {
                                            if (args.length == 7) {
                                                this.plugin.getConfig().set("inventories." + args[4] + ".items." + args[5], (Object)player.getInventory().getItemInMainHand());
                                                final List<String> cmdList = (List<String>)this.plugin.getConfig().getStringList("inventories." + args[4] + "." + args[5] + "Cmd");
                                                cmdList.add(ChatColor.translateAlternateColorCodes('&', args[6].replace("{spc}", " ")));
                                                this.plugin.getConfig().set("inventories." + args[4] + "." + args[5] + "Cmd", (Object)cmdList);
                                                this.plugin.saveConfig();
                                                player.sendMessage("§6\uba85\ub839\uc904\uc774 " + args[1] + "\uac8c\uc784\uc758 \uba54\ub274 " + args[4] + "\uc758 " + args[5] + "\ubc88\uc904\uc5d0 \uc190\uc5d0 \ub4e4\uace0 \uc788\ub358 \uc544\uc774\ud15c\uacfc \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                                break;
                                            }
                                            if (args.length == 6) {
                                                this.plugin.getConfig().set("inventories." + args[4] + ".items." + args[5], (Object)player.getInventory().getItemInMainHand());
                                                this.plugin.saveConfig();
                                                player.sendMessage("§6\uc544\uc774\ud15c\uc774 " + args[1] + "\uac8c\uc784\uc758 \uba54\ub274 " + args[4] + "\uc758 " + args[5] + "\ubc88\uc904\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                                break;
                                            }
                                            player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> inv addItem <\uba54\ub274\uc774\ub984> <\uc544\uc774\ud15c\uc704\uce58> [\uba85\ub839\uc904]");
                                            player.sendMessage("§c\ucc38\uace0: <\uba85\ub839\uc904> \uc785\ub825\ub780\uc5d0\ub294 \uacf5\ubc31\uc744 {spc}\uc73c\ub85c \ub123\uc73c\uc138\uc694");
                                            break;
                                        }
                                        case "create": {
                                            if (args.length == 7) {
                                                this.plugin.getConfig().set("inventories." + args[4] + ".title", (Object)ChatColor.translateAlternateColorCodes('&', args[6].replace("{spc}", " ")));
                                                this.plugin.getConfig().set("inventories." + args[4] + ".size", (Object)Integer.parseInt(args[5]));
                                                final List<String> inventoryList2 = (List<String>)this.plugin.getConfig().getStringList("inventoryList");
                                                if (!inventoryList2.contains(args[4])) {
                                                    inventoryList2.add(args[4]);
                                                }
                                                this.plugin.getConfig().set("inventoryList", (Object)inventoryList2);
                                                this.plugin.saveConfig();
                                                player.sendMessage("§6\uba54\ub274\uac00 " + args[1] + " \uac8c\uc784\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                                break;
                                            }
                                            player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> inv create <\uba54\ub274\uc774\ub984> <9|18|27|36|45|54> <\ud0c0\uc774\ud2c0>");
                                            break;
                                        }
                                        default: {
                                            player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> inv <addItem|create> ...");
                                            break;
                                        }
                                    }
                                    break;
                                }
                                player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> inv <addItem|create> ...");
                                break;
                            }
                            case "needClearInv": {
                                if (args.length == 4) {
                                    this.plugin.getConfig().set("miniGames." + args[1] + ".needClearInv", (Object)Boolean.parseBoolean(args[3]));
                                    this.plugin.saveConfig();
                                    player.sendMessage("§6" + args[3] + "\uc774 " + args[1] + "\uac8c\uc784\uc5d0 \ucc38\uc5ec\uc2dc \uc778\ubca4\uc744 \ube44\uc6cc\uc57c \ud558\ub294\uc9c0 \uc5ec\ubd80\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                    break;
                                }
                                player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> needClearInv <true|false>");
                                break;
                            }
                            case "startGameMode": {
                                if (args.length == 4) {
                                    this.plugin.getConfig().set("miniGames." + args[1] + ".startGameMode", (Object)Boolean.parseBoolean(args[3]));
                                    this.plugin.saveConfig();
                                    player.sendMessage("§6" + args[3] + "\uc774 " + args[1] + "\uac8c\uc784\uc5d0 \ucc38\uc5ec\uc2dc \uac8c\uc784 \ubaa8\ub4dc\ub97c \ubc14\uafb8\ub294\uc9c0 \uc5ec\ubd80\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                    break;
                                }
                                player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> startGameMode <true|false>");
                                break;
                            }
                            case "endGameMode": {
                                if (args.length == 4) {
                                    this.plugin.getConfig().set("miniGames." + args[1] + ".endGameMode", (Object)Boolean.parseBoolean(args[3]));
                                    this.plugin.saveConfig();
                                    player.sendMessage("§6" + args[3] + "\uc774 " + args[1] + "\uac8c\uc784\uc5d0 \ud1f4\uc7a5\uc2dc \uac8c\uc784 \ubaa8\ub4dc\ub97c \ubc14\uafb8\ub294\uc9c0 \uc5ec\ubd80\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                    break;
                                }
                                player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> endGameMode <true|false>");
                                break;
                            }
                            case "addCmd": {
                                if (args.length == 5) {
                                    final List<String> cmdList2 = (List<String>)this.plugin.getConfig().getStringList("miniGames." + args[1] + "." + args[3] + "Cmd");
                                    cmdList2.add(ChatColor.translateAlternateColorCodes('&', args[4].replace("{spc}", " ")));
                                    this.plugin.getConfig().set("miniGames." + args[1] + "." + args[3] + "Cmd", (Object)cmdList2);
                                    this.plugin.saveConfig();
                                    player.sendMessage("§6\uba85\ub839\uc904\uc774 " + args[1] + " \uac8c\uc784\uc5d0 " + args[3] + "\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                    break;
                                }
                                player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> addCmd <start|join|quit|conStart|conEnd|winner> <\uba85\ub839\uc904>");
                                player.sendMessage("§c\ucc38\uace0: <\uba85\ub839\uc904> \uc785\ub825\ub780\uc5d0\ub294 \uacf5\ubc31\uc744 {spc}\uc73c\ub85c \ub123\uc73c\uc138\uc694");
                                break;
                            }
                            case "msg": {
                                if (args.length == 5) {
                                    this.plugin.getConfig().set("miniGames." + args[1] + "." + args[3] + "Msg", (Object)ChatColor.translateAlternateColorCodes('&', args[4].replace("{spc}", " ")));
                                    this.plugin.saveConfig();
                                    player.sendMessage("§6\uba54\uc138\uc9c0\uac00 " + args[1] + " \uac8c\uc784\uc5d0 " + args[3] + "\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                    break;
                                }
                                player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> msg <join|quit|start|end|noWinnerEndMsg|redWinEnd|blueWinEnd|beZombie|extraDamage|dropItem|resistingDamage|startTimer|endTimer> <\uba54\uc138\uc9c0>");
                                player.sendMessage("§c\ucc38\uace0: <\uba85\ub839\uc904> \uc785\ub825\ub780\uc5d0\ub294 \uacf5\ubc31\uc744 {spc}\uc73c\ub85c \ub123\uc73c\uc138\uc694");
                                break;
                            }
                            case "loc": {
                                if (args.length == 4) {
                                    this.miniGames.getEditor(args[1]).setLocation(args[3] + "Location", playerLocation.getWorld().getName(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), playerLocation.getYaw(), playerLocation.getPitch());
                                    player.sendMessage("§6\uc704\uce58\uac00 " + args[1] + " \ucee4\uc2a4\ud140 \uc704\uce58\uc5d0 \uc800\uc7a5 \ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                    break;
                                }
                                player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> loc <customName>");
                                break;
                            }
                            default: {
                                player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> <wait|start|end|addDropItem|gameType|teamMode|maxHealth|worldBoarder|timePerPlayer|worldBoarder|scoreBoard|inv|needClearInv|startGameMode|endGameMode|addCmd|msg|loc> ...");
                                break;
                            }
                        }
                        break;
                    }
                    player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg set <\uac8c\uc784\uc774\ub984> <wait|start|end|addBlock|addDropItem|gameType|teamMode|maxHealth|startGameMode|quitGameMode|timePerPlayer|worldBoarder|scoreBoard|addCmd|msg|loc> ...");
                    break;
                }
                case "list": {
                    player.sendMessage("§6\uac8c\uc784 \ubaa9\ub85d: " + this.plugin.getConfig().getStringList("gameList").toString());
                    break;
                }
                case "reload": {
                    if (player.hasPermission("freedyminigamemaker.admin")) {
                        this.plugin.reloadConfig();
                        player.sendMessage("§a\ub9ac\ub85c\ub4dc\uac00 \uc644\ub8cc\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                        break;
                    }
                    player.sendMessage("§c\uad8c\ud55c\uc774 \uc5c6\uc2b5\ub2c8\ub2e4.");
                    break;
                }
                default: {
                    player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg <gui|join|quit|create|remove|set|list|reload> ...");
                    break;
                }
            }
        }
        else {
            player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fmg <gui|join|quit|create|remove|set|list|reload> ...");
        }
        return true;
    }
}
