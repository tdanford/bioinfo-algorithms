package tdanford.chess;

import static tdanford.chess.Board.*;
import java.util.function.Function;

public interface PieceValues {

  double apply(final byte value, final boolean color, final int r, final int c, final Board b);
}

class BasicPieceValues extends UniformDispatch<Double> implements PieceValues {

  @Override
  public double apply(byte value, boolean color, int r, int c, Board b) {
    switch(Board.pieceValue(value)) {
      case PAWN: return 1.0;
      case KING: return 9999.0;
      case QUEEN: return 9.0;
      case BISHOP: return 3.0;
      case KNIGHT: return 3.0;
      case ROOK: return 5.0;
    }

    return 0.0;
  }

  @Override
  Double dispatch(byte value, boolean isWhite, int r, int c, Board board) {
    return apply(value, isWhite, r, c, board);
  }
}

class PositionalPieceValues extends UniformDispatch<Double> implements PieceValues {

  private BasicPieceValues basic = new BasicPieceValues();

  @Override
  public double apply(byte value, boolean color, int r, int c, Board b) {
    final double pieceValue = basic.apply(value, color, r, c, b);
    final double centrality = -0.05 * (Math.abs(r - 3.5) + Math.abs(c - 3.5));
    return pieceValue + centrality;
  }

  @Override
  Double dispatch(byte value, boolean isWhite, int r, int c, Board board) {
    return apply(value, isWhite, r, c, board);
  }
}

