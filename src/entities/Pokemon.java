package entities;

import application.JSONHandler;
import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import resources.DPSUtils;

public class Pokemon {
	private Integer id;
	private String name;
	private Boolean catchable;
	private String displayName; // Sometimes the pokemons' name is different
								// than the display name
	private Label label; // keep the amount of pokemons caught by the bot
	private Integer amount;
	private CheckBox checkbox;

	/*
	 * public Pokemon(Integer id, String name, Boolean c, String dName) {
	 * this.id = id; this.name = name; this.displayName = dName == null ? name :
	 * dName; this.catchable = c; this.setAmount(0)*; }
	 */
	public Pokemon(Integer id, String name, Boolean c, String dName, String amount) {
		this.id = id;
		this.name = name;
		this.displayName = dName == null ? name : dName;
		this.catchable = c;
		if (amount != null)
			this.setAmount(Integer.valueOf(amount));
		else
			this.setAmount(0);
	}

	public Pokemon() {
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setPokemonId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getCatchable() {
		return catchable;
	}

	public void setCatchable(Boolean catchable) {
		this.catchable = catchable;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public void updateLableValue() {
		this.amount++;
		Pokemon that = this;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				DPSUtils.getFullCounter()
						.setText(String.valueOf((Integer.parseInt(DPSUtils.getFullCounter().getText()) + 1)));
				that.label.setText(amount.toString());
				DPSUtils.setPokeCatchCounter();
				JSONHandler.UpdatePokeList();
			}
		});
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
		if (this.label != null) {
			this.label.setText(amount.toString());
			JSONHandler.UpdatePokeList();
		}
	}

	@Override
	public String toString() {
		return "Pokemon [id=" + id + ", name=" + name + ", catchable=" + catchable + ", displayName=" + displayName
				+ "]";
	}

	public String toJSON() {
		// {"id": "152","name": "Mew","catch": true},
		String str = "\t\t{\"id\": \"" + String.format("%03d", id) + "\", \"name\": \"" + name + "\", \"catch\": "
				+ catchable + "";
		if (!displayName.equals(name)) {
			str += ", \"displayName\": \"" + displayName + "\"";
		}
		if (this.amount > 0) {
			str += ", \"amount\": \"" + amount + "\"";
		}
		str += "},\n";
		return str;

	}

	public CheckBox getCheckbox() {
		return checkbox;
	}

	public void setCheckbox(CheckBox checkbox) {
		this.checkbox = checkbox;
	}

	public void setPokemon(Pokemon value) {
		this.amount = value.amount;
		this.catchable = value.catchable;
		this.id = value.id;
		this.name = value.name;
		this.displayName = value.displayName;
		this.label = value.label;
		this.checkbox = value.checkbox;
	}
}
