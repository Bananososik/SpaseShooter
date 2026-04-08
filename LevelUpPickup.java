import greenfoot.*;

/**
 * Rare upgrade pickup that increases player stats.
 */
public class LevelUpPickup extends Actor
{
    public LevelUpPickup()
    {
        GreenfootImage image = new GreenfootImage("UI/lvlUp.png");
        image.scale(20, 20);
        setImage(image);
    }

    @Override
    public void act()
    {
        if (getWorld() == null)
        {
            return;
        }

        setLocation(getX(), getY() + 2);

        if (getY() > getWorld().getHeight() + 20)
        {
            getWorld().removeObject(this);
        }
    }
}
