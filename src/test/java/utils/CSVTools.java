package utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVTools {

    public static List<Map<String, String>> getRecords(String path)  {

        CSVFormat csvFormat = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader()
                .build();
        List<Map<String, String>> records = new ArrayList<>();

        try {
            FileReader reader = new FileReader(path);
            CSVParser csvParser = new CSVParser(reader, csvFormat);

            for (CSVRecord csvRecord : csvParser) {
                Map<String, String> record = new HashMap<>();
                for (String header : csvParser.getHeaderNames()) {
                    record.put(header, csvRecord.get(header));
                }
                records.add(record);
            }

            csvParser.close();

        } catch (IOException e){

            e.printStackTrace();
            return null;

        }

        return records;

    }

    public static String[] getColumnValues(List<Map<String, String>> records, String column) {

        List<String> columnValues = new ArrayList<>();

        for (Map<String, String> record : records) {
            String value = record.get(column);
            columnValues.add(value != null ? value : "");
        }

        return columnValues.toArray(new String[0]);

    }

}
