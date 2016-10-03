package entities;

import java.util.ArrayList;

import application.JSONHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class AllJsonData {
	private static String token;
	private static ArrayList<Pokemon> pokelist;
	private static VBox view;
	private static ScrollPane ScrollForLog;
	
	public AllJsonData(String token, ArrayList<Pokemon> list) {
		AllJsonData.token = token;
		AllJsonData.pokelist = list;
	}

	public static String getToken() {
		return token;
	}

	public static void setToken(String token) {
		AllJsonData.token = token;
		JSONHandler.UpdatePokeList();
	}

	public static ArrayList<Pokemon> getPokelist() {
		return pokelist;
	}

	public static void setPokelist(ArrayList<Pokemon> pokelist) {
		AllJsonData.pokelist = pokelist;
	}

	public static void setLog(VBox logArea) {
		AllJsonData.view = logArea;
	}
	
	public static VBox getLog() {
		return view;
	}

	public static ScrollPane getScrollForLog() {
		return AllJsonData.ScrollForLog;
	}

	public static void setScrollForLog(ScrollPane scrollForLog) {
		AllJsonData.ScrollForLog = scrollForLog;
	}
}
