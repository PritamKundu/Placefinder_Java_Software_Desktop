
package map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import models.Location;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
class DetailsResponse {
    public Location result;
}

/**
 *
 * @author fayshaluddin
 */
public class MapView extends JFXPanel {

    private WebEngine engine = null;
    private WebView webView = null;

    public void loadMap(String mapLocation) {
        Platform.runLater(() -> {
            webView = new WebView();
            engine = webView.getEngine();
            engine.setJavaScriptEnabled(true);
            setScene(new Scene(webView));
            File f = new File(getClass().getClassLoader().getResource(mapLocation).getFile());
            engine.load(f.toURI().toString());
        });

    }

    public void centerMap(ArrayList<Location> locations, String zoomLevel){
        int centerIndex = (int) (locations.size() / 2);
        var centerData = locations.get(centerIndex);

         Platform.runLater(() -> {
            engine.executeScript("setCenter(" + centerData.geometry.location.lat + "," + centerData.geometry.location.lng + "," + zoomLevel + ")");
        });

    }
    public void addMarkersToMap(ArrayList<Location> locations) {
        this.centerMap(locations, "12");

        for (Location loc : locations) {
              this.addMarker(loc);
        }
    }

    public void addMarker(Location loc) {
        Location location = this.getLocationDetails(loc.place_id);
        ObjectMapper mapper = new ObjectMapper();

        try {
            System.out.println(mapper.writeValueAsString(location));
            var locationData = mapper.writeValueAsString(location);
            var coords = mapper.writeValueAsString(location.geometry.location);
            var openingHours = mapper.writeValueAsString(location.opening_hours);
            String reviews = mapper.writeValueAsString(location.reviews);

            Platform.runLater(() -> {
                engine.executeScript("addMarker(" + locationData + "," + coords + ","+ openingHours + "," + reviews +")");
            });
        } catch (JsonProcessingException ex) {
            System.out.println("Exception occured. " + ex.getMessage());
        }
    }

    public void removeAllMarkers() {
        Platform.runLater(() -> {
            engine.executeScript("deleteMarkers()");
        });
    }

    private Location getLocationDetails(String placeId) {
        String fields = "place_id,name,formatted_address,formatted_phone_number,geometry,international_phone_number,rating,reviews,url,website,opening_hours,rating,user_ratings_total,business_status";
        Location result = new Location();
        try {
            var client = HttpClient.newHttpClient();

            var request = HttpRequest.newBuilder(
                    URI.create("https://maps.googleapis.com/maps/api/place/details/json?key=API_KEY&place_id=" + placeId + "&fields=" + fields))
                .header("accept", "application/json")
                .build();

            var api = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

            var res = api.body();
            ObjectMapper mapper = new ObjectMapper();
            DetailsResponse response = mapper.readValue(res, DetailsResponse.class);
            result = response.result;
        } catch (Exception ex) {
            System.out.println("Exception occured. " + ex.getMessage());
        }

        return result;
    }
}
