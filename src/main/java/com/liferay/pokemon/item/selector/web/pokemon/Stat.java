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

import com.liferay.portal.kernel.util.HashMapBuilder;

import java.util.Map;

/**
 * @author Alejandro Tardín
 */
public class Stat {

	public Stat(String name, int value, int max) {
		_name = name;
		_value = value;
		_max = max;
	}

	public String getAbbr() {
		return _abbrs.getOrDefault(_name, _name);
	}

	public int getMax() {
		return _max;
	}

	public String getName() {
		return _name;
	}

	public int getValue() {
		return _value;
	}

	private static Map<String, String> _abbrs = HashMapBuilder.put(
		"attack", "ATK"
	).put(
		"defense", "DEF"
	).put(
		"experience", "EXP"
	).put(
		"special-attack", "SP ATK"
	).put(
		"special-defense", "SP DEF"
	).put(
		"speed", "SPD"
	).build();

	private int _max;
	private String _name;
	private int _value;

}