package org.usfirst.frc330.commands;

import org.usfirst.frc330.Robot;
import org.usfirst.frc330.constants.ArmConst;
import org.usfirst.frc330.constants.HandConst;

public class CoordinatedRelease extends CoordinatedMove {
	double releaseAngle;
	
	public CoordinatedRelease() {
		super(ArmConst.vertical, 210);
		releaseAngle = 20;
	}

	public CoordinatedRelease(double armAngle, double handAngleRelGround, double releaseAngle) {
		super(armAngle, handAngleRelGround);
		this.releaseAngle = releaseAngle;
	}

	public CoordinatedRelease(double armAngle, double handAngleRelGround, double releaseAngle, double timeout) {
		super(armAngle, handAngleRelGround, timeout);
		this.releaseAngle = releaseAngle;
	}
	
	@Override 
	protected void execute() {
		super.execute();
		if (Robot.arm.getArmAngle() > releaseAngle)
			Robot.grabber.openClaw();
	}
	
	@Override
	protected boolean isFinished( ) {
		return (isTimedOut() || Robot.arm.getArmAngle() > releaseAngle+20);
	}
	
	@Override
	protected void end() {
		Robot.arm.setArmAngle(releaseAngle+20);
		Robot.grabber.openClaw();
		super.end();
	}

}
