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

<header class="mb-3">
	<img
		alt="Poke Liferay Logo"
		class="mw-100"
		src="<%= PortalUtil.getStaticResourceURL(request, application.getContextPath() + "/images/logo.png") %>"
	/>
</header>

<div id="<portlet:namespace />-root">
	<clay:button
		label="select-a-pokemon"
	/>
</div>

<aui:script require="<%= mainRequire %>">
	main.default(
		'<portlet:namespace />-root',
		'<%= (String)request.getAttribute("eventName") %>',
		'<%= (String)request.getAttribute("itemSelectorURL") %>'
	);
</aui:script>