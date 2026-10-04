package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class Intake extends SubsystemBase implements TeamConstants {
    private final MotorEx intakeMotor;


    public Intake(MotorEx motor) {
        this.intakeMotor = motor;

//        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
//        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
//        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor.setRunMode(Motor.RunMode.RawPower);
//        intakeMotor.setPower(0);
        intakeMotor.set(0);
    }


    public void setMotorPower(double power) {
//        intakeMotor.setPower(power);
        intakeMotor.set(power);

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