package tdanford.maps;

public class SiteTriple extends Tuple<Point> implements Comparable<SiteTriple> {

    public SiteTriple(final Point s1, final Point s2, final Point s3) {
        super(s1, s2, s3);
    }

    @Override
    public int compareTo(SiteTriple o) {
        for(int i = 0; i < array.length; i++) {
            int c = array[i].compareTo(o.array[i]);
            if(c != 0) { return c; }
        }
        return 0;
    }
}
