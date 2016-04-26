
package Monitor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.testng.annotations.Test;

public class FileMonitor {

	public static int count = 0, countBuffer = 0, countLine = 0;
	public static String lineNumber = "";

	public static BufferedReader br = null;

	public static String line = "";

	@Test
	public void RobotFile() {
		String filePath = "./File/output.txt";
		String URL = "https://www.makaan.com/robots.txt";
		FileWriter(filePath, URL);
		ReadFileRobot(filePath);

	}

	@Test
	public void SecureSite() {
		String filePath = "./File/output2.xml";
		String URL = "https://www.makaan.com/sitemap/secure-sitemap-index.xml";
		FileWriter(filePath, URL);
		ReadFileXML(filePath);

	}

	public static void ReadFileRobot(String Filepath) {
		try {
			br = new BufferedReader(new FileReader(Filepath));
			try {
				while ((line = br.readLine()) != null) {
					countLine++;
					// System.out.println(line);
					String[] words = line.split(" ");

					for (String word : words) {

						if (word.equals("Disallow:")) {
							count++;
							countBuffer++;
						}
					}

					if (countBuffer > 0) {
						countBuffer = 0;
						lineNumber += countLine + ",";
					}

				}
				br.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
		}

		System.out.println("Times found at--" + count);
		System.out.println("Word found at--" + lineNumber);

	}

	public static void ReadFileXML(String Filepath) {
		try {
			int count = 0;
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(Filepath);

			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("sitemap");

			System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					String Value = eElement.getElementsByTagName("loc").item(0).getTextContent();
					System.out.println("loc : " + Value);
					if (Value.contains("xml.gz")) {
						System.out.println("success");
					} else if (Value.contains("404")) {
						System.out.println("contains 404");
						count++;
					}

				}
			}
			System.out.println(count + "number of times 404 occured");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void FileWriter(String FilePath, String URL) {

		URL url;

		try {
			// get URL content
			url = new URL(URL);
			URLConnection conn = url.openConnection();

			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String inputLine;

			// save to this filename
			// String fileName = "/home/surabhi/Desktop/output.txt";
			File file = new File(FilePath);

			if (!file.exists()) {
				file.createNewFile();
			}

			// use FileWriter to write file
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			while ((inputLine = br.readLine()) != null) {
				bw.write(inputLine);
				bw.newLine();

			}

			bw.close();
			br.close();

			System.out.println("Done");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
