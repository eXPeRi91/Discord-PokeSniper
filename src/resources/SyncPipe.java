package resources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import entities.AllJsonData;
import entities.Pokemon;

public class SyncPipe implements Runnable {
	private Boolean flag = false;
	public SyncPipe(InputStream istrm, OutputStream ostrm) {
		istrm_ = istrm;
		// ostrm_ = ostrm;
	}

	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(istrm_, "UTF-8"));
			String str = new String();
			while ((str = br.readLine()) != null) {
				analyzeString(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void analyzeString(String str) {
		Pokemon temp = checkWhatWasCaught(str);
		if (str.contains("There is no"))
			DPSUtils.log("The Pokemon " + temp.getDisplayName() + " was not found at the location.");
		else if (str.contains("We caught a")) {
			temp.updateLableValue();
			DPSUtils.log("The Pokemon " + temp.getDisplayName() + " was caught.");
		} else if (str.contains("got away.")) {
			DPSUtils.log("The Pokemon " + temp.getDisplayName() + " got away.");
		} else if (str.contains("We have") && str.contains("berries left")) {
			Pattern p = Pattern.compile("\\d+");
			Matcher m = p.matcher(str);
			String s = "";
			for (int x = 0; x < 6 && m.find(); x++) {
				if (x == 3) {
					s += "We have " + m.group() + " Pokeballs, ";
					if (Integer.parseInt(m.group()) == 0) {
						DPSUtils.stopBot();
						this.flag = true;
					}
				} else if (x == 4) {
					s += "" + m.group() + " berries, ";
				} else if (x == 5) {
					s += "" + m.group() + " Pokemons";
				}
			}
			DPSUtils.log(s);
		} else if (str.contains("Got into the fight without any Pokeballs")) {
			flag = true;
		} else if(str.contains("is not recognized as an internal or externa")) {
			DPSUtils.log(" - Can not find Pokesniper2.exe file. ");
			DPSUtils.log(" - did you put them in the same folder?");
			DPSUtils.stopBot("Can not catch pokemons with no Pokesniper2.exe file.");
		} else if(str.contains("Could not load settings")) {
			DPSUtils.log(" - Please check whether user.xml file is edited currectly.");
			DPSUtils.stopBot("Problem with user.xml file.");
		} else if(str.contains("Please confirm that the PokemonGo servers are online before using")) {
			DPSUtils.log(" - Please check whether user.xml file is edited currectly, or PokemonGo servers are online!");
			DPSUtils.stopBot("Could not connect to Pokemon Go servers.");
		} else {
			
		}

		if (flag == true) {
			DPSUtils.stopBot("No more Pokeballs left!");
			flag = false;
		}
	}

	private Pokemon checkWhatWasCaught(String str) {
		Pokemon pokemonType = null;
		for (Pokemon type : AllJsonData.getPokelist()) {
			if (StringUtils.containsIgnoreCase(str, type.getName())
					|| StringUtils.containsIgnoreCase(str, type.getDisplayName())) {
				pokemonType = type;
				break;
			}
		}
		return pokemonType;
	}

	// private final OutputStream ostrm_;
	private final InputStream istrm_;
}