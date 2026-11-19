package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;



@TeleOp(name = "TestAll")
public class TestAll extends LinearOpMode {

    // Define the three states abd then the variable tracking thema
    private enum IntakeState {
        FORWARD,
        REVERSE,
        STOPPED
    }

    private IntakeState currentMode = IntakeState.STOPPED;

    @Override
    public void runOpMode() throws InterruptedException {

        //Flower Tube Wheel
//        CRServo intakeServoR = hardwareMap.get(CRServo.class, "intakeServoR");
//        intakeServoR.setDirection(DcMotorSimple.Direction.FORWARD);
//        double speed = 1;

        Servo pollenGate = hardwareMap.get(Servo.class, "pollenGate");
        // nectarGate = hardwareMap.get(Servo.class, "nectarGate");

        //Intake
        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        double fSpeed = 1.0;
        double rSpeed = -0.5;
        double noSpeed = 0.0;
        intakeMotor.setPower(0);

        //Shooter
        DcMotorEx nectarShooterMotor = hardwareMap.get(DcMotorEx.class, "nectarShooter");
        DcMotorEx pollenShooterMotor = hardwareMap.get(DcMotorEx.class, "pollenShooter");
        nectarShooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        nectarShooterMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        nectarShooterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        nectarShooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        pollenShooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        pollenShooterMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        pollenShooterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pollenShooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


//        pollenShooterMotor.setVelocityPIDFCoefficients(2, 0.5, 0, 13);
//        nectarShooterMotor.setVelocityPIDFCoefficients(2, 0.5, 0, 13);
        pollenShooterMotor.setVelocityPIDFCoefficients(2, 0, 0, 13);
        nectarShooterMotor.setVelocityPIDFCoefficients(2, 0, 0, 13);


        pollenShooterMotor.setPower(0);
        nectarShooterMotor.setPower(0);
        double pollenRPM = 0;
        double nectarRPM = 0;
        double targetPollenRPM = 3000;
        double targetNectarRPM = 3000;


        //Drivetrain
        MotorEx driveMotorRF = new MotorEx(hardwareMap, "RightFront").setCachingTolerance(0.01);
        MotorEx driveMotorLF = new MotorEx(hardwareMap, "LeftFront").setCachingTolerance(0.01);
        MotorEx driveMotorLR = new MotorEx(hardwareMap, "LeftRear").setCachingTolerance(0.01);
        MotorEx driveMotorRR = new MotorEx(hardwareMap, "RightRear").setCachingTolerance(0.01);
        driveMotorLF.setInverted(true);
        driveMotorLR.setInverted(true);
        driveMotorRF.setInverted(false);  //changed 10-27
        driveMotorRR.setInverted(false);


        driveMotorRF.setRunMode(Motor.RunMode.RawPower);
        driveMotorLF.setRunMode(Motor.RunMode.RawPower);
        driveMotorLR.setRunMode(Motor.RunMode.RawPower);
        driveMotorRR.setRunMode(Motor.RunMode.RawPower);

        //Initialize drive wheels to zero
        driveMotorRF.set(0.0);
        driveMotorLF.set(0.0);
        driveMotorRR.set(0.0);
        driveMotorLR.set(0.0);



        waitForStart();
        while (opModeIsActive()) {
            //Change input states according to gamepad buttons pressed
//            if (gamepad2.rightTriggerWasPressed()) {
//                nectarGate.setPosition(0.2);
//            }
//            if (gamepad2.rightTriggerWasReleased()) {
//                nectarGate.setPosition(0.0);
//            }
            if (gamepad2.leftTriggerWasPressed()) {
                pollenGate.setPosition(0.4);
            } if (gamepad2.leftTriggerWasReleased()) {
                pollenGate.setPosition(0.0);
            }
            if (gamepad2.aWasPressed()) {
                if (currentMode == IntakeState.FORWARD) {
                    currentMode = IntakeState.STOPPED; // If already running forward, stop it
                } else {
                    currentMode = IntakeState.FORWARD; // Otherwise, turn it forward
                }
            }
            else if (gamepad2.bWasPressed()) {
                if (currentMode == IntakeState.REVERSE) {
                    currentMode = IntakeState.STOPPED; // If already reversing, stop it
                } else {
                    currentMode = IntakeState.REVERSE; // Otherwise, turn it to reverse
                }
            }

            double maxRPM = 5500; // capped below 6000 RPM max

            if (gamepad2.rightBumperWasPressed()) {
                pollenRPM = Math.min(maxRPM, pollenRPM + 500);
                pollenShooterMotor.setVelocity(pollenRPM / 60.0 * 28.0);
            }

            if (gamepad2.leftBumperWasPressed()) {
                pollenRPM = Math.max(0, pollenRPM - 500);
                pollenShooterMotor.setVelocity(pollenRPM / 60.0 * 28.0);
            }

            if (gamepad2.dpadUpWasPressed()) {
                nectarRPM = Math.min(maxRPM, nectarRPM + 500);
                nectarShooterMotor.setVelocity(nectarRPM / 60.0 * 28.0);
            }

            if (gamepad2.dpadDownWasPressed()) {
                nectarRPM = Math.max(0, nectarRPM - 500);
                nectarShooterMotor.setVelocity(nectarRPM / 60.0 * 28.0);
            }

            if (gamepad2.xWasPressed()) {
                pollenRPM = 0;
                nectarRPM = 0;
                pollenShooterMotor.setVelocity(0);
                nectarShooterMotor.setVelocity(0);
            }


            telemetry.addData("Pollen Target RPM", pollenRPM);
            telemetry.addData("Pollen Actual RPM", pollenShooterMotor.getVelocity() / 28.0 * 60.0);

            telemetry.addData("Nectar Target RPM", nectarRPM);
            telemetry.addData("Nectar Actual RPM", nectarShooterMotor.getVelocity() / 28.0 * 60.0);
            telemetry.update();
            double drive = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x * 1.1;
            double turn = gamepad1.right_stick_x;


            double denominator = Math.max(Math.abs(drive) + Math.abs(strafe) + Math.abs(turn), 1);

            double frontLeftPower = (drive + strafe + turn)/denominator;
            double backLeftPower = (drive - strafe + turn)/denominator;
            double frontRightPower = (drive - strafe - turn)/denominator;
            double backRightPower = (drive + strafe - turn)/denominator;



            // Set the speeds to set the speeds according to the state
            if (currentMode == IntakeState.FORWARD) {
                intakeMotor.setPower(fSpeed);
            } else if (currentMode == IntakeState.REVERSE) {
                intakeMotor.setPower(rSpeed);
            } else {
                intakeMotor.setPower(noSpeed);
            }


            driveMotorRF.set(frontRightPower);
            driveMotorLF.set(frontLeftPower);
            driveMotorRR.set(backRightPower);
            driveMotorLR.set(backLeftPower);

        }
    }
}