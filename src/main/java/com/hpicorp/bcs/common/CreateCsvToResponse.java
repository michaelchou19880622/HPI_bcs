package com.hpicorp.bcs.common;

import java.io.PrintWriter;
import java.util.List;

import org.apache.log4j.Logger;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;

public class CreateCsvToResponse {

	static final Logger logger = Logger.getLogger(CreateCsvToResponse.class);

	private CreateCsvToResponse() {
		throw new IllegalStateException("CreateCsvToResponse class");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void createCSV(PrintWriter writer, List<String> titleList, List<String> columnList, List<Object> dataList) {
		try {
			ColumnPositionMappingStrategy mapStrategy = new ColumnPositionMappingStrategy();
			mapStrategy.setType(titleList.getClass());
			mapStrategy.generateHeader();
			mapStrategy.setColumnMapping(columnList.stream().toArray(String[]::new));

			StatefulBeanToCsv btcsv = new StatefulBeanToCsvBuilder(writer).withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(',').build();
			btcsv.write(dataList);
		} catch (CsvException ex) {
			logger.error("createCSV err:" + ex.getMessage());
		}
	}
	
}
