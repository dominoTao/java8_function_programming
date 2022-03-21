package com.liangmou.functional.ch1.bean;

import lombok.Data;

import java.util.List;
import java.util.stream.Stream;

/**
 * 专辑
 */
@Data
public class Album {
    private String name;
    private List<Track> tracks;
    private List<String> musicians;
}
