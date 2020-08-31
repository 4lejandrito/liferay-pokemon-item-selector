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

package com.liferay.pokemon.item.selector.web.internal.pokemon;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.Http;

import java.io.IOException;

import java.net.HttpURLConnection;

import java.util.ArrayList;
import java.util.List;

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

	public List<Pokemon> getPokemons(int start, int end)
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
						public JSONObject getAll() {
							return pokemonJSONObject;
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

}