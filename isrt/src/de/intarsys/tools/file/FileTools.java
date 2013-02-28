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
package de.intarsys.tools.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.intarsys.tools.logging.LogTools;
import de.intarsys.tools.stream.StreamTools;
import de.intarsys.tools.string.StringTools;

/**
 * Some utility methods to ease life with {@link File} instances.
 */
public class FileTools {

	public static class Lock {
		protected File file;
		protected File lockFile;
		protected FileOutputStream lockStream;
		protected boolean valid = true;

		synchronized public boolean isValid() {
			return valid;
		}

		synchronized public void release() {
			unlock(this);
		}
	}

	public static final String DIRECTORY_LOCK = "directory.lock"; //$NON-NLS-1$

	private final static Logger Log = LogTools.getLogger(FileTools.class);

	static private Map<File, Lock> maps = new HashMap<File, Lock>();

	/**
	 * Concatenate the two files given in <code>source</code> and
	 * <code>destination</code>.
	 * 
	 * @param source
	 *            The file to be appended.
	 * @param destination
	 *            The file to append to.
	 * 
	 * @throws IOException
	 */
	public static void appendFile(File source, File destination)
			throws IOException {
		FileInputStream is = null;
		FileOutputStream os = null;
		if (equalsOnSystem(source, destination)) {
			return;
		}
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(destination, true);
			StreamTools.copyStream(is, false, os, false);
		} catch (Exception e) {
			throw new IOException("copying failed (" + e.getMessage() + ")");
		} finally {
			StreamTools.close(is);
			StreamTools.close(os);
		}
	}

	/**
	 * Utility method for checking the availablity of a directory.
	 * 
	 * @param dir
	 *            The directory to check.
	 * @param create
	 *            Flag if we should create if dir not already exists.
	 * @param checkCanRead
	 *            Flag if we should check read permission.
	 * @param checkCanWrite
	 *            Flag if we should check write permission.
	 * 
	 * @return The checked directory.
	 * 
	 * @throws IOException
	 */
	public static File checkDirectory(File dir, boolean create,
			boolean checkCanRead, boolean checkCanWrite) throws IOException {
		if (dir == null) {
			return dir;
		}
		if (!dir.exists() && create) {
			if (!dir.mkdirs()) {
				// concurrent creation may lead to exception - recheck later
				// throw new IOException("Can't create directory " +
				// dir.getPath());
			}
		}
		if (!dir.exists()) {
			throw new IOException("Can't create directory " + dir.getPath());
		}
		if (!dir.isDirectory()) {
			throw new IOException("Can't create directory " + dir.getPath());
		}
		if (checkCanRead && !dir.canRead()) {
			throw new IOException("No read access for directory "
					+ dir.getPath());
		}
		if (checkCanWrite && !dir.canWrite()) {
			throw new IOException("No write access for directory "
					+ dir.getPath());
		}
		return dir;
	}

	/**
	 * @see #checkDirectory(File, boolean, boolean, boolean)
	 */
	public static File checkDirectory(String path, boolean create,
			boolean checkCanRead, boolean checkCanWrite) throws IOException {
		if (StringTools.isEmpty(path)) {
			return null;
		}
		return checkDirectory(new File(path), create, checkCanRead,
				checkCanWrite);
	}

	/**
	 * Copy the byte content of <code>source</code> to <code>destination</code>.
	 * 
	 * @param source
	 *            The file whose contents we should copy.
	 * @param destination
	 *            The file where the contents are copied to.
	 * 
	 * @throws IOException
	 */
	public static void copyBinaryFile(File source, File destination)
			throws IOException {
		if (destination.isDirectory()) {
			destination = new File(destination, source.getName());
		}
		if (destination.getParentFile() != null
				&& !destination.getParentFile().exists()) {
			destination.getParentFile().mkdirs();
		}
		FileInputStream is = null;
		FileOutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(destination);
			StreamTools.copyStream(is, os);
		} finally {
			StreamTools.close(is);
			StreamTools.close(os);
		}
		destination.setLastModified(source.lastModified());
	}

	/**
	 * @see #copyBinaryFile(File, File)
	 */
	public static void copyFile(File source, File destination)
			throws IOException {
		copyBinaryFile(source, destination);
	}

	/**
	 * Copy the character content of <code>source</code> to
	 * <code>destination</code>.
	 * 
	 * @param source
	 *            The file whose contents we should copy.
	 * @param sourceEncoding
	 *            The encoding of the source byte stream.
	 * @param destination
	 *            The file where the contents are copied to.
	 * @param destinationEncoding
	 *            The encoding of the destination byte stream.
	 * 
	 * @throws IOException
	 */
	public static void copyFile(File source, String sourceEncoding,
			File destination, String destinationEncoding) throws IOException {
		// todo move to stream
		if ((sourceEncoding == null) || (destinationEncoding == null)
				|| sourceEncoding.equals(destinationEncoding)) {
			copyBinaryFile(source, destination);
			return;
		}

		if (destination.isDirectory()) {
			destination = new File(destination, source.getName());
		}
		if (destination.getParentFile() != null
				&& !destination.getParentFile().exists()) {
			destination.getParentFile().mkdirs();
		}
		FileInputStream is = null;
		FileOutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(destination);
			StreamTools.copyEncodedStream(is, sourceEncoding, os,
					destinationEncoding);
		} catch (Exception e) {
			throw new IOException("copying failed (" + e.getMessage() + ")");
		} finally {
			StreamTools.close(is);
			StreamTools.close(os);
		}
	}

	public static void copyRecursively(File source, File destination)
			throws IOException {
		if (source.isFile()) {
			copyFile(source, destination);
			return;
		}
		if (!source.isDirectory()) {
			throw new IOException("file '" + source.getAbsolutePath()
					+ "' does not exist.");
		}
		if (destination.isFile()) {
			throw new IOException("cannot copy directory into file");
		}
		destination.mkdirs();
		String[] content = source.list();
		for (int i = 0; i < content.length; i++) {
			copyRecursively(new File(source, content[i]), new File(destination,
					content[i]));
		}
	}

	public static File copyRecursivelyInto(File source, File destinationParent,
			String newName) throws IOException {
		if (destinationParent.isFile()) {
			throw new IOException("can't copy into file");
		}
		String destinationName;
		if (newName == null) {
			destinationName = source.getName();
		} else {
			destinationName = newName;
		}
		File destinationFile = new File(destinationParent, destinationName);
		if (source.equals(destinationFile)) {
			return destinationFile;
		}
		if (source.isFile()) {
			copyFile(source, destinationFile);
			return destinationFile;
		}
		if (!source.isDirectory()) {
			throw new IOException("file '" + source.getAbsolutePath()
					+ "' does not exist.");
		}
		String[] content = source.list();
		// play safe - list before creating directory (no recursion)
		destinationFile.mkdirs();
		for (int i = 0; i < content.length; i++) {
			copyRecursivelyInto(new File(source, content[i]), destinationFile,
					content[i]);
		}
		return destinationFile;
	}

	/**
	 * Create an empty file.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public static void createEmptyFile(File file) throws IOException {
		FileOutputStream os = new FileOutputStream(file);
		try {
			//
		} finally {
			StreamTools.close(os);
		}
	}

	/**
	 * Create a file object representing a temporary file in the user's temp dir
	 * with the same name as the given file.
	 * 
	 * @param file
	 *            file to use
	 * @return file object representing a temporary file
	 */
	public static File createTempFile(File file) throws IOException {
		String name;
		String extension;
		int index;

		name = file.getName();
		index = name.lastIndexOf('.');
		if (index >= 0) {
			extension = name.substring(index);
			name = name.substring(0, index);
		} else {
			extension = StringTools.EMPTY;
		}
		if (name.length() < 3) {
			name = "tmp" + name;
		}
		return TempTools.createTempFile(name, extension);
	}

	/**
	 * Create a file object representing a temporary file in the user's temp dir
	 * with the given filename.
	 * <p>
	 * This does not actually create a file in the file system.
	 * 
	 * @param filename
	 *            filename to use
	 * @return file object representing a temporary file
	 */
	public static File createTempFile(String filename) throws IOException {
		return createTempFile(new File(filename));
	}

	/**
	 * Delete any file in <code>directory</code> that is older than
	 * <code>millis</code> milliseconds. When <code>recursiveScan</code> is
	 * <code>true</code> the directory lookup is made recursive.
	 * 
	 * @param directory
	 *            The directory to scan.
	 * @param millis
	 *            The number of milliseconds a file is allowed to live.
	 * @param recursiveScan
	 *            Flag if we should handle directories recursive.
	 * 
	 * @throws IOException
	 */
	public static void deleteAfter(File directory, long millis,
			boolean recursiveScan) throws IOException {
		if (millis <= 0) {
			return;
		}

		String[] fileNames = directory.list();
		if (fileNames == null) {
			throw new IOException("can not list " + directory);
		}

		long checkMillis = System.currentTimeMillis() - millis;
		for (int j = 0; j < fileNames.length; j++) {
			File file = new File(directory, fileNames[j]);
			if (file.isDirectory() && recursiveScan) {
				deleteAfter(file, millis, recursiveScan);
			}
			if (file.lastModified() < checkMillis) {
				file.delete();
			}
		}
	}

	/**
	 * Deletes a file or directory, if necessary recursivly.
	 * 
	 * <p>
	 * Returns <code>true</code> if file could be deleted inclusive its
	 * components, otherwise false.
	 * </p>
	 * 
	 * @param file
	 *            The file or directory to delete.
	 * 
	 * @return <code>true</code> if file could be deleted inclusive its
	 *         components, otherwise false.
	 */
	public static boolean deleteRecursivly(File file) {
		return deleteRecursivly(file, true);
	}

	/**
	 * Deletes a file or directory, if necessary recursivly.
	 * 
	 * <p>
	 * Returns <code>true</code> if file could be deleted inclusive its
	 * components, otherwise false.
	 * </p>
	 * 
	 * @param file
	 *            The file or directory to delete.
	 * @param deleteRoot
	 *            Flag if the root directory should be deleted itself.
	 * 
	 * @return <code>true</code> if file could be deleted inclusive its
	 *         components, otherwise false.
	 */
	public static boolean deleteRecursivly(File file, boolean deleteRoot) {
		if (file == null || !file.exists()) {
			return true;
		}
		if (file.isFile()) {
			return file.delete();
		}
		String[] files = file.list();
		if (files == null) {
			return false;
		}
		for (int i = 0; i < files.length; i++) {
			if (!deleteRecursivly(new File(file, files[i]))) {
				return false;
			}
		}
		if (deleteRoot) {
			return file.delete();
		}
		return true;
	}

	/**
	 * <code>true</code> when the two files represent the same physical file in
	 * the file system.
	 * 
	 * @param source
	 *            The first file to be checked.
	 * @param destination
	 *            The second file to be checked.
	 * 
	 * @return <code>true</code> when the two files represent the same physical
	 *         file in the file system.
	 */
	public static boolean equalsOnSystem(File source, File destination) {
		try {
			if (isWindows()) {
				return source.getCanonicalPath().equalsIgnoreCase(
						destination.getCanonicalPath());
			} else {
				return source.getCanonicalPath().equals(
						destination.getCanonicalPath());
			}
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Get the local name of the file in its directory without the extension.
	 * 
	 * @param file
	 *            The file whose base name is requested.
	 * 
	 * @return The local name of the file in its directory without the
	 *         extension.
	 */
	public static String getBaseName(File file) {
		if (file == null) {
			return getBaseName((String) null, StringTools.EMPTY);
		} else {
			return getBaseName(file.getName(), StringTools.EMPTY);
		}
	}

	/**
	 * Get the local name of the file in its directory without the extension.
	 * 
	 * @param file
	 *            The file whose base name is requested.
	 * 
	 * @return The local name of the file in its directory without the
	 *         extension.
	 */
	public static String getBaseName(File file, String defaultName) {
		if (file == null) {
			return getBaseName((String) null, defaultName);
		} else {
			return getBaseName(file.getName(), defaultName);
		}
	}

	/**
	 * Get the local name of the file in its directory without the extension.
	 * 
	 * @param filename
	 *            The filename whose base name is requested.
	 * 
	 * @return The local name of the file in its directory without the
	 *         extension.
	 */
	public static String getBaseName(String filename) {
		return getBaseName(filename, StringTools.EMPTY);
	}

	/**
	 * Get the local name of the file in its directory without the extension.
	 * 
	 * @param filename
	 *            The filename whose base name is requested.
	 * @param defaultName
	 *            returned if filename is null or a empty String
	 * 
	 * @return The local name of the file in its directory without the
	 *         extension.
	 */
	public static String getBaseName(String filename, String defaultName) {
		if (StringTools.isEmpty(filename)) {
			return defaultName;
		}
		int dotPos = filename.lastIndexOf('.');
		if (dotPos >= 1) {
			return filename.substring(0, dotPos);
		}
		return filename;
	}

	public static String getEncoding() {
		return System.getProperty("file.encoding");
	}

	/**
	 * Get the extension of the file name. If no extension is present, the empty
	 * string is returned.
	 * 
	 * @param file
	 *            The file whose extension is requested.
	 * 
	 * @return The extension of the file name. If no extension is present, the
	 *         empty string is returned.
	 */
	public static String getExtension(File file) {
		return getExtension(file.getName(), StringTools.EMPTY);
	}

	/**
	 * Get the extension of the file name. If no extension is present, the empty
	 * string is returned.
	 * 
	 * @param filename
	 *            The filename whose extension is requested.
	 * 
	 * @return The extension of the file name. If no extension is present, the
	 *         empty string is returned.
	 */
	public static String getExtension(String filename) {
		return getExtension(filename, StringTools.EMPTY);
	}

	/**
	 * Get the extension of the file name. If no extension is present, the
	 * defaultName is returned.
	 * 
	 * @param filename
	 *            The filename whose extension is requested.
	 * 
	 * @param defaultName
	 *            returned if the filename is empty or null or there is no
	 *            extension
	 * 
	 * @return The extension of the file name. If no extension is present, the
	 *         empty string is returned.
	 */
	public static String getExtension(String filename, String defaultName) {
		if (StringTools.isEmpty(filename)) {
			return defaultName;
		}
		int dotPos = filename.lastIndexOf('.');
		if (dotPos >= 0) {
			return filename.substring(dotPos + 1);
		}
		return defaultName;
	}

	/**
	 * Get the local name of the file in its directory (with extension).
	 * 
	 * @param filename
	 *            The filename whose name is requested.
	 * 
	 * @return The local name of the file in its directory (with extension)
	 */
	public static String getFileName(String filename) {
		return getFileName(filename, StringTools.EMPTY);
	}

	/**
	 * Get the local name of the file in its directory (with extension).
	 * 
	 * @param filename
	 *            The filename whose name is requested.
	 * @param defaultName
	 *            returned if filename is null or a empty String
	 * 
	 * @return The local name of the file in its directory (with extension)
	 */
	public static String getFileName(String filename, String defaultName) {
		if (StringTools.isEmpty(filename)) {
			return defaultName;
		}
		int dotPos = filename.lastIndexOf('/');
		if (dotPos >= 0) {
			if (dotPos == filename.length() - 1) {
				return defaultName;
			} else {
				filename = filename.substring(dotPos + 1);
			}
		}
		dotPos = filename.lastIndexOf('\\');
		if (dotPos >= 0) {
			if (dotPos == filename.length() - 1) {
				return defaultName;
			} else {
				filename = filename.substring(dotPos + 1);
			}
		}
		return filename;
	}

	/**
	 * Try to get a valid parent for file.
	 * 
	 * @param file
	 */
	public static File getParentFile(File file) {
		File parentFile = file.getParentFile();
		if (parentFile == null) {
			parentFile = file.getAbsoluteFile().getParentFile();
		}
		if (parentFile == null) {
			return null;
		}
		File grandpa = parentFile.getParentFile();
		if (grandpa != null) {
			// filter UNC path root also, it is not "enumerable"
			String grandpaPath = grandpa.getAbsolutePath();
			if (grandpaPath.equals("\\\\")) {
				return null;
			}
		}
		return parentFile;
	}

	/**
	 * break a path down into individual elements and add to a list. example :
	 * if a path is /a/b/c/d.txt, the breakdown will be [d.txt,c,b,a]
	 * 
	 * @param f
	 *            input file
	 * 
	 * @return a List collection with the individual elements of the path in
	 *         reverse order
	 */
	private static List getPathList(File f) throws IOException {
		List l = new ArrayList();
		File r = f.getCanonicalFile();
		while (r != null) {
			if (r.getName().length() == 0) {
				int dblptIndex = r.getPath().indexOf(":");
				if (dblptIndex == -1) {
					l.add("");
				} else {
					l.add(r.getPath().substring(0, dblptIndex));
				}
			} else {
				l.add(r.getName());
			}
			r = r.getParentFile();
		}

		List reversed = new ArrayList();
		for (int i = l.size() - 1; i >= 0; i--) {
			reversed.add(l.get(i));
		}
		return reversed;
	}

	/**
	 * Get the path name of the file.
	 * 
	 * @param filename
	 *            The filename whose path is requested.
	 * @param defaultName
	 *            returned if filename is null or a empty String
	 * 
	 * @return The path name of the file
	 */
	public static String getPathName(String filename, String defaultName) {
		if (filename == null) {
			return defaultName;
		}
		int dotPos = filename.lastIndexOf('/');
		if (dotPos >= 0) {
			filename = filename.substring(0, dotPos);
		}
		dotPos = filename.lastIndexOf('\\');
		if (dotPos >= 0) {
			filename = filename.substring(0, dotPos);
		}
		if (StringTools.isEmpty(filename)) {
			return defaultName;
		}
		return filename;
	}

	/**
	 * get relative path of "file" with respect to "base" directory example :
	 * 
	 * <code>
	 * base = /a/b/c;
	 * file = /a/d/e/x.txt;
	 * getRelativePath(file, base) == ../../d/e/x.txt;
	 * </code>
	 * 
	 * @param base
	 *            base path, should be a directory, not a file, or it doesn't
	 *            make sense
	 * @param file
	 *            file to generate path for
	 * 
	 * @return path from home to f as a string
	 */
	public static String getPathRelativeTo(File file, File base)
			throws IOException {
		String relativePath = null;
		if (base != null) {
			List fileList = getPathList(file);
			List baseList = getPathList(base);
			relativePath = matchPathLists(fileList, baseList);
		}
		if (relativePath == null) {
			return file.getAbsolutePath();
		} else {
			return relativePath;
		}
	}

	public static String getPathRelativeTo(File file, File base,
			boolean ifAncestor) {
		if (base == null) {
			return file.getPath();
		} else {
			if (FileTools.isAncestor(base, file)) {
				try {
					return FileTools.getPathRelativeTo(file, base);
				} catch (IOException e) {
					return file.getPath();
				}
			} else {
				return file.getPath();
			}
		}
	}

	public static boolean isAncestor(File parent, File descendant) {
		if (parent == null) {
			return false;
		}
		File current = descendant;
		while (!parent.equals(current)) {
			if (current == null) {
				return false;
			}
			current = current.getParentFile();
		}
		return true;
	}

	public static boolean isWindows() {
		return File.separatorChar == '\\';
	}

	public static Lock lock(File file) {
		synchronized (maps) {
			if (maps.get(file) != null) {
				// some systems do not prevent locking in the same VM
				return null;
			}
			FileOutputStream os = null;
			FileChannel channel = null;
			File lockFile = null;
			try {
				if (file.isFile()) {
					lockFile = new File(file.getAbsolutePath() + ".lock");
				} else {
					lockFile = new File(file, DIRECTORY_LOCK);
				}
				os = new FileOutputStream(lockFile);
				channel = os.getChannel();
				channel.tryLock();
				Lock lock = new Lock();
				lock.file = file;
				lock.lockFile = lockFile;
				lock.lockStream = os;
				maps.put(file, lock);
				return lock;
			} catch (Exception e) {
				StreamTools.close(os);
				if (channel != null) {
					try {
						channel.close();
					} catch (IOException ex) {
						//
					}
				}
				if (lockFile != null) {
					lockFile.delete();
				}
				return null;
			}
		}
	}

	/**
	 * figure out a string representing the relative path of 'f' with respect to
	 * 'r'
	 * 
	 * @param r
	 *            home path
	 * @param f
	 *            path of file
	 * 
	 * @return The relative path
	 */
	private static String matchPathLists(List fileList, List baseList) {
		Iterator sourceIterator = baseList.iterator();
		Iterator targetIterator = fileList.iterator();

		// remove equal path components
		boolean intersection = false;
		while (sourceIterator.hasNext() && targetIterator.hasNext()) {
			if (sourceIterator.next().equals(targetIterator.next())) {
				sourceIterator.remove();
				targetIterator.remove();
				intersection = true;
			} else {
				break;
			}
		}

		// only the differing path components remain
		if (!intersection) {
			return null;
		}
		String relPath = "";
		for (int i = 0; i < baseList.size(); i++) {
			relPath += (".." + File.separator);
		}
		for (Iterator i = fileList.iterator(); i.hasNext();) {
			relPath += (i.next());
			if (i.hasNext()) {
				relPath += File.separator;
			}
		}
		return relPath;
	}

	/**
	 * @see #renameFile(File, String, File, String)
	 */
	public static void renameFile(File source, File destination)
			throws IOException {
		renameFile(source, null, destination, null);
	}

	/**
	 * "Rename" a file.
	 * 
	 * <p>
	 * The effect is that there is a new file <code>destination</code>, encoded
	 * in <code>destinationEncoding</code>, the old file <code>source</code> is
	 * deleted.
	 * </p>
	 * 
	 * @param source
	 *            The source name of the file.
	 * @param sourceEncoding
	 *            The encoding of the source file.
	 * @param destination
	 *            The destination name of the file.
	 * @param destinationEncoding
	 *            The encoding of the destination file.
	 * 
	 * @throws IOException
	 * 
	 */
	public static void renameFile(File source, String sourceEncoding,
			File destination, String destinationEncoding) throws IOException {
		if (source.getCanonicalFile().equals(destination.getCanonicalFile())) {
			return;
		}
		destination.delete();
		if (((sourceEncoding != null) && (destinationEncoding != null) && !sourceEncoding
				.equals(destinationEncoding)) || !source.renameTo(destination)) {
			// try a little harder
			// some implementations can't rename over different file system
			// platforms
			// (e.g. from NT to OS/2)
			File tempDestination = new File(destination.getAbsolutePath()
					+ ".--temporary--");
			copyFile(source, sourceEncoding, tempDestination,
					destinationEncoding);
			tempDestination.renameTo(destination);
			if (!source.delete()) {
				// undo creation of copy
				destination.delete();
				throw new IOException("deleting " + source + " failed");
			}
		}
	}

	/**
	 * Return a new {@link File} instance for "path". If path is relative, than
	 * it will be interpreted as a child of "parent", if it is absolute, it is
	 * returned as is.
	 * <p>
	 * ATTENTION: On windows, if "path" is absolute but without drive or UNC
	 * prefix, this root information is NOT taken from "parent".
	 * 
	 * @param parent
	 * @param path
	 * @return Return a new {@link File} instance for "path".
	 */
	public static File resolvePath(File parent, String path) {
		if (StringTools.isEmpty(path)) {
			return (parent == null) ? new File("") : parent;
		}
		if (parent == null) {
			return new File(path);
		}

		File file = new File(path);
		if (file.isAbsolute()) {
			return file;
		}
		return new File(parent, path);
	}

	/**
	 * Create a byte array with the files content.
	 * 
	 * @param file
	 *            The file to read.
	 * 
	 * @return Create a byte array with the files content.
	 * 
	 * @throws IOException
	 */
	public static byte[] toBytes(File file) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			return StreamTools.toByteArray(is);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	/**
	 * Read a file's content at once and return as a string.
	 * 
	 * <p>
	 * Use with care!
	 * </p>
	 * 
	 * @param file
	 *            The file to read.
	 * 
	 * @return The string content of the file.
	 * 
	 * @throws IOException
	 */
	public static String toString(File file) throws IOException {
		return toString(file, System.getProperty("file.encoding"));
	}

	/**
	 * Read a file's content at once and return as a string in the correct
	 * encoding.
	 * 
	 * <p>
	 * Use with care!
	 * </p>
	 * 
	 * @param file
	 *            The file to read.
	 * @param encoding
	 *            The encoding to use.
	 * 
	 * @return The string content of the file.
	 * 
	 * @throws IOException
	 */
	public static String toString(File file, String encoding)
			throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			return StreamTools.toString(is, encoding);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	/**
	 * Replaces all characters that are generally not allowed or considered
	 * useful in filenames with underscore.
	 * 
	 * @param param
	 *            java.lang.String
	 * 
	 * @return java.lang.String
	 */
	public static String trimPath(String param) {
		if (param == null) {
			return null;
		}
		String tmp = param.trim();
		// uniform use of slash and backslash
		String drivePrefix = StringTools.EMPTY;
		// save drive
		if ((tmp.length() >= 2) && (tmp.charAt(1) == ':')) {
			drivePrefix = tmp.substring(0, 2);
			tmp = tmp.substring(2);
		}
		tmp = tmp.replaceAll("[\\*\"\\?\\<\\>\\|\\:!\\n\\t\\r\\f]", "_");
		return drivePrefix + tmp;
	}

	protected static void unlock(Lock lock) {
		synchronized (maps) {
			maps.remove(lock.file);
			lock.valid = false;
			StreamTools.close(lock.lockStream);
			lock.lockFile.delete();
		}
	}

	/**
	 * Wait for a file to arrive.
	 * 
	 * <p>
	 * The method waits at most <code>timeout</code> milliseconds for a file to
	 * arrive. When <code>delay</code> is != 0 the method checks the file's size
	 * for changes until it reaches a stable size.
	 * </p>
	 * 
	 * @param file
	 *            The file to wait for.
	 * @param timeout
	 *            The maximum time in milliseconds to wait for first occurence
	 *            of <code>file</code>.
	 * @param delay
	 *            The number of milliseconds between two checks against the
	 *            files size.
	 * 
	 * @throws IOException
	 */
	public static void wait(File file, long timeout, long delay)
			throws IOException {
		// todo zero length files
		long stop = System.currentTimeMillis() + timeout;
		for (;;) {
			try {
				if (file.exists()) {
					if (delay > 0) {
						long oldSize = -1;
						long newSize = file.length();
						for (;;) {
							if (oldSize != newSize) {
								oldSize = newSize;
								Thread.sleep(delay);
								newSize = file.length();
								continue;
							}
							break;
						}
					}
					return;
				}
				if (System.currentTimeMillis() > stop) {
					// timeout
					throw new IOException("timeout waiting for "
							+ file.getPath());
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// interrupted
				throw new IOException("interrupted waiting for "
						+ file.getPath());
			}
		}
	}

	/**
	 * Create a file from the byte content.
	 * 
	 * @param file
	 *            The file to write/create
	 * @param bytes
	 *            The data to be written into the file.
	 * 
	 * @throws IOException
	 */
	public static void write(File file, byte[] bytes) throws IOException {
		FileOutputStream os = new FileOutputStream(file);
		try {
			// write stream all at once
			os.write(bytes);
		} finally {
			StreamTools.close(os);
		}
	}

	/**
	 * Create a file from the string content.
	 * 
	 * @param file
	 *            The file to write/create
	 * @param text
	 *            The text to be written into the file.
	 * 
	 * @throws IOException
	 */
	public static void write(File file, String text) throws IOException {
		write(file, text, Charset.defaultCharset().name(), false);
	}

	/**
	 * Create a file from the string content.
	 * 
	 * @param file
	 *            The file to write/create
	 * @param text
	 *            The text to be written into the file.
	 * @param append
	 *            Flag to append to an existing file or create a new file.
	 * 
	 * @throws IOException
	 */
	public static void write(File file, String text, boolean append)
			throws IOException {
		write(file, text, Charset.defaultCharset().name(), append);
	}

	/**
	 * Create a file from the string content.
	 * 
	 * @param file
	 *            The file to write/create
	 * @param text
	 *            The text to be written into the file.
	 * 
	 * @throws IOException
	 */
	public static void write(File file, String text, String encoding)
			throws IOException {
		write(file, text, encoding, false);
	}

	/**
	 * Create a file from the string content / append a string to a file
	 * 
	 * @param file
	 *            The file to write/create
	 * @param text
	 *            The text to be written into the file.
	 * @param append
	 *            Flag to append to an existing file or create a new file.
	 * 
	 * @throws IOException
	 */
	public static void write(File file, String text, String encoding,
			boolean append) throws IOException {
		OutputStream os = null;
		Writer writer = null;
		try {
			os = new FileOutputStream(file, append);
			writer = new OutputStreamWriter(os, encoding);
			writer.write(text);
		} finally {
			StreamTools.close(writer);
			StreamTools.close(os);
		}
	}

	private FileTools() {
		//
	}

	public static boolean isExtensionMatch(File file, String extensions) {
		if (StringTools.isEmpty(extensions)) {
			return false;
		}
		String[] tempExtensions = extensions.toLowerCase().split(";");
		String tempName = file.getName().toLowerCase();
		for (int i = 0; i < tempExtensions.length; i++) {
			if (tempName.endsWith(tempExtensions[i])) {
				return true;
			}
		}
		return false;
	}
}
