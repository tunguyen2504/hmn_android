package hanoi.hms.hanoimetronavigator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tungu on 5/27/2017.
 */

public class LineInfoAdapter extends ArrayAdapter<Line> {
    public LineInfoAdapter(Context context, int resource, List<Line> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.item_lineinfo, null);
        }
        Line line = getItem(position);
        if (line != null) {
            TextView lineCode = (TextView) view.findViewById(R.id.lineCode);
            TextView lineName = (TextView) view.findViewById(R.id.lineName);
            TextView lineLength = (TextView) view.findViewById(R.id.lineLengthData);
            TextView lineTotalTime = (TextView) view.findViewById(R.id.lineTotalTimeData);
            TextView lineStartTime = (TextView) view.findViewById(R.id.lineStartTime);
            TextView lineEndTime = (TextView) view.findViewById(R.id.lineEndTime);
            TextView lineFrequency = (TextView) view.findViewById(R.id.lineFreqTime);
            lineCode.setText(line.code);
            lineName.setText(line.name);
            lineLength.setText(String.valueOf(line.length).concat(" km"));
            lineTotalTime.setText(line.totalTime);
            lineStartTime.setText(line.startTime);
            lineEndTime.setText(line.endTime);
            lineFrequency.setText(line.frequency);
        }
        return view;
    }
}
