import greenfoot.*;

public class HudIcon extends Actor
{
    public HudIcon(String path, int size)
    {
        GreenfootImage image = new GreenfootImage(path);
        image.scale(size, size);
        setImage(image);
    }

    @Override
    public void act()
    {
        // HUD icon is static.
    }
}
