package tdanford.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MoveGenerator implements PieceDispatch<List<Move>> {

  private static Logger LOG = LoggerFactory.getLogger(MoveGenerator.class);

  public boolean onBoard(final int r, final int c) {
    return Board.isValid(r, c);
  }

  public boolean isAttacked(final Board board, final int r, final int c, final boolean byColor) {
    return byColor ?
        board.whiteAttack.isSet(r, c) :
        board.blackAttack.isSet(r, c);
  }

  public boolean isOccupied(final Board board, final int r, final int c) {
    return board.board[Board.offset(r, c)] != Board.EMPTY;
  }

  public boolean isOccupied(final Board board, final int r, final int c, final boolean byColor) {
    final int offset = Board.offset(r, c);
    return board.board[offset] != Board.EMPTY &&
        Board.isWhite(board.board[offset]) == byColor;
  }

  public List<Move> scanMoveLine(final List<Move> moves,
                                 final Board board,
                                 final boolean color,
                                 final int r,
                                 final int c,
                                 final int rDiff,
                                 final int cDiff,
                                 final int depth) {
    return scanMoveLine(moves, board, color, r, c, rDiff, cDiff, depth, true, null);
  }


  public  List<Move> scanMoveLine(final List<Move> moves,
                                  final Board board,
                                  final boolean color,
                                  final int r,
                                  final int c,
                                  final int rDiff,
                                  final int cDiff,
                                  final int depth,
                                  final boolean attacksPieces,
                                  final BiPredicate<Integer, Integer> filter) {

    final int offset = Board.offset(r, c);
    for(int i = 1; i <= depth; i++) {
      final int r2 = r + (i * rDiff), c2 = c + (i * cDiff);

      if(!onBoard(r2, c2)) {
        break;
      }

      if(filter == null || filter.test(r2, c2)) {
        if (isOccupied(board, r2, c2)) {
          if (attacksPieces && Board.isWhite(board.board[offset]) != color) {
            moves.add(new Move(board.board[offset], r, c, r2, c2));
          }

          break;
        }

        moves.add(new Move(board.board[offset], r, c, r2, c2));
      }
    }

    return moves;
  }

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

    // TODO: generate castling moves

    LOG.info("{} king ({},{}) moves: {}", isWhite ? "white" : "black", r, c, moves.size());
    return moves;
  }

  @Override
  public List<Move> queen(boolean isWhite, int r, int c, Board board) {
    List<Move> moves = new ArrayList<>();

    moves = scanMoveLine(moves, board, isWhite, r, c, 1, 1, 7);
    moves = scanMoveLine(moves, board, isWhite, r, c, 1, -1, 7);
    moves = scanMoveLine(moves, board, isWhite, r, c, -1, 1, 7);
    moves = scanMoveLine(moves, board, isWhite, r, c, -1, -1, 7);

    moves = scanMoveLine(moves, board, isWhite, r, c, 1, 0, 7);
    moves = scanMoveLine(moves, board, isWhite, r, c, 0, 1, 7);
    moves = scanMoveLine(moves, board, isWhite, r, c, -1, 0, 7);
    moves = scanMoveLine(moves, board, isWhite, r, c, 0, -1, 7);

    LOG.info("{} queen ({},{}) moves: {}", isWhite ? "white" : "black", r, c, moves.size());
    return moves;
  }

  @Override
  public List<Move> bishop(boolean isWhite, int r, int c, Board board) {
    List<Move> moves = new ArrayList<>();

    moves = scanMoveLine(moves, board, isWhite, r, c, 1, 1, 7);
    moves = scanMoveLine(moves, board, isWhite, r, c, 1, -1, 7);
    moves = scanMoveLine(moves, board, isWhite, r, c, -1, 1, 7);
    moves = scanMoveLine(moves, board, isWhite, r, c, -1, -1, 7);

    LOG.info("{} bishop ({},{}) moves: {}", isWhite ? "white" : "black", r, c, moves.size());
    return moves;
  }

  @Override
  public List<Move> knight(boolean isWhite, int r, int c, Board board) {
    List<Move> moves = new ArrayList<>();

    moves = scanMoveLine(moves, board, isWhite, r, c, 2, 1, 1);
    moves = scanMoveLine(moves, board, isWhite, r, c, 2, -1, 1);
    moves = scanMoveLine(moves, board, isWhite, r, c, -2, 1, 1);
    moves = scanMoveLine(moves, board, isWhite, r, c, -2, -1, 1);

    moves = scanMoveLine(moves, board, isWhite, r, c, 1, 2, 1);
    moves = scanMoveLine(moves, board, isWhite, r, c, 1, -2, 1);
    moves = scanMoveLine(moves, board, isWhite, r, c, -1, 2, 1);
    moves = scanMoveLine(moves, board, isWhite, r, c, -1, -2, 1);

    LOG.info("{} knight ({},{}) moves: {}", isWhite ? "white" : "black", r, c, moves.size());
    return moves;
  }

  @Override
  public List<Move> rook(boolean isWhite, int r, int c, Board board) {
    List<Move> moves = new ArrayList<>();

    moves = scanMoveLine(moves, board, isWhite, r, c, 1, 0, 7);
    moves = scanMoveLine(moves, board, isWhite, r, c, 0, 1, 7);
    moves = scanMoveLine(moves, board, isWhite, r, c, -1, 0, 7);
    moves = scanMoveLine(moves, board, isWhite, r, c, 0, -1, 7);

    LOG.info("{} rook ({},{}) moves: {}", isWhite ? "white" : "black", r, c, moves.size());
    return moves;
  }

  @Override
  public List<Move> pawn(boolean isWhite, int r, int c, Board board) {
    List<Move> moves = new ArrayList<>();
    final int offset = Board.offset(r, c);
    final boolean hasMoved = isWhite ? r > 1 : r < 6;
    final int direction = isWhite ? 1 : -1;

    moves = scanMoveLine(moves, board, isWhite, r, c, direction, 0, hasMoved ? 1 : 2, false, null);

    if(onBoard(r + direction, c - 1) && isOccupied(board, r + direction, c - 1, !isWhite)) {
      moves.add(new Move(board.board[offset], r, c, r + direction, c - 1));
    }
    if(onBoard(r + direction, c + 1) && isOccupied(board, r + direction, c + 1, !isWhite)) {
      moves.add(new Move(board.board[offset], r, c, r + direction, c - 1));
    }

    // TODO: en passant

    LOG.info("{} pawn ({},{}) moves: {}", isWhite ? "white" : "black", r, c, moves.size());
    return moves;
  }
}
