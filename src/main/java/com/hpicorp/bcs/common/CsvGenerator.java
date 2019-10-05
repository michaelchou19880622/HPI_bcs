package com.hpicorp.bcs.common;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.hpicorp.bcs.entities.dto.CsvGeneratorDto;

public class CsvGenerator {

	
	private static final Logger logger = LoggerFactory.getLogger(CsvGenerator.class);

	
	private CsvGenerator() {
		
	}
	public static void writeWithResponse(HttpServletResponse response, CsvGeneratorDto dto) {
		List<String> dtoHeaders = new ArrayList<>();
		List<List<Object>> rows = new ArrayList<>(); 
		if (dto != null) {
			dtoHeaders = dto.getHeaders() == null ? dtoHeaders : dto.getHeaders();
			rows = dto.getRows() == null ? rows : dto.getRows();
		}
		String[] headers = dtoHeaders.stream().toArray(String[]::new);
		headers = dtoHeaders.toArray(headers);
		writeWithResponse(response, headers, rows);
	}
	public static void writeWithResponse(HttpServletResponse response, String[] headers, List<List<Object>> row) {
		try {
			response.setContentType("text/csv;charset=UTF-8");
			String csvFileName = "result.csv";
			String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
	        response.setHeader(headerKey, headerValue);
			writeWithWriter(response.getWriter(), headers, row);
		} catch (Exception e) {
			response.setStatus(400);
		}
	}
	
	public static void writeWithWriter(Writer writer, String[] headers, List<List<Object>> row) {
		try {
			writer.write('\uFEFF');
			CsvListWriter csvWriter = new CsvListWriter(writer,
	                CsvPreference.STANDARD_PREFERENCE);
			csvWriter.writeHeader(headers);
			if (row != null && ! row.isEmpty()) {
				for (List<Object> list : row) {
					csvWriter.write(list);
				}
			}
			csvWriter.close();
		} catch (Exception e) {
			logger.error("CSV Exporter writeWithWriter", e);
		}
	}
}
