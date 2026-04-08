import greenfoot.*;

public class BossRay extends Actor
{
    private static final int FRAME_COUNT = 4;
    private static final int FRAME_TICKS = 4;
    private static final int LIFE_TICKS = 55;

    private final GreenfootImage[] frames;
    private int frameIndex;
    private int frameTick;
    private int lifeTick;
    private int hitCooldown;

    public BossRay(int x)
    {
        frames = loadFrames();
        setImage(frames[0]);
    }

    private GreenfootImage[] loadFrames()
    {
        GreenfootImage[] loaded = new GreenfootImage[FRAME_COUNT];
        for (int i = 0; i < FRAME_COUNT; i++)
        {
            GreenfootImage image = new GreenfootImage("Attack/ray_" + (i + 1) + ".png");
            image.rotate(90);
            image.scale(34, 360);
            loaded[i] = image;
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

        lifeTick++;
        if (lifeTick >= LIFE_TICKS)
        {
            getWorld().removeObject(this);
            return;
        }

        frameTick++;
        if (frameTick >= FRAME_TICKS)
        {
            frameTick = 0;
            frameIndex = (frameIndex + 1) % FRAME_COUNT;
            setImage(frames[frameIndex]);
        }

        if (hitCooldown > 0)
        {
            hitCooldown--;
        }

        PlayerShip player = (PlayerShip) getOneIntersectingObject(PlayerShip.class);
        if (player != null && hitCooldown == 0)
        {
            player.takeDamage(1);
            hitCooldown = 16;
        }
    }
}
