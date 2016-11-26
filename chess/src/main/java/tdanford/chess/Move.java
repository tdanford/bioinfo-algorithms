package tdanford.chess;

import java.util.Objects;

class Move {

  public final int rFrom, cFrom, rTo, cTo;
  public final byte piece;

  public Move(final byte piece,
              final int rFrom, final int cFrom,
              final int rTo, final int cTo) {
    this(piece, (byte)rFrom, (byte)cFrom, (byte)rTo, (byte)cTo);
  }

  public Move(final byte piece,
              final byte rFrom, final byte cFrom,
              final byte rTo, final byte cTo) {
    this.rFrom = rFrom;
    this.cFrom = cFrom;
    this.rTo = rTo;
    this.cTo = cTo;
    this.piece = piece;
  }

  public boolean isKingsCastle() {
    return Board.pieceValue(piece) == Board.KING &&
        cTo == cFrom + 2;
  }

  public boolean isQueensCastle() {
    return Board.pieceValue(piece) == Board.KING &&
        cTo == cFrom - 2;
  }

  public boolean isEnPassant() {
    return Board.pieceValue(piece) == Board.PAWN &&
        cFrom != cTo;
  }

  public boolean isPawnDoubleMove() {
    return Board.pieceValue(piece) == Board.PAWN &&
        Math.abs(rFrom - rTo) > 1;
  }

  /**
   * Applies the move to a Board, to generate the new Board in which that move
   * has been made.
   *
   * @param from
   * @return
   */
  public Board makeMove(final Board from) {
    final int fromOffset = Board.offset(rFrom, cFrom);
    final int toOffset = Board.offset(rTo, cTo);

    if(fromOffset < 0) {
      throw new IllegalArgumentException(String.format("%d,%d", rFrom, cFrom));
    }
    if(toOffset < 0) {
      throw new IllegalArgumentException(String.format("%d,%d", rTo, cTo));
    }

    final Board to = new Board(from);

    to.board[toOffset] = from.board[fromOffset];
    to.board[fromOffset] = Board.EMPTY;

    if(isPawnDoubleMove()) {
      to.pawnMoved = (short)cFrom;
    } else {
      to.pawnMoved = -1;
    }

    if(isEnPassant()) {
      if(Board.isWhite(piece)) {
        to.board[Board.offset(rTo - 1, cTo)] = Board.EMPTY;
      } else {
        to.board[Board.offset(rTo + 1, cTo)] = Board.EMPTY;
      }

    } else if(isKingsCastle()) {
      final int rookFromOffset = Board.offset(rFrom, 7), rookToOffset = Board.offset(rFrom, cTo - 1);
      to.board[rookToOffset] = from.board[rookFromOffset];
      to.board[rookFromOffset] = Board.EMPTY;

    } else if(isQueensCastle()) {
      final int rookFromOffset = Board.offset(rFrom, 0), rookToOffset = Board.offset(rFrom, cTo + 1);
      to.board[rookToOffset] = from.board[rookFromOffset];
      to.board[rookFromOffset] = Board.EMPTY;

    }

    to.turn = Board.isWhite(to.turn) ? Board.BLACK : Board.WHITE;
    to.recreateAttackMasks();

    return to;
  }

  public String toString() { return String.format("(%x) %d,%d -> %d,%d", piece, rFrom, cFrom, rTo, cTo); }

  public int hashCode() {
    return Objects.hash(rFrom, rTo, cFrom, cTo, piece);
  }

  public boolean equals(Object o) {
    if(!(o instanceof Move)) { return false; }
    Move m = (Move)o;
    return rFrom == m.rFrom &&
        rTo == m.rTo &&
        cFrom == m.cFrom &&
        cTo == m.cTo &&
        piece == m.piece;
  }

  public int fromOffset() {
    return Board.offset(rFrom, cFrom);
  }

  public int toOffset() {
    return Board.offset(rTo, cTo);
  }

  public boolean isWhiteMove() {
    return Board.isWhite(piece);
  }
}
