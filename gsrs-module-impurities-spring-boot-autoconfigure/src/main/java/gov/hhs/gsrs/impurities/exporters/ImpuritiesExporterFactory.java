package gov.hhs.gsrs.impurities.exporters;

import gov.hhs.gsrs.impurities.models.Impurities;

import ix.ginas.exporters.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;;
import java.util.*;

public class ImpuritiesExporterFactory implements ExporterFactory {

	private static final Set<OutputFormat> FORMATS;

	static {
		Set<OutputFormat> set = new LinkedHashSet<>();
		set.add(SpreadsheetFormat.TSV);
		set.add(SpreadsheetFormat.CSV);
		set.add(SpreadsheetFormat.XLSX);

		FORMATS = Collections.unmodifiableSet(set);
	}

	@Override
	public Set<OutputFormat> getSupportedFormats() {
		return FORMATS;
	}

	@Override
	public boolean supports(Parameters params) {
		return params.getFormat() instanceof SpreadsheetFormat;
	}

	@Override
	public ImpuritiesExporter createNewExporter(OutputStream out, Parameters params) throws IOException {

		SpreadsheetFormat format = SpreadsheetFormat.XLSX;
		Spreadsheet spreadsheet = format.createSpeadsheet(out);

		ImpuritiesExporter.Builder builder = new ImpuritiesExporter.Builder(spreadsheet);
		configure(builder, params);
		
		return builder.build();
	}

	protected void configure(ImpuritiesExporter.Builder builder, Parameters params) {
		builder.includePublicDataOnly(params.publicOnly());
	}

}