import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements  PathingStrategy{



    public List<Point> computePath(Point start, Point end,
                            Predicate<Point> canPassThrough,
                            BiPredicate<Point, Point> withinReach,
                            Function<Point, Stream<Point>> potentialNeighbors){

        List<Point> closedSet= new ArrayList<>();
        List<Point> openSet= new ArrayList<>();

        HashMap<Point,Integer> gScoreMap = new HashMap<>();
        HashMap<Point,Integer> fScoreMap = new HashMap<>();
        HashMap<Point,Point> cameFrom = new HashMap<>();

        gScoreMap.put(start,0);
        fScoreMap.put(start,hScore(start,end));

        openSet.add(start);

        //while (openSet.size()!=0){
        while (!openSet.isEmpty()){
            //Point current = findSmallestF(openSet,start,end);
            Point current = lowestFkey(fScoreMap,openSet);

            //System.out.println("END: " + end.x + ", " + end.y);
            //System.out.println("CURRENT: " +current.x+", " + current.y);
            if (withinReach.test(current,end)){
                //System.out.println("build");
                return reconstruct_path(cameFrom,current);
            }

            openSet.remove(current);
            closedSet.add(current);

            List<Point> neighbors = potentialNeighbors.apply(current)
                    .filter(canPassThrough)
                    .filter(pt ->
                            !pt.equals(start)
                                    && !pt.equals(end)
                                    && Math.abs(end.x - pt.x) <= Math.abs(end.x - start.x)
                                    && Math.abs(end.y - pt.y) <= Math.abs(end.y - start.y))
                    .collect(Collectors.toList());


            for (Point neighbor: neighbors){
                if (closedSet.contains(neighbor)){continue;}

                int tentative_gScore= gScoreMap.get(current) + gScore(current,neighbor);

                //if neighbor not in openSet ( Discover a new node )
                if (!openSet.contains(neighbor)){
                    openSet.add(neighbor);
                }
                else if (tentative_gScore >= gScoreMap.get(neighbor)){
                    continue;
                }

                cameFrom.put(neighbor, current);
                gScoreMap.put(neighbor, tentative_gScore);
                fScoreMap.put(neighbor, gScoreMap.get(neighbor)+ hScore(neighbor,end));
            }
        }
        //System.out.println("huh?");
        List<Point>temp = new ArrayList<>();
        return temp;
    }

    private Point findSmallestF(List<Point> set, Point start, Point end){
        Point min = set.get(0);
        for (Point p : set){
            int fCost = gScore(start,p) + hScore(p,end);
            if (fCost < gScore(start,min) + hScore(min,end)){
                min=p;
            }
        }
        return min;
    }

//    private Point findSmallestF1(HashMap<Point,Integer> map){
//        Point minKey=null;
//        int minValue=Integer.MAX_VALUE;
//        for (Point p:map.keySet()){
//            int value = map.get(p);
//            if (value < minValue){
//                minValue = value;
//                minKey = p;
//            }
//        }
//        return minKey;
//    }

    private Point lowestFkey(HashMap<Point, Integer> fScore, List<Point> openNodes)
    {
        Point lowKey = openNodes.get(0);
        Integer lowValue = fScore.get(lowKey);

        for(Point p : openNodes)
        {
            Integer newValue = fScore.get(p);

            if(newValue < lowValue){
                lowKey = p;
                lowValue = newValue;
            }
        }

        return lowKey;
    }

    private List<Point> reconstruct_path(HashMap<Point,Point> cameFrom, Point current){
        List<Point> total_path = new ArrayList<>();
        total_path.add(current);
        while (cameFrom.containsKey(current)){
            current = cameFrom.get(current);
            total_path.add(0,current);
        }
        return total_path;
    }

    //calculates distance from start (I know I have 2 of same method just diff name. Just so I don't get confused)
    private int gScore(Point start, Point f)
    {
        return Math.abs(start.x - f.x) + Math.abs(start.y - f.y);
    }


    //calculates distance from end
    private int hScore(Point start, Point end){
        return Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
    }
}
