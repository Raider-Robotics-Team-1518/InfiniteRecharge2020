/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Autonomous;


public class AutonomousTest extends CommandBase {
  /**
   * Creates a new AutonomousTest.
   */

  Autonomous autosystem = new Autonomous();
  private boolean done = false;
  private static int forwardDistance = 36;
  private static int strafeDistance = 36;
  private static int rotationDistance = 360;

  public AutonomousTest() {
    // Use addRequirements() here to declare subsystem dependencies.
    
  }

  // Called when the command is initially scheduled.
  // @Override
  public void initialize() {
    done = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  // @Override
  public void execute() {
    if (done == false) {
      autosystem.driveforward(forwardDistance);
      Timer.delay(1);
      autosystem.drivebackward(forwardDistance);
      Timer.delay(1);
      autosystem.strafeleft(strafeDistance);
      Timer.delay(1);
      autosystem.straferight(strafeDistance);
      Timer.delay(1);
      autosystem.turnleft(rotationDistance);
      done = true;
    }
  }

  // Called once the command ends or is interrupted.
  // @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return done;
  }
}
