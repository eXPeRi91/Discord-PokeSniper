package application;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import entities.AllJsonData;
import entities.Pokemon;
import resources.DPSUtils;

/**
 * @author Crunchify.com
 */
/*
 * example { "id": 1, "firstname": "Katerina", "languages": [ {"lang":
 * "en","knowledge": "proficient"}, {"lang": "fr","knowledge": "advanced"} ],
 * "job": { "site": "www.javacodegeeks.com", "name": "Java Code Geeks" } }
 */
public class JSONHandler {

	public static void PokeList() {
		ArrayList<Pokemon> poke = new ArrayList<>();
		//JSONParser parser = new JSONParser();
		try {
			FileReader reader = new FileReader(DPSUtils.getCurrentDirectory() + "/settings.json");
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
			// get a String from the JSON object
			String token = (String) jsonObject.get("token");
			// System.out.println("The first name is: " + firstName);
			// get a number from the JSON object
			// long id = (long) jsonObject.get("id");
			// System.out.println("The id is: " + id);
			// get an array from the JSON object
			JSONArray pokemon = (JSONArray) jsonObject.get("pokemons");
			// take the elements of the json array
			// for (int i = 0; i < pokemon.size(); i++) {
			// System.out.println("The " + i + " element of the array: " +
			// pokemon.get(i));
			// }
			@SuppressWarnings("rawtypes")
			Iterator i = pokemon.iterator();
			// take each value from the json array separately
			// int x= 0;
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				if (innerObj.get("id") != null) {
					poke.add(new Pokemon(
							Integer.parseInt(
									(String) innerObj.get("id")), 
									(String) innerObj.get("name"),
									(Boolean) innerObj.get("catch"), 
									(String) innerObj.get("displayName"),
									(String) innerObj.get("amount")));
				}
			}
			new AllJsonData(token, poke);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void UpdatePokeList() {
		try {
			FileWriter write = new FileWriter(DPSUtils.getCurrentDirectory() + "/settings.json");
			String str = "{\n";
			str += "\t\"token\": \"" + AllJsonData.getToken() + "\", \n";
			str += "\t\"pokemons\": [\n";
			for (Pokemon pokemon : AllJsonData.getPokelist()) {
				str += pokemon.toJSON();
			}
			str += "\t\t{}\n";
			str += "\t]\n";
			str += "}";
			write.write(str);
			write.flush();
			write.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}
}