package com.integ.task.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Objects;

@Slf4j
public class StaticMethods {

    // Private constructor to prevent instantiation
    private StaticMethods() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // Google Cloud Storage upload method
    public static void uploadFileToGcs(String bucketName, String objectName, byte[] fileBytes, String contentType) throws Exception {
        try {
            // Load Google credentials from the JSON file in resources folder
            ClassPathResource jsonResource = new ClassPathResource("google-credentials.json");
            InputStream credentialsStream = jsonResource.getInputStream();
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .build()
                    .getService();

            // Create a BlobId and BlobInfo for the file
            BlobId blobId = BlobId.of(bucketName, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();

            // Upload the file to GCS
            storage.create(blobInfo, fileBytes);
            log.debug("Care professional signature File uploaded successfully to GCS: {}", objectName);

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

    public static String getCitizenFullName(String firstName, String middleName, String familyName) {

        return (firstName != null && !firstName.isEmpty() ? firstName + " " : "") +
                (middleName != null && !middleName.isEmpty() ? middleName + " " : "") +
                (familyName != null && !familyName.isEmpty() ? familyName : "");
    }

    public static Long getLongValueObject(Object object) {
        return Objects.nonNull((object)) ? Long.parseLong(object.toString()) : null;
    }

    public static Integer getIntegerValueObject(Object object) {
        return Objects.nonNull((object)) ? Integer.parseInt(object.toString()) : null;
    }
    public static Double getDoubleValueObject(Object object) {
        return Objects.nonNull((object)) ? Double.parseDouble(object.toString()) : null;
    }

    public static String getStringValueObject(Object object) {
        return Objects.nonNull((object)) ? object.toString() : null;
    }

    public static Float getFloatValueObject(Object object) {
        return Objects.nonNull((object)) ? Float.parseFloat(object.toString()) : null;
    }

    public static Boolean getBooleanValueObject(Object object) {
        if (Objects.isNull(object)) {
            return null;
        }
        if (object instanceof Boolean booleanValue) {
            return booleanValue;
        }
        if (object instanceof String stringValue) {
            return Boolean.parseBoolean(stringValue);
        }
        throw new IllegalArgumentException("Object cannot be converted to Boolean: " + object);
    }
    public static String getUTCInstantFormattedDateValue(Object object) {
        try {
            if (object == null) {
                return null;
            }

            String inputString = object.toString();

            // Remove trailing ".0" if present and standardize fractional seconds
            if (inputString.matches(".*\\.\\d+$")) {
                inputString = inputString.replaceAll("\\.(\\d+)$", ".$1"); // Keep fractional seconds
            }

            // Use formatter that allows optional milliseconds
            DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd HH:mm:ss")
                    .optionalStart()
                    .appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
                    .optionalEnd()
                    .toFormatter();

            LocalDateTime localDateTime = LocalDateTime.parse(inputString, inputFormatter);

            // Convert to Instant (UTC)
            Instant instant = localDateTime.toInstant(ZoneOffset.UTC);

            // Format to ISO 8601 with milliseconds and 'Z'
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
                    .withZone(ZoneOffset.UTC);

            return outputFormatter.format(instant);

        } catch (DateTimeParseException e) {
            log.error("Error parsing and formatting UTC instant from object: {}", object, e);
        }
        return null;
    }

}
