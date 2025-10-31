package br.com.cinema.cinemasystem.controller;

import br.com.cinema.cinemasystem.model.Movie;
import br.com.cinema.cinemasystem.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;


import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MovieService movieService;

    @GetMapping("/")
    public String Homepage(Model model){
        List<Movie> movies = movieService.getplayingMovies();
        model.addAttribute("movies", movies);
        return "index";
    }

}
