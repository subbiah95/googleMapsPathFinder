import com.google.maps.GeoApiContext;
import com.google.maps.model.*;
import model.LegDetails;
import model.RouteDetails;

public class RoutePlotter {

    private static final String apiKey = "YOUR_API_KEY";

    public static void main(String[] args) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
        try{
            GoogleRoutes googleRoutes = new GoogleRoutes.GoogleRoutesBuilder()
                    .context(context)
                    .origin(new LatLng(12.9917286,80.0850574))
                    .destination(new LatLng(13.0016952,79.9776599))
                    .build();

            for( RouteDetails routeDetail : googleRoutes.getPoint()){
                for (LegDetails legDetails : routeDetail.getLegDetails()){
                    System.out.println(legDetails.getInfoOnRoute());
                    System.out.println(legDetails.getStepPoints());
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        context.shutdown();
    }
}
