package org.firstinspires.ftc.teamcode.OpModes;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

//import org.firstinspires.ftc.teamcode.Commands.DriveCommand;
import org.firstinspires.ftc.teamcode.Commands.DriveCommand;
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


    public static double current_velocity = 3200;
    public double increment = 25;


//    private Pose startPose;
//    private Pose autoEndPose;


    @Override
    public void initialize() {

        // Must-have for all opModes
//        Robot.OP_MODE_TYPE = Robot.OpModeType.TELEOP;
        // Resets the command scheduler
        super.reset();

        //Initialize the robot
        try {
            robot.init(hardwareMap);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        robot.mecanumDrive.setDefaultCommand(new DriveCommand(robot.mecanumDrive, gamepad1));



        driver = new GamepadEx(gamepad1);
        operator = new GamepadEx(gamepad2);


        //******OPERATOR CONTROLS*****


//        operator.getGamepadButton(GamepadKeys.Button.A)
//                .whenPressed(new InstantCommand(robot.intake::IntakeIn));
//
//        operator.getGamepadButton(GamepadKeys.Button.B)
//                .whenPressed(new InstantCommand(robot.intake::IntakeOut));
        driver.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(() -> robot.intakeMotor.setVelocity(100))
                .whenReleased(() -> robot.intakeMotor.setVelocity(0));
        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(() -> robot.shooterMotor.setVelocity(4000));
        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(() -> robot.shooterMotor.setVelocity(0));

        driver.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(() -> robot.dropServo.setPosition(0.3))
                .whenReleased(() -> robot.dropServo.setPosition(0));



        //******DRIVER CONTROLS*****

        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new InstantCommand(robot.mecanumDrive::enableSnailDrive))
                .whenReleased(new InstantCommand(robot.mecanumDrive::disableSnailDrive));

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


    }
}