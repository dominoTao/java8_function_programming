package com.liangmou.functional.ch1.bean;

import lombok.Data;
/**
 * 创作音乐的个人或者团队
 */
@Data
public class Artist {
    private String name;

    private String country;

    private String members;

    private String origin;
}
