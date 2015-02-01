package com.example.adrian.nba;

/**
 * Created by sscsis on 1/10/15.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Async task class to get json by making HTTP call
 * */
public class GetGameTask extends AsyncTask<Void, Void,Map<String, String[]>>{
    private Activity activity;
    private String url = "http://mi.nba.com/statsm2/game/snapshot.json";
    private String gameId;
    public GetGameTask(String gameId, Activity activity){

        this.activity = activity;
        this.gameId = gameId;
    }

    @Override
    protected Map<String, String[]> doInBackground(Void... arg0) {
        HashMap<String, String[]> map = new HashMap<String, String[]>();

        HttpService http = new HttpService();

        try {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("gameId",gameId));

            String response = http.get(url,params);
            JSONObject json = new JSONObject(response);

            JSONObject payload = json.getJSONObject("payload");
            JSONObject gameProfile = payload.getJSONObject("gameProfile");

            String [] homePlayers =null;
            String [] awayPlayers = null;
            String homeTeamName  = parseTeam(payload, "homeTeam", homePlayers);
            String awayTeamName  = parseTeam(payload, "awayTeam", awayPlayers);
            map.put("homeName",new String[]{homeTeamName});
            map.put("awayName",new String[]{awayTeamName});


            String arenaName = gameProfile.getString("arenaName");
            map.put("arenaName",new String[]{arenaName});
            map.put("matchup", new String[]{awayTeamName + " @ " + homeTeamName});
            map.put("home", homePlayers);
            map.put("away", awayPlayers);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return map;
    }

    private String parseTeam(JSONObject payload, String prop, String [] players) throws JSONException {
        JSONObject homeTeam = payload.getJSONObject(prop);

        JSONArray gamePlayers = homeTeam.getJSONArray("gamePlayers");
        players = new String[gamePlayers.length()];
        for(int i =0; i < gamePlayers.length(); i++){
            JSONObject player = gamePlayers.getJSONObject(i);
            JSONObject profile = player.getJSONObject("profile");
            String displayName = profile.getString("displayName");
            players[i] = displayName;
        }
        return homeTeam.getJSONObject("profile").getString("city") + " " + homeTeam.getJSONObject("profile").getString("name");
    }

    @Override
    protected void onPostExecute(Map<String, String[]> dataMap) {
        super.onPostExecute(dataMap);

        TextView arenaName = (TextView)activity.findViewById(R.id.arena);
        arenaName.setText(dataMap.get("arenaName")[0]);
        TextView matchup = (TextView)activity.findViewById(R.id.matchup);
        matchup.setText(dataMap.get("matchup")[0]);

        ArrayList<Map<String, String>> dataList = new ArrayList<Map<String, String>>();

        //showPlayerList(dataList, dataMap.get("home"));
        //showPlayerList(dataList, dataMap.get("away"));



    }

    private void showPlayerList(ArrayList<Map<String, String>> dataList, String[] homePlayers) {

        ListView list = (ListView) activity.findViewById(R.id.player_list);
        for(int i=0; i < homePlayers.length; i++){
            Map<String,String> playerMap = new HashMap();
            playerMap.put("name",homePlayers[i]);
            dataList.add(playerMap);
        }
        ListAdapter adapter = new SimpleAdapter(
                activity,
                dataList,
                R.layout.player_list_item,
                new String[] {"name"},
                new int[] { R.id.name});

        list.setAdapter(adapter);
    }

}
