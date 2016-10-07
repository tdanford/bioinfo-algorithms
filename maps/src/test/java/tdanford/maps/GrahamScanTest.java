package tdanford.maps;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.junit.Test;

public class GrahamScanTest {

    public static Point point(final int x, final int y) { return new Point(x, y); }

    @Test
    public void testGrahamScan() {
        Point[] ps = new Point[] {
            point(3, -2),
            point(5, 1),
            point(7, 4),
            point(6, 5),
            point(4, 2),
            point(3, 3),
            point(3, 5),
            point(2, 5),
            point(0, 5),
            point(0, 1),
            point(-3, 4),
            point(-2, 2),
            point(0, 0),
            point(-3, 2),
            point(-5, 2),
            point(-5, 1),
            point(-5, -1),
            point(1, -2),
            point(-3, -2)
        };

        final List<Point> lst = Arrays.asList(ps);

        Random rand = new Random(12346L);
        Collections.shuffle(lst, rand);

        Sites s = new Sites(() -> lst);
        GrahamScan scan = new GrahamScan(s);

        s.regenerate();
        scan.regenerate();

        assertThat(scan).isNotNull();
    }
}
