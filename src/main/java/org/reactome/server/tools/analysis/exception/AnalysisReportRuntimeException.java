package org.reactome.server.tools.analysis.exception;

public class AnalysisReportRuntimeException extends RuntimeException {

	public AnalysisReportRuntimeException(String message) {
		super(message);
	}

	public AnalysisReportRuntimeException(Throwable throwable) {
		super(throwable);
	}

	public AnalysisReportRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
