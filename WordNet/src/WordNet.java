import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
public class WordNet
{
    private HashMap<String, List> nounsMap;
    private HashMap<Integer, String> synsetMap;
    private List<String> nounsList;
    private Digraph graph;
    private SAP sap;
	
	public WordNet(String synsets, String hypernyms)
    {
        // TODO: You may use the code below to open and parse the
        // synsets and hypernyms file. However, you MUST add your
        // own code to actually store the file contents into the
        // data structures you create as fields of the WordNet class.

        // Parse synsets
    	
		// Check for null parameters
		if ((synsets == null) || (hypernyms == null)) {
			throw new NullPointerException();
		}
		
		nounsMap = new HashMap<String, List>();
		synsetMap = new HashMap<Integer, String>();
		nounsList = new ArrayList<String>();
        int largestId = -1; // TODO: You might find this value useful
        In inSynsets = new In(synsets);
        while (inSynsets.hasNextLine())
        {
            String line = inSynsets.readLine();
            String[] tokens = line.split(",");

            // Synset ID
            int id = Integer.parseInt(tokens[0]);
            if (id > largestId)
            {
                largestId = id;
            }

            // synset to map and digraph
            String synset = tokens[1];
            synsetMap.put(id, synset);
            
            // Nouns in synset
            String[] nouns = synset.split(" ");
            for (String noun : nouns)
            {
                // TODO: you should probably do something here
            	if (!(nounsMap.containsKey(noun))) {
            		nounsMap.put(noun, new ArrayList<Integer>());
            	}
            	nounsMap.get(noun).add(id);
            	//nounsMap.put(noun, id);
            	nounsList.add(noun);
            }

            // tokens[2] is gloss, but we're not using that
        }
        inSynsets.close();
        
        graph = new Digraph(largestId + 1);
        
        // Parse hypernyms
        In inHypernyms = new In(hypernyms);
        while (inHypernyms.hasNextLine())
        {
            String line = inHypernyms.readLine();
            String[] tokens = line.split(",");

            int v = Integer.parseInt(tokens[0]);
            int w = 0;
            for (int i = 1; i < tokens.length; i++)
            {
                // TODO: you should probably do something here
            	w = Integer.parseInt(tokens[i]);
            	graph.addEdge(v, w);
            }
        }
        inHypernyms.close();
        
        /*top = new Topological(graph);
        if (top.order() == null) throw new IllegalArgumentException();
        cycle = new DirectedCycle(graph);
        if (cycle.hasCycle()) throw new IllegalArgumentException();*/
        
        DirectedCycle cycle = new DirectedCycle(graph);
        if (cycle.hasCycle() || !rootedDAG(graph)) {
            throw new IllegalArgumentException();
        }
        
        sap = new SAP(graph);
        
        // TODO: Remember to remove this when your constructor is done!
        // throw new UnsupportedOperationException();
    }
	
	private boolean rootedDAG(Digraph g) {
        int roots = 0;
        for (int i = 0; i < g.V(); i++) {
            if (!g.adj(i).iterator().hasNext()) {
                roots++;
                if (roots > 1) {
                    return false;
                }
            }
        }

        return roots == 1;
    }

    public Iterable<String> nouns()
    {
        if (nounsList == null || nounsList.isEmpty()) throw new NullPointerException();
        Iterable<String> temp = nounsList;
        return temp;
    }

    public boolean isNoun(String word)
    {
		if (word == null) {
			throw new NullPointerException();
		}		

    	if (nounsMap.isEmpty()) throw new NullPointerException();
    	//if (!(nounsMap.containsKey(word))) throw new IllegalArgumentException();
    	
        return nounsMap.containsKey(word);
    }

    public int distance(String nounA, String nounB)
    {
		if ((nounA == null) || (nounB == null)) {
			throw new NullPointerException();
		}
		    	    	
        if (!(nounsList.contains(nounA))) throw new IllegalArgumentException();
        if (!(nounsList.contains(nounB))) throw new IllegalArgumentException();
        
        List<Integer> idA = nounsMap.get(nounA);
        List<Integer> idB = nounsMap.get(nounB);
        return sap.length(idA, idB);
    }

    public String sap(String nounA, String nounB)
    {
		if ((nounA == null) || (nounB == null)) {
			throw new NullPointerException();
		}

		if (!(nounsList.contains(nounA))) throw new IllegalArgumentException();
        if (!(nounsList.contains(nounB))) throw new IllegalArgumentException();
        
    	List<Integer> idA = nounsMap.get(nounA);
    	List<Integer> idB = nounsMap.get(nounB);
    	int ancestor = sap.ancestor(idA, idB);
        return synsetMap.get(ancestor);
    }

    private void testNouns(String nounA, String nounB)
    {
		if ((nounA == null) || (nounB == null)) {
			throw new NullPointerException();
		}
    	
    	System.out.print("'" + nounA + "' and '" + nounB + "': ");
        System.out.print("sap: '" + sap(nounA, nounB));
        System.out.println("', distance=" + distance(nounA, nounB));
    }

    // for unit testing of this class
    public static void main(String[] args)
    {
        String synsetsFile = "testInput/synsets.txt";
        String hypernymsFile = "testInput/hypernyms.txt";

        WordNet wordnet = new WordNet(synsetsFile, hypernymsFile);
        
        wordnet.testNouns("municipality", "urban_area");
        
        wordnet.testNouns("municipality", "region");
        wordnet.testNouns("individual", "edible_fruit");
        wordnet.testNouns("Black_Plague", "black_marlin");
        wordnet.testNouns("American_water_spaniel", "histology");
        wordnet.testNouns("Brown_Swiss", "barrel_roll");

        wordnet.testNouns("chocolate", "brownie");
        wordnet.testNouns("cookie", "brownie");
        wordnet.testNouns("martini", "beer");

        synsetsFile = "testInput/synsets100-subgraph.txt";
        hypernymsFile = "testInput/hypernyms100-subgraph.txt";
        wordnet = new WordNet(synsetsFile, hypernymsFile);
    
    }
}
