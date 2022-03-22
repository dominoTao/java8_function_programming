package com.liangmou.functional.ch1.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

/**
 * 专辑
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Album {
    private String name;
    private List<Track> tracks;
    private List<Artist> musicians;

    public List<Artist> getMusicianList() {
        return unmodifiableList(musicians);
    }
    public Stream<Artist> getMusicians() {
        return musicians.stream();
    }
    public Artist getMainMusician() {
        return musicians.get(0);
    }
    public List<Track> getTrackList() {
        return unmodifiableList(tracks);
    }

    public Album copy() {
        List<Track> tracks = getTracks().stream().map(Track::copy).collect(toList());
        List<Artist> musicians = getMusicians().map(Artist::copy).collect(toList());
        return new Album(name, tracks, musicians);
    }
}
