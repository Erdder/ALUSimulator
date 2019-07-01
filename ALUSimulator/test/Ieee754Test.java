import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class Ieee754Test {

	//@Rule
	/*public TestLogger tl = new TestLogger();

	@Rule
    public Timeout globalTimeout = Timeout.seconds(10);*/

	@Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
        		{"-Inf", 64, "1111111111110000000000000000000000000000000000000000000000000000"},
        		{"0", 32, "00000000000000000000000000000000"},
        		{"11.375", 32, "01000001001101100000000000000000"}
        });
    }

    private ALU alu = new ALU();
    private String number;
    private int length;
    private String expected;

    public Ieee754Test(String number, int length, String expected) {
    	this.number = number;
    	this.length = length;
    	this.expected = expected;
    }

	@Test
	public void test() {
		assertEquals(expected, alu.ieee754(number,length));
	}

}
