package cn.edu.tsinghua.ee.Dreamland.DreamHalma.utils;

import java.util.Properties;
import java.io.FileInputStream;

//this is the class with functionality to get configurations from ".properties" file
@SuppressWarnings("serial")
public class Configure extends Properties{

	//set up the configuration from reading the file
	public void setConfigure() throws Exception{
		FileInputStream fis = new FileInputStream("./etc/DreamHalma.properties");
		this.load(fis);
		fis.close();
	}
}
