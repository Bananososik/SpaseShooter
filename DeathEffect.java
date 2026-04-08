import greenfoot.*;

public class DeathEffect extends Actor
{
    private static final int FRAME_SIZE = 8;
    private static final int FRAME_COUNT = 4;
    private static final int DISPLAY_SIZE = 28;
    private static final int FRAME_TICKS = 5;

    private final GreenfootImage[] frames;
    private int frame;
    private int tick;

    public DeathEffect()
    {
        int variant = Greenfoot.getRandomNumber(3) + 1;
        frames = loadAnimationVariant("animated/Death_" + variant + ".png");
        setImage(frames[0]);
    }

    private GreenfootImage[] loadAnimationVariant(String path)
    {
        GreenfootImage strip = new GreenfootImage(path);
        GreenfootImage[] loaded = new GreenfootImage[FRAME_COUNT];

        for (int i = 0; i < FRAME_COUNT; i++)
        {
            GreenfootImage frameImage = new GreenfootImage(FRAME_SIZE, FRAME_SIZE);
            frameImage.drawImage(strip, -i * FRAME_SIZE, 0);
            frameImage.scale(DISPLAY_SIZE, DISPLAY_SIZE);
            loaded[i] = frameImage;
        }

        return loaded;
    }

    @Override
    public void act()
    {
        if (getWorld() == null)
        {
            return;
        }

        tick++;
        if (tick >= FRAME_TICKS)
        {
            tick = 0;
            frame++;
            if (frame >= frames.length)
            {
                getWorld().removeObject(this);
                return;
            }
            setImage(frames[frame]);
        }
    }
}
