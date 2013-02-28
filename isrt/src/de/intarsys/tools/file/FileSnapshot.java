package de.intarsys.tools.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import de.intarsys.tools.stream.StreamTools;

public class FileSnapshot {

	final private File file;

	private List<FileSnapshot> children;

	final private boolean directory;

	private long fileLength = 0;

	private long lastModified = 0;

	public FileSnapshot(File file) {
		super();
		this.file = file;
		this.directory = file.isDirectory();
		updateLocal(file.length(), file.lastModified());
		File[] tempFiles = file.listFiles();
		if (tempFiles != null) {
			updateChildren(Arrays.asList(tempFiles));
		}
	}

	public FileSnapshot[] getChildren() {
		if (children == null) {
			return null;
		}
		return children.toArray(new FileSnapshot[children.size()]);
	}

	public File getFile() {
		return file;
	}

	public long getFileLength() {
		return fileLength;
	}

	public long getLastModified() {
		return lastModified;
	}

	public boolean isAvailable() {
		if (children != null) {
			Iterator<FileSnapshot> it = children.iterator();
			while (it.hasNext()) {
				FileSnapshot child = it.next();
				if (!child.isAvailable()) {
					return false;
				}
			}
			return true;
		}
		if (directory) {
			return !isLost();
		}
		InputStream is = null;
		try {
			is = new FileInputStream(getFile());
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			StreamTools.close(is);
		}
	}

	public boolean isChanged() {
		long newLastModified = getFile().lastModified();
		long newLength = getFile().length();
		File[] tempFiles = getFile().listFiles();
		List<File> newFiles = tempFiles == null ? null : new ArrayList<File>(
				Arrays.asList(tempFiles));
		boolean exists = getFile().exists();

		if (!exists) {
			children = null;
			return true;
		}

		boolean changed = false;
		if (children != null) {
			Iterator<FileSnapshot> it = children.iterator();
			while (it.hasNext()) {
				FileSnapshot child = it.next();
				if (child.isChanged()) {
					changed = true;
					if (child.isLost()) {
						it.remove();
					}
				}
				if (newFiles != null) {
					newFiles.remove(child.getFile());
				}
			}
		}

		if (newFiles != null && newFiles.size() > 0) {
			updateChildren(newFiles);
			changed = true;
		}

		if (newLastModified != lastModified || newLength != fileLength) {
			updateLocal(newLength, newLastModified);
			changed = true;
		}

		return changed;
	}

	public boolean isLost() {
		return !getFile().exists();
	}

	protected void updateChildren(List<File> newFiles) {
		if (children == null) {
			children = new ArrayList<FileSnapshot>();
		}
		for (File file : newFiles) {
			children.add(new FileSnapshot(file));
		}
	}

	protected void updateLocal(long newLength, long newModified) {
		this.fileLength = newLength;
		this.lastModified = newModified;
	}

}
