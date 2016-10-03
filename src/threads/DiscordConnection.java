package threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import entities.PokeLocation;
import javafx.application.Platform;
import javafx.scene.control.Button;
import resources.DPSUtils;
import resources.PokemonChatHandler;
import resources.SnipeCache;

public class DiscordConnection implements Runnable {
	public Thread t;
	private static DiscordAPI discordAPI;
	private boolean flag;
	private Button btn;
	public DiscordConnection(Button btn) {
		// TODO Auto-generated constructor stub
		this.btn = btn;
	}

	@Override
	public void run() {
		flag = true;
		String token = DPSUtils.getToken();
		discordAPI = Javacord.getApi(token, false);
		discordAPI.connectBlocking();
		DPSUtils.log("Connected to Discord with the given token.");
		discordAPI.registerListener(new PokemonChatHandler());
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				List<PokeLocation> toRemove = new ArrayList<PokeLocation>();
				for (PokeLocation pokeLocation : SnipeCache.sniperCache.keySet()) {
					long timePlaced = SnipeCache.sniperCache.get(pokeLocation);
					long currentTime = System.currentTimeMillis();
					if ((timePlaced + 3_600_000L) < currentTime)
						toRemove.add(pokeLocation);
				}
				toRemove.forEach(pL -> SnipeCache.sniperCache.remove(pL));
			}
		}, 3_600_000L, 60000L);
		DPSUtils.log("Autosniper Started...");
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				btn.setDisable(false);
				btn.setText("Stop bot!");
			}
		});
	}

	public void terminate() {
		flag = false;
		t.interrupt();
		discordAPI.disconnect();
		DPSUtils.log("Autosniper Finished...");
		DPSUtils.log("End Discord session.");
	}

	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}

	public static DiscordAPI getDiscordAPI() {
		return discordAPI;
	}

	public void setDiscordAPI(DiscordAPI discordAPI) {
		DiscordConnection.discordAPI = discordAPI;
	}

}
