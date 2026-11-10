package org.firstinspires.ftc.teamcode.OpModes;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot.Robot;

@TeleOp(name="LimelightTestOpMode", group="Testing")
public class LimelightTestOpMode extends CommandOpMode {

    private final Robot robot = Robot.getInstance();
    public GamepadEx driver;

    Telemetry dsTelemetry = telemetry;
    Telemetry panelsTelemetry = PanelsTelemetry.INSTANCE.getFtcTelemetry();
    Telemetry joinedTelemetry = new JoinedTelemetry(panelsTelemetry, dsTelemetry);

    @Override
    public void initialize() {
        super.reset();

        try {
            robot.init(hardwareMap, Robot.OpModeType.TELEOP, new Pose(0, 0, 0));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        telemetry = joinedTelemetry;

        driver = new GamepadEx(gamepad1);

        // Set default drive command to gamepad 1
        robot.mecanumDrive.setDefaultCmdGamepad(gamepad1);

        // Snail drive on right bumper
        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(robot.mecanumDrive::enableSnailDrive)
                .whenReleased(robot.mecanumDrive::disableSnailDrive);

        // Take a snapshot with Limelight on button A
        driver.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(() -> {
                    if (robot.limeVision != null)
                        robot.limeVision.takePhoto("limelight_snapshot");
                });

        // Re-localize / reset follower pose using Limelight vision on button B
        driver.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(() -> {
                    if (robot.limeVision != null) {
                        boolean localized = robot.limeVision.updatePedroPose(robot.follower);
                        telemetry.addData("Localization", localized ? "Success" : "Failed (No Valid Pose)");
                    }
                });
    }

    @Override
    public void run() {
        super.run();

        // Update follower odometry
        robot.follower.update();

        // Update Limelight vision processing & hive tracking
        if (robot.limeVision != null) {
            robot.limeVision.update();

            // Add Limelight & Hive diagnostics to telemetry
            robot.limeVision.addDiagnostics(telemetry);
        } else {
            telemetry.addData("LimeVision", "Not available");
        }

        // Display Follower Telemetry
        telemetry.addLine();
        telemetry.addLine("Follower Telemetry");
        telemetry.addData("X", robot.follower.pose().x());
        telemetry.addData("Y",  robot.follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(robot.follower.pose().heading()));
        telemetry.addData("Follower Mode", robot.follower.mode());

        telemetry.update();
    }
}
