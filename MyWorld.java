import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Main world for a small ship battle shooter prototype.
 */
public class MyWorld extends World
{
    private static final int BACKGROUND_SPEED = 2;
    private static final int BOSS_SCORE_STEP = 300;
    private static int recordScore;

    private int spawnTimer;
    private int score;
    private boolean gameFinished;
    private int backgroundOffset;
    private int currentBackgroundIndex;
    private int enemiesDestroyed;
    private int roundTicks;
    private int difficultyTier;
    private boolean guaranteedLevelUpDropped;
    private final String[] enemySprites;

    private final GreenfootImage[] backgrounds;
    private PlayerShip player;
    private BossShip boss;
    private int nextBossScore;

    /**
     * Constructor for objects of class MyWorld.
     */
    public MyWorld()
    {    
        super(320, 512, 1);

        backgrounds = new GreenfootImage[] {
            loadBackground("Backgrounds/BackGround_1.png"),
            loadBackground("Backgrounds/BackGround_2.png"),
            loadBackground("Backgrounds/BackGround_3.png")
        };
        enemySprites = new String[] {
            "Enemies/Enemy_1.png",
            "Enemies/Enemy_2.png",
            "Enemies/Enemy_3.png",
            "Enemies/Enemy_4.png",
            "Enemies/Enemy_5.png",
            "Enemies/Enemy_7.png",
            "Enemies/Enemy_8.png",
            "Enemies/Enemy_9.png",
            "Enemies/Enemy_10.png",
            "Enemies/Enemy_11.png",
            "Enemies/Enemy_12.png",
            "Enemies/Enemy_13.png",
            "Enemies/Enemy_14.png",
            "Enemies/Enemy_15.png",
            "Enemies/Enemy_16.png",
            "Enemies/Enemy_17.png",
            "Enemies/Enemy_18.png",
            "Enemies/Enemy_19.png",
            "Enemies/Enemy_20.png",
            "Enemies/Enemy_21.png",
            "Enemies/Enemy_22.png",
            "Enemies/Enemy_23.png",
            "Enemies/Enemy_24.png",
            "Enemies/Enemy_25.png",
            "Enemies/Enemy_26.png",
            "Enemies/Enemy_27.png",
            "Enemies/Enemy_28.png",
            "Enemies/Enemy_29.png",
            "Enemies/Enemy_30.png",
            "Enemies/Enemy_31.png",
            "Enemies/Enemy_32.png",
            "Enemies/Enemy_33.png",
            "Enemies/Enemy_34.png",
            "Enemies/Enemy_35.png",
            "Enemies/Enemy_36.png"
        };

        drawBackground();

        preparePlayers();
        prepareHud();
        nextBossScore = BOSS_SCORE_STEP;
    }

    private GreenfootImage loadBackground(String path)
    {
        GreenfootImage image = new GreenfootImage(path);
        image.scale(getWidth(), getHeight());
        return image;
    }

    private void prepareHud()
    {
        addObject(new HudIcon("UI/Health.png", 18), 16, 20);
        addObject(new HudIcon("UI/Shield.png", 18), 16, 72);
    }

    private void preparePlayers()
    {
        player = new PlayerShip(
            "Players/Player_1.png",
            "a", "d", "w", "s", "space",
            4,
            10
        );

        addObject(player, getWidth() / 2, getHeight() - 60);
    }

    @Override
    public void act()
    {
        updateBackground();

        if (gameFinished)
        {
            return;
        }

        roundTicks++;

        spawnTimer++;
        if (boss == null && spawnTimer >= 60)
        {
            spawnEnemy();
            spawnTimer = 0;
        }

        if (boss == null && score >= nextBossScore)
        {
            spawnBoss();
            nextBossScore += BOSS_SCORE_STEP;
        }

        showText("Score: " + score, 62, 16);
        showText(getPlayerHpPercent() + "%", 26, 40);
        showText(getPlayerShieldPercent() + "%", 26, 92);
        showText("Lvl " + getPlayerWeaponLevel(), 24, 120);
        showText("", 266, 16);

        drawRightHudText("Record: " + recordScore, 16);
        drawRightHudText(getBossStatusText(), 40);
        drawRightHudText("Round: " + getRoundTimeText(), 64);
        drawRightHudText("Reload: " + getReloadText(), 88);
        drawRightHudText("Kills: " + enemiesDestroyed, 112);
        drawRightHudText("Diff: " + difficultyTier, 136);
    }

    private void drawRightHudText(String text, int y)
    {
        GreenfootImage label = new GreenfootImage(text, 16, Color.WHITE, new Color(0, 0, 0, 0));
        int x = getWidth() - 10 - label.getWidth();
        getBackground().drawImage(label, x, y - label.getHeight() / 2);
    }

    private String getRoundTimeText()
    {
        int totalSeconds = roundTicks / 60;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        String paddedSeconds = (seconds < 10 ? "0" : "") + seconds;
        return minutes + ":" + paddedSeconds;
    }

