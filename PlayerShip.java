import greenfoot.*;

/**
 * Player-controlled ship with configurable keys.
 */
public class PlayerShip extends Ship
{
    private final String leftKey;
    private final String rightKey;
    private final String upKey;
    private final String downKey;
    private final String fireKey;
    private final int minX;
    private final int speed;
    private final int fireCooldown;

    private int cooldown;

    public PlayerShip(
        String imagePath,
        String leftKey,
        String rightKey,
        String upKey,
        String downKey,
        String fireKey,
        int minX,
        int speed,
        int fireCooldown
    )
    {
        super(6);
        setImage(imagePath);

        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.upKey = upKey;
        this.downKey = downKey;
        this.fireKey = fireKey;
        this.minX = minX;
        this.speed = speed;
        this.fireCooldown = fireCooldown;
    }

    @Override
    public void act()
    {
        if (cooldown > 0)
        {
            cooldown--;
        }

        handleMovement();
        handleFire();
    }

    private void handleMovement()
    {
        int dx = 0;
        int dy = 0;

        if (Greenfoot.isKeyDown(leftKey))
        {
            dx -= speed;
        }
        if (Greenfoot.isKeyDown(rightKey))
        {
            dx += speed;
        }
        if (Greenfoot.isKeyDown(upKey))
        {
            dy -= speed;
        }
        if (Greenfoot.isKeyDown(downKey))
        {
            dy += speed;
        }

        World world = getWorld();
        int maxX = world.getWidth() - minX;
        int nextX = Math.max(minX, Math.min(maxX, getX() + dx));
        int nextY = Math.max(80, Math.min(world.getHeight() - 30, getY() + dy));
        setLocation(nextX, nextY);
    }

    private void handleFire()
    {
        if (cooldown == 0 && Greenfoot.isKeyDown(fireKey))
        {
            getWorld().addObject(new Bullet(-12, 1, true), getX(), getY() - 25);
            cooldown = fireCooldown;
        }
    }

    @Override
    protected void onDestroyed()
    {
        MyWorld world = (MyWorld) getWorld();
        world.onPlayerDestroyed();
    }
}
