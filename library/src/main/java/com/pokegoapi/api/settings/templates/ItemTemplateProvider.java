/*
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.`
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pokegoapi.api.settings.templates;

import POGOProtos.Networking.Responses.DownloadItemTemplatesResponseOuterClass.DownloadItemTemplatesResponse;
import POGOProtos.Networking.Responses.DownloadItemTemplatesResponseOuterClass.DownloadItemTemplatesResponse.ItemTemplate;

import java.io.IOException;
import java.util.Map;

public interface ItemTemplateProvider {
	/**
	 * @return the last timestamp at which these templates were updated
	 */
	long getUpdatedTimestamp();

	/**
	 * @return the current templates
	 */
	Map<String, ItemTemplate> getTemplates();

	/**
	 * Update the current templates with the given response
	 *
	 * @param templates new templates to merge
	 * @param time the current timestamp
	 * @throws IOException if the templates could not be updated
	 */
	void updateTemplates(DownloadItemTemplatesResponse templates, long time) throws IOException;
}
