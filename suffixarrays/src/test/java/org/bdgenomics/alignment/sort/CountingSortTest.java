package org.bdgenomics.alignment.sort;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Stopwatch;

public class CountingSortTest {

  public static Logger LOG = LoggerFactory.getLogger(CountingSortTest.class);

  @Test
  public void sortIntegers() {
    Integer[] values = new Integer[] { 5, 6, 9, 0, 0, 2, 7 };

    Integer[] copied = values.clone();
    Arrays.sort(copied);

    new CountingSort<Integer>().sort(values, 0, values.length, i -> i, 10);

    assertThat(values).containsExactly(copied);
  }

  @Test
  public void testTiming() {
    Random rand = new Random(12345L);
    int max = 100;
    Integer[] jarray = randomIntegers(rand, max, 10000000);
    Integer[] carray = jarray.clone();

    Stopwatch javaTime = Stopwatch.createUnstarted();
    Stopwatch countedTime = Stopwatch.createUnstarted();

    javaTime.start();
    Arrays.sort(jarray);
    javaTime.stop();

    countedTime.start();
    new CountingSort<Integer>().sort(carray, 0, carray.length, i -> i, max);
    countedTime.stop();

    LOG.debug(String.format("java   : %.5s", (double)javaTime.elapsed(TimeUnit.MILLISECONDS) / 1000.0));
    LOG.debug(String.format("counted: %.5s", (double)countedTime.elapsed(TimeUnit.MILLISECONDS) / 1000.0));

    assertThat(carray).containsExactly(jarray);
  }

  public Integer[] randomIntegers(Random rand, int max, int N) {
    Integer[] array = new Integer[N];
    for(int i = 0; i < N; i++) {
      array[i] = rand.nextInt(max);
    }

    return array;
  }

}
