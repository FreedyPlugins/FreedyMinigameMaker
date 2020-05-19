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
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c플레이어만 실행할 수 있습니다");
            return true;
        }
        Player player = (Player) sender;
        Location playerLocation = player.getLocation();
        if (args.length != 0) {
            switch (args[0]) {
                case "gui":
                    if (args.length == 2) miniGames.getNoneGame().openInv(player, args[1]);
                    else if (args.length == 3) miniGames.getNoneGame().openInv(Bukkit.getPlayer(args[2]), args[1]);
                    else player.sendMessage("§c사용법: /fmg gui <메뉴이름> [플레이어]");
                    break;
                case "join":
                    if (args.length == 2) miniGames.get(args[1]).add(player);
                    else if (args.length == 3) miniGames.get(args[1]).add(Bukkit.getPlayer(args[2]));
                    else player.sendMessage("§c사용법: /fmg join <게임이름> [플레이어]");
                    break;
                case "joinAll":
                    if (player.hasPermission("freedyminigamemaker.admin")) {
                        if (args.length == 2) {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                miniGames.get(args[1]).add(p);
                                miniGames.get(args[1]).getPlayerData(p).dropItemMode = true;
                            }
                        } else player.sendMessage("§c사용법: /fmg joinAll <게임이름>");
                    }
                    break;
                case "quitAll":
                    if (player.hasPermission("freedyminigamemaker.admin")) {
                        if (args.length == 2) {
                            miniGames.get(args[1]).removeAll(miniGames.get(args[1]).playerList);
                        } else player.sendMessage("§c사용법: /fmg quitAll <게임이름>");
                    }
                    break;
                case "quit":
                    if (miniGames.isJoined(player)) {
                        MiniGame miniGame = miniGames.getJoined(player);
                        miniGame.remove(player);
                        miniGame.stop();
                    } else player.sendMessage("§c참여중인 미니게임이 없습니다");
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
                                        plugin.saveConfig();
                                        player.sendMessage("§6" + args[3] + "이 " + args[1] + "게임에 랜덤 블럭 리스트에 저장되었습니다");
                                    } else {
                                        player.sendMessage("§c사용법: /fmg set <게임이름> addBlock <블럭이름>");
                                        player.sendMessage("블럭목록: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html");
                                    }
                                    break;
                                case "addDropItem":
                                    if (args.length == 5) {
                                        List<String> dropItemList = plugin.getConfig().getStringList("miniGames." + args[1] + ".dropItems.dropList");
                                        dropItemList.add(args[3]);
                                        plugin.getConfig().set("miniGames." + args[1] + ".dropItems.dropList", dropItemList);
                                        plugin.getConfig().set("miniGames." + args[1] + ".dropItems.drop." + args[3], Integer.parseInt(args[4]));
                                        plugin.saveConfig();
                                        player.sendMessage("§6" + args[3] + " 블럭이 " + args[1] + "게임에 " + args[4] + " 만큼 떨꿔짐에 저장되었습니다");
                                    } else {
                                        player.sendMessage("§c사용법: /fmg set <게임이름> addDropItem <블럭이름> <아이템수>");
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
                                case "worldBoarder":
                                    if (args.length >= 4) {
                                        switch (args[3]) {
                                            case "setLocation":
                                                //String path, String dataType
                                                miniGames.getEditor(args[1]).setLocation("worldBoarder.location",
                                                        playerLocation.getWorld().getName(),
                                                        playerLocation.getX(),
                                                        playerLocation.getY(),
                                                        playerLocation.getZ());
                                                player.sendMessage("§6위치가 " + args[1] + " 월드 보더 중심 위치에 저장 되었습니다");
                                                break;
                                            case "enable":
                                                if (args.length == 5) {
                                                    plugin.getConfig().set("miniGames." + args[1] + ".worldBoarder.enable", Boolean.parseBoolean(args[4]));

                                                    plugin.saveConfig();
                                                    player.sendMessage("§6" + args[4] + "이 " + args[1] + "게임에 참여시 월드 보더 설정 여부에 저장되었습니다");
                                                } else player.sendMessage("§c사용법: /fmg set <게임이름> worldBoarder enable <true|false>");
                                                break;
                                            case "sizePerPlayer":
                                                if (args.length == 5) {
                                                    plugin.getConfig().set("miniGames." + args[1] + ".worldBoarder.sizePerPlayer", Integer.parseInt(args[4]));
                                                    plugin.saveConfig();

                                                    player.sendMessage("§6" + args[4] + "이 " + args[1] + "게임에 플레이어수의비례한월드보더크기배수에 저장되었습니다");

                                                } else
                                                    player.sendMessage("§c사용법: /fmg set <게임이름> worldBoarder sizePerPlayer <플레이어수의비례한월드보더크기배수>");
                                                break;
                                            case "outDamage":
                                                if (args.length == 5) {
                                                    plugin.getConfig().set("miniGames." + args[1] + ".worldBoarder.outDamage", Integer.parseInt(args[4]));
                                                    plugin.saveConfig();

                                                    player.sendMessage("§6" + args[4] + "이 " + args[1] + "게임에 월드 보더 바깥 데미지에 저장되었습니다");

                                                } else
                                                    player.sendMessage("§c사용법: /fmg set <게임이름> worldBoarder outDamage <플레이어수의비례한월드보더크기배수>");
                                                break;
                                            case "minSize":
                                                if (args.length == 5) {
                                                    plugin.getConfig().set("miniGames." + args[1] + ".worldBoarder.minSize", Integer.parseInt(args[4]));
                                                    plugin.saveConfig();

                                                    player.sendMessage("§6" + args[4] + "이 " + args[1] + "게임에 월드 보더 최소 사이즈에 저장되었습니다");

                                                } else
                                                    player.sendMessage("§c사용법: /fmg set <게임이름> worldBoarder minSize <최소사이즈>");
                                                break;
                                            case "speed":
                                                if (args.length == 5) {
                                                    plugin.getConfig().set("miniGames." + args[1] + ".worldBoarder.speed", Integer.parseInt(args[4]));
                                                    plugin.saveConfig();

                                                    player.sendMessage("§6" + args[4] + "이 " + args[1] + "게임에 월드 보더 감소 속도에 저장되었습니다");

                                                } else
                                                    player.sendMessage("§c사용법: /fmg set <게임이름> worldBoarder minSize <감소속도>");
                                                break;
                                        }
                                    } else player.sendMessage("§c사용법: /fmg set <게임이름> worldBoarder <enable|sizePerPlayer|setLocation|outDamage|minSize|speed> ...");
                                    break;
                                case "scoreBoard":
                                    if (args.length >= 4) {
                                        switch (args[3]) {
                                            case "enable":
                                                if (args.length == 5) {
                                                    plugin.getConfig().set("miniGames." + args[1] + ".scoreBoardEnable", Boolean.parseBoolean(args[4]));
                                                    plugin.saveConfig();
                                                    player.sendMessage("§6" + args[4] + "이 " + args[1] + "게임에 스코어 보드 사용 여부에 저장되었습니다");

                                                } else player.sendMessage("§c사용법: /fmg set <게임이름> scoreBoard enable <true|false>");

                                                break;
                                            case "addMsg":
                                                if (args.length == 5) {
                                                    miniGames.getEditor(args[1]).addMessage("scoreBoard",
                                                            ChatColor.translateAlternateColorCodes('&', args[4].replace("{spc}", " ")));
                                                    player.sendMessage("§6메새지가 " + args[1] + " 스코어보드 목록에 추가 되었습니다");
                                                } else {
                                                    player.sendMessage("§c사용법: /fmg set <게임이름> scoreBoard addMsg <메세지>");
                                                    player.sendMessage("§c참고: <명령줄> 입력란에는 공백을 {spc}으로 넣으세요");
                                                }
                                                
                                                break;
                                            case "setTitle":
                                                if (args.length == 5) {
                                                    plugin.getConfig().set("miniGames." + args[1] + ".scoreBoardTitle", ChatColor.translateAlternateColorCodes('&', args[4].replace("{spc}", " ")));
                                                    plugin.saveConfig();
                                                    player.sendMessage("§6스코어보드 제목이 " + args[1] + " 게임에 저장되었습니다");
                                                } else {
                                                    player.sendMessage("§c사용법: /fmg set <게임이름> scoreBoard setTitle <메세지>");
                                                    player.sendMessage("§c참고: <명령줄> 입력란에는 공백을 {spc}으로 넣으세요");
                                                }
                                                break;
                                        }
                                    } else player.sendMessage("§c사용법: /fmg set <게임이름> scoreBoard <enable|addMsg|setTitle> ...");
                                    break;
                                case "inv":
                                    if (args.length >= 4) {
                                        switch (args[3]) {
                                            case "addItem":
                                                if (args.length == 7) {

                                                    plugin.getConfig().set("inventories." + args[4] + ".items." + args[5]
                                                            , player.getInventory().getItemInMainHand());

                                                    List<String> cmdList = plugin.getConfig().getStringList("inventories." + args[4] + "." + args[5] + "Cmd");
                                                    cmdList.add(ChatColor.translateAlternateColorCodes('&', args[6]
                                                            .replace("{spc}", " ")));
                                                    plugin.getConfig().set("inventories." + args[4] + "." + args[5] + "Cmd", cmdList);
                                                    plugin.saveConfig();
                                                    player.sendMessage("§6명령줄이 " + args[1] + "게임의 메뉴 " + args[4] + "의 " + args[5] + "번줄에 손에 들고 있던 아이템과 저장되었습니다");
                                                } else if (args.length == 6) {
                                                    plugin.getConfig().set("inventories." + args[4] + ".items." + args[5]
                                                            , player.getInventory().getItemInMainHand());
                                                    plugin.saveConfig();
                                                    player.sendMessage("§6아이템이 " + args[1] + "게임의 메뉴 " + args[4] + "의 " + args[5] + "번줄에 저장되었습니다");

                                                } else {
                                                    player.sendMessage("§c사용법: /fmg set <게임이름> inv addItem <메뉴이름> <아이템위치> [명령줄]");
                                                    player.sendMessage("§c참고: <명령줄> 입력란에는 공백을 {spc}으로 넣으세요");
                                                }

                                                break;
                                            case "create":
                                                if (args.length == 7) {
                                                    plugin.getConfig().set("inventories." + args[4] + ".title"
                                                            , ChatColor.translateAlternateColorCodes('&', args[6].replace("{spc}", " ")));
                                                    plugin.getConfig().set("inventories." + args[4] + ".size"
                                                            , Integer.parseInt(args[5]));
                                                    List<String> inventoryList = plugin.getConfig().getStringList("inventoryList");
                                                    if (!inventoryList.contains(args[4])) inventoryList.add(args[4]);
                                                    plugin.getConfig().set("inventoryList", inventoryList);
                                                    plugin.saveConfig();
                                                    player.sendMessage("§6메뉴가 " + args[1] + " 게임에 저장되었습니다");
                                                } else player.sendMessage("§c사용법: /fmg set <게임이름> inv create <메뉴이름> <9|18|27|36|45|54> <타이틀>");
                                                break;
                                            default: player.sendMessage("§c사용법: /fmg set <게임이름> inv <addItem|create> ...");
                                        }
                                    } else player.sendMessage("§c사용법: /fmg set <게임이름> inv <addItem|create> ...");
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
                                        cmdList.add(ChatColor.translateAlternateColorCodes('&', args[4].replace("{spc}", " ")));
                                        plugin.getConfig().set("miniGames." + args[1] + "." + args[3] + "Cmd", cmdList);
                                        plugin.saveConfig();
                                        player.sendMessage("§6명령줄이 " + args[1] + " 게임에 " + args[3] + "에 저장되었습니다");
                                    } else {
                                        player.sendMessage("§c사용법: /fmg set <게임이름> addCmd <start|join|quit|conStart|conEnd|winner> <명령줄>");
                                        player.sendMessage("§c참고: <명령줄> 입력란에는 공백을 {spc}으로 넣으세요");
                                    }
                                    break;
                                case "msg":
                                    if (args.length == 5) {
                                        plugin.getConfig().set("miniGames." + args[1] + "." + args[3] + "Msg", ChatColor.translateAlternateColorCodes('&', args[4].replace("{spc}", " ")));
                                        plugin.saveConfig();
                                        player.sendMessage("§6메세지가 " + args[1] + " 게임에 " + args[3] + "에 저장되었습니다");
                                    } else {
                                        player.sendMessage("§c사용법: /fmg set <게임이름> msg <join|quit|start|end|noWinnerEndMsg|redWinEnd|blueWinEnd|beZombie|extraDamage|dropItem|resistingDamage|startTimer|endTimer> <메세지>");
                                        player.sendMessage("§c참고: <명령줄> 입력란에는 공백을 {spc}으로 넣으세요");
                                    }
                                    break;
                                case "loc":
                                    if (args.length == 4) {
                                        miniGames.getEditor(args[1]).setLocation(args[3] + "Location",
                                                playerLocation.getWorld().getName(),
                                                playerLocation.getX(),
                                                playerLocation.getY(),
                                                playerLocation.getZ(),
                                                playerLocation.getYaw(),
                                                playerLocation.getPitch());
                                        player.sendMessage("§6위치가 " + args[1] + " 커스텀 위치에 저장 되었습니다");
                                    } else player.sendMessage("§c사용법: /fmg set <게임이름> loc <customName>");
                                    break;

                                default:
                                    player.sendMessage("§c사용법: /fmg set <게임이름> <wait|start|end|addDropItem|gameType|teamMode|maxHealth|worldBoarder|timePerPlayer|worldBoarder|scoreBoard|inv|needClearInv|startGameMode|endGameMode|addCmd|msg|loc> ...");

                            }
                        } else
                            player.sendMessage("§c사용법: /fmg set <게임이름> <wait|start|end|addBlock|addDropItem|gameType|teamMode|maxHealth|startGameMode|quitGameMode|timePerPlayer|worldBoarder|scoreBoard|addCmd|msg|loc> ...");
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
                    player.sendMessage("§c사용법: /fmg <gui|join|quit|create|remove|set|list|reload> ...");
                    break;
            }
        } else player.sendMessage("§c사용법: /fmg <gui|join|quit|create|remove|set|list|reload> ...");
        return true;
    }
}
