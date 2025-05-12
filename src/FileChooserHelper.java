import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileChooserHelper {

    public static void saveItemsToExcel(List<ShoppingItem> items) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save List as Excel");
        chooser.setFileFilter(new FileNameExtensionFilter("Excel files (*.xlsx)", "xlsx"));

        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".xlsx")) {
                file = new File(file.getAbsolutePath() + ".xlsx");
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Shopping List");

                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("ID");
                header.createCell(1).setCellValue("Item");

                int rowNum = 1;
                for (int i = 0; i < items.size(); i++) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(i + 1);
                    row.createCell(1).setCellValue(items.get(i).getName());
                }

                try (FileOutputStream out = new FileOutputStream(file)) {
                    workbook.write(out);
                }

                JOptionPane.showMessageDialog(null, "File saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                showError("Failed to save file: " + e.getMessage());
            }
        }
    }

    public static List<ShoppingItem> loadItemsFromExcel(Component parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load Excel File");
        chooser.setFileFilter(new FileNameExtensionFilter("Excel files (*.xlsx)", "xlsx"));

        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            try (FileInputStream fis = new FileInputStream(file);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheetAt(0);
                Row header = sheet.getRow(0);

                if (!isHeaderValid(header)) {
                    showError("Invalid Excel file format.");
                    return null;
                }

                List<ShoppingItem> items = new ArrayList<>();
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    Cell nameCell = row.getCell(1);
                    if (nameCell != null && nameCell.getCellType() == CellType.STRING) {
                        String name = nameCell.getStringCellValue().trim();
                        if (!name.isEmpty()) {
                            items.add(new ShoppingItem(name));
                        }
                    }
                }
                return items;
            } catch (IOException e) {
                showError("Failed to load file: " + e.getMessage());
            }
        }
        return null;
    }

    private static boolean isHeaderValid(Row header) {
        if (header == null || header.getPhysicalNumberOfCells() < 2) return false;

        Cell idCell = header.getCell(0);
        Cell itemCell = header.getCell(1);
        return idCell != null && itemCell != null &&
                "ID".equalsIgnoreCase(idCell.getStringCellValue()) &&
                "Item".equalsIgnoreCase(itemCell.getStringCellValue());
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}