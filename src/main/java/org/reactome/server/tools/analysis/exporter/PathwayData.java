package org.reactome.server.tools.analysis.exporter;

import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.analysis.core.result.model.PathwayBase;
import org.reactome.server.graph.domain.model.Pathway;

public class PathwayData {

	final private PathwayNodeSummary summary;
	final private PathwayBase base;
	private Pathway pathway;

	PathwayData(PathwayNodeSummary summary, PathwayBase base, Pathway pathway) {
		this.summary = summary;
		this.base = base;
		this.pathway = pathway;
	}

	public PathwayBase getBase() {
		return base;
	}

	public PathwayNodeSummary getSummary() {
		return summary;
	}

	public Pathway getPathway() {
		return pathway;
	}
}
