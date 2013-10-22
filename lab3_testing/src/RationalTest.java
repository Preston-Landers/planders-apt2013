import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class RationalTest extends TestCase {

    protected Rational HALF;

    protected void setUp() {
      HALF = new Rational( 1, 2 );
    }

    // Create new test
    public RationalTest (String name) {
        super(name);
    }

    public void testEquality() {
        assertEquals(new Rational(1,3), new Rational(1,3));
        assertEquals(new Rational(1,3), new Rational(2,6));
        assertEquals(new Rational(3,3), new Rational(1,1));
    }

    // Test for nonequality
    public void testNonEquality() {
        assertFalse(new Rational(2,3).equals(
            new Rational(1,3)));
    }

    public void testAccessors() {
    	assertEquals(new Rational(2,3).numerator(), 2);
    	assertEquals(new Rational(2,3).denominator(), 3);
    }

    public void testRoot() {
        Rational s = new Rational( 1, 4 );
        Rational sRoot = null;
        try {
            sRoot = s.root();
        } catch (IllegalArgumentToSquareRootException e) {
            e.printStackTrace();
        }
        assertTrue( sRoot.isLessThan( HALF.plus( Rational.getTolerance() ) ) 
                        && HALF.minus( Rational.getTolerance() ).isLessThan( sRoot ) );
    }

    public void testSignPlacementChanges() {
        assertEquals(
                new Rational(-34, 283),
                new Rational(34, -283)
        );
    }

    public void testPlus() {
        Rational _7_22 = new Rational(7,22);
        Rational _9_37 = new Rational(9,37);
        assertEquals(_7_22.plus(_9_37), new Rational(457,814));

        Rational __93_122 = new Rational(-93,122);
        Rational _5_27 = new Rational(5,27);
        assertEquals(__93_122.plus(_5_27), new Rational(-1901,3294));
    }

    public void testPlusSignPlacementChanges() {
        Rational __93_122 = new Rational(-93,122);
        Rational _93__122 = new Rational(93,-122);
        Rational _5_27 = new Rational(5,27);
        assertEquals(
                __93_122.plus(_5_27),
                _93__122.plus(_5_27)
        );
    }

    public void testMinus() {
        Rational _7_22 = new Rational(7,22);
        Rational _9_37 = new Rational(9,37);
        assertEquals(_7_22.minus(_9_37), new Rational(61,814));

        Rational __93_122 = new Rational(-93,122);
        Rational _5_27 = new Rational(5,27);
        assertEquals(__93_122.minus(_5_27), new Rational(-3121,3294));
    }

    public void testTimes() {
        Rational _7_22 = new Rational(7,22);
        Rational _9_37 = new Rational(9,37);
        assertEquals(_7_22.times(_9_37), new Rational(63,814));

        Rational __93_122 = new Rational(-93,122);
        Rational _5_27 = new Rational(5,27);
        assertEquals(__93_122.times(_5_27), new Rational(-155,1098));
    }

    public void testDivides() {
        Rational _7_22 = new Rational(7,22);
        Rational _9_37 = new Rational(9,37);
        assertEquals(_7_22.divides(_9_37), new Rational(259,198));

        Rational __93_122 = new Rational(-93,122);
        Rational _5_27 = new Rational(5,27);
        assertEquals(__93_122.divides(_5_27), new Rational(-2511,610));

    }

    public void testTolerance() {
        Rational.setTolerance(new Rational(1, 1000000));
        Rational s = new Rational( 1, 4 );
        Rational sRoot = null;
        try {
            sRoot = s.root();
        } catch (IllegalArgumentToSquareRootException e) {
            e.printStackTrace();
        }
        assertTrue( sRoot.isLessThan( HALF.plus( Rational.getTolerance() ) )
                && HALF.minus( Rational.getTolerance() ).isLessThan( sRoot ) );

    }

    public void testIllegalArgumentToSquareRootException() {
        // Test too high
        Rational s = new Rational( 46341, 1 );
        Rational sRoot = null;
        try {
            sRoot = s.root();
            fail("Did not receive expected IllegalArgumentToSquareRootException");
        } catch (IllegalArgumentToSquareRootException e) {
            // exception is expected here
        }

        // Test too low
        s = new Rational( 0, 1 );
        sRoot = null;
        try {
            sRoot = s.root();
            fail("Did not receive expected IllegalArgumentToSquareRootException");
        } catch (IllegalArgumentToSquareRootException e) {
            // exception is expected here
        }

    }

    public void testAbsSameSign() {
        assertEquals(
                new Rational( 27, 9845 ).abs(),
                new Rational( 27, 9845 ));
        assertEquals(
                new Rational( 1, 3).abs(),
                new Rational( 1, 3));
    }

    public void testAbsMixedSign() {
        assertEquals(
                new Rational( -27, 9845 ).abs(),
                new Rational( 27, 9845 ));
        assertEquals(
                new Rational( 1, -3).abs(),
                new Rational( 1, 3));
    }

    public void testToString() {
        assertEquals(
                new Rational( 1, -3).toString(),
                "1/-3");
        assertEquals(
                new Rational( -38, 297).toString(),
                "-38/297");
    }

    public void testModuleMain() {
        PrintStream savedOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Rational.main(null);
        assertEquals(
                outContent.toString(),
                "Root of half is ??");
                // can't be represented as a rational number?

        System.setOut(savedOut);
    }

    public static void main(String args[]) {
        String[] testCaseName = 
            { RationalTest.class.getName() };
        // junit.swingui.TestRunner.main(testCaseName);
        junit.textui.TestRunner.main(testCaseName);
    }
}
