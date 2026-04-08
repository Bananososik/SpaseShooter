import greenfoot.*;

/**
 * Basic AI enemy ship.
 */
public class EnemyShip extends Ship
{
    private final int speed;
    private int fireTimer;

    public EnemyShip(String imagePath, int speed, int hp)
    {
        super(hp);
        setImage(imagePath);
        this.speed = speed;
    }

    @Override
    public void act()
    {
        moveDown();
        fireTimer++;

        if (fireTimer >= 50)
        {
            fireTimer = 0;
            getWorld().addObject(new Bullet(8, 1, false), getX(), getY() + 20);
        }

        if (isAtEdge() || getY() > getWorld().getHeight() + 30)
        {
            getWorld().removeObject(this);
        }
    }

    private void moveDown()
    {
        setLocation(getX(), getY() + speed);
    }

    @Override
    protected void onDestroyed()
    {
        MyWorld world = (MyWorld) getWorld();
        world.addScore(10);
    }
}
