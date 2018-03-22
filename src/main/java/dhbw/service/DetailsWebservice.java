package dhbw.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dhbw.pojo.detail.album.DetailsAlbum;
import dhbw.pojo.detail.artist.DetailsArtist;
import dhbw.pojo.detail.track.DetailsTrack;
import dhbw.pojo.result.detail.DetailResult;
import dhbw.spotify.RequestCategory;
import dhbw.spotify.RequestType;
import dhbw.spotify.SpotifyRequest;
import dhbw.spotify.WrongRequestTypeException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
public class DetailsWebservice {

    @GetMapping("/detail/{id}")
    public Object getDetails(@PathVariable String id, @RequestParam String type) {
        SpotifyRequest spotifyRequest = new SpotifyRequest(RequestType.DETAIL);
        RequestCategory category = RequestCategory.valueOf(type);
        ObjectMapper mapper = new ObjectMapper();
        Optional<String> opt = null;
        try {
            opt = spotifyRequest.performeRequestDetail(category, id);


            if(category == RequestCategory.TRACK) {
                DetailsTrack detailsTrack = mapper.readValue(opt.get(), DetailsTrack.class);
                DetailResult detailResponse = new DetailResult();
                detailResponse.setInfo(detailsTrack.getType());
                detailResponse.setTitle(detailsTrack.getName());
                return detailResponse;
            }

            if(category == RequestCategory.ALBUM) {
                DetailsAlbum detailsTrack = mapper.readValue(opt.get(), DetailsAlbum.class);
                DetailResult detailResponse = new DetailResult();
                detailResponse.setInfo(detailsTrack.getType());
                detailResponse.setTitle(detailsTrack.getName());
                return detailResponse;
            }

            if(category == RequestCategory.ARTIST) {
                DetailsArtist detailsTrack = mapper.readValue(opt.get(), DetailsArtist.class);
                DetailResult detailResponse = new DetailResult();
                detailResponse.setInfo(detailsTrack.getType());
                detailResponse.setTitle(detailsTrack.getName());
                return detailResponse;
            }

        } catch (WrongRequestTypeException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return opt.get();
    }
}