    private String getReloadText()
    {
        if (player == null || player.getWorld() == null)
        {
            return "-";
        }

        int remaining = player.getReloadTicksRemaining();
        if (remaining <= 0)
        {
            return "READY";
        }

        return (remaining / 60.0) + "s";
    }

    private String getBossStatusText()
    {
        if (boss == null || boss.getWorld() == null)
        {
            return "Boss: none";
        }
        return "Boss HP: " + boss.getHpPercent() + "%";
    }

    private void updateBackground()
    {
        int wantedIndex = (score / 300) % backgrounds.length;
        if (wantedIndex != currentBackgroundIndex)
        {
            currentBackgroundIndex = wantedIndex;
        }

        backgroundOffset = (backgroundOffset + BACKGROUND_SPEED) % getHeight();
        drawBackground();
    }

    private void drawBackground()
    {
        GreenfootImage canvas = getBackground();
        GreenfootImage texture = backgrounds[currentBackgroundIndex];
        int y1 = backgroundOffset;
        int y2 = backgroundOffset - getHeight();

        canvas.drawImage(texture, 0, y1);
        canvas.drawImage(texture, 0, y2);
    }

    private int getPlayerHpPercent()
    {
        if (player == null || player.getWorld() == null)
        {
            return 0;
        }
        return (player.getHp() * 100) / player.getMaxHp();
    }

    private int getPlayerShieldPercent()
    {
        if (player == null || player.getWorld() == null)
        {
            return 0;
        }
        return (player.getShieldPoints() * 100) / player.getMaxShieldPoints();
    }

    private void spawnEnemy()
    {
        int x = Greenfoot.getRandomNumber(getWidth() - 30) + 15;
        int speed = 2 + Greenfoot.getRandomNumber(3) + Math.min(3, difficultyTier / 2);
        int hp = 2 + Greenfoot.getRandomNumber(3) + difficultyTier;
        String imagePath = enemySprites[Greenfoot.getRandomNumber(enemySprites.length)];
        EnemyShip.EnemyAttackType attackType = chooseAttackType();
        EnemyShip.EnemyMoveType moveType = chooseMoveType();

        EnemyShip enemy = new EnemyShip(imagePath, speed, hp, attackType, moveType, difficultyTier);
        addObject(enemy, x, -30);
    }

    private EnemyShip.EnemyAttackType chooseAttackType()
    {
        int roll = Greenfoot.getRandomNumber(100);
        if (roll < 50)
        {
            return EnemyShip.EnemyAttackType.BASIC;
        }
        if (roll < 80)
        {
            return EnemyShip.EnemyAttackType.SPREAD;
        }
        return EnemyShip.EnemyAttackType.WAVE;
    }

    private EnemyShip.EnemyMoveType chooseMoveType()
    {
        return Greenfoot.getRandomNumber(100) < 35
            ? EnemyShip.EnemyMoveType.ZIGZAG
            : EnemyShip.EnemyMoveType.STRAIGHT;
    }

    private void spawnBoss()
    {
        int bossIndex = ((nextBossScore / BOSS_SCORE_STEP) - 1) % 6 + 1;
        String bossPath = "Bosses/Boss_" + bossIndex + ".png";
        int hp = 40 + (nextBossScore / BOSS_SCORE_STEP) * 10;

        boss = new BossShip(bossPath, hp);
        addObject(boss, getWidth() / 2, -40);
    }

    public void addScore(int delta)
    {
        score += delta;
        if (score > recordScore)
        {
            recordScore = score;
        }
    }

    public void onEnemyDestroyed(int x, int y)
    {
        addScore(10);
        enemiesDestroyed++;

        int roll = Greenfoot.getRandomNumber(1000);
        if (!guaranteedLevelUpDropped && score < BOSS_SCORE_STEP)
        {
            boolean guaranteedNow = enemiesDestroyed >= 8 || (score >= 260 && roll < 1000);
            if (guaranteedNow || roll < 30)
            {
                addObject(new LevelUpPickup(), x, y);
                guaranteedLevelUpDropped = true;
                return;
            }
        }
        else if (roll < 12)
        {
            addObject(new LevelUpPickup(), x, y);
            return;
        }

        if (roll < 160)
        {
            addObject(new HealthPickup(), x, y);
        }
        else if (roll < 290)
        {
            addObject(new ShieldPickup(), x, y);
        }
    }

    private int getPlayerWeaponLevel()
    {
        if (player == null || player.getWorld() == null)
        {
            return 0;
        }
        return player.getWeaponLevel();
    }

    public void onBossDestroyed(BossShip destroyedBoss)
    {
        if (boss == destroyedBoss)
        {
            boss = null;
        }
        difficultyTier++;
        addScore(100);
    }

    public void onPlayerDestroyed()
    {
        gameFinished = true;
        showText("Game Over. Score: " + score, getWidth() / 2, getHeight() / 2);
    }
}
