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
import listeners.MyMasterBallListener;
import listeners.MyPokemonChangeListener;
import resources.DPSUtils;
import javafx.scene.control.CheckBox;

public class MyController implements Initializable {
	@FXML
	private Button StartBot;
	@FXML
	private GridPane PokeListGrid;
	@FXML
	private TextField token;
	@FXML
	private VBox LogArea;
	@FXML
	private ScrollPane ScrollForLog;
	@FXML
	private Button MoreInfo;
	@FXML
	private Button resetCounter;
	@FXML
	private Label pokeCounter;
	@FXML
	private TextField totalAmount;
	@FXML
	private CheckBox farmPokestops;

	private static Boolean start = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			initializePokemonList();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void pokeFarmEnable() {
		AllJsonData.setPokeFarm(farmPokestops.isSelected());
	}

	public void resetCounterNow() {
		for (Pokemon poke : AllJsonData.getPokelist()) {
			if (poke.getId() != null)
				poke.setAmount(0);
		}
		pokeCounter.setText("0");
	}

	public void updateToken() {
		AllJsonData.setToken(this.token.getText());
	}

	public void openPaypalLink() {
		try {
			Desktop.getDesktop().browse(
					new URL("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=WNDFMD6MVL4KN")
							.toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void startTheBot() {
		// TODO
		if (token.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No token entered!");
			alert.setHeaderText("No token was entered!");
			alert.setContentText("Please enter token and try again!");
			alert.showAndWait();
			return;
		}
		if (!MyController.start) {
			token.setEditable(false);
			token.setDisable(true);
			MyController.start = true;
			StartBot.setText("Starting program, please wait!");
			StartBot.setDisable(true);
			DPSUtils.startBot(StartBot, token);
		} else {
			DPSUtils.stopBot();
			token.setDisable(false);
			token.setEditable(true);
			StartBot.setText("Start catching 100 IV!");
			MyController.start = false;
		}
	}

	public void initializePokemonList() throws InterruptedException {
		int x = 0;
		ArrayList<Pokemon> list = AllJsonData.getPokelist();
		for (Pokemon poke : list) {
			if (poke.getId() != null) {
				CheckBox checkBox = new CheckBox();
				checkBox.setText(String.format("%03d", poke.getId()) +": " +poke.getDisplayName());
				checkBox.setSelected(poke.getCatchable());
				checkBox.setId(poke.getId().toString());
				checkBox.selectedProperty().addListener(new MyPokemonChangeListener<Boolean>(poke));
				Label lable = new Label(poke.getAmount().toString());
				poke.setLabel(lable);
				poke.setCheckbox(checkBox);
				RowConstraints row = new RowConstraints();
				row.setMaxHeight(20);
				row.setMinHeight(20);
				PokeListGrid.getRowConstraints().add(row);
				PokeListGrid.addRow(x, checkBox, lable);
				x++;
			}
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
		pokeCounter.setText(DPSUtils.getPokeCatchCounter().toString());
		totalAmount.setText(AllJsonData.getAmountToCatch().toString());
		DPSUtils.setFullCounter(pokeCounter);
		farmPokestops.selectedProperty().addListener(new MyMasterBallListener<Boolean>(farmPokestops));
		farmPokestops.setSelected(AllJsonData.getPokeFarm());
	}

	public void showPopUp() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText("DiscordPokeSniper " + DPSUtils.getVersion());
		alert.setContentText(
				"This program was built by RebliNk17.\n\nIt is based on DiscordSniper of CandyBuns, PokeSniper2 and Masterball bot.\n\n"
						+ "All of them are combined together so you can snipe all night long ;)\n\n" + "Enjoy!\n\n"
						+ "If you can afford, please consider donation. Thank you :)");
		alert.showAndWait();
	}

	public static Boolean getStart() {
		return start;
	}

	public void changeTotalAmount() {
		if (!totalAmount.getText().isEmpty()) {
			if (Integer.parseInt(totalAmount.getText()) > 995)
				totalAmount.setText("995");
			AllJsonData.setAmountToCatch(Integer.parseInt(totalAmount.getText()));
		}
	}

	public void selectAllpokemons() {
		for (Pokemon poke : AllJsonData.getPokelist()) {
			if (poke.getId() != null)
				poke.getCheckbox().setSelected(true);
		}
	}

	public void deSelectAllPokemons() {
		for (Pokemon poke : AllJsonData.getPokelist()) {
			if (poke.getId() != null)
				poke.getCheckbox().setSelected(false);
		}
	}

	public static void setStart(Boolean start) {
		MyController.start = start;
	}

}
