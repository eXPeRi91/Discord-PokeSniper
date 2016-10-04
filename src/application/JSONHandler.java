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

public class JSONHandler {

	public static void PokeList() {
		ArrayList<Pokemon> poke = new ArrayList<>();
		try {
			FileReader reader = new FileReader(DPSUtils.getCurrentDirectory() + "/settings.json");
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
			String token = (String) jsonObject.get("token");
			String amount = (String) jsonObject.get("amountToCatch");
			JSONArray pokemon = (JSONArray) jsonObject.get("pokemons");
			@SuppressWarnings("rawtypes")
			Iterator i = pokemon.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				if (innerObj.get("id") != null) {
					poke.add(new Pokemon(Integer.parseInt((String) innerObj.get("id")), (String) innerObj.get("name"),
							(Boolean) innerObj.get("catch"), (String) innerObj.get("displayName"),
							(String) innerObj.get("amount")));
					if (innerObj.get("amount") != null)
						DPSUtils.setPokeCatchCounter(Integer.parseInt((String) innerObj.get("amount")));
				}
			}
			if(amount == null)
				amount = "921";
			new AllJsonData(token, poke, amount);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void UpdatePokeList() {
		try {
			FileWriter write = new FileWriter(DPSUtils.getCurrentDirectory() + "/settings.json");
			String str = "{\n";
			str += "\t\"token\": \"" + AllJsonData.getToken() + "\", \n";
			str += "\t\"amountToCatch\": \""+ AllJsonData.getAmountToCatch() +"\", \n";
			str += "\t\"pokemons\": [\n";
			for (Pokemon pokemon : AllJsonData.getPokelist()) {
				str += pokemon.toJSON();
			}
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