package mindgo.scene;

import arc.util.Interval;
import mindgo.Main;
import mindgo.logic.PlayerData;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;

public class Lobby extends Scene {
    final float waitTime;
    Interval interval;

    public Lobby() {
        super();
        specialMap = "lobby";
        waitTime = 60f * 10f;
        interval = new Interval();
    }

    @Override
    public void onWorldLoad() {
        super.onWorldLoad();
    }

    @Override
    public void update() {
        super.update();
        if (Groups.player.size() > 1) {
            if (interval.get(60)) {
                Call.setHudText("Start : " + ((int) (waitTime - time) / 60));
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
