package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "IntakeTestTeleop")
public class IntakeTest extends LinearOpMode {

    // Define the three states abd then the variable tracking thema
    private enum IntakeState {
        FORWARD,
        REVERSE,
        STOPPED
    }

    private IntakeState currentMode = IntakeState.STOPPED;

    @Override
    public void runOpMode() throws InterruptedException {

        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "Intake");
        double fSpeed = 0.75;
        double rSpeed = 0.5;
        double noSpeed = 0.0;

        waitForStart();
        while (opModeIsActive()){
            //Change input states according to gamepad buttons pressed
            if (gamepad1.aWasPressed()) {
                if (currentMode == IntakeState.FORWARD) {
                    currentMode = IntakeState.STOPPED; // If already running forward, stop it
                } else {
                    currentMode = IntakeState.FORWARD; // Otherwise, turn it forward
                }
            }
            else if (gamepad1.bWasPressed()) {
                if (currentMode == IntakeState.REVERSE) {
                    currentMode = IntakeState.STOPPED; // If already reversing, stop it
                } else {
                    currentMode = IntakeState.REVERSE; // Otherwise, turn it to reverse
                }
            }

            // Set the speeds to set the speeds according to the state
            if (currentMode == IntakeState.FORWARD) {
                intakeMotor.setPower(fSpeed);
            } else if (currentMode == IntakeState.REVERSE) {
                intakeMotor.setPower(rSpeed);
            } else {
                intakeMotor.setPower(noSpeed);
            }
        }
    }
}
