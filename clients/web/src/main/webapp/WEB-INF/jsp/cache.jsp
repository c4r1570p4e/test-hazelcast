<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Etat des caches</title>
</head>
<body>



	<%
		List<String> cacheNames = (List<String>) request.getAttribute("cacheNames");
		String cacheName = (String) request.getAttribute("cacheName");
	%>

	<form method="post" action="/web/mvc/cache">
		<table border="0">
			<tr>
				<td>Nom du cache :</td>
				<td><select name="cacheName">
						<%
							for (String name : cacheNames) {
								String selected = "";
								if (name.equalsIgnoreCase(cacheName)) {
									selected = "SELECTED";
								}
						%>
						<option value="<%=name%>" <%=selected%>><%=name%></option>
						<%
							}
						%>
				</select></td>
				<td><input type="submit" /></td>
			</tr>
		</table>
	</form>

	<br />

	<%
		Map<String, String> map = null;
		if (cacheName != null) {
			map = (Map<String, String>) request.getAttribute("cache");
		}

		if (map != null) {
	%>

	<%
		for (Map.Entry<String, String> entry : map.entrySet()) {
	%>
	<form method="post" action="/web/mvc/cache">
		<input name="cacheName" type="hidden" value="<%=cacheName%>" />
		<table border="0">
			<tr>
				<td>ID :</td>
				<td><input name="id" type="text" value="<%=entry.getKey()%>" /></td>

				<td>VAL :</td>
				<td><input name="val" type="text"
					value="<%=entry.getValue()%>" /></td>

				<td><input type="submit" value="Modifier" /></td>
			</tr>
		</table>
	</form>

	<%
		}
	%>


	<form method="post" action="/web/mvc/cache">
		<input name="cacheName" type="hidden" value="<%=cacheName%>" />
		<table border="0">
			<tr>
				<td>ID :</td>
				<td><input name="id" type="text" value="" /></td>

				<td>VAL :</td>
				<td><input name="val" type="text" value="" /></td>

				<td><input type="submit" value="Ajouter" /></td>
			</tr>
		</table>
	</form>



	<%
		}
	%>



</body>
</html>