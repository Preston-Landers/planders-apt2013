package com.appspot.cee_me.android;

import android.util.Log;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.StorageObject;

import java.io.*;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

// import com.google.api.client.auth.oauth2.Credential;


/**
 * Simple wrapper around the Google Cloud Storage API
 * original version:
 * https://github.com/pliablematter/simple-cloud-storage
 */
public class CloudStorage {

    private GoogleAccountCredential credential;
    private Storage storage;

    private static final String TAG = Config.APPNAME + ".CloudStorage";
    private static final String PROJECT_ID = Config.GCM_SENDER_KEY;
    private static final String APPLICATION_NAME = Config.APPNAME;

    public CloudStorage(GoogleAccountCredential credential) {
        this.credential = credential;
        storage = getStorage();
    }

    /**
     * Uploads a file to a bucket. Filename and content type will be based on
     * the original file.
     *
     * @param bucketName  Bucket where file will be uploaded
     * @param filePath    Absolute path of the local file to upload
     * @param gcsFilename Filename to create within GCS
     * @param ioProgress  an object to report upload progress for the GUI
     * @throws IOException
     */
    public void uploadFile(String bucketName, String filePath, String gcsFilename, IOProgress ioProgress)
            throws IOException {

        StorageObject object = new StorageObject();
        object.setBucket(bucketName);

        File file = new File(filePath);
        Long fileSize = file.length();
        Log.d(TAG, "uploadFile START: " + bucketName + ":" + gcsFilename + " -> " + filePath);

        try (InputStream stream = new FileInputStream(file)) {
            String contentType = URLConnection
                    .guessContentTypeFromStream(stream);
            InputStreamContent content = new InputStreamContent(contentType,
                    stream);

            Storage.Objects.Insert insert = storage.objects().insert(
                    bucketName, null, content);
            insert.setName(gcsFilename);
            insert.getMediaHttpUploader().setProgressListener(new CloudUploadProgressListener(ioProgress, fileSize));

            insert.execute();
            Log.d(TAG, "uploadFile FINISH: " + bucketName + ":" + gcsFilename + " -> " + filePath);
        }
    }


    public void downloadFile(String bucketName, String gcsFilename, String destinationDirectory, IOProgress ioProgress) throws IOException {

        File directory = new File(destinationDirectory);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Provided destinationDirectory path is not a directory");
        }
        File file = new File(directory.getAbsolutePath() + "/" + gcsFilename);
        Log.d(TAG, "downloadFile START: " + bucketName + ":" + gcsFilename);

