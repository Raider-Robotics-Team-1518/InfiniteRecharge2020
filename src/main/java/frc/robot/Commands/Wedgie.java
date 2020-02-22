/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Command;

public class Wedgie extends Command {

  public Wedgie() {
  }

  public static void lockWedgie() {
    System.out.println("Relay Lock");
    for (int x = 1; x <= 20000; x++) {
      Robot.m_relay.set(Relay.Value.kForward);
    }
    restingWedgie();
  }

  public static void unlockWedgie() {
    System.out.println("Relay Unlock");
    for (int x = 1; x <= 20000; x++) {
      Robot.m_relay.set(Relay.Value.kReverse);
    }
    restingWedgie();
  }

  public static void restingWedgie() {
    Robot.m_relay.set(Relay.Value.kOff);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
