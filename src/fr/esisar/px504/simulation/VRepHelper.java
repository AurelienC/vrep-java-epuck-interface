package fr.esisar.px504.simulation;
import java.util.Arrays;
import java.util.List;

import coppelia.IntW;
import coppelia.IntWA;
import coppelia.remoteApi;
import fr.esisar.px504.simulation.Orientation.Cardinality;

/**
 * Class used with V-Rep simulator for map a maze and navigate in it
 * @author acadiou
 * @version 0.1
 *
 */
public class VRepHelper {

	// Variables
	private remoteApi vrep;
	private int clientId;
	private boolean[] sensors = new boolean[8];
	private IntW leftJoint = new IntW(0);
	private IntW rightJoint = new IntW(0);
	private Orientation orientation = new Orientation(Cardinality.yPlus);
	private Coordinate coordinate = new Coordinate();
	private Path path = new Path(new Coordinate(0, 0));
	

	
	// Getters and setters

	/**
	 * Get the V-Rep simulator objet (created by Coppelia)
	 * @return vrep The V-Rep simulator object 
	 */
	public remoteApi getVrep() {
		return vrep;
	}
	
	
	/**
	 * Get the client id used for exchange data with V-Rep simulator
	 * @return clientId 
	 */
	public int getClientId() {
		return clientId;
	}

	
	/**
	 * Get the state of 8 IR sensors on the E-Puck robot 
	 * @return sensors Array of bool. Index 0 = sensor 1 (left). Index 7 = sensor 8 (left below)
	 */
	public boolean[] getSensors() {
		refreshSensors();
		return sensors;
	}
	
	
	/**
	 * Get the actual orientation of the E-Puck robot
	 * @return orientation Object with the orientation of robot
	 */
	public Orientation getOrientation() {
		return orientation;
	}
	
	
	/**
	 * Set the actual orientation of the E-Puck robot
	 * @warning be careful ! This can compromise the entire mapping
	 * @return orientation Object with the orientation of robot
	 */
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}
	
	
	/**
	 * Get the actual position of the E-Puck robot in the maze (x,y)
	 * @return coordinate Object with the (x,y)
	 */
	public Coordinate getCoordinate() {
		return coordinate;
	}
	
	
	/**
	 * Set the actual position of the E-Puck robot
	 * @warning be careful ! This can compromise the entire mapping
	 * @param coordinate Object with the coordinates of the robot
	 */
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
	
	

	
	
	// Constructor
	
	/**
	 * Constructor of VRepHelper
	 * @param port The port of the socket used for communicate with V-Rep simulator. Nominale value: 19997
	 */
	public VRepHelper(int port) {
		super();
		this.vrep = new remoteApi();

		vrep.simxFinish(-1); // just in case, close all opened connections
		
		this.clientId = vrep.simxStart("127.0.0.1",port,true,true,5000,5);

		if (this.clientId!=-1)
		{
			System.out.println("Connected to remote API server");
			vrep.simxSynchronous(clientId,false);
			vrep.simxSetIntegerSignal(clientId, "cde", 10, remoteApi.simx_opmode_oneshot);
			vrep.simxAddStatusbarMessage(clientId,"Hello V-REP! Here Java program",remoteApi.simx_opmode_oneshot);
		}		
	}
	
	
	// Methods
	
	/**
	 * Start the simulation in V-Rep simulator (like "play" button)
	 * @throws Exception 
	 */
	public void startSimulation() throws VRepException {
		vrep.simxStartSimulation(clientId,remoteApi.simx_opmode_blocking);
		
		IntWA objectHandles = new IntWA(1);
		int ret=vrep.simxGetObjects(clientId,remoteApi.sim_handle_all,objectHandles,remoteApi.simx_opmode_blocking);
		
		if (ret==remoteApi.simx_return_ok)
			System.out.format("Number of objects in the scene: %d\n",objectHandles.getArray().length);
		else
			throw new VRepException("Remote API function call returned with error code: " + ret);
	
        try
        {
            Thread.sleep(100);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
		
		vrep.simxGetObjectHandle(this.clientId, "ePuck_leftJoint", this.leftJoint, remoteApi.simx_opmode_blocking);
		vrep.simxGetObjectHandle(this.clientId, "ePuck_rightJoint", this.rightJoint, remoteApi.simx_opmode_blocking);
		refrechSensorsWithSettings(remoteApi.simx_opmode_streaming);
		
		IntW ret1 = new IntW(0);		
		vrep.simxGetIntegerSignal(clientId, "return", ret1, remoteApi.simx_opmode_streaming);
	}
	
	
	/**
	 * Stop the simulation in V-Rep simulator (like "stop" button)
	 */
	public void stopSimulation() {
		// Before closing the connection to V-REP, make sure that the last command sent out had time to arrive.
		IntW pingTime = new IntW(0);
		vrep.simxGetPingTime(clientId,pingTime);

		// stop the simulation:
		vrep.simxStopSimulation(clientId,remoteApi.simx_opmode_blocking);

		// Now close the connection to V-REP:   
		vrep.simxFinish(clientId);
	}
	
	
	
	/**
	 * Linear motion of the E-Puck robot
	 * Function 1 on V-Rep Lua code
	 * @param distance Travel distance (in cm)
	 * @param velocity Angular speed
	 * @param saveWay True = save the way into the map (used for map the maze). False = just move without mapping (used for go to a point when the map is known).
	 */
	public void move(int distance, int velocity, boolean saveWay) {
		vrep.simxSetIntegerSignal(clientId, "distance", distance, remoteApi.simx_opmode_oneshot);
		vrep.simxSetIntegerSignal(clientId, "vel", velocity, remoteApi.simx_opmode_oneshot);
		vrep.simxSetIntegerSignal(clientId, "cde", 1, remoteApi.simx_opmode_oneshot);

		IntW ret = new IntW(0);
		
		do {
			vrep.simxGetIntegerSignal(clientId, "return", ret, remoteApi.simx_opmode_buffer);
		} while(ret.getValue() != 1);
		
		vrep.simxSetIntegerSignal(clientId, "cde", 10, remoteApi.simx_opmode_oneshot);

		do {
			vrep.simxGetIntegerSignal(clientId, "return", ret, remoteApi.simx_opmode_buffer);
		} while(ret.getValue() != 0);

		
		Coordinate precedent = new Coordinate(coordinate);
		coordinate.move(orientation.getCardinality());
		
		//System.out.println("[PREC] " + precedent.toString() + "  [POS] " + coordinate.toString());		
		//path.printAll();
		//System.out.println("Add...");
		
		if(saveWay) {
			path.addNode(precedent, coordinate);
		}
		
		System.out.println("[move] ok " + orientation.toString() + " - " + coordinate.toString());
		System.out.println("");

	}
	


	/**
	 * Turn the E-Puck robot to the left (90°) 
	 * Function 2 on V-Rep Lua code
	 * @param velocity Angular speed (ratio of 1/10 to the speed of the move function)
	 */
	public void turnLeft(int velocity) {
		vrep.simxSetIntegerSignal(clientId, "distance", 0, remoteApi.simx_opmode_oneshot);
		vrep.simxSetIntegerSignal(clientId, "vel", velocity, remoteApi.simx_opmode_oneshot);
		vrep.simxSetIntegerSignal(clientId, "cde", 2, remoteApi.simx_opmode_oneshot);
		
		IntW ret = new IntW(0);
		
		do {
			vrep.simxGetIntegerSignal(clientId, "return", ret, remoteApi.simx_opmode_buffer);
		} while(ret.getValue() != 2);
		
		vrep.simxSetIntegerSignal(clientId, "cde", 10, remoteApi.simx_opmode_oneshot);
		
		do {
			vrep.simxGetIntegerSignal(clientId, "return", ret, remoteApi.simx_opmode_buffer);
		} while(ret.getValue() != 0);

		orientation.turnLeft();
		System.out.println("[left] ok " + orientation.toString());
	}
	
	
	/**
	 * Turn the E-Puck robot to the rigth (90°) 
	 * Function 3 on V-Rep Lua code
	 * @param velocity Angular speed (ratio of 1/10 to the speed of the move function)
	 */
	public void turnRight(int velocity) {
		vrep.simxSetIntegerSignal(clientId, "distance", 0, remoteApi.simx_opmode_oneshot);
		vrep.simxSetIntegerSignal(clientId, "vel", velocity, remoteApi.simx_opmode_oneshot);
		vrep.simxSetIntegerSignal(clientId, "cde", 3, remoteApi.simx_opmode_oneshot);
		
		IntW ret = new IntW(0);
		
		do {
			vrep.simxGetIntegerSignal(clientId, "return", ret, remoteApi.simx_opmode_buffer);
		} while(ret.getValue() != 3);
		
		vrep.simxSetIntegerSignal(clientId, "cde", 10, remoteApi.simx_opmode_oneshot);
		
		do {
			vrep.simxGetIntegerSignal(clientId, "return", ret, remoteApi.simx_opmode_buffer);
		} while(ret.getValue() != 0);

		orientation.turnRight();
		System.out.println("[right] ok " + orientation.toString());

	}
	
	
	/**
	 * Half turn the E-Puck robot to the left (180°) 
	 * Function 4 on V-Rep Lua code
	 * @param velocity Angular speed (ratio of 1/10 to the speed of the move function)
	 */
	public void uTurn(int velocity) {
		vrep.simxSetIntegerSignal(clientId, "distance", 0, remoteApi.simx_opmode_oneshot);
		vrep.simxSetIntegerSignal(clientId, "vel", velocity, remoteApi.simx_opmode_oneshot);
		vrep.simxSetIntegerSignal(clientId, "cde", 4, remoteApi.simx_opmode_oneshot);
		
		IntW ret = new IntW(0);
		
		do {
			vrep.simxGetIntegerSignal(clientId, "return", ret, remoteApi.simx_opmode_buffer);
		} while(ret.getValue() != 4);
		
		vrep.simxSetIntegerSignal(clientId, "cde", 10, remoteApi.simx_opmode_oneshot);
		
		do {
			vrep.simxGetIntegerSignal(clientId, "return", ret, remoteApi.simx_opmode_buffer);
		} while(ret.getValue() != 0);

		orientation.uTurn();
		System.out.println("[uTurn] ok " + orientation.toString());

	}
	
	
	/**
	 * Ge the state of sensors of E-Puck robot
	 * @param operationMode The type of query (opMode in V-Rep)
	 */
	private void refrechSensorsWithSettings(int operationMode) {
			IntW ret = new IntW(0);
			
			vrep.simxGetIntegerSignal(clientId, "sensors", ret, operationMode);
			
			boolean[] bits = new boolean[8];
		    for (int i = 7; i >= 0; i--) {
		        bits[i] = (ret.getValue() & (1 << i)) != 0;
		    }
		    
		    this.sensors = bits;
	}
	
	
	/**
	 * Get the state of sensors of E-Puck robot
	 */
	public void refreshSensors() {
		refrechSensorsWithSettings(remoteApi.simx_opmode_buffer);
	}
	
	
	/**
	 * Know if there is an obstacle in front of E-Puck robot
	 * @return True if an obstacle is detected on sensor 3 or 4
	 */
	public boolean isFrontObstacle() {
		refreshSensors();
		return (sensors[2] & sensors[3]);
	}
	
	
	/**
	 * Know if there is an obstacle in rigth of E-Puck robot
	 * @return True if an obstacle is detected on sensor 6
	 */
	public boolean isRightObstacle() {
		refreshSensors();
		return sensors[5];
	}
	
	
	/**
	 * Know if there is an obstacle in left of E-Puck robot
	 * @return True if an obstacle is detected on sensor 1
	 */
	public boolean isLeftObstacle() {
		refreshSensors();
		return sensors[0];
	}
	
	
	/**
	 * Get the state of sensors left, front and right
	 * @return String : "| left | front | right |", with detection = 1 and no detection = 0
	 */
	public String sensorsToString() {
		refreshSensors();
		return "| " + ((isLeftObstacle()) ? 1 : 0)+ " | " + ((isFrontObstacle()) ? 1 : 0) + " | " + ((isRightObstacle()) ? 1 : 0) + " |";
	}
	
	
	
	
	/**
	 * Go from the current point to another point
	 * @warning Requires mapping the maze
	 * @param end Destination
	 * @param distance Distance on each cell of maze
	 * @param vMove Velocity of moves
	 * @param vTurn velocity of turns (ratio of 1/10 to the speed of the move function)
	 * @throws VRepException The destination (end) does not exist
	 */
	public void goTo(Coordinate end, int distance, int vMove, int vTurn) throws VRepException {
		
		if(!path.isExisting(end)) {
			throw new VRepException("The destination does not exist : " + end.toString());
		}

		List<Coordinate> way = path.findWay(getCoordinate(), end);
		System.out.println(path.wayToString(way, "[WAY]"));
		
		Integer i = 1;
		
		while(!getCoordinate().equals(end)) {
			
			try{
			System.out.println("[GOTO]" + way.get(i).toString() + "[ACTUAL]" + getCoordinate().toString() +"[ORIENTATION]" + getOrientation().toString());
			} catch (Exception e) {
				System.out.println("erreur i:" + i + " - " + e.getMessage());
			}
			
			if(way.get(i).getX() > getCoordinate().getX()) {
				switch (getOrientation().getCardinality()) {
				case xPlus:
					break;
				case yPlus:
					turnRight(vTurn);
					break;
				case xMoins:
					uTurn(vTurn);
					break;
				case yMoins:
					turnLeft(vTurn);
					break;
				default:
					break;
				}
			} else if(way.get(i).getX() < getCoordinate().getX()) {
				switch (getOrientation().getCardinality()) {
				case xPlus:
					uTurn(vTurn);
					break;
				case yPlus:
					turnLeft(vTurn);
					break;
				case xMoins:
					break;
				case yMoins:
					turnRight(vTurn);
					break;
				default:
					break;
				}
			} else if (way.get(i).getY() > getCoordinate().getY()) {
				switch (getOrientation().getCardinality()) {
				case xPlus:
					turnLeft(vTurn);
					break;
				case yPlus:
					break;
				case xMoins:
					turnRight(vTurn);
					break;
				case yMoins:
					uTurn(vTurn);
					break;
				default:
					break;
				}
			} else if (way.get(i).getY() < getCoordinate().getY()) {
				switch (getOrientation().getCardinality()) {
				case xPlus:
					turnRight(vTurn);
					break;
				case yPlus:
					uTurn(vTurn);
					break;
				case xMoins:
					turnLeft(vTurn);
					break;
				case yMoins:
					break;
				default:
					break;
				}
			} else {
				throw new VRepException("Impossible to choose the movement to be made");
			}

			move(distance, vMove, false);
			i++;
		}


	}
	
	
	/**
	 * Get the XML tree (mapping of the maze)
	 */
	public String treeToString() {
		return path.printTree();
	}


	@Override
	public String toString() {
		return "VRepHelper [vrep=" + vrep + ", clientId=" + clientId + ", sensors=" + Arrays.toString(sensors)
				+ ", orientation=" + orientation + ", coordinate=" + coordinate + "]";
	}
	
	

}
