package freedy.freedyminigamemaker.commands;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CommandBlock;
import org.bukkit.boss.BarColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.*;

public class MinigameUtilities implements CommandExecutor {

    FreedyMinigameMaker plugin;
    MiniGames miniGames;
    public static FreedyCommandSender newFreedyCommandSender;
    public MinigameUtilities(final FreedyMinigameMaker plugin) {
        this.plugin = plugin;
        this.miniGames = FreedyMinigameMaker.miniGames;
        newFreedyCommandSender = new FreedyCommandSender();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("freedyminigamemaker.admin")) {
            if (args.length >= 2) {
                Player player = Bukkit.getPlayer(args[0]);
                MiniGame miniGame = this.miniGames.getJoined(player);
                if (!(sender instanceof FreedyCommandSender)) {
                    try {
                        if (sender instanceof BlockCommandSender) {
                            final BlockCommandSender blockCommandSender = (BlockCommandSender) sender;
                            final Block commandBlock = blockCommandSender.getBlock();
                            if (commandBlock.getType().equals(Material.COMMAND_BLOCK)) {
                                final CommandBlock cmdBlock = (CommandBlock) commandBlock.getState();
                                final Location locatinon = cmdBlock.getLocation();
                                String p = "none";
                                double closest = 5;
                                for (Entity entity : locatinon.getWorld().getNearbyEntities(locatinon, 4, 4, 4)) {
                                    if (entity instanceof Player) {
                                        final double distance = entity.getLocation().distance(locatinon);
                                        if (distance < closest) {
                                            closest = distance;
                                            p = ((Player) entity).getName();
                                        }

                                    }
                                }

                                Bukkit.dispatchCommand(newFreedyCommandSender, cmdBlock.getCommand()
                                        .replace("@p", p));
                                return true;
                            }
                        }
                        sender = newFreedyCommandSender;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                final String s = args[1];
                switch (s) {
                    case "saveInv": {
                        if (args.length == 3) {
                            player.updateInventory();
                            List<ItemStack> itemStacks = new ArrayList<>();
                            PlayerInventory inventory = player.getInventory();
                            for (int i = 0; i < inventory.getSize(); i++) {
                                itemStacks.add(inventory.getItem(i));
                            }
                            miniGame.saveInv(args[2], itemStacks);
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> saveInv <invName>");
                        player.sendMessage("§cHow to Use: Store the Player's inventory");
                        break;
                    }
                    case "loadInv": {
                        if (args.length == 3) {
                            miniGame.loadInv(player, args[2]);
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> loadInv <invName>");
                        player.sendMessage("§cHow to Use: Overwrite the player's inventory with the inventory that was stored");
                        break;
                    }
                    case "gui": {
                        if (args.length == 3) {
                            miniGame.openGui(player, args[2]);
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> gui <\uba54\ub274\uc774\ub984>");
                        player.sendMessage("§cHow to Use: Open a new inventory menu for each player in the participating mini-game");
                        break;
                    }
                    case "setGui": {
                        if (args.length == 5) {
                            miniGame.setGui(player, this.replace(player, miniGame, args[2]), Integer.parseInt(args[3]), this.replace(player, miniGame, args[4]));
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> setGui <invName> <index> <customItemName>");
                        player.sendMessage("§cHow to Use: Set the Player's Inventory Menu for a participating mini-game");
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
                                    } else {
                                        player.sendMessage("§cHow to Use: /fut <player> setGuiItem name <invName> <index> <itemName>");
                                        player.sendMessage("§cHow to Use: 참여중인 미니게임에 플레이어의 인벤토리메뉴를 설정하기");
                                    }
                                    break;
                                case "lore":
                                    //fut <player> setGuiItem name <invName> <index> <line> <loreName>
                                    if (args.length >= 7) {
                                        String message = String.join(" ", Arrays.copyOfRange(args, 6, args.length));
                                        final String s3 = args[2];

                                        miniGame.setGuiItemLore(player,
                                                this.replace(player, miniGame, args[3]),
                                                Integer.parseInt(this.replace(player, miniGame, args[4])),
                                                Integer.parseInt(this.replace(player, miniGame, args[5])),
                                                this.replace(player, miniGame, message));
                                    } else {
                                        player.sendMessage("§cHow to Use: /fut <player> setGuiItem name <invName> <index> <line> <loreName>");
                                        player.sendMessage("§cHow to Use: 참여중인 미니게임에 플레이어의 인벤토리메뉴를 설정하기");
                                    }
                                    break;
                            }
                            break;
                        } else player.sendMessage("§cHow to Use: /fut <player> setGuiItem <name|lore> ...");
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
                        player.sendMessage("§cHow to Use: /fut <player> teleport <private|game> <\ubbf8\ub2c8\uac8c\uc784> <\uc800\uc7a5\ub41c\uc704\uce58>");
                        player.sendMessage("§cHow to Use: Teleport the Player to a location stored in a mini-game ");
                        break;
                    }
                    case "head": {
                        if (args.length == 4) {
                            Location location = player.getLocation();
                            location.setYaw(Float.parseFloat(args[2]));
                            location.setPitch(Float.parseFloat(args[3]));
                            player.teleport(location);
                            player.teleport(player);

                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> head <yaw> <pitch>");
                        player.sendMessage("§cHow to Use: Set direction of the player ");
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
                        player.sendMessage("§cHow to Use: /fut <player> tp <world> <x> <y> <z> [yaw] [pitch]");
                        player.sendMessage("§cHow to Use: Teleport the player to a coordinate");
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
                        player.sendMessage("§cHow to Use: /fut <player> sendMsg <public|private|team|game> <\uba54\uc0c8\uc9c0>");
                        player.sendMessage("§cHow to Use: Send a message to the player or player");
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
                        player.sendMessage("§cHow to Use: /fut <player> sendJson <public|private|team|game> <eventType>, <eventText>, <hover>, <message>-<jsonMessage>-<message>");
                        player.sendMessage("§cHow to Use: Send a Jason message to the player or player");
                        break;
                    }
                    case "sendActionBar": {
                        if (args.length >= 4) {
                            String message = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                            final String s3 = args[2];
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
                        player.sendMessage("§cHow to Use: /fut <player> sendActionBar <public|private|team|game> <\uc5d1\uc158\ubc14\ub9e4\uc138\uc9c0>");
                        player.sendMessage("§cHow to Use: Send an article bar message to the player or player");
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
                        player.sendMessage("§cHow to Use: /fut <player> sendTitle <public|private|team|game> <fadeIn> <stay> <fadeOut> <\uc81c\ubaa9-\ubd80\uc81c\ubaa9>");
                        player.sendMessage("§cHow to Use: Send title messages to players or players");
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
                        player.sendMessage("§cHow to Use: /fut <player> sendBossBar <public|private|team|game> <barName> <progress> <color> <message|none>");
                        player.sendMessage("§cHow to Use: Floating boss bars to players or players");
                        player.sendMessage("§c" + "color list: BLUE GREEN PINK PURPLE RED WHITE YELLOW");
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
                        player.sendMessage("§cHow to Use: /fut <player> sendSound <public|private|team|game> <sound> <volume> <pitch>");
                        player.sendMessage("§cHow to Use: Play sound to the player or player");
                        player.sendMessage("\uc0ac\uc6b4\ub4dc\ubaa9\ub85d: https://helpch.at/docs/1.12.2/org/bukkit/Sound.html");
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
                        player.sendMessage("§cHow to Use: /fut <player> gamemode <private|team|game> <\uac8c\uc784\ubaa8\ub4dc>");
                        player.sendMessage("§cHow to Use: Set the Player or Player's Game Mode");
                        player.sendMessage("§cPlease write all the game modes in English capital letters.");
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
                        player.sendMessage("§cHow to Use: /fut <player> hide <private|public|game> <player>");
                        player.sendMessage("§cHow to Use: Hide a player or player from a player");
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
                        player.sendMessage("§cHow to Use: /fut <player> show <private|public|game> <player>");
                        player.sendMessage("§cHow to Use: Show a player or player what kind of player");
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
                        player.sendMessage("§cHow to Use: /fut <player> health <private|team|game> <health>");
                        player.sendMessage("§cHow to Use: To set a player's or player's physical strength");
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
                        player.sendMessage("§cHow to Use: /fut <player> food <private|team|game> <foodLevel>");
                        player.sendMessage("§cHow to Use: Set the hunger of the player or players");
                        break;
                    }
                    case "addPotion": {
                        if (args.length >= 6) {
                            final PotionEffect potionEffect = new PotionEffect(PotionEffectType.getByName(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                            final String s8 = args[2];
                            switch (s8) {
                                case "private": {
                                    player.addPotionEffect(potionEffect);
                                    break;
                                }
                                case "game": {
                                    for (final Player p2 : miniGame.playerList) {
                                        p2.addPotionEffect(potionEffect);
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> addPotion <private|game> <potionName> <duration> <amplification>");
                        player.sendMessage("§cHow to Use: Give the Player or Player a Potion Effect");
                        break;
                    }
                    case "removePotion": {
                        if (args.length == 3) {
                            final String s9 = args[2];
                            player.removePotionEffect(PotionEffectType.getByName(s9));
                            break;
                        } else if (args.length == 2) {
                            for (final PotionEffect effect : player.getActivePotionEffects()) {
                                player.removePotionEffect(effect.getType());
                            }
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> removePotion <potion>");
                        player.sendMessage("§cHow to Use: To remove a player or player's port effect");
                        break;
                    }
                    case "kit": {
                        if (args.length >= 3) {
                            this.miniGames.getNoneGame().applyKit(player, this.replace(player, miniGame, args[2]));
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> kit <\ud0b7\uc774\ub984>");
                        player.sendMessage("§cHow to Use: To apply a saved kit to the player");
                        break;
                    }
                    case "openGui": {
                        if (args.length >= 3) {
                            this.miniGames.getNoneGame().openInv(player, this.replace(player, miniGame, args[2]));
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> openGui <\uba54\ub274\uc774\ub984>");
                        player.sendMessage("§cHow to Use: Open a saved GUI menu");
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
                        player.sendMessage("§cHow to Use: /fut <player> addToTeam <team>");
                        player.sendMessage("§cHow to Use: To add a player to the scoreboard team");
                        break;
                    }
                    case "setWeather": {
                        if (args.length >= 3) {
                            player.setPlayerWeather(WeatherType.valueOf(args[2]));
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> setWeather <weatherType>");
                        player.sendMessage("§cHow to Use: Set personal weather for players CLEAR, DOWNFALL");
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
                        player.sendMessage("§cHow to Use: /fut <player> setTime <timeTick>");
                        player.sendMessage("§cHow to Use: Set player's personal time");

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
                        player.sendMessage("§cHow to Use: /fut <player> loadChunk <world> <x> <z>");
                        player.sendMessage("§cHow to Use: Load chunk");
                        break;
                    }
                    case "regenChunk": {
                        if (args.length == 5) {
                            Bukkit.getWorld(args[2]).regenerateChunk(Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> regenChunk <world> <x> <z>");
                        player.sendMessage("§cHow to Use: Regen chunk");

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
                        player.sendMessage("§cHow to Use: /fut <player> fakeBlock <world> <x> <y> <z> <block>");
                        player.sendMessage("§cHow to Use: Install blocks that are visible only to players");
                        break;
                    }
                    case "join": {
                        if (args.length == 3) {
                            MiniGame mg = this.miniGames.get(this.replace(player, miniGame, args[2]));
                            mg.add(player);
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> join <\uac8c\uc784\uc774\ub984>");
                        player.sendMessage("§cHow to Use: Getting players involved in mini-games");
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
                        player.sendMessage("§cHow to Use: /fut <player> joinAll <\uac8c\uc784\uc774\ub984>");
                        player.sendMessage("§cHow to Use: Get all online players who are not participating in a mini game to participate in a mini game");
                        break;
                    }
                    case "knockBack": {
                        if (args.length == 5) {
                            player.setVelocity(new Vector(Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4])));
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> knockBack <x> <y> <z>");
                        player.sendMessage("§cHow to Use: Give the player momentum");
                        break;
                    }
                    case "entityKnockBack": {
                        if (args.length == 6) {
                            Bukkit.getEntity(UUID.fromString(args[2])).setVelocity(new Vector(Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5])));
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> entityKnockBack <entityUuid> <x> <y> <z>");
                        player.sendMessage("§cHow to Use: Give the entity momentum");
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
                        player.sendMessage("§cHow to Use: /fut <player> addKnockBack <amount> <yaw> <pitch>");
                        player.sendMessage("§cHow to Use: Cycle the propulsion in the direction of the amount");
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
                        player.sendMessage("§cHow to Use: /fut <player> entityAddKnockBack <entityUuid> <amount> <yaw> <pitch>");
                        player.sendMessage("§cHow to Use: Gives the entity a boost in the direction of an amount");

                        break;
                    }
                    case "giveHand": {
                        if (args.length == 3) {
                            this.miniGames.getNoneGame().giveItemHand(player, this.replace(player, miniGame, args[2]));
                            break;
                        } else if (args.length == 4) {
                            this.miniGames.getNoneGame().giveItemHand(player, this.replace(player, miniGame, args[2]), Integer.parseInt(args[3]));
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> giveHand <customItem> [amount]");
                        player.sendMessage("§cHow to Use: Give items stored in the player's hand");
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
                        player.sendMessage("§cHow to Use: /fut <player> giveCursor <customItem> <cursor> [amount]");
                        player.sendMessage("§cHow to Use: Cursor Stored Item Cycle");
                        break;
                    }
                    case "setHelmet": {
                        if (args.length == 3) {
                            this.miniGames.getNoneGame().setHelmet(player, this.replace(player, miniGame, args[2]));
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> setHelmet <customItem>");
                        player.sendMessage("§cHow to Use: Overlay items stored on the player's head");
                        break;
                    }
                    case "setChestplate": {
                        if (args.length == 3) {
                            this.miniGames.getNoneGame().setChestplate(player, this.replace(player, miniGame, args[2]));
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> setChestplate <customItem>");
                        player.sendMessage("§cHow to Use: Overlay items stored on the player's upper body");
                        break;
                    }
                    case "setLeggings": {
                        if (args.length == 3) {
                            this.miniGames.getNoneGame().setLeggings(player, this.replace(player, miniGame, args[2]));
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> setLeggings <customItem>");
                        player.sendMessage("§cHow to Use: Wrap items stored on the player's pants");
                        break;
                    }
                    case "setBoots": {
                        if (args.length == 3) {
                            this.miniGames.getNoneGame().setBoots(player, this.replace(player, miniGame, args[2]));
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> setBoots <customItem>");
                        player.sendMessage("§cHow to Use: Covering items stored on the player's feet");
                        break;
                    }
                    case "cursor": {
                        if (args.length == 3) {
                            player.getInventory().setHeldItemSlot(Integer.parseInt(args[2]));
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> cursor <cursor>");
                        player.sendMessage("§cHow to Use: Replace the player's cursor");
                        break;
                    }
                    case "kill": {
                        if (args.length == 3) {
                            Bukkit.getEntity(UUID.fromString(args[2])).remove();
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> kill <entityUuid>");
                        player.sendMessage("§cHow to Use: Remove an entity completely");
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
                        player.sendMessage("§cHow to Use: /fut <player> damage <entityUuid> <damage>");
                        player.sendMessage("§cHow to Use: Damage to entities");
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
                        player.sendMessage("§cHow to Use: /fut <player> move <\uac8c\uc784\uc774\ub984>");
                        player.sendMessage("§cHow to Use: Moving the Player to a Mini Game");
                        break;
                    }
                    case "if": {
                        if (args.length >= 6) {

                            String message2 = String.join(" ", Arrays.copyOfRange(args, 5, args.length));
                            message2 = this.replace(player, miniGame, message2);
                            final String elseCmd = MiniGame.getSubFunc(message2, "{else(");
                            message2 = message2.replace("{else(" + elseCmd + ")}", "");
                            final String doCmd = MiniGame.getSubFunc(message2, "{do(");
                            message2 = message2.replace("{do(" + doCmd + ")}", "");
                            final boolean result = this.checkIf(args[2], args[4], args[3]);
                            if (result) {
                                if (doCmd != null) {
                                    final String[] split = doCmd.split(" && ");
                                    for (String cmd : split) {
                                        if (miniGame.executeCommand((FreedyCommandSender) sender, cmd, player).equals("false"))
                                            return true;
                                    }
                                }
                                miniGame.executeCommand((FreedyCommandSender) sender, message2, player);

                            } else if (elseCmd != null) {
                                final String[] split2 = elseCmd.split(" && ");
                                for (String cmd : split2) {
                                    if (miniGame.executeCommand((FreedyCommandSender) sender, cmd, player).equals("false"))
                                        return true;

                                }
                            }

                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> if <value1> <==|/=|>|>=|<|<=> <value2> <cmdLine>");
                        break;
                    }
                    case "while": {
                        if (args.length >= 6) {

                            String message = String.join(" ", Arrays.copyOfRange(args, 5, args.length));
                            message = this.replace(player, miniGame, message);

                            final String doCmd2 = MiniGame.getSubFunc(message, "{do(");
                            message = message.replace("{do(" + doCmd2 + ")}", "");

                            while (this.checkIf(args[2], args[4], args[3])) {
                                if (doCmd2 != null) {
                                    final String[] split3 = doCmd2.split(" && ");
                                    for (final String cmd2 : split3) {
                                        if (miniGame.executeCommand((FreedyCommandSender) sender, cmd2, player).equals("false"))
                                            return true;

                                    }
                                }
                            }
                            miniGame.executeCommand((FreedyCommandSender) sender, message, player);
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> while <value1> <==|/=|>|>=|<|<=> <value2> <cmdLine>");
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
                        player.sendMessage("§cHow to Use: /fut <player> do <cmdBundle> [dataName, data]");
                        break;
                    }
                    case "execute": {
                        if (args.length >= 3) {
                            String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), this.replace(player, miniGame, message));
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> execute <cmdLine>");
                        player.sendMessage("§cHow to Use: Run the vanilla command from the console");
                        break;
                    }
                    case "executeCmd": {
                        if (args.length >= 3) {
                            String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                            player.performCommand(message);
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> executeCmd <cmdLine>");
                        player.sendMessage("§cHow to Use: Allow the Player to execute commands");
                        break;
                    }
                    case "executeDelayCmd": {
                        if (args.length >= 4) {
                            String message = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                            Bukkit.getScheduler().runTaskLater(this.plugin, () -> player.performCommand(message), Integer.parseInt(args[2]));
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> executeDelayCmd <delayTick> <cmdLine>");
                        player.sendMessage("§cHow to Use: Allow the Player to execute the command after a few ticks");
                        break;
                    }
                    case "executeConCmd": {
                        if (args.length >= 3) {
                            String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                            Bukkit.getServer().dispatchCommand(sender, message);

                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> executeConCmd <cmdLine>");
                        player.sendMessage("§cHow to Use: Allow command to run from console");
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
                        player.sendMessage("§cHow to Use: /fut <player> executeConDelayCmd <delayTick> <cmdLine>");
                        player.sendMessage("§cHow to Use: Allow a few ticks to run commands on the console");
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
                        miniGame.cancelAllTask();
                        break;

                    }
                    case "resetBlocks": {
                        if (args.length == 3) {
                            this.miniGames.get(args[2]).resetBlocks();
                            break;
                        } else if (args.length == 4) {
                            this.miniGames.get(args[2]).resetBlocks(args[3]);
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut none resetBlocks <gameName> [blocksName]");
                        player.sendMessage("§cHow to Use: If gameType is a build, initialize blocks installed or destroyed by the player except sand or gravel");
                        break;
                    }
                    case "setData": {
                        if (args.length >= 4) {
                            String message5 = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                            if (miniGame != null) {
                                miniGame.setCustomData(args[2], message5);
                            } else {
                                player.sendMessage("§c\ud50c\ub808\uc774\uc5b4\uac00 \ubbf8\ub2c8\uac8c\uc784\uc5d0 \ucc38\uc5ec \uc911\uc774\uc9c0 \uc54a\uae30 \ub54c\ubb38\uc5d0 \ub370\uc774\ud0c0\ub97c \uc800\uc7a5\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.");
                            }
                            break;
                        }
                        player.sendMessage("§cHow to Use: /fut <player> setData <customData> <data>");
                        break;
                    }
                    case "addData": {
                        if (args.length < 4) {
                            player.sendMessage("§cUsage: /fut <player> addData <customData> <amount>");
                            break;
                        }
                        if (miniGame != null) {
                            miniGame.setCustomData(args[2], String.valueOf(Double.parseDouble(miniGame.getCustomData(args[2])) + Double.parseDouble(args[3])));
                            break;
                        }
                        player.sendMessage("§c\ud50c\ub808\uc774\uc5b4\uac00 \ubbf8\ub2c8\uac8c\uc784\uc5d0 \ucc38\uc5ec \uc911\uc774\uc9c0 \uc54a\uae30 \ub54c\ubb38\uc5d0 \ub370\uc774\ud0c0\ub97c \uc800\uc7a5\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.");
                        break;
                    }
                    case "setPlayerData": {
                        if (args.length >= 4) {
                            String message5 = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                            if (miniGame != null) {
                                miniGame.getPlayerData(player).setCustomData(args[2], message5);
                            } else {
                                player.sendMessage("§c\ud50c\ub808\uc774\uc5b4\uac00 \ubbf8\ub2c8\uac8c\uc784\uc5d0 \ucc38\uc5ec \uc911\uc774\uc9c0 \uc54a\uae30 \ub54c\ubb38\uc5d0 \ub370\uc774\ud0c0\ub97c \uc800\uc7a5\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.");
                            }
                            break;
                        }
                        player.sendMessage("§cUsage: /fut <player> setPlayerData <customData> <data>");
                        break;
                    }
                    case "addPlayerData": {
                        if (args.length < 4) {
                            player.sendMessage("§cUsage: /fut <player> addPlayerData <customData> <amount>");
                            break;
                        }
                        if (miniGame != null) {
                            miniGame.getPlayerData(player).setCustomData(args[2], String.valueOf(Double.parseDouble(miniGame.getPlayerData(player).getCustomData(args[2])) + Double.parseDouble(args[3])));
                            break;
                        }
                        player.sendMessage("§c\ud50c\ub808\uc774\uc5b4\uac00 \ubbf8\ub2c8\uac8c\uc784\uc5d0 \ucc38\uc5ec \uc911\uc774\uc9c0 \uc54a\uae30 \ub54c\ubb38\uc5d0 \ub370\uc774\ud0c0\ub97c \uc800\uc7a5\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.");
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
                            break;
                        }
                        player.sendMessage("§cUsage: /fut <player> setBlockWE <world> <x> <y> <z> <blockCode> [blockData]");
                        player.sendMessage("§cHow to Use: 1.12.2 Installing blocks from a dedicated worldEdit");
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
                        player.sendMessage("§cUsage: /fut <player> setBlock <world> <x> <y> <z> <blockType>");
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
                        player.sendMessage("§cUsage: /fut <player> setBlockChange <world> <x> <y> <z> <blockType>");
                        break;
                    }
                    case "setBlocksWE": {
                        if (args.length >= 10) {
                            break;
                        }
                        player.sendMessage("§cUsage: /fut <player> setBlocksWE <world> <x> <y> <z> <x2> <y2> <z2> <blockCode> [blockData]");
                        player.sendMessage("§cHow to Use: 1.12.2 Installing Blocks from a WorldEdit");
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
                        player.sendMessage("§cUsage: /fut <player> relativeBlock <world> <x> <y> <z> <SOUTH|WEST|NORTH|EAST|UP|DOWN|");
                        break;
                    }

                    case "addBlock": {
                        if (args.length >= 6) {
                            final World world2 = Bukkit.getWorld(args[2]);
                            final int x2 = Integer.parseInt(args[3]);
                            final int y1 = Integer.parseInt(args[4]);
                            final int z2 = Integer.parseInt(args[5]);
                            final Block block = world2.getBlockAt(x2, y1,z2);
                            if (!(args.length == 7)) {
                                miniGame.addBlock(block);
                            } else {
                                miniGame.addBlock(args[6], block.getState());
                            }
                            break;
                        }
                        player.sendMessage("§cUsage: /fut <player> addBlock <world> <x> <y> <z> [blocksName]");
                        player.sendMessage("§cHow to Use: Adding blocks to blocks to be initialized");
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
                        player.sendMessage("§cUsage: /fut <player> setBlocks <world> <x> <y> <z> <x2> <y2> <z2> <blockType>");
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
                        player.sendMessage("§cUsage: /fut <player> addBlocks <world> <x> <y> <z> <x2> <y2> <z2>");
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
                        player.sendMessage("§cUsage: /fut <player> nearByEntities <x> <y> <z> <cmd>");
                        player.sendMessage("§cHow to Use: Run as many entities in the radius of the player");
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
                        player.sendMessage("§cUsage: /fut <player> entityNearByEntities <entityUuid> <x> <y> <z> <cmd>");
                        player.sendMessage("§cHow to Use: Running as many entities in the entity radius of");
                        break;
                    }
                    case "goalTarget": {
                        if (args.length >= 4) {
                            Entity entity = Bukkit.getEntity(UUID.fromString(args[2]));
                            Creature target = (Creature) entity;
                            target.setTarget(((LivingEntity) Bukkit.getEntity(UUID.fromString(args[3]))));
                            break;
                        }
                        player.sendMessage("§cUsage: /fut <player> goalTarget <entityUuid> <passengerUuid>");
                        player.sendMessage("§cHow to Use: Targeting entities on an entity");
                        break;
                    }
                    case "ride": {
                        if (args.length >= 4) {
                            Bukkit.getEntity(UUID.fromString(args[2])).setPassenger(Bukkit.getEntity(UUID.fromString(args[3])));
                            break;
                        }
                        player.sendMessage("§cUsage: /fut <player> ride <entityUuid> <passengerUuid>");
                        player.sendMessage("§cHow to Use: Entity is targeting an entity");
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
                        player.sendMessage("§cUsage: /fut <player> playSound <world> <x> <y> <z> <sound> <volume> <pitch>");
                        player.sendMessage("§cHow to Use: Play a sound from a location");
                        break;
                    }
                    case "stopSound": {
                        if (args.length >= 3) {
                            for (Sound sound : Sound.values()) {
                                player.stopSound(sound);
                            }
                            break;
                        }
                        player.sendMessage("§cUsage: /fut <player> stopSound <sound>");
                        player.sendMessage("§cHow to Use: Stop a sound from the player");
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
                        player.sendMessage("§cUsage: /fut <player> summon <world> <x> <y> <z> <Type> <name> <nameVisible>");
                        player.sendMessage("§cHow to Use: Summon entities");
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
                        player.sendMessage("§cUsage: /fut <player> blockParticle <world> <x> <y> <z> <Type> <Data> <count>");
                        player.sendMessage("§cHow to Use: To execute a particle that breaks a block");
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
                        player.sendMessage("§cUsage: /fut <player> particle <world> <x> <y> <z> <Type> <count> <isStatic>");
                        player.sendMessage("§cHow to Use: To execute a particle:");
                        break;
                    }
                    case "fallingBlock": {
                        if (args.length >= 11) {
                            World world = Bukkit.getWorld(args[2]);
                            Location loc = new Location(world, Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));
                            loc.setYaw(Float.parseFloat(args[6]));
                            loc.setPitch(Float.parseFloat(args[7]));
                            assert world != null;
                            FallingBlock fallingBlock = world.spawnFallingBlock(loc, Material.valueOf(args[10]).createBlockData());
                            fallingBlock.setDropItem(false);
                            fallingBlock.setVelocity(loc.getDirection().multiply(Double.parseDouble(args[8])));
                            fallingBlock.setGravity(Boolean.parseBoolean(args[9]));
                            fallingBlock.setCustomNameVisible(false);
                            assert player != null;
                            fallingBlock.setCustomName(player.getUniqueId().toString());
                            break;
                        }
                        player.sendMessage("§cUsage: /fut <player> fallingBlock <world> <x> <y> <z> <yaw> <pitch> <speed> <hasGravity> <blockType>");
                        player.sendMessage("§cHow to Use: Summon falling blocks");
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
                        player.sendMessage("§cUsage: /fut <player> shoot <world> <x> <y> <z> <yaw> <pitch> <Type> <speed> <hasGravity> <name> <nameVisible>");
                        player.sendMessage("§cHow to Use: Launch projectiles in a direction to a location");
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
                        player.sendMessage("§cUsage: /fut <player> shootTarget <world> <x> <y> <z> <Type> <speed> <hasGravity> <name> <nameVisible>");
                        player.sendMessage("§cHow to Use: Fire a projectile at the player");
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
                        player.sendMessage("§cUsage: /fut <player> drop <world> <x> <y> <z> <itemType> <name>");
                        player.sendMessage("§cHow to Use: Drop items to a location");
                        break;
                    }
                    case "conLog": {
                        if (args.length >= 3) {
                            System.out.println(this.replace(player, miniGame, args[2]));
                            break;
                        }
                        player.sendMessage("§cUsage: /fut <player> conLog <message>");
                        player.sendMessage("§cHow to Use: Output a message to the console");
                        break;
                    }
                    case "give": {
                        if (args.length == 3) {
                            this.miniGames.getNoneGame().giveItem(player, this.replace(player, miniGame, args[2]));
                            break;
                        }
                        else if (args.length == 4) {
                            this.miniGames.getNoneGame().giveItem(player, this.replace(player, miniGame, args[2]), Integer.parseInt(args[3]));
                            break;
                        }
                        player.sendMessage("§cUsage: /fut <player> give <customItem> [amount]");
                        player.sendMessage("§cHow to Use: Give the Player an Item");
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
                        player.sendMessage("§cUsage: /fut <player> setFileData <customData> <data>");
                        break;
                    }
                    case "saveFile": {
                        this.miniGames.getFileStore().saveConfig();
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
                        player.sendMessage("§cUsage: /fut <player> updateBoard <private|public|game> <title> <lineName> <line> <message>");
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
                        player.sendMessage("§cUsage: /fut <player> setBoard <private|public|game> <title>");
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
                sender.sendMessage("§cUsage: /fut <player> <gui|setGui|resetGui|teleport|tp|sendMsg|sendJson|sendActionBar|sendTitle|sendBossBar|sendSoundgameMode|hide|show|health|food|addPotion|removePotion|kit|openGui|closeGui|addToTeam|setWeather|resetWeather|setTime|resetTime|removeFire|loadChunk|regenChunk|fakeBlock|join|joinAll|knockBack|entityKnockBack|addKnockBack|entityAddKnockBack|giveHand|giveCursor|setHelmet|setChestplate|setLeggings|setBoots|cursor|kill|damage|kick|move|if|while|do|execute|executeCmd|executeDelayCmd|executeConCmd|executeConDelayCmd|topTp|cancelAllDelayCmd|resetBlocks|setData|addData|setPlayerData|addPlayerData|nearByEntities|entityNearByEntities|goalTarget|ride|removeRide|playSound|stopSound|summon|blockParticle|particle|fallingBlock|shoot|shootTarget|drop|conLog|give|repairItem|cancelEvent|setFile|saveFile|shutDown> ...");
            }
        }
        else {
            sender.sendMessage("§cNo permission");
        }
        return true;
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
