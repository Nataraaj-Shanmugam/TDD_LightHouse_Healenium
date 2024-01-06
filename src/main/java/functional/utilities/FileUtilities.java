package functional.utilities;

import java.io.File;

import functional.genericKeywords.GenericKeywords;
import functional.genericKeywords.ProjectCustomException;

/**
 * Utility class for handling file operations such as renaming and deleting files.
 * Extends the GenericKeywords class for additional utility functionalities.
 */
public class FileUtilities extends GenericKeywords {

	/**
	 * Renames a file from one name and extension to another.
	 *
	 * @param sourceFilePath The path of the file to be renamed.
	 * @param destinationFilePath The destination path for the renamed file.
	 * @param currentFileName The current name of the file (without extension).
	 * @param currentFileExtension The current file extension.
	 * @param renameFileName The new name for the file (without extension).
	 * @param renameFileExtension The new file extension.
	 */
	public void renameFile(String sourceFilePath, String destinationFilePath, String currentFileName, String currentFileExtension, String renameFileName, String renameFileExtension) {
		try {
			getFile(sourceFilePath, currentFileName+"."+currentFileExtension).renameTo(new File(destinationFilePath+"\\"+renameFileName+"."+renameFileExtension));
		} catch (Exception e) {
			new ProjectCustomException(getClassName(), getMethodName(), e,"Unable to perform file rename operation for file </br>Source Path : "+sourceFilePath+"</br>Source file Name: "+currentFileName+"."+currentFileExtension+"</br>Destination Path : "+destinationFilePath);
		}
	}


	/**
	 * Deletes a file from a specified path.
	 *
	 * @param filePath The path where the file is located.
	 * @param fileName The name of the file to be deleted.
	 * @return true if the file is successfully deleted, false otherwise.
	 */	public boolean deleteFile(String filePath, String fileName) {
		boolean flag = false;
		try {
			if(getFile(filePath, fileName).delete())
				flag=true;
			else
				new Exception();
		}catch (NullPointerException e) {
			new ProjectCustomException(getClassName(), getMethodName(), e,"Unable to perform file delete operation for file </br>File Path : "+filePath+"</br>File Name: "+fileName);
		}
		return  flag;
	}

	/**
	 * Retrieves a File object for a specific file located in a given path.
	 *
	 * @param filePath The path where the file is located.
	 * @param fileName The name of the file to be retrieved.
	 * @return The File object representing the specified file.
	 */
	public File getFile(String filePath, String fileName) {
		File file = null ;
		try {
			for (File currentFile : getFiles(filePath)) {
				if(currentFile.getName().equals(fileName)) {
					file=currentFile;
					break;
				}
			}
		} catch (Exception e) {
			new ProjectCustomException(getClassName(), getMethodName(), e,"Unable to get file '"+fileName+"' from specified path '"+filePath+"'");
		}
		return file;
	}

	/**
	 * Retrieves an array of File objects from a specified directory.
	 *
	 * @param filePath The path of the directory from which to list the files.
	 * @return An array of File objects from the specified directory.
	 */
	public File[] getFiles(String filePath) {
		return new File(filePath).listFiles();
	}
}
