package resources;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import entities.PokeLocation;

import java.io.*;

public class PokemonChatHandler implements MessageCreateListener {

	static Thread t = null;

	public void onMessageCreate(DiscordAPI discordAPI, Message message) {
		String messageContents = message.getContent();
		PokeLocation pokeLocation = PokeLocation.parsePokemonNotificationString(messageContents);
		if (pokeLocation != null)
			snipePokemon(pokeLocation);
	}

	public void snipePokemon(PokeLocation pokeLocation) {
		if (SnipeCache.isCached(pokeLocation))
			return;

		double latitude = pokeLocation.getLatitude();
		double longitude = pokeLocation.getLongitude();
		String pokemonName = pokeLocation.getPokemonType().getDispalyName();

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
	}
}
