import java.util.Objects;

public class Complex {
    private final double re;
    private final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Complex(double re) {
        this(re, 0);
    }

    public double getRe() { return re; }
    public double getIm() { return im; }

    public Complex add(Complex other) {
        return new Complex(re + other.re, im + other.im);
    }

    public Complex subtract(Complex other) {
        return new Complex(re - other.re, im - other.im);
    }

    public Complex multiply(Complex other) {
        return new Complex(
            re * other.re - im * other.im,
            re * other.im + im * other.re
        );
    }

    public Complex divide(Complex other) {
        double denom = other.re * other.re + other.im * other.im;
        if (denom == 0) throw new ArithmeticException("Деление на ноль");
        return new Complex(
            (re * other.re + im * other.im) / denom,
            (im * other.re - re * other.im) / denom
        );
    }

    public double abs() {
        return Math.hypot(re, im);
    }

    public Complex conjugate() {
        return new Complex(re, -im);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complex complex = (Complex) o;
        return Double.compare(complex.re, re) == 0 && Double.compare(complex.im, im) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(re, im);
    }

    @Override
    public String toString() {
        if (im >= 0) {
            return String.format("%.2f+%.2fi", re, im);
        } else {
            return String.format("%.2f%.2fi", re, im);
        }
    }
}