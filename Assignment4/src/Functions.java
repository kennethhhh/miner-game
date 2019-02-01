import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import processing.core.PImage;
import processing.core.PApplet;

final class Functions
{
   public static final String QUAKE_ID = "quake";
   public static final int QUAKE_ACTION_PERIOD = 1100;
   public static final int QUAKE_ANIMATION_PERIOD = 100;


   public static final int PROPERTY_KEY = 0;

   public static final String BGND_KEY = "background";
   public static final int BGND_NUM_PROPERTIES = 4;
   public static final int BGND_ID = 1;
   public static final int BGND_COL = 2;
   public static final int BGND_ROW = 3;

   public static final String MINER_KEY = "miner";
   public static final int MINER_NUM_PROPERTIES = 7;
   public static final int MINER_ID = 1;
   public static final int MINER_COL = 2;
   public static final int MINER_ROW = 3;
   public static final int MINER_LIMIT = 4;
   public static final int MINER_ACTION_PERIOD = 5;
   public static final int MINER_ANIMATION_PERIOD = 6;

   public static final String OBSTACLE_KEY = "obstacle";
   public static final int OBSTACLE_NUM_PROPERTIES = 4;
   public static final int OBSTACLE_ID = 1;
   public static final int OBSTACLE_COL = 2;
   public static final int OBSTACLE_ROW = 3;

   public static final String ORE_KEY = "ore";
   public static final int ORE_NUM_PROPERTIES = 5;
   public static final int ORE_ID = 1;
   public static final int ORE_COL = 2;
   public static final int ORE_ROW = 3;
   public static final int ORE_ACTION_PERIOD = 4;

   public static final String SMITH_KEY = "blacksmith";
   public static final int SMITH_NUM_PROPERTIES = 4;
   public static final int SMITH_ID = 1;
   public static final int SMITH_COL = 2;
   public static final int SMITH_ROW = 3;

   public static final String VEIN_KEY = "vein";
   public static final int VEIN_NUM_PROPERTIES = 5;
   public static final int VEIN_ID = 1;
   public static final int VEIN_COL = 2;
   public static final int VEIN_ROW = 3;
   public static final int VEIN_ACTION_PERIOD = 4;


   public static boolean adjacent(Point p1, Point p2)
   {
      return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) ||
         (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
   }

   public static void load(Scanner in, WorldModel world, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), world, imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                  lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
               lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
               lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   public static boolean processLine(String line, WorldModel world,
      ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
         case BGND_KEY:
            return parseBackground(properties, world, imageStore);
         case MINER_KEY:
            return parseMiner(properties, world, imageStore);
         case OBSTACLE_KEY:
            return parseObstacle(properties, world, imageStore);
         case ORE_KEY:
            return parseOre(properties, world, imageStore);
         case SMITH_KEY:
            return parseSmith(properties, world, imageStore);
         case VEIN_KEY:
            return parseVein(properties, world, imageStore);
         }
      }

      return false;
   }

   public static boolean parseBackground(String [] properties,
      WorldModel world, ImageStore imageStore)
   {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
            Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         world.setBackground(pt,
            new Background(id, imageStore.getImageList(id)));

      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   public static boolean parseMiner(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == MINER_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[MINER_COL]),
            Integer.parseInt(properties[MINER_ROW]));
//         Entity entity = MinerNotFull.createMinerNotFull(properties[MINER_ID],
//            Integer.parseInt(properties[MINER_LIMIT]),
//            pt,
//            Integer.parseInt(properties[MINER_ACTION_PERIOD]),
//            Integer.parseInt(properties[MINER_ANIMATION_PERIOD]),
//            imageStore.getImageList(MINER_KEY));
         MinerNotFull entity = new MinerNotFull(properties[MINER_ID],pt,imageStore.getImageList(MINER_KEY),
              Integer.parseInt(properties[MINER_LIMIT]),0,
                 Integer.parseInt(properties[MINER_ACTION_PERIOD]),
                 Integer.parseInt(properties[MINER_ANIMATION_PERIOD]));

         world.tryAddEntity(entity);
      }

      return properties.length == MINER_NUM_PROPERTIES;
   }

   public static boolean parseObstacle(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
            Integer.parseInt(properties[OBSTACLE_COL]),
            Integer.parseInt(properties[OBSTACLE_ROW]));
