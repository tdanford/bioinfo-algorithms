package tdanford.chess;

import java.util.List;

/**
 * Created by tdanford on 11/22/16.
 */
public class Main {

  public static void main(String[] args) {
    BoardRenderer<String> renderer = new SimpleTestBoard();

    Board b = new Board();
    b.initialize();

    System.out.println(renderer.render(b));

    Notation notation = new SimpleAlgebraic();

    System.out.println(String.format("%.2f", b.score()));

    List<Move> moves = b.listMoves();
    System.out.println("# Moves: " + moves.size());
    for(final Move m : moves) {
      System.out.println(notation.notate(b, m));
    }
  }
}
