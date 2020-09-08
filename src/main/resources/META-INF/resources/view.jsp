<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/init.jsp" %>

<%
String itemSelectorURL = (String)request.getAttribute("itemSelectorURL");
String eventName = (String)request.getAttribute("eventName");
%>

<clay:container-fluid cssClass="text-center">
	<clay:row containerElement="header">
		<clay:col containerElement="h2">
			<img
				alt="Poke Liferay Logo"
				class="mw-100"
				src="<%= PortalUtil.getStaticResourceURL(request, application.getContextPath() + "/images/logo.png") %>"
			/>
		</clay:col>
	</clay:row>

	<clay:row>
		<clay:col cssClass="mx-md-auto" md="6" xl="4">

			<%
			Map<String, Object> props = HashMapBuilder.<String, Object>put(
				"eventName", eventName
			).put(
				"itemSelectorURL", itemSelectorURL
			).build();
			%>

			<div>
				<react:component
					module="js/pokemonReact"
					props="<%= props %>"
				/>
			</div>
		</clay:col>
	</clay:row>
</clay:container-fluid>