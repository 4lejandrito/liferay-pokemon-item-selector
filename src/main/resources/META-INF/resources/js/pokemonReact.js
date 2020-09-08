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
						title: 'Select a pokemon',
						url: itemSelectorURL,
					})
				}
			>
				Select a pokemon
			</ClayButton>

			{selectedItem && (
				<ClayCard className="my-3" displayType="image">
					<div className="card-header h3">{selectedItem.name}</div>

					<ClayCard.AspectRatio>
						<img
							className="aspect-ratio-item aspect-ratio-item-center-middle aspect-ratio-item-fluid aspect-ratio-item-flush p-1 p-lg-3"
							src={selectedItem.image}
						/>
					</ClayCard.AspectRatio>
					<ClayCard.Body>
						<ClayCard.Description displayType="title">
							Base Stats
						</ClayCard.Description>

						<div className="card-divider" />

						<dl className="base-stats card-text">
							{selectedItem.stats.map(({max, name, value}) => (
								<Fragment key={name}>
									<dt>{name}</dt>
									<dd>
										<ClayProgressBar
											className="pokemon-progress"
											value={(value * 100) / max}
										>
											<div className="pokemon-progress-text">
												{value}/{max}
											</div>
										</ClayProgressBar>
									</dd>
								</Fragment>
							))}
						</dl>
					</ClayCard.Body>
				</ClayCard>
			)}
		</Fragment>
	);
};

export default Pokemon;
