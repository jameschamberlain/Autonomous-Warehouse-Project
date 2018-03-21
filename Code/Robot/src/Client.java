import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import rp.config.RobotConfigs;

/**
 * 
 * Communication for a robot.
 * Enables movement where appropriate
 * 
 * @author James
 *
 */
public class Client {
	
	/**
	 * Stores the movement class for the robot
	 */
	private static Movement robot;
	
	private static String currentRoute = "";
	
	private static float currentWeight = 0.0f;
	
	private static float itemsWeight = 0.0f;
	
	public static boolean isFull = false;
	
	
	public static void main(String[] args) {
		
		
		initialiseSensors();
		
		
		Display display = new Display();
		
		
		Button.waitForAnyPress();
		
		
		System.out.println("Press for BT");
		Button.waitForAnyPress();

		System.out.println("Waiting for BT...");
		BTConnection connection = Bluetooth.waitForConnection();
		System.out.println("BT connected!");
		
		display.show();

		DataInputStream inputStream = connection.openDataInputStream();
		DataOutputStream outputStream = connection.openDataOutputStream();

		boolean run = true;
		
		
		/*
		 * Read routes from the server,
		 * then instantly execute them,
		 * and finally send the route the robot
		 * actually took back to the server
		 */
		while (run) {

			try {
				int weightLength = inputStream.readInt();
				byte[] weightArray = new byte[weightLength];
				inputStream.read(weightArray);
				String weight = new String(weightArray);
				itemsWeight = Float.parseFloat(weight);
				int length = inputStream.readInt();
				byte[] array = new byte[length];
				inputStream.read(array);
				String route = new String(array);
				
				/*
				 * If the route received is empty don't do anything,
				 * otherwise execute the route
				 */
				if (route.equals("5")) {
					outputStream.writeInt(1);
					outputStream.writeBytes("5");
					outputStream.flush();
				}
				if (route == null || route.equals("")) {
				}
				else {
					char[] instructions =  route.toCharArray();
					for(Character instruction : instructions) {
						// Execute the route and write the individual locations
						String routeExecuted = robot.executeRoute(route);
						currentRoute += routeExecuted;
						if (currentRoute.equals(route)) {
							currentWeight = display.pickItem(currentWeight, itemsWeight);
						}
						
						if (display.isFull) {
							outputStream.writeInt(1);
							outputStream.writeBytes("f");
							outputStream.flush();
						}
						outputStream.writeInt(routeExecuted.length());
						outputStream.writeBytes(routeExecuted);
						outputStream.flush();
					}
					
				}
				
			}
			catch (IOException e) {
				System.out.println("Failed to talk to server");
				run = false;
			}
		}
	}
	
	/**
	 * Setup the sensors for usage and create a new movement object
	 */
	public static void initialiseSensors() {
		LightSensor leftLightSensor = new LightSensor(SensorPort.S1);
		LightSensor rightLightSensor = new LightSensor(SensorPort.S2);
		LightSensor middleLightSensor = new LightSensor(SensorPort.S3);
		robot = new Movement(RobotConfigs.EXPRESS_BOT, leftLightSensor, rightLightSensor, middleLightSensor);
		robot.calibrate();
	}

}
