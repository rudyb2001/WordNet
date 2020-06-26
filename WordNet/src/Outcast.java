import java.util.Arrays;
import java.util.Scanner;

public class Outcast
{
    private WordNet net;
	
	public Outcast(WordNet wordnet)
    {
        net = wordnet;
    	
    	//throw new UnsupportedOperationException();
    }

    public String outcast(String[] nouns)
    {
        int[][] distances = new int[nouns.length][nouns.length];
    	int distance = 0;
        int dist = 0;
        String out = "";
    	
    	for (int i = 0; i < nouns.length; i++) {
        	dist = 0;
        	
        	for (int k = 0; k < i; k++) {
        		dist += distances[i][k];
        	}
        	
    		for (int j = i; j < nouns.length; j++) {
        		distances[i][j] = net.distance(nouns[i], nouns[j]);
        		distances[j][i] = distances[i][j];
    			dist += distances[i][j];
        	}
    		
    		if (dist > distance) {
    			distance = dist;
    			out = nouns[i];
    		}
        }
    	
    	return out;
    	
    	//throw new UnsupportedOperationException();
    }

    // for unit testing of this class
    public static void main(String[] args)
    {
        // Set this to an array of outcast files to feed them all through
        // your Outcast object, OR set it to null so you can enter nouns
        // directly in the Console window
        String[] outcastFiles = { "testInput/outcast3.txt", "testInput/outcast4.txt" };
        // String[] outcastFiles = null;

        String synsetsFile = "testInput/synsets.txt";
        String hypernymsFile = "testInput/hypernyms.txt";

        WordNet wordnet = new WordNet(synsetsFile, hypernymsFile);
        Outcast outcast = new Outcast(wordnet);

        // For testing outcasts, either read the noun list from files whose
        // filenames you put in Run Configurations OR read the noun list directly
        // from the interactive console

        if (outcastFiles == null)
        {
            // Get the outcast test list interactively from the user
            Scanner console = new Scanner(System.in);
            while (true)
            {
                System.out.print("Enter a space-separated list of nouns: ");
                String[] nouns = console.nextLine().split(" ");
                StdOut.println("Outcast is: " + outcast.outcast(nouns));
            }
        }
        else
        {
            // Get the outcast test list from array
            for (int t = 0; t < outcastFiles.length; t++)
            {
                // NOTE: Although Eclipse crosses out readStrings, it's ok to use.
                String[] nouns = In.readStrings(outcastFiles[t]);
                StdOut.println(outcastFiles[t] + ": " + Arrays.toString(nouns) + " --> "
                        + outcast.outcast(nouns));
            }
        }
    }
}
