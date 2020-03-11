/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Turret;

public class Auto_DriveAndShoot extends CommandBase {
  /**
   * Creates a new Auto_DriveAndShoot.
   */
  private static final double distanceToDrive = 6; // inches
  private static Autonomous auto;
  private static boolean isDone = false;


  public Auto_DriveAndShoot() {
    auto = new Autonomous();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!isDone) {
      Robot.m_turret.shooterRun();
      Robot.m_turret.enableTrackingMode();
      Robot.m_turret.lockOnTarget();
      auto.drivebackward(distanceToDrive);
      Robot.m_turret.fireOn();
      end(false);
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    isDone = true;
    Robot.m_turret.fireOff();
    Robot.m_turret.shooterStop();
    Robot.m_turret.disableTrackingMode();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isDone;
  }
}
