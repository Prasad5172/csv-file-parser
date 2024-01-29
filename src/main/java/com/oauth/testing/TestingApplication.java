package com.oauth.testing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication
public class TestingApplication {

	public static void main(String[] args) {
		// SpringApplication.run(TestingApplication.class, args);
		String inputFile = "E:\\SpringBoot\\testing\\src\\main\\java\\com\\oauth\\testing\\testing.csv";
		String outputFile = "E:\\SpringBoot\\testing\\src\\main\\java\\com\\oauth\\testing\\output.csv";

		try {
			List<String[]> parsedData = parseCSV(inputFile);
			List<String[]> cleanedData = cleanData(parsedData);
			Writer fileWriter = new FileWriter(outputFile,true);
			FileWriter clearData = new FileWriter(outputFile);
			clearData.write("");
			
			// Printing cleaned data for demonstration purposes
			for (String[] contractor : cleanedData) {
				// System.out.println("FullName: " + contractor[0]);
				// System.out.println("Title: " + contractor[1]);
				// System.out.println("Email: " + contractor[2]);
				// System.out.println("Phone: " + contractor[3]);
				// System.out.println();
				fileWriter.write(contractor[0]);
				fileWriter.write(",");
				fileWriter.write(contractor[1]);
				fileWriter.write(",");
				fileWriter.write(contractor[2]);
				fileWriter.write(",");
				fileWriter.write(contractor[3]);
				fileWriter.write(System.lineSeparator());
			}
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Parse the CSV file and return a list of String arrays containing the data
	private static List<String[]> parseCSV(String inputFile) throws IOException {
		List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            CSVParser csvParser = new CSVParser(br, CSVFormat.DEFAULT);
            for (CSVRecord csvRecord : csvParser) {
                String[] values = new String[csvRecord.size()];
                for (int i = 0; i < csvRecord.size(); i++) {
                    values[i] = csvRecord.get(i);
                }
                records.add(values);
            }
        }
        return records;
	}

	// Clean the parsed data and place it into proper fields, handling bad data
	private static List<String[]> cleanData(List<String[]> parsedData) {
		List<String[]> cleanedData = new ArrayList<>();
		for (String[] record : parsedData) {
			String fullName = "";
			String title = "";
			String email = "";
			String phone = "";

			// Check for bad data: empty fields or concatenated data
			boolean isBadData = false;
			if(record.length > 4){
				isBadData = true;
			}
			for (String data : record) {
				data = data.trim(); // Remove leading/trailing whitespaces

				if (data.length()==0) {
					isBadData = true;
					break;
				}
			}
			if (!isBadData) {
				for (String data : record) {
					data = data.trim(); // Remove leading/trailing whitespaces

					if (data.contains("@") && data.contains(".")) {
						// Assuming data containing "@" and "." as an email
						email = data;
					} else if (data.matches("\\d{3}-\\d{3}-\\d{4}")) {
						// Assuming data matching phone number pattern as phone number
						phone = data;
					} else {
						// Assuming remaining data as FullName or Title
						if (fullName.isEmpty()) {
							fullName = data;
						} else if (title.isEmpty()) {
							title = data;
						}
					}
				}
				cleanedData.add(new String[] { fullName, title, email, phone });
			}
		}
		return cleanedData;
	}
}
