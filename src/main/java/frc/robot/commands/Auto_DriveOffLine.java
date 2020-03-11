/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Autonomous;

public class Auto_DriveOffLine extends CommandBase {
  /**
   * Creates a new Auto_DriveOffLine.
   */
  private static final double distanceToDrive = 6; // inches
  private static Autonomous auto;
  private static boolean isDone = false;


  public Auto_DriveOffLine() {
    // Use addRequirements() here to declare subsystem dependencies.
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
      auto.drivebackward(distanceToDrive);
      end(false);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    isDone = true;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isDone;
  }
}
