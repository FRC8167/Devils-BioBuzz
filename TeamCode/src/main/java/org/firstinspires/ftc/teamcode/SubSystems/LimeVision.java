package org.firstinspires.ftc.teamcode.SubSystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Updated Limelight subsystem for FTC BioBuzz / Into The Deep season.
 * Integrates with HiveVisionProcessor for IMU-independent 3D field positioning
 * and pivoting hive state / tip tracking, including built-in diagnostics.
 */
public class LimeVision {
    private final Limelight3A limelight;
    private final HiveVisionProcessor hiveVisionProcessor;
    private int pipeline;
    private static final double LIME_VISION_STALENESS_TOL = 500.0; // ms

    public LimeVision(Limelight3A limelight) {
        this.limelight = limelight;
        this.hiveVisionProcessor = new HiveVisionProcessor();
        this.limelight.setPollRateHz(100);
        this.limelight.start();
        this.limelight.pipelineSwitch(0);
        this.pipeline = 0;
    }

    public void setPipeline(int pipeline) {
        this.limelight.pipelineSwitch(pipeline);
        this.pipeline = pipeline;
    }

    public void start() {
        limelight.start();
    }

    public void stop() {
        limelight.stop();
    }

    public LLResult getResult() {
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid() && result.getPipelineIndex() == pipeline && result.getStaleness() <= LIME_VISION_STALENESS_TOL) {
            return result;
        }
        return null;
    }

    /**
     * Call continuously in teleop/autonomous loops to update vision tracking
     * and pivot hive states.
     */
    public void update() {
        LLResult result = getResult();
        if (result != null) {
            hiveVisionProcessor.updateGlobalTracking(result);
        }
    }

    /**
     * Updates the Pedro Pathing follower pose using the latest vision calculation.
     */
    public boolean updatePedroPose(Follower follower) {
        return hiveVisionProcessor.updatePedroPose(follower);
    }

    public HiveVisionProcessor getHiveVisionProcessor() {
        return hiveVisionProcessor;
    }

    public FieldState.HiveState getRedHiveState() {
        return FieldState.getInstance().getRedHiveState();
    }

    public FieldState.HiveState getBlueHiveState() {
        return FieldState.getInstance().getBlueHiveState();
    }

    public int getEstimatedRedTips() {
        return FieldState.getInstance().getEstimatedRedTips();
    }

    public int getEstimatedBlueTips() {
        return FieldState.getInstance().getEstimatedBlueTips();
    }

    public boolean hasValidPose() {
        return hiveVisionProcessor.hasValidPose();
    }

    public double getLatestVisionX() {
        return hiveVisionProcessor.getLatestVisionX();
    }

    public double getLatestVisionY() {
        return hiveVisionProcessor.getLatestVisionY();
    }

    public double getLatestVisionHeadingRad() {
        return hiveVisionProcessor.getLatestVisionHeadingRad();
    }

    public void takePhoto(String snapshotName) {
        limelight.captureSnapshot(snapshotName);
    }

    /**
     * Adds Limelight debugging and diagnostic telemetry.
     */
    public void addDiagnostics(Telemetry tel) {
        if (tel != null) {
            tel.addData("LL Active Result", getResult() != null ? "Valid" : "None/Stale");
            tel.addData("Vision Pose Valid", hasValidPose());
            tel.addData("Vision X (in)", "%.2f", getLatestVisionX());
            tel.addData("Vision Y (in)", "%.2f", getLatestVisionY());
            tel.addData("Vision Heading (°)", "%.2f", Math.toDegrees(getLatestVisionHeadingRad()));
            tel.addData("Red Hive State", getRedHiveState());
            tel.addData("Blue Hive State", getBlueHiveState());
            tel.addData("Est. Red Tips", getEstimatedRedTips());
            tel.addData("Est. Blue Tips", getEstimatedBlueTips());
        }
    }
}
