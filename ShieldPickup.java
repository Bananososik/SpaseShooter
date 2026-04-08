import greenfoot.*;

public class ShieldPickup extends Actor
{
    public ShieldPickup()
    {
        GreenfootImage image = new GreenfootImage("UI/Shield.png");
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
