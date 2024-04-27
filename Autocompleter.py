class Autocompleter:
    class Entry:
        def __init__(self, word, x):
            #  A helper class called Entry that stores a string and a frequency.
            self.s = word
            self.freq = x

    class Node:
        def __init__(self, temp=None):
            #  A helper class called Node that implements a binary search tree node.
            self.e = temp
            self.height = 0
            self.left = None
            self.right = None

    def __init__(self):
        self.root = None
        # Root of the binary-search-tree-based data structure
    
    def height(self, cur):
        # A convenience method for getting the height of a subtree.
        if cur is None:
            return -1
        return cur.height

    def inorder(self):
        self.inorder_recurse(self.root)
        # A convenience method for in-order traversing the tree

    def inorder_recurse(self, cur):
        '''the parameter passing technique used here is pass by object reference, self is always passed implicitly, as a reference to the instance of the class and cur is passed by object reference, as a reference to the current Node object.'''
        if cur is not None:
            self.inorder_recurse(cur.left)
            print("         " * self.height(cur), cur.e.s)
            self.inorder_recurse(cur.right)


    # Adds a string x to the dictionary.
    # If x is already in the dictionary, does nothing.
    def insert(self, x, freq):
        e = self.Entry(x, freq)
        self.root = self.insert_recurse(e, self.root)

    # insert_recurse(e, cur) for inserting an Entry e
    # into an AVL tree rooted at cur. Runs in O(log(n)) time.
    def insert_recurse(self, e, cur):
        if cur is None:
            # Base case: if the current node is null, create a new node with the given entry and return it
            return self.Node(e)
        cR = (e.s > cur.e.s) - (e.s < cur.e.s)
        if cR < 0:
            cur.left = self.insert_recurse(e, cur.left)
            # left sub tree
        elif cR > 0:
            cur.right = self.insert_recurse(e, cur.right)
            # right sub tree
        else:
            return cur
        cur.height = max(self.height(cur.left), self.height(cur.right)) + 1
        return self.rebalance(cur)
        #In this code, when a node is replaced by a new node or modified, the old node may become unreachable. It will be collected by the garbage collector when Python's automatic memory management determines that it's no longer needed

    '''
    function rebalance() for rebalancing the AVL tree rooted at cur, helpful for insert_recurse() and should be called on every node visited during
    the search in reverse search order.
    '''
    def rebalance(self, cur):
        if cur is None:
            return cur
        bf = self.height(cur.left) - self.height(cur.right)
        # Left side is large
        if bf < -1:
            if self.height(cur.right.right) >= self.height(cur.right.left):
                cur = self.left_rotate(cur)
            else:
                cur.right = self.right_rotate(cur.right)
                cur = self.left_rotate(cur)
        # Right side is large
        elif bf > 1:
            if self.height(cur.left.left) >= self.height(cur.left.right):
                cur = self.right_rotate(cur)
            else:
                cur.left = self.left_rotate(cur.left)
                cur = self.right_rotate(cur)
        return cur

    '''
    function right_rotate(cur) for the right rotation around the node cur
    of an AVL tree (helpful for implementing rebalance).
    '''
    def right_rotate(self, cur):
        nR = cur.left
        t = nR.right
        nR.right = cur
        cur.left = t
        cur.height = max(self.height(cur.left), self.height(cur.right)) + 1
        nR.height = max(self.height(nR.left), self.height(nR.right)) + 1
        return nR
        # parameter passing technique used is pass by object reference and the scoping is lexical scoping, which is a form of static scoping.
        
    
    '''
    To fill left_rotate(cur) for the left rotation around the node cur
    of an AVL tree (helpful for implementing rebalance).
    '''
    def left_rotate(self, cur):
        nR = cur.right
        t = nR.left
        nR.left = cur
        cur.right = t
        cur.height = max(self.height(cur.left), self.height(cur.right)) + 1
        nR.height = max(self.height(nR.left), self.height(nR.right)) + 1
        return nR
    # nR and t are local variables scoped to the left_rotate function. They are created when the function is called and destroyed when the function exits.
    # cur is a parameter of the left_rotate function. Its scope is also limited to the function.
    # In this left_rotate method, when nR is reassigned to a different node and the original cur.right becomes unreachable, it becomes eligible for garbage collection.
    # Similarly, when t is reassigned to a different node, the original nR.left becomes unreachable and eligible for garbage collection.

    # size function returns the number of strings in the dictionary
    def size(self):
        return self.size_recurse(self.root)

    # size_recurse() to calculate the size of the binary tree rooted at cur recursively.
    def size_recurse(self, cur):
        if cur is None:
            return 0
        return 1 + self.size_recurse(cur.left) + self.size_recurse(cur.right)

    def completions(self, x, T):
        E = []
        self.completions_recurse(x, self.root, E)
        T.clear()
        for entry in E:
            T.append(entry.s)

    def completions_recurse(self, x, cur, C):
         # cur is a reference to the current node in the tree
         # x is the target string for completion
         # C is the list of completions

         # Base case: if cur is None, return
        if cur is None:
            return
        
        # cR is a comparison result between x and the string of the current node
        cR = (x > cur.e.s) - (x < cur.e.s)

        # If the string of the current node starts with x
        if cur.e.s.startswith(x):
            # Add the current node's entry to C if it's empty
            if not C:
                C.append(cur.e)

             # If C has one element
            elif len(C) == 1:
                # Compare frequencies and rearrange accordingly
                if C[0].freq < cur.e.freq:
                    t = C[0]
                    C.append(t)
                    C[0] = cur.e
                else:
                    C.append(cur.e)
            # If C has two elements
            elif len(C) == 2:
                # Compare frequencies and rearrange accordingly
                if cur.e.freq > C[0].freq:
                    C.append(C[1])
                    C[1] = C[0]
                    C[0] = cur.e
                elif cur.e.freq > C[1].freq:
                    C.append(C[1])
                    C[1] = cur.e
                else:
                    C.append(cur.e)
            # If C has more than two elements
            else:
                if cur.e.freq > C[0].freq:
                    C[2] = C[1]
                    C[1] = C[0]
                    C[0] = cur.e
                elif cur.e.freq > C[1].freq:
                    C[2] = C[1]
                    C[1] = cur.e
                elif len(C) >= 3:
                    if cur.e.freq > C[2].freq:
                        C[2] = cur.e
                else:
                    C.append(cur.e)
            # Recur on the left and right subtrees
            self.completions_recurse(x,cur.left,C)
            self.completions_recurse(x,cur.right,C)    

        # If x is less than the string of the current node
        elif cR < 0:
            # Recur on the left subtree
            self.completions_recurse(x, cur.left, C)
        # If x is greater than the string of the current node
        else:
            # Recur on the right subtree
            self.completions_recurse(x, cur.right, C)
        ''' x, cur, and C are parameters of the function and their scope is limited to the function completions_recurse. cR is a local variable inside the function and is used for comparison purposes. Its scope is limited to the function as well.
        t is a local variable used for temporary storage within the function. Its scope is limited to the if-else blocks where it's defined.'''


