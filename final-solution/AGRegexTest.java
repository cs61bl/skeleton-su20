import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;
import static org.junit.Assert.*;
import jh61b.grader.GradedTest;
import jh61b.grader.AutograderRunner;

public class AGRegexTest {
    @Test(timeout = 5000)
    @GradedTest(name = "compilationBonus", number = "a000", max_score = 1)
    public void compilationBonus() {
        assertTrue(true);
    }

    @Test(timeout = 5000)
    @GradedTest(name = "basicTest", number = "a001", max_score = 1)
    public void basicTest() {
        Pattern pattern = Card.pattern();

        Matcher match = pattern.matcher("2 of spades");
        assertTrue(match.matches());
        match.reset();

        match = pattern.matcher("quEen of hearts");
        assertTrue(match.matches());
        match.reset();

        match = pattern.matcher("one of clubs");
        assertFalse(match.matches());
        match.reset();

        match = pattern.matcher("jack ofdiamonds");
        assertFalse(match.matches());
        match.reset();
    }

    @Test(timeout = 5000)
    @GradedTest(name = "weirdCaps", number = "a002", max_score = 1)
    public void weirdCaps() {
        Pattern pattern = Card.pattern();

        Matcher match = pattern.matcher("2 of spades");
        assertTrue(match.matches());
        match.reset();

        match = pattern.matcher("2 of sPades");
        assertTrue(match.matches());
        match.reset();

        match = pattern.matcher("2 of spadEs");
        assertTrue(match.matches());
        match.reset();

        match = pattern.matcher("2 of spADes");
        assertTrue(match.matches());
        match.reset();

        match = pattern.matcher("qUeeN of spades");
        assertTrue(match.matches());
        match.reset();

        match = pattern.matcher("2 OF spades");
        assertTrue(match.matches());
        match.reset();

        match = pattern.matcher("2 oF spades");
        assertTrue(match.matches());
        match.reset();

        match = pattern.matcher("KinG of spaDeS");
        assertTrue(match.matches());
        match.reset();
    }

    @Test(timeout = 5000)
    @GradedTest(name = "testBad", number = "a003", max_score = 1)
    public void testBad() {
        Pattern pattern = Card.pattern();

        Matcher match = pattern.matcher("2");
        assertFalse(match.matches());
        match.reset();

        match = pattern.matcher("two of sPades");
        assertFalse(match.matches());
        match.reset();

        match = pattern.matcher("queen");
        assertFalse(match.matches());
        match.reset();

        match = pattern.matcher("queenof hearts");
        assertFalse(match.matches());
        match.reset();

        match = pattern.matcher("2ofhearts");
        assertFalse(match.matches());
        match.reset();

        match = pattern.matcher("10ofhearts");
        assertFalse(match.matches());
        match.reset();

        match = pattern.matcher("10 of diamondois");
        assertFalse(match.matches());
        match.reset();

        match = pattern.matcher("10 of fearts");
        assertFalse(match.matches());
        match.reset();
    }

    public static void main(String[] args) {
        AutograderRunner.run(args);
    }

}