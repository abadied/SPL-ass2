package bgu.spl.a2.sim;

public class GsonReader {
	int nthreads;
	ToolInfo[] tools;
	Plan[] plans;
	Zerg[][] waves;
	
	class ToolInfo {
		String tool;
		int qty;
	}
	
	class Plan {
		String product;
		String[] tools;
		String[] parts;
	}
	
	class Zerg {
		String product;
		int qty;
		int startId;
	}
}
