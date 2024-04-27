import java.util.ArrayList;

class Autocompleter {
	public
	// A helper class called Entry that stores a string and a frequency.
	class Entry {
		public String s;
		int freq;

		Entry(String word, int x) {
			s = word;
			freq = x;
		}
	}

	// A helper class called Node that implements a binary search tree node.
	class Node {
		public Node() {
			height = 0;
			left = right = null;
		}

		Node(Entry temp) {
			e = temp;
			height = 0;
			left = right = null;
		}

		Entry e;
		int height;
		Node left;
		Node right;
	}


	Node root; // Root of the binary-search-tree-based data structure

	Autocompleter() {
		root = null;
	}

	// A convenience method for getting the height of a subtree.
	int height(Node cur) {
		//Pass by value: Node object is passed as an argument. 
		//Although a copy of the reference is passed to the function, changes to the object's attributes (like height) inside the function will affect the original object since both the copy and the original reference point to the same object in memory.
        if (cur == null)
			return -1;
		return cur.height;
	}

	// A convenience method for in-order traversing the tree.
	void inorder() {
		inorder_recurse(root);
	}
    
	//To disaply the elements in tree in Order traversal way
	void inorder_recurse(Node cur) {
		//parameter passing technique is pass by value
		if (cur != null) {
			inorder_recurse(cur.left);
			for (int i = 0; i < height(cur); i++)
				System.out.print("         ");
			System.out.println(cur.e.s);
			inorder_recurse(cur.right);
		}
	}

	// Adds a string x to the dictionary.
	// If x is already in the dictionary, does nothing.
	void insert(String x, int freq) {
		Entry e = new Entry(x, freq);
		root = insert_recurse(e, root);
	}

	// function insert_recurse(e, cur) for inserting an Entry e into an AVL tree rooted at cur. 
	// Runs in O(log(n)) time.

	Node insert_recurse(Entry e, Node cur) {
		// Base case: if the current node is null, create a new node with the given entry and return it
		if (cur == null) {
			return new Node(e);
		}
		// Compare the entry with the current node's entry
		int cR = e.s.compareTo(cur.e.s);
		if (cR < 0) {
			// If the entry is smaller, go to the left subtree
			cur.left = insert_recurse(e, cur.left);
		} else if (cR > 0) {
			// If the entry is larger, go to the right subtree
			cur.right = insert_recurse(e, cur.right);
		} else {
			// If the entry already exists, do nothing and return the current node
			return cur;
		}
		// Update the height of the current node
		cur.height = Math.max(height(cur.left), height(cur.right)) + 1;
		// Rebalance the tree and return the updated node
		return rebalance(cur);

		//the original references to e and cur remain unchanged in the calling context. Therefore, the parameter passing technique used here is pass by value.
	}
	
	// function rebalance() for rebalancing the AVL tree rooted at cur based on the balance factor bf
	// This is helpful for insert_recurse() and should be called on every node visited during
	// the search in reverse search order.

	Node rebalance(Node cur) {
		if (cur == null)
			return cur;
		int bf = height(cur.left) - height(cur.right);
		// Left side is large
		if (bf < -1) {
			if (height(cur.right.right) >= height(cur.right.left)) {
				cur = left_rotate(cur);
			} else {
				cur.right = right_rotate(cur.right);
				cur = left_rotate(cur);
			}
		}
		// Right side is large
		else if (bf > 1) {
			if (height(cur.left.left) >= height(cur.left.right)) {
				cur = right_rotate(cur);
			} else {
				cur.left = left_rotate(cur.left);
				cur = right_rotate(cur);
			}
		}
		return cur;
		/*cur, bf, height, left_rotate, right_rotate are defined within the Autocompleter class. Since Java uses static scoping, these variables are resolved at compile time and their scope is determined by their declaration within the class. */
		//the parameter passing used in this code is pass by value, where the value being passed is a reference to the Node object. Any changes made to the parameter cur inside the function won't affect the original reference passed by the caller
	}

	// function right_rotate(cur) is used for the right rotation around the node cur
	// of an AVL tree (helpful for implementing rebalance).
	Node right_rotate(Node cur) {
		Node nR = cur.left;
		Node t = nR.right;
		nR.right = cur;
		cur.left = t;
		cur.height = Math.max(height(cur.left), height(cur.right)) + 1;
		nR.height = Math.max(height(nR.left), height(nR.right)) + 1;
		return nR;
		/*Here, cur, nR, and t are all variables scoped within the right_rotate() function. Their scope is determined by the function's declaration. They are not accessible outside of this function. */
	}

	// function left_rotate(cur) is used for the left rotation around the node cur
	// of an AVL tree (helpful for implementing rebalance).
	Node left_rotate(Node cur) {
		Node nR = cur.right;
		Node t = nR.left;
		nR.left = cur;
		cur.right = t;
		cur.height = Math.max(height(cur.left), height(cur.right)) + 1;
		nR.height = Math.max(height(nR.left), height(nR.right)) + 1;
		return nR;
		/*Here, cur, nR, and t are all variables scoped within the left_rotate() function. Their scope is determined by the function's declaration. They are not accessible outside of this function. */
	}

	// Returns the number of strings in the dictionary
	int size() {
		return size_recurse(root);
	}

	// size_recurse() to calculate the size of the binary tree rooted at cur recursively.
	int size_recurse(Node cur) {
		if (cur == null)
			return 0;
		return 1 + size_recurse(cur.left) + size_recurse(cur.right);
	}

