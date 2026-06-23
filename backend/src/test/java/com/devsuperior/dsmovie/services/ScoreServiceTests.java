package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;

    @Mock
    private UserService userService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ScoreRepository scoreRepository;

    private UserEntity user;
    private MovieEntity movie;
    private ScoreEntity score;
    private ScoreDTO scoreDTO;
    private Long  nonExistingId;
    private Double value;

    @BeforeEach
    void setUp() throws Exception{

        nonExistingId = 2L;
        value = 4.0;

        user = UserFactory.createUserEntity();
        movie = MovieFactory.createMovieEntity();
        score = ScoreFactory.createScoreEntity();
        scoreDTO = ScoreFactory.createScoreDTO();


        Mockito.when(userService.authenticated()).thenReturn(user);
        Mockito.when(movieRepository.findById(scoreDTO.getMovieId())).thenReturn(Optional.of(movie));
        Mockito.when(movieRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        Mockito.when(movieRepository.save(any())).thenReturn(movie);

        Mockito.when(scoreRepository.saveAndFlush(any())).thenReturn(score);

    }
	
	@Test
	public void saveScoreShouldReturnMovieDTO() {
        MovieDTO result = service.saveScore(scoreDTO);

        Assertions.assertNotNull(result);

        Mockito.verify(userService).authenticated();
        Mockito.verify(movieRepository).findById(scoreDTO.getMovieId());
        Mockito.verify(scoreRepository).saveAndFlush(any());
        Mockito.verify(movieRepository).save(any());
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {

        ScoreDTO dto = ScoreFactory.createScoreDTO(nonExistingId, value);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.saveScore(dto);
        });

        Mockito.verify(movieRepository).findById(nonExistingId);
	}
}
