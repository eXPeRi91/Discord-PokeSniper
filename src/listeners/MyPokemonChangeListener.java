package listeners;

import application.JSONHandler;
import entities.Pokemon;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class MyPokemonChangeListener<T> implements ChangeListener<Boolean> {

	private Pokemon poke;

	public MyPokemonChangeListener(Pokemon poke) {
		this.poke = poke;
	}

	@Override
	public void changed(ObservableValue<? extends Boolean> ov, Boolean oldv, Boolean newv) {
		this.poke.setCatchable(newv);
		JSONHandler.UpdatePokeList();
	}

}
