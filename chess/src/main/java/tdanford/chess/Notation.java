package tdanford.chess;

public interface Notation {
  String notate(final Board board, final Move move);
}

class SimpleAlgebraic implements Notation {

  public static String pieceNotation(final byte value) {
    switch(Board.pieceValue(value)) {
      case Board.KING: return "K";
      case Board.QUEEN: return "Q";
      case Board.BISHOP: return "B";
      case Board.KNIGHT: return "N";
      case Board.ROOK: return "R";
      case Board.PAWN: return "";
      default: return "?";
    }
  }

  public static String file(final int c) {
    switch(c) {
      case 0: return "a";
      case 1: return "b";
      case 2: return "c";
      case 3: return "d";
      case 4: return "e";
      case 5: return "f";
      case 6: return "g";
      case 7: return "h";
      default: return "?";
    }
  }

  @Override
  public String notate(final Board board, final Move move) {
    if(move.isKingsCastle()) { return "O-O"; }
    if(move.isQueensCastle()) { return "O-O-O"; }

    final byte from = board.board[Board.offset(move.rFrom, move.cFrom)];
    final byte to = board.board[Board.offset(move.rTo, move.cTo)];
    final String toString = String.format("%s%d", file(move.cTo), move.rTo + 1);
    final String modifier = "";
    final String takes = (to == Board.EMPTY) ? "" : "x";
    return String.format("%s%s%s%s", pieceNotation(from), modifier, takes, toString);
  }
}
