package test.model.fileaccess;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.filewriter.FileAccess;
import test.GeneralTestUtilityClass;
//@Execution(value = ExecutionMode.SAME_THREAD)
class FileAccessTest {	
	private FileAccess fa;
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	private String fileNAE = FileAccess.getDefaultFileNameForClass()+FileAccess.getExtensionForClass();

	private String fileContent =
			"line1"+System.lineSeparator()+
			"somelongline1"+System.lineSeparator()+
			"someline2"+System.lineSeparator()+
			"somelongline3";
	
	private String getAddress() {
		return this.testFolderAddress+File.separator+this.fileNAE;
	}
	
	@BeforeEach
	void prep() {
		GeneralTestUtilityClass.deletePathContent(new File(this.testFolderAddress));
		fa = new FileAccess(this.testFolderAddress) {};
		Assertions.assertTrue(fa.isAddressValid());
		Assertions.assertTrue(fa.fileExists());
	}
	
	@AfterEach
	void cleanUp() {
		fa.close();
		fa.deleteFile();
		GeneralTestUtilityClass.deletePathContent(new File(this.testFolderAddress));
	}
	
	@Test
	void writeTest() {
		Assertions.assertTrue(fa.writeToFile(fileContent));
		File f = new File(this.getAddress());
//		try {
//			BufferedReader r = null;
//			try {
//				r = new BufferedReader(new FileReader(f));
//			} catch (FileNotFoundException e) {
//				this.deleteFile(f);
//				fail();
//			}
//			String ls = r.lines().reduce("", (l1,l2) -> {return l1 + System.lineSeparator() + l2;});
//			if (ls.startsWith(System.lineSeparator())) {
//				ls = ls.replaceFirst(System.lineSeparator(), "");
//			}
//			Assertions.assertEquals(ls,fileContent);
//			try {
//				r.close();
//			} catch (IOException e) {
//				this.deleteFile(f);
//				fail();
//			}
//		} catch (Exception e) {
//			this.deleteFile(f);
//			fail();
//		}
		Assertions.assertEquals(this.readFile(f),fileContent);
		this.deleteFile(f);
	}
	
	private void deleteFile(File f) {
		f.delete();
		f.deleteOnExit();
		GeneralTestUtilityClass.deletePathContent(new File(this.testFolderAddress));
	}
	
	@Test
	void addressTest() {
		Assertions.assertEquals(this.getAddress(), fa.getFilePath());
	}
	
	private void fillFile(File f) {
		try {
			PrintWriter w = new PrintWriter(new FileWriter(f));
			w.write(fileContent);
			w.flush();
			w.close();
		} catch (IOException e) {
			this.deleteFile(f);
			fail();
		}
	}
	
	private String readFile(File f) {
		try {
			BufferedReader r = null;
			try {
				r = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				this.deleteFile(f);
				fail();
			}
			String ls = r.lines().reduce("", (l1,l2) -> {return l1 + System.lineSeparator() + l2;});
			if (ls.startsWith(System.lineSeparator())) {
				ls = ls.replaceFirst(System.lineSeparator(), "");
			}
			try {
				r.close();
			} catch (IOException e) {
				this.deleteFile(f);
				fail();
			}
			return ls;
		} catch (Exception e) {
			this.deleteFile(f);
			fail();
		}
		return null;
	}
	
	@Test
	void readTest() {
		File f = new File(this.getAddress());
		this.fillFile(f);
		String content = fa.readFile();
		if (!content.equals(fileContent)) {
			this.deleteFile(f);
			fail("Expected to read: " +
			fileContent + System.lineSeparator() +
			"Actually read: " + content);
		}
	}
	
	@Test
	void deleteTest() {
		Assertions.assertTrue((new File(this.getAddress())).exists());
		Assertions.assertTrue(fa.deleteFile());
		Assertions.assertFalse((new File(this.getAddress())).exists());
	}
	
