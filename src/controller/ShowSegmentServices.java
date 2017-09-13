package controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
 * Servlet implementation class ShowSegmentServices
 */
@WebServlet("/ShowSegmentServices")
public class ShowSegmentServices extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ShowSegmentServices() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @throws IOException
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String xml_doc = (String) session.getAttribute("xml_response");
		Map<String, Map<String, String[]>> flight_info = (Map<String, Map<String, String[]>>) session
				.getAttribute("flight_info");

		String flight_id = request.getParameter("flight");
		Map<String, String[]> segments_ids = flight_info.get(flight_id);
		Map<String, List<ServiceModel>> segment_services = new HashMap<String, List<ServiceModel>>();
		try {
			for (String segment_id : segments_ids.keySet()) {
				List<ServiceModel> segment_service= getSegmentService(segment_id, xml_doc);
				segment_services.put(segment_id, segment_service);
			}
			session.setAttribute("segment_services", segment_services);
			RequestDispatcher requestdispatcher = request.getRequestDispatcher("showSegments.jsp");
			requestdispatcher.forward(request, response);
		} catch (XPathExpressionException | ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public List<ServiceModel> getSegmentService(String segment_key, String xml_doc)
			throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputStream inputStream = new    ByteArrayInputStream(xml_doc.getBytes());
		Document doc = dBuilder.parse(inputStream);
		XPath xpath = XPathFactory.newInstance().newXPath();
		String path_to_offers_items = "/AirShoppingRS/OffersGroup/AirlineOffers/ALaCarteOffer/ALaCarteOfferItem";
		XPathExpression expr = xpath.compile(path_to_offers_items);
		NodeList offeritems = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		List<ServiceModel> service_model_list = new ArrayList<ServiceModel>();
		for(int i =0;i<offeritems.getLength();i++){
			Node offeritem = offeritems.item(i);
			String offerItemID = ((Element)offeritem).getAttribute("OfferItemID");
			String path_to_segments = "/AirShoppingRS/OffersGroup/AirlineOffers/ALaCarteOffer/ALaCarteOfferItem[@OfferItemID='"+offerItemID+"']/Eligibility/SegmentRefs";
			expr = xpath.compile(path_to_segments);
			Node segment_node = (Node) expr.evaluate(doc, XPathConstants.NODE);
			String segments = segment_node.getTextContent();
			if(segments.contains(segment_key)){
				String path_to_service= "/AirShoppingRS/OffersGroup/AirlineOffers/ALaCarteOffer/ALaCarteOfferItem[@OfferItemID='"+offerItemID+"']/Service/ServiceDefinitionRef";
				String path_to_cost= "/AirShoppingRS/OffersGroup/AirlineOffers/ALaCarteOffer/ALaCarteOfferItem[@OfferItemID='"+offerItemID+"']/UnitPriceDetail/TotalAmount/SimpleCurrencyPrice";
				
				expr = xpath.compile(path_to_service);
				Node service_node = (Node) expr.evaluate(doc, XPathConstants.NODE);
				String service = service_node.getTextContent();
				
				expr = xpath.compile(path_to_cost);
				Node cost_node = (Node) expr.evaluate(doc, XPathConstants.NODE);
				String cost= cost_node.getTextContent();

				String path_to_service_definition = "/AirShoppingRS/DataLists/ServiceDefinitionList/ServiceDefinition[@ServiceDefinitionID='"
						+ service + "']/Name";
				expr = xpath.compile(path_to_service_definition);
				Node service_definition_node = (Node) expr.evaluate(doc, XPathConstants.NODE);
				String service_definition= service_definition_node.getTextContent();

				ServiceModel model = new ServiceModel();
				model.serv_id=service;
				model.serv_cost = Float.parseFloat(cost);
				model.serv_name = service_definition;
				String path_to_from = "/AirShoppingRS/DataLists/FlightSegmentList/FlightSegment[@SegmentKey='"
						+ segment_key + "']/Departure/AirportCode";
				expr = xpath.compile(path_to_from);
				Node from_node = (Node) expr.evaluate(doc, XPathConstants.NODE);
				model.from = from_node.getTextContent();
				service_model_list.add(model);
			}
		}
		return service_model_list;
	}

}
