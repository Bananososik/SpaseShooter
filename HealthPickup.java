import greenfoot.*;

public class HealthPickup extends Actor
{
    public HealthPickup()
    {
        GreenfootImage image = new GreenfootImage("UI/Health.png");
        image.scale(20, 20);
        setImage(image);
    }

    @Override
    public void act()
    {
        setLocation(getX(), getY() + 2);

        if (getY() > getWorld().getHeight() + 20)
        {
            getWorld().removeObject(this);
        }
    }
}
