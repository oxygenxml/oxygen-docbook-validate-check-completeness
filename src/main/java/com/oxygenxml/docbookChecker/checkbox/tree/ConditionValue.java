package com.oxygenxml.docbookChecker.checkbox.tree;

/**
 * The leaf in JCheckBoxTree.
 * @author intern4
 *
 */
public class ConditionValue {
	private String value;
	private String atrib;
	
	public ConditionValue( String atrib, String value) {
		super();
		this.value = value;
		this.atrib = atrib;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public int hashCode() {
		return value.hashCode()+ atrib.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.hashCode() == obj.hashCode());
	}
	
	
	
}
