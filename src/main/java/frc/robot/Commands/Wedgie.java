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

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class Wedgie extends Command {



  public Wedgie() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  public static void lockWedgie() {
    System.out.println("Relay Lock");
    for (int x = 1; x <= 20000; x++) {
      //Robot.m_relay.setDirection(Relay.Direction.kForward);
      Robot.m_relay.set(Relay.Value.kForward);
    }
    restingWedgie();
  }

  public static void unlockWedgie() {
    System.out.println("Relay Unlock");
    for (int x = 1; x <= 20000; x++) {
      //Robot.m_relay.setDirection(Relay.Direction.kReverse);
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
    // TODO Auto-generated method stub
    return false;
  }
}