        Storage.Objects.Get get = storage.objects().get(bucketName, gcsFilename);
        StorageObject SO = get.execute();
        long fileSize = -1;
        if (SO != null) {
            fileSize = SO.getSize().longValue();
            Log.i(TAG, "found file size " + fileSize + "  for download: " + gcsFilename);
        }
        get.getMediaHttpDownloader().setProgressListener(new CloudDownloadProgressListener(ioProgress, fileSize));
        try (FileOutputStream stream = new FileOutputStream(file)) {
            // get.executeAndDownloadTo(stream);   /// what's the difference?
            get.executeMediaAndDownloadTo(stream); /// this is the one in docs
        }

    }

    /**
     * Deletes a file within a bucket
     *
     * @param bucketName Name of bucket that contains the file
     * @param fileName   The file to delete
     * @throws Exception
     */
    public void deleteFile(String bucketName, String fileName)
            throws Exception {

        storage.objects().delete(bucketName, fileName).execute();
    }

    /**
     * Creates a bucket
     *
     * @param bucketName Name of bucket to create
     * @throws Exception
     */
    public void createBucket(String bucketName) throws Exception {

        Bucket bucket = new Bucket();
        bucket.setName(bucketName);

        storage.buckets().insert(
                PROJECT_ID, bucket).execute();
    }

    /**
     * Deletes a bucket
     *
     * @param bucketName Name of bucket to delete
     * @throws Exception
     */
    public void deleteBucket(String bucketName) throws Exception {
        storage.buckets().delete(bucketName).execute();
    }

    /**
     * Lists the objects in a bucket
     *
     * @param bucketName bucket name to list
     * @return Array of object names
     * @throws Exception
     */
    public List<String> listBucket(String bucketName) throws Exception {

        List<String> list = new ArrayList<String>();

        List<StorageObject> objects = storage.objects().list(bucketName).execute().getItems();
        if (objects != null) {
            for (StorageObject o : objects) {
                list.add(o.getName());
            }
        }

        return list;
    }

    /**
     * List the buckets with the project
     * (Project is configured in properties)
     *
     * @return a list of all buckets
     * @throws Exception
     */
    public List<String> listBuckets() throws Exception {

        List<String> list = new ArrayList<String>();

        List<Bucket> buckets = storage.buckets().list(PROJECT_ID).execute().getItems();
        if (buckets != null) {
            for (Bucket b : buckets) {
                list.add(b.getName());
            }
        }

        return list;
    }

    private Storage getStorage() {

        if (storage == null) {

            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new JacksonFactory();

            List<String> scopes = getStorageScopes();

            storage = new Storage.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }

        return storage;
    }

    /**
     * Return a list of the Google API scopes used by our storage operations.
     * Needed to procure the GoogleAccountCredential.
     * @return list of storage scopes
     */
    public static List<String> getStorageScopes() {
        List<String> scopes = new ArrayList<>();
        scopes.add(StorageScopes.DEVSTORAGE_FULL_CONTROL);
        return scopes;
    }

    /**
     * Listen for and report upload progress
     */
    public static class CloudUploadProgressListener implements MediaHttpUploaderProgressListener {
        private IOProgress ioProgress;
        private long fileSize;

        public CloudUploadProgressListener(IOProgress ioProgress, long fileSize) {
            this.ioProgress = ioProgress;
            this.fileSize = fileSize;
        }

        public void progressChanged(MediaHttpUploader uploader) throws IOException {
            switch (uploader.getUploadState()) {
                case INITIATION_STARTED:
                    Log.i(TAG, "Initiation Started");
                    if (ioProgress != null) {
                        ioProgress.started();
                    }
                    break;
                case INITIATION_COMPLETE:
                    Log.i(TAG, "Initiation Completed");
                    if (ioProgress != null) {
                        ioProgress.initiationCompleted();
                    }
                    break;
                case MEDIA_IN_PROGRESS:
                    // API Javadoc comment says:
                    // Do not use if the specified AbstractInputStreamContent has no content length specified. Instead, consider using getNumBytesUploaded() to denote progress.
                    // are we handling that?
                    int currentProgress = new Double(uploader.getProgress() * 100.0).intValue();
                    long bytesSent = uploader.getNumBytesUploaded();
                    Log.i(TAG, "Upload percentage: " + currentProgress);
                    if (ioProgress != null) {
                        ioProgress.setProgress(currentProgress, bytesSent, fileSize);
                    }
                    break;
                case MEDIA_COMPLETE:
                    Log.i(TAG, "Upload Completed!");
                    if (ioProgress != null) {
                        ioProgress.completed();
                    }
                    break;
            }
        }
    }

    /**
     * Listen for and report download progress
     */
    public static class CloudDownloadProgressListener implements MediaHttpDownloaderProgressListener {
        private IOProgress ioProgress;
        private long fileSize;

        public CloudDownloadProgressListener(IOProgress ioProgress, long fileSize) {
            this.ioProgress = ioProgress;
            this.fileSize = fileSize;
        }

        public void progressChanged(MediaHttpDownloader downloader) throws IOException {
            switch (downloader.getDownloadState()) {
                case MEDIA_IN_PROGRESS:
                    int currentProgress = new Double(downloader.getProgress() * 100.0).intValue();
                    long bytesReceived = downloader.getNumBytesDownloaded();
                    Log.i(TAG, "Download percentage: " + currentProgress);
                    if (ioProgress != null) {
                        ioProgress.setProgress(currentProgress, bytesReceived, fileSize);
                    }
                    break;
                case MEDIA_COMPLETE:
                    Log.i(TAG, "Download Completed!");
                    if (ioProgress != null) {
                        ioProgress.completed();
                    }
                    break;
            }
        }
    }

}
