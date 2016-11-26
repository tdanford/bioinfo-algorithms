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

  abstract T dispatch(final byte value, final boolean isWhite, final int r, final int c, final Board board);

  @Override
  public T king(boolean isWhite, int r, int c, Board board) {
    return dispatch(board.board[Board.offset(r, c)], isWhite, r, c, board);
  }

  @Override
  public T queen(boolean isWhite, int r, int c, Board board) {
    return dispatch(board.board[Board.offset(r, c)], isWhite, r, c, board);
  }

  @Override
  public T bishop(boolean isWhite, int r, int c, Board board) {
    return dispatch(board.board[Board.offset(r, c)], isWhite, r, c, board);
  }

  @Override
  public T knight(boolean isWhite, int r, int c, Board board) {
    return dispatch(board.board[Board.offset(r, c)], isWhite, r, c, board);
  }

  @Override
  public T rook(boolean isWhite, int r, int c, Board board) {
    return dispatch(board.board[Board.offset(r, c)], isWhite, r, c, board);
  }

  @Override
  public T pawn(boolean isWhite, int r, int c, Board board) {
    return dispatch(board.board[Board.offset(r, c)], isWhite, r, c, board);
  }
}

