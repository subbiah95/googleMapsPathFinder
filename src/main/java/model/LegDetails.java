package model;

import com.google.maps.model.LatLng;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LegDetails {
    List<LatLng> stepPoints;
    String infoOnRoute;
}
