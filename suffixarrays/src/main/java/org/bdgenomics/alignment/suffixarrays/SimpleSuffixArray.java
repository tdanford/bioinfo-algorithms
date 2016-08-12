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

  private boolean matchesAt(String query, int position) {
    int i;
    for(i = 0; i < query.length() && position + i < base.length(); i++) {
      if(query.charAt(i) != base.charAt(position + i)) {
        return false;
      }
    }
    return i >= query.length();
  }

  public Set<Integer> findMatches(String query) {
    System.out.println(String.format("findMatches(%s)", query));
    TreeSet<Integer> matches = new TreeSet<>();
    int[] range = findMatchingRange(query, 0, suffixArray.size()-1);
    if(range != null) {
      System.out.println(String.format("Matching range: %d, %d", range[0], range[1]));
      for(int i = range[0]; i <= range[1]; i++) {
        matches.add(suffixArray.get(i));
      }
      System.out.println("Matches: " + matches);
    }
    return matches;
  }

  private int compare(String query, int index) {
    int baseIndex = suffixArray.get(index);
    int baseLength = base.length() - baseIndex;
    for(int i = 0; i < Math.min(query.length(), baseLength); i++) {
      char qc = query.charAt(i), bc = base.charAt(baseIndex + i);
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
        System.out.println(String.format("%s <%d %s", query, middle,
          base.substring(suffixArray.get(middle)) + "$"));
        top = middle - 1;
      } else if(c == 1) {
        System.out.println(String.format("%s >%d %s", query, middle,
          base.substring(suffixArray.get(middle)) + "$"));
        bottom = middle + 1;
      } else {
        System.out.println(String.format("%s =%d %s", query, middle,
          base.substring(suffixArray.get(middle)) + "$"));
        int[] leftInterval = middle > bottom ? findMatchingRange(query, bottom, middle-1) : null;
        int[] rightInterval = middle < top ? findMatchingRange(query, middle+1, top) : null;

        int left = leftInterval != null ? leftInterval[0] : middle;
        int right = rightInterval != null ? rightInterval[1] : middle;

        System.out.println(String.format("=> [%d, %d]", left, right));

        return new int[] { left, right };
      }
    }

    int pos = suffixArray.get(bottom);
    if(matchesAt(query, pos)) {
      System.out.println(String.format("=> [%d, %d]", bottom, top));
      return new int[] { bottom, top };
    } else {
      String baseSubstring = base.substring(pos, Math.min(pos + query.length(), base.length()));
      System.out.println(String.format("Query %s doesn't match base substring %s (at position %d)", query, baseSubstring, pos));
      return null;
    }
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
