package listeners;

import application.JSONHandler;
import entities.AllJsonData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;

public class MyMasterBallListener<T> implements ChangeListener<Boolean> {

//	private CheckBox farm;

	public MyMasterBallListener(CheckBox pokeFarm) {
	//	this.farm = pokeFarm;
	}

	@Override
	public void changed(ObservableValue<? extends Boolean> ov, Boolean oldv, Boolean newv) {
		AllJsonData.setPokeFarm(newv);
		JSONHandler.UpdatePokeList();
	}

}