def test(a):
    if a:
        print("Match!!!")
    else:
        print("Fail!")
    return


if __name__ == "__main__":
    # R is a list to store completions
    R = []

    # animals is an instance of Autocompleter class
    animals = Autocompleter()

    # Test if the size of animals is 0
    test(animals.size() == 0)

    # Insertions into the Autocompleter
    animals.insert("aardvark", 629356)
    animals.insert("albatross", 553191)
    animals.insert("alpaca", 852363)

    animals.insert("armadillo", 393754)
    animals.insert("crow", 4592109)
    animals.insert("crocodile", 1658300)
    animals.insert("cat", 46839855)
    animals.insert("camel", 11005001)

    animals.insert("goat", 5231735)
    animals.insert("gorilla", 1931906)
    animals.insert("goose", 3739382)

    animals.insert("goatfish", 19984)
    animals.insert("giraffe", 978584)
    
    # Test if the size of animals is 13
    test(animals.size() == 13)

    # Print Inorder Traversal
    print("Inorder Traversal:::::")
    animals.inorder()
    
    # Insertions
    animals.insert("buffalo", 17808542)
    test(animals.size() == 14)

    animals.insert("deer", 10007644)
    test(animals.size() == 15)

    animals.insert("horse", 58453720)
    test(animals.size() == 16)

    animals.insert("bullfrog", 273571)
    test(animals.size() == 17)
    
    # Get completions for strings starting with 'a' and store them in list R
    
    animals.completions("a", R)
    print("Top criteria of strings starting with a are : "+ str(R))
    
    # Test if the length of list R is 3
    test(len(R) == 3)
    # Test if the completions are in the correct order
    
    test(R[0] == "alpaca")
    test(R[1] == "aardvark")
    test(R[2] == "albatross")
    
    # Get completions for strings starting with 'b' and store them in list R
    animals.completions("b", R)
    print("Top criteria of strings starting with b are : "+ str(R))

    


