package org.firstinspires.ftc.teamcode.OpModes;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot.Robot;

@Autonomous(name = "TestAuto")
public class TestAuto extends CommandOpMode {

    private final Robot robot =   Robot.getInstance();

    Telemetry dsTelemetry     = telemetry;
    Telemetry panelsTelemetry = PanelsTelemetry.INSTANCE.getFtcTelemetry();
    Telemetry joinedTelemetry = new JoinedTelemetry(panelsTelemetry, dsTelemetry);

    private final PoseFactory poseFactory = PoseFactory.degrees();
    // Poses
    private final Pose startPose = poseFactory.of(60, 9.92126, 90);
//    private final Pose scorePose = poseFactory.of(48, 48, 90);
    private final Pose controlPose1 = poseFactory.of(60, 24, 90);
    private final Pose controlPose2 = poseFactory.of(24, 24, 90);
    private final Pose parkPose = poseFactory.of(12, 96, 90);

//    private Path startToScore() {
//        return Paths.line(startPose, scorePose).linear(startPose, scorePose);
//    }
    private Path park(){
        return Paths.curve(startPose, controlPose1, controlPose2, parkPose).constant(startPose);
    }

    @Override
    public void initialize() {

        // Resets the command scheduler
        super.reset();

        //Initialize the robot
        try {
            robot.init(hardwareMap, Robot.OpModeType.AUTO, startPose);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        schedule(
                new SequentialCommandGroup(
                    new FollowPathCommand(robot.follower, park())
                )
        );
    }

    @Override
    public void run() {
        super.run();

        robot.follower.update();

        telemetry.addData("X", robot.follower.pose().x());
        telemetry.addData("Y", robot.follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(robot.follower.pose().heading()));
        telemetry.addData("Follower Mode", robot.follower.mode());
        telemetry.update();

        telemetry.addData("ShooterRPM", robot.shooter.getVelocityRPM());
        telemetry.addData("IntakeRPM", robot.intake.getVelocityRPM());
        telemetry.update();
    }
}
