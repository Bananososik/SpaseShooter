import greenfoot.*;

public abstract class Ship extends Actor
{
    protected int hp;
    protected int maxHp;

    public Ship(int hp)
    {
        this.hp = hp;
        this.maxHp = hp;
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

    public void heal(int amount)
    {
        hp = Math.min(maxHp, hp + amount);
    }

    public int getHp()
    {
        return hp;
    }

    public int getMaxHp()
    {
        return maxHp;
    }

    public void increaseMaxHp(int amount)
    {
        maxHp += amount;
        hp += amount;
    }

    protected abstract void onDestroyed();
}
