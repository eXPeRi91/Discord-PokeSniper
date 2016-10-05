package application;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import entities.AllJsonData;
import entities.Pokemon;
import resources.DPSUtils;

public class JSONHandler {

	@SuppressWarnings("rawtypes")
	public static void PokeList() {
		HashMap<String, Pokemon> pokehash = new HashMap<>();
		ArrayList<Pokemon> poke = new ArrayList<>();
		try {
			FileReader reader = new FileReader(DPSUtils.getCurrentDirectory() + "/settings.json");
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
			String token = (String) jsonObject.get("token");
			String amount = (String) jsonObject.get("amountToCatch");
			String farm = (String) jsonObject.get("autoFarm");
			Boolean autoFarm = farm == null ? true : Boolean.parseBoolean(farm);
			JSONArray pokemon = (JSONArray) jsonObject.get("pokemons");
			Iterator i = pokemon.iterator();
			boolean start = false;
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				if (innerObj.get("id") != null) {
					String pokeName = (String) innerObj.get("name");
					if (pokeName.equals("Exeggcutor"))
						pokeName = "Exeggutor";
					Integer number = Integer.parseInt((String) innerObj.get("id"));
					if (number == 69 && pokeName.equals("Machamp"))
						start = true;
					if (start) {
						number = number - 1;
					}
					String checkName = pokeName;
					if (number == 29) {
						checkName = "Nidoran (F)";
						pokeName = "nidoran-f";
					}
					if (number == 32) {
						checkName = "Nidoran (M)";
						pokeName = "nidoran-m";
					}
					if (number == 84)
						checkName = "Farfetch'd";

					if ((String) innerObj.get("displayName") == null)
						checkName = pokeName;
					pokehash.put(checkName, new Pokemon(number, pokeName, (Boolean) innerObj.get("catch"),
							checkName.equals(pokeName) ? null : checkName, (String) innerObj.get("amount")));
					if (innerObj.get("amount") != null)
						DPSUtils.setPokeCatchCounter(Integer.parseInt((String) innerObj.get("amount")));
				}
			}
			Iterator it = pokehash.entrySet().iterator();
			for (int x = 0; x <= pokehash.size(); x++) {
				poke.add(new Pokemon());
			}
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				poke.get(((Pokemon) pair.getValue()).getId()).setPokemon((Pokemon) pair.getValue());
				it.remove();
			}
			if (amount == null)
				amount = "921";
			new AllJsonData(token, poke, amount, autoFarm);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void UpdatePokeList() {
		try {
			FileWriter write = new FileWriter(DPSUtils.getCurrentDirectory() + "/settings.json");
			String str = "{\n";
			str += "\t\"token\": \"" + AllJsonData.getToken() + "\", \n";
			str += "\t\"amountToCatch\": \"" + AllJsonData.getAmountToCatch() + "\", \n";
			str += "\t\"autoFarm\": \"" + AllJsonData.getPokeFarm() + "\", \n";
			str += "\t\"pokemons\": [\n";
			for (Pokemon pokemon : AllJsonData.getPokelist()) {
				if (pokemon.getId() != null)
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