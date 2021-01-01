package freedy.freedyminigamemaker.commands;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            sender.sendMessage("§cOnly players can run");
            return true;
        }
        final Player player = (Player)sender;
        final Location playerLocation = player.getLocation();
        if (args.length != 0) {
            final String s = args[0];
            switch (s) {
                case "join": {
                    if (args.length == 2) {
                        if (!player.hasPermission("freedyminigamemaker.admin")
                                && !player.hasPermission("freedyminigamemaker.join")) {
                            break;
                        }
                        this.miniGames.get(args[1]).add(player);
                        break;
                    }
                    if (args.length == 3) {
                        if (!player.hasPermission("freedyminigamemaker.admin")) {
                            break;
                        }
                        this.miniGames.get(args[1]).add(Bukkit.getPlayer(args[2]));
                        break;
                    }
                    player.sendMessage("§cHow to Use: /fmg join <gameName> [player]");
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
                    player.sendMessage("§cHow to Use: /fmg joinAll <gameName>");
                    break;
                }
                case "quitAll": {
                    if (!player.hasPermission("freedyminigamemaker.admin")) {
                        break;
                    }
                    if (args.length == 2) {
                        this.miniGames.get(args[1]).removeAll();
                        break;
                    }
                    player.sendMessage("§cHow to Use: /fmg quitAll <gameName>");
                    break;
                }
                case "quitAllGames": {
                    if (!player.hasPermission("freedyminigamemaker.admin")) {
                        break;
                    }
                    for (MiniGame miniGame : FreedyMinigameMaker.miniGames.miniGames.values()) {
                        while(miniGame.playerList.size() != 0) {
                            Player p = miniGame.playerList.get(0);
                            miniGame.playerList.remove(0);
                            miniGame.kick(p);
                            miniGame.stop();
                        }
                        miniGame.stop();
                        miniGames.reset();
                    }

                    player.sendMessage("§aAll mini-games have been stopped.");
                    break;
                }
                case "quit": {
                    if (!player.hasPermission("freedyminigamemaker.admin")
                            && !player.hasPermission("freedyminigamemaker.quit")) {
                        break;
                    }

                    if (this.miniGames.isJoined(player)) {
                        final MiniGame miniGame = this.miniGames.getJoined(player);
                        miniGame.remove(player);
                        miniGame.stop();
                        break;
                    }
                    player.sendMessage(miniGames.getNoneGame().getSettings("noGameToQuit"));
                    break;
                }
                case "create": {
                    if (!player.hasPermission("freedyminigamemaker.admin")) {
                        player.sendMessage("§cYou do not have permission.");
                        break;
                    }
                    if (args.length != 5) {
                        player.sendMessage("§cHow to Use: /fmg create <gameName> <maximumPeopleNumber> <startingPeopleNumber> <startWaitingSeconds>");
                        break;
                    }
                    if (args[1].length() <= 16) {
                        this.miniGames.get(args[1]).create(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                        this.miniGames.add(args[1]);
                        player.sendMessage("§6Game " + args[1] +" created successfully");
                        break;
                    }
                    player.sendMessage("§cThe game name can only be up to 16 characters long.");
                    break;
                }
                case "remove": {
                    if (!player.hasPermission("freedyminigamemaker.admin")) {
                        player.sendMessage("§cYou do not have permission.");
                        break;
                    }
                    if (args.length == 2) {
                        this.miniGames.getEditor(args[1]).remove(args[1]);
                        this.miniGames.remove(args[1]);
                        player.sendMessage("§6Game " + args[1] + "has been deleted successfully");
                        break;
                    }
                    player.sendMessage("§cHow to Use: /fmg remove <gameName>");
                    break;
                }
                case "set": {
                    if (!player.hasPermission("freedyminigamemaker.admin")) {
                        player.sendMessage("§cYou do not have permission.");
                        break;
                    }
                    if (args.length > 2) {
                        final String s2 = args[2];
                        switch (s2) {
                            case "wait": {
                                this.miniGames.getEditor(args[1]).setLocation("waitLocation", playerLocation.getWorld().getName(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), playerLocation.getYaw(), playerLocation.getPitch());
                                player.sendMessage("§6Your location has been saved to the " + args[1] + " game's wait location.");
                                break;
                            }
                            case "start": {
                                this.miniGames.getEditor(args[1]).setLocation("startLocation", playerLocation.getWorld().getName(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), playerLocation.getYaw(), playerLocation.getPitch());
                                player.sendMessage("§6Your location has been saved to the " + args[1] + " game's start location.");
                                break;
                            }
                            case "end": {
                                this.miniGames.getEditor(args[1]).setLocation("endLocation", playerLocation.getWorld().getName(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), playerLocation.getYaw(), playerLocation.getPitch());
                                player.sendMessage("§6Your location has been saved to the " + args[1] + " game's end location.");
                                break;
                            }
                            case "gameType": {
                                if (args.length == 4) {
                                    this.plugin.getConfig().set("miniGames." + args[1] + ".gameType", args[3]);
                                    this.plugin.saveConfig();
                                    player.sendMessage("§6" + args[3] + "gameType was saved as build in the " + args[1] + " game.");
                                    break;
                                }
                                player.sendMessage("§cHow to Use: /fmg set <gameName> gameType <death|build>");
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
                                                this.plugin.getConfig().set("miniGames." + args[1] + ".worldBoarder.enable", Boolean.parseBoolean(args[4]));
                                                this.plugin.saveConfig();
                                                player.sendMessage("§6" + args[4] + "\uc774 " + args[1] + "\uac8c\uc784\uc5d0 \ucc38\uc5ec\uc2dc \uc6d4\ub4dc \ubcf4\ub354 \uc124\uc815 \uc5ec\ubd80\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                                break;
                                            }
                                            player.sendMessage("§cHow to Use: /fmg set <gameName> worldBoarder enable <true|false>");
                                            break;
                                        }
                                        case "sizePerPlayer": {
                                            if (args.length == 5) {
                                                this.plugin.getConfig().set("miniGames." + args[1] + ".worldBoarder.sizePerPlayer", Integer.parseInt(args[4]));
                                                this.plugin.saveConfig();
                                                player.sendMessage("§6" + args[4] + "\uc774 " + args[1] + "\uac8c\uc784\uc5d0 \ud50c\ub808\uc774\uc5b4\uc218\uc758\ube44\ub840\ud55c\uc6d4\ub4dc\ubcf4\ub354\ud06c\uae30\ubc30\uc218\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                                break;
                                            }
                                            player.sendMessage("§cHow to Use: /fmg set <gameName> worldBoarder sizePerPlayer <\ud50c\ub808\uc774\uc5b4\uc218\uc758\ube44\ub840\ud55c\uc6d4\ub4dc\ubcf4\ub354\ud06c\uae30\ubc30\uc218>");
                                            break;
                                        }
                                        case "outDamage": {
                                            if (args.length == 5) {
                                                this.plugin.getConfig().set("miniGames." + args[1] + ".worldBoarder.outDamage", Integer.parseInt(args[4]));
                                                this.plugin.saveConfig();
                                                player.sendMessage("§6" + args[4] + "\uc774 " + args[1] + "\uac8c\uc784\uc5d0 \uc6d4\ub4dc \ubcf4\ub354 \ubc14\uae65 \ub370\ubbf8\uc9c0\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                                break;
                                            }
                                            player.sendMessage("§cHow to Use: /fmg set <gameName> worldBoarder outDamage <\ud50c\ub808\uc774\uc5b4\uc218\uc758\ube44\ub840\ud55c\uc6d4\ub4dc\ubcf4\ub354\ud06c\uae30\ubc30\uc218>");
                                            break;
                                        }
                                        case "minSize": {
                                            if (args.length == 5) {
                                                this.plugin.getConfig().set("miniGames." + args[1] + ".worldBoarder.minSize", Integer.parseInt(args[4]));
                                                this.plugin.saveConfig();
                                                player.sendMessage("§6" + args[4] + "\uc774 " + args[1] + "\uac8c\uc784\uc5d0 \uc6d4\ub4dc \ubcf4\ub354 \ucd5c\uc18c \uc0ac\uc774\uc988\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                                break;
                                            }
                                            player.sendMessage("§cHow to Use: /fmg set <gameName> worldBoarder minSize <\ucd5c\uc18c\uc0ac\uc774\uc988>");
                                            break;
                                        }
                                        case "speed": {
                                            if (args.length == 5) {
                                                this.plugin.getConfig().set("miniGames." + args[1] + ".worldBoarder.speed", Integer.parseInt(args[4]));
                                                this.plugin.saveConfig();
                                                player.sendMessage("§6" + args[4] + "\uc774 " + args[1] + "\uac8c\uc784\uc5d0 \uc6d4\ub4dc \ubcf4\ub354 \uac10\uc18c \uc18d\ub3c4\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                                                break;
                                            }
                                            player.sendMessage("§cHow to Use: /fmg set <gameName> worldBoarder minSize <\uac10\uc18c\uc18d\ub3c4>");
                                            break;
                                        }
                                    }
                                    break;
                                }
                                player.sendMessage("§cHow to Use: /fmg set <gameName> worldBoarder <enable|sizePerPlayer|setLocation|outDamage|minSize|speed> ...");
                                break;
                            }
                            case "kit": {
                                if (args.length == 5) {
                                    final PlayerInventory inventory = player.getInventory();
                                    for (int i = 0; i < inventory.getSize(); ++i) {
                                        miniGames.getItems().getConfig().set("kits." + args[4] + ".items." + i, inventory.getItem(i));
                                    }
                                    final List<String> inventoryList = miniGames.getItems().getConfig().getStringList("kitList");
                                    if (!inventoryList.contains(args[4])) {
                                        inventoryList.add(args[4]);
                                    }
                                    miniGames.getItems().getConfig().set("kitList", inventoryList);
                                    miniGames.getItems().saveConfig();
                                    player.sendMessage("§6Your kit that inventory items  of " + args[3] + " has been saved");
                                    break;
                                }
                                player.sendMessage("§cHow to Use: /fmg set <gameName> kit create <kitName>");
                                break;
                            }
                            case "item": {
                                if (args.length == 4) {
                                    final PlayerInventory inventory = player.getInventory();
                                    miniGames.getItems().getConfig().set("items." + args[3], inventory.getItemInMainHand());
                                    final List<String> itemList = miniGames.getItems().getConfig().getStringList("itemList");
                                    if (!itemList.contains(args[3])) {
                                        itemList.add(args[3]);
                                    }
                                    miniGames.getItems().getConfig().set("itemList", itemList);
                                    miniGames.getItems().saveConfig();
                                    player.sendMessage("§6Your item of " + args[3] + " has been saved");
                                    break;
                                }
                                player.sendMessage("§cHow to Use: /fmg set <gameName> item <itemName>");
                                break;
                            }
                            case "inv": {
                                if (args.length >= 4) {
                                    final String s5 = args[3];
                                    switch (s5) {
                                        case "addItem": {
                                            if (args.length == 6) {
                                                miniGames.getItems().getConfig().set("inventories." + args[4] + ".items." + args[5], player.getInventory().getItemInMainHand());
                                                miniGames.getItems().saveConfig();
                                                player.sendMessage("§6Your item has been saved in line " + args[5] + " of " + args[4] + " inventory");
                                                break;
                                            }
                                            player.sendMessage("§cHow to Use: /fmg set <gameName> inv addItem <invName> <line>");
                                            break;
                                        }
                                        case "create": {
                                            if (args.length == 7) {
                                                miniGames.getItems().getConfig().set("inventories." + args[4] + ".title", ChatColor.translateAlternateColorCodes('&', args[6].replace("{spc}", " ")));
                                                miniGames.getItems().getConfig().set("inventories." + args[4] + ".size", Integer.parseInt(args[5]));
                                                final List<String> inventoryList2 = miniGames.getItems().getConfig().getStringList("inventoryList");
                                                if (!inventoryList2.contains(args[4])) {
                                                    inventoryList2.add(args[4]);
                                                }
                                                miniGames.getItems().getConfig().set("inventoryList", inventoryList2);
                                                miniGames.getItems().saveConfig();
                                                player.sendMessage("§6" + args[3] + " inventory successfully created");
                                                break;
                                            }
                                            player.sendMessage("§cHow to Use: /fmg set <gameName> inv create <invName> <9|18|27|36|45|54> <title>");
                                            break;
                                        }
                                        default: {
                                            player.sendMessage("§cHow to Use: /fmg set <gameName> inv <addItem|create> ...");
                                            break;
                                        }
                                    }
                                    break;
                                }
                                player.sendMessage("§cHow to Use: /fmg set <gameName> inv <addItem|create> ...");
                                break;
                            }
                            case "needClearInv": {
                                if (args.length == 4) {
                                    this.plugin.getConfig().set("miniGames." + args[1] + ".needClearInv", Boolean.parseBoolean(args[3]));
                                    this.plugin.saveConfig();
                                    player.sendMessage("§6" + args[3] + " saved whether the inventory should be empty when joining the " + args[1] + "game");
                                    break;
                                }
                                player.sendMessage("§cHow to Use: /fmg set <gameName> needClearInv <true|false>");
                                break;
                            }
                            case "setCmd": {
                                if (args.length >= 6) {
                                    String message2 = String.join(" ", Arrays.copyOfRange(args, 5, args.length));
                                    final List<String> cmdList2 = this.plugin.getConfig().getStringList("miniGames." + args[1] + "." + args[3] );
                                    int line = Integer.parseInt(args[4]);
                                    this.plugin.getConfig().set("miniGames." + args[1] + "." + args[3], cmdList2);
                                    this.plugin.saveConfig();
                                    List<String> commands = cmdList2;
                                    if (commands == null) commands = new ArrayList<>();
                                    if (message2.equals("none")) {
                                        commands.remove(message2);
                                    }
                                    else {
                                        if (commands.size() > line && commands.size() != 0) {
                                            commands.set(line, message2);
                                        } else {
                                            commands.add(message2);
                                        }
                                    }

                                    player.sendMessage("§6Your " + args[3] + " has been saved to the " + args[1] + " game's constant command.");
                                    break;
                                } else if (args.length == 4) {
                                    final List<String> cmdList2 = this.plugin.getConfig().getStringList("miniGames." + args[1] + "." + args[3]);
                                    player.sendMessage("§f------------------------------------------");
                                    for (int i = 0; i < cmdList2.size(); i++) {
                                        player.sendMessage(i % 2 == 0 ? "§a" + cmdList2.get(i) : "§7" + cmdList2.get(i));
                                    }
                                    player.sendMessage("§f------------------------------------------");
                                    this.plugin.saveConfig();
                                    break;
                                }
                                player.sendMessage("§cHow to Use: /fmg set <gameName> setCmd <customCmd>Cmd <cmdLine> <cmd>");
                                break;
                            }
                            case "msg": {
                                if (args.length == 5) {
                                    String message2 = String.join(" ", Arrays.copyOfRange(args, 4, args.length));
                                    this.plugin.getConfig().set("miniGames." + args[1] + "." + args[3], ChatColor.translateAlternateColorCodes('&', message2));
                                    this.plugin.saveConfig();
                                    player.sendMessage("§6Your custom message has been saved to the " + args[1] + " game's constant message.");
                                    break;
                                }
                                player.sendMessage("§cHow to Use: /fmg set <gameName> msg <customMsg> <message>");
                                break;
                            }
                            case "loc": {
                                if (args.length == 4) {
                                    this.miniGames.getEditor(args[1]).setLocation(args[3] + "Location", playerLocation.getWorld().getName(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), playerLocation.getYaw(), playerLocation.getPitch());
                                    player.sendMessage("§6Your custom location " + args[3] + " has been saved to the " + args[1] + " game's end location.");
                                    break;
                                }
                                player.sendMessage("§cHow to Use: /fmg set <gameName> loc <customName>");
                                break;
                            }
                            default: {
                                player.sendMessage("§cHow to Use: /fmg set <gameName> <wait|start|end|gameType|kit|item|inv|needClearInv|setCmd|msg|loc> ...");
                                break;
                            }
                        }
                        break;
                    }
                    player.sendMessage("§cHow to Use: /fmg set <gameName> <wait|start|end|gameType|kit|item|inv|needClearInv|setCmd|msg|loc> ...");
                    break;
                }
                case "list": {
                    player.sendMessage("§6List of games: " + this.plugin.getConfig().getStringList("gameList").toString());
                    break;
                }
                case "reload": {
                    if (player.hasPermission("freedyminigamemaker.admin")) {
                        this.plugin.reloadConfig();
                        miniGames.getSettings().load();
                        miniGames.getItems().load();
                        player.sendMessage("§aThe reload has been completed.");
                        break;
                    }
                    player.sendMessage("§cYou do not have permission.");
                    break;
                }
                default: {
                    if (!player.hasPermission("freedyminigamemaker.admin")) {
                        player.sendMessage("§cHow to Use: /fmg <join|quit|list> ...");

                        break;
                    }
                    player.sendMessage("§cHow to Use: /fmg <join|quit|create|remove|set|list|reload> ...");
                    break;
                }
            }
        }
        else {
            if (!player.hasPermission("freedyminigamemaker.admin")) {
                player.sendMessage("§cHow to Use: /fmg <join|quit|list> ...");
            } else player.sendMessage("§cHow to Use: /fmg <join|quit|create|remove|set|list|reload> ...");
        }
        return true;
    }
}
