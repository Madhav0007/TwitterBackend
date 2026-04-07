package com.integ.task.service;


import com.integ.task.dto.PaginationRequestDTO;
import com.integ.task.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

/**
 * Entity Service representing methods of an entity
 * @param <M> Model
 * @param <D> DTO
 * @param <I> ID Data type
 */
public interface EntityService<M, D, I> {
	UnsupportedOperationException uoe = new UnsupportedOperationException(Constant.YET_TO_BE_IMPLEMENTED);
	/**
	 * Save DTO to database
	 * @param d DTO object
	 * @return Saved DTO object with ID
	 */
	default D save(D d){
		throw uoe;
	}
	/**
	 * Save DTO changes to database
	 * @param d DTO object
	 * @return Updated DTO object
	 */
	default D update(D d){
		throw uoe;
	}

	/**
	 * Deletes OR deactivates the record from the database
	 * @param i Entity ID
	 */
	default void delete(I i){
		throw uoe;
	}
	/**
	 * Fetches entity by ID
	 * @param id Entity ID
	 * @return Entity with given ID if available else <code>null</code>
	 */
	default D findById(I id){
		throw uoe;
	}

	/**
	 * Finds entities paginated
	 * @param paginationRequestDTO Pagination request options
	 * @return List of entities for the given page
	 */
	default Page<D> paginate(PaginationRequestDTO paginationRequestDTO){
		throw uoe;
	}

	/**
	 * Converts DTO to model object
	 * @param d DTO Object
	 * @return Model object
	 */
	default M dtoToModel(D d){
		throw uoe;
	}

	/**
	 * Copies DTO data to given model object
	 * @param d DTO object
	 * @param m Model object
	 * @return Updated model objet
	 */
	default M dtoToModel(D d, M m){
		throw uoe;
	}
	/**
	 * Converts model to DTO object
	 * @param m Model Object
	 * @return DTO Object
	 */
	default D modelToDto(M m){
		throw uoe;
	}

	/**
	 * Fetches all entities
	 * @return DTO collection of the entity
	 */
	default Collection<D> findAll(){
		throw uoe;
	}

	/**
	 * Provides Care Provider ID
	 * @return Care Provider ID
	 */
	default Long getCareProviderIDP(){
		Jwt userDetailsDto = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userDetailsDto.getClaim("careProviderIDP");
	}
}
