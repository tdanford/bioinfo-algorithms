package org.bdgenomics.suffixarrays;

import static org.bdgenomics.suffixarrays.EqualityUtils.*;
import static org.bdgenomics.suffixarrays.HashUtils.hash;
import com.google.common.base.Preconditions;

/**
 * Represets a match of a query string against a target.
 */
public class Hit {

  public final String targetName;
  public final String query;
  public final Integer offset;

  public Hit(final String query, final String targetName, final Integer offset) {
    Preconditions.checkNotNull(query);
    Preconditions.checkArgument((targetName == null && offset == null) || (targetName != null && offset != null));
    Preconditions.checkArgument(targetName == null || targetName.length() > 0);
    Preconditions.checkArgument(offset >= 0);

    this.targetName = targetName;
    this.query = query;
    this.offset = offset;
  }

  public int hashCode() {
    return hash(targetName, query, offset);
  }

  public boolean equals(Object o) {
    if(!(o instanceof Hit)) { return false; }
    Hit h = (Hit)o;
    return eq(of(query, targetName, offset), to(h.query, h.targetName, h.offset));
  }

  public String toString() { return String.format("%s:%d:%s", targetName, offset, query); }
}
