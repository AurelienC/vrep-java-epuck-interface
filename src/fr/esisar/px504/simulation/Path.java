package fr.esisar.px504.simulation;

import java.util.ArrayList;
import java.util.List;



/**
 * Path class
 * Manage all things about ways, cells, mapping into the maze
 * @author acadiou
 *
 */
public class Path {

	/**
	 * Node class
	 * Represent a cell in a maze with a parent and, eventually, two children
	 * @author acadiou
	 *
	 */
	private static class Node {
		private Integer parent;
		private Integer left;
		private Integer rigth;



		@SuppressWarnings("unused")
		public Integer getParent() {
			return parent;
		}


		@SuppressWarnings("unused")
		public void setParent(Integer parent) {
			this.parent = parent;
		}

		@SuppressWarnings("unused")
		public Integer getLeft() {
			return left;
		}
		
		public void setLeft(Integer left) {
			this.left = left;
		}

		@SuppressWarnings("unused")
		public Integer getRigth() {
			return rigth;
		}



		public void setRigth(Integer rigth) {
			this.rigth = rigth;
		}



		public Node(Integer parent) {
			this.parent = parent;
			this.left = null;
			this.rigth = null;
		}

		@SuppressWarnings("unused")
		public Node getNode() {
			return this;
		}
	}

	// Variables
	private List<Node> paths;
	private List<Coordinate> cells;

	// Constructor

	/**
	 * Create the path object with the initial cell (initial position of robot)
	 * @param data The initial cell
	 */
	public Path(Coordinate data) {
		super();
		this.paths = new ArrayList<Node>();
		this.paths.add(new Node(null));
		this.cells = new ArrayList<Coordinate>();
		this.cells.add(data);
	}


	/**
	 * Check if a coordinate is mapped
	 * @param data The coordinate to check
	 * @return True if the coordinate is existing
	 */
	public boolean isExisting(Coordinate data) {
		return cells.contains(data);
	}





	/**
	 * Get index of a mapped coordinate
	 * @param data Coordinate to find
	 * @return -1 if not found
	 */
	public Integer getIndex(Coordinate data) {
		return cells.indexOf(data);
	}


	/**
	 * Print all nodes mapped
	 */
	public void printAll() {
		for(int i = 0; i < cells.size(); i++) {
			StringBuilder st = new StringBuilder("[IDX]" + i + "[XY]" + cells.get(i).toString() + "[PARENT]");

			//Parent
			if(paths.get(i).parent != null) {
				st.append(paths.get(i).parent);
			} else {
				st.append("null");				
			}

			//Left
			st.append("[LEFT]");
			if(paths.get(i).left != null) {
				st.append(paths.get(i).left);
			} else {
				st.append("null");				
			}

			//Right
			st.append("[RIGHT]");
			if(paths.get(i).rigth != null) {
				st.append(paths.get(i).rigth);
			} else {
				st.append("null");				
			}


			System.out.println(st.toString());

		}
	}


	/**
	 * Add a node in the tree
	 * If the node is already added (same coordinate), the node isn't added
	 * @param parent Parent to add the node as a child
	 * @param data Coordinate to add
	 * @return Index of added node
	 */
	public Integer addNode(Coordinate parent, Coordinate data) {
		if(cells.indexOf(data) >= 0) {
			System.out.println("existe déjà");
			return cells.indexOf(data);

		}

		Integer indexParent = cells.indexOf(parent);

		//System.out.println("[PATH][PARENT]" + parent.toString() + "[INDEX]" + indexParent + "[ADD]" + data.toString());

		if(indexParent < 0) {
			System.out.println("NOT FIND ! " + cells.get(1).toString());
		}

		Node nodeParent = paths.get(indexParent);	

		if(paths.get(indexParent).left == null) {
			cells.add(new Coordinate(data));
			paths.add(new Node(indexParent));
			nodeParent.setLeft(cells.lastIndexOf(data));
			paths.set(indexParent, nodeParent);

			return cells.lastIndexOf(data);

		} else if(paths.get(indexParent).rigth == null) {
			cells.add(new Coordinate(data));
			paths.add(new Node(indexParent));
			nodeParent.setRigth(cells.lastIndexOf(data));
			paths.set(indexParent, nodeParent);

			return cells.lastIndexOf(data);

		} else {
			System.out.println("[ERROR] No children free.");
		}
		return null;
	}


