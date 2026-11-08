package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.Robot.TeamConstants;

public class Gate extends SubsystemBase {
    private final Servo1D gateServo;


    public Gate(Servo servo) {
        gateServo = new Servo1D(servo, 0, 0, 1, true);
        register();
    }

    public void setPosition(double position) {
        gateServo.setPosition(position);
    }

    public InstantCommand setPositionInstantCommand(double position) {
        return new InstantCommand(
                () -> gateServo.setPosition(position)
        );
    }

    public Command setPositionCommand(double position) {
        return new SetPositionCommand(position);
    }

    public Command openGateCommand() {
        return new SetPositionCommand(TeamConstants.GATE_OPEN_POSITION);
    }

    public Command closeGateCommand() {
        return new SetPositionCommand(TeamConstants.GATE_CLOSED_POSITION);
    }

    public class SetPositionCommand extends CommandBase {
        private final ElapsedTime timer = new ElapsedTime();
        private double durationSeconds = TeamConstants.GATE_DEFAULT_DURATION_SECONDS; //TODO: Confirm default duration (or make dynamic)

        private final Gate gate = Gate.this;
        private final double position;

        public SetPositionCommand(double position) {
            this.position = position;
            addRequirements(gate);
        }

        public SetPositionCommand(double position, double durationSeconds) {
            this(position);
            this.durationSeconds = durationSeconds;
        }

        @Override
        public void initialize() {
            gate.setPosition(position);
            timer.reset();
        }

        @Override
        public boolean isFinished() {
            return timer.seconds() >= durationSeconds;
        }

    }
}