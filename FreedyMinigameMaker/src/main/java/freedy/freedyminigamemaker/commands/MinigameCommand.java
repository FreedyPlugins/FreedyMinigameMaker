package freedy.freedyminigamemaker.commands;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MinigameCommand implements CommandExecutor {

    private FreedyMinigameMaker plugin;

    public MinigameCommand(FreedyMinigameMaker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        String playerName = player.getName();
        Location playerLocation = player.getLocation();
        if (args.length != 0) {
            switch (args[0]) {
                case "join":
                    if (args.length == 2) plugin.addPlayer(player, args[1]);
                    else player.sendMessage("§c사용법: /fmg join [게임이름]");
                    break;
                case "quit":
                    if (args.length == 2) {
                        plugin.removePlayer(playerName, args[1]);
                        if (plugin.checkWillStopGame(args[1])) plugin.stopGame(args[1]);
                    }
                    else player.sendMessage("§c사용법: /fmg quit <게임이름>");
                    break;
                case "create":
                    if (args.length == 6) {
                        plugin.createGame(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                        player.sendMessage("§6" + "게임 "+ args[1] + "이 생성되었습니다");
                    } else player.sendMessage("§c사용법: /fmg create <게임이름> <최대인원> <시작인원> <시작대기초> <종료대기초>");
                    break;
                case "remove":
                    if (args.length == 2)  {
                        plugin.removeGame(args[1]);
                        player.sendMessage("§6게임 " + args[1] +"이 삭제되었습니다");
                    }
                    else player.sendMessage("§c사용법: /fmg remove <게임이름>");
                    break;
                case "set":
                    if (args.length > 2) {
                        switch (args[2]) {
                            case "start":
                                //String path, String dataType
                                String team = "default";
                                if (args.length == 4) team = args[3];
                                int locNum = 1 + plugin.getConfig().getInt("miniGames." + args[1] + "." + team + "StartLocationAmount");
                                    plugin.setData("miniGames." + args[1] + "." + team + "startLocation." + locNum + ".world", playerLocation.getWorld().getName());
                                    plugin.setData("miniGames." + args[1] + "." + team + "startLocation." + locNum + ".x", String.valueOf(playerLocation.getX()));
                                    plugin.setData("miniGames." + args[1] + "." + team + "startLocation." + locNum + ".y", String.valueOf(playerLocation.getY()));
                                    plugin.setData("miniGames." + args[1] + "." + team + "startLocation." + locNum + ".z", String.valueOf(playerLocation.getZ()));
                                    plugin.setData("miniGames." + args[1] + "." + team + "startLocation." + locNum + ".yaw", String.valueOf(playerLocation.getYaw()));
                                    plugin.setData("miniGames." + args[1] + "." + team + "startLocation." + locNum + ".pitch", String.valueOf(playerLocation.getPitch()));
                                    plugin.setData("miniGames." + args[1] + "." + team + "StartLocationAmount", locNum);
                                    player.sendMessage("§6위치가 "+args[1]+ " 게임에 " + team + " 시작위치에 저장되었습니다");
                                break;
                            case "setStartLocation":
                                if (args.length == 9) {
                                    plugin.setData("miniGames." + args[1] + ".startLocation.world", args[3]);
                                    plugin.setData("miniGames." + args[1] + ".startLocation.x", args[4]);
                                    plugin.setData("miniGames." + args[1] + ".startLocation.y", args[5]);
                                    plugin.setData("miniGames." + args[1] + ".startLocation.z", args[6]);
                                    plugin.setData("miniGames." + args[1] + ".startLocation.yaw", args[7]);
                                    plugin.setData("miniGames." + args[1] + ".startLocation.pitch", args[8]);
                                    player.sendMessage("§6위치가 "+args[1]+ " 게임에 시작위치에 저장되었습니다");
                                } else player.sendMessage("§c사용법: /fmg set <게임이름> setStartLocation <월드이름> <x> <y> <z> <yaw> <pitch>");
                                break;
                            case "end":
                                //String path, String dataType
                                    plugin.setData("miniGames." + args[1] + ".endLocation.world", playerLocation.getWorld().getName());
                                    plugin.setData("miniGames." + args[1] + ".endLocation.x", String.valueOf(playerLocation.getX()));
                                    plugin.setData("miniGames." + args[1] + ".endLocation.y", String.valueOf(playerLocation.getY()));
                                    plugin.setData("miniGames." + args[1] + ".endLocation.z", String.valueOf(playerLocation.getZ()));
                                    plugin.setData("miniGames." + args[1] + ".endLocation.yaw", String.valueOf(playerLocation.getYaw()));
                                    plugin.setData("miniGames." + args[1] + ".endLocation.pitch", String.valueOf(playerLocation.getPitch()));
                                    player.sendMessage("§6위치가 "+args[1]+ " 게임에 종료위치에 저장되었습니다");
                                break;
                            case "setEndLocation":
                                if (args.length == 9) {
                                    plugin.setData("miniGames." + args[1] + ".endLocation.world", args[3]);
                                    plugin.setData("miniGames." + args[1] + ".endLocation.x", args[4]);
                                    plugin.setData("miniGames." + args[1] + ".endLocation.y", args[5]);
                                    plugin.setData("miniGames." + args[1] + ".endLocation.z", args[6]);
                                    plugin.setData("miniGames." + args[1] + ".endLocation.yaw", args[7]);
                                    plugin.setData("miniGames." + args[1] + ".endLocation.pitch", args[8]);
                                    player.sendMessage("§6위치가 "+args[1]+ " 게임에 종료위치에 저장되었습니다");
                                } else player.sendMessage("§c사용법: /fmg set <게임이름> setEndLocation <월드이름> <x> <y> <z> <yaw> <pitch>");
                                break;
                            case "setBlock":
                                if (args.length == 4) {
                                    List<String> allowedBlocks = plugin.getConfig().getStringList("miniGames." + args[1] + ".allowedBlocks");
                                    allowedBlocks.add(args[3]);
                                    plugin.getConfig().set("miniGames." + args[1] + ".allowedBlocks", allowedBlocks);
                                    player.sendMessage("§6" + args[3] + "이 " + args[1] +"게임에 랜덤 블럭 리스트에 저장되었습니다");
                                    plugin.saveConfig();
                                }
                                else player.sendMessage("§c사용법: /fmg set <게임이름> setBlock <블럭이름> \n블럭목록: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html");
                                break;
                            case "setType":
                                if (args.length == 4) {
                                    plugin.getConfig().set("miniGames." + args[1] + ".gameType", args[3]);
                                    plugin.saveConfig();
                                    player.sendMessage("§6" + args[3] + "이 " + args[1] +"게임에 타입에 저장되었습니다");
                                }
                                else player.sendMessage("§c사용법: /fmg set <게임이름> setType <hideAndSeek|zombieMode>");
                                break;
                            case "teamMode":
                                if (args.length == 4) {
                                    plugin.setData("miniGames." + args[1] + ".teamMode", Boolean.parseBoolean(args[3]));
                                    player.sendMessage("§6" + args[3] + "이 " + args[1] +"게임에 티밍여부에 저장되었습니다");
                                } else player.sendMessage("§c사용법: /fmg set <게임이름> teamMode <true|false>");
                                break;
                            default: player.sendMessage("§c사용법: /fmg set <게임이름> <start|end|setBlock|setType|teamMode>");
                        }
                    } else player.sendMessage("§c사용법: /fmg set <게임이름> <start|end|setBlock|setType|teamMode>");
                    break;
                case "list":
                    player.sendMessage("§6게임 목록: " +plugin.getConfig().getStringList("gameList").toString());
                    break;
                case "reload":
                    plugin.saveConfig();
                    player.sendMessage("§a리로드가 완료되었습니다");
                    break;
                default:
                    player.sendMessage("§c사용법: /fmg <join|quit|create|remove|set|list|reload> ...");
                    break;
            }
        } else player.sendMessage("§c사용법: /fmg <join|quit|create|remove|set|list|reload> ...");
        return true;
    }
}
