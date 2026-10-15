import java.util.Arrays;
import java.util.Objects;

public class ComplexMatrix {
    private final Complex[][] data;
    private final int rows;
    private final int cols;

    public ComplexMatrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) throw new IllegalArgumentException("Размеры должны быть положительными");
        this.rows = rows;
        this.cols = cols;
        this.data = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = new Complex(0, 0);
            }
        }
    }

    public ComplexMatrix(Complex[][] data) {
        if (data == null || data.length == 0 || data[0].length == 0)
            throw new IllegalArgumentException("Некорректные данные");
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            if (data[i].length != cols) throw new IllegalArgumentException("Разная длина строк");
            for (int j = 0; j < cols; j++) {
                this.data[i][j] = data[i][j];
            }
        }
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    public Complex get(int i, int j) {
        if (i < 0 || i >= rows || j < 0 || j >= cols) throw new IndexOutOfBoundsException();
        return data[i][j];
    }

    public void set(int i, int j, Complex value) {
        if (i < 0 || i >= rows || j < 0 || j >= cols) throw new IndexOutOfBoundsException();
        data[i][j] = value;
    }

    public ComplexMatrix add(ComplexMatrix other) {
        if (rows != other.rows || cols != other.cols)
            throw new IllegalArgumentException("Размеры матриц не совпадают");
        Complex[][] result = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = data[i][j].add(other.data[i][j]);
            }
        }
        return new ComplexMatrix(result);
    }

    public ComplexMatrix multiply(ComplexMatrix other) {
        if (cols != other.rows)
            throw new IllegalArgumentException("Число столбцов первой матрицы не равно числу строк второй");
        Complex[][] result = new Complex[rows][other.cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                Complex sum = new Complex(0, 0);
                for (int k = 0; k < cols; k++) {
                    sum = sum.add(data[i][k].multiply(other.data[k][j]));
                }
                result[i][j] = sum;
            }
        }
        return new ComplexMatrix(result);
    }

    public ComplexMatrix transpose() {
        Complex[][] result = new Complex[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j][i] = data[i][j];
            }
        }
        return new ComplexMatrix(result);
    }

    public Complex determinant() {
        if (rows != cols)
            throw new IllegalArgumentException("Определитель существует только для квадратной матрицы");
        int n = rows;
        Complex[][] a = copy(data);
        Complex det = new Complex(1, 0);
        int sign = 1;
        for (int i = 0; i < n; i++) {
            int pivot = i;
            for (int j = i + 1; j < n; j++) {
                if (a[j][i].abs() > a[pivot][i].abs()) {
                    pivot = j;
                }
            }
            if (a[pivot][i].abs() < 1e-12) {
                return new Complex(0, 0);
            }
            if (pivot != i) {
                Complex[] temp = a[i];
                a[i] = a[pivot];
                a[pivot] = temp;
                sign = -sign;
            }
            det = det.multiply(a[i][i]);
            for (int j = i + 1; j < n; j++) {
                Complex factor = a[j][i].divide(a[i][i]);
                for (int k = i; k < n; k++) {
                    a[j][k] = a[j][k].subtract(factor.multiply(a[i][k]));
                }
            }
        }
        return det.multiply(new Complex(sign, 0));
    }

    public ComplexMatrix inverse() {
        if (rows != cols)
            throw new IllegalArgumentException("Обратная матрица существует только для квадратной матрицы");
        int n = rows;
        Complex[][] a = copy(data);
        Complex[][] inv = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inv[i][j] = (i == j) ? new Complex(1, 0) : new Complex(0, 0);
            }
        }
        for (int i = 0; i < n; i++) {
            int pivot = i;
            for (int j = i + 1; j < n; j++) {
                if (a[j][i].abs() > a[pivot][i].abs()) {
                    pivot = j;
                }
            }
            if (a[pivot][i].abs() < 1e-12) {
                throw new ArithmeticException("Матрица вырождена, обратной не существует");
            }
            if (pivot != i) {
                Complex[] temp = a[i];
                a[i] = a[pivot];
                a[pivot] = temp;
                temp = inv[i];
                inv[i] = inv[pivot];
                inv[pivot] = temp;
            }
            Complex pivotVal = a[i][i];
            for (int j = 0; j < n; j++) {
                a[i][j] = a[i][j].divide(pivotVal);
                inv[i][j] = inv[i][j].divide(pivotVal);
            }
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    Complex factor = a[j][i];
                    for (int k = 0; k < n; k++) {
                        a[j][k] = a[j][k].subtract(factor.multiply(a[i][k]));
                        inv[j][k] = inv[j][k].subtract(factor.multiply(inv[i][k]));
                    }
                }
            }
        }
        return new ComplexMatrix(inv);
    }

    public ComplexMatrix divide(ComplexMatrix other) {
        if (cols != other.rows || other.rows != other.cols)
            throw new IllegalArgumentException("Некорректные размеры для деления");
        return this.multiply(other.inverse());
    }

    private Complex[][] copy(Complex[][] source) {
        Complex[][] dest = new Complex[source.length][source[0].length];
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source[0].length; j++) {
                dest[i][j] = source[i][j];
            }
        }
        return dest;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("[ ");
            for (int j = 0; j < cols; j++) {
                sb.append(data[i][j]);
                if (j < cols - 1) sb.append(", ");
            }
            sb.append(" ]\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexMatrix that = (ComplexMatrix) o;
        return rows == that.rows && cols == that.cols && Arrays.deepEquals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rows, cols);
        result = 31 * result + Arrays.deepHashCode(data);
        return result;
    }
}