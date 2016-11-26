package tdanford.chess;

import static java.util.stream.Collectors.toList;
import java.util.*;

public class MoveTree {

  public static final Notation notation = new SimpleAlgebraic();

  public final MoveTree parent;
  public final Move move;    // the (last) move
  public final Board board;  // the board that results from that move
  public final int moveCount;
  public double score;

  public List<MoveTree> children;  // possible (next) moves

  public MoveTree() {
    this(null, null);
  }

  public MoveTree(final MoveTree parent, final Move move) {
    this.parent = parent;
    this.move = move;
    this.board = parent == null ? Board.initial() : move.makeMove(parent.board);
    this.moveCount = parent == null ? 1 : parent.moveCount + (parent.isWhiteMove() ? 0 : 1);
    this.children = null;
    this.score = 0.0;
  }

  public void expandChildren() {
    if(children == null) {
      children = board.listMoves().stream().map(m -> new MoveTree(this, m)).collect(toList());
    } else {
      children.forEach(MoveTree::expandChildren);
    }
  }

  public void expandByDepth(final int depth) {
    if(depth > 0) {
      if (children == null) {
        children = board.listMoves().stream().map(m -> new MoveTree(this, m)).collect(toList());
        children.forEach(child -> child.expandByDepth(depth - 1));
      } else {
        children.forEach(child -> child.expandByDepth(depth));
      }
    }
  }

  public static final double CUTOFF = 6.0;

  public double evaluate() {
    if(children == null || children.isEmpty()) {
      score = board.score();
    } else {

      MoveTree bestChild = null;
      Iterator<MoveTree> childItr = children.iterator();

      if(isWhiteMove()) {
        while (childItr.hasNext()) {
          MoveTree child = childItr.next();
          child.evaluate();
          if (bestChild == null || child.score > bestChild.score) {
            bestChild = child;
          } else if (child.score < bestChild.score - CUTOFF) {
            childItr.remove();
          }
        }
        children.sort(Comparator.comparing((MoveTree t) -> t.score).reversed());
      } else {
        while (childItr.hasNext()) {
          MoveTree child = childItr.next();
          child.evaluate();
          if (bestChild == null || child.score < bestChild.score) {
            bestChild = child;
          } else if (child.score > bestChild.score + CUTOFF) {
            childItr.remove();
          }
        }
        children.sort(Comparator.comparing((MoveTree t) -> t.score));
      }

      score = bestChild != null ? bestChild.score : board.score();
    }

    return score;
  }

  public String history() {
    if(move == null) {
      return "";
    }

    final String history = parent.history();
    final String prefix = isWhiteMove() ? "" : String.format("%d. ", moveCount);
    final String moveString = notation.notate(parent.board, move);
    return String.format("%s %s%s", history, prefix, moveString);
  }

  public void printHistories() {
    if(children != null) {
      children.forEach(MoveTree::printHistories);
    } else {
      System.out.println(String.format("%s %.2f", history(), score));
    }
  }

  public void printMoveTree(final int topN) { printMoveTree(0, topN); }

  public void printMoveTree(final int indent, final int topN) {
    if(move == null) {
      if(children != null) {
        children.forEach(child -> child.printMoveTree(indent));
      }
    } else {
      for (int i = 0; i < indent; i++) {
        System.out.print("  ");
      }
      final String prefix = String.format("%d. %s", moveCount, move.isWhiteMove() ? "" : "...");
      final String moveString = notation.notate(parent.board, move);
      final String scoreString = String.format("%.2f", score);
      System.out.println(String.format("%s %s %s", prefix, moveString, scoreString));
      if(children != null) {
        children.stream().limit((long)topN).forEach(child -> child.printMoveTree(indent + 1, topN));
      }
    }
  }

  public boolean isWhiteMove() {
    return Board.isWhite(board.turn);
  }
}
