


### Q&A

##  Add weighted edges support in your lib.

Weight is a function that return how much cost travel throw specific Edge. U
sed to compare with other edges during A* pathfinding 
algorithm

##  Add traverse function that will take a user defined function and apply it on every vertex of the graph.

That would be realisation of visitor pattern

 - if available to take all vertex directly from database. let's keep it simple.
 Simple select query can do the job. Receive data from database by chunks to optimise process.
 - If not than need to define some Vertex to start search neighbor Vertices. Search should be done following way 
Implement method with arguments "Vertex visitMe", "Set<Vertex> alreadyVisited". 
The method should return Set<Vertex> without "alreadyVisited". 
   -  Optimisation: No need to store parents of parents! Implement index "deep". To count how far from root Vertex we are. 
   Iterate every time we collect new level of neighbors. Clean up "Set<Vertex>alreadyVisited" every second "deep".   

##  Make you graphs thread safe.
 - Use thread safe collections like "ConcurrentHashMap" or "java.util.Collections.synchronizedCollection(java.util.Collection<T>)" 
 - Or synchronize lock on modification
    ```
         Object lockObject = new Object();
         void modifySomething() {
           synchronized (lockObject) {
              ...do the job   
           }
         }
    ```