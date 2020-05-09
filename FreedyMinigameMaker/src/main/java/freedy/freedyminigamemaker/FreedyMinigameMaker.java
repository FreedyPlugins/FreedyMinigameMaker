package freedy.freedyminigamemaker;

import freedy.freedyminigamemaker.commands.*;
import freedy.freedyminigamemaker.events.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class FreedyMinigameMaker extends JavaPlugin {

    public MiniGames miniGames;


    //private Map<String, Integer> taskIDList = new HashMap<>();
    //private Map<Location, Block> blockMap = new HashMap<>();


    @Override
    public void onEnable() {
        miniGames = new MiniGames(this);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new DamageEvent(this), this);
        getServer().getPluginManager().registerEvents(new BreakEvent(this), this);
        getServer().getPluginManager().registerEvents(new QuitEvent(this), this);
        getServer().getPluginManager().registerEvents(new DeathEvent(this), this);
        getCommand("fmg").setExecutor(new MinigameCommand(this));
        getCommand("fut").setExecutor(new MinigameUtilities(this));

    }

    @Override
    public void onDisable() {
        for (MiniGame miniGame : miniGames.miniGames.values())
            miniGame.disable();
    }
/*
    public void addPlayer(Player player, String gameName) {
        final int maxPlayers = getConfig().getInt("miniGames." + gameName + ".maxPlayers");
        boolean isPlaying = getConfig().getBoolean("miniGames." + gameName + ".isPlaying");
        List<String> playerNameList = getConfig().getStringList("miniGames." + gameName + ".players");
        String playerName = player.getName();
        String joinMsg = getConfig().getString("miniGames." + gameName + ".joinMsg");
        List<String> gameList = getConfig().getStringList("gameList");

        if (gameList.contains(gameName)) {
            if (!playerNameList.contains(playerName)) {
                if (playerNameList.size() < maxPlayers) {
                    if (!isPlaying) {

                        playerNameList.add(playerName);
                        for (String s : playerNameList)
                            Bukkit.getPlayer(s).sendMessage(joinMsg.replace("{player}", playerName).replace("{game}", gameName));
                        getConfig().set("miniGames." + gameName + ".players", playerNameList);
                        saveConfig();
                        startGameLogic(gameName);

                    } else player.sendMessage("§c" + "게임이 이미 시작되었습니다");
                } else player.sendMessage("§c" + "게임이 최대인원에 도달했습니다");
            } else player.sendMessage("§c" + "게임을 이미 플레이 중입니다");
        } else player.sendMessage("§c" + "그 게임이 없습니다");
    }

    final List<String> teamTypeList = Arrays.asList("default", "blue", "red");

    private void startGame(String gameName) {
        final String gameType = getConfig().getString("miniGames." + gameName + ".gameType");
        final boolean teamMode = getConfig().getBoolean("miniGames." + gameName + ".teamMode");
        List<String> playerNameList = getConfig().getStringList("miniGames." + gameName + ".players");
        String startMsg = getConfig().getString("miniGames." + gameName + ".startMsg");
        List<String> startCmd = getConfig().getStringList("miniGames." + gameName + ".startCmd");
        Map<String, Integer> teamStartLocationSize = new HashMap<>();
        Map<String, List<Location>> teamStartLocationList = new HashMap<>();
        List<String> conStartCmd = getConfig().getStringList("miniGames." + gameName + ".conStartCmd");

        for (String cmd : conStartCmd)
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("{game}", gameName));
        for (String teamName : teamTypeList) {
            teamStartLocationSize.put(teamName, getConfig().getInt("miniGames." + gameName + "." + teamName + "StartLocationAmount"));
            teamStartLocationList.put(teamName, new ArrayList<>());
        }
        for (String team : teamTypeList)
            for (int num = 1; num < teamStartLocationSize.get(team) + 1; num++) {
                World world = Bukkit.getWorld(getConfig().getString("miniGames." + gameName + "." + team + "startLocation." + num + ".world"));
                double x = Double.parseDouble(getConfig().getString("miniGames." + gameName + "." + team + "startLocation." + num + ".x"));
                double y = Double.parseDouble(getConfig().getString("miniGames." + gameName + "." + team + "startLocation." + num + ".y"));
                double z = Double.parseDouble(getConfig().getString("miniGames." + gameName + "." + team + "startLocation." + num + ".z"));
                float yaw = Float.parseFloat(getConfig().getString("miniGames." + gameName + "." + team + "startLocation." + num + ".yaw"));
                float pitch = Float.parseFloat(getConfig().getString("miniGames." + gameName + "." + team + "startLocation." + num + ".pitch"));
                teamStartLocationList.get(team).add(new Location(world, x, y, z, yaw, pitch));
                Collections.shuffle(teamStartLocationList.get(team));
            }
        int i = 0;
        List<String> redTeamPlayerNameList = getConfig().getStringList("miniGames." + gameName + ".redTeamPlayerList");
        List<String> blueTeamPlayerNameList = getConfig().getStringList("miniGames." + gameName + ".blueTeamPlayerList");
        double redTeamStartMaxHeart = getConfig().getDouble("miniGames." + gameName + ".redTeamStartMaxHeart");
        double blueTeamStartMaxHeart = getConfig().getDouble("miniGames." + gameName + ".blueTeamStartMaxHeart");
        double defaultStartMaxHeart = getConfig().getDouble("miniGames." + gameName + ".defaultStartMaxHeart");
        switch (gameType) {
            case "hideAndSeek":
                for (String s : playerNameList) {
                    Player player = Bukkit.getPlayer(s);
                    player.sendMessage(startMsg.replace("{game}", gameName));
                    for (String cmd : startCmd)
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("{player}", s).replace("{game}", gameName));
                    player.teleport(teamStartLocationList.get("default").get(i));
                    i++;
                    player.setHealthScale(defaultStartMaxHeart);
                }
                setBlockOnStart(playerNameList, gameName);
                break;
            case "zombieMode":
                Collections.shuffle(playerNameList);
                i = 0;
                for (String playerName : playerNameList) {
                    Player player = Bukkit.getPlayer(playerName);
                    player.sendMessage(startMsg.replace("{game}", gameName));
                    for (String cmd : startCmd)
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("{player}", playerName).replace("{game}", gameName));
                    if (i == 0) {
                        redTeamPlayerNameList.add(playerName);
                        player.teleport(teamStartLocationList.get("red").get(i));
                        i++;
                        player.setHealthScale(redTeamStartMaxHeart);
                    } else {
                        blueTeamPlayerNameList.add(playerName);
                        player.teleport(teamStartLocationList.get("blue").get(i));
                        i++;
                        player.setHealthScale(blueTeamStartMaxHeart);
                    }
                }
                break;
            default:
                i = 0;
                int j = 0;
                if (teamMode) {
                    for (String playerName : playerNameList) {
                        Player player = Bukkit.getPlayer(playerName);
                        player.sendMessage(startMsg.replace("{game}", gameName));
                        for (String cmd : startCmd)
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("{player}", playerName).replace("{game}", gameName));
                        if (i % 2 == 0) {
                            redTeamPlayerNameList.add(playerName);
                            player.teleport(teamStartLocationList.get("red").get(i));
                            i++;
                            player.setHealthScale(redTeamStartMaxHeart);
                        } else {
                            blueTeamPlayerNameList.add(playerName);
                            player.teleport(teamStartLocationList.get("blue").get(j));
                            j++;
                            player.setHealthScale(blueTeamStartMaxHeart);
                        }
                    }
                } else {
                    for (String playerName : playerNameList) {
                        Player player = Bukkit.getPlayer(playerName);
                        player.sendMessage(startMsg.replace("{game}", gameName));
                        for (String cmd : startCmd)
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("{player}", playerName).replace("{game}", gameName));
                        player.teleport(teamStartLocationList.get("default").get(i));
                        i++;
                        player.setHealthScale(defaultStartMaxHeart);
                    }
                }
                break;
        }
        getConfig().set("miniGames." + gameName + ".blueTeamPlayerList", blueTeamPlayerNameList);
        getConfig().set("miniGames." + gameName + ".redTeamPlayerList", redTeamPlayerNameList);
        saveConfig();
    }


    private void startGameLogic(String gameName) {
        final int maxStartPlayers = getConfig().getInt("miniGames." + gameName + ".maxStartPlayers");
        final int waitForStartTime = getConfig().getInt("miniGames." + gameName + ".waitForStartTime");
        int timer = getConfig().getInt("miniGames." + gameName + ".waitTime");
        boolean isPlaying = getConfig().getBoolean("miniGames." + gameName + ".isPlaying");
        boolean isWaiting = getConfig().getBoolean("miniGames." + gameName + ".isWaiting");
        String startTimerMsg = getConfig().getString("miniGames." + gameName + ".startTimerMsg");
        List<String> playerNameList = getConfig().getStringList("miniGames." + gameName + ".players");
        if (playerNameList.size() >= maxStartPlayers) {
            isWaiting = true;
            if (timer >= waitForStartTime) {
                isWaiting = false;
                isPlaying = true;
                timer = 0;
                startGame(gameName);
                taskIDList.replace(gameName + "-stopping", Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> stopGameLogic(gameName), 20));
            } else {
                for (String s : playerNameList)
                    Bukkit.getPlayer(s).sendMessage(startTimerMsg.replace("{time}", String.valueOf(waitForStartTime - timer)));
                timer++;
                taskIDList.replace(gameName + "-starting", Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> startGameLogic(gameName), 20));
            }
        }
        getConfig().set("miniGames." + gameName + ".isPlaying", isPlaying);
        getConfig().set("miniGames." + gameName + ".isWaiting", isWaiting);
        getConfig().set("miniGames." + gameName + ".waitTime", timer);
        saveConfig();
    }

    public boolean checkWillStopGame(String gameName) {
        boolean willStop = false;
        final boolean teamMode = getConfig().getBoolean("miniGames." + gameName + ".teamMode");
        final String gameType = getConfig().getString("miniGames." + gameName + ".gameType");
        boolean isPlaying = getConfig().getBoolean("miniGames." + gameName + ".isPlaying");
        boolean isWaiting = getConfig().getBoolean("miniGames." + gameName + ".isWaiting");
        List<String> playerNameList = getConfig().getStringList("miniGames." + gameName + ".players");
        List<String> redTeamPlayerList = getConfig().getStringList("miniGames." + gameName + ".redTeamPlayerList");
        List<String> blueTeamPlayerList = getConfig().getStringList("miniGames." + gameName + ".blueTeamPlayerList");

        if (isPlaying || isWaiting) {
            if (gameType.equalsIgnoreCase("zombieMode") && (redTeamPlayerList.isEmpty() || blueTeamPlayerList.isEmpty()))
                willStop = true;
            else if (teamMode && (redTeamPlayerList.isEmpty() || blueTeamPlayerList.isEmpty())) willStop = true;
            else if (playerNameList.isEmpty()) willStop = true;
        }
        return willStop;
    }

    public void stopGame(String gameName) {
        List<String> playerNameList = getConfig().getStringList("miniGames." + gameName + ".players");
        List<String> redTeamPlayerNameList = getConfig().getStringList("miniGames." + gameName + ".redTeamPlayerList");
        List<String> blueTeamPlayerNameList = getConfig().getStringList("miniGames." + gameName + ".blueTeamPlayerList");
        String noWinnerEndMsg = getConfig().getString("miniGames." + gameName + ".noWinnerEndMsg").replace("{game}", gameName);
        String redWinEndMsg = getConfig().getString("miniGames." + gameName + ".redWinEndMsg").replace("{game}", gameName);
        String blueWinEndMsg = getConfig().getString("miniGames." + gameName + ".blueWinEndMsg").replace("{game}", gameName);
        String endMsg = getConfig().getString("miniGames." + gameName + ".EndMsg");
        String showingEndMsg;
        String gameType = getConfig().getString("miniGames." + gameName + ".gameType");
        boolean teamMode = getConfig().getBoolean("miniGames." + gameName + ".teamMode");
        int timer = getConfig().getInt("miniGames." + gameName + ".waitTime");
        boolean isPlaying = getConfig().getBoolean("miniGames." + gameName + ".isPlaying");

        switch (gameType) {
            case "zombieMode":
                if (isPlaying && timer == 0) showingEndMsg = noWinnerEndMsg;
                else if (redTeamPlayerNameList.size() == 0) showingEndMsg = blueWinEndMsg;
                else if (blueTeamPlayerNameList.size() == 0) showingEndMsg = redWinEndMsg;
                else showingEndMsg = noWinnerEndMsg;
                break;
            default:
                if (teamMode) {
                    if (isPlaying && timer == 0) showingEndMsg = noWinnerEndMsg;
                    else if (redTeamPlayerNameList.size() == 0) showingEndMsg = blueWinEndMsg;
                    else if (blueTeamPlayerNameList.size() == 0) showingEndMsg = redWinEndMsg;
                    else showingEndMsg = noWinnerEndMsg;
                } else {
                    if (isPlaying && timer == 0) showingEndMsg = noWinnerEndMsg;
                    else if (playerNameList.size() == 1)
                        showingEndMsg = noWinnerEndMsg.replace("{game}", gameName).replace("{player}", playerNameList.get(0));
                    else showingEndMsg = endMsg.replace("{game}", gameName);
                }
                break;
        }
        for (String playerName : playerNameList) {
            Player player = Bukkit.getPlayer(playerName);
            player.sendMessage(showingEndMsg);
            removePlayer(playerName, gameName);
        }
        disableGame(gameName);
    }

    public void stopGameLogic(String gameName) {
        final int waitForEndTime = getConfig().getInt("miniGames." + gameName + ".waitForEndTime");
        int timer = getConfig().getInt("miniGames." + gameName + ".waitTime");
        List<String> playerNameList = getConfig().getStringList("miniGames." + gameName + ".players");
        int timePerPlayer = getConfig().getInt("miniGames." + gameName + ".timePerPlayer");
        String endTimerMsg = getConfig().getString("miniGames." + gameName + ".endTimerMsg");
        int endTime = (waitForEndTime * timePerPlayer);
        if (timer >= endTime) {
            timer = 0;
            getConfig().set("miniGames." + gameName + ".isPlaying", false);
            stopGame(gameName);
        } else {
            for (String s : playerNameList)
                Bukkit.getPlayer(s).sendMessage(endTimerMsg.replace("{time}", String.valueOf(endTime - timer)));
            timer++;
            taskIDList.replace(gameName + "-stopping", Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> stopGameLogic(gameName), 20));
        }
        getConfig().set("miniGames." + gameName + ".waitTime", timer);
        saveConfig();
    }

    public void removePlayer(String playerName, String gameName) {
        final int waitForStartTime = getConfig().getInt("miniGames." + gameName + ".waitForStartTime");
        boolean isWaiting = getConfig().getBoolean("miniGames." + gameName + ".isWaiting");
        List<String> playerNameList = getConfig().getStringList("miniGames." + gameName + ".players");
        List<String> redTeamPlayerNameList = getConfig().getStringList("miniGames." + gameName + ".redTeamPlayerList");
        List<String> blueTeamPlayerNameList = getConfig().getStringList("miniGames." + gameName + ".blueTeamPlayerList");
        World world = Bukkit.getWorld(getConfig().getString("miniGames." + gameName + ".endLocation.world"));
        double x = Double.parseDouble(getConfig().getString("miniGames." + gameName + ".endLocation.x"));
        double y = Double.parseDouble(getConfig().getString("miniGames." + gameName + ".endLocation.y"));
        double z = Double.parseDouble(getConfig().getString("miniGames." + gameName + ".endLocation.z"));
        float yaw = Float.parseFloat(getConfig().getString("miniGames." + gameName + ".endLocation.yaw"));
        float pitch = Float.parseFloat(getConfig().getString("miniGames." + gameName + ".endLocation.pitch"));
        String quitMsg = getConfig().getString("miniGames." + gameName + ".quitMsg");
        List<String> quitCmd = getConfig().getStringList("miniGames." + gameName + ".quitCmd");
        Location endLocation = new Location(world, x, y, z, yaw, pitch);
        Player player = Bukkit.getPlayer(playerName);

        for (String s : playerNameList)
            Bukkit.getPlayer(s).sendMessage(quitMsg.replace("{player}", s));
        for (String cmd : quitCmd)
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("{player}", playerName).replace("{game}", gameName));
        player.teleport(endLocation);
        player.setGameMode(GameMode.valueOf(getConfig().getString("miniGames." + gameName + ".defaultEndGameMode")));
        player.setHealthScale(20.0);

        if (isWaiting && waitForStartTime > playerNameList.size()) {
            getConfig().set("miniGames." + gameName + ".isWaiting", false);
            getConfig().set("miniGames." + gameName + ".waitTime", 0);
            if (!taskIDList.isEmpty()) {
                Bukkit.getScheduler().cancelTask(taskIDList.get(gameName + "-starting"));
                Bukkit.getScheduler().cancelTask(taskIDList.get(gameName + "-stopping"));
            }
        }

        playerNameList.remove(playerName);
        redTeamPlayerNameList.remove(playerName);
        blueTeamPlayerNameList.remove(playerName);
        getConfig().set("miniGames." + gameName + ".players", playerNameList);
        getConfig().set("miniGames." + gameName + ".redTeamPlayerList", redTeamPlayerNameList);
        getConfig().set("miniGames." + gameName + ".blueTeamPlayerList", redTeamPlayerNameList);
        saveConfig();
    }

    private void disableGame(String gameName) {
        List<String> playerNameList = getConfig().getStringList("miniGames." + gameName + ".players");
        List<String> conEndCmd = getConfig().getStringList("miniGames." + gameName + ".conEndCmd");

        for (String cmd : conEndCmd)
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("{game}", gameName));
        if (!playerNameList.isEmpty()) {
            for (String playerName : playerNameList) {
                removePlayer(playerName, gameName);
            }
        }
        getConfig().set("miniGames." + gameName + ".isPlaying", false);
        getConfig().set("miniGames." + gameName + ".isWaiting", false);
        getConfig().set("miniGames." + gameName + ".redTeamPlayerList", null);
        getConfig().set("miniGames." + gameName + ".blueTeamPlayerList", null);
        getConfig().set("miniGames." + gameName + ".waitTime", 0);
        saveConfig();
        if (!taskIDList.isEmpty()) {
            Bukkit.getScheduler().cancelTask(taskIDList.get(gameName + "-starting"));
            Bukkit.getScheduler().cancelTask(taskIDList.get(gameName + "-stopping"));
        }
    }

    public void createGame(String gameName, int maxPlayers, int maxStartPlayers, int waitForStartTime, int waitForEndTime) { //<미니게임이름> <미니게임최대인원> <미니게임시작인원> <시작대기시간초> <게임종료시간초>
        getConfig().set("miniGames." + gameName + ".maxPlayers", maxPlayers);
        getConfig().set("miniGames." + gameName + ".maxStartPlayers", maxStartPlayers);
        getConfig().set("miniGames." + gameName + ".waitForStartTime", waitForStartTime);
        getConfig().set("miniGames." + gameName + ".waitForEndTime", waitForEndTime);
        getConfig().set("miniGames." + gameName + ".gameType", "death");
        getConfig().set("miniGames." + gameName + ".defaultStartGameMode", "ADVENTURE");
        getConfig().set("miniGames." + gameName + ".defaultEndGameMode", "ADVENTURE");
        getConfig().set("miniGames." + gameName + ".defaultStartMaxHeart", 20.0);
        getConfig().set("miniGames." + gameName + ".redTeamStartMaxHeart", 20.0);
        getConfig().set("miniGames." + gameName + ".blueTeamStartMaxHeart", 20.0);
        getConfig().set("miniGames." + gameName + ".timePerPlayer", 1);
        getConfig().set("miniGames." + gameName + ".joinMsg", "§6{player}이(가) {game}에 참여했습니다");
        getConfig().set("miniGames." + gameName + ".quitMsg", "§6{player}이(가) 떠났습니다");
        getConfig().set("miniGames." + gameName + ".startMsg", "§a{game}이(가) 시작되었어요!");
        getConfig().set("miniGames." + gameName + ".noWinnerEndMsg", "§a{game}이(가) 종료되었어요, 무승부입니다!");
        getConfig().set("miniGames." + gameName + ".redWinEndMsg", "§a{game}이(가) 종료되었어요, 레드팀이 승리하였습니다!");
        getConfig().set("miniGames." + gameName + ".blueWinEndMsg", "§a{game}이(가) 종료되었어요, 블루팀이 승리하였습니다!");
        getConfig().set("miniGames." + gameName + ".endMsg", "§a{game}이(가) 종료되었어요! 승자는 {player}입니다!");
        getConfig().set("miniGames." + gameName + ".startTimerMsg", "§7{time}초 후에 시작...");
        getConfig().set("miniGames." + gameName + ".endTimerMsg", "§7{time}초 후에 종료...");
        getConfig().set("miniGames." + gameName + ".beZombieMsg", "§c{player}이(가) {killer}에 의해 좀비가 되었습니다!");
        getConfig().set("miniGames." + gameName + ".startCmd", new ArrayList<>());
        getConfig().set("miniGames." + gameName + ".ConStartCmd", new ArrayList<>());
        getConfig().set("miniGames." + gameName + ".quitCmd", new ArrayList<>());
        getConfig().set("miniGames." + gameName + ".conEndCmd", new ArrayList<>());
        List<String> gameList = getConfig().getStringList("gameList");
        gameList.add(gameName);
        getConfig().set("gameList", gameList);
        saveConfig();
    }

    public void setData(String path, Object dataType) {
        getConfig().set(path, dataType);
        saveConfig();
    }

    public void removeGame(String gameName) {
        List<String> gameList = getConfig().getStringList("gameList");
        gameList.remove(gameName);
        getConfig().set("miniGames." + gameName, null);
        getConfig().set("gameList", gameList);
        saveConfig();
    }

    public void setBlockOnStart(List<String> playerNameList, String gameName) {
        List<String> allowedBlocks = getConfig().getStringList("miniGames." + gameName + ".allowedBlocks");
        Collections.shuffle(allowedBlocks);
        int i = 0;
        for (String playerName : playerNameList) {
            String materialName = allowedBlocks.get(i);
            getConfig().set("miniGames." + gameName + ".playerData." + playerName + ".blockData", materialName);
            Bukkit.getPlayer(playerName).getLocation().getBlock().setType(Material.valueOf(materialName));
            getConfig().set("miniGames." + gameName + ".playerData." + playerName + ".backup", "AIR");
            i++;
        }
        saveConfig();
    }


 */
}