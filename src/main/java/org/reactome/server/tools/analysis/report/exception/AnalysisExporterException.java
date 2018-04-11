package org.reactome.server.tools.analysis.report.exception;

public class AnalysisExporterException extends Exception {
	public AnalysisExporterException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public AnalysisExporterException(Throwable throwable) {
		super(throwable);
	}
}
