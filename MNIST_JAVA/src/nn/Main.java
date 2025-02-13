package nn;

public class Main {
    static double neuron(double[] inputs, double[] weights, double bias) {
        double sum =  0.0;
        for (int i = 0; i < inputs.length; i++) {
            sum += inputs[i] * weights[i];
        }
        sum += bias;
        return sum > 0 ? 1.0 : 0.0; // Activation function
    }

    static double and(double x, double y) {
        return neuron(new double[] {x, y}, new double[] {1, 1}, -1);
    }

    static double nand(double x, double y) {
        return neuron(new double[] {x, y}, new double[] {-1, -1}, 2);
    }

    static double or(double x, double y) {
        return neuron(new double[] {x, y}, new double[] {1, 1}, 0);
    }

    static double nor(double x, double y) {
        return neuron(new double[] {x, y}, new double[] {-1, -1}, 1);
    }

    static double xor(double x, double y) {
        return and(or(x, y), nand(x, y));
    }

    static double xnor(double x, double y) {
        return or(and(x, y), nor(x, y));
    }

    public static void main(String[] args) {
        for (int x = 0; x <= 1; x++) {
            for (int y = 0; y <= 1; y++) {
                double out = xor(x, y);
                System.out.printf("%d %d -> %d\n", x, y, (int)out);
            }
        }
    }
}