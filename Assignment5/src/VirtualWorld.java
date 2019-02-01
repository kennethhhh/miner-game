import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import processing.core.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;


public final class VirtualWorld
   extends PApplet
{
   public static final int TIMER_ACTION_PERIOD = 100;

   public static final int VIEW_WIDTH = 640;
   public static final int VIEW_HEIGHT = 480;
   public static final int TILE_WIDTH = 32;
   public static final int TILE_HEIGHT = 32;
   public static final int WORLD_WIDTH_SCALE = 2;
   public static final int WORLD_HEIGHT_SCALE = 2;

   public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   public static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   public static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   public static final String IMAGE_LIST_FILE_NAME = "imagelist";
   public static final String DEFAULT_IMAGE_NAME = "background_default";
   public static final int DEFAULT_IMAGE_COLOR = 0x808080;

   public static final String LOAD_FILE_NAME = "gaia.sav";

   public static final String FAST_FLAG = "-fast";
   public static final String FASTER_FLAG = "-faster";
   public static final String FASTEST_FLAG = "-fastest";
   public static final double FAST_SCALE = 0.5;
   public static final double FASTER_SCALE = 0.25;
   public static final double FASTEST_SCALE = 0.10;

   public static double timeScale = 1.0;

   public ImageStore imageStore;
   public WorldModel world;
   public WorldView view;
   public EventScheduler scheduler;

   public long next_time;

   public boolean firstClicked=false;
   public Point spawn;
   public boolean stop=false;
   public Point translation=new Point(0,0);

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         this.scheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      view.drawViewport();
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;
         }
         this.translation=view.shiftView(dx, dy);
      }
   }

   public void mousePressed(){
      if(firstClicked) {
         int x = mouseX/TILE_WIDTH;
         int y = mouseY/TILE_HEIGHT;
////         Point topLeft = view.viewport.viewportToWorld(x-1,y-1);
////         Point topMid = view.viewport.viewportToWorld(x,y-1);
////         Point topRight = view.viewport.viewportToWorld(x+1,y-1);
////         Point left = view.viewport.viewportToWorld(x-1,y);
////         Point right = view.viewport.viewportToWorld(x+1,y);
////         Point botLeft = view.viewport.viewportToWorld(x-1,y+1);
////         Point botMid = view.viewport.viewportToWorld(x,y+1);
////         Point botRight = view.viewport.viewportToWorld(x+1,y+1);
//
////         world.removeEntityAt(topLeft);
////         world.removeEntityAt(topMid);
////         world.removeEntityAt(topRight);
////         world.removeEntityAt(left);
////         world.removeEntityAt(right);
////         world.removeEntityAt(botLeft);
////         world.removeEntityAt(botMid);
////         world.removeEntityAt(botRight);
//
////         world.setBackground(topLeft, new Background("blackhole0", imageStore.getImageList("blackhole0")));
////         world.setBackground(topMid, new Background("blackhole0", imageStore.getImageList("blackhole0")));
////         world.setBackground(topRight, new Background("blackhole0", imageStore.getImageList("blackhole0")));
////         world.setBackground(left, new Background("blackhole0", imageStore.getImageList("blackhole0")));
////         world.setBackground(right, new Background("blackhole0", imageStore.getImageList("blackhole0")));
////         world.setBackground(botLeft, new Background("blackhole0", imageStore.getImageList("blackhole0")));
////         world.setBackground(botMid, new Background("blackhole0", imageStore.getImageList("blackhole0")));
////         world.setBackground(botRight, new Background("blackhole0", imageStore.getImageList("blackhole0")));
//
         Point mid = view.viewport.viewportToWorld(x,y);
         if (mid.equals(spawn)){
            stop=true;
         }
         world.removeEntityAt(mid);
         world.setBackground(mid, new Background("blackhole0", imageStore.getImageList("blackhole0")));
         disappear(mid);


      }
      else{
         try {
            // Open an audio input stream.
            URL url = this.getClass().getClassLoader().getResource("naruto.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
         }
         catch (Exception e) { e.printStackTrace(); }

            // Exceute code
         firstClicked = true;
         int x = mouseX/TILE_WIDTH;
         int y = mouseY/TILE_HEIGHT;
         Point topLeft = view.viewport.viewportToWorld(x-1,y-1);
         Point topMid = view.viewport.viewportToWorld(x,y-1);
         Point topRight = view.viewport.viewportToWorld(x+1,y-1);
         Point left = view.viewport.viewportToWorld(x-1,y);
         Point mid = view.viewport.viewportToWorld(x,y);
         Point right = view.viewport.viewportToWorld(x+1,y);
         Point botLeft = view.viewport.viewportToWorld(x-1,y+1);
         Point botMid = view.viewport.viewportToWorld(x,y+1);
         Point botRight = view.viewport.viewportToWorld(x+1,y+1);

         List<Point> surroundings = new ArrayList<>();
         int[] nums = {-5,-4,-3,-2,-1,0,1,2,3,4,5};
         for (int first:nums){
            for (int second:nums){
               Point yeet = new Point(mid.x+first,mid.y+second);
               if (world.withinBounds(yeet)) {
                  surroundings.add(yeet);
               }
            }
         }
//         for (Point p:surroundings){
//            System.out.println("("+p.x+" , "+p.y+")");
//         }


         world.setBackground(topLeft, new Background("hokage0", imageStore.getImageList("hokage0")));
         world.setBackground(topMid, new Background("hokage1", imageStore.getImageList("hokage1")));
         world.setBackground(topRight, new Background("hokage2", imageStore.getImageList("hokage2")));
         world.setBackground(left, new Background("hokage3", imageStore.getImageList("hokage3")));
         world.setBackground(mid, new Background("hokage4", imageStore.getImageList("hokage4")));
         world.setBackground(right, new Background("hokage5", imageStore.getImageList("hokage5")));
         world.setBackground(botLeft, new Background("hokage6", imageStore.getImageList("hokage6")));
         world.setBackground(botMid, new Background("hokage7", imageStore.getImageList("hokage7")));
         world.setBackground(botRight, new Background("hokage8", imageStore.getImageList("hokage8")));

         this.spawn=new Point(x+translation.x,y+2+translation.y);

         Sakura sakura = new Sakura("sakura", this.spawn,imageStore.getImageList("sakura"),0,0,
                 200,100);

         for (Point check:surroundings){
            if ((world.getOccupancyCell(check) instanceof MinerNotFull)||world.getOccupancyCell(check) instanceof OreBlob){
               world.removeEntityAt(check);
               Sakura convertedToSakura =new Sakura("sakura",check,imageStore.getImageList("sakura"),0,0,
                       200,100);
               world.tryAddEntity(convertedToSakura);
               convertedToSakura.scheduleActions(scheduler,world,imageStore);
            }
         }
         if (world.isOccupied(spawn)){
            world.removeEntityAt(spawn);
         }
         if (world.withinBounds(this.spawn)) {
            world.tryAddEntity(sakura);
            sakura.scheduleActions(scheduler, world, imageStore);
         }
         s();
      }
      
   }

   public void s(){
      TimerTask task = new TimerTask() {
         @Override
         public void run() {
            Sakura intervalSakura = new Sakura("sakura",spawn,imageStore.getImageList("sakura"),0,0,
                    200,100);
            // task to run goes here
            //System.out.println("Hello !!!");
            if(!world.isOccupied(spawn)&&world.withinBounds(spawn)&&!stop){
               System.out.println("("+spawn.x+ " , "+spawn.y + ")");
               world.tryAddEntity(intervalSakura);
               intervalSakura.scheduleActions(scheduler,world,imageStore);
            }
         }
      };

      Timer timer = new Timer();
      long delay = 0;
      long intevalPeriod = 1 * 5000;

      // schedules the task to be run in an interval
      timer.scheduleAtFixedRate(task, delay,
              intevalPeriod);
   }

   public void disappear(Point p){
      new java.util.Timer().schedule(
              new java.util.TimerTask() {
                 @Override
                 public void run() {
                    world.setBackground(p,new Background("grass",imageStore.getImageList("grass")));
                    // your code here
                 }
              },
              3000
      );
   }

   public static Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
         imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }

   public static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private static void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         imageStore.loadImages(in, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public void loadWorld(WorldModel world, String filename,
      ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         Functions.load(in, world, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void scheduleActions(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      for (Entity entity : world.entities)
      {
         if (entity instanceof ActivityEntity){
         ((ActivityEntity)entity).scheduleActions(scheduler, world, imageStore);}
      }
   }

   public static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
