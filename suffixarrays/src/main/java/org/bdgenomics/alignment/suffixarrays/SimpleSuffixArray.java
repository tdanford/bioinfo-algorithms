package org.bdgenomics.alignment.suffixarrays;

import java.util.*;
import java.util.stream.IntStream;
import com.google.common.base.Preconditions;

public class SimpleSuffixArray implements SuffixArray {

  public final String base;
  public final ArrayList<Integer> suffixArray;
  public final String alphabet;

  private final Map<Character, Integer> alphabetOrder;

  public SimpleSuffixArray(String base, String alphabet) {
    Preconditions.checkNotNull(base);
    Preconditions.checkNotNull(alphabet);

    this.base = base;
    this.alphabet = alphabet;

    this.alphabetOrder = new TreeMap<>();
    for(int i = 0; i < alphabet.length(); i++) {
      char c = alphabet.charAt(i);
      if(alphabetOrder.containsKey(c)) { throw new IllegalArgumentException(); }
      alphabetOrder.put(c, i);
    }

    this.suffixArray = sortSuffixes(0, IntStream.range(0, base.length()+1));
  }

  public int length() { return base.length(); }
  public int suffix(int offset) { return suffixArray.get(offset); }
  public char base(int offset) { return base.charAt(offset); }
  public String alphabet() { return alphabet; }

  public String baseString(int start, int end) { return base.substring(start, end); }

  public String suffixString(int start) {
    //Preconditions.checkArgument(start < base.length(), String.format("start %d >= base.length()=%d", start, base.length()));
    return base.substring(start, base.length());
  }

  private ArrayList<Integer> sortSuffixes(int prefix, IntStream indices) {

    Map<Character, List<Integer>> subsets = new TreeMap<>();

    ArrayList<Integer> sorted = new ArrayList<>();

    indices.forEach( index -> {
        if (index + prefix >= base.length()) {
          sorted.add(index);
        } else {
          char c = base.charAt(index + prefix);
          if (!subsets.containsKey(c)) {
            subsets.put(c, new LinkedList<>());
          }
          subsets.get(c).add(index);
        }
      });

    for(Character c : alphabet.toCharArray()) {
      if(subsets.containsKey(c)) {
        sorted.addAll(sortSuffixes(prefix + 1, subsets.get(c).stream().mapToInt(i -> i)));
      }
      subsets.remove(c);
    }

    return sorted;
  }
}
