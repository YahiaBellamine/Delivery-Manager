package com.pld.agile.utils.xml;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.*;


public class XMLFileOpener extends FileFilter {

  private static XMLFileOpener instance = null;

  // create the singleton
  XMLFileOpener() {
  }

  // retrieve the instance of the XMLFileOpener class
  public static XMLFileOpener getInstance(){
    if (instance == null) instance = new XMLFileOpener();
    return instance;
  }

  // open only .xml files
  public File open(boolean read) throws ExceptionXML{
    int returnVal;
    JFileChooser jFileChooserXML = new JFileChooser();
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

  // we only accept .xml as the type of file here
  @Override
  public boolean accept(File f) {
    if (f == null) return false;
    if (f.isDirectory()) return false;
    String extension = getExtension(f);
    if (extension == null) return false;
    return extension.contentEquals("xml");
  }

  // all the files are of type XML here
  @Override
  public String getDescription() {
    return "XML file";
  }

  // get the extension of the file in parameter
  private String getExtension(File f) {
    String filename = f.getName();
    int i = filename.lastIndexOf('.');
    if (i>0 && i<filename.length()-1)
      return filename.substring(i+1).toLowerCase();
    return null;
  }
}
