<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Success</title>
</head>
<body>
	<h3>
		Welcome
		<%=session.getAttribute("username")%>
		!
	</h3>
	<form action="GetFlightInfo" method="get">
		From : <select name="from">
			<option value="ARN">Stockholm</option>
			<option value="BCN">Barcelona</option>
			<option value="CDG">Charles De Gaulles</option>
			<option value="DXB" selected="selected">Dubai</option>
			<option value="FRA">Frankfurt</option>
			<option value="LHR">London Heathrow</option>
			<option value="MUC">Munich</option>
			<option value="PRG">Prague</option>
			<option value="RIX">Riga</option>
			<option value="TXL">Berlin Tegel</option>
		</select> &nbsp;&nbsp; To : <select name="to">
			<option value="ARN">Stockholm</option>
			<option value="BCN">Barcelona</option>
			<option value="CDG">Charles De Gaulles</option>
			<option value="DXB">Dubai</option>
			<option value="FRA">Frankfurt</option>
			<option value="LHR" selected="selected">London Heathrow</option>
			<option value="MUC">Munich</option>
			<option value="PRG">Prague</option>
			<option value="RIX">Riga</option>
			<option value="TXL">Berlin Tegel</option>
		</select> &nbsp;&nbsp; Date : <input type="text" name="date" value="2017-08-26">
		<br />
		<br /> <input type="submit" value="Get Flights">
	</form>
	<form>
		<table>
			<tr>
				<th>Flight Info</th>
				<th>From</th>
				<th>To</th>
			</tr>
			<%
				Map<String, Map<String, String[]>> user_name = (Map<String, Map<String, String[]>>) session
						.getAttribute("flight_info");
				if (user_name != null) {
					for (String flight : user_name.keySet()) {
			%>
			<tr>
				<td><a href="ShowSegmentServices?flight=<%=flight%>"><%=flight%></a></td>
				<td><%=request.getParameter("from")%></a></td>
				<td><%=request.getParameter("to")%></td>
			</tr>
			<%
				Map<String, String[]> flight_segment = user_name.get(flight);
						for (String key : flight_segment.keySet()) {
							String[] segment_values = flight_segment.get(key);
							%>
							<tr>
							<td colspan="3">
							<b>Departure</b>
							</td>
							</tr>
							<tr>
							<th>From </th><th>Date</th><th>Time</th>
							</tr>
							<tr>
							<td><%=segment_values[0] %> </td><td><%=segment_values[1] %></td><td><%=segment_values[2] %></td>
							</tr>
							<tr>
							<td colspan="3">
							<b>Arrival</b>
							</td>
							</tr>
							<tr>
							<th>From </th><th>Date</th><th>Time</th>
							</tr>
							<tr>
							<td><%=segment_values[3] %> </td><td><%=segment_values[4] %></td><td><%=segment_values[5] %></td>
							</tr>
							<%
						}
					}
				}
			%>
		</table>
	</form>

</body>
</html>