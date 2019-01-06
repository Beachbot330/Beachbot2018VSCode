// RobotBuilder Version: 2.0BB
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc330.subsystems;

import org.usfirst.frc330.Robot;
import org.usfirst.frc330.commands.ManualArm;
import org.usfirst.frc330.commands.commandgroups.Calibrate;
import org.usfirst.frc330.constants.ArmConst;
import org.usfirst.frc330.constants.HandConst;
import org.usfirst.frc330.constants.LiftConst;
import org.usfirst.frc330.util.CSVLoggable;
import org.usfirst.frc330.util.CSVLogger;
import org.usfirst.frc330.util.Logger;
import org.usfirst.frc330.util.Logger.Severity;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

/**
 *
 */

public class Arm extends Subsystem {
	
	boolean calibrated = false; // Has the encoder been properly zeroed?

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private WPI_TalonSRX armL;
    private DigitalInput limitSwitch;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public Arm() {
    	
    	super();
    	//SmartDashboard.putData("Arm", this);
    	
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        armL = new WPI_TalonSRX(1);
        
        
        limitSwitch = new DigitalInput(4);
        addChild(limitSwitch);
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        

        // Setup CAN Talons
        armL.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, ArmConst.CAN_Timeout);
        armL.setInverted(false);
        armL.setSensorPhase(false);
        
        setPIDConstantsArm(ArmConst.proportional, ArmConst.integral, ArmConst.derivative, ArmConst.feedForward, ArmConst.MaxOutputPercent, true);
        setArmAbsoluteTolerance(ArmConst.tolerance);
		
		armL.configForwardSoftLimitEnable(false, ArmConst.CAN_Timeout); // Disable limits until after calibration
		armL.configReverseSoftLimitEnable(false, ArmConst.CAN_Timeout);
		armL.setNeutralMode(NeutralMode.Brake);
		
		armL.configOpenloopRamp(0, ArmConst.CAN_Timeout);
        
        armL.configNominalOutputForward(0, ArmConst.CAN_Timeout);	
		armL.configNominalOutputReverse(0, ArmConst.CAN_Timeout);
		
		armL.configForwardLimitSwitchSource(RemoteLimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0, ArmConst.CAN_Timeout);
		armL.configReverseLimitSwitchSource(RemoteLimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0, ArmConst.CAN_Timeout);
		
		//Magic Motion
		armL.configMotionCruiseVelocity(ArmConst.velocityLimit, ArmConst.CAN_Timeout);
        armL.configMotionAcceleration(ArmConst.accelLimit, ArmConst.CAN_Timeout);
        
