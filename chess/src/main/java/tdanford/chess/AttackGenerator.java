package tdanford.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

class AttackGenerator extends MoveGenerator {

  @Override
  public List<Move> king(final boolean isWhite, final int r, final int c, final Board board) {
    List<Move> moves = new ArrayList<>();

    final BiPredicate<Integer, Integer> attack = (r2, c2) -> isAttacked(board, r2, c2, !isWhite);

    moves = scanMoveLine(moves, board, isWhite, r, c, 1, 1, 1, true, attack);
    moves = scanMoveLine(moves, board, isWhite, r, c, 1, -1, 1, true, attack);
    moves = scanMoveLine(moves, board, isWhite, r, c, -1, 1, 1, true, attack);
    moves = scanMoveLine(moves, board, isWhite, r, c, -1, -1, 1, true, attack);

    moves = scanMoveLine(moves, board, isWhite, r, c, 1, 0, 1, true, attack);
    moves = scanMoveLine(moves, board, isWhite, r, c, 0, 1, 1, true, attack);
    moves = scanMoveLine(moves, board, isWhite, r, c, -1, 0, 1, true, attack);
    moves = scanMoveLine(moves, board, isWhite, r, c, 0, -1, 1, true, attack);

    // no castling.

    return moves;
  }

  @Override
  public List<Move> pawn(boolean isWhite, int r, int c, Board board) {
    List<Move> moves = new ArrayList<>();
    final int offset = Board.offset(r, c);
    final int direction = isWhite ? 1 : -1;

    if(onBoard(r + direction, c - 1) && isOccupied(board, r + direction, c - 1, !isWhite)) {
      moves.add(new Move(board.board[offset], r, c, r + direction, c - 1));
    }
    if(onBoard(r + direction, c + 1) && isOccupied(board, r + direction, c + 1, !isWhite)) {
      moves.add(new Move(board.board[offset], r, c, r + direction, c - 1));
    }

    // We do *not* generate the en passant moves as 'attacks', since those squares aren't
    // attacked by any reasonable definition of the board, except for the immediately passed
    // pawn...

    return moves;
  }
}
