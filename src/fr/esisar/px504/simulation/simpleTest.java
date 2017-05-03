package fr.esisar.px504.simulation;


//Make sure to have the server side running in V-REP: 
//in a child script of a V-REP scene, add following command
//to be executed just once, at simulation start:
//
//simExtRemoteApiStart(19999)
//
//then start simulation, and run this program.
//
//IMPORTANT: for each successful call to simxStart, there
//should be a corresponding call to simxFinish at the end!

public class simpleTest
{


	private static VRepHelper v;

	private final static int MOVE = 23;
	private final static int SPEED = 7;
	private final static int RSPEED = 2;
	

	public static void main(String[] args)
	{
		
		System.out.println("Program started");

		v = new VRepHelper(19997);

		try
		{
			v.startSimulation();
			time((long)1*1000);

			vrep(new Coordinate(3,1));
			
						
			//
			//			path.addNode(new Coordinate(0, 0), new Coordinate(0, 1)); // 1
			//
			//			path.addNode(new Coordinate(0, 1), new Coordinate(-1, 1)); // 2
			//
			//			path.addNode(new Coordinate(-1, 1), new Coordinate(-1, 0)); // 3
			//			path.addNode(new Coordinate(-1, 1), new Coordinate(-1, 2)); // 4
			//
			//			path.addNode(new Coordinate(-1, 2), new Coordinate(0, 2)); // 5
			//
			//			path.addNode(new Coordinate(0, 2), new Coordinate(0, 3)); // 6
			//
			//			path.addNode(new Coordinate(0, 3), new Coordinate(-1, 3)); // 7
			//			path.addNode(new Coordinate(0, 3), new Coordinate(1, 3)); // 8


			v.goTo(new Coordinate(0, 0), MOVE, SPEED, RSPEED);
		} catch (VRepException e) {
			System.out.println("[EXCEPTION]" + e.getMessage());
		}



		System.out.println("fin");

		time((long)5*1000);

		v.stopSimulation();


		System.out.println("End of program");

	}




	static public void time(Long t) {

		try
		{
			Thread.sleep(t);
		}
		catch(InterruptedException ex)
		{
			Thread.currentThread().interrupt();
		}

	}


	static public void vrep(Coordinate end) {

	
	while (!v.getCoordinate().equals(end)) {
		//System.out.println(i);
			System.out.println(v.sensorsToString());
			//v.coordinatepresent();
			//time((long)1*1000);
					if ((v.isLeftObstacle()==true && v.isFrontObstacle()==false && v.isRightObstacle()==true)	|| (v.isLeftObstacle()==true && v.isFrontObstacle()==false && v.isRightObstacle()==false))
				{
						//System.out.println(v.sensorsToString());
						v.move(MOVE, SPEED,true);
						
				}
					
				else if (v.isLeftObstacle()==true && v.isFrontObstacle()==true && v.isRightObstacle()==false) 
				{
					//v.move(03, 3);
					v.turnRight(RSPEED);
					v.move(MOVE, SPEED,true);
					
				}
				
				else if ((v.isRightObstacle()==true && v.isFrontObstacle()==true && v.isLeftObstacle()==false) ||
						 (v.isRightObstacle()==false && v.isFrontObstacle()==true && v.isLeftObstacle()==false) ||
						 (v.isRightObstacle()==true && v.isFrontObstacle()==false && v.isLeftObstacle()==false))
						
				{
					
					//v.move(03, 3);
					v.turnLeft(RSPEED);
					v.move(MOVE, SPEED,true);
				
				}
				else if (v.isLeftObstacle()==true && v.isFrontObstacle()==true && v.isRightObstacle()==true) 
				{
					v.uTurn(RSPEED);
					//v.turnLeft(RSPEED);
					//v.turnLeft(RSPEED);
					v.move(MOVE, SPEED,true);
					
				}	
					
				
		
		//return false;
		
	//}
		
		
	/*	v.move(MOVE, SPEED, true);
		v.turnLeft(RSPEED);
		v.move(MOVE, SPEED, true);
		v.turnRight(RSPEED);	
		v.move(MOVE, SPEED, true);
		v.turnRight(RSPEED);	
		v.move(MOVE, SPEED, true);
		v.turnLeft(RSPEED);
		v.move(MOVE, SPEED, true);
		v.turnLeft(RSPEED);	
		v.move(MOVE, SPEED, true);
		v.turnRight(RSPEED);	
		v.turnRight(RSPEED);	
		v.move(MOVE, SPEED, true);
		v.move(MOVE, SPEED, true);
		v.turnRight(RSPEED);	
		v.move(MOVE, SPEED, true);
		v.move(MOVE, SPEED, true);
		v.move(MOVE, SPEED, true);
		v.turnLeft(RSPEED);	
		v.move(MOVE, SPEED, true);
		v.turnLeft(RSPEED);	
		v.move(MOVE, SPEED, true);
		v.turnRight(RSPEED);	
		v.move(MOVE, SPEED, true);
*/

		// Tree
		System.out.println("Arbre" + v.treeToString());


		time((long)(5*100));

	}
}
}