        //set feedback frame so that getClosedLoopError comes faster then 160ms
        armL.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, ArmConst.CAN_Status_Frame_13_Period, ArmConst.CAN_Timeout);
		
        //--------------------------------------------------------------------
    	// Logging
    	//--------------------------------------------------------------------
        //VERIFY log all of the get commands. See 2017 as example -JB
		CSVLoggable temp = new CSVLoggable(true) {
			public double get() { return getArmAngle(); }
		};
		CSVLogger.getInstance().add("ArmAngle", temp);

		temp = new CSVLoggable(true) {
			public double get() { return getArmOutput(); }
		};
		CSVLogger.getInstance().add("ArmOutput", temp);
		
		temp = new CSVLoggable(true) {
			public double get() { return getSetpoint(); }
		};
		CSVLogger.getInstance().add("ArmSetpoint", temp);
		
		temp = new CSVLoggable(true) {
			public double get() { return getVelocity(); }
		};
		CSVLogger.getInstance().add("ArmVelocity", temp);
		
		temp = new CSVLoggable(true) {
			public double get() { 
				if(getCalibrated()) {
					return 1.0;
				}
				else{
					return 0.0;
				}
			};
		};
		CSVLogger.getInstance().add("Arm Calibrated", temp);
    }
    
    @Override
    public void initDefaultCommand() {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
        this.setDefaultCommand(new ManualArm());
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void periodic() {
        // Calibrate the first time the limit switch is pressed
    	if(!calibrated) {
    		if(!limitSwitch.get()) {
    			armL.setSelectedSensorPosition(degreesToTicks(ArmConst.limitSwitchAngle), 0, ArmConst.CAN_Timeout_No_Wait);
    			armL.disable();
    			calibrated = true;
    			armL.configForwardSoftLimitThreshold(degreesToTicks(ArmConst.upperLimit), ArmConst.CAN_Timeout_No_Wait);
    			armL.configReverseSoftLimitThreshold(degreesToTicks(ArmConst.lowerLimit), ArmConst.CAN_Timeout_No_Wait);
    			armL.configForwardSoftLimitEnable(true, ArmConst.CAN_Timeout_No_Wait);
    			armL.configReverseSoftLimitEnable(true, ArmConst.CAN_Timeout_No_Wait);
    		}
    	}
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PIDGETTERS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PIDGETTERS

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	//--------------------------------------------------------------------
	// Get Methods
	//--------------------------------------------------------------------
	
	//VERIFY implement getArmAngle -JB
    public double getArmAngle()
	{
		return (ticksToDegrees(armL.getSelectedSensorPosition(0)));
	} 
	
	//VERIFY Implement getArmLowerLimit, getArmUpperLimit -JB
    
    public double getLowerLimit()
	{
		return (ticksToDegrees((int)armL.configGetParameter(ParamEnum.eForwardSoftLimitThreshold, 0, ArmConst.CAN_Timeout_No_Wait)));
	}
    
    public double getUpperLimit()
	{
		return (ticksToDegrees((int)armL.configGetParameter(ParamEnum.eReverseSoftLimitThreshold, 0, ArmConst.CAN_Timeout_No_Wait)));
	}
	
	//VERIFY implement get armOutput -JB
    public double getArmOutput() {
		return armL.getMotorOutputVoltage()/armL.getBusVoltage();
	}
	
    public boolean getCalibrated() {
    	return calibrated;
    }	
    
    public boolean getArmOnTarget() {
    	double error = this.getSetpoint() - this.getArmAngle();
    	return (Math.abs(error) < tolerance);
    }
    
	public double getSetpoint() {
		if(armL.getControlMode() == ControlMode.Position || armL.getControlMode() == ControlMode.Velocity || armL.getControlMode() == ControlMode.MotionMagic) {
			return ticksToDegrees(armL.getClosedLoopTarget(0));
		}
		else {
			return 999;
		}
	}
	
	public double getVelocity() {
		return armL.getSelectedSensorVelocity(0);
	}
	
	//--------------------------------------------------------------------
	// Set Methods
	//--------------------------------------------------------------------
	
	public void setUncalibrated() {
		this.calibrated = false;
	}
	
    public void setArmThrottle(double output) {
        if(calibrated) {
        	armL.set(ControlMode.PercentOutput, output);
        }
        else {
        	Scheduler.getInstance().add(new Calibrate());
        }
    }

    public void setArmAngle(double position) {
    	if(calibrated && Robot.hand.getCalibrated()) {
    		armL.set(ControlMode.MotionMagic, degreesToTicks(position));
    	}
    	else {
    		Scheduler.getInstance().add(new Calibrate());
    	}
    }
    
    public void setPIDConstantsArm (double P, double I, double D, double F, double maxOutput, boolean timeout)
   	{
    	Logger.getInstance().println("Changing arm P: " + P, Severity.INFO);
    	Logger.getInstance().println("Changing arm I: " + I, Severity.INFO);
    	Logger.getInstance().println("Changing arm D: " + D, Severity.INFO);
    	Logger.getInstance().println("Changing arm F: " + F, Severity.INFO);
    	Logger.getInstance().println("Changing arm Max: " + maxOutput, Severity.INFO);
       	if(timeout) {
       		//assume using main PID loop (index 0)
       		armL.config_kP(0, P, ArmConst.CAN_Timeout);
       		armL.config_kI(0, I, ArmConst.CAN_Timeout);
       		armL.config_kD(0, D, ArmConst.CAN_Timeout);
       		armL.config_kF(0, F, ArmConst.CAN_Timeout);
       		armL.configPeakOutputForward(maxOutput, ArmConst.CAN_Timeout);
            armL.configPeakOutputReverse(-maxOutput, ArmConst.CAN_Timeout);
       	}
       	else {
   	    	//assume using main PID loop (index 0)
   			armL.config_kP(0, P, ArmConst.CAN_Timeout_No_Wait);
   			armL.config_kI(0, I, ArmConst.CAN_Timeout_No_Wait);
   			armL.config_kD(0, D, ArmConst.CAN_Timeout_No_Wait);
   			armL.config_kF(0, F, ArmConst.CAN_Timeout_No_Wait);
   			armL.configPeakOutputForward(maxOutput, ArmConst.CAN_Timeout_No_Wait);
            armL.configPeakOutputReverse(-maxOutput, ArmConst.CAN_Timeout_No_Wait);
       	}
   	}
    
     
    double tolerance = 0;
    
    public void setArmAbsoluteTolerance(double absvalue) {
    	tolerance = absvalue;
	}
   	
	//--------------------------------------------------------------------
	// Other Methods
	//--------------------------------------------------------------------
    
	public void stopArm() {
		if (armL.isAlive())		
			armL.setIntegralAccumulator(0.0, 0, 0);
		
		armL.set(0);
		armL.disable(); 
		Logger.getInstance().println("Arm disabled", Logger.Severity.INFO); 
	}
	

	public boolean isEnable() {
		return armL.isAlive();
	}
		

	int inertiaCounter;
	
    public void manualArm() {	
    	double gamepadCommand = -Robot.oi.armGamePad.getY();
    	double angle;
    	
    	if (Math.abs(gamepadCommand) > ArmConst.gamepadDeadZone) {
    		setArmThrottle(gamepadCommand/Math.abs(gamepadCommand)*Math.pow(gamepadCommand, 2)*0.4); //scaled to 0.4 max
    		inertiaCounter = ArmConst.inertiaCounter;
    	}
    	else if (inertiaCounter > 0) {
    		inertiaCounter--;
			setArmThrottle(0);
    	}
    	else if (armL.getControlMode() != ControlMode.MotionMagic) {
			angle = getArmAngle();
			setArmAngle(angle);
    	}  	
    }
    
    private int degreesToTicks(double degrees) {
    	return (int)(degrees * ArmConst.ticksPerEncoderRev * ArmConst.gearRatio / 360.0);
    }
    
    private double ticksToDegrees(int ticks) {
    	return ((double)ticks * 360.0 / (double)ArmConst.ticksPerEncoderRev / ArmConst.gearRatio);
    }
	    
    public void calibrateMove() {
    	if(!calibrated) {
    		armL.set(ControlMode.PercentOutput, ArmConst.calibrationSpeed);
    	}
    }
    
    public double getArmLFirmwareVersion() {
		int firmwareVersion = armL.getFirmwareVersion();
		return ((firmwareVersion & 0xFF00) >> 8) + (firmwareVersion & 0xFF) / 100.0;
	}
}
