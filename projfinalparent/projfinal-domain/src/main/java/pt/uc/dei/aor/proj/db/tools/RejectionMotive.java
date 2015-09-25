/*
 */
package pt.uc.dei.aor.proj.db.tools;

/**
 * @author Carlos Santos + Catarina
 */
public enum RejectionMotive {

	CV ("CV"),
	PERSONALITY ("CV Personality"),
	NEGOTIATION("Negotiation"),
	OTHER ("Other rejection motive"),
	NONE ("");
	
	private String description;

	private RejectionMotive (String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}

