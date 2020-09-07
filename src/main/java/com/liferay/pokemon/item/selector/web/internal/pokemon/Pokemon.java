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

import java.util.List;

/**
 * @author Alejandro Tardín
 */
public interface Pokemon {

	public String getImageURL();

	public String getName();

	public List<Stat> getStats();

	public String getType();

	public static class Stat {

		public Stat(String name, int value, int max) {
			_name = name;
			_value = value;
			_max = max;
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

		private int _max;
		private String _name;
		private int _value;

	}

}