/*
 */
package pt.uc.dei.aor.proj.db.tools;

/**
 * @author Carlos Santos + Catarina
 */
public enum TechAreas {
	
	SSPA ("SSPA"),
	DOTNETDEVELOPMENT (".Net Development"),
	JAVADEVELOPMENT ("Java Development"),
	SAFETYCRITICAL ("Safety Critical"),
	PROJECTMANAGEMENT ("Project Management"),
	INTEGRATION ("Integration");
	
	private String description;

	private TechAreas (String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
