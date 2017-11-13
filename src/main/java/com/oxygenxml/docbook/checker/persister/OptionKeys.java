package com.oxygenxml.docbook.checker.persister;

/**
 * Option keys used for persistence.
 * @author intern4
 *
 */
public class OptionKeys {

	/**
	 * Private constructor.
	 */
	 private OptionKeys() {
	    throw new IllegalStateException("Utility class");
	  }
	
	public static final String CHECK_CURRENT_RESOURCE = "check.current.resource";
	public static final String CONFIG_CONDITION_SET_MANUALLY = "manually.configure.condition.set";
	public static final String USE_PROFILING = "use.profiling";
	public static final String CHECK_EXTERNAL_RESOURCES = "check.external.resources";
	public static final String CHECK_BROKEN_IMAGES = "check.broken.images";
	public static final String CHECK_INTERNAL_LINKS = "check.internal.links";
	public static final String GENERATE_HIERARCHY_REPORT = "generate.hierarchy.report";
	public static final String REPORTE_UNDEFINED_CONDITIONS = "report.undefined.conditions";
	public static final String DOCUMENT_TYPES = "document.types.list";
	public static final String SELECTED_DOCUMENT_TYPE = "selected.document.type";
	public static final String RESOURCES_TO_CHECK = "check.resources.list";
	public static final String CONDITIONS_USED_TO_CHECK = "conditions.used.to.check";

}
