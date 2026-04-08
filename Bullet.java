import greenfoot.*;

/**
 * Shared bullet for players and enemies.
 */
public class Bullet extends Actor
{
    private final int speedY;
    private final int damage;
    private final boolean fromPlayer;

    public Bullet(int speedY, int damage, boolean fromPlayer)
    {
        this.speedY = speedY;
        this.damage = damage;
        this.fromPlayer = fromPlayer;
        setImage(fromPlayer ? "SpaceShooterAssets/SpaceSBullet_1.png" : "UI/Enemy_attack.png");
    }

    @Override
    public void act()
    {
        setLocation(getX(), getY() + speedY);

        if (getY() < -20 || getY() > getWorld().getHeight() + 20)
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
