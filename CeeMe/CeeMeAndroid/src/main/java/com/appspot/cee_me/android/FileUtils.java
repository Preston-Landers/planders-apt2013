package com.appspot.cee_me.android;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.UUID;

/**
 * File utilities.
 */
public class FileUtils {
    private static final String TAG = Config.APPNAME + ".FileUtils";
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
        String filePath = getPath(uri, contentResolver, false);
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

    /**
     * Get a human readable file size
     * @param bytes size of the file to interpret
     * @return human readable string describing file size
     */
    public static String byteCountToDisplaySize(long bytes) {
        return org.apache.commons.io.FileUtils.byteCountToDisplaySize(bytes);
    }

    /**
     * Check whether external storage is writeable
     * @return true if external storage is writeable
     */
    public static boolean isStorageWritable() {
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        return mExternalStorageAvailable && mExternalStorageWriteable;
    }

    /**
     * Decides the name within Google Cloud Storage for this file. Incorporates the sender's deviceKey,
     * a random UUID, and the original filename.
     *
     * @param deviceKey sending device's key
     * @param filePath  original local file path (complete path)
     * @return a new filename suitable for GCS.
     */
    public static String getNewGCSFilename(String deviceKey, String filePath) {
        UUID newUUID = UUID.randomUUID();
        String baseFilename = FileUtils.getBaseFilenameFromPath(filePath);
        String prefix = "";
        if (Config.LOCAL_APP_SERVER) {
            // so we can distinguish which GCS files are associated with my
            // development app server as opposed to the live website.
            prefix = "dev/";
        }
        return prefix + newUUID + "/" + baseFilename;
    }

    /**
     * Ensure that the directory containing the file exists
     * @param file a file to check
     */
    public static void ensureDirectory(File file) {
        if (file.exists()) {
            return;
        }
        if (!file.getParentFile().mkdirs()) {
            Log.e(TAG, "Failed to create directory for: " + file);
        }
    }

}
