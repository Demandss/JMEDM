package su.demands;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class Controller {
    @Getter
    List<Double> inputData;
    @Getter
    List<String> dataForFormulas;
    @Getter
    List<String> dataForFormula;
    @Getter
    List<String> foundUniqueOperands;

    String[][] dataForFormulasArray;
    int i = 0, j = 0;
    public Controller() {
        inputData = getDataFromResource("inputdata.txt")
                .stream().mapToDouble(Double::parseDouble).boxed().toList();

        dataForFormulas = getDataFromResource("dataforformulas.txt");
        dataForFormulas.remove(0);
        dataForFormulasArray = new String[dataForFormulas.size()][3];

        dataForFormulas.forEach(s -> {
            for (String var : s.split("\\|")) {
                dataForFormulasArray[i][j++] = var.replaceAll(" ","");
                if (j == 3)
                    j = 0;
            }
            i++;
        });
    }

    public double getTimeUntilTestingIsCompleted(int modifier) {
        double sum = 0.0d;
        for (int k = inputData.size(); k < inputData.size() + modifier; k++) {
           sum += getXnPlusOne(k);
        }
        return sum;
    }

    public double getTotalTestingTime(int modifier) {
        return getTimeUntilTestingIsCompleted(modifier) + sumOffDataBottom();
    }

    private double getXnPlusOne(int n) {
        return 1 / (getK() * (getB() - n));
    }

    private double getK() {
        return inputData.size() / ((getB() + 1) * sumOffDataBottom() - sumOffDataTop());
    }

    private int getB() {
        return getMinGnDifferenceFn() - 1;
    }

    private double sumOffDataTop() {
        int sum = 0;
        for (int i = 0; i < inputData.size(); i++) {
            sum += (i + 1) * inputData.get(i);
        }
        return sum;
    }

    private double sumOffDataBottom() {
        return inputData.stream().mapToDouble(Double::doubleValue).sum();
    }

    private double getA() {
        return sumOffDataTop() / sumOffDataBottom();
    }

    private double getFn(int m) {
        double sum = 0;
        for (int i = 0; i < inputData.size(); i++) {
            sum += 1.0D / (m - (i+1));
        }
        return sum;
    }

    private int getMinGnDifferenceFn() {
        double[] nums = new double[dataForFormulas.size()];
        for (int k = 0; k < dataForFormulas.size(); k++) {
            for (int j = inputData.size(); j < inputData.size() + dataForFormulas.size(); j++) {
                nums[k] = Math.max(
                        Math.abs(Double.parseDouble(dataForFormulasArray[k][2]) - getGnDifferenceFn(j)),
                        Double.parseDouble(dataForFormulasArray[k][2])
                );
            }
        }
        int number = 0;
        for (int i1 = 0; i1 < dataForFormulasArray.length; i1++) {
            if (Double.parseDouble(dataForFormulasArray[i1][2]) == Arrays.stream(nums).min().getAsDouble()) {
                number = inputData.size() + i1;
            }
        }
        return number;
    }

    private double getGnDifferenceFn(int m) {
        return Math.abs(getFn(m) - getGn(m));
    }

    private double getGn(int m) {
        return inputData.size() / (m - getA());
    }

    private boolean convergenceCondition() {
        return getA() > (inputData.size() + 1) / 2.0f;
    }

    @SneakyThrows
    private List<String> getDataFromResource(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new BufferedReader(new FileReader(new File(resource.toURI()))).lines()
                    .map(String::toLowerCase).collect(Collectors.toList());
        }
    }

    public boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
