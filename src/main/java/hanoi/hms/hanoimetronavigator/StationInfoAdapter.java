package hanoi.hms.hanoimetronavigator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tungu on 5/28/2017.
 */

public class StationInfoAdapter extends ArrayAdapter<Station> {
    public StationInfoAdapter(Context context, int resource, List<Station> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.item_stationinfo, null);
        }
        Station station = getItem(position);
        if (station != null) {
            //TextView stationCode = (TextView) view.findViewById(R.id.stationCode);
            //TextView stationName = (TextView) view.findViewById(R.id.stationName);
            TextView stationFirstTrain = (TextView) view.findViewById(R.id.firstTrainData);
            TextView stationLastTrain = (TextView) view.findViewById(R.id.lastTrainData);
            TextView stationNextTrain = (TextView) view.findViewById(R.id.nextTrainData);
            TextView desStationData = (TextView) view.findViewById(R.id.desStationData);
            //stationCode.setText(station.code);
            //stationName.setText(station.name);
            stationFirstTrain.setText(station.firstTrain);
            stationLastTrain.setText(station.lastTrain);
            stationNextTrain.setText(station.nextTrain);
            desStationData.setText(station.destStation);
        }
        return view;
    }
}