	// Function completions(x, T) fills the Arraylist T with the three most-frequent
	// completions of x.
	// If x has less than three completions, then
	// T is filled with all completions of x.
	// The completions appear in T from most to least frequent.
	void completions(String x, ArrayList<String> T) {
		ArrayList<Entry> E = new ArrayList<Entry>();
		completions_recurse(x, root, E);
		T.clear();
		for (int i = 0; i < E.size(); i++) {
			T.add(E.get(i).s);
		}
		/*both x and T are passed by value. While x is just a primitive type String, T is an object of type ArrayList<String>. 
		Changes made to T inside the method (T.clear()) affect the original object that T refers to, 
		but the reference itself (the value of T) remains unchanged outside the method. */
	}

	// completions_recurse(String x, Node cur, ArrayList<Entry> C) for
	// Filling C with the completions of x in the BST rooted at cur.
	// Will run in O(log(n) + k), where n is the size of the BST rooted at root, k is the number of Entrys in the BST rooted at cur whose strings start with x.

	void completions_recurse(String x, Node cur, ArrayList<Entry> C) {

		if (cur == null)
			return;
		int cR = x.compareTo(cur.e.s);
		if (cur.e.s.startsWith(x)) {
			
			if (C.isEmpty()) {
				C.add(cur.e);
			} else if (C.size() == 1) {
				if (C.get(0).freq < cur.e.freq) {
					Entry t = C.get(0);
					C.add(t);
					C.set(0, cur.e);

				} else {
					C.add(cur.e);
				}
			} else if (C.size() == 2) {
				if (cur.e.freq > C.get(0).freq) {
					C.add(C.get(1));
					C.set(1, C.get(0));
					C.set(0, cur.e);

				}

				else if (cur.e.freq > C.get(1).freq) {
					C.add(C.get(1));
					C.set(1, cur.e);
				} else
					C.add(cur.e);
			} else {
				if (cur.e.freq > C.get(0).freq) {
					C.set(2, C.get(1));
					C.set(1, C.get(0));
					C.set(0, cur.e);
					// C.addFirst(cur.e);
				}

				else if (cur.e.freq > C.get(1).freq) {
					C.set(2, C.get(1));
					C.set(1, cur.e);
				} else if (C.size() == 3) {
					if (cur.e.freq > C.get(0).freq) {
						C.set(2, C.get(1));
						C.set(1, C.get(0));
						C.set(0, cur.e);
					} else if (cur.e.freq > C.get(1).freq) {
						C.set(2, C.get(1));
						C.set(1, cur.e);
					} else if (cur.e.freq > C.get(2).freq)
						C.set(2, cur.e);
				} else {
					C.add(cur.e);
				}
			}

			completions_recurse(x, cur.left, C);
			completions_recurse(x, cur.right, C);
		} else if (cR < 0) {
			// System.out.println("lesser than "+cur.e.s);
			completions_recurse(x, cur.left, C);
		} else { // System.out.println("greater "+cur.e.s);
			completions_recurse(x, cur.right, C);
		}

	}
	/*Changes to x,cur inside the function won't affect the original string and Node refernece respectively.
    However, Changes to the attributes of the Node object referred to by 'cur' will affect the original Node object.
    Changes to the elements of the ArrayList C inside the function will affect the original ArrayList in the caller. */
}


public class autoCompleterClass {

	public static void main(String[] args) {

		// Create an empty ArrayList R
		ArrayList<String> R = new ArrayList<String>();
		
		// Create an instance of the Autocompleter class
		Autocompleter animals = new Autocompleter();
		
		// Test if the size of the Autocompleter is 0
		test(animals.size() == 0);

		// Insert some entries into the Autocompleter
		animals.insert("aardvark", 629356);
		animals.insert("albatross", 553191);
		animals.insert("alpaca", 852363);
		animals.insert("armadillo", 393754);
		animals.insert("crow", 4592109);
		animals.insert("crocodile", 1658300);
		animals.insert("cat", 46839855);
		animals.insert("camel", 11005001);
		animals.insert("goat", 5231735);
		animals.insert("gorilla", 1931906);
		animals.insert("goose", 3739382);
		animals.insert("goatfish", 19984);
		animals.insert("giraffe", 978584);
		
		// Test if the size of the Autocompleter is 13
		test(animals.size() == 13);
		
		// Print the entries in the Autocompleter in inorder traversal
		System.out.println("Inorder Traversal:::::");
		animals.inorder();
		
		// Insert more entries into the Autocompleter
		animals.insert("buffalo", 17808542);
		test(animals.size() == 14);
		animals.insert("deer", 10007644);
		test(animals.size() == 15);
		animals.insert("horse", 58453720);
		test(animals.size() == 16);
		animals.insert("bullfrog", 273571);
		test(animals.size() == 17);
		
		// Get completions for string "a" and store them in ArrayList R
		animals.completions("a", R);

		// Print the contents of ArrayList R
		System.out.println("Top criteria of strings starting with a are : "+ R);
		
		// Test if the size of ArrayList R is 3
		test(R.size() == 3);

		// Test if the completions are in the correct order
		test(R.get(0).equals("alpaca"));
		test(R.get(1).equals("aardvark"));
		test(R.get(2).equals("albatross"));


		animals.completions("b", R);
		System.out.println("Top criteria of strings starting with b are : "+ R);
	}

	// Method to perform test and print result
	public static void test(Boolean a) {

		if (a) {
			System.out.println("Match!!!");
		} else
			System.out.println("Fail!");
		return;
	}

}
