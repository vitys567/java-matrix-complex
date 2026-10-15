import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<String, ComplexMatrix> matrices = new HashMap<>();

    public static void main(String[] args) {
        scanner.useLocale(Locale.US);
        while (true) {
            printMenu();
            int choice = readInt("Выберите пункт: ");
            switch (choice) {
                case 1 -> createMatrix();
                case 2 -> addMatrices();
                case 3 -> multiplyMatrices();
                case 4 -> divideMatrices();
                case 5 -> transposeMatrix();
                case 6 -> determinantMatrix();
                case 7 -> showAllMatrices();
                case 0 -> {
                    System.out.println("Выход.");
                    return;
                }
                default -> System.out.println("Неверный пункт.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Меню ===");
        System.out.println("1. Создать матрицу");
        System.out.println("2. Сложить матрицы");
        System.out.println("3. Умножить матрицы");
        System.out.println("4. Разделить матрицы");
        System.out.println("5. Транспонировать матрицу");
        System.out.println("6. Вычислить определитель");
        System.out.println("7. Показать все матрицы");
        System.out.println("0. Выход");
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Введите число.");
            }
        }
    }

    private static Complex readComplex(String prompt) {
        System.out.println(prompt);
        double re = readDouble("  Действительная часть: ");
        double im = readDouble("  Мнимая часть: ");
        return new Complex(re, im);
    }

    private static void createMatrix() {
        System.out.print("Введите имя матрицы (например, A): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Имя не может быть пустым.");
            return;
        }
        if (matrices.containsKey(name)) {
            System.out.println("Матрица с таким именем уже существует.");
            return;
        }
        int rows = readInt("Введите количество строк: ");
        int cols = readInt("Введите количество столбцов: ");
        if (rows <= 0 || cols <= 0) {
            System.out.println("Размеры должны быть положительными.");
            return;
        }
        Complex[][] data = new Complex[rows][cols];
        System.out.println("Введите элементы матрицы:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("Элемент [%d][%d]:%n", i, j);
                data[i][j] = readComplex("");
            }
        }
        matrices.put(name, new ComplexMatrix(data));
        System.out.println("Матрица " + name + " создана.");
    }

    private static String readMatrixName(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static void saveResult(ComplexMatrix result) {
        System.out.print("Сохранить результат под именем (оставьте пустым, чтобы не сохранять): ");
        String resName = scanner.nextLine().trim();
        if (!resName.isEmpty()) {
            matrices.put(resName, result);
            System.out.println("Результат сохранён как " + resName);
        }
    }

    private static void addMatrices() {
        String name1 = readMatrixName("Введите имя первой матрицы: ");
        String name2 = readMatrixName("Введите имя второй матрицы: ");
        ComplexMatrix m1 = matrices.get(name1);
        ComplexMatrix m2 = matrices.get(name2);
        if (m1 == null || m2 == null) {
            System.out.println("Одна или обе матрицы не найдены.");
            return;
        }
        try {
            ComplexMatrix result = m1.add(m2);
            System.out.println("Результат сложения:");
            System.out.println(result);
            saveResult(result);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void multiplyMatrices() {
        String name1 = readMatrixName("Введите имя первой матрицы: ");
        String name2 = readMatrixName("Введите имя второй матрицы: ");
        ComplexMatrix m1 = matrices.get(name1);
        ComplexMatrix m2 = matrices.get(name2);
        if (m1 == null || m2 == null) {
            System.out.println("Одна или обе матрицы не найдены.");
            return;
        }
        try {
            ComplexMatrix result = m1.multiply(m2);
            System.out.println("Результат умножения:");
            System.out.println(result);
            saveResult(result);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void divideMatrices() {
        String name1 = readMatrixName("Введите имя делимой матрицы (A): ");
        String name2 = readMatrixName("Введите имя матрицы-делителя (B): ");
        ComplexMatrix m1 = matrices.get(name1);
        ComplexMatrix m2 = matrices.get(name2);
        if (m1 == null || m2 == null) {
            System.out.println("Одна или обе матрицы не найдены.");
            return;
        }
        try {
            ComplexMatrix result = m1.divide(m2);
            System.out.println("Результат деления (A * B^{-1}):");
            System.out.println(result);
            saveResult(result);
        } catch (IllegalArgumentException | ArithmeticException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void transposeMatrix() {
        String name = readMatrixName("Введите имя матрицы: ");
        ComplexMatrix m = matrices.get(name);
        if (m == null) {
            System.out.println("Матрица не найдена.");
            return;
        }
        ComplexMatrix result = m.transpose();
        System.out.println("Транспонированная матрица:");
        System.out.println(result);
        saveResult(result);
    }

    private static void determinantMatrix() {
        String name = readMatrixName("Введите имя матрицы: ");
        ComplexMatrix m = matrices.get(name);
        if (m == null) {
            System.out.println("Матрица не найдена.");
            return;
        }
        try {
            Complex det = m.determinant();
            System.out.println("Определитель: " + det);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void showAllMatrices() {
        if (matrices.isEmpty()) {
            System.out.println("Матриц пока нет.");
            return;
        }
        for (Map.Entry<String, ComplexMatrix> entry : matrices.entrySet()) {
            System.out.println("Матрица " + entry.getKey() + ":");
            System.out.println(entry.getValue());
        }
    }
}