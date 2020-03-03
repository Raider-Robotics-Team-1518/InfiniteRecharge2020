/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class AimTurret extends CommandBase {
  /**
   * Creates a new TurnTurret.
   */


  public AimTurret() {
    // Use addRequirements() here to declare subsystem dependencies.
    System.out.println("AimTurret instantiated");
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("AimTurret - initialize()");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    System.out.println("AimTurret - execute()");
    Robot.m_turret.shooterRun();
    Robot.m_turret.enableTrackingMode();
    Robot.m_turret.lockOnTarget();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean wasInterrupted) {
    super.end(false);
    Robot.m_turret.shooterStop();
    Robot.m_turret.disableTrackingMode();
  }

  // // Returns true when the command should end.
//   @Override
//   public boolean isFinished() {
//     return true;
//   }

}
