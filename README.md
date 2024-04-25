## $\textcolor{blue}{Comparitive\ Programming\ Language}$
# Description

The code is written to implement autocompletion feature, where a class maintains a dictionary of words with its popularity of occurrence and computes the top-3 most-likely completions of any input word quickly.
[Involves the concept of AVL Trees] 
``````````````````````````````````````````````````
Example
                                crocodile
                                16583
                    alpaca                       goat
                    8523                         52317
          albatross    camel              crow         goose
          5531         110050             45921        37393
````````````````````````````````````````````````````````````````````

words starting with "a" -> alpaca, albatross

words starting with "cr" ->crocodile, crow

# Requirements/Commands
Requires python/Java compiler

Java:
``````````
javac Autocompleter.java
java Autocompleter
````````````

Python:
````````````
python autocompleter.py
```````````````````

# Explanation/Comparison

##### concept #1 

STATIC OR DYNAMIC SCOPING

Considering the rebalance function, all variables used in that like cur, bf, height, left_rotate, right_rotate are defined within the Autocompleter class. Since Java uses static scoping, these variables are resolved at compile time and their scope is determined by their declaration within the class.
Java does not support dynamic scoping for local variables, but it does for instance variables.
So the cur variable is an instance variable of the Node class. Its scope is determined by the instance of Node passed to the rebalance function. 
Overall, the rebalance function primarily demonstrates static scoping, as it operates within the lexical structure of the code. The cur variable exhibits dynamic scoping to some extent, but within the confines of its instance's scope.


Let's take the insert_recurse function from the Python code to explain static and dynamic scoping.
e, cur, cR, self.height, and self.rebalance are all within the scope of the Autocompleter class. These are all accessible from within the method because they are defined within the class. Since e and cur are defined within the method signature, their scope is limited to that method.
cur parameter scope is dynamic. It is set to different values each time the function is called recursively, the value of cur changes according to the current node in the binary search tree being processed. This dynamic behavior is determined at runtime.Python resolves variables dynamically during execution, and their scope is determined at runtime.

##### concept #2

PARAMETER PASSING TECHNIQUES

Considering insert and insert_recurse function. In Java, all arguments are passed by value. When an object is passed as an argument to a method, the value of the reference to that object is passed. In the insert function 'x' and freq are primitive data type (String, int respectively), so these values are passed by value. In insert_recurse function, e is an object of type Entry. The reference to this object is passed by value. Any changes made to the object within the method will affect the original object.cur is also an object of type Node, and its reference is passed by value. The actual objects themselves are not duplicated. Any changes made to these objects inside the method will be reflected in the original objects.

Python passes arguments by object reference. In the Python code, the insert function takes x and freq as arguments, which are objects.
In Python, everything is an object, and objects are passed by reference. Thus, changes made to the objects within the function will affect the original objects outside the function. So the parameters x, freq in one function and e, cur in another function are all treated as objects.


##### concept #3

COMPILERS OR INTERPRETERS

Java code is compiled into bytecode, which is then interpreted by the Java Virtual Machine (JVM). It is statically typed, which means variable types are determined at compile time. Few best Java compilers used by developers are Eclipse,NetBeans,Intellij,XCode,Tabnine,Codenvy,BlueJ, JGrasp,Javac and Slikedit. Here, javac compiler was used for compiling the code.

Python code is interpreted by the Python interpreter directly.Python is dynamically typed, meaning variable types are determined at runtime.
Here, Visual studio code IDE is used for compiling, which comes with own Python interpreters and compilers.

Java's compilation process provides better performance and type safety but requires more verbose code, where as Python's interpreted nature offers faster development and readability but may sacrifice performance in some cases due to dynamic typing and the GIL.

##### concept #4

GARBAGE COLLECTOR

Both Java and Python use automatic garbage collection to manage memory.Both languages allow developers to focus on coding without worrying about manual memory management, but Java's garbage collector often provides better performance in larger and more complex applications.
Python's garbage collector uses reference counting and a cycle-detecting garbage collector to reclaim memory. Reference counting keeps track of the number of references to an object, and when the count drops to zero, the object is deallocated. The cycle-detecting garbage collector handles more complex cases where objects reference each other in cycles.

Considering the insert_recurse function new Node objects are created, Once these objects are no longer referenced, they become eligible for garbage collection. Similarly, In the completions_recurse function, there are no new objects being created, so there's no explicit memory allocation. However, the function operates on the ArrayList<Entry> C, which is passed by reference. The elements of this list could potentially be modified, added to, or removed from within the function. If any elements are removed from the C list, they become eligible for garbage collection once there are no more references to them.

##### concept #5

PROGRAMMING PARADIGM

Java (Object-oriented paradigm):
*Object-oriented:* In Java, the Autocompleter class is designed using an object-oriented paradigm.

*Encapsulation:* The Entry and Node classes encapsulate data and operations related to entries and nodes in the binary search tree (BST), respectively.

*State and Behavior:* The insert function is part of the Autocompleter class, which contains both data (the root node) and behavior (methods to manipulate the BST).

*Mutability:* Java allows mutable objects, so the Node objects in the BST can be modified directly, such as updating heights or rebalancing the tree.

*Imperative Style:* The code is written in an imperative style, focusing on how to achieve the desired result step by step, including mutation of state.


Python (Functional and Imperative paradigm):
*Functional:* While Python supports object-oriented programming, it also supports functional programming paradigms.

*Pure Functions:* The insert function and related methods in the Autocompleter class are pure functions as they don't modify the state of the object (self) directly.

*No Explicit Encapsulation:* Python doesn't enforce encapsulation as strictly as Java. However, encapsulation is still achieved through conventions (e.g., using _ prefix for private attributes).

*Immutability:* In Python, objects are generally treated as immutable. Instead of directly modifying the tree nodes, new nodes are created as needed.

*Recursion:* The insert_recurse and related methods use recursion heavily, which aligns with the functional programming paradigm.

Imperative Style: Despite supporting functional programming, the code also employs an imperative style, especially in the mutation of state (e.g., updating node heights and rebalancing).


*Comparison:*
Both Java and Python versions use an object-oriented approach to structure the code.Python, being a more flexible language, allows mixing paradigms, including functional and imperative. The functional aspect is more evident in the Python version due to the use of pure functions and recursion. Both versions achieve the same functionality, but the implementation details differ due to language-specific features and conventions.



