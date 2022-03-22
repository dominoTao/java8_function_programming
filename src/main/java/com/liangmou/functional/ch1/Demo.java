package com.liangmou.functional.ch1;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.liangmou.functional.ch1.bean.Album;
import com.liangmou.functional.ch1.bean.Artist;
import com.liangmou.functional.ch1.bean.Track;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

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
            final List<Artist> musicians = album.getMusicians().collect(Collectors.toList());
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
    @Ignore
    public Optional<Artist> biggestGroup(Stream<Artist> artits) {
        final Function<Artist, Long> getCount = artist -> artist.getMembers().count();;
        return artits.collect(maxBy(comparing(getCount)));
    }

    @Ignore
    public void toCollectionTreeset() {
        Stream<Integer> stream = Stream.of(1, 2, 3);
        // BEGIN TO_COLLECTION_TREESET
        stream.collect(toCollection(TreeSet::new));
        // END TO_COLLECTION_TREESET
    }
    @Ignore
    public double averageNumberOfTracks(List<Album> albums){
        final Double collect = albums.stream().collect(averagingInt(album -> album.getTracks().size()));
        return collect;
    }
//    https://github.com/RichardWarburton/java-8-lambdas-exercises/blob/master/src/main/java/com/insightfullogic/java8/examples/chapter5/CollectorExamples.java

    /**
     * 数据分块
     * @param artists
     * @return
     */
    public Map<Boolean, List<Artist>> bandsAndSolo(Stream<Artist> artists) {
        return artists.collect(partitioningBy(Artist::isSolo));
    }

    /**
     * 数据分组
     * @param albums
     * @return
     */
    public Map<Artist, List<Album>> albumsByArtist(Stream<Album> albums) {
        return albums.collect(groupingBy(album -> album.getMainMusician()));
    }
}

