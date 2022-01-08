package mindgo.scene;

import arc.util.Interval;
import arc.util.Time;
import mindgo.Main;
import mindgo.logic.PlayerData;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;

public class Lobby extends Scene {
    float timer = 0;
    Interval interval;

    public Lobby() {
        super();
        specialMap = "lobby";
        interval = new Interval(2);
    }

    @Override
    public void onWorldLoad() {
        super.onWorldLoad();
        timer = 60f * 10f;
    }

    @Override
    public void update() {
        super.update();
        if (Groups.player.size() > 1) {
            timer -= Time.delta;
            if (interval.get(0, 1)) {
                Call.setHudText("Start : " + (int) timer);
            }
        }
    }

    @Override
    public void assignTeam(PlayerData pd) {
        pd.data.team = Team.sharded;
        pd.player.team(Team.sharded);
    }

    private void runGame() {
        Game game = new Game();

        Main.ME.goToScene(game);
    }
}
