package com.integ.task.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GcsUtil {

    private GcsUtil() {
        throw new IllegalStateException("Utility class");
    }

    // Initialize the Google Cloud Storage client
    private static Storage initializeGcsClient() throws IOException {

        ClassPathResource jsonResource = new ClassPathResource("google-credentials.json");
        InputStream credentialsStream = jsonResource.getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }

    // Google Cloud Storage upload method
    public static void uploadFileToGcs(String bucketName, String objectName, byte[] fileBytes, String contentType) throws Exception {
        try {
            // Load Google credentials from the JSON file in resources folder
            Storage storage = initializeGcsClient();

            // Create a BlobId and BlobInfo for the file
            BlobId blobId = BlobId.of(bucketName, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();

            // Upload the file to GCS
            storage.create(blobInfo, fileBytes);
            log.debug("File uploaded successfully to GCS: {}", objectName);

        } catch (Exception e) {
            log.error("Failed to upload file to GCS: {}", objectName, e);
            throw new Exception("Error uploading file to GCS", e);
        }
    }

    // Helper method to extract the bucket name from the ftpBasePath URL
    public static String extractBucketName(String ftpBasePath) {
        String[] urlParts = ftpBasePath.split("/");
        return urlParts[3];
    }

    // Get InputStream from Google Cloud Storage
    public static InputStream getInputStreamFromGcs(String bucketName, String objectName) throws IOException {
        Storage storage = initializeGcsClient();
        Blob blob = storage.get(bucketName, objectName);

        if (blob == null || !blob.exists()) {
            log.error("File not found in GCS: " + Constant.LOGGER_PLACE_HOLDER_1, objectName);
            return null;
        }

        // Read the blob's content as InputStream
        ReadChannel reader = blob.reader();
        return Channels.newInputStream(reader);
    }

    public static String cleanPath(String objectName) {
        // Replace any occurrence of multiple slashes (// or more) with a single slash
        return objectName.replaceAll("/+", "/");
    }

    public static String generatePreSignedUrl(String bucketName, String objectName) throws IOException {
        Storage storage = initializeGcsClient();
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName).build();
        URL signedUrl = storage.signUrl(blobInfo, 15, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature());
        return signedUrl.toString();
    }
}
