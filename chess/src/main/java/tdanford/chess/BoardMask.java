package tdanford.chess;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by tdanford on 11/24/16.
 */
class BoardMask {

  public byte[] mask;

  public BoardMask() { mask = new byte[8]; }

  public BoardMask(final BoardMask copy) {
    mask = Arrays.copyOf(copy.mask, 8);
  }

  public void setBits(final Stream<Integer> offsets) {
    offsets.forEach(i -> setBit(i / 8, i % 8));
  }

  public void setBit(final int r, final int c) {
    final byte byteMask = (byte)(0x01 << c);
    mask[r] |= byteMask;
  }

  public void clearBit(final int r, final int c) {
    final byte byteMask = (byte)(0x01 << c);
    mask[r] &= (~byteMask);
  }

  public boolean isSet(final int r, final int c) {
    final byte byteMask = (byte)(0x01 << c);
    return (mask[r] & byteMask) != 0;
  }

  public void clearBits() {
    for(int i = 0; i < mask.length; i++) {
      mask[i] = 0;
    }
  }
}
