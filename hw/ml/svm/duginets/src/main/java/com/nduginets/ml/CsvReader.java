package com.nduginets.ml;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CsvReader<T> {

    private final File csvPath;

    public CsvReader(String csvPath) {
        this.csvPath = new File(csvPath);
    }

    public CsvData<T> parseCsv(Function<String, T> converter) {
        try (CSVParser parser = CSVParser.parse(csvPath, StandardCharsets.UTF_8, CSVFormat.DEFAULT)) {
            List<List<T>> rows = new ArrayList<>();
            boolean isHeader = true;
            for (CSVRecord record : parser.getRecords()) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                List<T> col = new ArrayList<>();
                for (String currentColumn : record) {
                    col.add(converter.apply(currentColumn));
                }
                rows.add(col);
            }
            return new CsvData<>(parser.getHeaderMap(), rows);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public List<List<String>> readToStringList() {
        try (CSVParser parser = CSVParser.parse(csvPath, StandardCharsets.UTF_8, CSVFormat.DEFAULT)) {
            List<List<String>> rows = new ArrayList<>();
            boolean isHeader = true;
            for (CSVRecord record : parser.getRecords()) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                List<String> col = new ArrayList<>();
                for (String currentColumn : record) {
                    col.add(currentColumn);
                }
                rows.add(col);
            }
            return rows;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static class CsvData<T> {
        private final Map<String, Integer> header;
        private final List<List<T>> rows;

        private CsvData(Map<String, Integer> header, List<List<T>> rows) {
            this.header = header;
            this.rows = rows;
        }

        public Map<String, Integer> getHeader() {
            return header;
        }

        public List<List<T>> getRows() {
            return rows;
        }

        @Override
        public String toString() {
            return "CsvData{" +
                    "header=" + header +
                    ", rows=" + rows +
                    '}';
        }
    }

}
