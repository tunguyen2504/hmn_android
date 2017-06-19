package hanoi.hms.hanoimetronavigator;

/**
 * Created by tungu on 6/18/2017.
 */

public class Navigation {
    String stationStart, stationStartTowards, stationChange, stationChangeTowards, stationArrival;
    String tripTime;
    double tripFare;

    public Navigation (String stationStart, String stationStartTowards, String stationArrival, String tripTime, double tripFare) {
        this.stationStart = stationStart;
        this.stationStartTowards = stationStartTowards;
        this.stationArrival = stationArrival;
        this.tripTime = tripTime;
        this.tripFare = tripFare;
    }

    public Navigation () {

    }

    public void setStationChange(String stationChange) {
        this.stationChange = stationChange;
    }

    public void setStationChangeTowards(String stationChangeTowards) {
        this.stationChangeTowards = stationChangeTowards;
    }
}
