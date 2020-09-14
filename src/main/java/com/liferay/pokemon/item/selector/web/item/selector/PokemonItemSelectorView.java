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

package com.liferay.pokemon.item.selector.web.item.selector;

import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.ItemSelectorView;
import com.liferay.item.selector.ItemSelectorViewDescriptor;
import com.liferay.item.selector.ItemSelectorViewDescriptorRenderer;
import com.liferay.pokemon.item.selector.web.pokemon.Pokemon;
import com.liferay.pokemon.item.selector.web.pokemon.PokemonService;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONSerializer;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.util.JavaConstants;

import java.io.IOException;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 */
@Component(
	property = "item.selector.view.order:Integer=100",
	service = ItemSelectorView.class
)
public class PokemonItemSelectorView
	implements ItemSelectorView<PokemonItemSelectorCriterion> {

	@Override
	public Class<PokemonItemSelectorCriterion> getItemSelectorCriterionClass() {
		return PokemonItemSelectorCriterion.class;
	}

	@Override
	public List<ItemSelectorReturnType> getSupportedItemSelectorReturnTypes() {
		return _supportedItemSelectorReturnTypes;
	}

	@Override
	public String getTitle(Locale locale) {
		return "Pokémons";
	}

	@Override
	public void renderHTML(
			ServletRequest servletRequest, ServletResponse servletResponse,
			PokemonItemSelectorCriterion pokemonItemSelectorCriterion,
			PortletURL portletURL, String itemSelectedEventName, boolean search)
		throws IOException, ServletException {

		JSONSerializer jsonSerializer = JSONFactoryUtil.createJSONSerializer();

		_itemSelectorViewDescriptorRenderer.renderHTML(
			servletRequest, servletResponse, pokemonItemSelectorCriterion,
			portletURL, itemSelectedEventName, search,
			new ItemSelectorViewDescriptor<Pokemon>() {

				@Override
				public ItemDescriptor getItemDescriptor(Pokemon pokemon) {
					return new ItemDescriptor() {

						@Override
						public String getIcon() {
							return null;
						}

						@Override
						public String getImageURL() {
							return pokemon.getImageURL();
						}

						@Override
						public String getPayload() {
							return jsonSerializer.serializeDeep(pokemon);
						}

						@Override
						public String getSubtitle(Locale locale) {
							return pokemon.getType();
						}

						@Override
						public String getTitle(Locale locale) {
							return pokemon.getName();
						}

					};
				}

				@Override
				public ItemSelectorReturnType getItemSelectorReturnType() {
					return new PokemonItemSelectorReturnType();
				}

				@Override
				public SearchContainer<Pokemon> getSearchContainer()
					throws PortalException {

					SearchContainer<Pokemon> pokemonSearchContainer =
						new SearchContainer<>(
							(PortletRequest)servletRequest.getAttribute(
								JavaConstants.JAVAX_PORTLET_REQUEST),
							portletURL, null, null);

					pokemonSearchContainer.setTotal(
						_pokemonService.getPokemonCount());
					pokemonSearchContainer.setResults(
						_pokemonService.getPokemons(
							(HttpServletRequest)servletRequest,
							pokemonSearchContainer.getStart(),
							pokemonSearchContainer.getEnd()));

					return pokemonSearchContainer;
				}

				@Override
				public boolean isShowBreadcrumb() {
					return false;
				}

				@Override
				public boolean isShowManagementToolbar() {
					return false;
				}

			});
	}

	private static final List<ItemSelectorReturnType>
		_supportedItemSelectorReturnTypes = Collections.singletonList(
			new PokemonItemSelectorReturnType());

	@Reference
	private ItemSelectorViewDescriptorRenderer<PokemonItemSelectorCriterion>
		_itemSelectorViewDescriptorRenderer;

	@Reference
	private Language _language;

	@Reference
	private PokemonService _pokemonService;

}