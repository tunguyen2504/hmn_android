package hanoi.hms.hanoimetronavigator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class MainActivity extends AppCompatActivity {

    private ImageMap imageMap;
    public static SearchView searchView1, searchView2;
    private EditText searchEditText1, searchEditText2;
    private SimpleCursorAdapter myAdapter;
    private String[] strArrData = {"No Suggestions"};

    private ListView ls;
    private TextView stationCode, stationName;
    private FloatingActionButton fab_navi;
    private RadioGroup radioGroup;

    private String urlStation_ArrivalTime, urlStation_Name, urlLine_Info, urlNavigation;
    private String stCode, stName, firstTrain, lastTrain, nextTrain, destStation;
    private String lineCode, lineName, lineTotalTime, lineStartTime, lineEndTime, lineFreqTime;
    private String stationStartName, stationStartTowards, stationChange, stationChangeTowards, stationArrival, tripTime;
    private double tripFare;
    private double lineLength;
    private ArrayList<Station> stations = new ArrayList<Station>();
    private ArrayList<Line> lines = new ArrayList<Line>();
    private ArrayList<Navigation> navis = new ArrayList<Navigation>();

    private static int stationId, lineId, stationStart = 0, stationEnd = 0;

    private static boolean naviMode = false;

    public StationInfoAdapter stationAdapter;
    public LineInfoAdapter lineAdapter;
    public NavigationAdapter naviAdapter;
    public Navigation navigation;
    public static LinearLayout linearLayout;
    public LinearLayout stCodenName;
    public RelativeLayout.LayoutParams layoutParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] from = new String[]{"fishName"};
        final int[] to = new int[]{android.R.id.text1};

        searchView1 = (SearchView) findViewById(R.id.search_bar_1);
        searchView2 = (SearchView) findViewById(R.id.search_bar_2);
        searchEditText1 = (EditText) searchView1.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText2 = (EditText) searchView2.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        linearLayout = (LinearLayout) findViewById(R.id.listViewParent);
        stCodenName = (LinearLayout) findViewById(R.id.stationCodenName);
        ls = (ListView) findViewById(R.id.listView);
        stationCode = (TextView) findViewById(R.id.stationCode);
        stationName = (TextView) findViewById(R.id.stationName);
        fab_navi = (FloatingActionButton) findViewById(R.id.fabNavi);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        imageMap = (ImageMap) findViewById(R.id.map);
        layoutParams = (RelativeLayout.LayoutParams) imageMap.getLayoutParams();
        navigation = new Navigation();

        myAdapter = new SimpleCursorAdapter(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, null,
                from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        searchView1.setFocusable(false);
        searchView1.setBackground(getResources().getDrawable(R.drawable.searchview_border_shadow));
        searchView1.setQueryHint("Search here");
        searchView1.setIconifiedByDefault(false);
        searchView2.setFocusable(false);
        searchView2.setBackground(getResources().getDrawable(R.drawable.searchview_border_shadow));
        searchView2.setIconifiedByDefault(false);
        searchView2.setVisibility(GONE);
        radioGroup.setVisibility(GONE);
        layoutParams.setMargins(0, 130, 0, 0);
        imageMap.setLayoutParams(layoutParams);

        fab_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!naviMode) {
                    naviMode = true;
                    layoutParams.setMargins(0, 360, 0, 0);
                    //if (searchView1.getVisibility() == GONE) {
                    searchView1.setVisibility(VISIBLE);
                    //}
                    if (searchView2.getVisibility() == GONE) {
                        searchView2.setVisibility(VISIBLE);
                    }
                    searchView1.setQueryHint("Start station");
                    searchView2.setQueryHint("End station");
                    searchView2.setBaselineAligned(true);
                    fab_navi.setImageResource(R.drawable.search);
                    radioGroup.setVisibility(VISIBLE);
                    radioGroup.check(R.id.radio_fastest);
                    imageMap.setLayoutParams(layoutParams);
                } else if (naviMode) {
                    naviMode = false;
                    layoutParams.setMargins(0, 130, 0, 0);
                    if (searchView2.getVisibility() == VISIBLE) {
                        searchView2.setVisibility(GONE);
                    }
                    searchView1.setQueryHint("Search here");
                    fab_navi.setImageResource(R.drawable.navi);
                    radioGroup.setVisibility(GONE);
                    imageMap.setLayoutParams(layoutParams);
                }
            }
        });


        searchEditText1.setTextColor(Color.BLACK);
        searchEditText1.setAlpha(0.8f);
        searchEditText2.setTextColor(Color.BLACK);
        searchEditText2.setAlpha(0.8f);


        // find the image map in the view

        imageMap.setImageResource(R.drawable.map_2000);
        imageMap.setMaxZoom(5);

        // add a click handler to react when areas are tapped
        imageMap.addOnImageMapClickedHandler(new ImageMap.OnImageMapClickedHandler() {
            @Override
            public void onImageMapClicked(int id, ImageMap imageMap) {
                // when the area is tapped, show the name in a
                // text bubble
                if (!naviMode) {
                    if (stationAdapter != null) {
                        stationAdapter.clear();
                    }
                    if (lineAdapter != null) {
                        lineAdapter.clear();
                    }
                    imageMap.showBubble(id);
                    if (id > 2131558590) {
                        stationId = id - 2131558590;
                        Log.d("debug", "Station ID: " + id);

                        urlStation_Name = getString(R.string.host_ipAddress).concat(getString(R.string.url_stationInfo)).concat(String.valueOf(stationId));
                        urlStation_ArrivalTime = getString(R.string.host_ipAddress).concat(getString(R.string.url_stationInfo)).concat(String.valueOf(stationId)).concat(getString(R.string.url_stationInfo_arrivalTime));
                        if (linearLayout.getVisibility() == GONE) {
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                        if (stCodenName.getVisibility() == GONE) {
                            stCodenName.setVisibility(View.VISIBLE);
                        }

                        Ion.with(getApplicationContext())
                                .load(urlStation_ArrivalTime)
                                .asString()
                                .setCallback(new FutureCallback<String>() {
                                    @Override
                                    public void onCompleted(Exception e, String result) {
                                        // do stuff with the result or error
                                        try {
                                            JSONArray stationInfoList = new JSONArray(result);
                                            for (int i = 0; i < stationInfoList.length(); i++) {
                                                final JSONObject stationInfo = stationInfoList.getJSONObject(i);
                                                firstTrain = stationInfo.getString("firstTrain").substring(0, 5);
                                                lastTrain = stationInfo.getString("lastTrain").substring(0, 5);
                                                nextTrain = stationInfo.getString("nextTrain").substring(0, 5);
                                                destStation = stationInfo.getString("destName");
                                                stations.add(new Station(firstTrain, lastTrain, nextTrain, destStation));
                                                stationAdapter = new StationInfoAdapter(getApplicationContext(), R.layout.item_stationinfo, stations);
                                                ls.setAdapter(stationAdapter);
                                            }
                                            try {
                                                Ion.with(getApplicationContext())
                                                        .load(urlStation_Name)
                                                        .asString()
                                                        .setCallback(new FutureCallback<String>() {
                                                            @Override
                                                            public void onCompleted(Exception e, String result) {
                                                                // do stuff with the result or error
                                                                try {
                                                                    JSONObject station = new JSONObject(result);
                                                                    stCode = station.getString("code");
                                                                    stName = station.getString("name");
                                                                    stationCode.setText(stCode);
                                                                    stationName.setText(stName);
                                                                    System.out.println("code: " + stCode + "name: " + stName);
                                                                } catch (JSONException e1) {
                                                                    e1.printStackTrace();
                                                                }
                                                            }
                                                        })
                                                        .get(500, TimeUnit.MILLISECONDS);
                                            } catch (InterruptedException | ExecutionException | TimeoutException e2) {
                                                e.printStackTrace();
                                            }
                                /*System.out.println("first: " +firstTrain);
                                System.out.println("last: " +lastTrain);
                                System.out.println("next: " +nextTrain);
                                System.out.println("dest: " +destStation);*/
                                        } catch (JSONException e2) {
                                            e2.printStackTrace();
                                        }
                                    }
                                });
                        Log.d("debug", "Start execute JSON");
                    } else {
                        if (stCodenName.getVisibility() == View.VISIBLE) {
                            stCodenName.setVisibility(GONE);
                        }
                        lineId = id - 2131558573 + 1;
                        Log.d("debug", "Station ID: " + id);
                        switch (lineId) {
                            case 1:
                            case 2:
                                lineId = 1;
                                break;
                            case 3:
                            case 4:
                                lineId = 2;
                                break;
                            case 5:
                            case 6:
                                lineId = 3;
                                break;
                            case 7:
                            case 8:
                                lineId = 4;
                                break;
                            case 9:
                            case 10:
                                lineId = 5;
                                break;
                            case 11:
                            case 12:
                                lineId = 6;
                                break;
                            case 13:
                            case 14:
                                lineId = 7;
                                break;
                            case 15:
                            case 16:
                                lineId = 8;
                                break;
                            case 17:
                            case 18:
                                lineId = 9;
                                break;
                        }
                        urlLine_Info = getString(R.string.host_ipAddress).concat(getString(R.string.url_lineInfo)).concat(String.valueOf(lineId));
                        if (linearLayout.getVisibility() == GONE) {
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                        try {
                            Ion.with(getApplicationContext())
                                    .load(urlLine_Info)
                                    .asString()
                                    .setCallback(new FutureCallback<String>() {
                                        @Override
                                        public void onCompleted(Exception e, String result) {
                                            // do stuff with the result or error
                                            try {
                                                JSONObject line = new JSONObject(result);
                                                lineCode = line.getString("code");
                                                lineName = line.getString("name");
                                                lineLength = line.getDouble("distance");
                                                lineTotalTime = (line.getString("totalTime").substring(0, 2)).concat("h").concat(line.getString("totalTime").substring(3, 5));
                                                lineStartTime = line.getString("startTime").substring(0, 5);
                                                lineEndTime = line.getString("endTime").substring(0, 5);
                                                lineFreqTime = line.getString("frequency").substring(3, 5).concat(" mins");
                                                lines.add(new Line(lineCode, lineName, lineLength, lineTotalTime, lineStartTime, lineEndTime, lineFreqTime));
                                                lineAdapter = new LineInfoAdapter(getApplicationContext(), R.layout.item_lineinfo, lines);
                                                ls.setAdapter(lineAdapter);
                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    })
                                    .get(500, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException | ExecutionException | TimeoutException e2) {
                            e2.printStackTrace();
                        }
                    }
                } else if (naviMode) {
                    Log.d("debug", String.valueOf(naviMode));
                    if (stationAdapter != null) {
                        stationAdapter.clear();
                    }
                    if (lineAdapter != null) {
                        lineAdapter.clear();
                    }

                    if (id > 2131558590) {
                        imageMap.showBubble(id);
                        stationId = id - 2131558590;
                        urlStation_Name = getString(R.string.host_ipAddress).concat(getString(R.string.url_stationInfo)).concat(String.valueOf(stationId));
                        try {
                            Ion.with(getApplicationContext())
                                    .load(urlStation_Name)
                                    .asString()
                                    .setCallback(new FutureCallback<String>() {
                                        @Override
                                        public void onCompleted(Exception e, String result) {
                                            // do stuff with the result or error
                                            try {
                                                JSONObject station = new JSONObject(result);
                                                stName = station.getString("name");
                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    })
                                    .get(500, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException | ExecutionException | TimeoutException e2) {
                            e2.printStackTrace();
                        }

                        if (searchEditText1.getText().toString().trim().length() == 0 && stationStart == 0) {
                            if (naviAdapter != null) {
                                naviAdapter.clear();
                            }
                            searchEditText1.setText(stName);
                            stationStart = stationId;
                        } else {
                            if (searchEditText2.getText().toString().trim().length() == 0 && stationEnd == 0) {
                                searchEditText2.setText(stName);
                                stationEnd = stationId;
                            }
                        }
                        if (stationStart != 0 && stationEnd != 0) {
                            if (radioGroup.getCheckedRadioButtonId() == R.id.radio_fastest) {
                                urlNavigation = getString(R.string.host_ipAddress).concat(getString(R.string.url_navigation)).concat(String.valueOf(stationStart)).concat("/").concat(String.valueOf(stationEnd)).concat("/").concat("shortest");
                                Log.d("debug", "url: " + urlNavigation);
                            } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_leastTransfer) {
                                urlNavigation = getString(R.string.host_ipAddress).concat(getString(R.string.url_navigation)).concat(String.valueOf(stationStart)).concat("/").concat(String.valueOf(stationEnd)).concat("/").concat("leasttransfer");
                                Log.d("debug", "url: " + urlNavigation);
                            }
                            if (linearLayout.getVisibility() == GONE) {
                                linearLayout.setVisibility(View.VISIBLE);
                            }
                            if (stCodenName.getVisibility() == VISIBLE) {
                                stCodenName.setVisibility(GONE);
                            }
                            try {
                                Ion.with(getApplicationContext())
                                        .load(urlNavigation)
                                        .asString()
                                        .setCallback(new FutureCallback<String>() {
                                            @Override
                                            public void onCompleted(Exception e, String result) {
                                                // do stuff with the result or error
                                                try {
                                                    JSONObject navi = new JSONObject(result);
                                                    tripTime = navi.getString("time").substring(3, 5).concat("'").concat(navi.getString("time").substring(6, 8));
                                                    tripFare = navi.getDouble("fare");
                                                    JSONArray naviArray = navi.getJSONArray("route");
                                                    stationStartName = naviArray.getJSONObject(0).getString("stationName");
                                                    Log.d("debug", "Station Start: " + stationStartName);
                                                    stationStartTowards = naviArray.getJSONObject(0).getString("toward");
                                                    stationArrival = naviArray.getJSONObject(naviArray.length() - 1).getString("stationName");
                                                    Log.d("debug", "Start toward: " + stationStartTowards);
                                                    Log.d("debug", "Arrival: " + stationArrival);
                                                    //View stationChangeLayout = findViewById(R.id.stationChangeLayout);
                                                    //TextView stChange = new TextView(getApplicationContext());
                                                    //TextView stChangeTowards = new TextView(getApplicationContext());
                                                    for (int i = 0; i < naviArray.length(); i++) {
                                                        JSONObject naviObj = naviArray.getJSONObject(i);
                                                        Log.d("debug","isChanged: "+naviObj.getInt("isLineChanged"));
                                                        if (naviObj.getInt("isLineChanged") == 1) {
                                                            stationChange = naviObj.getString("stationName");
                                                            stationChangeTowards = naviObj.getString("toward");
                                                            /*if (stationChangeLayout == null) {
                                                                Log.d("debug", "Layout null");
                                                            }
                                                            if (stChange == null) {
                                                                Log.d("debug", "stChange null");
                                                            }
                                                            if (stChangeTowards == null) {
                                                                Log.d("debug", "stChangeTowards null");
                                                            }
                                                            stChange.setText("Change at ".concat(stationChange));
                                                            stChangeTowards.setText("Towards ".concat(stationChangeTowards));
                                                            stChange.setLayoutParams(new ViewGroup.LayoutParams(
                                                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                                                            stChangeTowards.setLayoutParams(new ViewGroup.LayoutParams(
                                                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                                                            ((LinearLayout) stationChangeLayout).addView(stChange);
                                                            //((LinearLayout) stationChangeLayout).addView(stChangeTowards);
                                                            Log.d("debug", "Change at: " +stationChange);
                                                            Log.d("debug", "Towards: " +stationChangeTowards);*/
                                                        } else {
                                                            stationChange = "";
                                                            stationChangeTowards = "";
                                                        }

                                                        navigation.setStationChange(stationChange);
                                                        navigation.setStationChangeTowards(stationChangeTowards);
                                                    }
                                                    navis.add(new Navigation(stationStartName, stationStartTowards, stationArrival, tripTime, tripFare));
                                                    naviAdapter = new NavigationAdapter(getApplicationContext(), R.layout.item_navi, navis);
                                                    ls.setAdapter(naviAdapter);
                                                    searchEditText1.setText("");
                                                    searchEditText2.setText("");
                                                    stationStart = 0;
                                                    stationEnd = 0;
                                                } catch (JSONException e1) {
                                                    e1.printStackTrace();
                                                }
                                            }
                                        })
                                        .get(1000, TimeUnit.MILLISECONDS);
                            } catch (InterruptedException | ExecutionException | TimeoutException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }
            }


            //Intent intent = new Intent(getApplicationContext(),Station.class);
            //startActivity(intent);

            @Override
            public void onBubbleClicked(int id) {
                // react to info bubble for area being tapped
            }
        });
        //linearLayout.setVisibility(View.GONE);
    }

    public static int getStationId() {
        return stationId;

    }

    public static boolean getNaviMode() {
        return naviMode;
    }
}

