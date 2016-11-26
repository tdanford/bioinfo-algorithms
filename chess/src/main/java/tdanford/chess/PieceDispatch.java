package tdanford.chess;

import java.util.function.Function;

/**
 * Created by tdanford on 11/24/16.
 */
interface PieceDispatch<T> {
  T king(boolean isWhite, int r, int c, Board board);
  T queen(boolean isWhite, int r, int c, Board board);
  T bishop(boolean isWhite, int r, int c, Board board);
  T knight(boolean isWhite, int r, int c, Board board);
  T rook(boolean isWhite, int r, int c, Board board);
  T pawn(boolean isWhite, int r, int c, Board board);
}

abstract class UniformDispatch<T> implements PieceDispatch<T> {

  public UniformDispatch() {
  }

  abstract T dispatch(final boolean isWhite, final int r, final int c, final Board board);

  @Override
  public T king(boolean isWhite, int r, int c, Board board) {
    return dispatch(isWhite, r, c, board);
  }

  @Override
  public T queen(boolean isWhite, int r, int c, Board board) {
    return dispatch(isWhite, r, c, board);
  }

  @Override
  public T bishop(boolean isWhite, int r, int c, Board board) {
    return dispatch(isWhite, r, c, board);
  }

  @Override
  public T knight(boolean isWhite, int r, int c, Board board) {
    return dispatch(isWhite, r, c, board);
  }

  @Override
  public T rook(boolean isWhite, int r, int c, Board board) {
    return dispatch(isWhite, r, c, board);
  }

  @Override
  public T pawn(boolean isWhite, int r, int c, Board board) {
    return dispatch(isWhite, r, c, board);
  }
}

class ValueDispatch<T> extends UniformDispatch<T> {
  private final Function<Byte, T> valuation;

  public ValueDispatch(final Function<Byte, T> f) {
    this.valuation = f;
  }

  @Override
  T dispatch(boolean isWhite, int r, int c, Board board) {
    return valuation.apply(board.board[Board.offset(r, c)]);
  }
}
