import java.util.ArrayList;
import java.util.List;


public class SAP
{
	private Digraph G; 
	//Number of vertices in graph G
	private int vertices;
	
	//Constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G)
    {
    	if (G == null)
    	{
    		throw new NullPointerException("Null argument given.");
    	}
        this.G = G; 
        vertices = G.V(); 
    }

    //Length of shortest ancestral path between v and w; -1 if no such path 
    public int length(int v, int w)
    {
    	//Throws error if any argument vertex is invalid 
    	if (v < 0 || v >= vertices)
    	{
    		throw new IndexOutOfBoundsException("Given vertex is out of bounds.");
    	}
    	if (w < 0 || w >= vertices)
    	{
    		throw new IndexOutOfBoundsException("Given vertex is out of bounds.");
    	}
    	//Path from v to ancestor 
    	BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(G, v); 
    	//Path from w to ancestor
    	BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(G, w); 
    	
    	//Keeps track of the shortest ancestral path distance, will change accordingly if
    	//there is an available path. 
    	int shortestDistance = -1; 
    	//Goes through all the vertices in the digraph
    	for (int i = 0; i < vertices; i++)
    	{
    		//Check to see if both v and w have a path to the current vertex in the loop
    		if (pathV.hasPathTo(i) && pathW.hasPathTo(i))
    		{
    			//Distance is equal to the length of path from v to i plus length of path from w to i
    			int currentDistance = pathV.distTo(i) + pathW.distTo(i); 
    			
    			//Shortest distance changes if the shortest distance is currently -1 or if 
    			//the current recorded distance is less than the distance currently recorded
    			//as the shortest. 
    			if (shortestDistance == -1 || currentDistance < shortestDistance)
    			{
    				shortestDistance = currentDistance; 
    			}
    		}
    	}
    	return shortestDistance; 
    }

    //A common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w)
    {
    	//Throws error if any argument vertex is invalid 
    	if (v < 0 || v >= vertices)
    	{
    		throw new IndexOutOfBoundsException("Given vertex is out of bounds.");
    	}
    	if (w < 0 || w >= vertices)
    	{
    		throw new IndexOutOfBoundsException("Given vertex is out of bounds.");
    	}
    	//Path from v to ancestor 
    	BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(G, v); 
    	//Path from w to ancestor
    	BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(G, w); 
    	
    	//Keeps track of the vertex of the common ancestor and also the distance of the shortest path
    	int shortestAncestor = -1; 
    	int shortestDistance = -1;  
    	for (int i = 0; i < vertices; i++)
    	{
    		//Check if both vertices have a path to the common ancestor 
    		if (pathV.hasPathTo(i) && pathW.hasPathTo(i))
    		{
    			//Change the shortest path and common ancestor if the current path is shorter than
    			//the current recorded shortest path. 
    			int currentDistance = pathV.distTo(i) + pathW.distTo(i);
    			if (shortestDistance == -1 || currentDistance < shortestDistance)
    			{
    				shortestDistance = currentDistance; 
    				shortestAncestor = i; 
    			}
    		}
    	}
    	return shortestAncestor; 
    }

    //Length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w)
    {
    	//Check to see if the lists are null
    	if (v == null || w == null)
    	{
    		throw new NullPointerException("Null argument(s) given.");
    	}
    	//Path from current vertex in v to ancestor 
        BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(G, v);
        //Path from current vertex in w to ancestor 
        BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(G, w); 
        
        //Keeps track of the shortest ancestral path distance, will change accordingly if
    	//there is an available path. 
        int shortestDistance = -1; 
        //Goes through all the vertices in the digraph.
        for (int i = 0; i < vertices; i++)
        {
        	//If there is a path from current vertex in v and current vertex in w to the current 
        	//vertex in the loop...
        	if (pathV.hasPathTo(i) && pathW.hasPathTo(i))
        	{
        		//Set the current distance to the sum between the distance from current vertex in v
        		//to i and the distance from the current vertex in w to i. 
        		int currentDistance = pathV.distTo(i) + pathW.distTo(i); 
        		//If this distance is shorter than the currently recorded shortest distance,
        		//then set the new shortest distance to this new distance. 
        		if (shortestDistance == -1 || currentDistance < shortestDistance)
        		{
        			shortestDistance = currentDistance; 
        		}
        	}
        }
        return shortestDistance; 
    }

    //A common ancestor that participates in shortest ancestral path; -1 if no such path 
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {
        //Check to see if the lists are null
    	if (v == null || w == null)
    	{
    		throw new NullPointerException("Null argument(s) given.");
    	}
    	
    	//Path from current vertex in v to ancestor 
        BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(G, v);
        //Path from current vertex in w to ancestor 
        BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(G, w); 
        
        //Keeps track of the vertex of the shortest common ancestor and the distance between the 
        //common ancestor and the vertex in v and the vertex in w. 
        int shortestAncestor = -1;
        int shortestDistance = -1;
        for (int i = 0; i < vertices; i++)
        {
        	//If there is a path from current vertex in v and current vertex in w to the current 
        	//vertex in the loop...
        	if (pathV.hasPathTo(i) && pathW.hasPathTo(i))
        	{
        		//Set the current distance to the sum between the distance from current vertex in v
        		//to i and the distance from the current vertex in w to i. 
        		int currentDistance = pathV.distTo(i) + pathW.distTo(i); 
        		//If this distance is shorter than the currently recorded shortest distance,
        		//then set the new shortest distance to this new distance. Also, set the shortest
        		//ancestor vertex to the current vertex. 
        		if (shortestDistance == -1 || currentDistance < shortestDistance)
        		{
        			shortestDistance = currentDistance; 
        			shortestAncestor = i; 
        		}
        	}
        }
        return shortestAncestor; 
    }

    //Unit testing of this class 
    public static void main(String[] args)
    {
        String digraphFile = "testInput/digraph1.txt";

        In in = new In(digraphFile);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty())
        {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}