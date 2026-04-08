import greenfoot.*;

public class BossDialogue extends Actor
{
    private static final int SOURCE_WIDTH = 38;
    private static final int SOURCE_HEIGHT = 10;
    private static final int SCALE = 8;
    private static final int PANEL_WIDTH = SOURCE_WIDTH * SCALE;
    private static final int PANEL_HEIGHT = SOURCE_HEIGHT * SCALE;
    private static final int PORTRAIT_FRAME_SIZE = 8;
    private static final int PORTRAIT_DISPLAY_SIZE = 64;
    private static final int PORTRAIT_X = 16;
    private static final int PORTRAIT_Y = 16;
    private static final int TEXT_X = 88;
    private static final int TEXT_Y = 28;
    private static final int FRAME_TICKS = 5;
    private static final int LINE_DURATION = 30;

    private static final String[] PLAYER_LINES = {
        "krrk...",
        "vzzt?",
        "mrrh...",
        "shk-rrt",
        "tzz?",
        "brrn..."
    };

    private static final String[] BOSS_LINES = {
        "grrzz...",
        "xraa...",
        "thrrk?",
        "zzr-hm",
        "krx...",
        "vrrt..."
    };

    private final GreenfootImage panel;
    private final GreenfootImage[] playerFrames;
    private final GreenfootImage[] bossFrames;
    private final int totalTicks;

    private int frameTick;
    private int playerFrameIndex;
    private int bossFrameIndex;
    private int playerFrameTick;
    private int bossFrameTick;

    public BossDialogue(String bossPortraitPath, int introTicks)
    {
        panel = new GreenfootImage(PANEL_WIDTH, PANEL_HEIGHT);
        playerFrames = loadFrames("NPCs/NPC_5.png");
        bossFrames = loadFrames(bossPortraitPath);
        totalTicks = introTicks;
        render(introTicks);
    }

    private GreenfootImage[] loadFrames(String path)
    {
        GreenfootImage strip = new GreenfootImage(path);
        GreenfootImage[] frames = new GreenfootImage[5];

        for (int i = 0; i < frames.length; i++)
        {
            GreenfootImage frame = new GreenfootImage(PORTRAIT_FRAME_SIZE, PORTRAIT_FRAME_SIZE);
            frame.drawImage(strip, -i * PORTRAIT_FRAME_SIZE, 0);
            frame.scale(PORTRAIT_DISPLAY_SIZE, PORTRAIT_DISPLAY_SIZE);
            frames[i] = frame;
        }

        return frames;
    }

    @Override
    public void act()
    {
        MyWorld world = (MyWorld) getWorld();
        if (world == null)
        {
            return;
        }

        if (!world.isBattlePaused())
        {
            if (getWorld() != null)
            {
                getWorld().removeObject(this);
            }
            return;
        }

        render(world.getBattleLockTicksRemaining());
    }

    private void render(int ticksRemaining)
    {
        panel.setColor(new Color(0, 0, 0, 0));
        panel.fill();

        GreenfootImage bar = new GreenfootImage("UI/textBar.png");
        bar.scale(PANEL_WIDTH, PANEL_HEIGHT);
        panel.drawImage(bar, 0, 0);

        int elapsedTicks = totalTicks - ticksRemaining;
        int lineIndex = Math.min(elapsedTicks / LINE_DURATION, PLAYER_LINES.length - 1);
        boolean playerSpeaks = (lineIndex % 2 == 0);
        String line = playerSpeaks ? PLAYER_LINES[lineIndex] : BOSS_LINES[lineIndex];

        if (playerSpeaks)
        {
            playerFrameTick++;
            if (playerFrameTick >= FRAME_TICKS)
            {
                playerFrameTick = 0;
                playerFrameIndex = (playerFrameIndex + 1) % playerFrames.length;
            }
        }
        else
        {
            bossFrameTick++;
            if (bossFrameTick >= FRAME_TICKS)
            {
                bossFrameTick = 0;
                bossFrameIndex = (bossFrameIndex + 1) % bossFrames.length;
            }
        }

        GreenfootImage portrait = playerSpeaks ? playerFrames[playerFrameIndex] : bossFrames[bossFrameIndex];

        panel.drawImage(portrait, PORTRAIT_X, PORTRAIT_Y);
        panel.setColor(Color.WHITE);
        panel.setFont(new Font("Monospaced", false, false, 14));
        panel.drawString(line, TEXT_X, TEXT_Y);

        setImage(panel);
    }
}
