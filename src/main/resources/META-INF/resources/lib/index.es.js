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
import {openSelectionModal} from 'frontend-js-web';
import React, {Fragment, useState} from 'react';
import ReactDOM from 'react-dom';

const Pokemon = ({eventName, itemSelectorURL}) => {
	const [selectedItem, setSelectedItem] = useState();

	return (
		<Fragment>
			<ClayButton
				onClick={() =>
					openSelectionModal({
						onSelect: ({value}) =>
							setSelectedItem(JSON.parse(value)),
						selectEventName: eventName,
						title: Liferay.Language.get('select-a-pokemon'),
						url: itemSelectorURL,
					})
				}
			>
				{Liferay.Language.get('select-a-pokemon')}
			</ClayButton>

			{selectedItem && (
				<ClayCard className="my-4" displayType="image">
					<ClayCard.AspectRatio>
						<img
							className="aspect-ratio-item aspect-ratio-item-center-middle aspect-ratio-item-fluid aspect-ratio-item-flush p-1 p-lg-3"
							src={selectedItem.imageURL}
						/>
					</ClayCard.AspectRatio>
					<ClayCard.Body>
						<ClayCard.Description
							className="text-uppercase"
							displayType="title"
						>
							{selectedItem.name}
						</ClayCard.Description>

						<div className="card-divider" />

						<div className="card-text">
							{selectedItem.stats.map(
								({abbr, max, name, value}) => (
									<div
										className="d-flex flex-wrap"
										key={name}
									>
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
								)
							)}
						</div>
					</ClayCard.Body>
				</ClayCard>
			)}
		</Fragment>
	);
};

export default function (elementId, eventName, itemSelectorURL) {
	ReactDOM.render(
		<Pokemon eventName={eventName} itemSelectorURL={itemSelectorURL} />,
		document.getElementById(elementId)
	);
}
