package org.firstinspires.ftc.teamcode.SubSystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.firstinspires.ftc.teamcode.SubSystems.FieldState.HiveColor;
import static org.firstinspires.ftc.teamcode.SubSystems.FieldState.HiveState;

/**
 * IMU-independent 3D Vision Tracking Processor designed for FTC Into The Deep / BIOBUZZ.
 * Calculates absolute field pose, tracks Hive States, updates FieldState singleton,
 * and updates a Pedro Pathing Follower using a bottom-left origin coordinate frame.
 */
public class HiveVisionProcessor {

    public static class HiveConfig {
        public final HiveColor color;
        public final boolean isOppositeFace;
        public final double offsetAlongShaft;
        public final double shaftXFieldCoord;

        public HiveConfig(HiveColor color, boolean isOppositeFace, double offsetAlongShaft, double shaftXFieldCoord) {
            this.color = color;
            this.isOppositeFace = isOppositeFace;
            this.offsetAlongShaft = offsetAlongShaft;
            this.shaftXFieldCoord = shaftXFieldCoord;
        }
    }

    private static final double METERS_TO_INCHES = 39.3700787;
    private static final double HIVE_SPACING_X = 25.5; // Center-to-center distance between Red and Blue shafts
    private static final double RED_SHAFT_FIELD_X = -(HIVE_SPACING_X / 2.0); // -12.75 in
    private static final double BLUE_SHAFT_FIELD_X = (HIVE_SPACING_X / 2.0);  // +12.75 in
    private static final double TOWER_FIELD_Y = 0.00;
    private static final double TOWER_SHAFT_FIELD_ANGLE_RAD = Math.PI / 2.0;  // Shafts run parallel to Y-Axis

    private static final double HORIZONTAL_DISTANCE = 18.84 - 9.938;         // ~8.902 in
    private static final double VERTICAL_DROP         = 43.95 - 42.451532;     // ~1.498 in

    private static final double TARGET_STATE_MAGNITUDE = 30.0;
    private static final double STATE_TOLERANCE = 8.0;

    private final Map<Integer, HiveConfig> tagMap = new HashMap<>();

    // --- Stored Fields for External Access ---
    private double latestVisionX = 0.0;
    private double latestVisionY = 0.0;
    private double latestVisionHeadingRad = 0.0;
    private boolean hasValidPose = false;

    public HiveVisionProcessor() {
        // --- RED HIVE CONFIGURATION (IDs 30-37) ---
        tagMap.put(30, new HiveConfig(HiveColor.RED, false,  10.0, RED_SHAFT_FIELD_X));
        tagMap.put(31, new HiveConfig(HiveColor.RED, false,   3.5, RED_SHAFT_FIELD_X));
        tagMap.put(32, new HiveConfig(HiveColor.RED, false,  -3.5, RED_SHAFT_FIELD_X));
        tagMap.put(33, new HiveConfig(HiveColor.RED, false, -10.0, RED_SHAFT_FIELD_X));
        tagMap.put(34, new HiveConfig(HiveColor.RED, true,  -10.0, RED_SHAFT_FIELD_X));
        tagMap.put(35, new HiveConfig(HiveColor.RED, true,   -3.5, RED_SHAFT_FIELD_X));
        tagMap.put(36, new HiveConfig(HiveColor.RED, true,    3.5, RED_SHAFT_FIELD_X));
        tagMap.put(37, new HiveConfig(HiveColor.RED, true,   10.0, RED_SHAFT_FIELD_X));

        // --- BLUE HIVE CONFIGURATION (IDs 38-45) ---
        tagMap.put(38, new HiveConfig(HiveColor.BLUE, true, -10.0, BLUE_SHAFT_FIELD_X));
        tagMap.put(39, new HiveConfig(HiveColor.BLUE, true,  -3.5, BLUE_SHAFT_FIELD_X));
        tagMap.put(40, new HiveConfig(HiveColor.BLUE, true,   3.5, BLUE_SHAFT_FIELD_X));
        tagMap.put(41, new HiveConfig(HiveColor.BLUE, true,  10.0, BLUE_SHAFT_FIELD_X));
        tagMap.put(42, new HiveConfig(HiveColor.BLUE, false, 10.0, BLUE_SHAFT_FIELD_X));
        tagMap.put(43, new HiveConfig(HiveColor.BLUE, false,  3.5, BLUE_SHAFT_FIELD_X));
        tagMap.put(44, new HiveConfig(HiveColor.BLUE, false, -3.5, BLUE_SHAFT_FIELD_X));
        tagMap.put(45, new HiveConfig(HiveColor.BLUE, false,-10.0, BLUE_SHAFT_FIELD_X));
    }

