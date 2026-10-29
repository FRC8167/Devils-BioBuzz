package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.Cogintilities.DefaultMotorInfo;
import org.firstinspires.ftc.teamcode.Robot.TeamConstants;

public class Shooter extends SubsystemBase implements TeamConstants {
    private final Motor1D shooterMotor;


    public Shooter(DcMotorEx motor) {
        shooterMotor = new Motor1D(
                motor,
                Motor1D.Mode.VELOCITY_BASED,
                DefaultMotorInfo.GOBILDA_6000RPM
        );
        shooterMotor.setVelocityPIDFCoefficients(1.5,0,0,5);
        register();
    }


    public void setVelocityRPM(double velocityRPM) {
        shooterMotor.setVelocityRPM(velocityRPM);
    }

    public void setProportion(double proportion) {
        shooterMotor.setVelocityFractional(proportion);
    }

    public InstantCommand setVelocityRpmCommand(double velocityRPM) {
        return new InstantCommand(
                () -> shooterMotor.setVelocityRPM(velocityRPM)
        );
    }
}