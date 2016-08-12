package org.bdgenomics.suffixarrays;

public interface SuffixArray {

  int length();
  int suffix(int offset);
  char base(int offset);
  String baseString(int start, int end);
  String alphabet();
}
