import greenfoot.*;

public class PlayerShip extends Ship
{
    private static final int SPRITE_SIZE = 8;
    private static final int DISPLAY_SIZE = 32;
    private static final int MAX_SHIELD_POINTS = 3;
    private static final int MAX_WEAPON_LEVEL = 4;

    private final String leftKey;
    private final String rightKey;
    private final String upKey;
    private final String downKey;
    private final String fireKey;
    private final int speed;
    private final int fireCooldown;
    private final GreenfootImage leftFrame;
    private final GreenfootImage centerFrame;
    private final GreenfootImage rightFrame;

    private int cooldown;
    private int shieldPoints;
    private int weaponLevel = 1;
    private ShieldAura shieldAura;

    public PlayerShip(
        String imagePath,
        String leftKey,
        String rightKey,
        String upKey,
        String downKey,
        String fireKey,
        int speed,
        int fireCooldown
    )
    {
        super(6);
        GreenfootImage sheet = new GreenfootImage(imagePath);
        leftFrame = makeFrame(sheet, 0);
        centerFrame = makeFrame(sheet, 1);
        rightFrame = makeFrame(sheet, 2);
        setImage(centerFrame);

        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.upKey = upKey;
        this.downKey = downKey;
        this.fireKey = fireKey;
        this.speed = speed;
        this.fireCooldown = fireCooldown;
    }

    private GreenfootImage makeFrame(GreenfootImage sheet, int frameIndex)
    {
        GreenfootImage frame = new GreenfootImage(SPRITE_SIZE, SPRITE_SIZE);
        frame.drawImage(sheet, -frameIndex * SPRITE_SIZE, 0);
        frame.scale(DISPLAY_SIZE, DISPLAY_SIZE);
        return frame;
    }

    @Override
    public void act()
    {
        MyWorld world = (MyWorld) getWorld();
        if (world != null && world.isBattlePaused())
        {
            return;
        }

        collectPickups();

        if (cooldown > 0)
        {
            cooldown--;
        }

        handleMovement();
        handleFire();
    }

    private void collectPickups()
    {
        HealthPickup healthPickup = (HealthPickup) getOneIntersectingObject(HealthPickup.class);
        if (healthPickup != null)
        {
            heal(2);
            getWorld().removeObject(healthPickup);
        }

        ShieldPickup shieldPickup = (ShieldPickup) getOneIntersectingObject(ShieldPickup.class);
        if (shieldPickup != null)
        {
            addShield(3);
            getWorld().removeObject(shieldPickup);
        }

        LevelUpPickup levelUpPickup = (LevelUpPickup) getOneIntersectingObject(LevelUpPickup.class);
        if (levelUpPickup != null)
        {
            applyLevelUp();
            getWorld().removeObject(levelUpPickup);
        }
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
        int halfSprite = DISPLAY_SIZE / 2;
        int nextX = Math.max(halfSprite, Math.min(world.getWidth() - halfSprite, getX() + dx));
        int nextY = Math.max(70, Math.min(world.getHeight() - halfSprite, getY() + dy));
        setLocation(nextX, nextY);

        if (dx < 0)
        {
            setImage(leftFrame);
        }
        else if (dx > 0)
        {
            setImage(rightFrame);
        }
        else
        {
            setImage(centerFrame);
        }
    }

    private void handleFire()
    {
        if (cooldown == 0 && Greenfoot.isKeyDown(fireKey))
        {
            fireWeaponByLevel();
            cooldown = Math.max(4, fireCooldown - (weaponLevel - 1) * 2);
        }
    }

    private void fireWeaponByLevel()
    {
        String bulletSprite = "Attack/Bullet_" + weaponLevel + ".png";
        int y = getY() - 25;

        if (weaponLevel == 1)
        {
            getWorld().addObject(new Bullet(0, -12, 1, true, bulletSprite), getX(), y);
            return;
        }

        if (weaponLevel == 2)
        {
            getWorld().addObject(new Bullet(0, -12, 1, true, bulletSprite), getX() - 7, y);
            getWorld().addObject(new Bullet(0, -12, 1, true, bulletSprite), getX() + 7, y);
            return;
        }

        if (weaponLevel == 3)
        {
            getWorld().addObject(new Bullet(-1, -12, 2, true, bulletSprite), getX() - 10, y);
            getWorld().addObject(new Bullet(0, -13, 2, true, bulletSprite), getX(), y - 2);
            getWorld().addObject(new Bullet(1, -12, 2, true, bulletSprite), getX() + 10, y);
            return;
        }

        getWorld().addObject(new Bullet(-2, -12, 2, true, bulletSprite), getX() - 12, y);
        getWorld().addObject(new Bullet(-1, -13, 2, true, bulletSprite), getX() - 6, y - 1);
        getWorld().addObject(new Bullet(0, -14, 3, true, bulletSprite), getX(), y - 2);
        getWorld().addObject(new Bullet(1, -13, 2, true, bulletSprite), getX() + 6, y - 1);
        getWorld().addObject(new Bullet(2, -12, 2, true, bulletSprite), getX() + 12, y);
    }

    private void applyLevelUp()
    {
        if (weaponLevel < MAX_WEAPON_LEVEL)
        {
            weaponLevel++;
        }

        increaseMaxHp(2);
    }

    @Override
    public void takeDamage(int damage)
    {
        if (shieldPoints > 0)
        {
            shieldPoints -= damage;
            if (shieldPoints <= 0)
            {
                shieldPoints = 0;
                if (shieldAura != null)
                {
                    shieldAura.deactivate();
                    shieldAura = null;
                }
            }
            return;
        }

        super.takeDamage(damage);
    }

    public int getShieldPoints()
    {
        return shieldPoints;
    }

    public int getMaxShieldPoints()
    {
        return MAX_SHIELD_POINTS;
    }

    public void addShield(int points)
    {
        shieldPoints = Math.min(MAX_SHIELD_POINTS, shieldPoints + points);

        if (shieldAura == null)
        {
            shieldAura = new ShieldAura(this);
            getWorld().addObject(shieldAura, getX(), getY());
        }
        shieldAura.activate();
    }

    public int getWeaponLevel()
    {
        return weaponLevel;
    }

    public int getReloadTicksRemaining()
    {
        return cooldown;
    }

    public int getReloadTicksTotal()
    {
        return Math.max(4, fireCooldown - (weaponLevel - 1) * 2);
    }

    @Override
    protected void onDestroyed()
    {
        if (shieldAura != null)
        {
            shieldAura.forceRemove();
            shieldAura = null;
        }

        MyWorld world = (MyWorld) getWorld();
        world.onPlayerDestroyed();
    }
}
