<%@page import="controller.ServiceModel"%>
<%@page import="java.util.List"%>
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
<form method="get" action="ConfirmBooking">
<% 
Map<String, List<ServiceModel>> segment_services = (Map<String, List<ServiceModel>>)session.getAttribute("segment_services"); 
for(String segment : segment_services.keySet()){
	%>
	<table>
	<tr><td colspan="4"><b><%=segment %></b></td></tr>
	<tr><th>S:NO</th><th>Service ID</th><th>Service Name</th><th>Service Cost</th></tr>
	<% 
	List<ServiceModel> services = segment_services.get(segment);
	for(ServiceModel model : services){
		%>
		<tr><td><input type="checkbox" name="<%=segment %>-<%=model.from %>-<%=model.serv_id %>-<%=model.serv_cost %>" value="<%=segment %>-<%=model.from %>-<%=model.serv_id %>-<%=model.serv_cost %>"></td><td><%=model.serv_id %></td><td><%=model.serv_name %></td><td><%=model.serv_cost %></td></tr> <%
	}
	%>
	</table>
	<%
}
%>
<input type="submit" value="Confirm Booking">
</form>
</body>
</html>