//package fr.esisar.px504.simulation;
//
////Make sure to have the server side running in V-REP: 
////in a child script of a V-REP scene, add following command
////to be executed just once, at simulation start:
////
////simExtRemoteApiStart(19999)
////
////then start simulation, and run this program.
////
////IMPORTANT: for each successful call to simxStart, there
////should be a corresponding call to simxFinish at the end!
//
//public class simpleTest
//{
//
//
//	private static VRepHelper v;
//
//	private final static int MOVE = 23;
//	private final static int SPEED = 10;
//	private final static int RSPEED = 1;
//
//
//	public static void main(String[] args)
//	{
//		System.out.println("Program started");
//
//		v = new VRepHelper(19997);
//
//		try
//		{
//			v.startSimulation();
//			time((long)1*1000);
//
//			vrep();
//			//
//			//			path.addNode(new Coordinate(0, 0), new Coordinate(0, 1)); // 1
//			//
//			//			path.addNode(new Coordinate(0, 1), new Coordinate(-1, 1)); // 2
//			//
//			//			path.addNode(new Coordinate(-1, 1), new Coordinate(-1, 0)); // 3
//			//			path.addNode(new Coordinate(-1, 1), new Coordinate(-1, 2)); // 4
//			//
//			//			path.addNode(new Coordinate(-1, 2), new Coordinate(0, 2)); // 5
//			//
//			//			path.addNode(new Coordinate(0, 2), new Coordinate(0, 3)); // 6
//			//
//			//			path.addNode(new Coordinate(0, 3), new Coordinate(-1, 3)); // 7
//			//			path.addNode(new Coordinate(0, 3), new Coordinate(1, 3)); // 8
//
//
//			v.goTo(new Coordinate(0, 0), MOVE, SPEED, RSPEED);
//		} catch (VRepException e) {
//			System.out.println("[EXCEPTION]" + e.getMessage());
//		}
//
//
//
//		System.out.println("fin");
//
//		time((long)5*1000);
//
//		v.stopSimulation();
//
//
//		System.out.println("End of program");
//
//	}
//
//
//
//
//	static public void time(Long t) {
//
//		try
//		{
//			Thread.sleep(t);
//		}
//		catch(InterruptedException ex)
//		{
//			Thread.currentThread().interrupt();
//		}
//
//	}
//
//
//	static public void vrep() {
//
//		v.move(MOVE, SPEED, true);
//		v.turnLeft(RSPEED);
//		v.move(MOVE, SPEED, true);
//		v.turnRight(RSPEED);	
//		v.move(MOVE, SPEED, true);
//		v.turnRight(RSPEED);	
//		v.move(MOVE, SPEED, true);
//		v.turnLeft(RSPEED);
//		v.move(MOVE, SPEED, true);
//		v.turnLeft(RSPEED);	
//		v.move(MOVE, SPEED, true);
//		v.turnRight(RSPEED);	
//		v.turnRight(RSPEED);	
//		v.move(MOVE, SPEED, true);
//		v.move(MOVE, SPEED, true);
//		v.turnRight(RSPEED);	
//		v.move(MOVE, SPEED, true);
//		v.move(MOVE, SPEED, true);
//		v.move(MOVE, SPEED, true);
//		v.turnLeft(RSPEED);	
//		v.move(MOVE, SPEED, true);
//		v.turnLeft(RSPEED);	
//		v.move(MOVE, SPEED, true);
//		v.turnRight(RSPEED);	
//		//v.move(MOVE, SPEED, true);
//
//
//		// Tree
//		System.out.println("Arbre" + v.treeToString());
//
//
//		time((long)(5*1000));
//
//	}
//}
