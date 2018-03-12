package Test;

import RoutePlanner;
import DataObjects.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoutePlannerTest {

  // RoutePlanner.main(RoutePlanner.java:50) 0    [main] DEBUG RoutePlanner  - 0 : 1
  // RoutePlanner.main(RoutePlanner.java:50) 2    [main] DEBUG RoutePlanner  - 0 : 2
  // RoutePlanner.main(RoutePlanner.java:50) 2    [main] DEBUG RoutePlanner  - 1 : 2
  // RoutePlanner.main(RoutePlanner.java:50) 2    [main] DEBUG RoutePlanner  - 1 : 3
  // RoutePlanner.main(RoutePlanner.java:50) 2    [main] DEBUG RoutePlanner  - 1 : 4
  // RoutePlanner.main(RoutePlanner.java:50) 2    [main] DEBUG RoutePlanner  - 2 : 4
  // RoutePlanner.main(RoutePlanner.java:50) 3    [main] DEBUG RoutePlanner  - 3 : 4

  // Map = no obstables
  @Test
  public void test1() {
    Map map = new Map(5, 5, new ArryList<Location>());
    RoutePlanner planner = new RoutePlanner(map);
    Location start;
    Location end;
    start = new Location(0, 0);
    end = new Location(3, 4);

    ArrayList<GridPoint> desired = new ArrayList<>(Array.asList(new GridPoint(0, 0, true), new GridPoint(0, 1, true),
        new GridPoint(0, 2, true), new GridPoint(0, 3, true), new GridPoint(0, 4, true), new GridPoint(1, 4, true),
        new GridPoint(2, 4, true), new GridPoint(3, 4, true)));
    ArrayList<GridPoint> result = planner.findRoute(start, end);
    Assertions.assertEquals(desired, result);
  }

  @Test
  public void test2() {
    // obstacles in 0,3 and 3,0
    ArrayList<Location> unavailable = new ArrayList<Location>();
    unavailable.add(new Location(0, 3));
    unavailable.add(new Location(3, 0));
    Map map = new Map(5, 5, unavailable);
    RoutePlanner planner = new RoutePlanner(map);
    Location start = new Location(0, 0);
    Location end = new Location(3, 4);
    ArrayList<GridPoint> desired = new ArrayList<>(Array.asList(new GridPoint(0, 1, true), new GridPoint(0, 2, true),
        new GridPoint(1, 2, true), new GridPoint(1, 3, true), new GridPoint(1, 4, true), new GridPoint(2, 4, true),
        new GridPoint(3, 4, true)));
    ArrayList<GridPoint> result = planner.findRoute(start, end);
    Assertions.assertEquals(desired, result);
  }

  @Test
  public void test3() {

  }

}
