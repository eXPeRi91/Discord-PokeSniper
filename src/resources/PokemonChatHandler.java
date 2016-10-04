package resources;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import entities.PokeLocation;
import threads.CatchMe;

public class PokemonChatHandler implements MessageCreateListener {

	public void onMessageCreate(DiscordAPI discordAPI, Message message) {
		String messageContents = message.getContent();
		PokeLocation pokeLocation = PokeLocation.parsePokemonNotificationString(messageContents);
		if (pokeLocation != null)
			snipePokemon(pokeLocation);
	}

	public void snipePokemon(PokeLocation pokeLocation) {
		Thread pokeSniping = new CatchMe(pokeLocation);
		pokeSniping.start();
	}
}
