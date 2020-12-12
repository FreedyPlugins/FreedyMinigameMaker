// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.commands;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import freedy.freedyminigamemaker.WorldEditor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Bed;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.boss.BarColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.*;

public class MinigameUtilities implements CommandExecutor
{
    FreedyMinigameMaker plugin;
    MiniGames miniGames;
    FreedyCommandSender newFreedyCommandSender = new FreedyCommandSender();
    public MinigameUtilities(final FreedyMinigameMaker plugin) {
        this.plugin = plugin;
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    public boolean onCommand(CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender.hasPermission("freedyminigamemaker.admin")) {
            if (args.length >= 2) {
                Player player = Bukkit.getPlayer(args[0]);
                MiniGame miniGame = this.miniGames.getJoined(player);
                if (!(sender instanceof FreedyCommandSender)) {
                    sender = newFreedyCommandSender;
                }
                final String s = args[1];
                switch (s) {
                    case "gui": {
                        if (args.length == 3) {
                            miniGame.openGui(player, args[2]);
                            break;
                        }
                        player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> gui <\uba54\ub274\uc774\ub984>");
                        break;
                    }
                    case "setGui": {
                        if (args.length == 5) {
                            miniGame.setGui(player, this.replace(player, miniGame, args[2]), Integer.parseInt(args[3]), this.replace(player, miniGame, args[4]));
                            break;
                        }
                        player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> setGui <invName> <index> <customItemName>");
                        break;
                    }
                    case "resetGui": {
                        miniGame.resetInventory(player);
                        break;
                    }
                    case "setGuiItem": {
                        if (args.length >= 3) {
                            switch (args[2]) {
                                case "name":
                                    if (args.length >= 6) {
                                        String message = String.join(" ", Arrays.copyOfRange(args, 5, args.length));
                                        miniGame.setGuiItemName(player,
                                                this.replace(player, miniGame, args[3]),
                                                Integer.parseInt(this.replace(player, miniGame, args[4])),
                                                this.replace(player, miniGame, message));
                                    } else
                                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> setGuiItem name <invName> <index> <itemName>");
                                    break;
                                case "lore":
                                    //fut <player> setGuiItem name <invName> <index> <line> <loreName>
                                    if (args.length >= 7) {
                                        String message = String.join(" ", Arrays.copyOfRange(args, 6, args.length));                            final String s3 = args[2];

                                        miniGame.setGuiItemLore(player,
                                                this.replace(player, miniGame, args[3]),
                                                Integer.parseInt(this.replace(player, miniGame, args[4])),
                                                Integer.parseInt(this.replace(player, miniGame, args[5])),
                                                this.replace(player, miniGame, message));
                                    } else
                                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> setGuiItem name <invName> <index> <line> <loreName>");
                                    break;
                            }
                            break;
                        } else player.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> setGuiItem <name|lore> ...");
                        break;
                    }
                    case "teleport": {
                        if (args.length == 5) {
                            final MiniGame mg = this.miniGames.get(args[3]);
                            if (mg.getLocationIsExist(this.replace(player, miniGame, args[4]) + "Location")) {
                                final String s2 = args[2];
                                switch (s2) {
                                    case "private": {
                                        Location loc = mg.getLocation(this.replace(player, miniGame, args[4]) + "Location");
                                        if (loc != null) player.teleport(loc);
                                        player.setFallDistance(0F);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (p != player) {
                                                p.hidePlayer(player);
                                                p.showPlayer(player);
                                            }
                                        }
                                        break;
                                    }
                                    case "game": {
                                        for (final Player p2 : mg.playerList) {
                                            Location loc = mg.getLocation(this.replace(player, miniGame, args[4]) + "Location");
                                            if (loc != null) p2.teleport(loc);
                                            player.setFallDistance(0F);
                                            for (Player p : Bukkit.getOnlinePlayers()) {
                                                if (p != player) {
                                                    p.hidePlayer(player);
                                                    p.showPlayer(player);
                                                }
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> teleport <private|game> <\ubbf8\ub2c8\uac8c\uc784> <\uc800\uc7a5\ub41c\uc704\uce58>");
                        break;
                    }
                    case "tp": {
                        if (args.length >= 6) {
                            final MiniGame mg = this.miniGames.get(args[3]);
                            Location loc = player.getLocation();
                            loc.setWorld(Bukkit.getWorld(args[2]));
                            loc.setX(Double.parseDouble(args[3]));
                            loc.setY(Double.parseDouble(args[4]));
                            loc.setZ(Double.parseDouble(args[5]));
                            if (args.length >= 7) loc.setYaw(Float.parseFloat(args[6]));
                            if (args.length >= 8) loc.setPitch(Float.parseFloat(args[7]));
                            player.teleport(loc);

                            player.setFallDistance(0F);
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (p != player) {
                                    p.hidePlayer(player);
                                    p.showPlayer(player);
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> tp <world> <x> <y> <z> [yaw] [pitch]");
                        break;
                    }
                    case "sendMsg": {
                        if (args.length >= 4) {
                            String message = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                            final String s3 = args[2];
                            switch (s3) {
                                case "public": {
                                    Bukkit.getServer().broadcastMessage(message);
                                    break;
                                }
                                case "private": {
                                    player.sendMessage(message);
                                    break;
                                }
                                case "game": {
                                    for (final Player p4 : miniGame.playerList) {
                                        p4.sendMessage(message);
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> sendMsg <public|private|team|game> <\uba54\uc0c8\uc9c0>");
                        sender.sendMessage("§c\ucc38\uace0: <\uba85\ub839\uc904> \uc785\ub825\ub780\uc5d0\ub294 \uacf5\ubc31\uc744 {spc}\uc73c\ub85c \ub123\uc73c\uc138\uc694");
                        break;
                    }
                    case "sendJson": {
                        if (args.length >= 4) {
                            String message = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                            final List<String> stringList = new ArrayList<>(Arrays.asList(message.split(", ")));
                            final List<String> msgList = new ArrayList<>(Arrays.asList(stringList.get(3).split("-")));
                            final String s3 = args[2];
                            switch (s3) {
                                case "public": {
                                    BaseComponent mid = new TextComponent(msgList.get(1));
                                    if (!stringList.get(0).equals("none"))
                                        mid.setClickEvent(new ClickEvent(ClickEvent.Action.valueOf(stringList.get(0)), stringList.get(1)));
                                    //if (!stringList.get(2).equals("none")) component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(stringList.get(2))));
                                    BaseComponent component = new TextComponent(msgList.get(0));
                                    component.addExtra(mid);
                                    component.addExtra(msgList.get(2));
                                    for (final Player p4 : Bukkit.getOnlinePlayers()) {
                                        p4.spigot().sendMessage(component);
                                    }
                                    break;
                                }
                                case "private": {
                                    BaseComponent mid = new TextComponent(msgList.get(1));
                                    if (!stringList.get(0).equals("none"))
                                        mid.setClickEvent(new ClickEvent(ClickEvent.Action.valueOf(stringList.get(0)), stringList.get(1)));
                                    //if (!stringList.get(2).equals("none")) component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(stringList.get(2))));
                                    BaseComponent component = new TextComponent(msgList.get(0));
                                    component.addExtra(mid);
                                    component.addExtra(msgList.get(2));
                                    player.spigot().sendMessage(component);
                                    break;
                                }
                                case "game": {
                                    BaseComponent mid = new TextComponent(msgList.get(1));
                                    if (!stringList.get(0).equals("none"))
                                        mid.setClickEvent(new ClickEvent(ClickEvent.Action.valueOf(stringList.get(0)), stringList.get(1)));
                                    //if (!stringList.get(2).equals("none")) component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(stringList.get(2))));
                                    BaseComponent component = new TextComponent(msgList.get(0));
                                    component.addExtra(mid);
                                    component.addExtra(msgList.get(2));
                                    for (final Player p4 : miniGame.playerList) {
                                        p4.spigot().sendMessage(component);
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> sendJson <public|private|team|game> <eventType>, <eventText>, <hover>, <message>-<jsonMessage>-<message>");
                        sender.sendMessage("§c\ucc38\uace0: <\uba85\ub839\uc904> \uc785\ub825\ub780\uc5d0\ub294 \uacf5\ubc31\uc744 {spc}\uc73c\ub85c \ub123\uc73c\uc138\uc694");
                        break;
                    }
                    case "sendActionBar": {
                        if (args.length >= 4) {
                            String message = String.join(" ", Arrays.copyOfRange(args, 3, args.length));                            final String s3 = args[2];
                            final String s4 = args[2];
                            switch (s4) {
                                case "public": {
                                    for (final Player p4 : Bukkit.getOnlinePlayers()) {
                                        p4.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                                    }
                                    break;
                                }
                                case "private": {
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                                    break;
                                }
                                case "game": {
                                    for (final Player p4 : miniGame.playerList) {
                                        p4.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> sendActionBar <public|private|team|game> <\uc5d1\uc158\ubc14\ub9e4\uc138\uc9c0>");
                        sender.sendMessage("§c\ucc38\uace0: <\uba85\ub839\uc904> \uc785\ub825\ub780\uc5d0\ub294 \uacf5\ubc31\uc744 {spc}\uc73c\ub85c \ub123\uc73c\uc138\uc694");
                        break;
                    }
                    case "sendTitle": {
                        if (args.length >= 4) {
                            String message = String.join(" ", Arrays.copyOfRange(args, 6, args.length));
                            final List<String> stringList = new ArrayList<>(Arrays.asList(message.split("-")));
                            final String s5 = args[2];
                            switch (s5) {
                                case "public": {
                                    for (final Player p5 : Bukkit.getOnlinePlayers()) {
                                        p5.sendTitle(stringList.get(0), (stringList.size() == 2) ? stringList.get(1) : null, Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                                    }
                                    break;
                                }
                                case "private": {
                                    player.sendTitle(stringList.get(0), (stringList.size() == 2) ? stringList.get(1) : null, Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                                    break;
                                }
                                case "game": {
                                    for (final Player p5 : miniGame.playerList) {
                                        p5.sendTitle(stringList.get(0), (stringList.size() == 2) ? stringList.get(1) : null, Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> sendTitle <public|private|team|game> <fadeIn> <stay> <fadeOut> <\uc81c\ubaa9-\ubd80\uc81c\ubaa9>");
                        sender.sendMessage("§c\ucc38\uace0: <\uba85\ub839\uc904> \uc785\ub825\ub780\uc5d0\ub294 \uacf5\ubc31\uc744 {spc}\uc73c\ub85c \ub123\uc73c\uc138\uc694");
                        break;
                    }
                    case "sendBossBar": {
                        if (args.length >= 7) {
                            String message = String.join(" ", Arrays.copyOfRange(args, 6, args.length));
                            final String s5 = args[2];
                            switch (s5) {
                                case "public": {
                                    for (final Player p5 : Bukkit.getOnlinePlayers()) {
                                        miniGame.setBossBar(p5, args[3], BarColor.valueOf(args[5]), Double.parseDouble(args[4]), message);
                                    }
                                    break;
                                }
                                case "private": {
                                    miniGame.setBossBar(player, args[3], BarColor.valueOf(args[5]), Double.parseDouble(args[4]), message);
                                    break;
                                }
                                case "game": {
                                    for (final Player p5 : miniGame.playerList) {
                                        miniGame.setBossBar(p5, args[3], BarColor.valueOf(args[5]), Double.parseDouble(args[4]), message);

                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> sendBossBar <public|private|team|game> <barName> <progress> <color> <message>");
                        sender.sendMessage("§c" + "color list: BLUE GREEN PINK PURPLE RED WHITE YELLOW");
                        break;
                    }
                    case "sendSound": {
                        if (args.length == 6) {
                            final String s6 = args[2];
                            switch (s6) {
                                case "public": {
                                    for (final Player p5 : Bukkit.getOnlinePlayers()) {
                                        p5.playSound(p5.getLocation(), Sound.valueOf(this.replace(player, miniGame, args[3])), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
                                    }
                                    break;
                                }
                                case "private": {
                                    player.playSound(player.getLocation(), Sound.valueOf(this.replace(player, miniGame, args[3])), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
                                    break;
                                }
                                case "game": {
                                    for (final Player p7 : miniGame.playerList) {
                                        p7.playSound(p7.getLocation(), Sound.valueOf(this.replace(player, miniGame, args[3])), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> sendSound <public|private|team|game> <sound> <volume> <pitch>");
                        sender.sendMessage("\uc0ac\uc6b4\ub4dc\ubaa9\ub85d: https://helpch.at/docs/1.12.2/org/bukkit/Sound.html");
                        break;
                    }
                    case "gameMode": {
                        if (args.length == 4) {
                            final String s7 = args[2];
                            switch (s7) {
                                case "private": {
                                    player.setGameMode(GameMode.valueOf(args[3]));
                                    break;
                                }
                                case "game": {
                                    for (final Player p7 : miniGame.playerList) {
                                        p7.setGameMode(GameMode.valueOf(args[3]));
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> gamemode <private|team|game> <\uac8c\uc784\ubaa8\ub4dc>");
                        sender.sendMessage("§c\uac8c\uc784\ubaa8\ub4dc\ub294 \ubaa8\ub450 \uc601\uc5b4 \ub300\ubb38\uc790\ub85c \uc368\uc8fc\uc138\uc694");
                        break;
                    }
                    case "hide": {
                        if (args.length == 4) {
                            final String s7 = args[2];
                            switch (s7) {
                                case "private": {
                                    player.hidePlayer(Bukkit.getPlayer(args[3]));
                                    break;
                                }
                                case "public": {
                                    for (final Player p7 : Bukkit.getOnlinePlayers()) {
                                        if (!Bukkit.getPlayer(args[3]).equals(p7))
                                            p7.hidePlayer(Bukkit.getPlayer(args[3]));
                                    }
                                    break;
                                }
                                case "game": {
                                    for (final Player p7 : miniGame.playerList) {
                                        p7.hidePlayer(Bukkit.getPlayer(args[3]));
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> hide <private|public|game> <player>");
                        break;
                    }
                    case "show": {
                        if (args.length == 4) {
                            final String s7 = args[2];
                            switch (s7) {
                                case "private": {
                                    player.showPlayer(Bukkit.getPlayer(args[3]));
                                    break;
                                }
                                case "public": {
                                    for (final Player p7 : Bukkit.getOnlinePlayers()) {
                                        if (!Bukkit.getPlayer(args[3]).equals(p7))
                                            p7.showPlayer(Bukkit.getPlayer(args[3]));
                                    }
                                    break;
                                }
                                case "game": {
                                    for (final Player p7 : miniGame.playerList) {
                                        p7.showPlayer(Bukkit.getPlayer(args[3]));
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> show <private|public|game> <player>");
                        break;
                    }
                    case "health": {
                        if (args.length == 4) {
                            final String s7 = args[2];
                            switch (s7) {
                                case "private": {
                                    player.setHealth(Double.parseDouble(args[3]));
                                    break;
                                }
                                case "game": {
                                    for (final Player p7 : miniGame.playerList) {
                                        p7.setHealth(Double.parseDouble(args[3]));
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> health <private|team|game> <health>");
                        break;
                    }
                    case "food": {
                        if (args.length == 4) {
                            final String s7 = args[2];
                            switch (s7) {
                                case "private": {
                                    player.setFoodLevel(Integer.parseInt(args[3]));
                                    break;
                                }
                                case "game": {
                                    for (final Player p7 : miniGame.playerList) {
                                        p7.setFoodLevel(Integer.parseInt(args[3]));
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> food <private|team|game> <foodLevel>");
                        break;
                    }
                    case "addPotion": {
                        if (args.length == 7) {
                            final PotionEffect potionEffect = new PotionEffect(PotionEffectType.getByName(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                            final String s8 = args[2];
                            switch (s8) {
                                case "private": {
                                    player.addPotionEffect(potionEffect, Boolean.parseBoolean(args[6]));
                                    break;
                                }
                                case "game": {
                                    for (final Player p2 : miniGame.playerList) {
                                        p2.addPotionEffect(potionEffect, Boolean.parseBoolean(args[6]));
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> addPotion <private|team|game> <\ud3ec\uc158\uc774\ub984> <\uc9c0\uc18d\uc2dc\uac04> <\uc99d\ud3ed> <\uc785\uc790\uc228\uae40\uc5ec\ubd80>");
                        break;
                    }
                    case "removePotion": {
                        if (args.length == 3) {
                            final String s9 = args[2];
                            switch (s9) {
                                case "private": {
                                    for (final PotionEffect effect : player.getActivePotionEffects()) {
                                        player.removePotionEffect(effect.getType());
                                    }
                                    break;
                                }
                                case "game": {
                                    for (final Player p7 : miniGame.playerList) {
                                        for (final PotionEffect effect3 : p7.getActivePotionEffects()) {
                                            p7.removePotionEffect(effect3.getType());
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> removePotion <private|team|game>");
                        break;
                    }
                    case "kit": {
                        if (args.length >= 3) {
                            this.miniGames.getNoneGame().applyKit(player, this.replace(player, miniGame, args[2]));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> kit <\ud0b7\uc774\ub984>");
                        break;
                    }
                    case "openGui": {
                        if (args.length >= 3) {
                            this.miniGames.getNoneGame().openInv(player, this.replace(player, miniGame, args[2]));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> openGui <\uba54\ub274\uc774\ub984>");
                        break;
                    }
                    case "closeGui": {
                        player.closeInventory();
                        break;
                    }
                    case "addToTeam": {
                        if (args.length >= 3) {
                            Team team = player.getScoreboard().getTeam(args[2]);
                            if (team == null) {
                                player.getScoreboard().registerNewTeam(args[2]).addPlayer(player);
                            } else {
                                team.addPlayer(player);
                            }

                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> addToTeam <team>");
                        break;
                    }
                    case "setWeather": {
                        if (args.length >= 3) {
                            player.setPlayerWeather(WeatherType.valueOf(args[2]));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> setWeather <weatherType>");
                        break;
                    }
                    case "resetWeather": {
                        player.resetPlayerWeather();
                        break;
                    }
                    case "setTime": {
                        if (args.length >= 3) {
                            player.setPlayerTime(Long.parseLong(args[2]), true);
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> setTime <timeTick>");
                        break;
                    }
                    case "resetTime": {
                        player.resetPlayerTime();
                        break;
                    }
                    case "removeFire": {
                        player.setFireTicks(0);
                        break;
                    }
                    case "loadChunk": {
                        if (args.length == 5) {
                            Chunk chunk = Bukkit.getWorld(args[2]).getChunkAt(Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                            if (chunk.isLoaded()) {
                                Bukkit.getWorld(args[2]).refreshChunk(Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                            } else
                                chunk.load();
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> loadChunk <world> <x> <z>");
                        break;
                    }
                    case "regenChunk": {
                        if (args.length == 5) {
                            Bukkit.getWorld(args[2]).regenerateChunk(Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> regenChunk <world> <x> <z>");
                        break;
                    }
                    case "fakeBlock": {
                        if (args.length == 7) {
                            Location loc = new Location(Bukkit.getWorld(args[2]),
                                    Integer.parseInt(args[3]),
                                    Integer.parseInt(args[4]),
                                    Integer.parseInt(args[5])
                            );
                            player.sendBlockChange(loc, Material.valueOf(args[6]), (byte) 0);

                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> fakeBlock <world> <x> <y> <z> <block>");
                        break;
                    }
                    case "join": {
                        if (args.length == 3) {
                            MiniGame mg = this.miniGames.get(this.replace(player, miniGame, args[2]));
                            mg.add(player);
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> join <\uac8c\uc784\uc774\ub984>");
                        break;
                    }
                    case "joinAll": {
                        if (args.length == 3) {

                            String result = this.replace(player, miniGame, args[2]);
                            for (final Player p : Bukkit.getOnlinePlayers()) {
                                if (!this.miniGames.isJoined(p)) this.miniGames.get(result).add(p);
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> joinAll <\uac8c\uc784\uc774\ub984>");
                        break;
                    }
                    case "knockBack": {
                        if (args.length == 5) {
                            player.setVelocity(new Vector(Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4])));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> knockBack <x> <y> <z>");
                        break;
                    }
                    case "entityKnockBack": {
                        if (args.length == 6) {
                            Bukkit.getEntity(UUID.fromString(args[2])).setVelocity(new Vector(Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5])));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> entityKnockBack <entityUuid> <x> <y> <z>");
                        break;
                    }
                    case "addKnockBack": {
                        if (args.length == 5) {
                            Location location = player.getLocation();
                            location.setYaw(Float.parseFloat(args[3]));
                            location.setPitch(Float.parseFloat(args[4]));
                            player.setVelocity(location.getDirection().multiply(Double.parseDouble(args[2])));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> addKnockBack <amount> <yaw> <pitch>");
                        break;
                    }
                    case "entityAddKnockBack": {
                        if (args.length == 6) {
                            Location location = player.getLocation();
                            location.setYaw(Float.parseFloat(args[4]));
                            location.setPitch(Float.parseFloat(args[5]));
                            Bukkit.getEntity(UUID.fromString(args[2])).setVelocity(location.getDirection().multiply(Double.parseDouble(args[3])));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> entityAddKnockBack <entityUuid> <amount> <yaw> <pitch>");
                        break;
                    }
                    case "giveHand": {
                        if (args.length == 3) {
                            this.miniGames.getNoneGame().giveItemHand(player, this.replace(player, miniGame, args[2]));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> giveHand <customItem>");
                        break;
                    }
                    case "giveCursor": {
                        if (args.length == 4) {
                            this.miniGames.getNoneGame().giveItemCursor(player, this.replace(player, miniGame, args[2]), Integer.parseInt(args[3]));
                            break;
                        }
                        else if (args.length == 5) {
                            this.miniGames.getNoneGame().giveItemCursor(player, this.replace(player, miniGame, args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> giveHand <customItem> <cursor> [amount]");
                        break;
                    }
                    case "setHelmet": {
                        if (args.length == 3) {
                            this.miniGames.getNoneGame().setHelmet(player, this.replace(player, miniGame, args[2]));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> setHelmet <customItem>");
                        break;
                    }
                    case "setChestplate": {
                        if (args.length == 3) {
                            this.miniGames.getNoneGame().setChestplate(player, this.replace(player, miniGame, args[2]));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> setChestplate <customItem>");
                        break;
                    }
                    case "setLeggings": {
                        if (args.length == 3) {
                            this.miniGames.getNoneGame().setLeggings(player, this.replace(player, miniGame, args[2]));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> setLeggings <customItem>");
                        break;
                    }
                    case "setBoots": {
                        if (args.length == 3) {
                            this.miniGames.getNoneGame().setBoots(player, this.replace(player, miniGame, args[2]));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> setBoots <customItem>");
                        break;
                    }
                    case "cursor": {
                        if (args.length == 3) {
                            player.getInventory().setHeldItemSlot(Integer.parseInt(args[2]));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> cursor <cursor>");
                        break;
                    }
                    case "kill": {
                        if (args.length == 3) {
                            Bukkit.getEntity(UUID.fromString(args[2])).remove();
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> kill <entityUuid>");
                        break;
                    }
                    case "damage": {
                        if (args.length == 4) {
                            Entity entity = Bukkit.getEntity(UUID.fromString(args[2]));
                            if (entity instanceof LivingEntity) {
                                ((LivingEntity) entity).damage(Double.parseDouble(args[3]));
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> damage <entityUuid> <damage>");
                        break;
                    }
                    case "kick": {
                        miniGame.kick(player);
                        final String defaultQuitGame = miniGame.getSettings("defaultQuitGame");
                        if (defaultQuitGame != null && !(defaultQuitGame.equals("none"))) {
                            FreedyMinigameMaker.miniGames.get(defaultQuitGame).add(player);
                        }
                        miniGame.stop();
                        break;
                    }
                    case "move": {
                        if (args.length == 3) {
                            MiniGame mg = miniGames.get(args[2]);
                            if (!mg.checkMove(player)) break;
                            miniGame.kick(player);
                            mg.add(player);
                            final String defaultQuitGame = miniGame.getSettings("defaultQuitGame");
                            if (defaultQuitGame != null && !(defaultQuitGame.equals("none"))) {
                                FreedyMinigameMaker.miniGames.get(defaultQuitGame).add(player);
                            }
                            miniGame.stop();
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> move <\uac8c\uc784\uc774\ub984>");
                        break;
                    }
                    case "if": {
                        if (args.length >= 6) {
                            final boolean result = this.checkIf(player, miniGame, args);
                            String message2 = String.join(" ", Arrays.copyOfRange(args, 5, args.length));
                            final String elseCmd = StringUtils.substringBetween(message2, "{else(", ")}");
                            final String doCmd = StringUtils.substringBetween(message2, "{do(", ")}");
                            String secondCmd = message2
                                    .replace("{else(" + elseCmd + ")}", "")
                                    .replace("{do(" + doCmd + ")}", "");

                            if (result) {
                                if (!(secondCmd.equals("") || secondCmd.equals(" "))) {
                                    miniGame.executeCommand((FreedyCommandSender) sender, message2, player);
                                    break;
                                }
                                if (doCmd != null) {
                                    final String[] split = doCmd.split(" && ");
                                    for (String cmd : split) {
                                        if (miniGame.executeCommand((FreedyCommandSender) sender, cmd, player).equals("false"))
                                            return true;
                                    }
                                }

                            } else if (elseCmd != null && (secondCmd.equals("") || secondCmd.equals(" "))) {
                                final String[] split2 = elseCmd.split(" && ");
                                for (String cmd : split2) {
                                    if (miniGame.executeCommand((FreedyCommandSender) sender, cmd, player).equals("false"))
                                        return true;

                                }
                            }

                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> if <value1> <==|/=|>|>=|<|<=> <value2> <cmdLine>");
                        break;
                    }
                    case "while": {
                        if (args.length >= 6) {
                            String message = String.join(" ", Arrays.copyOfRange(args, 5, args.length));
                            int index = StringUtils.lastIndexOfIgnoreCase(message, "{do(");
                            int lastIndex = StringUtils.indexOfIgnoreCase(message, ")}");
                            String string = message.substring(index, lastIndex);
                            final String doCmd2 = StringUtils.substringBetween(message, "{do(", ")}");
                            String secondCmd2 = message.replace("{do(" + doCmd2 + ")}", "");
                            while (this.checkIf(player, miniGame, args)) {
                                if (doCmd2 != null) {
                                    final String[] split3 = doCmd2.split(" && ");
                                    for (final String cmd2 : split3) {
                                        if (miniGame.executeCommand((FreedyCommandSender) sender, cmd2, player).equals("false"))
                                            return true;

                                    }
                                }
                            }
                            if (!secondCmd2.equals("")) {
                                miniGame.executeCommand((FreedyCommandSender) sender, secondCmd2, player);

                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> while <value1> <==|/=|>|>=|<|<=> <value2> <cmdLine>");
                        break;
                    }
                    case "do": {
                        if (args.length >= 3) {
                            String message = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                            final List<String> doArgs = Arrays.asList(message.split(", "));
                            final Map<String, String> replacement = new HashMap<>();
                            for (int k = 0; k < doArgs.size() - 1; k += 2) {
                                replacement.put("{" + doArgs.get(k) + "}", doArgs.get(k + 1));
                            }

                            for (String cmd3 : miniGame.getMessageList(args[2] + "Cmd")) {
                                for (final String key : replacement.keySet()) {
                                    cmd3 = cmd3.replace(key, replacement.get(key));
                                }
                                if (args[2].contains("keeped"))
                                    Bukkit.getServer().dispatchCommand(sender, cmd3);
                                else if (miniGame.executeCommand((FreedyCommandSender) sender, cmd3, player).equals("false"))
                                    return true;
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> do <cmdBundle> dataName, data");
                        break;
                    }
                    case "execute": {
                        if (args.length >= 3) {
                            String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), this.replace(player, miniGame, message));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> execute <cmdLine>");
                        break;
                    }
                    case "executeCmd": {
                        if (args.length >= 3) {
                            String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                            player.performCommand(message);
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> executeCmd <cmdLine>");
                        break;
                    }
                    case "executeDelayCmd": {
                        if (args.length >= 4) {
                            String message = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                            Bukkit.getScheduler().runTaskLater(this.plugin, () -> player.performCommand(message), Integer.parseInt(args[2]));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> executeDelayCmd <delayTick> <cmdLine>");
                        break;
                    }
                    case "executeConCmd": {
                        if (args.length >= 3) {
                            String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                            Bukkit.getServer().dispatchCommand(sender, message);

                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> executeConCmd <cmdLine>");
                        break;
                    }
                    case "executeConDelayCmd": {
                        if (args.length >= 4) {
                            String message = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                            CommandSender finalSender = sender;
                            if (player != null) miniGame.taskIdList.add(Bukkit.getScheduler().runTaskLater(this.plugin, () -> Bukkit.getServer().dispatchCommand(finalSender, message), Integer.parseInt(args[2])).getTaskId());
                            else Bukkit.getScheduler().runTaskLater(this.plugin, () -> Bukkit.getServer().dispatchCommand(finalSender, message), Integer.parseInt(args[2])).getTaskId();
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> executeConDelayCmd <delayTick> <cmdLine>");
                        break;
                    }
                    case "topTp": {
                        World world = player.getWorld();
                        Location location = player.getLocation();
                        Location loc;
                        for (int l = world.getMaxHeight(); l > 0; --l) {
                            loc = location.add(0, l + 1.5, 0);
                            if (!loc.getBlock().getType().equals(Material.AIR)) {
                                location = location.add(0, l + 1.5, 0);
                                player.teleport(location);
                                break;
                            }
                        }
                        break;
                    }
                    case "cancelAllDelayCmd": {
                        break;
                    }
                    case "resetBlocks": {
                        if (args.length >= 3) {
                            this.miniGames.get(args[2]).resetBlocks();
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> resetBlocks <gameName>");
                        break;
                    }
                    case "setData": {
                        if (args.length >= 4) {
                            String message5 = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                            if (miniGame != null) {
                                miniGame.setCustomData(args[2], message5);
                            } else {
                                sender.sendMessage("§c\ud50c\ub808\uc774\uc5b4\uac00 \ubbf8\ub2c8\uac8c\uc784\uc5d0 \ucc38\uc5ec \uc911\uc774\uc9c0 \uc54a\uae30 \ub54c\ubb38\uc5d0 \ub370\uc774\ud0c0\ub97c \uc800\uc7a5\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.");
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> setData <customData> <data>");
                        break;
                    }
                    case "addData": {
                        if (args.length < 4) {
                            sender.sendMessage("§cUsage: /fut <player> addData <customData> <amount>");
                            break;
                        }
                        if (miniGame != null) {
                            miniGame.setCustomData(args[2], String.valueOf(Double.parseDouble(miniGame.getCustomData(args[2])) + Double.parseDouble(args[3])));
                            break;
                        }
                        sender.sendMessage("§c\ud50c\ub808\uc774\uc5b4\uac00 \ubbf8\ub2c8\uac8c\uc784\uc5d0 \ucc38\uc5ec \uc911\uc774\uc9c0 \uc54a\uae30 \ub54c\ubb38\uc5d0 \ub370\uc774\ud0c0\ub97c \uc800\uc7a5\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.");
                        break;
                    }
                    case "setPlayerData": {
                        if (args.length >= 4) {
                            String message5 = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                            if (miniGame != null) {
                                miniGame.getPlayerData(player).setCustomData(args[2], message5);
                            } else {
                                sender.sendMessage("§c\ud50c\ub808\uc774\uc5b4\uac00 \ubbf8\ub2c8\uac8c\uc784\uc5d0 \ucc38\uc5ec \uc911\uc774\uc9c0 \uc54a\uae30 \ub54c\ubb38\uc5d0 \ub370\uc774\ud0c0\ub97c \uc800\uc7a5\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.");
                            }
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> setPlayerData <customData> <data>");
                        break;
                    }
                    case "addPlayerData": {
                        if (args.length < 4) {
                            sender.sendMessage("§cUsage: /fut <player> addPlayerData <customData> <amount>");
                            break;
                        }
                        if (miniGame != null) {
                            miniGame.getPlayerData(player).setCustomData(args[2], String.valueOf(Double.parseDouble(miniGame.getPlayerData(player).getCustomData(args[2])) + Double.parseDouble(args[3])));
                            break;
                        }
                        sender.sendMessage("§c\ud50c\ub808\uc774\uc5b4\uac00 \ubbf8\ub2c8\uac8c\uc784\uc5d0 \ucc38\uc5ec \uc911\uc774\uc9c0 \uc54a\uae30 \ub54c\ubb38\uc5d0 \ub370\uc774\ud0c0\ub97c \uc800\uc7a5\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.");
                        break;
                    }
                    case "setBlockWE": {
                        if (args.length >= 7) {
                            /*
                            final World world2 = Bukkit.getWorld(args[2]);
                            final double x2 = Double.parseDouble(args[3]);
                            final double y1 = Double.parseDouble(args[4]);
                            final double z2 = Double.parseDouble(args[5]);
                            final Material blockType = Material.valueOf(args[6]);
                            final Block block = new Location(world2, x2, y1, z2).getBlock();
                            miniGame.addBlock(block);
                            block.setType(blockType);
                            block.getState().update();
                            */
                            new WorldEditor().setBlockWE(args);
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> setBlock <world> <x> <y> <z> <blockCode> [blockData]");
                        break;
                    }
                    case "setBlock": {
                        if (args.length == 7) {

                            final World world2 = Bukkit.getWorld(args[2]);
                            final double x2 = Double.parseDouble(args[3]);
                            final double y1 = Double.parseDouble(args[4]);
                            final double z2 = Double.parseDouble(args[5]);
                            final Material blockType = Material.valueOf(args[6]);
                            final Block block = new Location(world2, x2, y1, z2).getBlock();
                            miniGame.addBlock(block);
                            block.setType(blockType);
                            block.getState().update();

                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> setBlock <world> <x> <y> <z> <blockType>");
                        break;
                    }
                    case "setBlockChange": {
                        if (args.length == 7) {

                            final World world2 = Bukkit.getWorld(args[2]);
                            final double x2 = Double.parseDouble(args[3]);
                            final double y1 = Double.parseDouble(args[4]);
                            final double z2 = Double.parseDouble(args[5]);
                            final Material blockType = Material.valueOf(args[6]);
                            final Block block = new Location(world2, x2, y1, z2).getBlock();
                            miniGame.addBlock(block);
                            block.setType(blockType);
                            //block.getState().update();

                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> setBlockChange <world> <x> <y> <z> <blockType>");
                        break;
                    }

                    case "setBedBlock": {
                        if (args.length == 8) {
                            final World world2 = Bukkit.getWorld(args[2]);
                            final double x2 = Double.parseDouble(args[3]);
                            final double y1 = Double.parseDouble(args[4]);
                            final double z2 = Double.parseDouble(args[5]);
                            final Location location = new Location(world2, x2, y1, z2);
                            Block block = location.getBlock();
                            Block block2;
                            byte flags = (byte) 8;
                            switch (BlockFace.valueOf(args[7])) {
                                case EAST:
                                    block2 = location.add(1, 0, 0).getBlock();
                                    break;
                                case SOUTH:
                                    block2 = location.add(0, 0, 1).getBlock();
                                    flags = (byte) (flags | 0x1);
                                    break;
                                case WEST:
                                    block2 = location.add(-1, 0, 0).getBlock();
                                    flags = (byte) (flags | 0x2);
                                    break;
                                case NORTH:
                                    block2 = location.add(0, 0, -1).getBlock();
                                    flags = (byte) (flags | 0x3);
                                    break;
                                default:
                                    block2 = block;
                            }
                            block.setTypeIdAndData(26, flags, true);
                            Bed bed = ((Bed) block.getState());
                            bed.setColor(DyeColor.valueOf(args[6]));
                            bed.update();

                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> setBedBlock <world> <x> <y> <z> <WHITE|ORANGE|MAGENTA|LIGHT_BLUE|YELLOW|LIME|PINK|GRAY|SILVER|CYAN|PURPLE|BLUE|BROWN|GREEN|RED|BLACK> <NORTH|SOUTH|EAST|WEST>");
                        break;
                    }
                    case "setBlocksWE": {
                        if (args.length >= 10) {
                            new WorldEditor().setBlocksWE(args);
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> setBlocksWE <world> <x> <y> <z> <x2> <y2> <z2> <blockCode> [blockData]");
                        break;
                    }
                    case "setBlockId": {
                        if (args.length == 8) {
                            final World world2 = Bukkit.getWorld(args[2]);
                            final double x2 = Double.parseDouble(args[3]);
                            final double y1 = Double.parseDouble(args[4]);
                            final double z2 = Double.parseDouble(args[5]);
                            final Material blockType = Material.getMaterial(Integer.parseInt(args[6]));
                            ItemStack item = new ItemStack(blockType);
                            item.setDurability(Short.parseShort(args[7]));
                            Block block = new Location(world2, x2, y1, z2).getBlock();
                            block.setData((byte) item.getDurability());
                            //block = block.getRelative(BlockFace.valueOf(args[8]));
                            miniGame.addBlock(block);
                            block.setType(blockType);
                            block.getState().update();
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> setBlockId <world> <x> <y> <z> <frontBlockId> <lastBlockId>");
                        break;
                    }
                    case "relativeBlock": {
                        if (args.length == 7) {
                            final World world2 = Bukkit.getWorld(args[2]);
                            final double x2 = Double.parseDouble(args[3]);
                            final double y1 = Double.parseDouble(args[4]);
                            final double z2 = Double.parseDouble(args[5]);
                            Block block = new Location(world2, x2, y1, z2).getBlock();
                            block.getRelative(BlockFace.valueOf(args[6]));
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> relativeBlock <world> <x> <y> <z> <SOUTH|WEST|NORTH|EAST|UP|DOWN|");
                        break;
                    }

                    case "addBlock": {
                        if (args.length == 6) {
                            final World world2 = Bukkit.getWorld(args[2]);
                            final double x2 = Double.parseDouble(args[3]);
                            final double y1 = Double.parseDouble(args[4]);
                            final double z2 = Double.parseDouble(args[5]);
                            final Block block = new Location(world2, x2, y1, z2).getBlock();
                            miniGame.addBlock(block);
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> addBlock <world> <x> <y> <z>");
                        break;
                    }
                    case "setBlocks": {
                        if (args.length == 10) {
                            final World world = Bukkit.getWorld(args[2]);
                            final int x1 = Integer.parseInt(args[3]);
                            final int y1 = Integer.parseInt(args[4]);
                            final int z1 = Integer.parseInt(args[5]);
                            final int x2 = Integer.parseInt(args[6]);
                            final int y2 = Integer.parseInt(args[7]);
                            final int z2 = Integer.parseInt(args[8]);
                            final int maxX = Math.max(x1, x2);
                            final int minX = Math.min(x1, x2);
                            final int maxY = Math.max(y1, y2);
                            final int minY = Math.min(y1, y2);
                            final int maxZ = Math.max(z1, z2);
                            final int minZ = Math.min(z1, z2);
                            final Material blockType = Material.valueOf(args[9]);
                            for (int x = maxX; x >= minX; x--) {
                                for (int y = maxY; y >= minY; y--) {
                                    for (int z = maxZ; z >= minZ; z--) {
                                        world.getBlockAt(x, y, z).setType(blockType);
                                    }
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> setBlocks <world> <x> <y> <z> <x2> <y2> <z2> <blockType>");
                        break;
                    }
                    case "addBlocks": {
                        if (args.length == 9) {
                            final World world = Bukkit.getWorld(args[2]);
                            final int x1 = Integer.parseInt(args[3]);
                            final int y1 = Integer.parseInt(args[4]);
                            final int z1 = Integer.parseInt(args[5]);
                            final int x2 = Integer.parseInt(args[6]);
                            final int y2 = Integer.parseInt(args[7]);
                            final int z2 = Integer.parseInt(args[8]);
                            final int maxX = Math.max(x1, x2);
                            final int minX = Math.min(x1, x2);
                            final int maxY = Math.max(y1, y2);
                            final int minY = Math.min(y1, y2);
                            final int maxZ = Math.max(z1, z2);
                            final int minZ = Math.min(z1, z2);
                            for (int x = maxX; x >= minX; x--) {
                                for (int y = maxY; y >= minY; y--) {
                                    for (int z = maxZ; z >= minZ; z--) {
                                        miniGame.addBlock(world.getBlockAt(x, y, z));
                                    }
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> addBlocks <world> <x> <y> <z> <x2> <y2> <z2>");
                        break;

                    }
                    case "nearByEntities": {
                        if (args.length >= 6) {
                            String cmd = String.join(" ", Arrays.copyOfRange(args, 5, args.length));
                            List<Entity> entities = player.getNearbyEntities(Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));
                            if (entities.size() != 0) {
                                while (entities.size() != 0) {
                                    miniGame.executeCommand((FreedyCommandSender) sender, cmd
                                                    .replace("{nearByEntityName}", entities.get(0).getName())
                                                    .replace("{nearByEntityType}", entities.get(0).getType().name())
                                                    .replace("{nearByEntityUUID}", entities.get(0).getUniqueId().toString())
                                                    .replace("{nearByEntityDistance}", String.valueOf(entities.get(0).getLocation().distance(player.getEyeLocation())))
                                            , player);
                                    entities.remove(0);
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> nearByEntities <x> <y> <z> <cmd>");
                        break;
                    }
                    case "entityNearByEntities": {
                        if (args.length >= 7) {
                            String cmd = String.join(" ", Arrays.copyOfRange(args, 6, args.length));
                            List<Entity> entities = Bukkit.getEntity(UUID.fromString(args[2])).getNearbyEntities(Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));
                            if (entities.size() != 0) {
                                while (entities.size() != 0) {
                                    miniGame.executeCommand((FreedyCommandSender) sender, cmd
                                                    .replace("{entityNearByEntityName}", entities.get(0).getName())
                                                    .replace("{entityNearByEntityType}", entities.get(0).getType().name())
                                                    .replace("{entityNearByEntityUUID}", entities.get(0).getUniqueId().toString())
                                                    .replace("{entityNearByEntityDistance}", String.valueOf(entities.get(0).getLocation().distance(player.getEyeLocation())))
                                            , player);
                                    entities.remove(0);
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> entityNearByEntities <entityUuid> <x> <y> <z> <cmd>");
                        break;
                    }
                    case "ride": {
                        if (args.length >= 4) {
                            Bukkit.getEntity(UUID.fromString(args[2])).setPassenger(Bukkit.getEntity(UUID.fromString(args[3])));
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> ride <entityUuid> <passengerUuid>");
                        break;
                    }
                    case "removeRide": {
                        Entity entity = Bukkit.getEntity(UUID.fromString(args[2]));
                        entity.removePassenger(entity.getPassenger());
                        break;
                    }
                    case "playSound": {
                        if (args.length >= 9) {
                            World world = Bukkit.getWorld(args[2]);
                            Location loc = new Location(world, Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));
                            world.playSound(loc, Sound.valueOf(args[6]), Float.parseFloat(args[7]), Float.parseFloat(args[8]));
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> playSound <world> <x> <y> <z> <sound> <volume> <pitch>");
                        break;
                    }
                    case "stopSound": {
                        if (args.length >= 3) {
                            for (Sound sound : Sound.values()) {
                                player.stopSound(sound);
                            }
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> stopSound <sound>");
                        break;
                    }
                    case "summon": {
                        if (args.length >= 9) {
                            World world = Bukkit.getWorld(args[2]);
                            Location loc = new Location(world, Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));
                            Entity entity = world.spawnEntity(loc, EntityType.valueOf(args[6]));
                            entity.setCustomName(args[7]);
                            entity.setCustomNameVisible(Boolean.parseBoolean(args[8]));
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> summon <world> <x> <y> <z> <Type> <name> <nameVisible>");
                        break;
                    }
                    case "blockParticle": {
                        if (args.length == 9) {
                            World world = Bukkit.getWorld(args[2]);
                            Location loc = new Location(world, Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));
                            ItemStack itemStack = new ItemStack(Material.valueOf(args[6]));
                            itemStack.setDurability(Short.parseShort(args[7]));
                            world.spawnParticle(Particle.ITEM_CRACK, loc, Integer.parseInt(args[8]), 0.1, 0.1, 0.1, 0.1, itemStack);
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> blockParticle <world> <x> <y> <z> <Type> <Data> <count>");
                        break;
                    }
                    case "particle": {
                        if (args.length >= 9) {
                            World world = Bukkit.getWorld(args[2]);
                            Location loc = new Location(world, Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));

                            if (Boolean.parseBoolean(args[8])) {
                                world.spawnParticle(Particle.valueOf(args[6]), Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]), 0, 0, 0, 0);
                            } else {
                                world.spawnParticle(Particle.valueOf(args[6]), loc, Integer.parseInt(args[7]));
                            }
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> particle <world> <x> <y> <z> <Type> <count> <isStatic>");
                        break;
                    }
                    case "fallingBlock": {
                        if (args.length >= 12) {
                            World world = Bukkit.getWorld(args[2]);
                            Location loc = new Location(world, Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));
                            loc.setYaw(Float.parseFloat(args[6]));
                            loc.setPitch(Float.parseFloat(args[7]));
                            FallingBlock fallingBlock = world.spawnFallingBlock(loc, Integer.parseInt(args[10]), (byte) Integer.parseInt(args[11]));
                            fallingBlock.setDropItem(false);
                            fallingBlock.setVelocity(loc.getDirection().multiply(Double.parseDouble(args[8])));
                            fallingBlock.setGravity(Boolean.parseBoolean(args[9]));
                            fallingBlock.setCustomNameVisible(false);
                            fallingBlock.setCustomName(player.getUniqueId().toString());
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> fallingBlock <world> <x> <y> <z> <yaw> <pitch> <speed> <hasGravity> <blockId> <blockData>");
                        break;
                    }
                    case "shoot": {
                        if (args.length >= 13) {
                            World world = Bukkit.getWorld(args[2]);
                            Location loc = new Location(world, Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));
                            loc.setYaw(Float.parseFloat(args[6]));
                            loc.setPitch(Float.parseFloat(args[7]));
                            Projectile projectile = (Projectile) world.spawnEntity(loc, EntityType.valueOf(args[8]));
                            if (player != null) projectile.setShooter(player);
                            projectile.setVelocity(loc.getDirection().multiply(Double.parseDouble(args[9])));
                            projectile.setGravity(Boolean.parseBoolean(args[10]));
                            projectile.setCustomName(args[11]);
                            projectile.setCustomNameVisible(Boolean.parseBoolean(args[12]));
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> shoot <world> <x> <y> <z> <yaw> <pitch> <Type> <speed> <hasGravity> <name> <nameVisible>");
                        break;
                    }
                    case "shootTarget": {
                        if (args.length >= 11) {
                            if (player == null) break;
                            World world = Bukkit.getWorld(args[2]);
                            Location loc = new Location(world, Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));
                            Projectile projectile = (Projectile) world.spawnEntity(loc, EntityType.valueOf(args[6]));
                            projectile.setShooter(player);
                            projectile.setVelocity(player.getEyeLocation().subtract(loc).toVector().multiply(Double.parseDouble(args[7])));
                            projectile.setGravity(Boolean.parseBoolean(args[8]));
                            projectile.setCustomName(args[9]);
                            projectile.setCustomNameVisible(Boolean.parseBoolean(args[10]));
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> shootTarget <world> <x> <y> <z> <Type> <speed> <hasGravity> <name> <nameVisible>");
                        break;
                    }
                    case "drop": {
                        if (args.length >= 9) {
                            World world = Bukkit.getWorld(args[2]);
                            Location loc = new Location(world, Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));
                            Entity entity = world.dropItem(loc, new ItemStack(Material.valueOf(args[6])));
                            entity.setCustomName(args[7]);
                            entity.setCustomNameVisible(Boolean.parseBoolean(args[8]));
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> drop <world> <x> <y> <z> <itemType> <name>");
                        break;
                    }
                    case "conLog": {
                        if (args.length >= 3) {
                            System.out.println(this.replace(player, miniGame, args[2]));
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> conLog <message>");
                        break;
                    }
                    case "give": {
                        if (args.length >= 3) {
                            this.miniGames.getNoneGame().giveItem(player, this.replace(player, miniGame, args[2]));
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> give <\ucee4\uc2a4\ud140\uc544\uc774\ud15c\uc774\ub984>");
                        break;
                    }
                    case "repairItem": {
                        player.getInventory().getItemInMainHand().setDurability((short) 0);
                        break;
                    }
                    case "cancelEvent": {
                        sender.sendMessage("false");
                        break;
                    }
                    case "setFile": {
                        if (args.length >= 4) {
                            String message5 = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                            this.miniGames.getNoneGame().setFile(args[2], message5.equals("none") ? null : message5);
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> setFileData <customData> <data>");
                        break;
                    }
                    case "saveFile": {
                        this.miniGames.getFileStore().save();
                        break;
                    }
                    case "updateBoard": {
                        if (args.length >= 7) {
                            String message5 = String.join(" ", Arrays.copyOfRange(args, 7, args.length));
                            final String s10 = args[2];
                            switch (s10) {
                                case "private": {
                                    miniGame.updateBoard(player, args[3], args[4], args[5], message5);
                                    break;
                                }
                                case "game": {
                                    for (final Player p9 : miniGame.playerList) {
                                        miniGame.updateBoard(p9, args[3], args[4], args[5], message5);
                                    }
                                    break;
                                }
                                case "public": {
                                    for (final Player p9 : Bukkit.getOnlinePlayers()) {
                                        miniGame.updateBoard(p9, args[3], args[4], args[5], message5);
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> updateBoard <private|public|game> <title> <lineName> <line> <message>");
                        break;
                    }
                    case "setBoard": {
                        if (args.length >= 4) {
                            String message5 = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                            final String s10 = args[2];
                            switch (s10) {
                                case "private": {
                                    miniGame.setBoard(player, message5);
                                    break;
                                }
                                case "game": {
                                    for (final Player p9 : miniGame.playerList) {
                                        miniGame.setBoard(p9, message5);
                                    }
                                    break;
                                }
                                case "public": {
                                    for (final Player p9 : Bukkit.getOnlinePlayers()) {
                                        miniGame.setBoard(p9, message5);
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> setBoard <private|public|game> <title>");
                        break;
                    }
                    case "shutDown":
                        List<Player> playerList = miniGame.playerList;
                        while (playerList.size() != 0) {
                            Player p = playerList.get(0);
                            playerList.remove(0);
                            miniGame.kick(p);
                            miniGame.stop();
                            final String defaultQuitGame = miniGame.getSettings("defaultQuitGame");
                            if (defaultQuitGame != null && !(defaultQuitGame.equals("none"))) {
                                FreedyMinigameMaker.miniGames.get(defaultQuitGame).add(p);
                            }
                        }
                        break;
                }
            }
            else {
                sender.sendMessage("§cUsage: /fut <player> <gui|teleport|sendMsg|sendActionBar|sendTitle|sendSound|gameMode|health|food|addPotion|removePotion|kit|openGui|closeGui|join|joinAll|knockBack|giveHand|cursor|kick|move|if|while|do|execute|executeCmd|executeDelayCmd|executeConCmd|executeConDelayCmd|topTp|resetBlocks|setData|addData|setPlayerData|addPlayerData|setBlock|give|repairItem|cancelEvent|setFile|saveFile|shutDown|||||||||> ...");
            }
        }
        else {
            sender.sendMessage("§cNo permission");
        }
        return true;
    }

    boolean checkIf(final Player player, final MiniGame miniGame, final String[] args) {
        return this.checkIf(this.replace(player, miniGame, args[2]), this.replace(player, miniGame, args[4]), args[3]);
    }

    String replace(final Player player, final MiniGame miniGame, String value) {
        value = value.replace("{player}", (player == null) ? "none" : player.getName()).replace("{gameName}", (miniGame == null) ? "none" : miniGame.gameName);
        if (miniGame != null) {
            value = miniGame.replaceAll(value, player);
        }
        return value;
    }

    boolean checkIf(final String var1, final String var2, final String math) {
        switch (math) {
            case "==": {
                return var1.equals(var2);
            }
            case "/=": {
                return !var1.equals(var2);
            }
            case "<": {
                return Double.parseDouble(var1) < Double.parseDouble(var2);
            }
            case "<=": {
                return Double.parseDouble(var1) <= Double.parseDouble(var2);
            }
            case ">": {
                return Double.parseDouble(var1) > Double.parseDouble(var2);
            }
            case ">=": {
                return Double.parseDouble(var1) >= Double.parseDouble(var2);
            }
            default: {
                return false;
            }
        }
    }
}
