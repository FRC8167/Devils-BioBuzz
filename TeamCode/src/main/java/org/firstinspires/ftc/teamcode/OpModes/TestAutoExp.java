package org.firstinspires.ftc.teamcode.OpModes;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot.Robot;

@Disabled
@Autonomous(name = "TestAutoExp")
public class TestAutoExp extends CommandOpMode {

    private final Robot robot =   Robot.getInstance();

    Telemetry dsTelemetry     = telemetry;
    Telemetry panelsTelemetry = PanelsTelemetry.INSTANCE.getFtcTelemetry();
    Telemetry joinedTelemetry = new JoinedTelemetry(panelsTelemetry, dsTelemetry);

    private final PoseFactory poseFactory = PoseFactory.degrees();
    // Poses
    private final Pose startPose = poseFactory.of(54.5, 9.92126, 90);

    private final Pose pickupPose = poseFactory.of(3+8.5, 9.92126+4, 90);

    private final Pose pickupPose2 = poseFactory.of(3+8.5+6, 9.92126+2, 90);

    private final Pose pickupControlPose = poseFactory.of(20, 24, 90);


//    private final Pose pickupTurnPose = poseFactory.of(4+8.5, 9.92126+4, 105); //105

//    private final Pose scorePose = poseFactory.of(48, 48, 90);
    private final Pose controlPose1 = poseFactory.of(60, 24, 90);
    private final Pose controlPose2 = poseFactory.of(24, 24, 90);
    private final Pose parkPose = poseFactory.of(12, 96, 90);

//    private Path startToScore() {
//        return Paths.line(startPose, scorePose).linear(startPose, scorePose);
//    }
    private Path pickupA() {
        return Paths.curve(startPose, pickupControlPose, pickupPose).constant(startPose);
    }

    private Path pickupB() {
        return Paths.line(pickupPose, pickupPose2).constant(pickupPose);
    }

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
                    new InstantCommand(),

                    new FollowPathCommand(robot.follower, pickupA()),
                    robot.intake.setPowerInstantCommand(1),
                    new FollowPathCommand(robot.follower, pickupB())
//                    robot.shooter.setVelocityShootCommand(),
//
//                    robot.gate.openGateCommand(),
//                    new WaitCommand(300), //TODO: Test timing
//                    robot.shooter.setVelocityShootCommand(),
//                    robot.gate.closeGateCommand(),
//
//                    robot.intake.setPowerCommand(0.5, 0.05),
//
//                    new WaitCommand(800),
//                    robot.gate.openGateCommand(),
//                    new WaitCommand(300), //TODO: Test timing
//                    robot.shooter.setVelocityShootCommand(),
//                    robot.gate.closeGateCommand(),
//
//                    robot.intake.setPowerCommand(0.5, 0.4),
//
//                    new WaitCommand(800),
//                    robot.gate.openGateCommand(),
//                    new WaitCommand(300), //TODO: Test timing
//                    robot.shooter.setVelocityShootCommand(),
//                    robot.gate.closeGateCommand(),
//
//                    robot.intake.setPowerCommand(-0.5, 0.1),
//                    robot.intake.setPowerCommand(1, 1),
//
//                    new WaitCommand(800),
//                    robot.gate.openGateCommand(),
//                    new WaitCommand(1000),
//
//                    new ParallelCommandGroup(
//                        robot.gate.closeGateCommand(),
//                        robot.shooter.setVelocityRpmInstantCommand(0),
//                        new FollowPathCommand(robot.follower, park())
//                    )
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
