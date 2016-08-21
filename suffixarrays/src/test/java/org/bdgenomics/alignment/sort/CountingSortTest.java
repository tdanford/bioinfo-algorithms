package org.bdgenomics.alignment.sort;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import org.junit.Test;

public class CountingSortTest {

  @Test
  public void sortIntegers() {
    Integer[] values = new Integer[] { 5, 6, 9, 0, 0, 2, 7 };

    Integer[] copied = values.clone();
    Arrays.sort(copied);

    new CountingSort<Integer>().sort(values, 0, values.length, i -> i, 10);

    assertThat(values).containsExactly(copied);
  }

}
