package org.firstinspires.ftc.teamcode;

//import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.hardware.SensorColor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.firstinspires.ftc.teamcode.Cogintilities.MirrorUtility;

import org.firstinspires.ftc.teamcode.SubSystems.Intake;

import org.firstinspires.ftc.teamcode.SubSystems.MecanumDrive;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;


import java.util.List;


public class Robot{

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
    public Intake intake;



    public void init(HardwareMap hardwareMap) throws InterruptedException {

        // Hardware
        MotorEx driveMotorRF = new MotorEx(hardwareMap, "RightFront").setCachingTolerance(0.01);
        MotorEx driveMotorLF = new MotorEx(hardwareMap, "LeftFront").setCachingTolerance(0.01);
        MotorEx driveMotorLR = new MotorEx(hardwareMap, "LeftRear").setCachingTolerance(0.01);
        MotorEx driveMotorRR = new MotorEx(hardwareMap, "RightRear").setCachingTolerance(0.01);

//        follower = Constants.createFollower(hardwareMap);

        MotorEx intakeMotor    = new MotorEx(hardwareMap, "Intake").setCachingTolerance(0.01);


        ctrlHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : ctrlHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }


        //Instantiate Subsystems
        mecanumDrive = new MecanumDrive(driveMotorLF, driveMotorLR, driveMotorRF, driveMotorRR);
        intake  = new Intake(intakeMotor);


        //Register Subsystems
        register(mecanumDrive, intake);

        if (OP_MODE_TYPE.equals(OpModeType.AUTO)) {
            initHasMovement();
        }
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