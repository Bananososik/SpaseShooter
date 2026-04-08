import greenfoot.*;

public class EnemyShip extends Ship
{
    public enum EnemyAttackType
    {
        BASIC,
        SPREAD,
        WAVE
    }

    public enum EnemyMoveType
    {
        STRAIGHT,
        ZIGZAG
    }

    private static final int DISPLAY_SIZE = 28;

    private final int speed;
    private final EnemyAttackType attackType;
    private final EnemyMoveType moveType;
    private final int horizontalAmplitude;
    private final int horizontalSpeed;
    private final int fireInterval;
    private final int projectileDamage;
    private int fireTimer;
    private int moveTimer;
    private int originX;

    public EnemyShip(
        String imagePath,
        int speed,
        int hp,
        EnemyAttackType attackType,
        EnemyMoveType moveType,
        int difficultyTier
    )
    {
        super(hp);
        GreenfootImage enemy = new GreenfootImage(imagePath);
        enemy.scale(DISPLAY_SIZE, DISPLAY_SIZE);
        setImage(enemy);
        this.speed = speed;
        this.attackType = attackType;
        this.moveType = moveType;
        this.horizontalAmplitude = 22;
        this.horizontalSpeed = 8;
        this.fireInterval = determineFireInterval(attackType, difficultyTier);
        this.projectileDamage = 1 + (difficultyTier / 3);
        this.fireTimer = Greenfoot.getRandomNumber(fireInterval);
    }

    private int determineFireInterval(EnemyAttackType type, int difficultyTier)
    {
        int base;
        if (type == EnemyAttackType.BASIC)
        {
            base = 90;
        }
        else if (type == EnemyAttackType.SPREAD)
        {
            base = 110;
        }
        else
        {
            base = 125;
        }

        return Math.max(50, base - difficultyTier * 6);
    }

    @Override
    public void act()
    {
        movePattern();
        fireTimer++;

        if (fireTimer >= fireInterval)
        {
            fireTimer = 0;
            fire();
        }

        if (isAtEdge() || getY() > getWorld().getHeight() + 30)
        {
            getWorld().removeObject(this);
        }
    }

    protected void addedToWorld(World world)
    {
        originX = getX();
    }

    private void movePattern()
    {
        int nextY = getY() + speed;

        if (moveType == EnemyMoveType.ZIGZAG)
        {
            moveTimer++;
            int offset = (int) (Math.sin(moveTimer / (double) horizontalSpeed) * horizontalAmplitude);
            int nextX = Math.max(14, Math.min(getWorld().getWidth() - 14, originX + offset));
            setLocation(nextX, nextY);
            return;
        }

        setLocation(getX(), nextY);
    }

    private void fire()
    {
        if (attackType == EnemyAttackType.BASIC)
        {
            getWorld().addObject(new Bullet(0, 8, projectileDamage, false, "Attack/Enemy_attack.png"), getX(), getY() + 20);
            return;
        }

        if (attackType == EnemyAttackType.SPREAD)
        {
            getWorld().addObject(new Bullet(-2, 8, projectileDamage, false, "Attack/Bullet_3.png"), getX() - 8, getY() + 18);
            getWorld().addObject(new Bullet(0, 9, projectileDamage, false, "Attack/Bullet_3.png"), getX(), getY() + 20);
            getWorld().addObject(new Bullet(2, 8, projectileDamage, false, "Attack/Bullet_3.png"), getX() + 8, getY() + 18);
            return;
        }

        int waveIndex = Greenfoot.getRandomNumber(4) + 1;
        String waveSprite = "Attack/Wave_" + waveIndex + ".png";
        getWorld().addObject(new Bullet(0, 7, projectileDamage, false, waveSprite), getX(), getY() + 20);
    }

    @Override
    protected void onDestroyed()
    {
        MyWorld world = (MyWorld) getWorld();
        world.addObject(new DeathEffect(), getX(), getY());
        world.onEnemyDestroyed(getX(), getY());
    }
}
