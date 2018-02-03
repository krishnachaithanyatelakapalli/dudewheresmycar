package com.mtks.finalproject.dudewheresmycar.parkfind;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Krishna on 5/17/2017.
 */

public class JSONParser {
    JSONParser() {

    }

    public JSONArray getJSONArray(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() != 0) {
                return jsonArray;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getJSONObject(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject != null) {
                return jsonObject;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
