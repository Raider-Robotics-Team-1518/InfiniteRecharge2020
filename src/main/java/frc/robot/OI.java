/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import frc.robot.commands.*;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class OI extends SubsystemBase {

  public final Joystick m_stick;
  public static double liveX;
  public static double liveY;
  public static double liveZ;
  public static double livePow;
  // public final JoystickButton turretUp = new JoystickButton(m_stick, 6);
  // public final JoystickButton turretDown = new JoystickButton(m_stick, 4);
  // public final JoystickButton turretLeft = new JoystickButton(m_stick, 3);
  // public final JoystickButton turretRight = new JoystickButton(m_stick, 5);
  public final JoystickButton turretControlButton;
  public final JoystickButton intakePivotOut;
  public final JoystickButton intakePivotIn;
  public final JoystickButton climbUpButton;
  public final JoystickButton climbDownButton;
  public final JoystickButton wedgieButton;
  public final JoystickButton testButton;
  
  private static boolean wedgieIsExtended = false;

  /**
   * Creates a new OI.
   */
  public OI() {
    System.out.println("OI is being loaded");
    m_stick = new Joystick(0);
    turretControlButton = new JoystickButton(m_stick, 2);
    turretControlButton.whileHeld(new AimTurret());

    intakePivotOut = new JoystickButton(m_stick, 5);
    intakePivotIn = new JoystickButton(m_stick, 6);
    wedgieButton = new JoystickButton(m_stick, 9);
    climbUpButton = new JoystickButton(m_stick, 11);
    climbDownButton = new JoystickButton(m_stick, 12);
    testButton = new JoystickButton(m_stick, 10);
    wedgieButton.whenPressed(() -> {
    
      if (wedgieIsExtended) {
        Wedgie.lockWedgie();
      } else {
        Wedgie.unlockWedgie();
      }
    }).whenReleased(() -> {wedgieIsExtended = !wedgieIsExtended;} ); 

  }

  public void get(){
    liveX = m_stick.getX();
    liveY = m_stick.getY();
    liveZ = m_stick.getZ();
    livePow = m_stick.getThrottle();
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if(m_stick.getPOV() == 90 ) {
      Robot.m_turret.lookRight();
    }
    else if (m_stick.getPOV() == 270) {
      Robot.m_turret.lookLeft();
    }
    else {
      Robot.m_turret.stopTurret();
    }
   
  }
}
