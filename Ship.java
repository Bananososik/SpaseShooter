import greenfoot.*;

/**
 * Base class for any ship with hit points.
 */
public abstract class Ship extends Actor
{
    protected int hp;

    public Ship(int hp)
    {
        this.hp = hp;
    }

    public void takeDamage(int damage)
    {
        hp -= damage;
        if (hp <= 0)
        {
            onDestroyed();
            if (getWorld() != null)
            {
                getWorld().removeObject(this);
            }
        }
    }

    protected abstract void onDestroyed();
}
