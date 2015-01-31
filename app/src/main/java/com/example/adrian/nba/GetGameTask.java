package com.example.adrian.nba;

/**
 * Created by sscsis on 1/10/15.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Async task class to get json by making HTTP call
 * */
public class GetGameTask extends AsyncTask<Void, Void,Map<String, String>>{
    private Activity activity;
    private String url = "http://mi.nba.com/statsm2/game/snapshot.json";
    private String gameId;
    public GetGameTask(String gameId, Activity activity){

        this.activity = activity;
        this.gameId = gameId;
    }

    @Override
    protected Map<String, String> doInBackground(Void... arg0) {
        HashMap<String, String> map = new HashMap<String, String>();

        HttpService http = new HttpService();

        try {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("gameId",gameId));

            String response = http.get(url,params);
            JSONObject json = new JSONObject(response);

            JSONObject payload = json.getJSONObject("payload");
            JSONObject gameProfile = payload.getJSONObject("gameProfile");
            String arenaName = gameProfile.getString("arenaName");

            map.put("arenaName",arenaName);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    protected void onPostExecute(Map<String, String> dataMap) {
        super.onPostExecute(dataMap);

        TextView arenaName = (TextView)activity.findViewById(R.id.arenaName);
        arenaName.setText(dataMap.get("arenaName"));
    }

}
