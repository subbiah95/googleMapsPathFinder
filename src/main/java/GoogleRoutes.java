import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.*;
import exception.UnExpectedException;
import lombok.Builder;
import lombok.NonNull;
import model.LegDetails;
import model.RouteDetails;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Builder
@NonNull
public class GoogleRoutes {

    private GeoApiContext context;
    private LatLng origin;
    private LatLng destination;

    /**
     * @param directionsSteps - DirectionsStep[]
     * @return The latitude and longitude between Steps of a Leg
     * @see DirectionsStep
     */
    private List<LatLng> getStepPaths(@NonNull DirectionsStep[] directionsSteps){
        List<LatLng> stepPath = new ArrayList<>();

        for( DirectionsStep directionsStep : directionsSteps){
            stepPath.add(directionsStep.startLocation);
            stepPath.addAll(directionsStep.polyline.decodePath());
            stepPath.add(directionsStep.endLocation);
        }

        return stepPath;
    }

    /**
     * @param directionsLegs - DirectionsLeg[]
     * @return The list of latitude and longitude between Legs of particular route
     * @see DirectionsLeg
     */
    private RouteDetails getLegPaths(@NonNull DirectionsLeg[] directionsLegs){

        RouteDetails routeDetail = new RouteDetails();
        for(DirectionsLeg directionsLeg : directionsLegs){

            List<LatLng> legPath = new ArrayList<>();
            legPath.add(directionsLeg.startLocation);
            legPath.addAll(getStepPaths(directionsLeg.steps));
            legPath.add(directionsLeg.endLocation);

            LegDetails legDetails = new LegDetails();
            legDetails.setStepPoints(legPath);
            legDetails.setInfoOnRoute(MessageFormat.format("LatLngs at a constant distance interval of {0} between A({1}) & B({2}) on the road, travel duration : {3}",
                    directionsLeg.distance, directionsLeg.startLocation, directionsLeg.endLocation, directionsLeg.duration));
            routeDetail.getLegDetails().add(legDetails);
        }

       return routeDetail;
    }

    /**
     * @param routes - DirectionsRoute[]
     * @return list of latitude and longitude between Origin and Destination
     * @see DirectionsRoute
     */
    private List<RouteDetails> getRoutePaths(@NonNull DirectionsRoute[] routes) {

        List<RouteDetails> routeDetails = new ArrayList<>();

        for(DirectionsRoute directionsRoute : routes){
            routeDetails.add(getLegPaths(directionsRoute.legs));
        }

        return routeDetails;
    }

    /**
     * @return The array of all possible route between Origin and Destination,
     * route is an array of latitude and longitudes
     * @throws Exception
     * @see LatLng
     * If geocoderStatus is not equal to "OK", the Address was not successfully parsed.
     * But if we provide latitude and longitude, "ZERO_RESULTS" might come up, due to wrong coordinates or impossible route.
     */
    public List<RouteDetails> getPoint() throws Exception{

        DirectionsResult result = DirectionsApi.newRequest(context)
                .origin(origin)
                .destination(destination)
                .await();

        for(GeocodedWaypoint geocodedWaypoint : result.geocodedWaypoints){
            if(geocodedWaypoint.geocoderStatus != GeocodedWaypointStatus.OK)
                throw new UnExpectedException("geocoderStatus not as expected(OK), status : {0} , Address type : {1}", geocodedWaypoint.geocoderStatus, geocodedWaypoint.types );
            if(geocodedWaypoint.partialMatch)
                throw new UnExpectedException("Only partial match attained, status : {0} , Address type : {1}", geocodedWaypoint.geocoderStatus, geocodedWaypoint.types );
        }
        return getRoutePaths(result.routes);
    }
}
