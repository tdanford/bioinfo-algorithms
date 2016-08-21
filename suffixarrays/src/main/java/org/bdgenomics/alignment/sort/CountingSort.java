package org.bdgenomics.alignment.sort;

import static java.util.stream.Collectors.toList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountingSort<T> {

  public static Logger LOG = LoggerFactory.getLogger(CountingSort.class);

  public CountingSort() {}

  public void sort(final T[] values,
                   final int start, final int end,
                   final Function<T, Integer> keyFunc,
                   final int numKeys) {

    LOG.debug("Input: {}", Arrays.asList(values));

    Integer[] counts = new Integer[numKeys];
    for(int i = 0; i < counts.length; i++) {
      counts[i] = 0;
    }

    for(int i = start; i < end; i++) {
      counts[keyFunc.apply(values[i])] += 1;
    }

    LOG.debug("Counts : {}", Arrays.asList(counts));

    int sum = 0;
    for(int i = 0; i < counts.length; i++) {
      final int tmp = counts[i];
      counts[i] = sum;
      sum += tmp;
    }

    LOG.debug("Summed : {}", Arrays.asList(counts));

    for(int i = end-1; i >= start; i--) {
      T val = values[i];
      int key = keyFunc.apply(val);
      int j = counts[key];

      if(j < i) {
        do {
          counts[key]++;

          T tmp = val;
          val = values[j];
          values[j] = tmp;

          key = keyFunc.apply(val);
          j = counts[key];
        } while(j < i);

        values[i] = val;
      }
    }

    LOG.debug("Final: {}", Arrays.asList(values));
  }

  private void swap(final T[] values, final int i1, final int i2) {
    T temp = values[i1];
    values[i1] = values[i2];
    values[i2] = temp;
  }
}
