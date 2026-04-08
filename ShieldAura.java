import greenfoot.*;

/**
 * Animated shield effect around player.
 */
public class ShieldAura extends Actor
{
    private enum State
    {
        OFF,
        ACTIVATING,
        ACTIVE,
        DEACTIVATING
    }

    private static final int FRAME_SIZE = 16;
    private static final int FRAME_COUNT = 4;
    private static final int DISPLAY_SIZE = 48;
    private static final int TICKS_PER_FRAME = 3;

    private final PlayerShip owner;
    private final GreenfootImage[] frames;

    private State state;
    private int frameIndex;
    private int frameTick;

    public ShieldAura(PlayerShip owner)
    {
        this.owner = owner;
        this.frames = loadFrames();
        this.state = State.OFF;
        this.frameIndex = 0;
        setImage(frames[0]);
    }

    private GreenfootImage[] loadFrames()
    {
        GreenfootImage sheet = new GreenfootImage("animated/shield.png");
        GreenfootImage[] loaded = new GreenfootImage[FRAME_COUNT];

        for (int i = 0; i < FRAME_COUNT; i++)
        {
            GreenfootImage frame = new GreenfootImage(FRAME_SIZE, FRAME_SIZE);
            frame.drawImage(sheet, -i * FRAME_SIZE, 0);
            frame.scale(DISPLAY_SIZE, DISPLAY_SIZE);
            loaded[i] = frame;
        }

        return loaded;
    }

    public void activate()
    {
        if (state == State.ACTIVE)
        {
            return;
        }
        state = State.ACTIVATING;
        frameIndex = 0;
        frameTick = 0;
        setImage(frames[frameIndex]);
    }

    public void deactivate()
    {
        if (state == State.OFF)
        {
            return;
        }

        state = State.DEACTIVATING;
        frameTick = 0;
        if (frameIndex < FRAME_COUNT - 1)
        {
            frameIndex = FRAME_COUNT - 1;
            setImage(frames[frameIndex]);
        }
    }

    public void forceRemove()
    {
        if (getWorld() != null)
        {
            getWorld().removeObject(this);
        }
    }

    @Override
    public void act()
    {
        if (owner == null || owner.getWorld() == null || getWorld() == null)
        {
            forceRemove();
            return;
        }

        setLocation(owner.getX(), owner.getY());

        if (state == State.ACTIVE || state == State.OFF)
        {
            return;
        }

        frameTick++;
        if (frameTick < TICKS_PER_FRAME)
        {
            return;
        }
        frameTick = 0;

        if (state == State.ACTIVATING)
        {
            frameIndex++;
            if (frameIndex >= FRAME_COUNT)
            {
                frameIndex = FRAME_COUNT - 1;
                state = State.ACTIVE;
            }
            setImage(frames[frameIndex]);
            return;
        }

        if (state == State.DEACTIVATING)
        {
            frameIndex--;
            if (frameIndex < 0)
            {
                state = State.OFF;
                forceRemove();
                return;
            }
            setImage(frames[frameIndex]);
        }
    }
}
