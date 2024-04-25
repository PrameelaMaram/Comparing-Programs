class Autocompleter:
    class Entry:
        def __init__(self, word, x):
            self.s = word
            self.freq = x

    class Node:
        def __init__(self, temp=None):
            self.e = temp
            self.height = 0
            self.left = None
            self.right = None

    def __init__(self):
        self.root = None

    def height(self, cur):
        if cur is None:
            return -1
        return cur.height

    def inorder(self):
        self.inorder_recurse(self.root)

    def inorder_recurse(self, cur):
        if cur is not None:
            self.inorder_recurse(cur.left)
            print(" " * self.height(cur), cur.e.s)
            self.inorder_recurse(cur.right)

    def insert(self, x, freq):
        e = self.Entry(x, freq)
        self.root = self.insert_recurse(e, self.root)

    def insert_recurse(self, e, cur):
        if cur is None:
            return self.Node(e)
        cR = (e.s > cur.e.s) - (e.s < cur.e.s)
        if cR < 0:
            cur.left = self.insert_recurse(e, cur.left)
        elif cR > 0:
            cur.right = self.insert_recurse(e, cur.right)
        else:
            return cur
        cur.height = max(self.height(cur.left), self.height(cur.right)) + 1
        return self.rebalance(cur)

    def rebalance(self, cur):
        if cur is None:
            return cur
        bf = self.height(cur.left) - self.height(cur.right)
        if bf < -1:
            if self.height(cur.right.right) >= self.height(cur.right.left):
                cur = self.left_rotate(cur)
            else:
                cur.right = self.right_rotate(cur.right)
                cur = self.left_rotate(cur)
        elif bf > 1:
            if self.height(cur.left.left) >= self.height(cur.left.right):
                cur = self.right_rotate(cur)
            else:
                cur.left = self.left_rotate(cur.left)
                cur = self.right_rotate(cur)
        return cur

    def right_rotate(self, cur):
        nR = cur.left
        t = nR.right
        nR.right = cur
        cur.left = t
        cur.height = max(self.height(cur.left), self.height(cur.right)) + 1
        nR.height = max(self.height(nR.left), self.height(nR.right)) + 1
        return nR

    def left_rotate(self, cur):
        nR = cur.right
        t = nR.left
        nR.left = cur
        cur.right = t
        cur.height = max(self.height(cur.left), self.height(cur.right)) + 1
        nR.height = max(self.height(nR.left), self.height(nR.right)) + 1
        return nR

    def size(self):
        return self.size_recurse(self.root)

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
        if cur is None:
            return
        cR = (x > cur.e.s) - (x < cur.e.s)
        if cur.e.s.startswith(x):
            if not C:
                C.append(cur.e)
            elif len(C) == 1:
                if C[0].freq < cur.e.freq:
                    t = C[0]
                    C.append(t)
                    C[0] = cur.e
                else:
                    C.append(cur.e)
            elif len(C) == 2:
                if cur.e.freq > C[0].freq:
                    C.append(C[1])
                    C[1] = C[0]
                    C[0] = cur.e
                elif cur.e.freq > C[1].freq:
                    C.append(C[1])
                    C[1] = cur.e
                else:
                    C.append(cur.e)
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
            self.completions_recurse(x,cur.left,C)
            self.completions_recurse(x,cur.right,C)    
        elif cR < 0:
            self.completions_recurse(x, cur.left, C)
        else:
            self.completions_recurse(x, cur.right, C)


def test(a):
    if a:
        print("Match!!!")
    else:
        print("Fail!")
    return


if __name__ == "__main__":
    R = []
    animals = Autocompleter()
    test(animals.size() == 0)

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

    test(animals.size() == 13)
    animals.insert("buffalo", 17808542)
    test(animals.size() == 14)

    animals.insert("deer", 10007644)
    test(animals.size() == 15)

    animals.insert("horse", 58453720)
    test(animals.size() == 16)

    animals.insert("bullfrog", 273571)
    test(animals.size() == 17)

    animals.completions("a", R)
    print(str(R))

    test(len(R) == 3)

    print(R)

    test(R[0] == "alpaca")
    test(R[1] == "aardvark")
    test(R[2] == "albatross")

  
