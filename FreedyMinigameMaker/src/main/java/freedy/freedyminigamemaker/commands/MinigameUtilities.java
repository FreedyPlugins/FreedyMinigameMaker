package freedy.freedyminigamemaker.commands;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MinigameUtilities implements CommandExecutor {

    FreedyMinigameMaker plugin;

    MiniGames miniGames;

    public MinigameUtilities(FreedyMinigameMaker plugin) {
        this.plugin = plugin;
        this.miniGames = plugin.miniGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender.hasPermission("freedyminigamemaker.admin")) {
            if (args.length >= 2) {
                Player player = Bukkit.getPlayer(args[0]);
                MiniGame miniGame = miniGames.getJoined(player);
                switch (args[1]) {
                    case "sendMsg":
                        if (args.length >= 4) {
                            StringBuilder message = new StringBuilder(args[3]);
                            for (int i = 4; i < args.length; i++) {
                                message.append(" ").append(args[i]);
                            }
                            switch (args[2]) {
                                case "public":
                                    Bukkit.getServer().broadcastMessage(message.toString());
                                    break;
                                case "private":
                                    player.sendMessage(message.toString());
                                    break;
                                case "team":
                                    for (String teamName : miniGame.teamPlayers.keySet()) {
                                        List<Player> teamPlayerList = miniGame.teamPlayers.get(teamName);
                                        if (teamPlayerList.contains(player)) {
                                            for (Player p : teamPlayerList)
                                                p.sendMessage(message.toString());
                                            break;
                                        }
                                    }
                                    break;
                                case "game":
                                    for (Player p : miniGame.playerList)
                                        p.sendMessage(message.toString());
                                    break;
                            }

                        } else {
                            sender.sendMessage("§c사용법: /fut <player> sendMsg <public|private|team|game> <메새지>");
                            sender.sendMessage("§c참고: <명령줄> 입력란에는 공백을 {spc}으로 넣으세요");
                        }
                        break;
                    case "sendTitle":
                        if (args.length >= 4) {
                            StringBuilder stringBuilder = new StringBuilder(args[6]);
                            for (int i = 7; i < args.length; i++) stringBuilder.append(" ").append(args[i]);
                            String message = stringBuilder.toString().replace("{spc}", " ");
                            List<String> stringList = new ArrayList<>(Arrays.asList(message.split("-")));
                            switch (args[2]) {
                                case "public":
                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        p.sendTitle(stringList.get(0), stringList.size() == 2 ? stringList.get(1) : null,
                                                Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                                    }
                                    break;
                                case "private":
                                        player.sendTitle(stringList.get(0), stringList.size() == 2 ? stringList.get(1) : null,
                                                Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));

                                    break;
                                case "team":
                                    for (String teamName : miniGame.teamPlayers.keySet()) {
                                        List<Player> teamPlayerList = miniGame.teamPlayers.get(teamName);
                                        if (teamPlayerList.contains(player)) {
                                            for (Player p : teamPlayerList) {
                                                p.sendTitle(stringList.get(0), stringList.size() == 2 ? stringList.get(1) : null,
                                                        Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                                            }
                                            break;
                                        }
                                    }
                                    break;
                                case "game":
                                    for (Player p : miniGame.playerList) {
                                        p.sendTitle(stringList.get(0), stringList.size() == 2 ? stringList.get(1) : null,
                                                Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                                    }
                                    break;
                            }

                        } else {
                            sender.sendMessage("§c사용법: /fut <player> sendTitle <public|private|team|game> <fadeIn> <stay> <fadeOut> <제목-부제목>");
                            sender.sendMessage("§c참고: <명령줄> 입력란에는 공백을 {spc}으로 넣으세요");
                        }
                        break;

                    case "set":
                        switch (args[2]) {
                            case "extraDamageMode":
                            if (args.length == 4) {
                                miniGame.getPlayerData(player).extraDamageMode = Boolean.parseBoolean(args[3]);
                            } else sender.sendMessage("§c사용법: /fut <player> set extraDamageMode <true|false>");
                            break;
                            case "resistingDamageMode":
                                if (args.length == 4) {
                                    miniGame.getPlayerData(player).resistingDamageMode = Boolean.parseBoolean(args[3]);
                                } else sender.sendMessage("§c사용법: /fut <player> set resistingDamageMode <true|false>");
                                break;
                            case "dropItemMode":
                                if (args.length == 4) {
                                    miniGame.getPlayerData(player).dropItemMode = Boolean.parseBoolean(args[3]);
                                } else sender.sendMessage("§c사용법: /fut <player> set dropItemMode <true|false>");
                                break;
                            default: sender.sendMessage("§c사용법: /fut <player> set <extraDamageMode|resistingDamageMode|dropItemMode> ...");
                        }
                        break;
                    case "openGui":
                        if (args.length >= 3) {
                            miniGames.getNoneGame().openInv(player, args[2]);
                        } else sender.sendMessage("§c사용법: /fut <player> openGui <메뉴이름>");
                        break;
                    case "closeGui":
                        player.closeInventory();
                        break;
                    case "join":
                        if (args.length == 3) {
                            miniGames.get(args[2]).add(player);
                        } else sender.sendMessage("§c사용법: /fut <player> join <게임이름>");
                        break;
                    case "if":
                        if (args.length >= 6) {
                            String var1 = args[2];
                            String var2 = args[4];
                            var1 = var1
                                    .replace("{player}", player.getName())
                                    .replace("{playerMode}", miniGame == null ? "none" : miniGame.getMode(player))
                                    .replace("{gameName}", miniGame == null ? "none" : miniGame.gameName);
                            var2 = var2
                                    .replace("{player}", player.getName())
                                    .replace("{playerMode}", miniGame == null ? "none" : miniGame.getMode(player))
                                    .replace("{gameName}", miniGame == null ? "none" : miniGame.gameName);
                            boolean result = var1.equals(var2);
                            if (args[3].equals("/=")) result = !result;
                            if (result) {
                                StringBuilder stringBuilder = new StringBuilder(args[5]);
                                for (int i = 6; i < args.length; i++) stringBuilder.append(" ").append(args[i]);
                                String message = stringBuilder.toString();
                                String area = StringUtils.substringBetween(message, "{do(", ")}");
                                //System.out.println(area);
                                if (area == null) return true;
                                String[] commands = area.split(" && ");
                                for (String cmd : commands)
                                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd
                                            .replace("{playerMode}", miniGame == null ? "none" : miniGame.getMode(player))
                                            .replace("{gameName}", miniGame == null ? "none" : miniGame.gameName)
                                            .replace("{player}", player.getName()));
                                String ifCmd = message.replace("{do(" + area + ")}", "");
                                if (!ifCmd.equals(""))
                                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), ifCmd);

                            }
                        } else sender.sendMessage("§c사용법: /fut <player> if <value1> == <value2> <명령줄>");
                        break;
                    case "executeCmd":
                        if (args.length >= 3) {
                            StringBuilder stringBuilder = new StringBuilder(args[2]);
                            for (int i = 3; i < args.length; i++) stringBuilder.append(" ").append(args[i]);
                            String message = stringBuilder.toString();
                            player.performCommand(message);
                        } else sender.sendMessage("§c사용법: /fut <player> executeCmd <명령줄>");
                        break;
                    case "executeDelayCmd":
                        if (args.length >= 4) {
                            StringBuilder stringBuilder = new StringBuilder(args[3]);
                            for (int i = 4; i < args.length; i++) stringBuilder.append(" ").append(args[i]);
                            String message = stringBuilder.toString();

                            Bukkit.getScheduler().runTaskLater(plugin, () -> player.performCommand(message), Integer.parseInt(args[2]));
                        } else sender.sendMessage("§c사용법: /fut <player> executeDelayCmd <딜레이틱> <명령줄>");
                        break;
                    case "executeConDelayCmd":
                        if (args.length >= 4) {
                            StringBuilder stringBuilder = new StringBuilder(args[3]);
                            for (int i = 4; i < args.length; i++) stringBuilder.append(" ").append(args[i]);
                            String message = stringBuilder.toString();
                            Bukkit.getScheduler().runTaskLater(plugin, () ->
                                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), message)
                                    , Integer.parseInt(args[2]));
                        } else sender.sendMessage("§c사용법: /fut <player> executeConCmd <딜레이틱> <명령줄>");
                        break;
                    case "topTpInWorldBoarder":
                        WorldBorder worldBorder = miniGame.getWorldBoarder();
                        Location location = worldBorder.getCenter();
                        int size = (int) (worldBorder.getSize() / 2);
                        int minPos = size * -1 + 2;
                        int maxPos = size - 2;
                        int x = ThreadLocalRandom.current().nextInt(minPos, maxPos);
                        int z = ThreadLocalRandom.current().nextInt(minPos, maxPos);
                        World world = location.getWorld();
                        int maxY = world.getMaxHeight();
                        for (int i = maxY; i > 0; i--) {
                            Location loc = new Location(world, x, i, z);
                            if (!loc.getBlock().getType().equals(Material.AIR)) {
                                location = location.add(x + 0.5, i + 1.5, z + 0.5);
                                break;
                            }
                        }
                        player.teleport(location);
                        break;
                }
            } else sender.sendMessage("§c사용법: /fut <player> <sendMsg|set|openGui|if|executeCmd> ...");
        } else sender.sendMessage("§c권한이 없습니다");
        return true;
    }
        
}
