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

package com.liferay.pokemon.item.selector.web.internal.portlet;

import com.liferay.item.selector.ItemSelector;
import com.liferay.pokemon.item.selector.web.internal.constants.PokemonPortletKeys;
import com.liferay.pokemon.item.selector.web.internal.item.selector.PokemonItemSelectorCriterion;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.Portal;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.application-type=widget",
		"com.liferay.portlet.css-class-wrapper=portlet-pokemon",
		"com.liferay.portlet.display-category=category.fun",
		"com.liferay.portlet.header-portlet-css=/main.css",
		"com.liferay.portlet.icon=/pokeball.png",
		"com.liferay.portlet.layout-cacheable=true",
		"com.liferay.portlet.use-default-template=true",
		"javax.portlet.display-name=Pokémon",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.always-display-default-configuration-icons=true",
		"javax.portlet.init-param.always-send-redirect=true",
		"javax.portlet.init-param.template-path=/META-INF/resources/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + PokemonPortletKeys.POKEMON,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=guest,power-user,user"
	},
	service = Portlet.class
)
public class PokemonPortlet extends MVCPortlet {

	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		String eventName =
			_portal.getPortletNamespace(_portal.getPortletId(renderRequest)) +
				"selectPokemon";

		PortletURL itemSelectorURL = _itemSelector.getItemSelectorURL(
			RequestBackedPortletURLFactoryUtil.create(renderRequest), eventName,
			new PokemonItemSelectorCriterion());

		renderRequest.setAttribute(
			"itemSelectorURL", itemSelectorURL.toString());

		renderRequest.setAttribute("eventName", eventName);

		super.render(renderRequest, renderResponse);
	}

	@Reference
	private ItemSelector _itemSelector;

	@Reference
	private Portal _portal;

}