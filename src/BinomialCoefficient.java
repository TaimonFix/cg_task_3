public class BinomialCoefficient {

    private double coefficient;

    public BinomialCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public void binomialSolution(int k, int i) {
        if ((k == 0) || (i == 0)) {
            coefficient = 1;
        }

        else coefficient = coefficient * (k - i + 1) / i;
    }

    public double getCoefficient() {
        return coefficient;
    }
}
