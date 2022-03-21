package com.liangmou.functional.ch1.bean;

import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class Artists {
    private List<Artist> artists;

    public Artists(List<Artist> artists) {
        this.artists = artists;
    }

    private void indexException(int index){
        throw new IllegalArgumentException(index+"doesn't correspond to an Artist");
    }

    public Artist getArtist(int index) {
        if (index < 0 || index >= artists.size()) {
            indexException(index);
        }
        return artists.get(index);
    }

    public Optional<Artist> getArtistNull(int index){
        if (index < 0 || index >= artists.size()) {
            Optional.empty();
        }
        return Optional.ofNullable(artists.get(index));
    }

    public String getArtistName(int index) {
        try {
            final Artist artist = getArtist(index);
            return artist.getName();
        } catch (IllegalArgumentException e) {
            return "unknown";
        }
    }

    public String getArtistNameNull(int index) {
        final Optional<Artist> artistNull = getArtistNull(index);
        return artistNull.map(Artist::getName).orElse("unknown");
    }
}
