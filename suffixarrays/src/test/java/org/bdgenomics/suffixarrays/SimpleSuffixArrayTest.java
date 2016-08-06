package org.bdgenomics.suffixarrays;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Random;
import java.util.Set;
import org.junit.Test;

public class SimpleSuffixArrayTest {

  public static void printSuffixes(SimpleSuffixArray array) {
    int rank = 0;
    for(int i : array.suffixArray) {
      String suffix = array.base.substring(i) + "$";
      System.out.println(String.format("#% 3d: (@% 4d) %s", rank++, i, suffix));
    }
  }

  public static String asBlocks(String original, int blockLength) {
    StringBuilder sb = new StringBuilder();
    int i = 0;
    while(i < original.length()) {
      if(i + blockLength < original.length()) {
        sb.append(original.substring(i, i + blockLength));
        sb.append(" ");
        i += blockLength;
      } else {
        sb.append(original.substring(i, original.length()));
        i = original.length();
      }
    }
    return sb.toString();
  }

  private void testNoMatch(String alphabet, String base, String query) {
    SimpleSuffixArray array = new SimpleSuffixArray(base, alphabet);

    printSuffixes(array);

    assertThat(array.findMatches(query))
      .as(String.format("Query string \"%s\" should have no matches in suffix array", query))
      .isEmpty();

    assertThat(base.indexOf(query))
      .as(String.format("Query string \"%s\" shouldn't match base at any position", query))
      .isEqualTo(-1);
  }

  private void testMatch(String alphabet, String base, String query, int position) {
    SimpleSuffixArray array = new SimpleSuffixArray(base, alphabet);

    printSuffixes(array);

    assertThat(array.findMatches(query))
      .as(String.format("Query string \"%s\" doesn't have match position %d in suffix array", query, position))
      .contains(position);

    String baseSubstring = base.substring(position, position + query.length());

    assertThat(query)
      .as(String.format("Query string \"%s\" doesn't match base substring \"%s\" at position %d", query, baseSubstring, position))
      .isEqualTo(baseSubstring);
  }

  @Test
  public void testMatchRandom1() {
    testMatch("ACGT", "GTATCTCTGAACGTAGTTACGTTTTGATTTTCGCCAGGAGCGTTCAGGGT", "AGCG", 38);
    testNoMatch("ACGT", "GTATCTCTGAACGTAGTTACGTTTTGATTTTCGCCAGGAGCGTTCAGGGT", "ACTA");
  }

  @Test
  public void testSimpleSuffixArray() {
    String base = "abeacadabea";
    String alphabet = "abcde";
    SimpleSuffixArray array = new SimpleSuffixArray(base, alphabet);
    assertThat(array.suffixArray)
      .containsOnly(11, 10, 7, 0, 3, 5, 8, 1, 4, 6, 9, 2);

    assertThat(array.findMatches("bea")).containsOnly(8, 1);
    assertThat(array.findMatches("acad")).containsOnly(3);
    assertThat(array.findMatches("a")).containsOnly(0, 3, 5, 7, 10);
  }

  @Test
  public void testRandomStrings() {
    Random rand = new Random(12345L);
    int N = 1000;
    int templateLength = 100;
    int queryLength = 3;
    int numQueries = 20;
    for(int i = 0; i < N; i++) {
      testRandomString(rand, templateLength, queryLength, numQueries);
    }
  }

  public void testRandomString(Random rand, int templateLength, int queryLength, int numQueries) {
    String alphabet = "ACGT";
    String base = randomString(rand, alphabet, templateLength);
    SimpleSuffixArray array = new SimpleSuffixArray(base, alphabet);

    for(int i = 0; i < numQueries; i++) {
      String query = randomString(rand, alphabet, queryLength);
      Set<Integer> matches = array.findMatches(query);

      for(Integer m : matches) {
        assertThat(query)
          .as(String.format("Query \"%s\" matches base \"%s\" at position %d", query, asBlocks(base, 10), m))
          .isEqualTo(base.substring(m, m + query.length()));
      }

      int mi = -1;
      while((mi = base.indexOf(query, mi + 1)) != -1) {
        assertThat(matches)
          .as(String.format("Base \"%s\" matches query \"%s\" at position %d", asBlocks(base, 10), query, mi))
          .contains(mi);
      }
    }
  }


  public static String randomString(Random rand, String alphabet, int length) {
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < length; i++) {
      int k = rand.nextInt(alphabet.length());
      assertThat(k).isGreaterThanOrEqualTo(0);
      assertThat(k).isLessThan(alphabet.length());
      sb.append(alphabet.charAt(k));
    }
    return sb.toString();
  }


}
