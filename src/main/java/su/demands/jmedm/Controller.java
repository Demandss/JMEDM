package su.demands.jmedm;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.List;
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
    int i = -1, j = 0;
    public Controller() {
        inputData = getDataFromResource("inputdata.txt")
                .stream().mapToDouble(Double::parseDouble).boxed().toList();

        dataForFormulas = getDataFromResource("dataforformulas.txt");
        dataForFormulas.remove(0);
        dataForFormulasArray = new String[dataForFormulas.size()][3];

        dataForFormulas.forEach(s -> {
            i++;
            for (String var : s.split("\\|")) {
                dataForFormulasArray[i][j++] = var;
                if (j == 3)
                    j = 0;
            }
        });

        System.out.println(getFn(36));
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
            sum = 1.0D / (m - (i+1));
        }
        return sum;
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
}
