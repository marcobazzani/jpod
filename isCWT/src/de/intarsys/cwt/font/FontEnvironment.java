/*
 * Copyright (c) 2008, intarsys consulting GmbH
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
package de.intarsys.cwt.font;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sun.font.FontManager;
import de.intarsys.cwt.freetype.Face;
import de.intarsys.cwt.freetype.Freetype;
import de.intarsys.cwt.freetype.Library;
import de.intarsys.tools.installresource.InstallFileList;
import de.intarsys.tools.locator.FileLocator;
import de.intarsys.tools.stream.StreamTools;

/**
 * 
 */
public class FontEnvironment {

	private static final Logger Log = PACKAGE.Log;

	private static FontEnvironment Unique = new FontEnvironment();

	static public FontEnvironment get() {
		return Unique;
	}

	static public void set(FontEnvironment environment) {
		Unique = environment;
	}

	private List<ClassLoader> fontClassLoaders = new ArrayList<ClassLoader>();

	private List<File> fontDirectories = new ArrayList<File>();

	private List<File> fontFiles = new ArrayList<File>();

	private boolean registeredSystem = false;

	private boolean registeredUser = false;

	public FontEnvironment() {
		registerFontClassLoader(getClass().getClassLoader());
	}

	synchronized public ClassLoader[] getFontClassLoaders() {
		return fontClassLoaders
				.toArray(new ClassLoader[fontClassLoaders.size()]);
	}

	synchronized public File[] getFontDirectories() {
		return fontDirectories.toArray(new File[fontDirectories.size()]);
	}

	synchronized public File[] getFontFiles() {
		return fontFiles.toArray(new File[fontFiles.size()]);
	}

	/**
	 * This method determines the system's font directories.
	 * 
	 * @return an array containing the font directory paths found on the local
	 *         system
	 */
	synchronized public File[] getSystemFontDirectories() {
		String definition = null;
		// force FontManager initialization
		Font.decode("dummy").getFamily(); //$NON-NLS-1$
		definition = FontManager.getFontPath(true);
		if (definition == null) {
			return new File[0];
		}
		String[] names = definition.split(System.getProperty("path.separator")); //$NON-NLS-1$
		File[] files = new File[names.length];
		for (int i = 0; i < names.length; i++) {
			files[i] = new File(names[i]);
		}
		return files;
	}

	protected void loadFontClassLoader(Library library, ClassLoader loader) {
		try {
			InstallFileList installer = new InstallFileList("fonts",
					"fonts.list", false);
			installer.loadAll();
			File[] roots = installer.getFiles();
			for (int i = 0; i < roots.length; i++) {
				File root = roots[i];
				File[] fontFiles = root.listFiles();
				for (int j = 0; j < fontFiles.length; j++) {
					File file = fontFiles[j];
					try {
						processFontFile(library, file);
					} catch (IOException e) {
						Log.log(Level.WARNING,
								"error loading font '" + file.getName() + "'",
								e);
					} finally {
					}
				}
			}
		} catch (IOException e) {
			Log.log(Level.WARNING,
					"error looking up 'font/fonts.list' resources", e);
		}
	}

