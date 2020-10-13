// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.commands;

import org.bukkit.block.Block;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import java.util.Map;
import java.util.Iterator;
import freedy.freedyminigamemaker.MiniGame;
import org.bukkit.Material;
import org.bukkit.Location;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.plugin.Plugin;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.entity.Player;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import freedy.freedyminigamemaker.MiniGames;
import freedy.freedyminigamemaker.FreedyMinigameMaker;
import org.bukkit.command.CommandExecutor;

public class MinigameUtilities implements CommandExecutor
{
    FreedyMinigameMaker plugin;
    MiniGames miniGames;
    
    public MinigameUtilities(final FreedyMinigameMaker plugin) {
        this.plugin = plugin;
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender.hasPermission("freedyminigamemaker.admin")) {
            if (args.length >= 2) {
                final Player player = Bukkit.getPlayer(args[0]);
                final MiniGame miniGame = this.miniGames.getJoined(player);
                final String s = args[1];
                switch (s) {
                    case "teleport": {
                        if (args.length == 5) {
                            final MiniGame mg = this.miniGames.get(args[3]);
                            if (mg.getLocationIsExist(this.replace(player, miniGame, args[4]) + "Location")) {
                                final String s2 = args[2];
                                switch (s2) {
                                    case "private": {
                                        player.teleport(mg.getLocation(this.replace(player, miniGame, args[4]) + "Location"));
                                        break;
                                    }
                                    case "game": {
                                        for (final Player p2 : mg.playerList) {
                                            p2.teleport(mg.getLocation(this.replace(player, miniGame, args[4]) + "Location"));
                                        }
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> teleport <private|team|game> <\ubbf8\ub2c8\uac8c\uc784> <\uc800\uc7a5\ub41c\uc704\uce58>");
                        break;
                    }
                    case "sendMsg": {
                        if (args.length >= 4) {
                            final StringBuilder stringBuilder = new StringBuilder(args[3]);
                            for (int i = 4; i < args.length; ++i) {
                                stringBuilder.append(" ").append(args[i]);
                            }
                            final String message = this.replace(player, miniGame, stringBuilder.toString().replace("{spc}", " "));
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
                    case "sendActionBar": {
                        if (args.length >= 4) {
                            final StringBuilder stringBuilder = new StringBuilder(args[3]);
                            for (int i = 4; i < args.length; ++i) {
                                stringBuilder.append(" ").append(args[i]);
                            }
                            final String message = this.replace(player, miniGame, stringBuilder.toString().replace("{spc}", " "));
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
                            final StringBuilder stringBuilder = new StringBuilder(args[6]);
                            for (int i = 7; i < args.length; ++i) {
                                stringBuilder.append(" ").append(args[i]);
                            }
                            final String message = this.replace(player, miniGame, stringBuilder.toString().replace("{spc}", " "));
                            final List<String> stringList = new ArrayList<String>(Arrays.asList(message.split("-")));
                            final String s5 = args[2];
                            switch (s5) {
                                case "public": {
                                    for (final Player p5 : Bukkit.getOnlinePlayers()) {
                                        p5.sendTitle((String)stringList.get(0), (stringList.size() == 2) ? stringList.get(1) : null, Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                                    }
                                    break;
                                }
                                case "private": {
                                    player.sendTitle((String)stringList.get(0), (stringList.size() == 2) ? stringList.get(1) : null, Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                                    break;
                                }
                                case "game": {
                                    for (final Player p5 : miniGame.playerList) {
                                        p5.sendTitle((String)stringList.get(0), (stringList.size() == 2) ? stringList.get(1) : null, Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
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
                    case "sendSound": {
                        if (args.length == 4) {
                            final String s6 = args[2];
                            switch (s6) {
                                case "private": {
                                    player.playSound(player.getLocation(), Sound.valueOf(this.replace(player, miniGame, args[3])), 1.0f, 1.0f);
                                    break;
                                }
                                case "game": {
                                    for (final Player p7 : miniGame.playerList) {
                                        p7.playSound(p7.getLocation(), Sound.valueOf(this.replace(player, miniGame, args[3])), 1.0f, 1.0f);
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> sendSound <private|team|game> <\uc0ac\uc6b4\ub4dc>");
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
                    case "join": {
                        if (args.length == 3) {
                            this.miniGames.get(this.replace(player, miniGame, args[2])).add(player);
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> join <\uac8c\uc784\uc774\ub984>");
                        break;
                    }
                    case "if": {
                        if (args.length >= 6) {
                            final boolean result = this.checkIf(player, miniGame, args);
                            final StringBuilder stringBuilder2 = new StringBuilder(args[5]);
                            for (int j = 6; j < args.length; ++j) {
                                stringBuilder2.append(" ").append(args[j]);
                            }
                            final String message2 = stringBuilder2.toString();
                            final String elseCmd = StringUtils.substringBetween(message2, "{else(", ")}");
                            final String doCmd = StringUtils.substringBetween(message2, "{do(", ")}");
                            String secondCmd = message2.replace("{else(" + elseCmd + ")}", "").replace("{do(" + doCmd + ")}", "");
                            if (result) {
                                if (doCmd != null) {
                                    final String[] split;
                                    final String[] commands = split = doCmd.split(" && ");
                                    for (String cmd : split) {
                                        cmd = this.replace(player, miniGame, cmd);
                                        Bukkit.getServer().dispatchCommand(sender, cmd);
                                    }
                                }
                                if (!secondCmd.equals("")) {
                                    secondCmd = this.replace(player, miniGame, secondCmd);
                                    Bukkit.getServer().dispatchCommand(sender, secondCmd);
                                }
                            }
                            else if (elseCmd != null) {
                                final String[] split2;
                                final String[] commands = split2 = elseCmd.split(" && ");
                                for (String cmd : split2) {
                                    cmd = this.replace(player, miniGame, cmd);
                                    Bukkit.getServer().dispatchCommand(sender, cmd);
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> if <value1> <==|/=|>|>=|<|<=> <value2> <cmdLine>");
                        break;
                    }
                    case "while": {
                        if (args.length >= 6) {
                            final StringBuilder stringBuilder = new StringBuilder(args[5]);
                            for (int i = 6; i < args.length; ++i) {
                                stringBuilder.append(" ").append(args[i]);
                            }
                            final String message = stringBuilder.toString();
                            final String doCmd2 = StringUtils.substringBetween(message, "{do(", ")}");
                            String secondCmd2 = message.replace("{do(" + doCmd2 + ")}", "");
                            while (this.checkIf(player, miniGame, args)) {
                                if (doCmd2 != null) {
                                    final String[] split3;
                                    final String[] commands2 = split3 = doCmd2.split(" && ");
                                    for (final String cmd2 : split3) {
                                        Bukkit.getServer().dispatchCommand(sender, this.replace(player, miniGame, cmd2));
                                    }
                                }
                            }
                            if (!secondCmd2.equals("")) {
                                secondCmd2 = this.replace(player, miniGame, secondCmd2);
                                Bukkit.getServer().dispatchCommand(sender, secondCmd2);
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> while <value1> <==|/=|>|>=|<|<=> <value2> <cmdLine>");
                        break;
                    }
                    case "do": {
                        if (args.length >= 3) {
                            final StringBuilder stringBuilder = new StringBuilder(args[3]);
                            for (int i = 4; i < args.length; ++i) {
                                stringBuilder.append(" ").append(args[i]);
                            }
                            final String message = this.replace(player, miniGame, stringBuilder.toString());
                            final List<String> doArgs = Arrays.asList(message.split(", "));
                            final Map<String, String> replacement = new HashMap<String, String>();
                            for (int k = 0; k < doArgs.size() - 1; k += 2) {
                                replacement.put("{" + doArgs.get(k) + "}", doArgs.get(k + 1));
                            }
                            for (String cmd3 : miniGame.getMessageList(args[2] + "Cmd")) {
                                for (final String key : replacement.keySet()) {
                                    cmd3 = cmd3.replace(key, replacement.get(key));
                                }
                                Bukkit.getServer().dispatchCommand(sender, cmd3);
                            }
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> do <cmdBundle> dataName, data");
                        break;
                    }
                    case "execute": {
                        if (args.length >= 3) {
                            final StringBuilder stringBuilder = new StringBuilder(args[2]);
                            for (int i = 3; i < args.length; ++i) {
                                stringBuilder.append(" ").append(args[i]);
                            }
                            final String message = this.replace(player, miniGame, stringBuilder.toString());
                            Bukkit.getServer().dispatchCommand(sender, this.replace(player, miniGame, message));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> execute <cmdLine>");
                        break;
                    }
                    case "executeCmd": {
                        if (args.length >= 3) {
                            final StringBuilder stringBuilder = new StringBuilder(args[2]);
                            for (int i = 3; i < args.length; ++i) {
                                stringBuilder.append(" ").append(args[i]);
                            }
                            final String message = this.replace(player, miniGame, stringBuilder.toString());
                            player.performCommand(message);
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> executeCmd <cmdLine>");
                        break;
                    }
                    case "executeDelayCmd": {
                        if (args.length >= 4) {
                            final StringBuilder stringBuilder = new StringBuilder(args[3]);
                            for (int i = 4; i < args.length; ++i) {
                                stringBuilder.append(" ").append(args[i]);
                            }
                            final String message = this.replace(player, miniGame, stringBuilder.toString());
                            Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> player.performCommand(message), (long)Integer.parseInt(args[2]));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> executeDelayCmd <delayTick> <cmdLine>");
                        break;
                    }
                    case "executeConDelayCmd": {
                        if (args.length >= 4) {
                            final StringBuilder stringBuilder = new StringBuilder(args[3]);
                            for (int i = 4; i < args.length; ++i) {
                                stringBuilder.append(" ").append(args[i]);
                            }
                            final String message = this.replace(player, miniGame, stringBuilder.toString());
                            Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getServer().getConsoleSender(), message), (long)Integer.parseInt(args[2]));
                            break;
                        }
                        sender.sendMessage("§c\uc0ac\uc6a9\ubc95: /fut <player> executeConCmd <delayTick> <cmdLine>");
                        break;
                    }
                    case "topTpInWorldBoarder": {
                        final WorldBorder worldBorder = miniGame.getWorldBoarder();
                        Location location = worldBorder.getCenter();
                        final int size = (int)(worldBorder.getSize() / 2.0);
                        final int minPos = size * -1 + 2;
                        final int maxPos = size - 2;
                        final int x = ThreadLocalRandom.current().nextInt(minPos, maxPos);
                        final int z = ThreadLocalRandom.current().nextInt(minPos, maxPos);
                        final World world = location.getWorld();
                        int l;
                        for (int maxY = l = world.getMaxHeight(); l > 0; --l) {
                            final Location loc = new Location(world, (double)x, (double)l, (double)z);
                            if (!loc.getBlock().getType().equals((Object)Material.AIR)) {
                                location = location.add(x + 0.5, l + 1.5, z + 0.5);
                                break;
                            }
                        }
                        player.teleport(location);
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
                            final StringBuilder stringBuilder3 = new StringBuilder(args[3]);
                            for (int m = 4; m < args.length; ++m) {
                                stringBuilder3.append(" ").append(args[m]);
                            }
                            final String message5 = this.replace(player, miniGame, stringBuilder3.toString());
                            if (miniGame != null) {
                                miniGame.setCustomData(args[2], message5);
                            }
                            else {
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
                            final StringBuilder stringBuilder3 = new StringBuilder(args[3]);
                            for (int m = 4; m < args.length; ++m) {
                                stringBuilder3.append(" ").append(args[m]);
                            }
                            final String message5 = this.replace(player, miniGame, stringBuilder3.toString());
                            if (miniGame != null) {
                                miniGame.getPlayerData(player).setCustomData(args[2], message5);
                            }
                            else {
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
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> setBlock <world> <x> <y> <z> <blockType>");
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
                        player.getInventory().getItemInMainHand().setDurability((short)0);
                        break;
                    }
                    case "cancelEvent": {
                        sender.sendMessage("false");
                        break;
                    }
                    case "setFile": {
                        if (args.length >= 4) {
                            final StringBuilder stringBuilder3 = new StringBuilder(args[3]);
                            for (int m = 4; m < args.length; ++m) {
                                stringBuilder3.append(" ").append(args[m]);
                            }
                            final String message5 = this.replace(player, miniGame, stringBuilder3.toString());
                            this.miniGames.getNoneGame().setFile(args[2], message5);
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> setFileData <customData> <data>");
                        break;
                    }
                    case "saveFile": {
                        this.miniGames.getFileStore().save();
                    }
                    case "scoreBoard": {
                        if (args.length >= 5) {
                            final StringBuilder stringBuilder3 = new StringBuilder(args[4]);
                            for (int m = 5; m < args.length; ++m) {
                                stringBuilder3.append(" ").append(args[m]);
                            }
                            final String message5 = stringBuilder3.toString();
                            final String s10 = args[2];
                            switch (s10) {
                                case "private": {
                                    miniGame.setScoreBoard(player, Integer.parseInt(args[3]), message5);
                                    break;
                                }
                                case "game": {
                                    for (final Player p9 : miniGame.playerList) {
                                        miniGame.setScoreBoard(p9, Integer.parseInt(args[3]), message5);
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> scoreBoard <private|team|game> <lineIndex> <\uc2a4\ucf54\uc5b4\ubcf4\ub4dc\ub9e4\uc138\uc9c0>");
                        break;
                    }
                    case "createScoreBoard": {
                        if (args.length >= 3) {
                            final StringBuilder stringBuilder3 = new StringBuilder(args[2]);
                            for (int m = 3; m < args.length; ++m) {
                                stringBuilder3.append(" ").append(args[m]);
                            }
                            final String message5 = stringBuilder3.toString();
                            miniGame.createScoreBoard(player, message5);
                            break;
                        }
                        sender.sendMessage("§cUsage: /fut <player> createScoreBoard <\uc2a4\ucf54\uc5b4\ubcf4\ub4dc\ud0c0\uc774\ud2c0>");
                        break;
                    }
                    case "shutDown":
                        miniGame.shutDown();
                        break;
                }
            }
            else {
                sender.sendMessage("§cUsage: /fut <player> <teleport|sendMsg|sendTitle|sendSound|set|openGui|closeGui|join|if|executeCmd|executeDelayCmd|topTpInWorldBoarder|resetBlocks|setData|addData|setPlayerData|addPlayerData|shutDown> ...");
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
