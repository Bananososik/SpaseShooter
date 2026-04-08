import greenfoot.*;

public class BossShip extends Ship
{
    private static final int DISPLAY_SIZE = 64;
    private static final int ATTACK_INTERVAL_TICKS = 80;

    private int moveDir = 1;
    private int attackTimer;

    public BossShip(String imagePath, int hp)
    {
        super(hp);
        GreenfootImage bossImage = new GreenfootImage(imagePath);
        bossImage.scale(DISPLAY_SIZE, DISPLAY_SIZE);
        setImage(bossImage);
    }

    @Override
    public void act()
    {
        if (getWorld() == null)
        {
            return;
        }

        movePattern();

        attackTimer++;
        if (attackTimer >= ATTACK_INTERVAL_TICKS)
        {
            attackTimer = 0;
            performAttack();
        }
    }

    private void movePattern()
    {
        if (getY() < 88)
        {
            setLocation(getX(), getY() + 2);
            return;
        }

        int nextX = getX() + moveDir * 3;
        int margin = 36;
        if (nextX < margin || nextX > getWorld().getWidth() - margin)
        {
            moveDir *= -1;
            nextX = getX() + moveDir * 3;
        }

        setLocation(nextX, getY());
    }

    private void performAttack()
    {
        int pattern = Greenfoot.getRandomNumber(3);
        if (pattern == 0)
        {
            attackSpread();
        }
        else if (pattern == 1)
        {
            attackBurst();
        }
        else
        {
            attackRay();
        }
    }

    private void attackSpread()
    {
        String sprite = "Attack/Boss_attack_1.png";
        getWorld().addObject(new Bullet(-2, 8, 1, false, sprite), getX() - 14, getY() + 24);
        getWorld().addObject(new Bullet(0, 9, 1, false, sprite), getX(), getY() + 24);
        getWorld().addObject(new Bullet(2, 8, 1, false, sprite), getX() + 14, getY() + 24);
    }

    private void attackBurst()
    {
        int waveFrame = Greenfoot.getRandomNumber(4) + 1;
        String sprite = "Attack/Wave_" + waveFrame + ".png";

        getWorld().addObject(
            new Bullet(0, 9, 1, false, sprite),
            getX() - 12,
            getY() + 24
        );
        getWorld().addObject(
            new Bullet(0, 9, 1, false, sprite),
            getX(),
            getY() + 24
        );
        getWorld().addObject(
            new Bullet(0, 9, 1, false, sprite),
            getX() + 12,
            getY() + 24
        );
    }

    private void attackRay()
    {
        int x1 = Math.max(20, Math.min(getWorld().getWidth() - 20, getX() - 24));
        int x2 = Math.max(20, Math.min(getWorld().getWidth() - 20, getX() + 24));

        getWorld().addObject(new BossRay(x1), x1, getWorld().getHeight() / 2 + 16);
        getWorld().addObject(new BossRay(x2), x2, getWorld().getHeight() / 2 + 16);
    }

    public int getHpPercent()
    {
        return (hp * 100) / maxHp;
    }

    @Override
    protected void onDestroyed()
    {
        MyWorld world = (MyWorld) getWorld();
        world.onBossDestroyed(this);
    }
}
