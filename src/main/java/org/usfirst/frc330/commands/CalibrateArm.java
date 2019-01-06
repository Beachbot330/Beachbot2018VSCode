// RobotBuilder Version: 2.0BB
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc330.commands;
import edu.wpi.first.wpilibj.command.BBCommand;
import org.usfirst.frc330.Robot;

/**
 *
 */
public class CalibrateArm extends BBCommand {

    public CalibrateArm() {
    	this.setRunWhenDisabled(false);
    	requires(Robot.arm);

    }

    protected void initialize() {
    	Robot.arm.calibrateMove();
    }


    protected void execute() {
    }

    protected boolean isFinished() {
        return Robot.arm.getCalibrated();
    }

    protected void end() {
    }


    protected void interrupted() {
    }
}
