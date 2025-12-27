package com.paintingscollectors.painting.service;

import com.paintingscollectors.painting.model.FavouritePainting;
import com.paintingscollectors.painting.model.Painting;
import com.paintingscollectors.painting.repository.FavouritePaintingRepository;
import com.paintingscollectors.painting.repository.PaintingRepository;
import com.paintingscollectors.user.model.User;
import com.paintingscollectors.web.dto.CreatePaintingRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaintingService {
    private final PaintingRepository paintingRepository;
    private final FavouritePaintingRepository favouritePaintingRepository;

    @Autowired
    public PaintingService(PaintingRepository paintingRepository, FavouritePaintingRepository favouritePaintingRepository) {
        this.paintingRepository = paintingRepository;
        this.favouritePaintingRepository = favouritePaintingRepository;
    }

    public void createNewPainting(CreatePaintingRequest createPaintingRequest, User user) {
        Painting painting = Painting.builder()
                .name(createPaintingRequest.getName())
                .author(createPaintingRequest.getAuthor())
                .style(createPaintingRequest.getStyle())
                .owner(user)
                .imageUrl(createPaintingRequest.getImageUrl())
                .votes(0)
                .build();

        paintingRepository.save(painting);
    }

    public void deletePaintingById(UUID id) {
        paintingRepository.deleteById(id);
    }

    public List<Painting> getOtherPaintings(User user) {
        return paintingRepository.findAll()
                .stream()
                .filter(p -> !p.getOwner().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    public void createFavouritePainting(UUID id, User user) {
        Painting painting = getById(id);

        boolean isAlreadyFavourite = user.getFavouritePaintings()
                .stream()
                .anyMatch(fp -> fp.getName().equals(painting.getName()) && fp.getAuthor().equals(painting.getAuthor()));

        if(isAlreadyFavourite){
            return;
        }

        FavouritePainting favouritePainting = FavouritePainting.builder()
                .name(painting.getName())
                .author(painting.getAuthor())
                .owner(user)
                .imageUrl(painting.getImageUrl())
                .createdOn(LocalDateTime.now())
                .build();

        favouritePaintingRepository.save(favouritePainting);
    }

    private Painting getById(UUID id) {
        return paintingRepository.findById(id).orElseThrow(() -> new RuntimeException("Painting with id "+id+" does not exist."));
    }

    public void incrementVotesByOne(UUID id) {
        Painting painting = getById(id);

        painting.setVotes(painting.getVotes() + 1);
        paintingRepository.save(painting);
    }

    public void deleteFavouritePainting(UUID id) {
        favouritePaintingRepository.deleteById(id);
    }

    public List<Painting> getAllPaintings() {
        return paintingRepository.findAllByOrderByVotesDescNameAsc();
    }
}
