package org.bdgenomics.suffixarrays;

public class LCP {

  public final int[] lcp;

  public LCP(SuffixArray array) {
    this.lcp = new int[array.length() + 1];
    computeLCP(array);
  }

  private void computeLCP(SuffixArray array) {
    int[] rank = new int[array.length()+1];
    for(int i = 0; i < rank.length; i++) {
      rank[array.suffix(i)] = i;
    }

    int h = 0;
    lcp[0] = -1;

    for(int i = 0; i < rank.length; i++) {
      if(rank[i] > 0) {
        int j = array.suffix(rank[i] - 1);
        while(i + h < array.length() &&
          j + h < array.length() &&
          array.base(i + h) == array.base(j + h)) {
          h++;
        }

        lcp[rank[i]] = h;
        if(h > 0) {
          h = h - 1;
        }
      }
    }
  }
}