	protected void loadFontDirectory(Library library, File directory) {
		if (!directory.exists()) {
			return;
		}
		if (!directory.isDirectory()) {
			return;
		}
		File[] files = directory.listFiles();
		if (files == null) {
			return;
		}
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			loadFontFile(library, file);
		}
	}

	protected void loadFontFile(Library library, File file) {
		if (!file.isFile()) {
			return;
		}
		String filepath = file.getAbsolutePath();
		try {
			/*
			 * the absolute path/canonical path check is used to find out if the
			 * file path contains a symbolic link. if so we can ignore the file
			 * because it will also show up in another directory as a "real"
			 * file.
			 */
			if (!filepath.equals(file.getCanonicalPath())) {
				return;
			}
		} catch (IOException ex) {
			return;
		}
		loadFontUnchecked(library, filepath);
	}

	protected void loadFontMapClassLoader(Library library, ClassLoader loader) {
		try {
			Enumeration<URL> urls = loader.getResources("fonts/fonts.maps");
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				InputStream is = null;
				try {
					is = url.openStream();
					parseMaps(library, loader, is);
				} catch (IOException e) {
					Log.log(Level.WARNING,
							"error loading 'fonts/fonts.maps' from '" + url
									+ "'", e);
				} finally {
					StreamTools.close(is);
				}
			}
		} catch (IOException e) {
			Log.log(Level.WARNING,
					"error looking up 'font/fonts.maps' resources", e);
		}
	}

	protected IFont loadFontUnchecked(Library library, String filepath) {
		FileLocator locator = new FileLocator(filepath);
		Face face = library.newFace(filepath, 0);
		if (face != null) {
			IFont font = null;
			try {
				font = GenericFont.createNew(locator, face);
				FontRegistry.get().registerFont(font);
			} finally {
				face.doneFace();
			}
			Log.log(Level.FINE, "loaded font for " + filepath);
			return font;
		} else {
			Log.log(Level.FINE, "can't load font for " + filepath);
			return null;
		}
	}

	protected void loadSystemFonts(Library library) {
		File[] files;
		files = getSystemFontDirectories();
		for (int i = 0; i < files.length; i++) {
			File directory = files[i];
			Log.log(Level.FINE,
					"load system font directory " + directory.getAbsolutePath());
			loadFontDirectory(library, directory);
		}
	}

	protected void loadUserFonts(Library library) {
		File[] files;
		files = getFontDirectories();
		for (int i = 0; i < files.length; i++) {
			File directory = files[i];
			Log.log(Level.FINE,
					"load user font directory " + directory.getAbsolutePath());
			loadFontDirectory(library, directory);
		}
		files = getFontFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			loadFontFile(library, file);
		}
		ClassLoader[] loaders = getFontClassLoaders();
		for (int i = 0; i < loaders.length; i++) {
			ClassLoader loader = loaders[i];
			loadFontClassLoader(library, loader);
			loadFontMapClassLoader(library, loader);
		}
	}

	protected void parseMaps(Library library, ClassLoader loader, InputStream is)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		int i = is.read();
		while (i != -1) {
			if (i == '\n') {
				String map = sb.toString().trim();
				processFontMap(library, loader, map);
				sb.setLength(0);
				i = is.read();
				continue;
			}
			sb.append((char) i);
			i = is.read();
		}
		String map = sb.toString().trim();
		processFontMap(library, loader, map);
	}

	protected void processFontFile(Library library, File file)
			throws IOException {
		IFont font = loadFontUnchecked(library, file.getAbsolutePath());
		if (font == null) {
			return;
		}
		FontTools.mapFont(font.getFontName(), font);
	}

	protected void processFontMap(Library library, ClassLoader loader,
			String map) throws IOException {
		if (map.length() == 0 || map.startsWith("#")) {
			return;
		}
		String[] split = map.split("\\=");
		if (split.length < 2) {
			return;
		}
		FontTools.mapAlias(split[0], split[1]);
	}

	public synchronized void registerFontClassLoader(ClassLoader loader) {
		fontClassLoaders.add(loader);
	}

	public synchronized void registerFontDirectory(File directory) {
		fontDirectories.add(directory);
	}

	synchronized public void registerFontFile(File file) {
		fontFiles.add(file);
	}

	synchronized public boolean registerSystemFonts() {
		if (registeredSystem) {
			return false;
		}
		registeredSystem = true;
		try {
			Library library = Freetype.initFreeType();
			try {
				loadSystemFonts(library);
			} finally {
				library.doneFreeType();
			}
			return true;
		} catch (Throwable e) {
			// we may fail to load natives for font lookup
			Log.log(Level.WARNING, "failed to register system fonts", e);
			return false;
		}
	}

	synchronized public boolean registerUserFonts() {
		if (registeredUser) {
			return false;
		}
		registeredUser = true;
		try {
			Library library = Freetype.initFreeType();
			try {
				loadUserFonts(library);
			} finally {
				library.doneFreeType();
			}
			return true;
		} catch (Throwable e) {
			// we may fail to load natives for font lookup
			Log.log(Level.WARNING, "failed to register user fonts", e);
			return false;
		}
	}

}
