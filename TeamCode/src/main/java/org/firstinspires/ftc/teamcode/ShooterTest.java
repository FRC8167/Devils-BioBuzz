package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.teamcode.Cogintilities.DefaultMotorInfo;
import org.firstinspires.ftc.teamcode.Cogintilities.MotorInformation;

@TeleOp(name = "ShooterTestTeleop")
public class ShooterTest extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {

        DcMotorEx shooterMotor = hardwareMap.get(DcMotorEx.class, "Shooter");
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        MotorInformation.adjustMotor(shooterMotor, DefaultMotorInfo.GOBILDA_1620RPM);

        double speed;

        waitForStart();
        while (opModeIsActive()) {
            //Change input states according to gamepad buttons pressed
            if (gamepad1.a) {
                speed = 0.75;
            }
            else if (gamepad1.b) {
                speed = -0.5;
            }
            else {
                speed = 0;
            }

            shooterMotor.setPower(speed);
        }
    }

}

