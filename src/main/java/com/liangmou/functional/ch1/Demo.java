package com.liangmou.functional.ch1;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.liangmou.functional.ch1.bean.Album;
import com.liangmou.functional.ch1.bean.Artist;
import com.liangmou.functional.ch1.bean.Track;
import com.sun.javafx.logging.PulseLogger;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RunWith(JUnit4.class)
public class Demo {
    @Test
    @Ignore
    public void func1(){
        final Runnable noArguments = () -> System.out.println("hello world");
        final Thread thread = new Thread(noArguments);
        thread.start();
        final IntBinaryOperator comparator = (x, y) -> x + y;
        final int i = comparator.applyAsInt(1, 2);
        System.out.println(i);

    }
    @Test
    @Ignore
    public void func2(){
//        final HashMap<String, Integer> map = Maps.newHashMap();
        List<Artist> list = Lists.newArrayList();
        final long count = list.stream().filter(t -> t.getCountry().equals("中国")).count();
        System.out.println(count);
    }
    @Test
    @Ignore
    public void func3(){
        final List<Integer> collect = Stream.of(1, 2, 3, 4, 5).collect(Collectors.toList());

        assert collect != null;

        System.out.println(collect);

        final List<String> strUpper = Stream.of("a", "b", "hello").map(String::toUpperCase).peek(System.out::println).collect(Collectors.toList());
    }

    @Test
    @Ignore
    public void func4(){
        // 合并两个流
        final List<Integer> collect = Stream.of(Arrays.asList(1, 2), Arrays.asList(3, 4)).flatMap(t -> t.stream()).peek(System.out::print).collect(Collectors.toList());
        System.out.println();
        final Track track = Arrays.asList(new Track("Bakai", 524),
                new Track("Violets for Your Furs", 378),
                new Track("Time Was", 451)).stream()
                .min(Comparator.comparing(t -> t.getLength())).get();
        System.out.println(track);
    }

    @Test
    @Ignore
    public void reduceDemo(){
        final Integer reduce = Stream.of(1, 2, 3).reduce(0, (t, u) -> t + u);
        System.out.println(reduce);
    }

    private Set<String> findLongTracksOld(List<Album> albums) {
        final HashSet<String> trackNames = Sets.newHashSet();
        for (Album album : albums) {
            for (Track track : album.getTracks()) {
                if (track.getLength() > 60) {
                    final String name = track.getName();
                    trackNames.add(name);
                }
            }
        }
        return trackNames;
    }

    @Test
    @Ignore
    public Set<String> findLongTracks(List<Album> albums) {
        HashSet<String> trackNames = Sets.newHashSet();
        albums.stream()
                .flatMap(album -> album.getTracks().stream())
                .filter(track -> track.getLength() > 60)
                .map(Track::getName)
                .forEach(trackNames::add);

        for (Album album : albums) {
            // 音乐人， 等价于new ArrayList<>(album.getMusicians())
            final List<Artist> musicians = album.getMusicians().stream().collect(Collectors.toList());
            // 乐队
            final List<Artist> bands = musicians.stream().filter(artist -> artist.getName().startsWith("The")).collect(Collectors.toList());
            // 乐队的国籍
            final List<String> origins = bands.stream().map(Artist::getNationality).collect(Collectors.toList());
        }


        return trackNames;
    }

    @Test
    public void printTrackLengthStatistics(Album album) {
        // 摘要统计信息 -> summary Statistics
        final IntSummaryStatistics trackLengthStats = album.getTracks().stream().mapToInt(Track::getLength).summaryStatistics();
        System.out.printf("Max: %d, Min: %d, Ave %f, Sum: %d",
                trackLengthStats.getMax(),
                trackLengthStats.getMin(),
                trackLengthStats.getAverage(),
                trackLengthStats.getSum());
    }

}

