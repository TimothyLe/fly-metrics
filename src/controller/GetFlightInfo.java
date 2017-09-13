package controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Servlet implementation class GetFlightInfo
 */
@WebServlet("/GetFlightInfo")
public class GetFlightInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetFlightInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String from = request.getParameter("from");
		String to = request.getParameter("to");
		String date = request.getParameter("date");
		session.setAttribute("order_date", date);
		System.out.println(from+" "+to+" "+date);
		String flight_info = "Hello";
		try {
			String xml_request = "<AirShoppingRQ xmlns='http://www.iata.org/IATA/EDIST/2017.1' Version='IATA2017.1'>"
					+ "	<Document>" + "		<Name>ZEUS NDC GATEWAY</Name>"
					+ "		<ReferenceVersion>1.0</ReferenceVersion>" + "	</Document>" + "	<Party>"
					+ "		<Sender>" + "			<TravelAgencySender>"
					+ "				<Name>JR TECHNOLOGIES</Name>" + "				<IATA_Number>12312312</IATA_Number>"
					+ "				<AgencyID Owner='Z9'>Z9</AgencyID>" + "			</TravelAgencySender>"
					+ "		</Sender>" + "		<Participants>" + "			<Participant>"
					+ "				<AggregatorParticipant SequenceNumber='1'>"
					+ "					<Name>JR TECHNOLOGIES</Name>"
					+ "					<AggregatorID>88888888</AggregatorID>"
					+ "				</AggregatorParticipant>" + "			</Participant>" + "		</Participants>"
					+ "	</Party>" + "	<CoreQuery>" + "		<OriginDestinations>" + "			<OriginDestination>"
					+ "				<Departure>" + "					<AirportCode>" + from + "</AirportCode>"
					+ "					<Date>" + date + "</Date>" + "				</Departure>"
					+ "				<Arrival>" + "					<AirportCode>" + to + "</AirportCode>"
					+ "				</Arrival>" + "			</OriginDestination>" + "		</OriginDestinations>"
					+ "	</CoreQuery>" + "	<DataLists>" + "		<PassengerList>"
					+ "			<Passenger PassengerID='" + session.getAttribute("username") + "'>"
					+ "				<PTC>ADT</PTC>" + "			</Passenger>" + "		</PassengerList>"
					+ "	</DataLists>" + "</AirShoppingRQ>";
			flight_info = sendPost(xml_request);
			session.setAttribute("xml_response", flight_info);
			Map<String, Map<String, String[]>> a = doParse(flight_info);
			String req_id = getReq_id(flight_info);
			session.setAttribute("req_id", req_id);
			session.setAttribute("flight_info", a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestDispatcher requestdispatcher = request.getRequestDispatcher("success.jsp");
		requestdispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public String sendPost(String request) throws Exception {
		System.out.println(request);
		String url = "http://iata.api.mashery.com/Zeus/NDC";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/xml");
		con.setRequestProperty("Authorization-key", "nt92ahs98ass2anxffzht6u3");

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(request);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		return response.toString();
	}
	
	public String getReq_id(String xml_doc) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		String path_to_flight = "AirShoppingRS/ShoppingResponseID/ResponseID";
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputStream inputStream = new    ByteArrayInputStream(xml_doc.getBytes());
		Document doc = dBuilder.parse(inputStream);
		doc.getDocumentElement().normalize();
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile(path_to_flight);
		Node flights = (Node) expr.evaluate(doc, XPathConstants.NODE);
		return flights.getTextContent();
	}

	public Map<String, Map<String, String[]>> doParse(String xml_doc) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputStream inputStream = new    ByteArrayInputStream(xml_doc.getBytes());
			Document doc = dBuilder.parse(inputStream);
			doc.getDocumentElement().normalize();
			NodeList flights = getFlightInfo(doc);
			Map<String, Map<String, String[]>> travelMap = new HashMap<String, Map<String, String[]>>();
			for (int i = 0; i < flights.getLength(); i++) {
				Node flight = flights.item(i);
				List<Node> flight_segments_node = getFlightSegments(doc, flight);
				Map<String, String[]> segment_map = new HashMap<String, String[]>();
				for (Node flight_segment_node : flight_segments_node) {
					String[] seg_info = getFlightSegmentDetails(doc, flight_segment_node);
					segment_map.put(((Element) flight_segment_node).getAttribute("SegmentKey"), seg_info);
				}
				travelMap.put(((Element) flight).getAttribute("FlightKey"), segment_map);
			}
			System.out.println("### End ###");
			return travelMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public NodeList getFlightInfo(Document doc) throws XPathExpressionException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		String path_to_flight = "/AirShoppingRS/DataLists/FlightList/Flight";
		XPathExpression expr = xpath.compile(path_to_flight);
		NodeList flights = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		return flights;
	}

	public List<Node> getFlightSegments(Document doc, Node flight) throws XPathExpressionException {
		List<Node> flightSegments = new ArrayList<Node>();
		NodeList childNodes = flight.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeName().equalsIgnoreCase("SegmentReferences")) {
				String[] segRefs = child.getTextContent().split(" ");
				XPath xpath = XPathFactory.newInstance().newXPath();
				for (String segRef : segRefs) {
					String path_to_flight_segment = "/AirShoppingRS/DataLists/FlightSegmentList/FlightSegment[@SegmentKey='"
							+ segRef + "']";
					XPathExpression expr = xpath.compile(path_to_flight_segment);
					flightSegments.add((Node) expr.evaluate(doc, XPathConstants.NODE));
				}
			}
		}
		return flightSegments;
	}

	public String[] getFlightSegmentDetails(Document doc, Node flightSegment) throws XPathExpressionException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		String path_to_departure = "/AirShoppingRS/DataLists/FlightSegmentList/FlightSegment[@SegmentKey='"
				+ ((Element) flightSegment).getAttribute("SegmentKey") + "']/Departure";
		String path_to_arrival = "/AirShoppingRS/DataLists/FlightSegmentList/FlightSegment[@SegmentKey='"
				+ ((Element) flightSegment).getAttribute("SegmentKey") + "']/Arrival";

		XPathExpression expr = xpath.compile(path_to_departure);
		Node departure = (Node) expr.evaluate(doc, XPathConstants.NODE);
		expr = xpath.compile(path_to_arrival);
		Node arrival = (Node) expr.evaluate(doc, XPathConstants.NODE);

		expr = xpath.compile(path_to_departure + "/Time");
		Node departure_time = (Node) expr.evaluate(doc, XPathConstants.NODE);
		expr = xpath.compile(path_to_departure + "/Date");
		Node departure_date = (Node) expr.evaluate(doc, XPathConstants.NODE);
		expr = xpath.compile(path_to_departure + "/AirportName");
		Node departure_name = (Node) expr.evaluate(doc, XPathConstants.NODE);

		expr = xpath.compile(path_to_arrival + "/Time");
		Node arrival_time = (Node) expr.evaluate(doc, XPathConstants.NODE);
		expr = xpath.compile(path_to_arrival + "/Date");
		Node arrival_date = (Node) expr.evaluate(doc, XPathConstants.NODE);
		expr = xpath.compile(path_to_arrival + "/AirportName");
		Node arrival_name = (Node) expr.evaluate(doc, XPathConstants.NODE);

		String[] segment_data = { departure_name.getTextContent(), departure_date.getTextContent(),
				departure_time.getTextContent(), arrival_name.getTextContent(), arrival_date.getTextContent(),
				arrival_time.getTextContent() };
		return segment_data;
	}
}
