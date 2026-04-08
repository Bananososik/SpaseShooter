import greenfoot.*;

/**
 * Short enemy death animation.
 */
public class DeathEffect extends Actor
{
    private static final int FRAME_TICKS = 5;

    private final GreenfootImage[] frames;
    private int frame;
    private int tick;

    public DeathEffect()
    {
        frames = new GreenfootImage[] {
            loadFrame("animated/Death_1.png"),
            loadFrame("animated/Death_2.png"),
            loadFrame("animated/Death_3.png")
        };
        setImage(frames[0]);
    }

    private GreenfootImage loadFrame(String path)
    {
        GreenfootImage img = new GreenfootImage(path);
        img.scale(28, 28);
        return img;
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