	@Test
	void remakeTest() {
		Assertions.assertTrue((new File(this.getAddress())).exists());
		Assertions.assertTrue(fa.writeToFile(fileContent));
		Assertions.assertEquals(fa.readFile(), fileContent);
		Assertions.assertTrue((new File(this.getAddress())).exists());
		Assertions.assertTrue(fa.remakeFile());
		Assertions.assertTrue((new File(this.getAddress())).exists());
		Assertions.assertTrue(fa.readFile() == null || fa.readFile().equals(""));
	}
	
	@Test
	void getNullFileTest() {
		Assertions.assertTrue((new File(this.getAddress())).exists());
		Assertions.assertTrue(fa.deleteFile());
		Assertions.assertFalse((new File(this.getAddress())).exists());
		Assertions.assertDoesNotThrow(() -> {fa.getFilePath();});
		Assertions.assertTrue((new File(this.getAddress())).exists());
	}
	
	@Test
	void remakeNullFileTest() {
		Assertions.assertTrue((new File(this.getAddress())).exists());
		Assertions.assertTrue(fa.deleteFile());
		Assertions.assertFalse((new File(this.getAddress())).exists());
		Assertions.assertTrue(fa.remakeFile());
		Assertions.assertTrue((new File(this.getAddress())).exists());
	}
	
	@Test
	void nullFolderAddressNoExceptionsTest() {
		Assertions.assertDoesNotThrow(()->{
			fa = new FileAccess(null) {};
			Assertions.assertFalse(fa.deleteFile());
			Assertions.assertFalse(fa.fileExists());
			Assertions.assertTrue(fa.getFilePath() == null);
			Assertions.assertFalse(fa.isAddressValid());
			Assertions.assertFalse(fa.writeToFile(fileContent));
			Assertions.assertTrue(fa.readFile() == null || fa.readFile().equals(""));
			Assertions.assertFalse(fa.remakeFile());
		});
	}
	
	@Test
	void changeFolderAddressTest() {
		String newFolderAddress = this.testFolderAddress+File.separator+"subfolder";
		File folder = new File(newFolderAddress);
		folder.mkdir();
		Assertions.assertTrue(folder.exists() && folder.isDirectory());
		fa.setAddress(folder.getPath());
		Assertions.assertEquals(fa.getAddress(), newFolderAddress);
	}
	
	@Test
	void migrationTest() {
		String newFolderAddress = this.testFolderAddress+File.separator+"subfolder";
		File folder = new File(newFolderAddress);
		folder.mkdir();
		Assertions.assertTrue(folder.exists() && folder.isDirectory());
		fa.setAddress(folder.getPath());
		String newFileAddress = newFolderAddress+File.separator+this.fileNAE;
		Assertions.assertEquals(fa.getFilePath(), newFileAddress);
		Assertions.assertTrue(fa.deleteFile());
	}
	
	@Test
	void loadExistingFileTest() {
		String fileAddress = testFolderAddress+File.separator+"testFile.txt";
		File f = new File(fileAddress);
		Assertions.assertFalse(f.length() > 0);
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedWriter w = null;
		
		try {
			w = new BufferedWriter(new FileWriter(f));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			w.write(fileContent);
			w.flush();
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fa.setAddress(fileAddress);
		Assertions.assertEquals(fileContent, fa.readFile());
		Assertions.assertTrue(fa.deleteFile());
	}
	
	@Test
	void remakeFileTest() {
		String fileAddress = testFolderAddress+File.separator+"testFile.txt";
		File f = new File(fileAddress);
		Assertions.assertFalse(f.length() > 0);
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedWriter w = null;
		
		try {
			w = new BufferedWriter(new FileWriter(f));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			w.write(fileContent);
			w.flush();
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fa.setAddress(fileAddress);
		Assertions.assertEquals(fileContent, fa.readFile());
//		System.out.println("File address: " + fa.getAddress());
//		System.out.println("File name: " + fa.getFileName());
		Assertions.assertTrue(fa.remakeFile());
		File f2 = new File(fileAddress);
		Assertions.assertFalse(f2.length() > 0);
		try {
			f2.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String readContent = fa.readFile();
		Assertions.assertTrue(readContent == null || readContent.equals(""));
	}
}
