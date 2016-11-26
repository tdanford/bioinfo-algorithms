package tdanford.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Board {

  public static final byte PIECE_MASK = 0x06 | 0x05 | 0x04 | 0x03 | 0x02 | 0x01;
  public static final byte EMPTY  = 0x00;
  public static final byte KING   = 0x01;
  public static final byte QUEEN  = 0x02;
  public static final byte BISHOP = 0x03;
  public static final byte KNIGHT = 0x04;
  public static final byte ROOK   = 0x05;
  public static final byte PAWN   = 0x06;

  public static byte pieceValue(final byte b) { return (byte)(b & PIECE_MASK); }

  public static final byte WHITE = 0x01 << 6;
  public static final byte BLACK = 0x00;

  public static boolean isWhite(final byte b) { return (b & WHITE) == WHITE; }
  public static boolean isBlack(final byte b) { return (b & WHITE) == BLACK; }

  public static final byte WHITE_KING_MOVED = 0x01;
  public static final byte WHITE_KR_MOVED = 0x01 << 1;
  public static final byte WHITE_QR_MOVED = 0x01 << 2;
  public static final byte BLACK_KING_MOVED = 0x01 << 3;
  public static final byte BLACK_KR_MOVED = 0x01 << 4;
  public static final byte BLACK_QR_MOVED = 0x01 << 5;

  public static final PositionalPieceValues VALUATION = new PositionalPieceValues();

  public byte[] board;
  public byte turn;
  public short pawnMoved;
  public byte castlingFlags;
  public BoardMask whiteAttack, blackAttack;

  public Board() {
    this.board = new byte[64];
    this.turn = WHITE;
    this.pawnMoved = 0;
    this.castlingFlags = 0;
    this.whiteAttack = new BoardMask();
    this.blackAttack = new BoardMask();
  }

  public Board(final Board b) {
    this.board = Arrays.copyOf(b.board, 64);
    this.turn = b.turn;
    this.pawnMoved = b.pawnMoved;
    this.castlingFlags = b.castlingFlags;
    this.whiteAttack = new BoardMask(b.whiteAttack);
    this.blackAttack = new BoardMask(b.blackAttack);
  }

  public void setKingMoved(final boolean color) {
    castlingFlags = (byte)( color ? (castlingFlags | WHITE_KING_MOVED) : (castlingFlags | BLACK_KING_MOVED) );
  }
  public void setKingsRookMoved(final boolean color) {
    castlingFlags = (byte)( color ? (castlingFlags | WHITE_KR_MOVED) : (castlingFlags | BLACK_KR_MOVED) );
  }
  public void setQueensRookMoved(final boolean color) {
    castlingFlags = (byte)( color ? (castlingFlags | WHITE_QR_MOVED) : (castlingFlags | BLACK_QR_MOVED) );
  }

  public boolean isKingMoved(final boolean color) {
    return (color ? (castlingFlags & WHITE_KING_MOVED) : (castlingFlags & BLACK_KING_MOVED)) != 0;
  }
  public boolean isKingsRookMoved(final boolean color) {
    return (color ? (castlingFlags & WHITE_KR_MOVED) : (castlingFlags & BLACK_KR_MOVED)) != 0;
  }
  public boolean isQueensRookMoved(final boolean color) {
    return (color ? (castlingFlags & WHITE_QR_MOVED) : (castlingFlags & BLACK_QR_MOVED)) != 0;
  }

  public void initialize() {
    for(int i = 0; i < board.length; i++) {
      board[i] = EMPTY;
    }

    for(int c = 0; c < 8; c++) {
      board[offset(1, c)] = WHITE | PAWN;
      board[offset(6, c)] = BLACK | PAWN;
    }

    turn = WHITE;
    pawnMoved = -1;
    castlingFlags = 0;

    board[offset(0, 0)] = board[offset(0, 7)] = WHITE | ROOK;
    board[offset(7, 0)] = board[offset(7, 7)] = BLACK | ROOK;

    board[offset(0, 1)] = board[offset(0, 6)] = WHITE | KNIGHT;
    board[offset(7, 1)] = board[offset(7, 6)] = BLACK | KNIGHT;

    board[offset(0, 2)] = board[offset(0, 5)] = WHITE | BISHOP;
    board[offset(7, 2)] = board[offset(7, 5)] = BLACK | BISHOP;

    board[offset(0, 3)] = WHITE | QUEEN;
    board[offset(7, 3)] = BLACK | QUEEN;

    board[offset(0, 4)] = (byte)(WHITE | KING);
    board[offset(7, 4)] = (byte)(BLACK | KING);

    recreateAttackMasks();
  }

  public double score(final boolean color) {
    final double pieceScore = dispatchOnColorPieces(VALUATION, new ScoreAggregator(), color);
    final double moveScore = isWhite(turn) == color ? 0.1 : 0.0;
    return moveScore + pieceScore;
  }

  public double score() {
    return score(true) - score(false);
  }

  public static boolean isValid(final int r, final int c) {
    return r >= 0 && c >= 0 && r < 8 && c < 8;
  }

  public static int offset(final int r, final int c) {
    return r * 8 + c;
  }

  public static int row(final int offset) {
    return offset / 8;
  }

  public static int column(final int offset) {
    return offset % 8;
  }

  private <T> T dispatchOnPiece(final PieceDispatch<T> dispatch, final int r, final int c) {
    final int offset = offset(r, c);
    final byte pieceValue = pieceValue(board[offset]);
    final boolean isWhite = isWhite(board[offset]);

    switch(pieceValue) {
      case PAWN: return dispatch.pawn(isWhite, r, c, this);
      case KING: return dispatch.king(isWhite, r, c, this);
      case QUEEN: return dispatch.queen(isWhite, r, c, this);
      case BISHOP: return dispatch.bishop(isWhite, r, c, this);
      case KNIGHT: return dispatch.knight(isWhite, r, c, this);
      case ROOK: return dispatch.rook(isWhite, r, c, this);
    }

    return null;
  }

  public <T, U> U dispatchOnPieces(final PieceDispatch<T> dispatch,
                                   final Aggregator<T, U> agg) {
    return dispatchOnPieces(dispatch, agg, null);
  }

  public <T, U> U dispatchOnColorPieces(final PieceDispatch<T> dispatch,
                                        final Aggregator<T, U> agg,
                                        final boolean color) {
    return dispatchOnPieces(dispatch, agg, value -> Board.isWhite(value) == color);
  }

  public <T, U> U dispatchOnPieces(final PieceDispatch<T> dispatch,
                                   final Aggregator<T, U> agg,
                                   final Predicate<Byte> valuePredicate) {
    U aggregated = null;
    for(int r = 0; r < 8; r++) {
      for(int c = 0; c < 8; c++) {
        final int offset = Board.offset(r, c);
        if(valuePredicate == null || valuePredicate.test(board[offset])) {
          T value = dispatchOnPiece(dispatch, r, c);
          if (value != null) {
            if (aggregated == null) {
              aggregated = agg.lift(value);
            } else {
              aggregated = agg.aggregate(aggregated, value);
            }
          }
        }
      }
    }
    return aggregated;
  }

  public void recreateAttackMasks() {
    whiteAttack.clearBits();
    blackAttack.clearBits();
    final ListAggregator<Move> aggregator = new ListAggregator<>();
    final List<Move> moves = dispatchOnPieces(new AttackGenerator(), aggregator);

    for(final Move m : moves) {
      if(Board.isWhite(board[m.fromOffset()])) {
        whiteAttack.setBit(m.rTo, m.cTo);
      } else {
        blackAttack.setBit(m.rTo, m.cTo);
      }
    }
  }

  public List<Move> listMoves() {
    return dispatchOnColorPieces(new MoveGenerator(), new ListAggregator<>(), Board.isWhite(turn));
  }

  public boolean isPawn(final int r, final int c, final boolean color) {
    final int offset = offset(r, c);
    return Board.isWhite(board[offset]) == color &&
        Board.pieceValue(board[offset]) == PAWN;
  }

  public boolean isEmpty(final int r, final int c) {
    return board[offset(r, c)] == EMPTY;
  }

  public boolean isAttacked(int r, int c, boolean color) {
    return color ? whiteAttack.isSet(r, c) : blackAttack.isSet(r, c);
  }
}

