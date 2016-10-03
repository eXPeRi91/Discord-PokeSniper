package application;

import java.awt.Desktop;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import entities.AllJsonData;
import entities.Pokemon;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import listeners.MyPokemonChangeListener;
import resources.DPSUtils;
import javafx.scene.control.CheckBox;

public class MyController implements Initializable {
	@FXML
	private Button StartBot;
	@FXML
	private GridPane PokeListGrid;
	private Boolean start = false;
	@FXML
	private TextField token;
	@FXML
	private VBox LogArea;
	@FXML
	private ScrollPane ScrollForLog;
	@FXML
	private Button MoreInfo;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			initializePokemonList();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void updateToken() {
		AllJsonData.setToken(this.token.getText());
	}

	public void openPaypalLink() {
		try {
			Desktop.getDesktop().browse(new URL("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=WNDFMD6MVL4KN").toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void startTheBot() {
		// TODO
		if(token.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No token entered!");
			alert.setHeaderText("No token was entered!");
			alert.setContentText("Please enter token and try again!");
			alert.showAndWait();
			return;
		}
		if (!this.start) {
			token.setEditable(false);
			token.setDisable(true);
			this.start = true;
			StartBot.setText("Starting program, please wait!");
			StartBot.setDisable(true);
			DPSUtils.startBot(StartBot);
		} else {
			DPSUtils.stopBot();
			token.setDisable(false);
			token.setEditable(true);
			StartBot.setText("Start catching 100% IV pokemon for me!!");
			this.start = false;
		}
	}

	public void initializePokemonList() throws InterruptedException {
		Thread.sleep(1100);
		int x = 0;
		ArrayList<Pokemon> list = AllJsonData.getPokelist();
		for (Pokemon poke : list) {
			CheckBox checkBox = new CheckBox();
			checkBox.setText(poke.getDispalyName());
			checkBox.setSelected(poke.getCatchable());
			checkBox.setId(poke.getId().toString());
			checkBox.selectedProperty().addListener(new MyPokemonChangeListener<Boolean>(poke));
			Label lable = new Label(poke.getAmount().toString());
			poke.setLabel(lable);
			RowConstraints row = new RowConstraints();
			row.setMaxHeight(30);
			row.setMinHeight(30);
			PokeListGrid.getRowConstraints().add(row);
			PokeListGrid.addRow(x, checkBox, lable);
			x++;
		}

		this.token.setText(AllJsonData.getToken());
		AllJsonData.setLog(this.LogArea);
		this.LogArea.heightProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldvalue, Object newValue) {
				ScrollForLog.setVvalue((Double) 1.0);
			}
		});
		AllJsonData.setScrollForLog(this.ScrollForLog);

	}

	public void showPopUp() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText("DiscordPokeSniper " + DPSUtils.getVersion());
		alert.setContentText(
				"This program was built by RebliNk17.\nIt is based on DiscordSniper of Candy and PokeSniper2 program.");
		alert.showAndWait();
	}
}
