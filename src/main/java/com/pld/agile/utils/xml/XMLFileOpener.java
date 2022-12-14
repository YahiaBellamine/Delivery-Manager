package com.pld.agile.utils.xml;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.*;

/**
 *
 */
public class XMLFileOpener extends FileFilter {

  /** The instance of XMLFileOpener */
  private static XMLFileOpener instance = null;

  /** Create the singleton */
  XMLFileOpener() {
  }


  /**
   * Retrieve the instance of the XMLFileOpener class.
   * @return The XMLFileOpener instance.
   */
  public static XMLFileOpener getInstance(){
    if (instance == null) instance = new XMLFileOpener();
    return instance;
  }

  //

  /**
   * Open only .xml files.
   * @param read if opening in read mode
   * @param defaultPath The path to the default folder.
   * @return The xml File.
   */
  public File open(boolean read, String defaultPath) throws ExceptionXML{
    int returnVal;
    JFileChooser jFileChooserXML;
    if(!defaultPath.isEmpty()){
      jFileChooserXML = new JFileChooser(defaultPath);
    }
    else{
      jFileChooserXML = new JFileChooser();
    }
    jFileChooserXML.setFileFilter(this);
    jFileChooserXML.setFileSelectionMode(JFileChooser.FILES_ONLY);

    if (read)
      returnVal = jFileChooserXML.showOpenDialog(null);
    else
      returnVal = jFileChooserXML.showSaveDialog(null);
    if (returnVal != JFileChooser.APPROVE_OPTION)
      throw new ExceptionXML("Problem when opening file");
    return new File(jFileChooserXML.getSelectedFile().getAbsolutePath());
  }

  //

  /**
   *
   * @param f the File to test
   * @return true if the File extension is xml.
   */
  @Override
  public boolean accept(File f) {
    if (f == null) return false;
    if (f.isDirectory()) return false;
    String extension = getExtension(f);
    if (extension == null) return false;
    return extension.contentEquals("xml");
  }

  /**
   *
   * @return "XML file"
   */
  @Override
  public String getDescription() {
    return "XML file";
  }

  /**
   * Returns the extension of a file.
   * @param f The File instance.
   * @return The extension in a String.
   */
  private String getExtension(File f) {
    String filename = f.getName();
    int i = filename.lastIndexOf('.');
    if (i>0 && i<filename.length()-1)
      return filename.substring(i+1).toLowerCase();
    return null;
  }
}
