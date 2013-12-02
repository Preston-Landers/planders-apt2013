package com.appspot.cee_me.android;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.UUID;

/**
 * File utilities.
 */
public class FileUtils {

    private static final String TAG = Config.APPNAME + ".FileUtils";

    /**
     * Get the mimetype of a file, including content: uris
     *
     * @param fileUri a file URI to find the mimetype for
     * @param cR a content resolver to use to find the mimetype
     * @return mimetype of the file
     */
    public static String getMimeType(Uri fileUri, ContentResolver cR) {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type;
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
        String filePath;
        if ("content".equals(_uri.getScheme())) {
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

    private static final BigDecimal KILO_DIVISOR = new BigDecimal(1024L);

    enum SizeSuffix {
        B, KB, MB, GB, TB, PB, EB, ZB, YB
    }

    /**
     * Get a human readable file size
     *
     * @param size size of the file to interpret
     * @return human readable string describing file size
     */
    public static String byteCountToDisplaySize(BigInteger size, int maxChars) {
        // return org.apache.commons.io.FileUtils.byteCountToDisplaySize(bytes);
        // From: https://issues.apache.org/jira/browse/IO-373
        String displaySize;
        BigDecimal bdSize = new BigDecimal(size);
        SizeSuffix selectedSuffix = SizeSuffix.B;
        for (SizeSuffix sizeSuffix : SizeSuffix.values()) {
            if (sizeSuffix.equals(SizeSuffix.B)) {
                continue;
            }
            if (bdSize.setScale(0, RoundingMode.HALF_UP).toString().length() <= maxChars) {
                break;
            }
            selectedSuffix = sizeSuffix;
            bdSize = bdSize.divide(KILO_DIVISOR);
        }
        displaySize = bdSize.setScale(0, RoundingMode.HALF_UP).toString();
        if (displaySize.length() < maxChars - 1) {
            displaySize = bdSize.setScale(
                    maxChars - 1 - displaySize.length(), RoundingMode.HALF_UP).toString();
        }
        return displaySize + " " + selectedSuffix.toString();
    }

    /**
     * Check whether external storage is writeable
     *
     * @return true if external storage is writeable
     */
    public static boolean isStorageWritable() {
        boolean mExternalStorageAvailable;
        boolean mExternalStorageWriteable;
        String state = Environment.getExternalStorageState();

        switch (state) {
            case Environment.MEDIA_MOUNTED:
                // We can read and write the media
                mExternalStorageAvailable = mExternalStorageWriteable = true;
                break;
            case Environment.MEDIA_MOUNTED_READ_ONLY:
                // We can only read the media
                mExternalStorageAvailable = true;
                mExternalStorageWriteable = false;
                break;
            default:
                // Something else is wrong. It may be one of many other states, but all we need
                //  to know is we can neither read nor write
                mExternalStorageAvailable = mExternalStorageWriteable = false;
                break;
        }
        return mExternalStorageAvailable && mExternalStorageWriteable;
    }

    /**
     * Decides the name within Google Cloud Storage for this file. Incorporates
     * a random UUID and the original filename.
     *
     * @param filePath original local file path (complete path)
     * @return a new filename suitable for GCS.
     */
    public static String getNewGCSFilename(String filePath) {
        String dateStr = DateUtils.getF8Date(null); // current date in YYYYMMDD format
        UUID newUUID = UUID.randomUUID();
        String baseFilename = FileUtils.getBaseFilenameFromPath(filePath);
        String prefix = "";
        if (Config.LOCAL_APP_SERVER) {
            // so we can distinguish which GCS files are associated with my
            // development app server as opposed to the live website.
            prefix = "dev/";
        }
        return prefix + dateStr + "/" + newUUID + "/" + baseFilename;
    }

    /**
     * Ensure that the directory containing the file exists
     *
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
