package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.teamcode.Cogintilities.DefaultMotorInfo;
import org.firstinspires.ftc.teamcode.Robot.TeamConstants;

public class Intake extends SubsystemBase implements TeamConstants {
    private final Motor1D intakeMotor;


    public Intake(DcMotorEx motor) {
        intakeMotor = new Motor1D(
                motor,
                Motor1D.Mode.POWER_BASED,
                DefaultMotorInfo.GOBILDA_1150RPM
        );
//        intakeMotor.setVelocityPIDFCoefficients( //Note: these are definitely wrong
//                1.17911653523,
//                0.117911653523,
//                0,
//                11.7911653523
//        );
        intakeMotor.setDirection(Motor1D.Direction.REVERSE);

        register();
    }


//    public void setVelocityRPM(double velocityRPM) {
//        intakeMotor.setVelocityRPM(velocityRPM);
//    }

    public void setPower(double power) {
        intakeMotor.setRawPower(power);
    }

    public double getVelocityRPM() {
        return intakeMotor.getVelocityRPM();
    }

    public void setProportion(double proportion) {
        intakeMotor.setVelocityFractional(proportion);
    }

//    public InstantCommand setVelocityRpmCommand(double velocityRPM) {
//        return new InstantCommand(
//                () -> intakeMotor.setVelocityRPM(velocityRPM)
//        );
//    }

    public InstantCommand setPowerCommand(double power) {
        return new InstantCommand(
                () -> intakeMotor.setRawPower(power)
        );
    }

}