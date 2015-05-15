package com.krokozyabr.lastfm;

public class Utils {

	public static boolean isEmpty(String string){
		if (string == null)
			return true;
		if (string.equalsIgnoreCase(""))
			return true;
		return false;
	}
}
