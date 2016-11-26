package tdanford.chess;

import static tdanford.chess.Board.*;
import java.util.function.Function;

public interface PieceValues extends Function<Byte,Double> {
}

class BasicPieceValues implements PieceValues {

  @Override
  public Double apply(Byte piece) {
    switch(Board.pieceValue(piece)) {
      case PAWN: return 1.0;
      case KING: return 9999.0;
      case QUEEN: return 9.0;
      case BISHOP: return 3.0;
      case KNIGHT: return 3.0;
      case ROOK: return 5.0;
    }

    return 0.0;
  }
}

