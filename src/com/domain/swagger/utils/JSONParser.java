/**
 * 
 */
package com.domain.swagger.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * @author lo
 *
 */
public class JSONParser< T> {

	private static Gson gson = new Gson();

	public String createGson( Object input) {
		return gson.toJson( input);
	}

	public T createObject( String jsonString, Class< T> t) {
		return gson.fromJson( jsonString, t);
	}

	public static String toJson( Object obj) {
		GsonBuilder bg = new GsonBuilder();
		bg.setPrettyPrinting();

		return bg.create().toJson( obj);
	}
}
