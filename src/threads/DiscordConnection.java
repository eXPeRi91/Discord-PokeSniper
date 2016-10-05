package threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import application.MyController;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import entities.AllJsonData;
import entities.PokeLocation;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import resources.DPSUtils;
import resources.MyColors;
import resources.PokemonChatHandler;
import resources.SnipeCache;

public class DiscordConnection implements Runnable {
	public Thread t;
	private static DiscordAPI discordAPI;
	private Button btn;
	private TextField tok;

	public DiscordConnection(Button btn, TextField token) {
		// TODO Auto-generated constructor stub
		this.tok = token;
		this.btn = btn;
	}

	@Override
	public void run() {
		String token = DPSUtils.getToken();
		discordAPI = Javacord.getApi(token, false);
		discordAPI.connectBlocking();
		if (discordAPI.getYourself() == null) {
			DPSUtils.log("Could not connect to discord via the given token, try again!", MyColors.hardError);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					tok.setDisable(false);
					btn.setDisable(false);
					btn.setText("Start catching 100 IV!");
					tok.setEditable(true);
					MyController.setStart(false);
				}
			});
			return;
		}
		discordAPI.registerListener(new PokemonChatHandler());
		DPSUtils.log("Connected to Discord with the given token.");
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
		t.interrupt();
		discordAPI.disconnect();
		DPSUtils.log("Autosniper Finished...", MyColors.error);
		DPSUtils.log("End Discord session.", MyColors.error);
		if (!AllJsonData.getPokeFarm()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					tok.setDisable(false);
					btn.setDisable(false);
					btn.setText("Start catching 100 IV!");
					tok.setEditable(true);
					MyController.setStart(false);
				}
			});
		}
	}

	public void forceTerminate() {
		t.interrupt();
		discordAPI.disconnect();
		DPSUtils.log("Stop botting to prevent ban!", MyColors.hardError);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				tok.setDisable(false);
				btn.setDisable(false);
				btn.setText("Start catching 100 IV!");
				tok.setEditable(true);
				MyController.setStart(false);
			}
		});

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
