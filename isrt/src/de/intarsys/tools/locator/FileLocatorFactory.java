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
package de.intarsys.tools.locator;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import de.intarsys.tools.file.FileTools;

/**
 * The factory for {@link FileLocator} objects.
 * 
 * <p>
 * {@link FileLocator} instances are created either using an absolute path name
 * or are looked up relative to the factorys search path. Multiple search paths
 * may be defined.
 * </p>
 */
public class FileLocatorFactory extends CommonLocatorFactory {

	/** The separator character for the definition of multiple search paths */
	public static final String PATH_SEPARATOR = ";";

	/** The root path where we look up relative references */
	private String searchPathDefinition;

	/** The collection of search paths to be looked up when creating a locator */
	private List searchPaths;

	/** flag if we synchronize synchronously with every check */
	private boolean synchSynchronous = true;

	/**
	 * Create a new factory.
	 */
	public FileLocatorFactory() {
		super();
		setSearchPathDefinition("./");
	}

	/**
	 * The file locator factory supports looking up resources in multiple paths.
	 * To preserve compatibility to ILocatorFactory, the last locator created is
	 * returned if no match is found. This is a valid locator, even so no
	 * existing physical resource is designated.
	 * 
	 * @see de.intarsys.tools.locator.ILocatorFactory#createLocator(java.lang.String)
	 */
	public ILocator createLocator(String path) {
		FileLocator result = null;
		for (Iterator it = getSearchPaths().iterator(); it.hasNext();) {
			String searchPath = (String) it.next();
			File parent = new File(searchPath.trim());
			File absolutePath = FileTools.resolvePath(parent, path.trim());
			result = new FileLocator(absolutePath);
			// avoid exists check if possible
			if (!it.hasNext() || result.exists()) {
				break;
			}
		}
		if (result != null) {
			result.setSynchSynchronous(isSynchSynchronous());
		}
		return result;
	}

	public String getSearchPathDefinition() {
		return searchPathDefinition;
	}

	public List getSearchPaths() {
		return searchPaths;
	}

	public boolean isSynchSynchronous() {
		return synchSynchronous;
	}

	public void setSearchPathDefinition(String searchPath) {
		this.searchPathDefinition = searchPath;
		// set up search paths
		searchPaths = new ArrayList();
		for (Enumeration e = new StringTokenizer(searchPathDefinition,
				PATH_SEPARATOR); e.hasMoreElements();) {
			String path = (String) e.nextElement();
			if ((path != null) && (path.trim().length() > 0)
					&& !searchPaths.contains(path)) {
				searchPaths.add(path.trim());
			}
		}
	}

	public void setSearchPaths(List searchPaths) {
		this.searchPaths = searchPaths;
	}

	public void setSynchSynchronous(boolean synchSynchronous) {
		this.synchSynchronous = synchSynchronous;
	}
}
