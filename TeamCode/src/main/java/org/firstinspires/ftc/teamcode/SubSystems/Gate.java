package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.Cogintilities.DefaultMotorInfo;
import org.firstinspires.ftc.teamcode.Robot.TeamConstants;

public class Gate extends SubsystemBase implements TeamConstants {
    private final Servo1D gateServo;


    public Gate(Servo servo) {
        gateServo = new Servo1D(servo, 0, 0, 1, true);
        register();
    }

    public void setPosition(double position) {
        gateServo.setPosition(position);
    }

    public InstantCommand setPositionCommand(double position) {
        return new InstantCommand(
                () -> gateServo.setPosition(position)
        );
    }
}