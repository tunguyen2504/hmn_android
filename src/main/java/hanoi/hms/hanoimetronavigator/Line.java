package hanoi.hms.hanoimetronavigator;

/**
 * Created by tungu on 5/27/2017.
 */

public class Line {
    double length;
    String code, name, totalTime, startTime, endTime, frequency;

    public Line (String code, String name, double length, String totalTime, String startTime, String endTime, String frequency) {
        this.code = code;
        this.name = name;
        this.length = length;
        this.totalTime = totalTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.frequency = frequency;
    }

}