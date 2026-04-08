    import greenfoot.*;

public class Bullet extends Actor
{
    private static final int BULLET_SIZE = 16;

    private final int speedX;
    private final int speedY;
    private final int damage;
    private final boolean fromPlayer;

    public Bullet(int speedY, int damage, boolean fromPlayer)
    {
        this(0, speedY, damage, fromPlayer, fromPlayer ? "Attack/Bullet_1.png" : "Attack/Bullet_3.png");
    }

    public Bullet(int speedX, int speedY, int damage, boolean fromPlayer, String spritePath)
    {
        this.speedX = speedX;
        this.speedY = speedY;
        this.damage = damage;
        this.fromPlayer = fromPlayer;

        GreenfootImage bullet = new GreenfootImage(spritePath);
        bullet.scale(BULLET_SIZE, BULLET_SIZE);
        setImage(bullet);
    }

    @Override
    public void act()
    {
        if (getWorld() == null)
        {
            return;
        }

        MyWorld world = (MyWorld) getWorld();
        if (world != null && world.isBattlePaused())
        {
            return;
        }

        setLocation(getX() + speedX, getY() + speedY);

        int maxX = getWorld().getWidth() - 1;
        int maxY = getWorld().getHeight() - 1;
        boolean leftScreen =
            (speedY < 0 && getY() <= 0) ||
            (speedY > 0 && getY() >= maxY) ||
            (speedX < 0 && getX() <= 0) ||
            (speedX > 0 && getX() >= maxX);

        if (leftScreen)
        {
            getWorld().removeObject(this);
            return;
        }

        if (fromPlayer)
        {
            EnemyShip target = (EnemyShip) getOneIntersectingObject(EnemyShip.class);
            if (target != null)
            {
                target.takeDamage(damage);
                getWorld().removeObject(this);
                return;
            }

            BossShip bossTarget = (BossShip) getOneIntersectingObject(BossShip.class);
            if (bossTarget != null)
            {
                bossTarget.takeDamage(damage);
                getWorld().removeObject(this);
            }
        }
        else
        {
            PlayerShip target = (PlayerShip) getOneIntersectingObject(PlayerShip.class);
            if (target != null)
            {
                target.takeDamage(damage);
                getWorld().removeObject(this);
            }
        }
    }
}
