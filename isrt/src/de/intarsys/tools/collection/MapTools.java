/*
 * Copyright (c) 2007, intarsys consulting GmbH
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * - Neither the name of intarsys nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific
 *   prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package de.intarsys.tools.collection;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.intarsys.tools.string.StringTools;

/**
 * A tool class for Map extensions.
 */
public class MapTools {
	//
	public static final String SEPARATOR = "=";

	/**
	 * Define new entries in <code>map</code> from all declaration strings in
	 * <code>declarations</code>. For every string in the collection,
	 * {@link #defineEntry(Map, String)} is called.
	 * 
	 * @param map
	 *            The map to receive the new declarations
	 * @param declarations
	 *            A collection of declaration strings.
	 * 
	 * @return The parameter <code>map</code>
	 */
	public static Map<Object, Object> defineEntries(Map<Object, Object> map,
			List<String> declarations) {
		if (declarations == null) {
			return map;
		}
		for (Iterator<String> i = declarations.iterator(); i.hasNext();) {
			defineEntry(map, i.next());
		}
		return map;
	}

	/**
	 * Define a new entry in the <code>map</code> from <code>definition</code>.
	 * <code>definition</code> contains a string in the form "key=value". A
	 * entry is defined in the map with "key" as the entries key and the trimmed
	 * "value" as its value. If no "=" is available, the value will be an empty
	 * string.
	 * 
	 * @param map
	 *            The map where we will put the key/value pair.
	 * @param declaration
	 *            The string representation of the key/value pair.
	 * 
	 * @return The parameter <code>map</code>
	 */
	public static Map defineEntry(Map map, String declaration) {
		if (declaration == null) {
			return map;
		}

		String key;
		String value;
		int pos = declaration.indexOf(SEPARATOR);
		if (pos > -1) {
			key = declaration.substring(0, pos).trim();
			value = declaration.substring(pos + 1);
		} else {
			key = declaration.trim();
			value = "";
		}
		map.put(key, value);
		return map;
	}

	public static Object get(Map map, Object key, Object defaultValue) {
		if (map == null) {
			return defaultValue;
		}
		Object value = map.get(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static String get(Map map, Object key, String defaultValue) {
		if (map == null) {
			return defaultValue;
		}
		Object value = map.get(key);
		if (value == null) {
			return defaultValue;
		}
		return value.toString();
	}

	static public void toMapDeep(Map<Object, Object> map, String prefix,
			Map<String, Object> result) {
		for (Map.Entry<Object, Object> entry : map.entrySet()) {
			String key = String.valueOf(entry.getKey());
			Object value = entry.getValue();
			if (value instanceof Map) {
				String nestedPrefix;
				if (StringTools.isEmpty(prefix)) {
					nestedPrefix = key;
				} else {
					nestedPrefix = prefix + "." + key;
				}
				toMapDeep((Map) value, nestedPrefix, result);
			} else {
				if (!StringTools.isEmpty(prefix)) {
					key = prefix + "." + key;
				} else {
					key = key;
				}
				result.put(key, value);
			}
		}
	}

	private MapTools() {
		super();
	}

}
