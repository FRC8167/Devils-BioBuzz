package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.Cogintilities.DefaultMotorInfo;
import org.firstinspires.ftc.teamcode.Robot.TeamConstants;

public class Intake extends SubsystemBase {
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

    public InstantCommand setPowerInstantCommand(double power) {
        return new InstantCommand(
                () -> intakeMotor.setRawPower(power)
        );
    }

    public Command setPowerCommand(double power, double durationSeconds) {
        return new SetPowerDurationCommand(power, durationSeconds);
    }

    public class SetPowerDurationCommand extends CommandBase {
        private final ElapsedTime timer = new ElapsedTime();
        private final double durationSeconds;

        private final Intake intake = Intake.this;
        private final double power;

        public SetPowerDurationCommand(double power, double durationSeconds) {
            this.power = power;
            this.durationSeconds = durationSeconds;
            addRequirements(intake);
        }

//        public SetPowerDurationCommand(double power) {
//            this(power, 1);
//        }



        @Override
        public void initialize() {
            intake.setPower(power);
            timer.reset();
        }

        @Override
        public boolean isFinished() {
            return timer.seconds() >= durationSeconds;
        }

        @Override
        public void end(boolean interrupted) {
            intake.setPower(0);
        }

    }

}