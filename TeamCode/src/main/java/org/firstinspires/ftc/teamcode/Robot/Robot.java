package org.firstinspires.ftc.teamcode.Robot;

//import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.teamcode.Cogintilities.MirrorUtility;

import org.firstinspires.ftc.teamcode.Cogintilities.DefaultMotorInfo;

import org.firstinspires.ftc.teamcode.SubSystems.Gate;
import org.firstinspires.ftc.teamcode.SubSystems.Intake;
import org.firstinspires.ftc.teamcode.SubSystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.SubSystems.Motor1D;
import org.firstinspires.ftc.teamcode.SubSystems.Servo1D;
import org.firstinspires.ftc.teamcode.SubSystems.Shooter;

//import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import com.pedropathing.follower.Follower;


import java.util.List;


public class Robot extends com.seattlesolvers.solverslib.command.Robot{

    private static final Robot instance = new Robot();
    public static Robot getInstance() {
        return instance;
    }

    public enum OpModeType {
        AUTO,
        TELEOP
    }

    public enum Alliance {
        RED,
        BLUE,
        UNSPECIFIED
    }

    //field poses

    private static Alliance alliance = Alliance.UNSPECIFIED;

    public Telemetry telemetry;


    public static OpModeType OP_MODE_TYPE;
    static List<LynxModule> ctrlHubs;

    public GoBildaPinpointDriver pinpoint;

    public Follower follower;
    public MecanumDrive mecanumDrive;
//    public Intake intake;
//    public Motor1D intakeMotor;
//    public Motor1D shooterMotor;
    public Intake intake;
    public Shooter shooter;
    public Gate gate;

//    public Servo1D dropServo;



    public void init(HardwareMap hardwareMap) throws InterruptedException {

        // Hardware
        MotorEx driveMotorRF = new MotorEx(hardwareMap, "RightFront").setCachingTolerance(0.01); //TODO: update with actual motor types (SolversMotor?)
        MotorEx driveMotorLF = new MotorEx(hardwareMap, "LeftFront").setCachingTolerance(0.01);
        MotorEx driveMotorLR = new MotorEx(hardwareMap, "LeftRear").setCachingTolerance(0.01);
        MotorEx driveMotorRR = new MotorEx(hardwareMap, "RightRear").setCachingTolerance(0.01);


//        dropServo = new Servo1D(
//                hardwareMap.get(Servo.class, "DropServo"),
//                0, 0, 1, true
//        );
//        follower = Constants.createFollower(hardwareMap);

//        intakeMotor  = new Motor1D(
//                hardwareMap.get(DcMotorEx.class, "Intake"),
//                Motor1D.Mode.VELOCITY_BASED,
//                DefaultMotorInfo.GOBILDA_117RPM
//        );
//
//        intakeMotor.setVelocityPIDFCoefficients(
//                1.17911653523,
//                0.117911653523,
//                0,
//                11.7911653523
//        );

//        shooterMotor = new Motor1D(
//                hardwareMap.get(DcMotorEx.class, "Shooter"),
//                Motor1D.Mode.VELOCITY_BASED,
//                DefaultMotorInfo.GOBILDA_6000RPM
//        );
//
//        shooterMotor.setVelocityPIDFCoefficients(1.5,0,0,5);


        ctrlHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : ctrlHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }


        //Instantiate Subsystems
        mecanumDrive = new MecanumDrive(driveMotorLF, driveMotorLR, driveMotorRF, driveMotorRR);
        intake       = new Intake(hardwareMap.get(DcMotorEx.class, "Intake"));
        shooter      = new Shooter(hardwareMap.get(DcMotorEx.class, "Shooter"));
        gate         = new Gate(hardwareMap.get(Servo.class, "DropServo"));


//        //Register Subsystems
//        register(mecanumDrive);

//        if (OP_MODE_TYPE.equals(OpModeType.AUTO)) {
//            initHasMovement();
//        }
    }

    public void initHasMovement() {
        //TODO what goes here??
    }

    public void setAlliance(Alliance ally) {
        alliance = ally;
    }

    public Alliance getAlliance() {
        return alliance;
    }




}