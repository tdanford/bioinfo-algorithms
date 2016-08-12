package org.bdgenomics.alignment.suffixarrays;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

/**
 * Utilities for testing equality between objects and sets of objects.
 */
public final class EqualityUtils {

  public static boolean eq(Object[] left, Object[] right) {
    if(left.length != right.length) { return false; }
    for(int i = 0; i < left.length; i++) {
      if(!equalValues(left[i], right[i])) {
        return false;
      }
    }
    return true;
  }

  public static Object[] of(Object... vals) { return vals; }
  public static Object[] to(Object... vals) { return vals; }

  /**
   * Checks deep equality between two objects.
   *
   * (If either object is a Collection or an Array, it checks equality of length and
   * component values.)
   *
   * @param left The first value to test
   * @param right The second value to test
   * @return 'true' if left is equal to right, 'false' otherwise.
   */
  public static boolean equalValues(final Object left, final Object right) {
    if(left == null || right == null) {
      return left == right;
    } else {
      if(left instanceof Collection || right instanceof Collection) {
        if(!(left instanceof Collection) || !(right instanceof Collection)) {
          return false;
        } else {
          final Collection leftc = (Collection) left;
          final Collection rightc = (Collection) right;

          if(leftc.size() != rightc.size()) { return false; }
          final Iterator lefti = leftc.iterator();
          final Iterator righti = rightc.iterator();

          while(lefti.hasNext()) {
            final Object leftv = lefti.next();
            final Object rightv = righti.next();

            if(!equalValues(leftv, rightv)) { return false; }
          }
        }
      } else if(left.getClass().isArray() || right.getClass().isArray()) {
        if(!left.getClass().isArray() || !right.getClass().isArray()) {
          return false;
        }

        if(Array.getLength(left) != Array.getLength(right)) { return false; }

        for(int i = 0; i < Array.getLength(left); i++) {
          if(!equalValues(Array.get(left, i), Array.get(right, i))) {
            return false;
          }
        }

      } else {

        return left.equals(right);
      }
    }

    return true;
  }
}
