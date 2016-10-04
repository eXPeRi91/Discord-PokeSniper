package threads;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import entities.PokeLocation;
import resources.DPSUtils;
import resources.SnipeCache;
import resources.SyncPipe;

public class CatchMe extends Thread {
	static boolean stop = false;
	private PokeLocation pokeLocation;

	public CatchMe(PokeLocation pokeLocation) {
		this.pokeLocation = pokeLocation;
	}

	public void run() {
		while (stop) {
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		stop = true;
		if (SnipeCache.isCached(this.pokeLocation)) {
			stop = false;
			return;
		}
		double latitude = pokeLocation.getLatitude();
		double longitude = pokeLocation.getLongitude();
		String pokemonName = pokeLocation.getPokemonType().getDisplayName();

		DPSUtils.log("Sniping " + pokemonName + " @ " + latitude + ", " + longitude + ".");

		String[] command = { "cmd", };
		Process proc = null;
		try {
			proc = Runtime.getRuntime().exec(command);
			new Thread(new SyncPipe(proc.getErrorStream(), System.err)).start();
			new Thread(new SyncPipe(proc.getInputStream(), System.out)).start();
			PrintWriter stdin = new PrintWriter(proc.getOutputStream());
			String ex = "PokeSniper2.exe " + pokeLocation.getPokemonType().getName() + " " + latitude + " " + longitude
					+ " exit";
			stdin.println("cd " + DPSUtils.getCurrentDirectory() + "\n");
			stdin.println(ex);
			stdin.close();
			proc.waitFor();
			proc.destroy();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SnipeCache.addToCache(pokeLocation);
		try {
			Random rand = new Random(); 
			int wait = rand.nextInt(5000) + 10000;
			DPSUtils.log("Waiting "+wait+"ms to prevent ban!");
			Thread.sleep(wait);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stop = false;
	}
}
