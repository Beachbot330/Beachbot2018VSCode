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
import org.usfirst.frc330.util.Logger;
import org.usfirst.frc330.util.Logger.Severity;

/**
 *
 */
public class Log extends BBCommand {

	String message;
	Severity sev;
	
    public Log(String message, Severity sev) {
    	this.setRunWhenDisabled(true);
    	this.message = message;
    	this.sev = sev;
    }
    
    public Log(String message) {
    	this(message, Severity.INFO);
    }

    protected void initialize() {
    	Logger.getInstance().println(this.message, this.sev);
    }


    protected void execute() {
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
    }


    protected void interrupted() {
    }
}
