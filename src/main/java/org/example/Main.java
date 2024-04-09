package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        // Création d'un nouveau classeur Excel
        Workbook workbook = new XSSFWorkbook();

        // Création d'une feuille dans le classeur Excel
        Sheet sheet = workbook.createSheet("XBRL");

        try (Stream<Path> paths = Files.walk(Paths.get("XBRL"))) {
            // Parcours des fichiers dans le répertoire XBRL
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> getData(sheet, path));

            // Obtention du chemin du répertoire courant
            File currDir = new File(".");
            String path = currDir.getCanonicalPath();
            // Génération d'un nom de fichier unique pour le résultat
            String fileLocation = path + "/result-" + System.currentTimeMillis() + ".xlsx";

            // Écriture du classeur Excel dans un fichier
            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getData(Sheet sheet, Path path) {
        try {
            // Création d'une nouvelle ligne pour le nom du fichier
            Row filename = sheet.createRow(sheet.getLastRowNum() + 1);
            Cell cell = filename.createCell(0);
            cell.setCellValue("FileName: " + path.getFileName());

            List<String> result = new LinkedList<>();
            // Lecture des lignes du fichier pour extraire les données
            Files.readAllLines(path).forEach(line -> extractData(result, line));
            // Tri des données extraites
            Collections.sort(result);
            // Ajout des lignes triées dans la feuille Excel
            result.forEach(line -> {
                Row rowLine = sheet.createRow(sheet.getLastRowNum() + 1);
                Cell cellLine = rowLine.createCell(0);
                cellLine.setCellValue(line);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void extractData(List<String> result, String line) {
        // Utilisation d'une expression régulière pour extraire les données spécifiques
        Matcher matcher = Pattern.compile("\\s*<pfs:([^\\s]*)[^>]*>([0-9]*[.]?[0-9]*)</pfs:.*>").matcher(line);

        if (matcher.matches()) {
            // Récupération des parties correspondantes de la ligne
            String name = matcher.group(1);
            String decimal = matcher.group(2);
            // Ajout du résultat formaté à la liste
            result.add(name + "," + decimal);
        }
    }
}