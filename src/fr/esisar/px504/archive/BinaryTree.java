package fr.esisar.px504.archive;

import fr.esisar.px504.simulation.Coordinate;

//BinaryTree.java
public class BinaryTree<T> {
	// Root node pointer. Will be null for an empty tree.
	@SuppressWarnings("rawtypes")
	private Node root;
	/*
--Node--
The binary tree is built using this nested node class.
Each node stores one data element, and has left and right
sub-tree pointer which may be null.
The node is a "dumb" nested class -- we just use it for
storage; it does not have any methods.
	 */
	private static class Node<T> {
		Node<T> left;
		Node<T> right;
		T data;
		Node(T newData) {
			left = null;
			right = null;
			data = newData;
		}
	}
	/**
Creates an empty binary tree -- a null root pointer.
	 */
	public BinaryTree(T data) {
		this.root = new Node<T>(data);
	}
	/**
Returns true if the given target is in the binary tree.
Uses a recursive helper.
	 */
	@SuppressWarnings("unchecked")
	public boolean lookup(T data) {
		return(lookup(root, data));
	}
	/**
Recursive lookup  -- given a node, recur
down searching for the given data.
	 */
	private boolean lookup(Node<T> node, T data) {
		if (node==null) {
			return(false);
		}
		if (data.equals(node.data)) {
			return(true);
		}
		else {
			if(lookup(node.left, data) == false) return(lookup(node.right, data));
		}

		return false;
	}


	/**
	 */
	@SuppressWarnings("unchecked")
	public Node<T> findNode(T data ) {
		return findNode(root, data);
	}

	private Node<T> findNode(Node<T> node, T data) {
		if (node==null) {
			return null;
		}
		if (data.equals(node.data)) {
			return node;
		}
		else {
			if(findNode(node.left, data) == null) return(findNode(node.right, data));
		}

		return node;
	}



	/**
Inserts the given data into the binary tree.
Uses a recursive helper.
	 */
	@SuppressWarnings("unchecked")
	public void insert(T parent, T data) {
		root = insert(root, parent, data);
	}
	/**
Recursive insert -- given a node pointer, recur down and
insert the given data into the tree. Returns the new
node pointer (the standard way to communicate
a changed pointer back to the caller).

if(this.data.equals(data)) {
    		return this;
    	}

    	if(this.left != null) {
    		if(this.left.data.equals(data)) return this.left;
    		TreeNode<T> result = left.findNode(data);
    		if (result != null) return result;
    	}
    	if(this.right != null) {
    		if(this.right.data.equals(data)) return this.right;
    		TreeNode<T> result = right.findNode(data);
    		if(result != null) return result;
    	}


	 */
	
	private String nodeToString(Node<T> node) {
		StringBuilder st = new StringBuilder("[BT][StartSearch]" + node.data.toString() + "[L]");
		
		if(node.left != null) {
			st.append(node.left.data.toString()); 
		} else { 
			st.append("null");
		}
		
		st.append("[R]");
		
		if(node.right != null) {
			st.append(node.right.data.toString()); 
		} else { 
			st.append("null");
		}
		
		return st.toString();
	}
	

	private Node<T> insert(Node<T> node, T parent, T data) {
		System.out.println(nodeToString(node) + "[Find]" + parent.toString());
		if(node.data.equals(parent)) {
			
			if(node.left == null) {
				
				node.left = new Node<T>(data);
				System.out.println("[BT][Add][LEFT]" + node.left.data.toString());
				
			} else if (node.right == null) {
				
				node.right = new Node<T>(data);
				System.out.println("[BT][Add][RIGHT]" + node.left.data.toString());
				
			} else {
				System.out.println("Children are not nulls");
			}
			
		} else {

			if(node.left != null) {
				System.out.println("[BT][GoLeft]" + node.left.data.toString());
				insert(node.left, parent, data);
				
			}
			if(node.right != null) {
				System.out.println("[BT][GoRight]" + node.right.data.toString());
				insert(node.right, parent, data);
				
			}

		}		
		return node;
	}



	//	private Node<T> insert(Node<T> node, T data) {
	//		if (node==null) {
	//			node = new Node<T>(data);
	//		}
	//		else {
	//			if (data <= node.data) {
	//				node.left = insert(node.left, data);
	//			}
	//			else {
	//				node.right = insert(node.right, data);
	//			}
	//		}
	//		return(node); // in any case, return the new pointer to the caller
	//	}
	
	
	
	/**
	 * Recursive function for find a node
	 * @param node The node to check
	 * @param data The coordinate to find
	 * @return True if the coordinate is existing
	 */
//	private boolean isExisting(Integer node, Coordinate data) {
//		if (paths.get(node) == null) {
//			return(false);
//		}
//		if (paths.get(node).equals(data)) {
//			return(true);
//		}
//		else {
//			if(isExisting(paths.get(node).left, data) == false) return(isExisting(paths.get(node).rigth, data));
//		}
//
//		return false;
//	}
//	
	
	
	@SuppressWarnings("unchecked")
	public void printTree() {
		 printTree(root);
		 System.out.println();
		}
		private void printTree(Node<T> node) {
		 if (node == null) return;
		 // left, node itself, right
		 printTree(node.left);
		 System.out.print(node.data.toString() + "  ");
		 printTree(node.right);
		}
}