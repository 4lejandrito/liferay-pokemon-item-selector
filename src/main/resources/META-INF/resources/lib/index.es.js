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
import ReactDOM from 'react-dom';

const Pokemon = ({pokemon}) => {
	return (
		<ClayCard
			className="flex-shrink-0 mw-100 mx-3 my-3 pokemon-card"
			displayType="image"
		>
			<ClayCard.AspectRatio>
				<img
					className="aspect-ratio-item aspect-ratio-item-center-middle aspect-ratio-item-fluid aspect-ratio-item-flush p-1 p-lg-3"
					src={pokemon.imageURL}
				/>
			</ClayCard.AspectRatio>
			<ClayCard.Body>
				<ClayCard.Description
					className="text-uppercase"
					displayType="title"
				>
					{pokemon.name}
				</ClayCard.Description>

				<div className="card-divider" />

				<div className="card-text">
					{pokemon.stats.map(({abbr, max, name, value}) => (
						<div className="d-flex flex-wrap" key={name}>
							<small
								className="font-weight-bold pokemon-stat text-left text-uppercase"
								title={name}
							>
								{abbr}
							</small>
							<ClayProgressBar
								className="flex-grow-1 flex-wrap"
								value={(value * 100) / max}
							>
								<div className="pokemon-progress-text">
									{value}/{max}
								</div>
							</ClayProgressBar>
						</div>
					))}
				</div>
			</ClayCard.Body>
		</ClayCard>
	);
};
const PokemonSelector = ({eventName, itemSelectorURL}) => {
	const [selectedItems, setSelectedItems] = useState([]);
	const [multiple, setMultiple] = useState(false);

	const itemSelectorDialog = new ItemSelectorDialog({
		eventName,
		singleSelect: !multiple,
		title: Liferay.Language.get('select-a-pokemon'),
		url: itemSelectorURL
	});

	itemSelectorDialog.on('selectedItemChange', (event) => {
		if (event.selectedItem) {
			const {value} = event.selectedItem;

			setSelectedItems(
				multiple ? value.map((item) => JSON.parse(item))
						 : [JSON.parse(value)]);
		}
	});

	return (
		<Fragment>
			<ClayButton
				onClick={() => itemSelectorDialog.open()}
			>
				{Liferay.Language.get('select-a-pokemon')}
			</ClayButton>

			<div className="align-items-center d-flex flex-wrap justify-content-center mt-2">
				{selectedItems.map((selectedItem, i) => (
					<Pokemon key={i} pokemon={selectedItem} />
				))}
			</div>
		</Fragment>
	);
};

export default function (elementId, eventName, itemSelectorURL) {
	ReactDOM.render(
		<PokemonSelector
			eventName={eventName}
			itemSelectorURL={itemSelectorURL}
		/>,
		document.getElementById(elementId)
	);
}
