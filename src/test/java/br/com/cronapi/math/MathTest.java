package br.com.cronapi.math;

import cronapi.Var;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static cronapi.math.Operations.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathTest {

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testPow() throws Exception {
        // simple
        assertEquals(pow(Var.valueOf(7) ,Var.valueOf(2)).getObjectAsDouble(), Var.valueOf(49).getObjectAsDouble());
        assertEquals(pow(Var.valueOf(7) ,Var.valueOf(3)).getObjectAsDouble(), Var.valueOf(343).getObjectAsDouble());
        assertEquals(pow(Var.valueOf(2) ,Var.valueOf(10)).getObjectAsDouble(), Var.valueOf(1024).getObjectAsDouble());
        // fractional exponents
        assertEquals(pow(Var.valueOf(4) ,Var.valueOf(0.5)).getObjectAsDouble(), Var.valueOf(2).getObjectAsDouble());
        assertEquals(pow(Var.valueOf(8) ,Var.valueOf(0.333333333)).getObjectAsDouble(), Var.valueOf(1.9999999986137056).getObjectAsDouble());
        // signed exponents
        assertEquals(pow(Var.valueOf(7) ,Var.valueOf(-2)).getObjectAsDouble(), Var.valueOf(0.02040816326530612).getObjectAsDouble());
        // signed bases
        assertEquals(pow(Var.valueOf(-7) ,Var.valueOf(2)).getObjectAsDouble(), Var.valueOf(49).getObjectAsDouble());
        assertEquals(pow(Var.valueOf(-7) ,Var.valueOf(3)).getObjectAsDouble(), Var.valueOf(-343).getObjectAsDouble());
        assertEquals(pow(Var.valueOf(-7) ,Var.valueOf(0.5)).getObjectAsDouble(), Var.valueOf(Double.NaN).getObjectAsDouble());
    }

    @Test
    public void testMultiply() throws Exception {
        assertEquals(multiply(Var.valueOf(Double.valueOf("5.1")), Var.valueOf(Double.valueOf("5.1"))).getObjectAsDouble(), Var.valueOf("26.009999999999998").getObjectAsDouble());
        assertEquals(multiply(Var.valueOf(Long.valueOf("5")), Var.valueOf(Long.valueOf("5"))).getObjectAsLong(), Var.valueOf("25").getObjectAsLong());
    }

    @Test
    public void testSubtract() throws Exception {
        assertEquals(subtract(Var.valueOf(Double.valueOf("5.1")), Var.valueOf(Double.valueOf("1.1"))).getObjectAsDouble(), Var.valueOf("3.9999999999999996").getObjectAsDouble());
        assertEquals(subtract(Var.valueOf(Long.valueOf("5")), Var.valueOf(Long.valueOf("2"))).getObjectAsLong(), Var.valueOf("3").getObjectAsLong());

    }

    @Test
    public void testSum() throws Exception {
        assertEquals(sum(Var.valueOf(Double.valueOf("5.1")), Var.valueOf(Double.valueOf("5.1"))).getObjectAsDouble(), Var.valueOf("10.2").getObjectAsDouble());
        assertEquals(sum(Var.valueOf(Long.valueOf("5")), Var.valueOf(Long.valueOf("5"))).getObjectAsLong(), Var.valueOf("10").getObjectAsLong());

    }

    @Test
    public void testListSum() {
    }

    @Test
    public void testAddLong() {
    }

    @Test
    public void testAddDouble() {
    }

    @Test
    public void testSubtractLong() {
    }

    @Test
    public void testSubtractDouble() {
    }

    @Test
    public void testMultiplyLong() {
    }

    @Test
    public void testMultiplyDouble() {
    }

    @Test
    public void testDivisor() {
    }

    @Test
    public void testAbs() {
    }

    @Test
    public void testSqrt() {
    }

    @Test
    public void testLog() {
    }

    @Test
    public void testLog10() {
    }

    @Test
    public void testExp() {
    }

    @Test
    public void testPow10() {
    }

    @Test
    public void testRound() {
    }

    @Test
    public void testCeil() {
    }

    @Test
    public void testFloor() {
    }

    @Test
    public void testSin() {
    }

    @Test
    public void testCos() {
    }

    @Test
    public void testTan() {
    }

    @Test
    public void testAsin() {
    }

    @Test
    public void testAcos() {
    }

    @Test
    public void testAtan() {
    }

    @Test
    public void testPi() {
    }

    @Test
    public void testNeg() {
    }

    @Test
    public void testInfinity() {
    }

    @Test
    public void testE() {
    }

    @Test
    public void testGoldenRatio() {
    }

    @Test
    public void testIsEven() {
    }

    @Test
    public void testIsOdd() {
    }

    @Test
    public void testIsPrime() {
    }

    @Test
    public void testIsInt() {
    }

    @Test
    public void testIsPositive() {
    }

    @Test
    public void testIsNegative() {
    }

    @Test
    public void testIsDivisibleBy() {
    }

    @Test
    public void testRandomInt() {
    }

    @Test
    public void testRandomFloat() {
    }

    @Test
    public void testListSmaller() {
    }

    @Test
    public void testListLarger() {
    }

    @Test
    public void testListAverage() {
    }

    @Test
    public void testListMedium() {
    }

    @Test
    public void testListModes() {
    }

    @Test
    public void testListRandomItem() {
    }

    @Test
    public void testListStandardDeviation() {
    }

    @Test
    public void testMod() {
    }

    @Test
    public void testMin() {
    }

    @Test
    public void testMax() {
    }

    @Test
    public void testNegate() {
    }
}