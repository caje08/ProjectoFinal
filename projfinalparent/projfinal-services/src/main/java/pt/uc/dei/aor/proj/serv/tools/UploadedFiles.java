/*
 */
package pt.uc.dei.aor.proj.serv.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;

/**
 * @author Carlos + Catarina
 */
public class UploadedFiles {

	/**
	 * Creates a new instance of UploadedFiles
	 */
	public UploadedFiles() {
	}

	private final String CV = "cv";
	private final String COVERLETTER = "cl";
	private final String LOGO = "logo";
	private final String CVDESTINATION = BundleUtils.getSettings("cvpath");
	private final String COVERLETTERDESTINATION = BundleUtils.getSettings("clpath");
	private final String LOGODESTINATION = BundleUtils.getSettings("imgpath");

	private String cvUploadName;
	private String clUploadName;
	private String logoUploadName;
	private String finalLogoDestination;
	private String finalCvDestination;
	private String finalCoverLetterDestination;

	/**
	 * Upload files depending on type of Upload File Selected
	 *
	 * @param event
	 * @param type
	 */
	public void upload(FileUploadEvent event, String type) {

		try {
			switch (type) {
			case CV:
				copyFile(event.getFile().getFileName(), event.getFile().getInputstream(), CV);
				getCvUploadName();
				break;
			case COVERLETTER:
				copyFile(event.getFile().getFileName(), event.getFile().getInputstream(), COVERLETTER);
				getClUploadName();
				break;
			case LOGO:
				copyFile(event.getFile().getFileName(), event.getFile().getInputstream(), LOGO);
				getLogoUploadName();
				break;
			}

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	/**
	 * cvUploadName, clUploadName and logoUploadName are random names with
	 * extension of upload file
	 * finalCvDestination,finalCoverLetterDestination
	 * and finalLogoDestination are cvUploadName, clUploadName and
	 * logoUploadName,with predefined path defined by User Copy file, depending
	 * on type, to a specific path, configured on property file
	 *
	 * @param fileName
	 * @param in
	 * @param type
	 */

	public void copyFile(String fileName, InputStream in, String type) {
		OutputStream out = null;
		try {
			// write the inputStream to a FileOutputStream
			File cvFile = new File(CVDESTINATION);
			File slFile = new File(COVERLETTERDESTINATION);
			File logoFile = new File(LOGODESTINATION);

			switch (type) {
			case CV:
				if (!cvFile.exists()) {
					cvFile.mkdirs();
				}

				cvUploadName = java.net.URLEncoder.encode(RandomName.getRandomName(10) + "." + FilenameUtils.getExtension(fileName), "utf-8");
				finalCvDestination = CVDESTINATION + cvUploadName;
				out = new FileOutputStream(new File(finalCvDestination));

				break;
			case COVERLETTER:
				if (!slFile.exists()) {
					slFile.mkdirs();
				}
				clUploadName = java.net.URLEncoder.encode(RandomName.getRandomName(10) + "." + FilenameUtils.getExtension(fileName), "utf-8");
				finalCoverLetterDestination = COVERLETTERDESTINATION + clUploadName;
				out = new FileOutputStream(new File(finalCoverLetterDestination));
				break;
			case LOGO:
				if (!logoFile.exists()) {
					logoFile.mkdirs();
				}
				logoUploadName = java.net.URLEncoder.encode(RandomName.getRandomName(10) + "." + FilenameUtils.getExtension(fileName), "utf-8");
				finalLogoDestination = LOGODESTINATION + logoUploadName;
				out = new FileOutputStream(new File(finalLogoDestination));
				break;

			}

			int read = 0;

			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			in.close();

			out.flush();

			out.close();

			System.out.println("New file created!");

		} catch (IOException e) {

			System.out.println(e.getMessage());
		}
	}

	/////////////////////Getters && Setters////////////////////
	public String getLOGO() {
		return LOGO;
	}

	public String getCV() {
		return CV;
	}

	public String getCvUploadName() {
		return cvUploadName;
	}

	public void setCvUploadName(String cvUploadName) {
		this.cvUploadName = cvUploadName;
	}

	public String getClUploadName() {
		return clUploadName;
	}

	public void setClUploadName(String clUploadName) {
		this.clUploadName = clUploadName;
	}

	public String getFinalCoverLetterDestination() {
		return finalCoverLetterDestination;
	}

	public void setFinalCoverLetterDestination(String finalCoverLetterDestination) {
		this.finalCoverLetterDestination = finalCoverLetterDestination;
	}

	public String getFinalCvDestination() {
		return finalCvDestination;
	}

	public void setFinalCvDestination(String finalCvDestination) {
		this.finalCvDestination = finalCvDestination;
	}

	public String getFinalSourceLetterDestination() {
		return finalCoverLetterDestination;
	}

	public void setFinalSourceLetterDestination(String finalSourceLetterDestination) {
		this.finalCoverLetterDestination = finalSourceLetterDestination;
	}

	public String getCOVERLETTER() {
		return COVERLETTER;
	}

	public String getCVDESTINATION() {
		return CVDESTINATION;
	}

	public String getCOVERLETTERDESTINATION() {
		return COVERLETTERDESTINATION;
	}

	public String getLOGODESTINATION() {
		return LOGODESTINATION;
	}

	public String getFinalLogoDestination() {
		return finalLogoDestination;
	}

	public void setFinalLogoDestination(String finalLogoDestination) {
		this.finalLogoDestination = finalLogoDestination;
	}

	public String getLogoUploadName() {
		return logoUploadName;
	}

	public void setLogoUploadName(String logoUploadName) {
		this.logoUploadName = logoUploadName;
	}

}
