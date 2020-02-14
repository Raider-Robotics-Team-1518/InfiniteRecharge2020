// package frc.robot.components;

// import edu.wpi.first.networktables.NetworkTableEntry;
// import edu.wpi.first.networktables.NetworkTableInstance;

// @Override
// public void RobotInit() {
//     // PWM port 9
//     // Must be a PWM header, not MXP or DIO
//     m_led = new AddressableLED(9);
    
//     // Reuse buffer
//     // Default to a length of 60, start empty output
//     // Length is expensive to set, so only set it once, then just update data
//     m_ledBuffer = new AddressableLEDBuffer(54);
//     m_led.setLength(m_ledBuffer.getLength());

//     // Set the data
//     m_led.setData(m_ledBuffer);
//     m_led.start();
//   }