	/**
	 * Get tree (paths) in XML format
	 * @return
	 */
	public String printTree() {
		return toXML(0);
	}


	/**
	 * Recursive function for get the tree (paths) in XML format
	 * @param node 
	 * @return
	 */
	private String toXML(Integer node) {
		StringBuilder str = new StringBuilder();

		str.append("\r\n<node id=\"" + node + "\" coordinates=\"" + cells.get(node).toString() + "\" >");

		if (paths.get(node) == null) {
			return "";
		} else if (paths.get(node).left != null) {
			str.append(toXML(paths.get(node).left));
			str.append("\r\n");
		}
		if (paths.get(node).rigth != null) {
			str.append(toXML(paths.get(node).rigth));
			str.append("\r\n");
		}

		str.append("</node>");

		return str.toString();
	}



	/**
	 * Find the best way between two points (cells)
	 * @param startCoordinate Coordinates of departure
	 * @param endCoordinate Coordinate of arrival
	 * @return List of coordinate to follow
	 */
	public List<Coordinate> findWay(Coordinate startCoordinate, Coordinate endCoordinate) {

		Integer start = cells.indexOf(startCoordinate);
		Integer end = cells.indexOf(endCoordinate);

		List<Integer> listStart = new ArrayList<Integer>();
		List<Integer> listEnd = new ArrayList<Integer>();
		List<Integer> listFinal = new ArrayList<Integer>();

		listStart.add(start);
		listEnd.add(end);
		listFinal.add(start);


		Integer index = start;

		// Parcours départ -> (0,0)
		while(index != 0) {
			index = paths.get(index).parent;
			listStart.add(index);
		}


		index = end;

		// Parcours arrivée -> point d'intersection avec le parcours "départ"
		while(!listStart.contains(index)) {
			index = paths.get(index).parent;
			listEnd.add(index);
		}		
		Integer crossPoint = index;
		Integer i = 1;

		// Ajout de la liste de départ jusqu'au point d'intersection. Départ -> intersection avec le parcours d'arrivée
		while(listStart.size() - 1 > i) {

			listFinal.add(listStart.get(i));
			if(listStart.get(i) == crossPoint)
				break;		
			i++;
		}

		i = listEnd.size() - 2;

		// Ajout de la liste d'arrivée jusqu'au point d'intersection. Point d'intersection -> Arrivée
		while(i > 0) {
			listFinal.add(listEnd.get(i));
			i--;
		}
		
		listFinal.add(end);

		List<Coordinate> listCoordinates = new ArrayList<Coordinate>();

		for (Integer idx : listFinal) {
			listCoordinates.add(cells.get(idx));
		}
		
		return listCoordinates;
	}




	/**
	 * Get a string with the path (indexes)
	 * @param path The path (list of indexes)
	 * @param st Add a string into each line
	 * @return The way [PATH]st[INDEX]idx[XY](x,y)
	 */
	@SuppressWarnings("unused")
	private String wayIndexesToString(List<Integer> path, String st) {
		StringBuilder sb = new StringBuilder();

		for (Integer index : path) {
			sb.append("[PATH]" + st + "[INDEX]" + index + "[XY]" + cells.get(index).toString());
		}
		
		return sb.toString();
	}

	/**
	 * Get a string with the path (coordinates)
	 * @param path The path (list of coordinates)
	 * @param st Add a string into each line
	 * @return The way [PATH]st[INDEX]idx[XY](x,y)
	 */
	public String wayToString(List<Coordinate> path, String st) {
		StringBuilder sb = new StringBuilder();
		Integer i = 0;
		
		for (Coordinate index : path) {
			sb.append("[PATH]" + st + "[INDEX]" + i + "[XY]" + index.toString());
			i++;
		}
		
		return sb.toString();
	}





}
