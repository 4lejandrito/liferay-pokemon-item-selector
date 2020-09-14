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

package com.liferay.pokemon.item.selector.web.pokemon;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.Portal;

import java.io.IOException;

import java.net.HttpURLConnection;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 */
@Component(service = PokemonService.class)
public class PokemonService {

	public int getPokemonCount() {
		return 150;
	}

	public List<Pokemon> getPokemons(
			HttpServletRequest httpServletRequest, int start, int end)
		throws PortalException {

		try {
			JSONObject jsonObject = _fetchJSONObject(
				String.format(
					"https://pokeapi.co/api/v2/pokemon?offset=%s&limit=%s",
					start, Math.min(end, getPokemonCount()) - start));

			JSONArray resultsJSONArray = jsonObject.getJSONArray("results");

			List<Pokemon> pokemons = new ArrayList<>();

			for (int i = 0; i < resultsJSONArray.length(); i++) {
				JSONObject pokemonInitialJSONObject =
					resultsJSONArray.getJSONObject(i);

				JSONObject pokemonJSONObject = _fetchJSONObject(
					pokemonInitialJSONObject.getString("url"));

				pokemons.add(
					new Pokemon() {

						@Override
						public String getCryURL() {
							return _portal.getStaticResourceURL(
								httpServletRequest,
								String.format(
									"%s/cries/%d.ogg",
									_servletContext.getContextPath(),
									pokemonJSONObject.getInt("id")));
						}

						@Override
						public String getImageURL() {
							return String.format(
								"https://pokeres.bastionbot.org/images" +
									"/pokemon/%d.png",
								pokemonJSONObject.getInt("id"));
						}

						@Override
						public String getName() {
							return pokemonJSONObject.getString("name");
						}

						@Override
						public List<Stat> getStats() {
							JSONArray statsJSONArray =
								pokemonJSONObject.getJSONArray("stats");
							List<Stat> stats = new ArrayList<>();

							for (int j = 0; j < statsJSONArray.length(); j++) {
								JSONObject statJSONObject =
									statsJSONArray.getJSONObject(j);

								stats.add(
									new Stat(
										statJSONObject.getJSONObject(
											"stat"
										).getString(
											"name"
										),
										statJSONObject.getInt("base_stat"),
										255));
							}

							stats.add(
								new Stat(
									"experience",
									pokemonJSONObject.getInt("base_experience"),
									1000));

							return stats;
						}

						@Override
						public String getType() {
							return pokemonJSONObject.getJSONArray(
								"types"
							).getJSONObject(
								0
							).getJSONObject(
								"type"
							).getString(
								"name"
							);
						}

					});
			}

			return pokemons;
		}
		catch (IOException ioException) {
			throw new PortalException(ioException);
		}
	}

	private JSONObject _fetchJSONObject(String url)
		throws IOException, PortalException {

		Http.Options options = new Http.Options();

		options.setLocation(url);

		String responseJSON = _http.URLtoString(options);

		Http.Response response = options.getResponse();

		if (response.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new PortalException(
				StringBundler.concat(
					"Response code ", response.getResponseCode(), ": ",
					responseJSON));
		}

		return JSONFactoryUtil.createJSONObject(responseJSON);
	}

	@Reference
	private Http _http;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.pokemon.item.selector.web)",
		unbind = "-"
	)
	private ServletContext _servletContext;

}