package io.akikr.betterreadsdataloader.utils;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author ankit
 * @since 1.0
 * @implNote A utility class to parse JSON-Object
 */

@Slf4j
public class JsonStringParser
{
	/**
	 * @implNote To create a JSON-Object from a JSON-String
	 * @param jsonString a valid jason-string
	 * @return JSONObject
	 */
	public JSONObject getJSONObjectFromString(String jsonString)
	{
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonString);
		} catch (JSONException e) {
			log.error("Error occurred while creating the JSON-Object from JSON-String : " + jsonString + "\n" + e.getMessage());
			return new JSONObject();
		}
		return jsonObject;
	}
}
