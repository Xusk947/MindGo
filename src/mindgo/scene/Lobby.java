package mindgo.scene;

import arc.util.Time;
import mindgo.Main;
import mindgo.logic.PlayerData;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.gen.Nulls;
import mindustry.gen.Player;

public class Lobby extends Scene {
    float timer = 0;

    public Lobby() {
        super();
        specialMap = "lobby";
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
