package com.liangmou.functional.ch1.bean;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
/**
 * 创作音乐的个人或者团队
 */
@Data
public class Artist {
    private String name;

    private String country;

//    private Stream<String> members;

    private String origin;

    private String nationality;

    private List<Artist> members;

    public Artist(String name, String nationality) {
        this(name, Collections.emptyList(), nationality);
    }

    public Artist(String name, List<Artist> members, String nationality) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(members);
        Objects.requireNonNull(nationality);
        this.name = name;
        this.members = Lists.newArrayList(members);
        this.nationality = nationality;
    }
    public boolean isSolo() {
        return members.isEmpty();
    }
    public boolean isFrom(String nationality) {
        return this.nationality.equals(nationality);
    }
    public Stream<Artist> getMembers() {
        return members.stream();
    }
    @Override
    public String toString() {
        return getName();
    }

    public Artist copy() {
        List<Artist> members = getMembers().map(Artist::copy).collect(toList());
        return new Artist(name, members, nationality);
    }
}
