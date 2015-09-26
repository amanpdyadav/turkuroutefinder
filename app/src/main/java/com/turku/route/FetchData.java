package com.turku.route;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import net.sf.json.xml.XMLSerializer;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.ClientProtocolException;

public class FetchData {

    public JSON getXMLdataFromurl(String url) {
        try {
            return new xmlData().execute(url).get();
        }
        catch (InterruptedException e) {}
        catch (ExecutionException e) {}
        return null;
    }

    private class xmlData extends AsyncTask<String, Void, JSON> {
        JSON objJson;
        @Override
        protected JSON doInBackground(String... url) {
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url[0]);
                HttpResponse httpResponse;
                httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                objJson = new XMLSerializer().read(EntityUtils.toString(httpEntity));
                Log.d("TAG", objJson.toString());
            }
            catch (ClientProtocolException e) {}
            catch (IOException e) {}
            return objJson;
        };

    }


    /*********************************************************************************************************************
     * 						SETTING AUTOCOMPLETE DATA FROM MATKA DATABASE.												 *
     *********************************************************************************************************************/
    public List<String> autocomplete(String input){
        List<String> resultList = new ArrayList<String>();
        JSONArray address;
        JSON xml = getXMLdataFromurl("http://api.matka.fi/public-lvm/fi/api/?key="
                + input + "&user=Projektkurs&pass=Reittiopas");
        JSONObject jobj = (JSONObject) xml;
        try{
            address = jobj.getJSONObject("GEOCODE").getJSONArray("LOC");
            for (int i = 0; i < address.size(); i++) {
                if (address.getJSONObject(i).getString("@city")
                        .equalsIgnoreCase("turku")
                        || address.getJSONObject(i).getString("@city")
                        .equalsIgnoreCase("kaarina")
                        || address.getJSONObject(i).getString("@city")
                        .equalsIgnoreCase("naantali")
                        || address.getJSONObject(i).getString("@city")
                        .equalsIgnoreCase("" +
                                "" +
                                "")) {
                    resultList.add(address.getJSONObject(i).getString("@name1")
                            + "" + address.getJSONObject(i).getString("@number")
                            + "," + address.getJSONObject(i).getString("@city"));
                }
            }
        }
        catch(Exception e){
            JSONObject keys = jobj.getJSONObject("GEOCODE");
            String result = keys.keySet().contains("LOC")+"";
            if(result.equalsIgnoreCase("false"))
            {
                resultList.add("No match found.");
                return resultList;
            }
            resultList.add(jobj.getJSONObject("GEOCODE").getJSONObject("LOC").getString("@name1")
                    + "" + jobj.getJSONObject("GEOCODE").getJSONObject("LOC").getString("@number")
                    + "," + jobj.getJSONObject("GEOCODE").getJSONObject("LOC").getString("@city"));
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

        str=str.replaceAll(" ", "%20");
        str=(str.toLowerCase()).replaceAll(",", "%2c");
        str=(str.toLowerCase()).replaceAll("ä", "%e4");
        str=(str.toLowerCase()).replaceAll("å", "%e5");
        str=(str.toLowerCase()).replaceAll("ö", "%f6");
        return str;
    }
}
