package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {
	
	@InjectMocks
	private MovieService service;

    @Mock
    private MovieRepository repository;


    private PageImpl<MovieEntity> page;
    private Pageable pageable;
    private MovieEntity movie;
    private String existingTitle;
    private Long existingId, nonExistingId;

    @BeforeEach
    void setUp() throws Exception{
        existingTitle = "Test Movie";
        existingId = 1L;
        nonExistingId = 2L;
        movie = MovieFactory.createMovieEntity();
        pageable = PageRequest.of(0,10);
        page = new PageImpl<>(Collections.singletonList(movie));

        Mockito.when(repository.searchByTitle(existingTitle, pageable)).thenReturn(page);
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(movie));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
    }
	
	@Test
	public void findAllShouldReturnPagedMovieDTO() {
        Page<MovieDTO> result = service.findAll(existingTitle, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getContent().getFirst().getTitle(), existingTitle);
	}
	
	@Test
	public void findByIdShouldReturnMovieDTOWhenIdExists() {
        MovieDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
	}
	
	@Test
	public void insertShouldReturnMovieDTO() {
	}
	
	@Test
	public void updateShouldReturnMovieDTOWhenIdExists() {
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
	}
}
