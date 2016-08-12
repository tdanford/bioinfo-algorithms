package org.bdgenomics.suffixarrays;

import java.lang.reflect.Array;

public final class HashUtils {

  public static int hash(Object... vals) {
    return hashValue(vals);
  }

  public static int hashValue(final Object o) {
    int code = 17;
    if(o != null) {
      if(o instanceof Iterable) {
        Iterable i = (Iterable) o;
        for(Object v : i) {
          code = hashAdd(code, hashValue(v));
        }

      } else if (o.getClass().isArray()) {
        final int length = Array.getLength(o);
        for(int i = 0; i < length; i++) {
          code = hashAdd(code, hashValue(Array.get(o, i)));
        }

      } else {
        code = o.hashCode();
      }
    }

    return code;
  }

  public static int hashAdd(final int left, final int right) {
    return 37 * ( left + right );
  }
}
