package org.firstinspires.ftc.teamcode.Robot;

import com.pedropathing.math.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;
import org.firstinspires.ftc.teamcode.SubSystems.FieldState;
import org.firstinspires.ftc.teamcode.SubSystems.Gate;
import org.firstinspires.ftc.teamcode.SubSystems.Intake;
import org.firstinspires.ftc.teamcode.SubSystems.LimeVision;
import org.firstinspires.ftc.teamcode.SubSystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.SubSystems.Shooter;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import com.pedropathing.follower.Follower;

import java.util.List;
import java.util.Locale;

public class Robot extends com.seattlesolvers.solverslib.command.Robot {

    private static final Robot instance = new Robot();
    public static Robot getInstance() {
        return instance;
    }

    public enum OpModeType {
        AUTO,
        TELEOP
    }

    public enum Alliance {
        RED,
        BLUE,
        UNSPECIFIED
    }

    private static Alliance alliance = Alliance.UNSPECIFIED;

    public Telemetry telemetry;

    public OpModeType OP_MODE_TYPE;
    static List<LynxModule> ctrlHubs;

    public GoBildaPinpointDriver pinpoint;

    public Follower follower;
    public MecanumDrive mecanumDrive;
    public Intake intake;
    public Shooter shooter;
    public Gate gate;
    public LimeVision limeVision;

    public void init(HardwareMap hardwareMap, OpModeType opModeType, Pose startPose) throws InterruptedException {
        OP_MODE_TYPE = opModeType;

        // Reset FieldState if running in Autonomous
        if (opModeType == OpModeType.AUTO) {
            FieldState.getInstance().reset();
        }

        // Hardware
        MotorEx driveMotorRF = new MotorEx(hardwareMap, "RightFront").setCachingTolerance(0.01);
        MotorEx driveMotorLF = new MotorEx(hardwareMap, "LeftFront").setCachingTolerance(0.01);
        MotorEx driveMotorLR = new MotorEx(hardwareMap, "LeftRear").setCachingTolerance(0.01);
        MotorEx driveMotorRR = new MotorEx(hardwareMap, "RightRear").setCachingTolerance(0.01);

        ctrlHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : ctrlHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        // Instantiate Subsystems
        mecanumDrive = new MecanumDrive(driveMotorLF, driveMotorLR, driveMotorRF, driveMotorRR);
        intake       = new Intake(hardwareMap.get(DcMotorEx.class, "Intake"));
        shooter      = new Shooter(hardwareMap.get(DcMotorEx.class, "Shooter"));
        gate         = new Gate(hardwareMap.get(Servo.class, "DropServo"));

        // Initialize LimeVision subsystem safely
        try {
            Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");
            limeVision = new LimeVision(limelight);
        } catch (Exception e) {
            // Limelight might not be present or configured in hardware map during bench testing
        }

        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();
    }

    public static String getControlHubVoltage() {
        if (ctrlHubs != null && !ctrlHubs.isEmpty()) {
            return String.format(Locale.getDefault(), "%.2f V", ctrlHubs.get(0).getInputVoltage(VoltageUnit.VOLTS));
        }
        return "N/A";
    }

    public static String getExpansionHubVoltage() {
        if (ctrlHubs != null && ctrlHubs.size() > 1) {
            return String.format(Locale.getDefault(), "%.2f V", ctrlHubs.get(1).getInputVoltage(VoltageUnit.VOLTS));
        }
        return "N/A";
    }

    public void initHasMovement() {
        // TODO what goes here??
    }

    public void setAlliance(Alliance ally) {
        alliance = ally;
    }

    public Alliance getAlliance() {
        return alliance;
    }
}
