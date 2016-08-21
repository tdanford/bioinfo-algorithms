package org.bdgenomics.alignment.suffixarrays;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Preconditions;

/**
 * Searches a suffix array for a query string.
 *
 * Implements the binary search on a suffix array.
 */
public class Searcher {

  private static Logger LOG = LoggerFactory.getLogger(Searcher.class);

  public final String targetName;
  private final SuffixArray array;
  private final Map<Character, Integer> alphabetOrder;

  /**
   * Prepare a series of searches against a single SuffixArray.
   *
   * @param targetName The name of the String indexed by the SuffixArray -- this is returned
   *                   inside each Hit object.
   * @param array The SuffixArray which indexes the reference string to search.
   */
  public Searcher(final String targetName, final SuffixArray array) {
    this.targetName = targetName;
    this.array = array;
    this.alphabetOrder = new TreeMap<>();

    String alphabet = array.alphabet();
    for(int i = 0; i < alphabet.length(); i++) {
      alphabetOrder.put(alphabet.charAt(i), i);
    }
  }

  /**
   * Primary entry point. For a given query, return a Stream of Hit objects that represent matches
   * of that query against the string whose suffix array was specified in teh constructor.
   *
   * @param query The string to use as the template for hits
   * @return A stream of Hit objects.
   */
  public Stream<Hit> search(String query) {
    LOG.debug("findMatches({})", query);
    TreeSet<Integer> matches = new TreeSet<>();
    int[] range = findMatchingRange(query, 0, array.length());
    if(range != null) {
      LOG.debug("Matching range: {}, {}", range[0], range[1]);

      for(int i = range[0]; i <= range[1]; i++) {
        matches.add(array.suffix(i));
      }
      LOG.debug("Matches: {}", matches);
    }

    return matches.stream().map(i -> new Hit(query, targetName, i));
  }

  private int compare(String query, int index) {
    int baseIndex = array.suffix(index);
    //Preconditions.checkElementIndex(baseIndex, array.length());

    int baseLength = array.length() - baseIndex;
    for(int i = 0; i < Math.min(query.length(), baseLength); i++) {
      char qc = query.charAt(i), bc = array.base(baseIndex + i);
      int qo = alphabetOrder.get(qc), bo = alphabetOrder.get(bc);

      if(qo < bo) { return -1; }
      if(qo > bo) { return 1; }
    }

    if(query.length() > baseLength) {
      LOG.debug("Query length ({}) exceeds baseLength ({}), compare==1", query, array.suffixString(baseIndex));
      return 1;
      //} else if(query.length() < baseLength) {
      //return -1;
    } else {
      return 0;
    }
  }

  private int[] findMatchingRange(String query, int bottom, int top) {
    LOG.debug("findMatchingRange({}, {})", bottom, top);
    if(compare(query, bottom) == -1) {
      LOG.debug("query is to the left of the range");
      return null;
    }
    if(compare(query, top) == 1) {
      LOG.debug("query is to the right of the range ({}->{}, \"{}\")", top, array.suffix(top), array.suffixString(array.suffix(top)));
      return null;
    }

    while(top - bottom >= 1) {
      LOG.debug("[{} -> {}, {} -> {}]", bottom, array.suffix(bottom), top, array.suffix(top));
      int middle = Math.round((bottom + top) / 2);

      int c = compare(query, middle);
      if(c == -1) {
        LOG.debug("{} <{}->{} {}", query, middle, array.suffix(middle), array.baseString(array.suffix(middle), array.length()) + "$");
        top = middle - 1;
      } else if(c == 1) {
        LOG.debug("{} >{}->{} {}", query, middle, array.suffix(middle), array.baseString(array.suffix(middle), array.length()) + "$");
        bottom = middle + 1;
      } else {
        LOG.debug("{} ={}->{} {}", query, middle, array.suffix(middle), array.baseString(array.suffix(middle), array.length()) + "$");
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
      LOG.debug("=> [{}->{}, {}->{}]", bottom, array.suffix(bottom), top, array.suffix(top));
      return new int[] { bottom, top };
    } else {
      String baseSubstring = array.baseString(pos, Math.min(pos + query.length(), array.length()));
      LOG.debug("Query {} doesn't match base substring {} (at position {})", query, baseSubstring, pos);
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
