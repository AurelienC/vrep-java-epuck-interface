package fr.esisar.px504.archive;




public class TreeNode<T> {

	// Variables
    T data;
    TreeNode<T> parent;
    TreeNode<T> left;
    TreeNode<T> right;

    
    // Constructors
    public TreeNode(T data) {
        this.data = data;
    }
    
    public TreeNode(T data, TreeNode<T> left, TreeNode<T> right) {
    	this.data = data;
    	this.left = left;
    	this.right = right;
    }

    public TreeNode(T data, TreeNode<T> parent) {
    	this.data = data;
    	this.parent = parent;
    }
    
    public TreeNode(T data, TreeNode<T> parent, TreeNode<T> left, TreeNode<T> right) {
    	this.data = data;
    	this.parent = parent;
    	this.left = left;
    	this.right = right;
    }
    
    

    public TreeNode<T> addLink(T data) throws Exception {
    	//if(!data.equals(this.left.data) & !data.equals(this.right.data)) { 		// test si déjà inséré
    		if(this.left == null) { 									// test si un de libre
    			this.left = new TreeNode<T>(data, this);
    			System.out.println("[TREE] add left (" + this.data.toString() + ") -> (" + this.left.data.toString());
    			return this.left;
    			
    		} else if (this.right == null) {
    			this.right = new TreeNode<T>(data, this);
    			System.out.println("[TREE] add rigth (" + this.data.toString() + ") -> (" + this.right.data.toString());
    			return this.right;
    		} else {
    			throw new Exception("No possibility for add a new child.\r\n" + "[ACTUAL] " + this.data.toString() + "\r\n[LEFT] " + left.data.toString() + "\r\n[RIGHT] " + right.data.toString());
    		}
    	//}
    	
    }


    public String toString() {
    	StringBuilder str = new StringBuilder();
    	
    	str.append("<node id=\"" + data.toString() + "\" >\r\n");

   		if(left != null) str.append("" + left.toString() + "");
   		if(right != null) str.append("" + right.toString() + "");

    		

    	//str.append("[/" + this.data.toString() + "]\r\n\r\n");
    	str.append("</node>\r\n");
    	
    	return str.toString();
    		
    } 
    

    
//    visiterPréfixe(Arbre A) :
//        visiter (A)
//        Si nonVide (gauche(A))
//              visiterPréfixe(gauche(A))
//        Si nonVide (droite(A))
//              visiterPréfixe(droite(A))
    
    
    public TreeNode<T> findNode(T data) {
    	
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
    	
    	return null;
    }


}
