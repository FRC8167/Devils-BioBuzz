package org.firstinspires.ftc.teamcode.SubSystems;

/**
 * Singleton class holding global field state, including hive positions,
 * current hive states, and estimated number of tips.
 */
public class FieldState {

    public enum HiveColor {
        RED,
        BLUE
    }

    public enum HiveState {
        STARTING,           // Settled in initial manual starting position (~+30°)
        REVERSED_TIPPED,    // Flipped into alternate stable state (~-30°)
        IN_BETWEEN_MOVING   // Actively moving or transitioning between states
    }

    private static final FieldState INSTANCE = new FieldState();

    public static FieldState getInstance() {
        return INSTANCE;
    }

    private FieldState() {}

    private HiveState redHiveState = HiveState.STARTING;
    private HiveState blueHiveState = HiveState.STARTING;

    private int estimatedRedTips = 0;
    private int estimatedBlueTips = 0;

    /**
     * Resets field state and estimated tip counts.
     */
    public void reset() {
        redHiveState = HiveState.STARTING;
        blueHiveState = HiveState.STARTING;
        estimatedRedTips = 0;
        estimatedBlueTips = 0;
    }

    public HiveState getRedHiveState() {
        return redHiveState;
    }

    public void setRedHiveState(HiveState redHiveState) {
        this.redHiveState = redHiveState;
    }

    public HiveState getBlueHiveState() {
        return blueHiveState;
    }

    public void setBlueHiveState(HiveState blueHiveState) {
        this.blueHiveState = blueHiveState;
    }

    public int getEstimatedRedTips() {
        return estimatedRedTips;
    }

    public void setEstimatedRedTips(int estimatedRedTips) {
        this.estimatedRedTips = estimatedRedTips;
    }

    public int getEstimatedBlueTips() {
        return estimatedBlueTips;
    }

    public void setEstimatedBlueTips(int estimatedBlueTips) {
        this.estimatedBlueTips = estimatedBlueTips;
    }

    public void incrementEstimatedRedTips() {
        this.estimatedRedTips++;
    }

    public void incrementEstimatedBlueTips() {
        this.estimatedBlueTips++;
    }
}
