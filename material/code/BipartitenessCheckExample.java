/**
 * The bipartiteness check example tests whether an input graph is bipartite
 * or not. A bipartite graph's vertices can be separated into two disjoint
 * groups, such as no two nodes inside the same group is connected by an edge.
 * The example uses the merge-tree abstraction of our graph streaming API.
 */
public class BipartitenessCheckExample implements ProgramDescription {

        // [...]

	private static boolean parseParameters(String[] args) {

		if (args.length > 0) {
			if (args.length != 2) {
				// [...]
				return false;
			}

			fileOutput = true;
			edgeInputPath = args[0];
			outputPath = args[1];
		} else {
                    // [...]
                }
		return true;
	}

}
