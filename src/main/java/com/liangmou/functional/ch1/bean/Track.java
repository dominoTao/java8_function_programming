package com.liangmou.functional.ch1.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 专辑中的一支曲目
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Track {
    private String name;
    private Integer length;

    public Track copy() {
        return new Track(name, length);
    }
}
