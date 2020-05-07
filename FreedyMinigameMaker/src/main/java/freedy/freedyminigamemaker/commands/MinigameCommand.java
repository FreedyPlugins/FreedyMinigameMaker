package freedy.freedyminigamemaker.commands;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MinigameCommand implements CommandExecutor {

    MiniGames miniGames;

    private final FreedyMinigameMaker plugin;

    public MinigameCommand(FreedyMinigameMaker plugin) {
        this.plugin = plugin;
        this.miniGames = plugin.miniGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Location playerLocation = player.getLocation();
        if (args.length != 0) {
            switch (args[0]) {
                case "join":
                    if (args.length == 2) miniGames.get(args[1]).add(player);
                    else if (args.length == 3) miniGames.get(args[1]).add(Bukkit.getPlayer(args[2]));
                    else player.sendMessage("§c사용법: /fmg join <게임이름> [플레이어]");
                    break;
                case "quit":
                    if (args.length == 2) {
                        miniGames.get(args[1]).remove(player);
                        miniGames.get(args[1]).stop();
                    } else player.sendMessage("§c사용법: /fmg quit <게임이름>");
                    break;
                case "create":
                    if (player.hasPermission("freedyminigamemaker.admin")) {
                        if (args.length == 6) {
                            miniGames.get(args[1]).create(
                                    Integer.parseInt(args[2]),
                                    Integer.parseInt(args[3]),
                                    Integer.parseInt(args[4]),
                                    Integer.parseInt(args[5])
                            );
                            miniGames.add(args[1]);
                            player.sendMessage("§6" + "게임 " + args[1] + "이 생성되었습니다");
                        } else player.sendMessage("§c사용법: /fmg create <게임이름> <최대인원> <시작인원> <시작대기초> <종료대기초>");
                    } else player.sendMessage("§c권한이 없습니다.");
                    break;
                case "remove":
                    if (player.hasPermission("freedyminigamemaker.admin")) {
                        if (args.length == 2) {
                            miniGames.getEditor(args[1]).remove(args[1]);
                            miniGames.remove(args[1]);
                            player.sendMessage("§6게임 " + args[1] + "이 삭제되었습니다");
                        } else player.sendMessage("§c사용법: /fmg remove <게임이름>");
                    } else player.sendMessage("§c권한이 없습니다.");
                    break;
                case "set":
                    if (player.hasPermission("freedyminigamemaker.admin")) {
                        String team;
                        if (args.length > 2) {
                            switch (args[2]) {
                                case "wait":
                                    //String path, String dataType
                                    miniGames.getEditor(args[1]).setLocation("waitLocation",
                                            playerLocation.getWorld().getName(),
                                            playerLocation.getX(),
                                            playerLocation.getY(),
                                            playerLocation.getZ(),
                                            playerLocation.getYaw(),
                                            playerLocation.getPitch());
                                    player.sendMessage("§6위치가 " + args[1] + " 게임에 대기 위치에 저장 되었습니다");
                                    break;
                                case "start":
                                    //String path, String dataType
                                    team = "default";
                                    if (args.length == 4) team = args[3];
                                    miniGames.getEditor(args[1]).addLocation(team + "StartLocation",
                                            playerLocation.getWorld().getName(),
                                            playerLocation.getX(),
                                            playerLocation.getY(),
                                            playerLocation.getZ(),
                                            playerLocation.getYaw(),
                                            playerLocation.getPitch());
                                    player.sendMessage("§6위치가 " + args[1] + " 게임에 " + team + " 시작위치에 저장되었습니다");
                                    break;
                                case "end":
                                    //String path, String dataType
                                    miniGames.getEditor(args[1]).setLocation("endLocation",
                                            playerLocation.getWorld().getName(),
                                            playerLocation.getX(),
                                            playerLocation.getY(),
                                            playerLocation.getZ(),
                                            playerLocation.getYaw(),
                                            playerLocation.getPitch());
                                    player.sendMessage("§6위치가 " + args[1] + " 게임에 종료 위치에 저장 되었습니다");
                                    break;
                                case "addBlock":
                                    if (args.length == 4) {
                                        List<String> allowedBlocks = plugin.getConfig().getStringList("miniGames." + args[1] + ".allowedBlocks");
                                        allowedBlocks.add(args[3]);
                                        plugin.getConfig().set("miniGames." + args[1] + ".allowedBlocks", allowedBlocks);
                                        player.sendMessage("§6" + args[3] + "이 " + args[1] + "게임에 랜덤 블럭 리스트에 저장되었습니다");
                                        plugin.saveConfig();
                                    } else {
                                        player.sendMessage("§c사용법: /fmg set <게임이름> addBlock <블럭이름>");
                                        player.sendMessage("블럭목록: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html");
                                    }
                                    break;
                                case "gameType":
                                    if (args.length == 4) {
                                        plugin.getConfig().set("miniGames." + args[1] + ".gameType", args[3]);
                                        plugin.saveConfig();
                                        player.sendMessage("§6" + args[3] + "이 " + args[1] + "게임에 타입에 저장되었습니다");
                                    } else
                                        player.sendMessage("§c사용법: /fmg set <게임이름> gameType <hideAndSeek|zombieMode>");
                                    break;
                                case "teamMode":
                                    if (args.length == 4) {
                                        plugin.getConfig().set("miniGames." + args[1] + ".teamMode", Boolean.parseBoolean(args[3]));

                                        plugin.saveConfig();
                                        player.sendMessage("§6" + args[3] + "이 " + args[1] + "게임에 티밍여부에 저장되었습니다");
                                    } else player.sendMessage("§c사용법: /fmg set <게임이름> teamMode <true|false>");
                                    break;
                                case "maxHealth":
                                    if (args.length >= 4) {
                                        if (args.length == 5) team = args[4];
                                        else team = "default";
                                        plugin.getConfig().set("miniGames." + args[1] + "." + team + "TeamStartMaxHeart", Double.parseDouble(args[3]));
                                        plugin.saveConfig();
                                        player.sendMessage("§6최대체력이 " + args[1] + " 게임에 " + team + "에 저장되었습니다");
                                    } else player.sendMessage("§c사용법: /fmg set <게임이름> maxHealth <체력> [팀이름]");
                                    break;
                                case "timePerPlayer":
                                    if (args.length == 4) {
                                        plugin.getConfig().set("miniGames." + args[1] + ".timePerPlayer", Integer.parseInt(args[3]));
                                        plugin.saveConfig();

                                        player.sendMessage("§6" + args[3] + "이 " + args[1] + "게임에 플레이어수의비례한죵료타이머배수에 저장되었습니다");

                                    } else
                                        player.sendMessage("§c사용법: /fmg set <게임이름> timePerPlayer <플레이어수의비례한죵료타이머배수>");
                                    break;
                                case "needClearInv":
                                    if (args.length == 4) {
                                        plugin.getConfig().set("miniGames." + args[1] + ".needClearInv", Boolean.parseBoolean(args[3]));

                                        plugin.saveConfig();
                                        player.sendMessage("§6" + args[3] + "이 " + args[1] + "게임에 참여시 인벤을 비워야 하는지 여부에 저장되었습니다");
                                    } else player.sendMessage("§c사용법: /fmg set <게임이름> needClearInv <true|false>");
                                    break;
                                case "startGameMode":
                                    if (args.length == 4) {
                                        plugin.getConfig().set("miniGames." + args[1] + ".startGameMode", Boolean.parseBoolean(args[3]));

                                        plugin.saveConfig();
                                        player.sendMessage("§6" + args[3] + "이 " + args[1] + "게임에 참여시 게임 모드를 바꾸는지 여부에 저장되었습니다");
                                    } else player.sendMessage("§c사용법: /fmg set <게임이름> startGameMode <true|false>");
                                    break;
                                case "endGameMode":
                                    if (args.length == 4) {
                                        plugin.getConfig().set("miniGames." + args[1] + ".endGameMode", Boolean.parseBoolean(args[3]));

                                        plugin.saveConfig();
                                        player.sendMessage("§6" + args[3] + "이 " + args[1] + "게임에 퇴장시 게임 모드를 바꾸는지 여부에 저장되었습니다");
                                    } else player.sendMessage("§c사용법: /fmg set <게임이름> endGameMode <true|false>");
                                    break;
                                case "addCmd":
                                    if (args.length == 5) {
                                        List<String> cmdList = plugin.getConfig().getStringList("miniGames." + args[1] + "." + args[3] + "Cmd");
                                        cmdList.add(args[4].replace("{spc}", " "));
                                        plugin.getConfig().set("miniGames." + args[1] + "." + args[3] + "Cmd", cmdList);
                                        player.sendMessage("§6명령줄이 " + args[1] + " 게임에 " + args[3] + "에 저장되었습니다");
                                    } else {
                                        player.sendMessage("§c사용법: /fmg set <게임이름> addCmd <start|quit|conStart|conEnd> <명령줄>");
                                        player.sendMessage("§c참고: <명령줄> 입력란에는 공백을 {spc}으로 넣으세요");
                                    }
                                    break;
                                case "msg":
                                    if (args.length == 5) {
                                        plugin.getConfig().set("miniGames." + args[1] + "." + args[3] + "Msg", ChatColor.translateAlternateColorCodes('&', args[4].replace("{spc}", " ")));
                                        player.sendMessage("§6메새지가 " + args[1] + " 게임에 " + args[3] + "에 저장되었습니다");
                                    } else {
                                        player.sendMessage("§c사용법: /fmg set <게임이름> msg <join|quit|start|end|noWinnerEndMsg|redWinEnd|blueWinEnd|beZombie|startTimer|endTimer> <메새지>");
                                        player.sendMessage("§c참고: <명령줄> 입력란에는 공백을 {spc}으로 넣으세요");
                                    }
                                    break;
                                default:
                                    player.sendMessage("§c사용법: /fmg set <게임이름> <wait|start|end|addBlock|gameType|teamMode|maxHealth|timePerPlayer|addCmd|msg> ...");
                            }
                        } else
                            player.sendMessage("§c사용법: /fmg set <게임이름> <wait|start|end|addBlock|gameType|teamMode|maxHealth|startGameMode|quitGameMode|timePerPlayer|addCmd|msg> ...");
                    } else player.sendMessage("§c권한이 없습니다.");
                    break;
                case "list":
                    player.sendMessage("§6게임 목록: " + plugin.getConfig().getStringList("gameList").toString());
                    break;
                case "reload":
                    if (player.hasPermission("freedyminigamemaker.admin")) {
                    plugin.reloadConfig();
                    player.sendMessage("§a리로드가 완료되었습니다");
                    } else player.sendMessage("§c권한이 없습니다.");
                    break;
                default:
                    player.sendMessage("§c사용법: /fmg <join|quit|create|remove|set|list|reload> ...");
                    break;
            }
        } else player.sendMessage("§c사용법: /fmg <join|quit|create|remove|set|list|reload> ...");
        return true;
    }
}
