package connexus;

import java.util.List;

public class FileUploadEntity {
    private List<FileMeta> files;

    public FileUploadEntity(List<FileMeta> files) {
        this.files = files;
    }

    public FileUploadEntity() {
    }
}