    /**
     * Call continuously in your loop to parse Limelight frames and refresh stored states and poses.
     */
    public void updateGlobalTracking(LLResult result) {
        if (result == null || !result.isValid() || result.getFiducialResults() == null) return;

        List<LLResultTypes.FiducialResult> redVisibleTags = new ArrayList<>();
        List<LLResultTypes.FiducialResult> blueVisibleTags = new ArrayList<>();
        List<Double> redNormalizedAngles = new ArrayList<>();
        List<Double> blueNormalizedAngles = new ArrayList<>();

        for (LLResultTypes.FiducialResult f : result.getFiducialResults()) {
            HiveConfig cfg = tagMap.get(f.getFiducialId());
            if (cfg == null) continue;

            double rawPitch = f.getTargetPoseRobotSpace().getOrientation().getPitch();
            double normalizedAngle = cfg.isOppositeFace ? -rawPitch : rawPitch;

            if (normalizedAngle >= -60.0 && normalizedAngle <= 60.0) {
                if (cfg.color == HiveColor.RED) {
                    redVisibleTags.add(f);
                    redNormalizedAngles.add(normalizedAngle);
                } else {
                    blueVisibleTags.add(f);
                    blueNormalizedAngles.add(normalizedAngle);
                }
            }
        }

        double redAvgAngle  = redNormalizedAngles.stream().mapToDouble(a -> a).average().orElse(Double.NaN);
        double blueAvgAngle = blueNormalizedAngles.stream().mapToDouble(a -> a).average().orElse(Double.NaN);

        if (!Double.isNaN(redAvgAngle)) updateRedState(resolveHiveState(redAvgAngle));
        if (!Double.isNaN(blueAvgAngle)) updateBlueState(resolveHiveState(blueAvgAngle));

        if (redVisibleTags.size() >= 2) {
            calculatePoseFromShaft(redVisibleTags, redAvgAngle, RED_SHAFT_FIELD_X);
        } else if (blueVisibleTags.size() >= 2) {
            calculatePoseFromShaft(blueVisibleTags, blueAvgAngle, BLUE_SHAFT_FIELD_X);
        }
    }

    private void updateRedState(HiveState newState) {
        HiveState currentState = FieldState.getInstance().getRedHiveState();
        if (currentState != newState) {
            if ((currentState == HiveState.STARTING && newState == HiveState.REVERSED_TIPPED) ||
                (currentState == HiveState.REVERSED_TIPPED && newState == HiveState.STARTING)) {
                FieldState.getInstance().incrementEstimatedRedTips();
            }
            FieldState.getInstance().setRedHiveState(newState);
        }
    }

    private void updateBlueState(HiveState newState) {
        HiveState currentState = FieldState.getInstance().getBlueHiveState();
        if (currentState != newState) {
            if ((currentState == HiveState.STARTING && newState == HiveState.REVERSED_TIPPED) ||
                (currentState == HiveState.REVERSED_TIPPED && newState == HiveState.STARTING)) {
                FieldState.getInstance().incrementEstimatedBlueTips();
            }
            FieldState.getInstance().setBlueHiveState(newState);
        }
    }

