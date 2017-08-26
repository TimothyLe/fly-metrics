import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class ReadXMLFile {

  public static void main(String argv[]) {

    try {
	File fXmlFile = new File("flight_data.xml");
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);

	doc.getDocumentElement().normalize();

	System.out.println("Root element:" + doc.getDocumentElement().getNodeName());

	NodeList nList = doc.getElementsByTagName("ServiceDefinition");

	System.out.println("----------------------------");

	for (int temp = 0; temp < nList.getLength(); temp++) {

		Node nNode = nList.item(temp);

		// System.out.println("\nCurrent Element :" + nNode.getNodeName());

		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;

			System.out.println("\nServiceDefinitionID: " + eElement.getAttribute("ServiceDefinitionID"));
			System.out.println("Name: " + eElement.getElementsByTagName("Name").item(0).getTextContent());
			System.out.println("Code: " + eElement.getElementsByTagName("Code").item(0).getTextContent());
			System.out.println("Description: " + eElement.getElementsByTagName("Text").item(0).getTextContent());
		}
	}
    } catch (Exception e) {
	e.printStackTrace();
    }
  }

}