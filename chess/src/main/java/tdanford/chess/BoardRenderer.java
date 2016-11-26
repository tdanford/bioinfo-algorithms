package tdanford.chess;

public interface BoardRenderer<T> {

  T render(final Board b);
}

class SimpleTestBoard implements BoardRenderer<String> {

  @Override
  public String render(Board b) {
    StringBuilder sb = new StringBuilder();

    for(int r = 7; r >= 0; r--) {
      for(int c = 0; c < 8; c++) {
        final int offset = Board.offset(r, c);
        final byte value = b.board[offset];

        String square = b.board[offset] == Board.EMPTY ?
            "." : SimpleAlgebraic.pieceNotation(b.board[offset]);

        if(Board.pieceValue(value) == Board.PAWN) {
          square = "p";
        }

        if (Board.isWhite(value)) {
          square = square.toUpperCase();
        } else {
          square = square.toLowerCase();
        }

        sb.append(square);
      }

      sb.append("\n");
    }

    return sb.toString();
  }
}
