import org.junit.Test;
import static org.junit.Assert.*;
import jh61b.grader.GradedTest;
import jh61b.grader.AutograderRunner;
import java.util.*;

public class AGTestOuterBanks {

	@Test(timeout = 5000)
    @GradedTest(name = "compilationBonus", number = "o00", max_score = 1)
    public void compilationBonus() {
        assertTrue(true);
    }

    @Test(timeout = 5000)
    @GradedTest(name = "basicTest", number = "o01", max_score = 1)
    public void basicTest() {
        Graph g = new Graph(10);
        for(int i = 1; i < 10; i++) {
        	g.addUndirectedEdge(0,i);
        }
        assertEquals(OuterBanks.getMinLocations(g), 1);
    }

    @Test(timeout = 5000)
    @GradedTest(name = "unconnected", number = "o02", max_score = 1)
    public void unconnected() {
        Graph g = new Graph(10);
        assertEquals(OuterBanks.getMinLocations(g), 10);
    }

    @Test(timeout = 5000)
    @GradedTest(name = "twoGroups", number = "o03", max_score = 1)
    public void twoGroups() {
        Graph g = new Graph(10);
        g.addUndirectedEdge(0,1);
        g.addUndirectedEdge(1,2);
        g.addUndirectedEdge(2,3);
        g.addUndirectedEdge(3,4);
        g.addUndirectedEdge(5,6);
        g.addUndirectedEdge(6,7);
        g.addUndirectedEdge(7,8);
        g.addUndirectedEdge(8,9);
        assertEquals(OuterBanks.getMinLocations(g), 2);
    }

     @Test(timeout = 5000)
    @GradedTest(name = "mediumTest", number = "o04", max_score = 1)
    public void mediumTest() {
        Graph g = new Graph(20);
        g.addUndirectedEdge(0,1);
        g.addUndirectedEdge(1,2);
        g.addUndirectedEdge(2,3);
        g.addUndirectedEdge(3,4);
        g.addUndirectedEdge(5,6);
        g.addUndirectedEdge(6,7);
        g.addUndirectedEdge(7,8);
        g.addUndirectedEdge(8,9);
        assertEquals(OuterBanks.getMinLocations(g), 12);
    }

    private static final int NUM_TRIALS = 100;
    private static final int NUM_VERT = 40;

    private Graph makeRandGraph(Random r) {
    	Graph g = new Graph(NUM_VERT);
    	int rand = r.nextInt();
    	for(int i = 0; i < NUM_VERT; i++) {
    		for(int j = 0; j < NUM_VERT; j++) {
    			if(i == j) {
    				continue;
    			}
    			if(rand%5==0) {
    				g.addUndirectedEdge(i,j);
    			}
    			if(rand%2==0) {
    				rand = r.nextInt();
    			}
    		}
    	}
    	return g;
    }

    @Test(timeout = 5000)
	@GradedTest(name = "fuzzTest", number = "o05", max_score = 6)
	public void fuzzTest1() {
		Random r = new Random(0x61bL);
		LinkedList<Double> studToStaffRatios = new LinkedList<>();
		for (int trial = 0; trial < NUM_TRIALS; trial += 1) {

			Graph g;

			double studentTime = 0.0;
			double staffTime = 0.0;
			double start;


			
			g = makeRandGraph(r);

			start = System.nanoTime();
			int student = OuterBanks.getMinLocations(g);
			studentTime += start - System.nanoTime();

			start = System.nanoTime();
			int soln = OuterBanksStaff.getMinLocations(g);
			staffTime += start - System.nanoTime();

			assertEquals("Student get does not match staff least", soln, student);
			assertEquals("Student get does not match staff greatest", soln, student);
			
			System.out.println("Successful .get test. Student to Staff time ratio: " + studentTime / staffTime);
			studToStaffRatios.addLast(studentTime / staffTime);
		}
		double minRatio = studToStaffRatios.stream().min(Double::compareTo).get();

		System.out.println("\nMinimum student to staff ratio: " + minRatio + "\n");

		assertTrue("Minimum student to staff ratio was greater than 5x", minRatio <= 5);
	}

    public static void main(String[] args) {
        AutograderRunner.run(args);
    }

}