package entities;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Pokemon {
	private Integer id;
	private String name;
	private Boolean catchable;	
	private String dispalyName;	// Sometimes the pokemons' name is different than the display name
	private Label label;		// keep the amount of pokemons caught by the bot
	private Integer amount;
	
	/*public Pokemon(Integer id, String name, Boolean c, String dName) {
		this.id = id;
		this.name = name;
		this.dispalyName = dName == null ? name : dName;
		this.catchable = c;
		this.setAmount(0)*;
	}*/
	public Pokemon(Integer id, String name, Boolean c, String dName, String amount) {
		this.id = id;
		this.name = name;
		this.dispalyName = dName == null ? name : dName;
		this.catchable = c;
		if(amount != null)
			this.setAmount(Integer.valueOf(amount));
		else 
			this.setAmount(0);
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

	public String getDispalyName() {
		return dispalyName;
	}

	public void setDispalyName(String dispalyName) {
		this.dispalyName = dispalyName;
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
				that.label.setText(amount.toString());
			}
		});
		
	}
	
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "Pokemon [id=" + id + ", name=" + name + ", catchable=" + catchable + ", dispalyName=" + dispalyName
				+ "]";
	}

	public String toJSON() {
		// {"id": "152","name": "Mew","catch": true},
		String str = "\t\t{\"id\": \"" + String.format("%03d", id) + "\", \"name\": \"" + name + "\", \"catch\": "
				+ catchable + "";
		if (!dispalyName.equals(name)) {
			str += ", \"dispalyName\": \"" + dispalyName + "\"";
		}
		if (this.amount > 0) {
			str += ", \"amount\": \"" + amount + "\"";
		}
		str += "},\n";
		return str;

	}
}
