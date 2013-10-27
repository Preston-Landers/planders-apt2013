package connexus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

// Used to return JSON data to file uploader
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FileMeta {
    String name;
    long size;
    String url;
    String delete_url;
    String delete_type;
    String thumbnail_url;
    String comment;

    public FileMeta(String filename, long size, String url, String urlPreview, String comment) {
        this.name = filename;
        this.size = size;
        this.url = url;
        this.delete_url = url;
        this.delete_type = "DELETE";
        this.thumbnail_url = urlPreview;
        this.comment = comment;
    }

    public FileMeta() {
    }
}
