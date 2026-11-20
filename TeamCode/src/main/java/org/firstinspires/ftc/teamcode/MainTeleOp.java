package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Commands.DriveCommand;
import org.firstinspires.ftc.teamcode.Robot.Robot;


//@Disabled
@TeleOp(name="MainTeleOp", group="Competition")
public class MainTeleOp extends CommandOpMode {

    public GamepadEx driver;
    public GamepadEx operator;
    public ElapsedTime timer;
    private final Robot robot =   Robot.getInstance();
//    static TelemetryManager telemetryM;
//    private Pose startPose;
//    private Pose autoEndPose;


    @Override
    public void initialize() {

        // Must have for all opModes
        Robot.OP_MODE_TYPE = Robot.OpModeType.TELEOP;
        robot.pollenGate.setPosition(0.4);
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
        operator.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(new InstantCommand(robot.intake::forward));

        operator.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(new InstantCommand(robot.intake::reverse));

        operator.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new InstantCommand(() -> robot.pollenShooter.setMotorSpeed(3000), robot.pollenShooter))
                .whenReleased(new InstantCommand(() -> robot.pollenShooter.setMotorSpeed(0), robot.pollenShooter));

        operator.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new InstantCommand(() -> robot.nectarShooter.setMotorSpeed(3000), robot.nectarShooter))
                .whenReleased(new InstantCommand(() -> robot.nectarShooter.setMotorSpeed(0), robot.nectarShooter));

        if (operator.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.5) {
            robot.pollenGate.setPosition(0.4);
        } else {
            robot.pollenGate.setPosition(0.0);
        }


        //******DRIVER CONTROLS*****

        driver.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(new InstantCommand(robot.mecanumDrive::enableSnailDrive))
                .whenReleased(new InstantCommand(robot.mecanumDrive::disableSnailDrive));
    }

    @Override
    public void run() {
        super.run();


    }
}