    private void calculatePoseFromShaft(List<LLResultTypes.FiducialResult> visibleTags, double activeHiveAngleDeg, double activeFieldShaftX) {
        if (visibleTags.size() < 2) return;

        visibleTags.sort(Comparator.comparingDouble(f -> Objects.requireNonNull(tagMap.get(f.getFiducialId())).offsetAlongShaft));

        LLResultTypes.FiducialResult first = visibleTags.get(0);
        LLResultTypes.FiducialResult last  = visibleTags.get(visibleTags.size() - 1);

        double spanAlongShaft = Objects.requireNonNull(tagMap.get(last.getFiducialId())).offsetAlongShaft
                - Objects.requireNonNull(tagMap.get(first.getFiducialId())).offsetAlongShaft;

        if (spanAlongShaft > 2.0) {
            double xFirstIn = first.getTargetPoseRobotSpace().getPosition().x * METERS_TO_INCHES;
            double yFirstIn = first.getTargetPoseRobotSpace().getPosition().y * METERS_TO_INCHES;
            double xLastIn  = last.getTargetPoseRobotSpace().getPosition().x * METERS_TO_INCHES;
            double yLastIn  = last.getTargetPoseRobotSpace().getPosition().y * METERS_TO_INCHES;

            double hx = xLastIn - xFirstIn;
            double hy = yLastIn - yFirstIn;
            double hNorm = Math.hypot(hx, hy);

            if (hNorm < 1e-5) return;

            double ux = hx / hNorm;
            double uy = hy / hNorm;
            double perpX = -uy;
            double perpY = ux;

            double shaftRobotAngleRad = Math.atan2(-hy, hx);
            double robotHeadingRad = normalizeAngle(TOWER_SHAFT_FIELD_ANGLE_RAD - shaftRobotAngleRad);

            double sumTowerX = 0, sumTowerY = 0;
            int validProjectionCount = 0;

            for (LLResultTypes.FiducialResult f : visibleTags) {
                HiveConfig cfg = Objects.requireNonNull(tagMap.get(f.getFiducialId()));
                if (Double.isNaN(activeHiveAngleDeg)) continue;

                double magnitudeCorrectedAngle = evaluateSignedAngle(activeHiveAngleDeg);
                double angleRad = Math.toRadians(magnitudeCorrectedAngle);

                double xIn = f.getTargetPoseRobotSpace().getPosition().x * METERS_TO_INCHES;
                double yIn = f.getTargetPoseRobotSpace().getPosition().y * METERS_TO_INCHES;

                double swingSign = cfg.isOppositeFace ? -1.0 : 1.0;
                double relativePerpDistance = swingSign * (HORIZONTAL_DISTANCE * Math.cos(angleRad) - VERTICAL_DROP * Math.sin(angleRad));

                sumTowerX += xIn - (ux * cfg.offsetAlongShaft) - (perpX * relativePerpDistance);
                sumTowerY += yIn - (uy * cfg.offsetAlongShaft) - (perpY * relativePerpDistance);
                validProjectionCount++;
            }

            if (validProjectionCount > 0) {
                double rToHingeFwd = sumTowerX / validProjectionCount;
                double rToHingeLeft = -(sumTowerY / validProjectionCount);

                double cosH = Math.cos(robotHeadingRad);
                double sinH = Math.sin(robotHeadingRad);

                double fieldToHingeX = (rToHingeFwd * cosH) - (rToHingeLeft * sinH);
                double fieldToHingeY = (rToHingeFwd * sinH) + (rToHingeLeft * cosH);

                latestVisionX = activeFieldShaftX - fieldToHingeX;
                latestVisionY = TOWER_FIELD_Y - fieldToHingeY;

                latestVisionHeadingRad = robotHeadingRad;
                hasValidPose = true;
            }
        }
    }

    /**
     * Manually call this method whenever you want to update Pedro Pathing using the latest stored vision pose.
     * Automatically converts center-origin field coordinates into Pedro Pathing's bottom-left origin frame.
     */
    public boolean updatePedroPose(Follower follower) {
        if (follower != null && hasValidPose) {
            // Shift center-origin (0,0 center) to Pedro's bottom-left corner origin (0,0 corner)
            double pedroX = latestVisionX + 72.0;
            double pedroY = latestVisionY + 72.0;

            double pedroHeading = normalizeAngle(latestVisionHeadingRad);

            follower.setPose(new Pose(pedroX, pedroY, pedroHeading));
            return true;
        }
        return false;
    }

    private double evaluateSignedAngle(double rawAngle) {
        HiveState state = resolveHiveState(rawAngle);
        if (state == HiveState.REVERSED_TIPPED) {
            return -TARGET_STATE_MAGNITUDE;
        } else if (state == HiveState.STARTING) {
            return TARGET_STATE_MAGNITUDE;
        }
        return rawAngle;
    }

    public HiveState resolveHiveState(double angleDeg) {
        if (Math.abs(Math.abs(angleDeg) - TARGET_STATE_MAGNITUDE) <= STATE_TOLERANCE) {
            return (angleDeg < 0) ? HiveState.REVERSED_TIPPED : HiveState.STARTING;
        }
        return HiveState.IN_BETWEEN_MOVING;
    }

    private double normalizeAngle(double angleRad) {
        while (angleRad > Math.PI) angleRad -= 2.0 * Math.PI;
        while (angleRad < -Math.PI) angleRad += 2.0 * Math.PI;
        return angleRad;
    }

    // --- Public Getters for Field State Trackers & Telemetry ---

    public HiveState getRedHiveState() {
        return FieldState.getInstance().getRedHiveState();
    }

    public HiveState getBlueHiveState() {
        return FieldState.getInstance().getBlueHiveState();
    }

    public boolean hasValidPose() {
        return hasValidPose;
    }

    public double getLatestVisionX() {
        return latestVisionX;
    }

    public double getLatestVisionY() {
        return latestVisionY;
    }

    public double getLatestVisionHeadingRad() {
        return latestVisionHeadingRad;
    }
}
