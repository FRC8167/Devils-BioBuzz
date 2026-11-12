package org.firstinspires.ftc.teamcode.OpModes;


import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

//import org.firstinspires.ftc.teamcode.Commands.DriveCommand;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot.Robot;
//import org.firstinspires.ftc.teamcode.Commands.DriveCommand;


//@Disabled
@TeleOp(name="MainTeleOp", group="Competition")
public class MainTeleOp extends CommandOpMode {

    public GamepadEx driver;
    public GamepadEx operator;
    public ElapsedTime timer;
    private final Robot robot =   Robot.getInstance();
//    static TelemetryManager telemetryM;

    Telemetry dsTelemetry     = telemetry;
    Telemetry panelsTelemetry = PanelsTelemetry.INSTANCE.getFtcTelemetry();
    Telemetry joinedTelemetry = new JoinedTelemetry(panelsTelemetry, dsTelemetry);


    public static double current_velocity = 3200;
    public double increment = 25;


//    private Pose startPose;
//    private Pose autoEndPose;


    @Override
    public void initialize() {

        // Resets the command scheduler
        super.reset();

        //Initialize the robot
        try {
            robot.init(hardwareMap, Robot.OpModeType.TELEOP, new Pose(0,0,0));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        telemetry = joinedTelemetry;


//        robot.mecanumDrive.setDefaultCommand(robot.mecanumDrive.new DriveCommand(gamepad1));

        driver = new GamepadEx(gamepad1);
        operator = new GamepadEx(gamepad2);


        robot.mecanumDrive.setDefaultCmdGamepad(gamepad1);

        //******OPERATOR CONTROLS*****


//        operator.getGamepadButton(GamepadKeys.Button.A)
//                .whenPressed(new InstantCommand(robot.intake::IntakeIn));
//
//        operator.getGamepadButton(GamepadKeys.Button.B)
//                .whenPressed(new InstantCommand(robot.intake::IntakeOut));
        operator.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(robot.intake.setPowerInstantCommand(0.75))
                .whenReleased(robot.intake.setPowerInstantCommand(0));
        operator.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(robot.intake.setPowerInstantCommand(-0.2))
                .whenReleased(robot.intake.setPowerInstantCommand(0));
//        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
//                .whenPressed(() -> robot.shooterMotor.setVelocity(4000));
//        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
//                .whenPressed(() -> robot.shooterMotor.setVelocity(0));
        operator.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(robot.shooter.setVelocityRpmCommand(3000))
                .whenReleased(robot.shooter.setVelocityRpmCommand(0));

        operator.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(robot.shooter.setVelocityRpmCommand(-3000))
                .whenReleased(robot.shooter.setVelocityRpmCommand(0));


        operator.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(robot.gate.setPositionCommand(0.1))
                .whenReleased(robot.gate.setPositionCommand(0));



        //******DRIVER CONTROLS*****

        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(robot.mecanumDrive::enableSnailDrive)
                .whenReleased(robot.mecanumDrive::disableSnailDrive);

        //must be HELD DOWN to hold the position
        //operator can still use anything
        //once driver releases and moves joystick, the hold ends


        //driver-assisted shoot commands


//        driver.getGamepadButton(GamepadKeys.Button.X).whenPressed(
//            new InstantCommand(()-> robot.shooter.setVelocity(current_velocity)));

    }

    @Override
    public void run() {
        super.run();

        robot.follower.update();

        telemetry.addData("ShooterRPM", robot.shooter.getVelocityRPM());
        telemetry.addData("IntakeRPM", robot.intake.getVelocityRPM());
        telemetry.update();
    }
}