//         Entity entity = Obstacle.createObstacle(properties[OBSTACLE_ID],
//            pt, imageStore.getImageList(OBSTACLE_KEY));
         Obstacle entity= new Obstacle(properties[OBSTACLE_ID],
                 pt, imageStore.getImageList(OBSTACLE_KEY),0,0,0,0);

         world.tryAddEntity(entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   public static boolean parseOre(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == ORE_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[ORE_COL]),
            Integer.parseInt(properties[ORE_ROW]));
//         Entity entity = Ore.createOre(properties[ORE_ID],
//            pt, Integer.parseInt(properties[ORE_ACTION_PERIOD]),
//            imageStore.getImageList(ORE_KEY));
         Ore entity = new Ore(properties[ORE_ID],
                 pt, imageStore.getImageList(ORE_KEY),0,0,0,Integer.parseInt(properties[ORE_ACTION_PERIOD])
                 );

         world.tryAddEntity(entity);
      }

      return properties.length == ORE_NUM_PROPERTIES;
   }

   public static boolean parseSmith(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == SMITH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[SMITH_COL]),
            Integer.parseInt(properties[SMITH_ROW]));
//         Entity entity = Blacksmith.createBlacksmith(properties[SMITH_ID],
//            pt, imageStore.getImageList(SMITH_KEY));
         Blacksmith entity= new Blacksmith(properties[SMITH_ID],pt,imageStore.getImageList(SMITH_KEY),0,0,
                 0,0);
         world.tryAddEntity(entity);
      }

      return properties.length == SMITH_NUM_PROPERTIES;
   }

   public static boolean parseVein(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == VEIN_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[VEIN_COL]),
            Integer.parseInt(properties[VEIN_ROW]));
//         Entity entity = Vein.createVein(properties[VEIN_ID],
//            pt,
//            Integer.parseInt(properties[VEIN_ACTION_PERIOD]),
//            imageStore.getImageList(VEIN_KEY));
         Vein entity = new Vein(properties[VEIN_ID],pt,imageStore.getImageList(VEIN_KEY),0,0,
                 Integer.parseInt(properties[VEIN_ACTION_PERIOD]),0);
         world.tryAddEntity(entity);
      }

      return properties.length == VEIN_NUM_PROPERTIES;
   }


   public static int distanceSquared(Point p1, Point p2)
   {
      int deltaX = p1.x - p2.x;
      int deltaY = p1.y - p2.y;

      return deltaX * deltaX + deltaY * deltaY;
   }


//   public static Entity createBlacksmith(String id, Point position,
//      List<PImage> images)
//   {
//      return new Entity(EntityKind.BLACKSMITH, id, position, images,
//         0, 0, 0, 0);
//   }

//   public static Entity createMinerFull(String id, int resourceLimit,
//      Point position, int actionPeriod, int animationPeriod,
//      List<PImage> images)
//   {
//      return new Entity(EntityKind.MINER_FULL, id, position, images,
//         resourceLimit, resourceLimit, actionPeriod, animationPeriod);
//   }

//   public static Entity createMinerNotFull(String id, int resourceLimit,
//      Point position, int actionPeriod, int animationPeriod,
//      List<PImage> images)
//   {
//      return new Entity(EntityKind.MINER_NOT_FULL, id, position, images,
//         resourceLimit, 0, actionPeriod, animationPeriod);
//   }

//   public static Entity createObstacle(String id, Point position,
//      List<PImage> images)
//   {
//      return new Entity(EntityKind.OBSTACLE, id, position, images,
//         0, 0, 0, 0);
//   }

//   public static Entity createOre(String id, Point position, int actionPeriod,
//      List<PImage> images)
//   {
//      return new Entity(EntityKind.ORE, id, position, images, 0, 0,
//         actionPeriod, 0);
//   }

//   public static Entity createOreBlob(String id, Point position,
//      int actionPeriod, int animationPeriod, List<PImage> images)
//   {
//      return new Entity(EntityKind.ORE_BLOB, id, position, images,
//            0, 0, actionPeriod, animationPeriod);
//   }

//   public static Entity createQuake(Point position, List<PImage> images)
//   {
//      return new Entity(EntityKind.QUAKE, QUAKE_ID, position, images,
//         0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
//   }

//   public static Entity createVein(String id, Point position, int actionPeriod,
//      List<PImage> images)
//   {
//      return new Entity(EntityKind.VEIN, id, position, images, 0, 0,
//         actionPeriod, 0);
//   }
}
