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

<clay:button
	id="selectPokemonBtn"
	label="Select Pokemon"
/>

<h1 id="pokemonTitle"></h1>

<aui:script require="frontend-js-web/liferay/ItemSelectorDialog.es as ItemSelectorDialog">
	var selectPokemonBtn = document.getElementById(
		'selectPokemonBtn'
	);

	var pokemonTitle = document.getElementById(
		'pokemonTitle'
	);

	selectPokemonBtn.addEventListener('click', function (event) {
		var itemSelectorDialog = new ItemSelectorDialog.default({
			eventName: '<%= eventName %>',
			title: 'Select a pokemon',
			singleSelect: true,
			url:'<%= itemSelectorURL %>',
		});

		itemSelectorDialog.open();

		itemSelectorDialog.on('selectedItemChange', function (event) {
			if (event.selectedItem) {//always check this, will be null is user cancel or close the dialog
				pokemonTitle.innerText = event.selectedItem.value;
			}
		});
	});
</aui:script>