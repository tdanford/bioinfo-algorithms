package org.bdgenomics.suffixarrays;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class LCPTest {

  @Test
  public void testSampleLCP() {
    String base = "abeacadabea";
    String alphabet = "abcde";
    SimpleSuffixArray array = new SimpleSuffixArray(base, alphabet);
    LCP lcp = new LCP(array);

    assertThat(lcp.lcp).containsExactly(-1, 0, 1, 4, 1, 1, 0, 3, 0, 0, 0, 2);
  }


}
