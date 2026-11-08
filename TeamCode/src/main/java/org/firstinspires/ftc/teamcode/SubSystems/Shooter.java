package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandBase;
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
//        shooterMotor.setVelocityPIDFCoefficients(1.5,0,0,5);
        shooterMotor.setVelocityPIDFCoefficients(
                1.17911653523,
                0.117911653523,
                0,
                11.7911653523
        );
        register();
    }


    public void setVelocityRPM(double velocityRPM) {
        shooterMotor.setVelocityRPM(velocityRPM);
    }

    public double getVelocityRPM() {
        return shooterMotor.getVelocityRPM();
    }

    public void setProportion(double proportion) {
        shooterMotor.setVelocityFractional(proportion);
    }

    public InstantCommand setVelocityRpmInstantCommand(double velocityRPM) {
        return new InstantCommand(
                () -> shooterMotor.setVelocityRPM(velocityRPM)
        );
    }

    public Command setVelocityRpmCommand(double velocityRPM) {
        return new SetVelocityRPMCommand(velocityRPM, 10); //TODO: Confirm tolerances
    }

    public Command setVelocityRpmCommand(double velocityRPM, double timeoutSeconds) {
        return new SetVelocityRPMCommand(velocityRPM, 10).withTimeout((long) (timeoutSeconds*1000));
    }

    public Command setVelocityShootCommand() {
        return setVelocityRpmCommand(3000);
    }
    public Command setVelocityShootCommand(double timeoutSeconds) {
        return setVelocityRpmCommand(3000, timeoutSeconds);
    }

    public Command setVelocityStopCommand() {
        return setVelocityRpmCommand(0);
    }



    public class SetVelocityRPMCommand extends CommandBase {
        private final Shooter shooter = Shooter.this;
        private final double velocityRPM;
        private final double toleranceRPM;

        public SetVelocityRPMCommand(double velocityRPM, double toleranceRPM) {
            this.velocityRPM = velocityRPM;
            this.toleranceRPM = toleranceRPM;
            addRequirements(shooter);
        }

        @Override
        public void initialize() {
            shooter.setVelocityRPM(velocityRPM);
        }

        @Override
        public boolean isFinished() {
            return Math.abs(shooter.getVelocityRPM() - velocityRPM) < toleranceRPM;
        }

    }
}