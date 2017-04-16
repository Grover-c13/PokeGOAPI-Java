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
import java.util.HashMap;
import java.util.Map;

/**
 * {@link ItemTemplateProvider} that doesn't store any templates, it instead downloads them every launch
 */
public class DirectTemplateProvider implements ItemTemplateProvider {
	private Map<String, ItemTemplate> templates = new HashMap<>();

	@Override
	public long getUpdatedTimestamp() {
		return 0;
	}

	@Override
	public Map<String, ItemTemplate> getTemplates() {
		return templates;
	}

	@Override
	public void updateTemplates(DownloadItemTemplatesResponse response, long time) throws IOException {
		for (ItemTemplate template : response.getItemTemplatesList()) {
			templates.put(template.getTemplateId(), template);
		}
	}
}
