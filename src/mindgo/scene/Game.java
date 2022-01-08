package mindgo.scene;

import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Timer;
import mindgo.Main;
import mindgo.logic.PlayerData;
import mindustry.Vars;
import mindustry.content.UnitTypes;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.world.blocks.storage.CoreBlock;

public class Game extends Scene {
    static final int MAX_GAMES = 4;
    static final float MAX_VAT_TIME = 60f * 60f * 3f; /* 3 minutes */

    public int currentGame;
    public ObjectMap<Integer, PlayerData> inGame;

    // need after World Load cool down for sync core destroy
    private float _timer;
    private boolean _run;

    public Game() {
        super();
        rules.unitCap = 9999;
        // when game is end go to Lobby
        currentGame = 0;
        // because this maps uses other scene
        cantLoad.add("lobby");
        cantLoad.add("shop");
        rules.defaultTeam = Team.derelict;

        needUpdatePlayers = true;

        inGame = new ObjectMap<>();
        _run = false;
    }

    @Override
    public void update() {
        super.update();
        if (_run) {
            // Switch Scene when Time out
            if(time > MAX_VAT_TIME) {
                announceWinner(Team.blue);
            }
            int red = 0, blue = 0;

            for (Unit unit : Groups.unit) {
                if (!unit.isNull() && !unit.dead) {
                    if (unit.team == Team.crux) {
                        red++;
                    } else if (unit.team == Team.blue) {
                        blue++;
                    }
                }
            }
            // if somebody team haven't units announce them
            if (blue <= 0 || red <= 0) {
                announceWinner((blue <= 0 ? Team.crux : Team.blue));
            }
        } else {
            _timer -= Time.delta;
            if (_timer < 0) onRun();
        }
    }

    @Override
    public void onPlayerJoin(Player player) {
        if (inGame.containsKey(player.id)) {
            PlayerData pd = inGame.get(player.id);
        } else {
            assignNewPlayer(PlayerData.all.find(pd -> {
                return pd.player.id == player.id;
            }));
        }
    }

    @Override
    public void onPlayerDie(Player player) {
        PlayerData.Data data = PlayerData.map.get(player.id).data;
        data.armor = 0;
        data.items.clear();
    }

    @Override
    public void assignTeam(PlayerData pd) {
        pd.player.team(pd.data.team);
    }

    @Override
    public void worldLoad() {
        // Exit from this scene when counter Get max value
        if (currentGame >= MAX_GAMES) Main.ME.goToScene(new Lobby());
        // Just in case if player not assigned
        for (PlayerData pd : PlayerData.all) {
            if (!inGame.containsKey(pd.player.id)) {
                assignNewPlayer(pd);
            }
        }
        super.worldLoad();
    }

    @Override
    public void onWorldLoad() {
        super.onWorldLoad();
        _timer = 60f * 3f;
        time = 0;
        currentGame++;
    }

    private void assignNewPlayer(PlayerData pd) {
        inGame.put(pd.player.id, pd);

        int red = 0;
        int blue = 0;
        for (PlayerData pd1 : inGame.values()) {
            if (pd1.data.team == Team.crux) red++;
            else if (pd1.data.team == Team.blue) blue++;
        }

        if (red >= blue) {
            pd.data.team = Team.blue;
            pd.player.team(Team.blue);
        } else {
            pd.data.team = Team.crux;
            pd.player.team(Team.crux);
        }
    }

    private void onRun() {
        _run = true;
        Timer.schedule(() -> {
            if (Main.ME.currentScene != this) return;
            for (PlayerData pd : inGame.values()) {
                if (pd.data.team == Team.blue) {
                    Unit unit = pd.data.type.create(pd.data.team);
                    CoreBlock.CoreBuild core = pd.data.team.core();
                    if (core != null) {
                        unit.set(core.x, core.y);
                    }
                }
            }
        }, 1.2f);
        if (Team.blue.core() != null) {
            Team.blue.core().kill();
        }
        if (Team.crux.core() != null) {
            Team.crux.core().kill();
        }
    }

    private void announceWinner(Team team) {
        String winner = team == Team.crux ? "[red]#@Red [yellow] win!" : team == Team.blue ? "[blue]#@Blue [yellow] win!" : "[white]Nobody@won";
        Call.sendMessage(winner, "[gray]MS:GO", Nulls.player);

        for (PlayerData pd : inGame.values()) {
            if (pd.data.team == team) {
                pd.data.money += 200;
            }
        }

        _run = false;

        Shop shop = new Shop();
        shop.game = this;
        Main.ME.goToScene(shop);
    }
}
