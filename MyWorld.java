import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Main world for a small ship battle shooter prototype.
 */
public class MyWorld extends World
{
    private int spawnTimer;
    private int score;
    private int alivePlayers;
    private boolean gameFinished;

    /**
     * Constructor for objects of class MyWorld.
     */
    public MyWorld()
    {    
        super(900, 600, 1);
        setBackground("Backgrounds/BackGround_1.png");

        preparePlayers();
    }

    private void preparePlayers()
    {
        PlayerShip player1 = new PlayerShip(
            "Players/Player_1.png",
            "a", "d", "w", "s", "space",
            120,
            6,
            10
        );

        PlayerShip player2 = new PlayerShip(
            "Players/Player_2.png",
            "left", "right", "up", "down", "enter",
            780,
            6,
            10
        );

        addObject(player1, 120, 500);
        addObject(player2, 780, 500);
        alivePlayers = 2;
    }

    @Override
    public void act()
    {
        if (gameFinished)
        {
            return;
        }

        spawnTimer++;
        if (spawnTimer >= 60)
        {
            spawnEnemy();
            spawnTimer = 0;
        }

        showText("Score: " + score, 90, 20);
        showText("Players alive: " + alivePlayers, 420, 20);
        showText("P1: WASD + SPACE | P2: ARROWS + ENTER", 450, 45);
    }

    private void spawnEnemy()
    {
        int x = Greenfoot.getRandomNumber(getWidth() - 120) + 60;
        int speed = 2 + Greenfoot.getRandomNumber(3);
        int hp = 2 + Greenfoot.getRandomNumber(3);

        EnemyShip enemy = new EnemyShip("Enemies/Enemy_1.png", speed, hp);
        addObject(enemy, x, -30);
    }

    public void addScore(int delta)
    {
        score += delta;
        if (score >= 100)
        {
            gameFinished = true;
            showText("Victory! Final score: " + score, getWidth() / 2, getHeight() / 2);
        }
    }

    public void onPlayerDestroyed()
    {
        alivePlayers--;
        if (alivePlayers <= 0)
        {
            gameFinished = true;
            showText("Game Over. Score: " + score, getWidth() / 2, getHeight() / 2);
        }
    }
}
