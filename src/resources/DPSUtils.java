package resources;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.slf4j.LoggerFactory;

import application.JSONHandler;
import application.Main;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import entities.AllJsonData;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import threads.DiscordConnection;

public class DPSUtils {
	private static String currentDirectory = null;
	private static DiscordConnection disCon;
	private static String version = "v1.1";
	private static Boolean running = false;
	public static double formatCoords(double coords) {
		DecimalFormat df = new DecimalFormat("000.00000");
		return Double.parseDouble(df.format(coords).replace(',', '.'));
	}

	public static void log(String logMessage) {
		LocalTime time = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
		System.out.println("[" + time.format(formatter) + "] " + logMessage);
		DPSUtils.updateLogArea("[" + time.format(formatter) + "] " + logMessage);
	}

	public static String getToken() {
		return AllJsonData.getToken();
	}

	public static void loadSnipingPokemon() {
		JSONHandler.PokeList();
	}

	public static void updateLogArea(String str) {
		Label lab = new Label(str);
		lab.setMinWidth(Region.USE_PREF_SIZE);
		RowConstraints row = new RowConstraints();
		row.setMaxHeight(30);
		row.setMinHeight(30);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				AllJsonData.getLog().getChildren().add(lab);
				AllJsonData.getScrollForLog().setVvalue(1.0);
			}
		});

	}

	public static void disableLoggers() {
		Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.setLevel(Level.ERROR);
	}


	public static void startBot(Button btn, TextField token) {
		disableLoggers();
		setCurrentDirectoryLocation();
		DPSUtils.log("Starting Program ");
		disCon = new DiscordConnection(btn,token);
		disCon.start();
	}
	
	public static void stopBot() {
		disCon.terminate();
	}

	public static void setCurrentDirectoryLocation() {
		try {
			CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
			File jarFile = new File(codeSource.getLocation().toURI().getPath());
			currentDirectory = jarFile.getParentFile().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static String getCurrentDirectory() {
		return currentDirectory;
	}

	public static String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		DPSUtils.version = version;
	}

	public static Boolean getRunning() {
		return running;
	}

	public static void setRunning(Boolean running) {
		DPSUtils.running = running;
	}

}
