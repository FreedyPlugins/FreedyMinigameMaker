package freedy.freedyminigamemaker.commands;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

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
                            miniGame.openInv(player, args[2]);
                        } else sender.sendMessage("§c사용법: /fut <player> openGui <메뉴이름>");
                        break;
                    case "closeGui":
                            player.closeInventory();
                        break;
                    default: sender.sendMessage("§c사용법: /fut <player> <sendMsg|set|openGui> ...");
                }
            } else sender.sendMessage("§c사용법: /fut <player> <sendMsg|set|openGui> ...");
        } else sender.sendMessage("§c권한이 없습니다");
        return true;
    }
        
}
