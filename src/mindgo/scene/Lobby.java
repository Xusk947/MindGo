package mindgo.scene;

import arc.util.Interval;
import mindgo.Main;
import mindgo.logic.PlayerData;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;

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
        if (Groups.player.size() > 1) {
            super.update();
            if (interval.get(60)) {
                Call.setHudText("[white]Start : [accent]" + ((int) (time) / 60) + " [white]: [accent]" + ((int) waitTime / 60));

                if (time > waitTime) {
                    Main.ME.goToScene(new Game());
                }
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
