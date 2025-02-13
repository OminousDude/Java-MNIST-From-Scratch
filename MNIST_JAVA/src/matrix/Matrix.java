package matrix;

import java.util.Arrays;
import java.util.Objects;

public class Matrix {
    private static final String NUMBER_FORMAT = "%+12.5f";
    private static final double TOLERANCE = 0.000001;
    private int rows;
    private int cols;
    private double[] a;

    public interface Producer {
        double produce(int index);
    }

    public interface ValueProducer {
        double produce(double value);
    }

    public interface IndexValueProducer {
        double produce(int index, double value);
    }

    public interface IndexValueConsumer {
        void consume(int index, double value);
    }

    public interface RowColProducer {
        double produce(int row, int col, double value);
    }

    public Matrix(int rows, int cols) {
        a = new double[rows * cols];
        this.rows = rows;
        this.cols = cols;
    }

    public Matrix(int rows, int cols, Producer producer) {
        this(rows, cols);

        for (int i = 0; i < a.length; i++) {
            a[i] = producer.produce(i);
        }
    }

    public Matrix apply(IndexValueProducer producer) {
        Matrix matrix = new Matrix(rows, cols);

        for (int i = 0; i < a.length; i++) {
            matrix.a[i] = producer.produce(i, a[i]);
        }

        return matrix;
    }

    public Matrix modify(RowColProducer producer) {
        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                a[index] = producer.produce(row, col, a[index]);

                index++;
            }
        }

        return this;
    }

    public Matrix modify(ValueProducer producer) {
        for (int i = 0; i < a.length; i++) {
            a[i] = producer.produce(a[i]);
        }
        return this;
    }

    public void forEach(IndexValueConsumer consumer) {
        for (int i = 0; i < a.length; i++) {
            consumer.consume(i, a[i]);
        }
    }

    public Matrix multiply(Matrix m) {
        Matrix result = new Matrix(rows, m.cols);

        assert cols == m.rows;

        for (int row = 0; row < result.rows; row++) {
            for (int n = 0; n < cols; n++) {
                for (int col = 0; col < result.cols; col++) {
                    result.a[row * result.cols + col] += a[row * cols + n] * m.a[col + n * m.cols];
                }
            }
        }

        return result;
    }

    public double get(int index) {
        return a[index];
    }

    public Matrix add(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new IllegalArgumentException("Matrix dimensions must match for addition.");
        }

        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < a.length; i++) {
            result.a[i] = this.a[i] + other.a[i];
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Matrix other = (Matrix) obj;

        for (int i = 0; i < a.length; i++) {
            if (Math.abs(a[i] - other.a[i]) > TOLERANCE) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows, cols, Arrays.hashCode(a));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        int index = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                sb.append(String.format(NUMBER_FORMAT, a[index]));
                index++;
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}