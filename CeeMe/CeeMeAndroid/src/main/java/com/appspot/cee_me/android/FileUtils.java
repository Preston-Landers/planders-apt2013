package com.appspot.cee_me.android;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * File utilities.
 */
public class FileUtils {

    /**
     * Get the mimetype of a file, including content: uris
     *
     * @param fileUri
     * @param cR
     * @return
     */
    public static String getMimeType(Uri fileUri, ContentResolver cR) {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = null;
        // type = mime.getExtensionFromMimeType(cR.getType(fileUri));
        if (fileUri.getScheme().equals("file")) {
            // guess based on the extension
            String fileExt = MimeTypeMap.getFileExtensionFromUrl(fileUri.getPath());
            type = mime.getMimeTypeFromExtension(fileExt);
        } else if (fileUri.getScheme().equals("content")) {
            type = cR.getType(fileUri);
        } else {
            throw new IllegalArgumentException("Unknown Uri scheme: " + fileUri.getScheme());
        }

        return type;
    }

    /**
     * Turn a content:// URI into a file:// URI
     *
     * @param _uri         a content:// URI
     * @param addFileProto if true, add the file:// protocol to the beginning
     * @return a file:// URI
     */
    public static String getPath(Uri _uri, ContentResolver contentResolver, boolean addFileProto) {
        if (_uri == null) {
            return null;
        }
        String filePath = "";
        if (_uri != null && "content".equals(_uri.getScheme())) {
            Cursor cursor = contentResolver.query(_uri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);
            cursor.moveToFirst();
            filePath = cursor.getString(0);
            cursor.close();
        } else {
            filePath = _uri.getPath();
        }
        if (addFileProto) {
            return "file://" + filePath;
        } else {
            return filePath;
        }
    }

    /**
     * Returns the size, in bytes, of the file uri
     *
     * @param uri file to check
     * @return size in bytes
     */
    public static Long getFileSize(Uri uri, ContentResolver contentResolver) {
        String filePath = getPath(uri, contentResolver, true);
        File f = new File(filePath);
        return f.length();
    }

    /**
     * Given a full file path, return the "base" filename.
     * E.g., long/directory/structure/fileName.jpg returns fileName.jpg
     *
     * @param filePath a local path to a file
     * @return base file from the path
     */
    public static String getBaseFilenameFromPath(String filePath) {
        return new File(filePath).getName();
    }
}
