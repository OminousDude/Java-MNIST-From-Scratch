package matrix;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {
    @Test
    void testMultiply() {
        Matrix matrix = new Matrix(2, 3, i -> i);
        Matrix other = new Matrix(3, 2, i -> i);

        double[] expectedValues = {10, 13, 28, 40};
        Matrix expected = new Matrix(2, 2, i -> expectedValues[i]);

        Matrix result = matrix.multiply(other);

        assertEquals(expected, result);
    }

    @Test
    void testMultiplySpeed() {
        int rows = 500;
        int cols = 500;
        int mid = 50;

        Matrix matrix = new Matrix(rows, mid, i -> i);
        Matrix other = new Matrix(mid, cols, i -> i);

        var start = System.currentTimeMillis();
        matrix.multiply(other);
        var end = System.currentTimeMillis();

        System.out.printf("Matrix multiplication took %d ms.\n", end - start);
    }

    @Test
    void testMatrixInitialization() {
        Matrix matrix = new Matrix(3, 4);
        assertNotNull(matrix, "Matrix should be initialized properly.");
    }

    @Test
    void testMatrixWithProducer() {
        Matrix.Producer producer = index -> index * 2.0;
        Matrix matrix = new Matrix(3, 4, producer);

        // Expected values: [0.0, 2.0, 4.0, 6.0]
        assertEquals(0.0, producer.produce(0));
        assertEquals(2.0, producer.produce(1));
        producer = index -> index * 2.0 + 8;
        assertEquals(12, producer.produce(2));
        assertEquals(14, producer.produce(3));
    }

    @Test
    void testMatrixString() {
        Matrix matrix = new Matrix(3, 4, i->i*2);

        String text = matrix.toString();

        double[] expected = new double[12];

        for (int i = 0; i < expected.length; i++) {
            expected[i] = i * 2;
        }

        var rows = text.split("\n");

        assertEquals(3, rows.length);

        int index = 0;

        for (var row : rows) {
            var values = row.split("\\s+");

            for (var textValue : values) {
                if (Objects.equals(textValue, "")) {
                    continue;
                }
                double doubleValue = Double.parseDouble(textValue);

                assertTrue(Math.abs(doubleValue - expected[index]) < 0.0001);

                index++;
            }
        }
    }

    @Test
    void testMultiplyMatrix() {
        double x = 0.5;

        Matrix.Producer producer = i -> .5 * (i - 6);
        Matrix matrix = new Matrix(3, 4, producer);

        producer = i -> x * .5 * (i - 6);
        Matrix expected = new Matrix(3, 4, producer);

        Matrix results = matrix.apply((index, value)->x * value);

        assertEquals(results, expected);
        assertTrue(Math.abs(results.get(0) + 1.5) < 0.0001);
    }

    @Test
    void testMatrixAddition() {
        // Create first matrix with elements i
        Matrix matrix1 = new Matrix(3, 4, i -> (double) i);

        // Create second matrix with elements i * 2
        Matrix matrix2 = new Matrix(3, 4, i -> i * 2.0);

        // The expected result is a matrix where each element equals i * 3
        Matrix expected = new Matrix(3, 4, i -> i * 3.0);

        Matrix result = matrix1.add(matrix2);

        assertEquals(expected, result, "Matrix addition did not produce the expected result.");
    }

    @Test
    void testEquals() {
        Matrix matrix1 = new Matrix(3, 4, i -> .5 * (i - 6));

        Matrix matrix2 = new Matrix(3, 4, i -> .5 * (i - 6));

        Matrix matrix3 = new Matrix(3, 4, i -> .5 * (i - 6.2));

        assertEquals(matrix1, matrix2);
        assertNotEquals(matrix1, matrix3);
    }
}