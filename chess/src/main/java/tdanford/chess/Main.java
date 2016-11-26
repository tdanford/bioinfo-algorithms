package tdanford.chess;

public class Main {

  public static void main(String[] args) {
    int depth = Integer.parseInt(args[0]);
    BoardRenderer<String> renderer = new SimpleTestBoard();

    Board b = new Board();
    b.initialize();

    System.out.println(renderer.render(b));

    MoveTree tree = new MoveTree();

    for(int i = 0; i < depth; i++) {
      tree.expandByDepth(2);
      tree.evaluate();
      tree.printHistories();
    }

    //tree.printMoveTree(0, 3);
  }
}
