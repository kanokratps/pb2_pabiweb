package pb.common.comparator;

import java.util.Comparator;

import pb.common.model.FileModel;

public class FileTimeComparator implements Comparator<FileModel> {

	@Override
	public int compare(FileModel f1, FileModel f2) {
		return f1.getTime().compareTo(f2.getTime());
	}

}
