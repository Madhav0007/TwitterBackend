package com.integ.task.controller;

import com.integ.task.dto.CollectionResponseDto;
import com.integ.task.dto.MultiColumnSearchAndPaginationRequestDto;
import com.integ.task.dto.PaginationRequestDTO;
import com.integ.task.dto.ResponseDto;
import com.integ.task.service.EntityService;
import com.integ.task.util.Constant;
import com.integ.task.util.LoggerUtil;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * Base controller to handle common operations
 * @param <S> Service
 * @param <D> DTO
 * @param <M> Model
 * @param <I> ID Type
 */
@Slf4j
public abstract class BaseController<S extends EntityService<M, D, I>, M, D, I> {
	/**
	 * Logger Util
	 */
	protected final LoggerUtil loggerUtil;
	/**
	 * Price list details service
	 */
	protected final S service;
	/**
	 * End point name
	 */
	protected final String endPointName;

	/**
	 * Constructor
	 * @param loggerUtil Logger util
	 * @param service Service
	 * @param endPointName End point name
	 */
	protected BaseController(LoggerUtil loggerUtil, S service, String endPointName){
		this.loggerUtil = loggerUtil;
		this.service = service;
		this.endPointName = endPointName;
	}
	/**
	 * Provides anonymous end point
	 * @return Anonymous end point response
	 */
	@Operation(
			summary = "Fetch public API details",
			description = """
                This endpoint returns a public API message for the specified endpoint name.
                """
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Public API details fetched successfully.",
					content = @Content(
							mediaType = "application/json"
					)
			)
	})
	@Hidden
	@GetMapping("/public")
	public ResponseEntity<ResponseDto> getPublicApi() {
		logRequest(Thread.currentThread().getStackTrace());
		return ResponseEntity.ok(ResponseDto.builder()
				.responseCode(HttpStatus.OK.value())
				.responseObject(String.format("%s Public API", endPointName))
				.build());
	}
	/**
	 * Saves the given entity
	 * @param dto DTO
	 * @return Saved entity
	 */
	@Operation(
			summary = "Save data for entity",
			description = """
                This endpoint saves the data for the provided entity.
                """
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Entity saved successfully.",
					content = @Content(
							mediaType = "application/json"
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error occurred while saving the entity.",
					content = @Content(
							mediaType = "application/json"
					)
			)
	})
	@PostMapping
	protected ResponseEntity<ResponseDto>  save(@RequestBody D dto){
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		logRequest(Thread.currentThread().getStackTrace());
		try{
			return ResponseEntity.ok(ResponseDto.builder()
					.responseCode(HttpStatus.OK.value())
					.responseObject(service.save(dto))
					.responseMessage(endPointName + " Added Successfully")
					.build());
		}catch(Exception e){
			log.error(Constant.LOGGER_PLACE_HOLDER_3, getLogLocation(stackTraceElements), e);
			return ResponseEntity.ok(ResponseDto.builder()
					.responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
					//.responseObject(e.getMessage())
					.responseMessage(e.getMessage())
					.build());
		}
	}
	/**
	 * Find all
	 * @return List of all records
	 */
	@Operation(
			summary = "Fetch all entities",
			description = """
                This endpoint fetches all available entities.
                """
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Entities fetched successfully.",
					content = @Content(
							mediaType = "application/json"
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error occurred while fetching the entities.",
					content = @Content(
							mediaType = "application/json"
					)
			)
	})
	@GetMapping
	protected ResponseEntity<ResponseDto> findAll(){
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		logRequest(Thread.currentThread().getStackTrace());
		try {
			Collection<D> entityList = service.findAll();
			return ResponseEntity.ok(CollectionResponseDto.builder()
					.responseCode(HttpStatus.OK.value())
					.responseObject(entityList)
					.totalRecords((long) entityList.size())
					.build());
		}catch(Exception e){
			log.error(Constant.LOGGER_PLACE_HOLDER_3, getLogLocation(stackTraceElements), e);
			return ResponseEntity.ok(ResponseDto.builder()
					.responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
					//.responseObject(e.getMessage())
					.responseMessage(e.getMessage())
					.build());
		}
	}
	/**
	 * Provides paginated response with given parameters
	 * @param paginationRequestDTO Pagination parameters
	 * @return Entities matching with the pagination criteria
	 */
	@Operation(
			summary = "Paginate records based on request parameters",
			description = """
                This API retrieves paginated records based on the criteria provided in the request.
                """
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Data retrieved successfully.",
					content = @Content(
							mediaType = "application/json"
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error occurred while paginating request parameters details.",
					content = @Content(
							mediaType = "application/json"
					)
			)
	})
	@PostMapping("/page")
	protected ResponseEntity<ResponseDto> paginate(@RequestBody PaginationRequestDTO paginationRequestDTO){
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		logRequest(Thread.currentThread().getStackTrace());
		try{
			Page<D> entitiesInPage = service.paginate(paginationRequestDTO);
			return ResponseEntity.ok(
					CollectionResponseDto.builder()
							.responseCode(HttpStatus.OK.value())
							.responseMessage("Data retrieved successfully")
							.responseObject(ObjectUtils.isEmpty(entitiesInPage) ? List.of() : entitiesInPage.stream().toList())
							.totalRecords(ObjectUtils.isEmpty(entitiesInPage) ? 0 : entitiesInPage.getTotalElements())
							.build());
		}catch(Exception e){
			log.error(Constant.LOGGER_PLACE_HOLDER_2, getLogLocation(stackTraceElements), e);
			return ResponseEntity.ok(ResponseDto.builder()
					.responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
					.responseObject(e.getMessage())
					.build());
		}
	}
	@Operation(
			summary = "Paginate and multi-column search for request parameters",
			description = """
                This endpoint fetches paginated data of request parameters with multi-column search
                based on the provided pagination and search parameters.
                """
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Data retrieved successfully.",
					content = @Content(
							mediaType = "application/json"
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error occurred while fetching or paginating request parameters details.",
					content = @Content(
							mediaType = "application/json"
					)
			)
	})
	@PostMapping("/page-multi-search")
	protected ResponseEntity<ResponseDto> paginate(@RequestBody MultiColumnSearchAndPaginationRequestDto paginationRequestDTO){
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		logRequest(Thread.currentThread().getStackTrace());
		try{
			Page<D> entitiesInPage = service.paginate(paginationRequestDTO);
			return ResponseEntity.ok(
					CollectionResponseDto.builder()
							.responseCode(HttpStatus.OK.value())
							.responseMessage("Data retrieved successfully")
							.responseObject(ObjectUtils.isEmpty(entitiesInPage) ? List.of() : entitiesInPage.stream().toList())
							.totalRecords(ObjectUtils.isEmpty(entitiesInPage) ? 0 : entitiesInPage.getTotalElements())
							.build());
		}catch(Exception e){
			log.error(Constant.LOGGER_PLACE_HOLDER_2, getLogLocation(stackTraceElements), e);
			return ResponseEntity.ok(ResponseDto.builder()
					.responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
					//.responseObject(e.getMessage())
					.responseMessage(e.getMessage())
					.build());
		}
	}
	/**
	 * Fetches entity by given ID
	 * @param id Entity ID
	 * @return Entity with given ID
	 */
	@Operation(
			summary = "Find entity by ID",
			description = """
                This endpoint retrieves a specific entity by its unique ID.
                """
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Entity retrieved successfully.",
					content = @Content(
							mediaType = "application/json"
					)
			),
			@ApiResponse(
					responseCode = "404",
					description = "No entity found with the provided ID.",
					content = @Content(
							mediaType = "application/json"
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error occurred while fetching the entity by ID.",
					content = @Content(
							mediaType = "application/json"
					)
			)
	})
	@GetMapping("/{id}")
	protected ResponseEntity<ResponseDto> findById(@PathVariable I id){
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		logRequest(Thread.currentThread().getStackTrace());
		try{
			return ResponseEntity.ok(ResponseDto.builder()
					.responseCode(HttpStatus.OK.value())
					.responseObject(service.findById(id))
					.build());
		}catch(EntityNotFoundException e){
			log.error(Constant.LOGGER_PLACE_HOLDER_5, getLogLocation(stackTraceElements), "No entity found with ID : ", id, "Error : ", e);
			return ResponseEntity.ok(ResponseDto.builder()
					.responseCode(HttpStatus.NOT_FOUND.value())
					//.responseObject(e.getMessage())
					.responseMessage(e.getMessage())
					.build());
		}catch(Exception e){
			log.error(Constant.LOGGER_PLACE_HOLDER_3, getLogLocation(stackTraceElements), e);
			return ResponseEntity.ok(ResponseDto.builder()
					.responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
					//.responseObject(e.getMessage())
					.responseMessage(e.getMessage())
					.build());
		}
	}
	/**
	 * Updates given entity
	 * @param dto DTO
	 * @return Updated entity
	 */
	@Operation(
			summary = "Update an existing entity",
			description = """
                This endpoint updates an existing entity with the provided data.
                """
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Entity updated successfully.",
					content = @Content(
							mediaType = "application/json"
					)
			),
			@ApiResponse(
					responseCode = "404",
					description = "No entity found with the provided ID to update.",
					content = @Content(
							mediaType = "application/json"
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error occurred while updating the entity.",
					content = @Content(
							mediaType = "application/json"
					)
			)
	})
	@PutMapping
	protected ResponseEntity<ResponseDto> update(@RequestBody D dto){
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		logRequest(Thread.currentThread().getStackTrace());
		try {
			return ResponseEntity.ok(ResponseDto.builder()
					.responseCode(HttpStatus.OK.value())
					.responseObject(service.update(dto))
					.responseMessage(endPointName + " Updated Successfully")
					.build());
		}catch(EntityNotFoundException e){
			log.error(Constant.LOGGER_PLACE_HOLDER_5, getLogLocation(stackTraceElements), "No entity found with ID : ", "{ID to display}", "Error : ", e);
			return ResponseEntity.ok(ResponseDto.builder()
					.responseCode(HttpStatus.NOT_FOUND.value())
					//.responseObject(e.getMessage())
					.responseMessage(e.getMessage())
					.build());
		}
		catch(Exception e){
			log.error(Constant.LOGGER_PLACE_HOLDER_3, getLogLocation(stackTraceElements), e);
			return ResponseEntity.ok(ResponseDto.builder()
					.responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
					//.responseObject(e.getMessage())
					.responseMessage(e.getMessage())
					.build());
		}
	}
	/**
	 * Deletes entity with given ID
	 * @param id Entity ID
	 * @return Success response
	 */
	@Operation(
			summary = "Delete an entity by ID",
			description = """
                This endpoint deletes an entity based on the provided ID.
                """
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Entity deleted successfully.",
					content = @Content(
							mediaType = "application/json"
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error occurred while deleting the entity.",
					content = @Content(
							mediaType = "application/json"
					)
			)
	})
	@DeleteMapping("/{id}")
	protected ResponseEntity<ResponseDto> delete(@PathVariable I id){
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		logRequest(stackTraceElements);
		try {
			service.delete(id);
			return ResponseEntity.ok(ResponseDto.builder()
					.responseCode(HttpStatus.OK.value())
					.responseMessage(endPointName + " Deleted Successfully")
					//.responseObject("Successfully Deleted")
					//.responseMessage("Successfully Deleted")
					.build());
		}catch(DataIntegrityViolationException e){
			log.error(Constant.LOGGER_PLACE_HOLDER_3, getLogLocation(stackTraceElements), e);
			return ResponseEntity.ok(ResponseDto.builder()
					.responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
					.responseMessage("Record can not be deleted, it's in use.")
					.build());
		}catch(Exception e){
			log.error(Constant.LOGGER_PLACE_HOLDER_3, getLogLocation(stackTraceElements), e);
			return ResponseEntity.ok(ResponseDto.builder()
					.responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
					//.responseObject(e.getMessage())
					.responseMessage(e.getMessage())
					.build());
		}
	}

	/**
	 * Gets log location
	 * @param stackTraceElements Stack trace elements
	 * @return Provides log location
	 */
	protected String getLogLocation(StackTraceElement[] stackTraceElements) {
		return loggerUtil.getLogLocation(getClass().getName(), stackTraceElements);
	}

	/**
	 * Logs the method entry point
	 * @param stackTraceElements Stack trace elements
	 */
	protected void logRequest(StackTraceElement[] stackTraceElements){
		log.debug(Constant.LOGGER_PLACE_HOLDER_1, getLogLocation(stackTraceElements));
	}
}
