package mindgo.scene;

import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.Time;
import mindgo.logic.PlayerData;
import mindustry.Vars;
import mindustry.game.Rules;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.maps.Map;
import mindustry.world.Tile;

import java.util.Objects;

public class Scene {

    public Rules rules;
    public boolean needUpdatePlayers;
    final Seq<String> cantLoad;
    float time = 0;
    boolean loaded;
    String specialMap;

    public Scene() {
        rules = new Rules();
        // Basic rules implements for Health UI
        rules.canGameOver = false;
        rules.waveTimer = true;
        rules.waves = false;

        specialMap = "none";
        cantLoad = new Seq<>();
    }

    public void worldLoad() {
        /* Save old player */
        Seq<Player> players = new Seq<>();
        Groups.player.copy(players);

        /* Reset all logic and start write Data */
        Vars.logic.reset();
        /* for maps with game mode or etc. */
        Map map = null;
        if (specialMap.equals("none")) {
            boolean unseted = true;
            while(unseted) {
                /* remove all map repeats variants */
                unseted = false;
                map = Vars.maps.customMaps().random();
                if (cantLoad.size > 0) {
                    for (int i = 0; i < cantLoad.size; i++) {
                        if (Objects.equals(map.name(), cantLoad.get(0))) unseted = true;
                    }
                }
            }
        } else /* load map with special name */{
            map = Vars.maps.byName(specialMap);
        }
        Vars.world.loadMap(map);
        Call.worldDataBegin();
        /* For custom generation */
        customGenerate();
        /* Set Scene rules */
        Vars.state.rules = rules.copy();
        Vars.logic.play();

        // Assign team and send Data
        for (PlayerData pd : PlayerData.all) {
            assignTeam((pd));
            Vars.netServer.sendWorldData(pd.player);
        }
    }

    public void customGenerate() { }

    public void assignTeam(PlayerData pd) { }

    public void onWorldLoad() {
        loaded = true;
    }

    public void onPlayerJoin(Player player) { }

    public void onPlayerDie(Player player) { }

    public void onPlayerTap(Player player, Tile tile) { }

    public void update() {
        if (!loaded) return;
        this.time += Time.delta;
    }
}
