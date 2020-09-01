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

import ClayButton from '@clayui/button';
import ClayCard from '@clayui/card';
import ClayProgressBar from '@clayui/progress-bar';
import {ItemSelectorDialog} from 'frontend-js-web';
import React, {Fragment, useState} from 'react';

const Pokemon = ({eventName, itemSelectorURL}) => {
	const [selectedItem, setSelectedItem] = useState();

	const itemSelectorDialog = new ItemSelectorDialog({
		eventName,
		singleSelect: true,
		title: 'Select a pokemon',
		url: itemSelectorURL,
	});

	itemSelectorDialog.on('selectedItemChange', (event) => {
		const selectedItem = event.selectedItem;

		if (selectedItem) {
			const itemValue = JSON.parse(selectedItem.value);
			setSelectedItem(itemValue);
		}
		else {
			setSelectedItem(null);
		}
	});

	return (
		<Fragment>
			<ClayButton
				onClick={() => {
					itemSelectorDialog.open();
				}}
			>
				Select a pokemon
			</ClayButton>

			{selectedItem && (
				<div className="preview">
					<h1>{selectedItem.name}</h1>
					<img src={selectedItem.image} />
				</div>
			)}
		</Fragment>
	);
};

export default Pokemon;
