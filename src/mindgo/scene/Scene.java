package mindgo.scene;

import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.Rules;
import mindustry.gen.Call;
import mindustry.maps.Map;

import java.util.Objects;

public class Scene {

    public Rules rules;

    private final Seq<String> cantLoad;
    private float time = 0;
    private boolean loaded;
    private String specialMap;

    public Scene() {
        rules = new Rules();
        specialMap = "none";
        cantLoad = new Seq<>();
    }

    public void onLoad() {
        Vars.logic.reset();
        Call.worldDataBegin();
        /* for maps with game mode or etc. */
        if (specialMap.equals("none")) {
            boolean unseted = true;
            while(unseted) {
                /* remove all map repeats variants */
                unseted = false;
                Map map = Vars.maps.all().random();
                if (cantLoad.size > 0) {
                    for (int i = 0; i < cantLoad.size; i++) {
                        if (Objects.equals(map.name(), cantLoad.get(0))) unseted = true;
                    }
                }
                Vars.world.loadMap(map);
            }
        } else /* load map with special name */{
            Vars.world.loadMap(Vars.maps.all().find(m ->
            {
                return Objects.equals(m.name(), specialMap);
            }));
        }
        Vars.state.rules = rules;
        Vars.logic.play();
    }

    public void onWorldLoad() {
        loaded = true;
    }

    public void update() {
        if (!loaded) return;
        this.time += Time.delta;
    }
}
