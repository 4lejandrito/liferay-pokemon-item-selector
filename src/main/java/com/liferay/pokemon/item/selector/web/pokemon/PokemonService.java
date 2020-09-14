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
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Validator;

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

	public int getPokemonCount(String keywords) throws PortalException {
		List<Pokemon> pokemons = getPokemons(keywords, 0, _POKEMON_AMOUNT);

		return pokemons.size();
	}

	public List<Pokemon> getPokemons(String keywords, int start, int end)
		throws PortalException {

		return ListUtil.subList(
			ListUtil.filter(
				_getPokemons(),
				pokemon ->
					Validator.isNull(keywords) ||
					pokemon.getName(
					).toLowerCase(
					).contains(
						keywords.toLowerCase()
					)),
			start, end);
	}

	private JSONObject _fetchJSONObject(String url) throws PortalException {
		Http.Options options = new Http.Options();

		options.setLocation(url);

		try {
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
		catch (IOException ioException) {
			throw new PortalException(ioException);
		}
	}

	private List<Pokemon> _getPokemons() throws PortalException {
		if (!_pokemons.isEmpty()) {
			return _pokemons;
		}

		JSONObject jsonObject = _fetchJSONObject(
			String.format(
				"https://pokeapi.co/api/v2/pokemon?limit=%s", _POKEMON_AMOUNT));

		JSONArray resultsJSONArray = jsonObject.getJSONArray("results");

		for (int i = 0; i < resultsJSONArray.length(); i++) {
			JSONObject pokemonInitialJSONObject =
				resultsJSONArray.getJSONObject(i);

			JSONObject pokemonJSONObject = _fetchJSONObject(
				pokemonInitialJSONObject.getString("url"));

			_pokemons.add(
				new Pokemon() {

					@Override
					public String getImageURL() {
						return String.format(
							"https://pokeres.bastionbot.org/images/pokemon" +
								"/%d.png",
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
									statJSONObject.getInt("base_stat"), 255));
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

		return _pokemons;
	}

	private static final int _POKEMON_AMOUNT = 150;

	@Reference
	private Http _http;

	private List<Pokemon> _pokemons = new ArrayList<>();

}