package hanoi.hms.hanoimetronavigator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by tungu on 5/27/2017.
 */

public class Station extends AppCompatActivity {
    public String firstTrain, lastTrain, nextTrain, destStation;
    ArrayList<Station> stations = new ArrayList<Station>();
    ListView ls;
    String urlStation_ArrivalTime, urlStation_Name;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        ls = (ListView) findViewById(R.id.listView);
    }

    public Station () {

    }

    public Station (String firstTrain, String lastTrain, String nextTrain, String destStation) {
        this.firstTrain = firstTrain;
        this.lastTrain = lastTrain;
        this.nextTrain = nextTrain;
        this.destStation = destStation;
    }

}
