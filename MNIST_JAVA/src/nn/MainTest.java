package nn;

import matrix.Matrix;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private Random random = new Random();

    @Test
    void testNeuron() {
        double result = Main.neuron(new double[]{1, 1}, new double[]{1, 1}, -1);
        assertEquals(1.0, result, "Neuron should return 1 for AND(1,1)");
    }

    @Test
    void testAnd() {
        assertEquals(1.0, Main.and(1, 1));
        assertEquals(0.0, Main.and(1, 0));
        assertEquals(0.0, Main.and(0, 1));
        assertEquals(0.0, Main.and(0, 0));
    }

    @Test
    void testNand() {
        assertEquals(0.0, Main.nand(1, 1));
        assertEquals(1.0, Main.nand(1, 0));
        assertEquals(1.0, Main.nand(0, 1));
        assertEquals(1.0, Main.nand(0, 0));
    }

    @Test
    void testOr() {
        assertEquals(1.0, Main.or(1, 1));
        assertEquals(1.0, Main.or(1, 0));
        assertEquals(1.0, Main.or(0, 1));
        assertEquals(0.0, Main.or(0, 0));
    }

    @Test
    void testNor() {
        assertEquals(0.0, Main.nor(1, 1));
        assertEquals(0.0, Main.nor(1, 0));
        assertEquals(0.0, Main.nor(0, 1));
        assertEquals(1.0, Main.nor(0, 0));
    }

    @Test
    void testXor() {
        assertEquals(0.0, Main.xor(1, 1));
        assertEquals(1.0, Main.xor(1, 0));
        assertEquals(1.0, Main.xor(0, 1));
        assertEquals(0.0, Main.xor(0, 0));
    }

    @Test
    void testXnor() {
        assertEquals(1.0, Main.xnor(1, 1));
        assertEquals(0.0, Main.xnor(1, 0));
        assertEquals(0.0, Main.xnor(0, 1));
        assertEquals(1.0, Main.xnor(0, 0));
    }

    @Test
    void testAddBias(){
        Matrix input = new Matrix(3 , 3, i -> i + 1);
        Matrix weights = new Matrix(3, 3, i -> i + 1);
        Matrix biases = new Matrix(1, 3, i -> i + 1);

        Matrix output = weights.multiply(input).modify((row, col, value) -> value + biases.get(row));

        double[] expected = {+31.0, +37.0, +43.0, +68.0, +83.0, +98.0, +105.0, +129.0, +153.0};

        Matrix expectedMatrix = new Matrix(3, 3, i -> expected[i]);

        assertEquals(expectedMatrix, output);
    }

    @Test
    void testReLu(){
        final int numberNeurons = 5;
        final int numberInputs = 6;
        final int inputSize = 4;

        Matrix input = new Matrix(inputSize, numberInputs, i -> random.nextDouble());
        Matrix weights = new Matrix(numberNeurons, inputSize, i -> random.nextGaussian());
        Matrix biases = new Matrix(numberNeurons, 1, i -> random.nextGaussian());

        Matrix output1 = weights.multiply(input).modify((row, col, value) -> value + biases.get(row));
        Matrix output2 = weights.multiply(input).modify((row, col, value) -> value + biases.get(row)).modify(value -> value > 0 ? value : 0);

        output2.forEach((index, value) -> {
            double originalValue = output1.get(index);

            if (originalValue > 0) {
                assertEquals(originalValue, value, 0.000001);
            } else {
                assertEquals(0.0, value, 0.000001);
            }
        });
    }
}