package com.turku.route;

import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;


public class FetchData {

    private final String USER_AGENT = "Mozilla/5.0";

    public JSONObject getXMLdataFromurl(String url) {
        try {
            return new xmlData().execute(url).get();
        }
        catch (InterruptedException e) {}
        catch (ExecutionException e) {}
        return null;
    }

    private class xmlData extends AsyncTask<String, Void, JSONObject> {
        JSONObject objJson;
        @Override
        protected JSONObject doInBackground(String... url) {
            try {
                System.out.println("Testing 1 - Send Http GET request");
                String res = sendGet(url[0]);
                objJson = new JSONObject(res);
                Log.d("TAG", objJson.toString());
            }
            catch (Exception e) {}
            return objJson;
        };

    }


    /*********************************************************************************************************************
     * 						SETTING AUTOCOMPLETE DATA FROM MATKA DATABASE.												 *
     *********************************************************************************************************************/
    public List<String> autocomplete(String input){
        List<String> resultList = new ArrayList<String>();
        JSONArray address;
        JSONObject xml = getXMLdataFromurl("http://api.matka.fi/public-lvm/fi/api/?key="
                + input + "&user=Projektkurs&pass=Reittiopas");
        JSONObject jobj = (JSONObject) xml;
        try{
            address = jobj.getJSONObject("GEOCODE").getJSONArray("LOC");
            for (int i = 0; i < address.length(); i++) {
                if (address.getJSONObject(i).getString("city")
                        .equalsIgnoreCase("turku")
                        || address.getJSONObject(i).getString("city")
                        .equalsIgnoreCase("kaarina")
                        || address.getJSONObject(i).getString("city")
                        .equalsIgnoreCase("naantali")
                        || address.getJSONObject(i).getString("city")
                        .equalsIgnoreCase("" +
                                "" +
                                "")) {
                    resultList.add(address.getJSONObject(i).getString("name1")
                            + "" + address.getJSONObject(i).getString("number")
                            + "," + address.getJSONObject(i).getString("city"));
                }
            }
        }
        catch(Exception e){
            try {
                JSONObject keys = jobj.getJSONObject("GEOCODE");
                String result = keys.keys().toString().contains("LOC")+"";
                if(result.equalsIgnoreCase("false"))
                {
                    resultList.add("No match found.");
                    return resultList;
                }
                resultList.add(jobj.getJSONObject("GEOCODE").getJSONObject("LOC").getString("name1")
                        + "" + jobj.getJSONObject("GEOCODE").getJSONObject("LOC").getString("number")
                        + "," + jobj.getJSONObject("GEOCODE").getJSONObject("LOC").getString("city"));
            }catch (JSONException ee){}
        }
        return resultList;
    }

    public static List<String> getDefaultAddress(){
        List<String> resultList = new ArrayList<String>();
        resultList.add("Turun Yliopisto");
        resultList.add("Tuomiokirkko");
        resultList.add("Muumimaailma");
        resultList.add("Lentokenttä");
        resultList.add("Kopkolmio");
        resultList.add("HK Areena");
        resultList.add("Airport");
        resultList.add("Hansa");
        resultList.add("Kauppatori");
        resultList.add("Mylly");
        resultList.add("TYKS");
        resultList.add("Ikea");
        resultList.add("TYS");
        return resultList;
    }
    /*********************************************************************************************************************
     * 						SETTING SOME DEFAULT ADDRESS IN TURKU.														 *
     *********************************************************************************************************************/
    public String validatePosition(String str){
        str=(str.toLowerCase()).replaceAll("turun yliopisto", "Rehtorinpellonkatu 4,Turku");
        str=(str.toLowerCase()).replaceAll("tuomiokirkko", "Tuomiokirkonkatu 1,Turku");
        str=(str.toLowerCase()).replaceAll("lentokenttä", "Lentoasemantie 150,Turku");
        str=(str.toLowerCase()).replaceAll("muumimaailma", "Tuulensuunkatu 14,Naantali");
        str=(str.toLowerCase()).replaceAll("hk areena", "Artukaistentie 8,Turku");
        str=(str.toLowerCase()).replaceAll("airport", "Lentoasemantie 150,Turku");
        str=(str.toLowerCase()).replaceAll("kauppatori", "Kristiinankatu 9,Turku");
        str=(str.toLowerCase()).replaceAll("tyks", "Savitehtaankatu 1,Turku");
        str=(str.toLowerCase()).replaceAll("hansa", "Kristiinankatu 9,Turku");
        str=(str.toLowerCase()).replaceAll("kopkolmio", "Aurakatu 8,Turku");
        str=(str.toLowerCase()).replaceAll("mylly", "Myllynkatu 1,Raisio");
        str=(str.toLowerCase()).replaceAll("ikea", "Itäniityntie 15,Raisio");
        str=(str.toLowerCase()).replaceAll("tys", "Inspehtorinkatu 4,Turku");

        str=str.replaceAll(" ", "%20");
        str=(str.toLowerCase()).replaceAll(",", "%2c");
        str=(str.toLowerCase()).replaceAll("ä", "%e4");
        str=(str.toLowerCase()).replaceAll("å", "%e5");
        str=(str.toLowerCase()).replaceAll("ö", "%f6");
        return str;
    }

    // HTTP GET request
    public String sendGet(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
//        System.out.println(response.toString());
        System.out.println(XML.toJSONObject(response.toString()).toString());
        return XML.toJSONObject(response.toString()).toString();
    }
}
