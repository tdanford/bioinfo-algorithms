package tdanford.chess;

interface Aggregator<T, U> {
  U lift(T value);
  U aggregate(U prior, T value);
}

class ScoreAggregator implements Aggregator<Double, Double> {

  @Override
  public Double lift(Double value) {
    return value;
  }

  @Override
  public Double aggregate(Double prior, Double value) {
    return prior + value;
  }
}
