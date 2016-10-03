package resources;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import entities.PokeLocation;

public class SnipeCache {

	public static Map<PokeLocation, Long> sniperCache = new ConcurrentHashMap<PokeLocation, Long>();

	public static void addToCache(PokeLocation pokeLocation) {
		sniperCache.put(pokeLocation, System.currentTimeMillis());
	}

	public static boolean isCached(PokeLocation pokeLocation) {
		return sniperCache.containsKey(pokeLocation);
	}

}
