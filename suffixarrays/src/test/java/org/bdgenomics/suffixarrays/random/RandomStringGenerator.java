package org.bdgenomics.suffixarrays.random;

import java.util.Random;
import com.google.common.base.Preconditions;

public class RandomStringGenerator {

  private final Random rand;
  private final String alphabet;

  public RandomStringGenerator(Random rand, String alphabet) {
    this.rand = rand;
    this.alphabet = alphabet;
  }

  public String nextString(int length) {
    Preconditions.checkArgument(length >= 0);

    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < length; i++) {
      sb.append(alphabet.charAt(rand.nextInt(alphabet.length())));
    }
    return sb.toString();
  }

  public String randomSubstring(String base, int length) {
    Preconditions.checkArgument(length <= base.length());
    if(length == base.length()) { return base; }
    final int offset = rand.nextInt(base.length()-length+1);
    return base.substring(offset, offset+length);
  }
}
