package org.bdgenomics.suffixarrays;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

/**
 * Searches a suffix array for a query string.
 *
 * Implements the binary search on a suffix array.
 */
public class Searcher {

  public final String targetName;
  private final SuffixArray array;
  private final Map<Character, Integer> alphabetOrder;

  public Searcher(final String targetName, SuffixArray array) {
    this.targetName = targetName;
    this.array = array;
    this.alphabetOrder = new TreeMap<>();

    String alphabet = array.alphabet();
    for(int i = 0; i < alphabet.length(); i++) {
      alphabetOrder.put(alphabet.charAt(i), i);
    }
  }

  public Stream<Hit> search(String query) {
    System.out.println(String.format("findMatches(%s)", query));
    TreeSet<Integer> matches = new TreeSet<>();
    int[] range = findMatchingRange(query, 0, array.length() - 1);
    if(range != null) {
      System.out.println(String.format("Matching range: %d, %d", range[0], range[1]));
      for(int i = range[0]; i <= range[1]; i++) {
        matches.add(array.suffix(i));
      }
      System.out.println("Matches: " + matches);
    }

    return matches.stream().map(i -> new Hit(query, targetName, i));
  }

  private int compare(String query, int index) {
    int baseIndex = array.suffix(index);
    int baseLength = array.length() - baseIndex;
    for(int i = 0; i < Math.min(query.length(), baseLength); i++) {
      char qc = query.charAt(i), bc = array.base(baseIndex + i);
      int qo = alphabetOrder.get(qc), bo = alphabetOrder.get(bc);

      if(qo < bo) { return -1; }
      if(qo > bo) { return 1; }
    }

    if(query.length() > baseLength) {
      return 1;
      //} else if(query.length() < baseLength) {
      //return -1;
    } else {
      return 0;
    }
  }

  private int[] findMatchingRange(String query, int bottom, int top) {
    System.out.println(String.format("findMatchingRange(%d, %d)", bottom, top));
    if(compare(query, bottom) == -1) {
      System.out.println("query is to the left of the range");
      return null;
    }
    if(compare(query, top) == 1) {
      System.out.println("query is to the right of the range");
      return null;
    }

    while(top - bottom >= 1) {
      System.out.println(String.format("[%d, %d]", bottom, top));
      int middle = Math.round((bottom + top) / 2);

      int c = compare(query, middle);
      if(c == -1) {
        System.out.println(String.format("%s <%d %s", query, middle, array.baseString(array.suffix(middle), array.length()) + "$"));
        top = middle - 1;
      } else if(c == 1) {
        System.out.println(String.format("%s >%d %s", query, middle, array.baseString(array.suffix(middle), array.length()) + "$"));
        bottom = middle + 1;
      } else {
        System.out.println(String.format("%s =%d %s", query, middle, array.baseString(array.suffix(middle), array.length()) + "$"));
        int[] leftInterval = middle > bottom ? findMatchingRange(query, bottom, middle-1) : null;
        int[] rightInterval = middle < top ? findMatchingRange(query, middle+1, top) : null;

        int left = leftInterval != null ? leftInterval[0] : middle;
        int right = rightInterval != null ? rightInterval[1] : middle;

        System.out.println(String.format("=> [%d, %d]", left, right));

        return new int[] { left, right };
      }
    }

    int pos = array.suffix(bottom);
    if(matchesAt(query, pos)) {
      System.out.println(String.format("=> [%d, %d]", bottom, top));
      return new int[] { bottom, top };
    } else {
      String baseSubstring = array.baseString(pos, Math.min(pos + query.length(), array.length()));
      System.out.println(String.format("Query %s doesn't match base substring %s (at position %d)", query, baseSubstring, pos));
      return null;
    }
  }

  private boolean matchesAt(String query, int position) {
    int i;
    for(i = 0; i < query.length() && position + i < array.length(); i++) {
      if(query.charAt(i) != array.base(position + i)) {
        return false;
      }
    }
    return i >= query.length();
  }

}
