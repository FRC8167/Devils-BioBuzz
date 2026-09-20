package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class Intake implements TeamConstants {
    private final DcMotorEx intakeMotor;


    public Intake(DcMotorEx motor) {
        this.intakeMotor = motor;

        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor.setPower(0);
    }


    public void setMotorPower(double power) {
        intakeMotor.setPower(power);
    }

    public void IntakeIn() {
        setMotorPower(0.75);
    }

    public void IntakeOut() {
        setMotorPower(-0.75);
    }


    public void IntakeOff() {
        setMotorPower(0);
    }



}