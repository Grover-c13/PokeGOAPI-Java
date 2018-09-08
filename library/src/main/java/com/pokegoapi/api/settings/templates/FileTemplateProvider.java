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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link ItemTemplateProvider} that stores templates in a static directory
 */
public class FileTemplateProvider implements ItemTemplateProvider {
	private File directory;
	private long timestamp;
	private Map<String, ItemTemplate> templates = new HashMap<>();

	/**
	 * Creates a {@link FileTemplateProvider}
	 *
	 * @param directory the directory to base this provider in
	 * @throws IOException if the templates could not be loaded
	 */
	public FileTemplateProvider(File directory) throws IOException {
		this.directory = directory;
		if (directory.isFile()) {
			throw new IllegalArgumentException("Cannot save templates in file!");
		}
		load();
	}

	@Override
	public long getUpdatedTimestamp() {
		return timestamp;
	}

	@Override
	public Map<String, ItemTemplate> getTemplates() {
		return templates;
	}

	@Override
	public void updateTemplates(DownloadItemTemplatesResponse response, long time) throws IOException {
		timestamp = time;
		for (ItemTemplate template : response.getItemTemplatesList()) {
			templates.put(template.getTemplateId(), template);
		}
		save();
	}

	private void load() throws IOException {
		File timestampFile = getTimestampFile();
		File templatesFile = getTemplatesFile();
		if (timestampFile.exists() && templatesFile.exists()) {
			try (DataInputStream in = new DataInputStream(new FileInputStream(timestampFile))) {
				timestamp = in.readLong();
			}
			try (DataInputStream in = new DataInputStream(new FileInputStream(templatesFile))) {
				while (in.available() > 0) {
					int length = in.readUnsignedShort();
					byte[] templateBytes = new byte[length];
					for (int i = 0; i < length; i++) {
						templateBytes[i] = in.readByte();
					}
					ItemTemplate template = ItemTemplate.parseFrom(templateBytes);
					templates.put(template.getTemplateId(), template);
				}
			}
		}
	}

	private void save() throws IOException {
		try (DataOutputStream out = new DataOutputStream(new FileOutputStream(getTimestampFile()))) {
			out.writeLong(timestamp);
		}
		try (DataOutputStream out = new DataOutputStream(new FileOutputStream(getTemplatesFile()))) {
			for (ItemTemplate template : templates.values()) {
				byte[] templateBytes = template.toByteArray();
				out.writeShort(templateBytes.length);
				out.write(templateBytes);
			}
		}
	}

	private File getTimestampFile() {
		return new File(directory, "item_template_timestamp");
	}

	private File getTemplatesFile() {
		return new File(directory, "item_templates");
	}
}
