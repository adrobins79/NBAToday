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
public class GetGameTask extends AsyncTask<Void, Void,Void>{
    private Activity activity;
    private String url = "http://mi.nba.com/statsm2/game/snapshot.json";
    private String rosterUrl = "http://mi.nba.com/statsm2/team/roster.json";
    private String gameId;
    private String matchupText;
    private String arenaName;
    private Team homeTeam;
    private Team awayTeam;
    public GetGameTask(String gameId, Activity activity){

        this.activity = activity;
        this.gameId = gameId;
        homeTeam = new Team();
        awayTeam = new Team();
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HashMap<String, String[]> map = new HashMap<String, String[]>();

        HttpService http = new HttpService();

        try {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("gameId",gameId));

            String response = http.get(url,params);
            JSONObject json = new JSONObject(response);

            JSONObject payload = json.getJSONObject("payload");
            JSONObject gameProfile = payload.getJSONObject("gameProfile");

            loadTeam(http, payload.getJSONObject("homeTeam"),homeTeam);
            loadTeam(http, payload.getJSONObject("awayTeam"),awayTeam);

            arenaName = gameProfile.getString("arenaName");
            matchupText = awayTeam.getDisplayName() + " @ " + homeTeam.getDisplayName();


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadTeam(HttpService http, JSONObject teamNode,Team team) throws JSONException, ServiceException {
        JSONObject profile = teamNode.getJSONObject("profile");

        List<NameValuePair> params;
        params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("teamId",profile.getString("id")));

        JSONObject rosterRoot = new JSONObject(http.get(rosterUrl,params));
        JSONArray players = rosterRoot.getJSONObject("payload").getJSONArray("players");

        String [] names = new String[players.length()];
        for(int i =0; i < players.length(); i++){
            JSONObject player = players.getJSONObject(i);
            String displayName = player.getJSONObject("profile").getString("displayName");
            names[i] = displayName;
        }

        team.setDisplayName(teamNode.getJSONObject("profile").getString("city") + " " + teamNode.getJSONObject("profile").getString("name"));
        team.setPlayerNames(names);
    }

    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);

        TextView arenaNameTV = (TextView)activity.findViewById(R.id.arena);
        arenaNameTV.setText(arenaName);
        TextView matchupTV = (TextView)activity.findViewById(R.id.matchup);
        matchupTV.setText(matchupText);

        TextView homeNameTV = (TextView)activity.findViewById(R.id.home_team_name);
        homeNameTV.setText(homeTeam.getDisplayName());
        populatePlayerList(homeTeam,(ListView) activity.findViewById(R.id.home_player_list));

        TextView awayNameTV = (TextView)activity.findViewById(R.id.away_team_name);
        awayNameTV.setText(awayTeam.getDisplayName());
        populatePlayerList(awayTeam,(ListView) activity.findViewById(R.id.away_player_list) );
    }

    private void populatePlayerList(Team team, ListView lv) {

        ArrayList<Map<String, String>> mapList = new ArrayList<>();

        for(int i=0; i < team.getPlayerNames().length; i++){
            Map<String,String> playerMap = new HashMap();
            playerMap.put("name",team.getPlayerNames()[i]);
            mapList.add(playerMap);
        }
        ListAdapter homeAdapter = new SimpleAdapter(
                activity,
                mapList,
                R.layout.player_list_item,
                new String[] {"name"},
                new int[] { R.id.name});

        lv.setAdapter(homeAdapter);
    }

}
