package com.integ.task.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

import java.text.SimpleDateFormat;

/**
 * Constant utility class
 */
@UtilityClass
public class Constant {
	/**
	 * Logger place holder for one variable
	 */
	public static final String LOGGER_PLACE_HOLDER_1 = "{}";
	/**
	 * Logger place holder for two variables
	 */
	public static final String LOGGER_PLACE_HOLDER_2 = LOGGER_PLACE_HOLDER_1.repeat(2);
	/**
	 * Logger place holder for three variables
	 */
	public static final String LOGGER_PLACE_HOLDER_3 = LOGGER_PLACE_HOLDER_2 + LOGGER_PLACE_HOLDER_1;
	/**
	 * Logger place holder for four variables
	 */
	public static final String LOGGER_PLACE_HOLDER_4 = LOGGER_PLACE_HOLDER_3 + LOGGER_PLACE_HOLDER_1;
	/**
	 * Logger place holder for five variables
	 */
	public static final String LOGGER_PLACE_HOLDER_5 = LOGGER_PLACE_HOLDER_4 + LOGGER_PLACE_HOLDER_1;

	public static final String DATA_NOT_FOUND = "Data not found";
	/**
	 * @deprecated Based on the last discussion regarding this. Use HttpStatus.OK.value() instead which will be more readable
	 */
	@Deprecated(forRemoval = true, since = "20240430")
	public static final Integer RESPONSE_CODE_SUCCESS = HttpStatus.OK.value();
	/**
	 * @deprecated Based on the last discussion regarding this. Use HttpStatus.INTERNAL_SERVER_ERROR.value() instead which will be more readable
	 */
	@Deprecated(forRemoval = true, since = "20240430")
	public static final Integer RESPONSE_CODE_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.value();
	public static final int ENTITY_NOT_FOUND = 1001;

	public static final Long ENUM_TYPE_MEASUREMENT_MODE = 1L;
	public static final Long ENUM_TYPE_PRICE_MODE = 2L;
	public static final Long ENUM_VALUE_PRICE_MODE_SINGLE = 1L;
	public static final Long ENUM_VALUE_PRICE_MODE_SLAB = 2L;
	public static final Long ENUM_TYPE_PAYMENT_MODE = 3L;
	/**
	 * Yet to be implemented
	 */
	public static final String YET_TO_BE_IMPLEMENTED = "Yet to be implemented";

	public static final String IS_ACTIVE = "active";
	/**
	 * Success response message
	 */
	public static final String RESPONSE_MESSAGE_SUCCESS = "Success";


	/**
	 * Date format
	 */
	public static final String DATE_FORMAT = "dd-MMM-yyyy";
	public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT);
	/**
	 * Date time format
	 */
	public static final String DATE_TIME_FORMAT = DATE_FORMAT + " HH:mm:ss";
	/**
	 * Day start time
	 */
	public static final String DAY_START_TIME = "00:00:00";
	/**
	 * Day end time
	 */
	public static final String DAY_END_TIME = "23:59:59";
	/**
	 * Simple date time format
	 */
	public static final SimpleDateFormat SIMPLE_DATE_TIME_FORMAT = new SimpleDateFormat(Constant.DATE_TIME_FORMAT);
	/**
	 * Response object swagger description
	 */
	public static final String SWAGGER_RESPONSE_OBJECT_DESCRIPTION = "<br/><strong>Response:</strong>" +
			"<br/>{" +
			"<br/>\"responseCode\": 200," +
			"<br/>\"responseMessage\": \"string\"," +
			"<br/>\"responseObject\": {}" +
			"<br/>}" +
			"<br/><strong>responseObject</strong> value example inside the response is given below.<br/>";
	/**
	 * Collection Response object swagger description
	 */
	public static final String SWAGGER_COLLECTION_RESPONSE_OBJECT_DESCRIPTION = "<br/><strong>Response:</strong>" +
			"<br/>{" +
			"<br/>\"responseCode\": 200," +
			"<br/>\"responseMessage\": \"string\"," +
			"<br/>\"responseObject\": {}," +
			"<br/>\"totalRecords\": 0" +
			"<br/>}" +
			"<br/><strong>responseObject</strong> value example inside the response is given below.<br/>";
	/**
	 * Valid response message
	 */
	public static final String SWAGGER_VALID_RESPONSE_MESSAGE = "Valid bearer token. <strong>responseObject</strong> value example inside the response is given below.";
	/**
	 * Invalid bearer token message
	 */
	public static final String SWAGGER_INVALID_BEARER_TOKEN_MESSAGE = "Invalid bearer token OR bearer token missing OR token expired.";

	public static final String TRANSACTION_KEY = "B4CB7209DF";

	public static final String AUTHORIZATION_HEADER = "4c756a7c4814ee78dc31d092b06741e6c847d0f11472e3143e96aa76fdd06013c84ed4af90d7807820c7a950f6a431170c0185c2b675da57062d524973906ffc";

	public static final String SALT_KEY = "33DC16E195";

	public static final String SQL_DATE_FORMAT = "yyyy-MMM-dd";

	public static final SimpleDateFormat SIMPLE_SQL_DATE_FORMAT = new SimpleDateFormat(SQL_DATE_FORMAT);

	public static final Long SERVICE_TYPE_PROCEDURE_SERVICES = 15L;

	public static final String PAGINATION_REQUEST = "Pagination Request : ";

	public static final String DATA_FETCH_SUCCESS = "Data Fetch Successfully..";

	public static final String ENTITY_NAME = "entityName";
	public static final String MODEL = "com.hospisoft.model.";

	public static final String DATA_INTEGRITY_VIOLATION_EXCEPTION_MESSAGE = "Record can not be deleted, it's in use.";

	public static final String INTERNAL_SERVER_ERROR_MESSAGE = "An unexpected error occurred.";

}
