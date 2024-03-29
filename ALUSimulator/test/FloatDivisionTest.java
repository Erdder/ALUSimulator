import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class FloatDivisionTest {

	//@Rule
	/*public TestLogger tl = new TestLogger();

	@Rule
    public Timeout globalTimeout = Timeout.seconds(10);*/

	@Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
        		{"001100000", "000000000", 4, 4, "0011110000"},
        		{"100000000", "001100000", 4, 4, "0000000000"},
        		{"001100000", "001110000", 4, 4, "0001100000"},
        		{"001110000", "001100000", 4, 4, "0010000000"},
        		{"00111110111000000", "00111111000000000", 8, 8, "000111111011000000"},
        });
    }

    private ALU alu = new ALU();
    private String operand1;
    private String operand2;
    private int eLength;
    private int sLength;
    private String expected;

    public FloatDivisionTest(String operand1, String operand2, int eLength, int sLength, String expected) {
    	this.operand1 = operand1;
    	this.operand2 = operand2;
    	this.eLength = eLength;
    	this.sLength = sLength;
    	this.expected = expected;
    }

	@Test
	public void test() {
		assertEquals(expected, alu.floatDivision(operand1, operand2, eLength, sLength));
	}

}
