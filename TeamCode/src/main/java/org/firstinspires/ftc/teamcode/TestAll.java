package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.teamcode.Cogintilities.DefaultMotorInfo;
import org.firstinspires.ftc.teamcode.Cogintilities.MotorInformation;


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
        CRServo intakeServoR = hardwareMap.get(CRServo.class, "intakeServoR");
        intakeServoR.setDirection(DcMotorSimple.Direction.FORWARD);
        double speed = 1;

        //Intake
        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "Intake");
        double fSpeed = 0.75;
        double rSpeed = -0.5;
        double noSpeed = 0.0;
        intakeMotor.setPower(0);

        //Shooter
        DcMotorEx shooterMotor = hardwareMap.get(DcMotorEx.class, "Shooter");
        double shooterSpeed;
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MotorInformation.adjustMotor(shooterMotor, DefaultMotorInfo.GOBILDA_6000RPM);

        shooterMotor.setPower(0);

        //Drivetrain
        MotorEx driveMotorRF = new MotorEx(hardwareMap, "RightFront").setCachingTolerance(0.01);
        MotorEx driveMotorLF = new MotorEx(hardwareMap, "LeftFront").setCachingTolerance(0.01);
        MotorEx driveMotorLR = new MotorEx(hardwareMap, "LeftRear").setCachingTolerance(0.01);
        MotorEx driveMotorRR = new MotorEx(hardwareMap, "RightRear").setCachingTolerance(0.01);
        driveMotorLF.setInverted(true);
        driveMotorLR.setInverted(true);
        driveMotorRF.setInverted(false);
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
            if (gamepad2.right_bumper) {
                intakeServoR.setPower(speed);
            } else if (gamepad2.left_bumper) {
                intakeServoR.setPower(-speed);
            } else {
                intakeServoR.setPower(0);
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
            shooterSpeed = -gamepad2.right_stick_y;
            shooterMotor.setPower(shooterSpeed);

            telemetry.addData("Speed", speed);
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