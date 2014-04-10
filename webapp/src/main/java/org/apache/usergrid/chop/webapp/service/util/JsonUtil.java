package org.apache.usergrid.chop.webapp.service.util;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonUtil {

    private static Logger LOG = LoggerFactory.getLogger(JsonUtil.class);

    public static void put(JSONObject json, String key, Object value) {
        try {
            json.put(key, value);
        } catch (JSONException e) {
            LOG.error("Exception while put to json: ", e);
        }
    }

    public static void inc(JSONObject json, String key, long incValue) {
        try {
            long value = json.optLong(key) + incValue;
            json.put(key, value);
        } catch (JSONException e) {
            LOG.error("Exception while inc json: ", e);
        }
    }

    public static void copy(JSONObject src, JSONObject dest) {
        Iterator iter = src.keys();

        try {
            while (iter.hasNext()) {
                String key = (String) iter.next();
                put(dest, key, src.get(key) );
            }
        } catch (JSONException e) {
            LOG.error("Exception while copying json: ", e);
        }
    }

    public static void copy(JSONObject src, JSONObject dest, String key) {
        Object value = src.opt(key);

        if (value != null) {
            put(dest, key, value);
        }
    }

    public static List<String> getKeys(JSONObject json) {

        ArrayList<String> keys = new ArrayList<String>();
        Iterator iter = json.keys();

        while ( iter.hasNext() ) {
            keys.add( (String) iter.next() );
        }

        return keys;
    }

    public static JSONArray parseArray(String s) {

        JSONArray arr = new JSONArray();

        if ( StringUtils.isEmpty(s) ) {
            return arr;
        }

        try {
            arr = new JSONArray(s);
        } catch (JSONException e) {
            LOG.error("Exception while parsing string to json: ", e);
        }

        return arr;
    }

    public static JSONObject get(JSONArray arr, int i) {

        JSONObject json = null;

        try {
            json = arr.getJSONObject(i);
        } catch (JSONException e) {
            LOG.error("Exception while getting element from json array: ", e);
        }

        return json;
    }

}
