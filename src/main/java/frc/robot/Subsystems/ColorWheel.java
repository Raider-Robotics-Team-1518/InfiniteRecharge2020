/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

public class ColorWheel extends SubsystemBase {

  // Color Sensor
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);
  private final ColorMatch m_colorMatcher = new ColorMatch();
  private final Color kBlueTarget = ColorMatch.makeColor(0.129, .428, .443);
  private final Color kGreenTarget = ColorMatch.makeColor(.172, .577, .251);
  private final Color kRedTarget = ColorMatch.makeColor(.519, .347, .133);
  private final Color kYellowTarget = ColorMatch.makeColor(.319, .558, .124);

  // Motor to rotate
  public static WPI_TalonSRX colorWheelMotor = new WPI_TalonSRX(5);
  private final double kMaxRotateMotorPower = 0.25;
  private final double kMinRotateMotorPower = 0.15;

  // Motor to raise and lower arm
  public static WPI_TalonSRX colorWheelArm = new WPI_TalonSRX(6);
  private final double kMaxArmMotorPower = 0.25;
  private final double kMinArmMotorPower = 0.15;

  // Pneumatics to lift/lower

  public ColorWheel() {
    m_colorMatcher.addColorMatch(kBlueTarget);
    m_colorMatcher.addColorMatch(kGreenTarget);
    m_colorMatcher.addColorMatch(kRedTarget);
    m_colorMatcher.addColorMatch(kYellowTarget);

  }

  @Override
  public void periodic() {
    // Update color value from sensor to dashboard
    // Send color data to dashboard
    Color detectedColor = colorSensor.getColor();
    SmartDashboard.putNumber("xRed", detectedColor.red);
    SmartDashboard.putNumber("xGreen", detectedColor.green);
    SmartDashboard.putNumber("xBlue", detectedColor.blue);
    ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

    String gameData = DriverStation.getInstance().getGameSpecificMessage();
    if (gameData.length() > 0) {
      switch (gameData.charAt(0)) {
        case 'B':
          if (match.color != kRedTarget) {
            // drive spinner motor
          } else {
            // stop motor
            System.out.println("we found blue");

          }
          SmartDashboard.putString("FMS Color", "BLUE");
          break;
        case 'G':
          if (match.color != kYellowTarget) {
            // drive spinner motor
          } else {
            // stop motor
            System.out.println("we found green");
          }
          SmartDashboard.putString("FMS Color", "GREEN");
          break;
        case 'R':
          if (match.color != kBlueTarget) {
            // drive spinner motor
          } else {
            // stop motor
            System.out.println("we found red");
          }
          SmartDashboard.putString("FMS Color", "RED");
          break;
        case 'Y':
          if (match.color != kGreenTarget) {
            // drive spinner motor
          } else {
            // stop motor
            System.out.println("we found yellow");
          }
          SmartDashboard.putString("FMS Color", "YELLOW");
          break;
        default:
          // This is corrupt data
          break;
      }
    } else {
      // Code for no data received yet
    }

  }
}
