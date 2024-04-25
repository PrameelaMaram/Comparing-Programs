import java.util.ArrayList;

class Autocompleter {
	public
	// A helper class that stores a string and a frequency.
	class Entry {
		public String s;
		int freq;

		Entry(String word, int x) {
			s = word;
			freq = x;
		}
	}

	// A helper class that implements a binary search tree node.
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

	// For the mandatory running times below:
	// n is the number of strings in the dictionary.
	Node root; // Root of the binary-search-tree-based data structure

	Autocompleter() {
		root = null;
	}

	// A convenience method for getting the height of a subtree.
	int height(Node cur) {
		if (cur == null)
			return -1;
		return cur.height;
	}

	// A convenience method for in-order traversing the tree.
	void inorder() {
		inorder_recurse(root);
	}

	void inorder_recurse(Node cur) {
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

	// To fill insert_recurse(e, cur) for inserting an Entry e
	// into an AVL tree rooted at cur. Runs in O(log(n)) time.
	Node insert_recurse(Entry e, Node cur) {

		if (cur == null) {
			return new Node(e);
		}
		int cR = e.s.compareTo(cur.e.s);
		if (cR < 0) {
			// left sub tree
			cur.left = insert_recurse(e, cur.left);
		} else if (cR > 0) {
			// right sub tree
			cur.right = insert_recurse(e, cur.right);
		} else {
			// Nothing to perform as entry already exists
			return cur;
		}
		cur.height = Math.max(height(cur.left), height(cur.right)) + 1;
		return rebalance(cur);
	}
	// To fill rebalance() for rebalancing the AVL tree rooted at cur.
	// Helpful for insert_recurse().
	// Should be called on every node visited during
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
	}

	// To fill right_rotate(cur) for the right rotation around the node cur
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

	// To fill left_rotate(cur) for the left rotation around the node cur
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

	// To fill size_recurse() to calculate the size of
	// the binary tree rooted at cur recursively.
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
	}

	// To fill completions_recurse(String x, Node cur, ArrayList<Entry> C) for
	// Filling C with the completions of x in the BST rooted at cur.
	// Note Entrys in C are in ascending order by their freqs.
	//
	// Must run in O(log(n) + k), where
	// -n is the size of the BST rooted at root.
	// -k is the number of Entrys in the BST rooted at cur
	// whose strings start with x.
	void completions_recurse(String x, Node cur, ArrayList<Entry> C) {

		if (cur == null)
			return;
		int cR = x.compareTo(cur.e.s);
		if (cur.e.s.startsWith(x)) {
			// System.out.println("starts");
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
}

public class autoCompleterClass {

	public static void main(String[] args) {

		ArrayList<String> R = new ArrayList<String>();
		Autocompleter animals = new Autocompleter();
		test(animals.size() == 0);

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
		test(animals.size() == 13);
		animals.inorder();
		animals.insert("buffalo", 17808542);
		test(animals.size() == 14);
		animals.insert("deer", 10007644);
		test(animals.size() == 15);
		animals.insert("horse", 58453720);
		test(animals.size() == 16);
		animals.insert("bullfrog", 273571);
		test(animals.size() == 17);
		animals.completions("a", R);

		System.out.println(R);
		test(R.size() == 3);

		test(R.get(0).equals("alpaca"));
		test(R.get(1).equals("aardvark"));
		test(R.get(2).equals("albatross"));

	}

	public static void test(Boolean a) {

		if (a) {
			System.out.println("Match!!!");
		} else
			System.out.println("Fail!");
		return;
	}

}
