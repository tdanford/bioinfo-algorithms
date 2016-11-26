package tdanford.chess;

import java.util.List;

class ListAggregator<T> implements Aggregator<List<T>, List<T>> {

  @Override
  public List<T> lift(final List<T> value) {
    return value;
  }

  @Override
  public List<T> aggregate(final List<T> prior, final List<T> value) {
    prior.addAll(value);
    return prior;
  }
}
