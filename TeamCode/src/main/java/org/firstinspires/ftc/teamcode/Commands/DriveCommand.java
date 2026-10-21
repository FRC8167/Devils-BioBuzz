package org.firstinspires.ftc.teamcode.Commands;

import com.seattlesolvers.solverslib.command.CommandBase;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.SubSystems.MecanumDrive;

public class DriveCommand extends CommandBase {

    private final MecanumDrive drive;
    private final Gamepad gamepad;

    public DriveCommand(MecanumDrive drive, Gamepad gamepad) {
        this.drive = drive;
        this.gamepad = gamepad;
        addRequirements(drive);
    }


    @Override
    public void execute() {

        // Read sticks
        double forward = -gamepad.left_stick_y;  // forward/backward
        double strafe  = gamepad.left_stick_x;  // left/right
        double turn    = gamepad.right_stick_x; // rotation

        // Apply deadzone
        if (Math.abs(forward) < 0.05) forward = 0;
        if (Math.abs(strafe) < 0.05) strafe = 0;
        if (Math.abs(turn) < 0.05) turn = 0;

        // Call subsystem drive method
        drive.drive(forward, strafe, turn);
    }


    @Override
    public boolean isFinished() {
        return false; // TeleOp runs continuously
    }


    @Override
    public void end(boolean interrupted) {
        drive.drive(0,0,0); // stop motors
    }
}
