package dhbw.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dhbw.pojo.result.search.SearchResult;
import dhbw.pojo.result.search.SearchResultList;
import dhbw.pojo.search.album.SearchAlbum;
import dhbw.pojo.search.artist.SearchArtist;
import dhbw.pojo.search.track.Item;
import dhbw.pojo.search.track.SearchTrack;
import dhbw.pojo.search.track.Tracks;
import dhbw.spotify.RequestCategory;
import dhbw.spotify.RequestType;
import dhbw.spotify.SpotifyRequest;
import dhbw.spotify.WrongRequestTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class SearchWebservice {


    @GetMapping("/search")
    public Object search(@RequestParam String query, @RequestParam String type) {
        RequestCategory type1 = RequestCategory.valueOf(type);
        SpotifyRequest spotifyRequest = new SpotifyRequest(RequestType.SEARCH);
        ObjectMapper mapper = new ObjectMapper();
        Optional<String> opt =  null;

        try {
            opt = spotifyRequest.performeRequestSearch(type1, query);
            if(opt.isPresent()) {
                if(type1 == RequestCategory.TRACK) {
                    SearchTrack track = mapper.readValue(opt.get(), SearchTrack.class);

                    SearchResult response = new SearchResult();
                    response.setResults(new ArrayList<>());

                    for(Item item : track.getTracks().getItems()) {
                        SearchResultList si = new SearchResultList();
                        si.setId(item.getId());
                        si.setPlayLink(item.getUri());
                        si.setTitle(item.getName());
                        si.setDescription(item.getType());
                        response.getResults().add(si);
                    }


                    return response;
                }
                else if(type1 == RequestCategory.ALBUM) {
                    SearchAlbum album = mapper.readValue(opt.get(), SearchAlbum.class);

                    SearchResult response = new SearchResult();
                    response.setResults(new ArrayList<>());

                    for(dhbw.pojo.search.album.Item item : album.getAlbums().getItems()) {
                        SearchResultList si = new SearchResultList();
                        si.setId(item.getId());
                        si.setPlayLink(item.getUri());
                        si.setTitle(item.getName());
                        si.setDescription(item.getType());
                        response.getResults().add(si);
                    }

                    return response;
                }
                else if(type1 == RequestCategory.ARTIST) {
                    SearchArtist album = mapper.readValue(opt.get(), SearchArtist.class);

                    SearchResult response = new SearchResult();
                    response.setResults(new ArrayList<>());

                    for(dhbw.pojo.search.artist.Item item : album.getArtists().getItems()) {
                        SearchResultList si = new SearchResultList();
                        si.setId(item.getId());
                        si.setPlayLink(item.getUri());
                        si.setTitle(item.getName());
                        si.setDescription(item.getType());
                        response.getResults().add(si);
                    }

                    return response;
                }
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
        return new SearchResult();
        //return new ResponseEntity<>(new String("test"), HttpStatus.OK);
    }


}
