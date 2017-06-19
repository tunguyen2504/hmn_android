package hanoi.hms.hanoimetronavigator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tungu on 6/18/2017.
 */

public class NavigationAdapter extends ArrayAdapter<Navigation> {
    public NavigationAdapter(Context context, int resource, List<Navigation> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.item_navi, null);
        }
        Navigation navi = getItem(position);
        if (navi != null) {
            TextView stationStart = (TextView) view.findViewById(R.id.stationStart);
            TextView stationStartTowards = (TextView) view.findViewById(R.id.stationStartTowards);
            TextView stationChange = (TextView) view.findViewById(R.id.stationChange);
            TextView stationChangeTowards = (TextView) view.findViewById(R.id.stationChangeTowards);
            TextView stationArrival = (TextView) view.findViewById(R.id.stationArrival);
            TextView tripTime = (TextView) view.findViewById(R.id.tripTime);
            TextView tripFare = (TextView) view.findViewById(R.id.tripFare);
            stationStart.setText("Start at ".concat(navi.stationStart));
            stationStartTowards.setText("Towards ".concat(navi.stationStartTowards));
            /*if (!navi.stationChange.isEmpty() && !navi.stationChangeTowards.isEmpty()) {
                //stationChange.setText("Change at ".concat(navi.stationChange));
                //stationChangeTowards.setText("Towards ".concat(navi.stationChangeTowards));
            } else {
                stationChange.setVisibility(GONE);
                stationChangeTowards.setVisibility(GONE);
            }*/
            stationArrival.setText("Arrive at ".concat(navi.stationArrival));
            tripTime.setText(navi.tripTime);
            tripFare.setText(String.valueOf(navi.tripFare*1000).concat("đ"));
        }
        return view;
    }
}
