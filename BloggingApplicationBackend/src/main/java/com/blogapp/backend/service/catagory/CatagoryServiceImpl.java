package com.blogapp.backend.service.catagory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.blogapp.backend.exception.MethodArgumentsNotFound;
import com.blogapp.backend.exception.ResourceAlreadyExists;
import com.blogapp.backend.exception.ResourceNotFoundException;
import com.blogapp.backend.model.Catagory;
import com.blogapp.backend.payloads.CatagoryDto;
import com.blogapp.backend.payloads.PaginationApiResponse;
import com.blogapp.backend.repo.CatagoryRepository;

@Service
public class CatagoryServiceImpl implements CatagoryServiceInterface {

    private static final Logger LOGGER = LogManager.getLogger(CatagoryServiceImpl.class);

    private static final String CATAGORY = "catagory";
    private static final String TITLE = "title";
    private static final String INVALID_ID = "Invalid id : {}";
    private static final String CATAGORY_NOT_FOUND_ID = "Catagory not found with id : {}";
    private static final String CATAGORY_FOUND_ID = "Catagory found with id : {}";
    private static final String CATAGORY_NOT_FOUND_TITLE = "Catagory not found with title : {}";
    private static final String CATAGORY_FOUND_TITLE = "Catagory found with title : {}";
    private static final String CATAGORY_DELETED = "Catagory deleted with id : {}";

    @Autowired
    private CatagoryRepository catagoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CatagoryDto saveCatagory(CatagoryDto catagory) {

        if (catagory != null) {
            if (this.catagoryRepository.findByTitleIgnoreCase(catagory.getTitle()) == null) {
                Catagory catagoryEntity = this.convertCatagoryDtoToCatagory(catagory);

                return convertCatagoryToCatagaoryDto(this.catagoryRepository.save(catagoryEntity));
            }
            throw new ResourceAlreadyExists(CATAGORY, TITLE, catagory.getTitle());
        }

        throw new ResourceNotFoundException(CATAGORY, "save Catagory", catagory);
    }

    @Override
    public CatagoryDto getCatagoryById(int id) {

        if (id > 0) {
            Catagory catagory = this.catagoryRepository.findById(id).orElse(null);

            if (catagory != null) {
                LOGGER.info(CATAGORY_FOUND_ID, id);
                return this.convertCatagoryToCatagaoryDto(catagory);
            }
            LOGGER.error(CATAGORY_NOT_FOUND_ID, id);
            throw new ResourceNotFoundException(CATAGORY, "Id", id);
        }
        LOGGER.error(INVALID_ID, id);
        throw new MethodArgumentsNotFound("Id", "find catagory by id", id);
    }

    @Override
    public CatagoryDto getCatagoryByTitle(String title) {

        if (!title.isEmpty()) {
            Catagory catagory = this.catagoryRepository.findByTitleIgnoreCase(title);

            if (catagory != null) {
                LOGGER.info(CATAGORY_FOUND_TITLE, title);
                return this.convertCatagoryToCatagaoryDto(catagory);
            }
            LOGGER.error(CATAGORY_NOT_FOUND_TITLE, title);
            throw new ResourceNotFoundException(CATAGORY, TITLE, title);
        }
        LOGGER.error("Title is empty");
        throw new MethodArgumentsNotFound(TITLE, "find catagory by Title", title);
    }

    @Override
    public CatagoryDto deleteCatagoryById(int id) {

        if (id > 0) {
            Catagory catagory = this.catagoryRepository.findById(id).orElseThrow(() -> {
                LOGGER.error(CATAGORY_NOT_FOUND_ID, id);
                return new ResourceNotFoundException(CATAGORY, "Id", id);
            });

            if (catagory != null) {
                this.catagoryRepository.delete(catagory);
                LOGGER.info(CATAGORY_DELETED, id);
                return this.convertCatagoryToCatagaoryDto(catagory);
            }
        }
        LOGGER.error(INVALID_ID, id);
        throw new MethodArgumentsNotFound("Id", "delete catagory by id", id);

    }

    @Override
    public CatagoryDto updateCatagoryById(int id, CatagoryDto catagory) {

        if (id > 0) {
            Catagory oldCatagory = this.catagoryRepository.findById(id).orElseThrow(() -> {
                LOGGER.error(CATAGORY_NOT_FOUND_ID, id);
                return new ResourceNotFoundException(CATAGORY, "Id", id);
            });

            if (oldCatagory != null && catagory.getTitle().equals(oldCatagory.getTitle())) {
                oldCatagory.setDescription(catagory.getDescription());
                return this.convertCatagoryToCatagaoryDto(this.catagoryRepository.save(oldCatagory));
            }
            throw new ResourceNotFoundException(CATAGORY, TITLE, catagory.getTitle());
        }

        LOGGER.error(INVALID_ID, id);
        throw new MethodArgumentsNotFound("Id", "update catagory by id", id);

    }

    @Override
    public List<CatagoryDto> getAllCatagory() {
        List<CatagoryDto> catagoryDtos = new ArrayList<>();
        List<Catagory> catagories = this.catagoryRepository.findAll();
        if (!catagories.isEmpty()) {
            for (Catagory catagory : catagories) {
                catagoryDtos.add(this.convertCatagoryToCatagaoryDto(catagory));
            }
            LOGGER.info("Catagories found : {}", catagoryDtos.size());
            return catagoryDtos;
        }
        LOGGER.info("No catagories found");
        return Collections.emptyList();
    }

    @Override
    public CatagoryDto convertCatagoryToCatagaoryDto(Catagory catagory) {

        return modelMapper.map(catagory, CatagoryDto.class);
    }

    @Override
    public Catagory convertCatagoryDtoToCatagory(CatagoryDto catagoryDto) {
        return modelMapper.map(catagoryDto, Catagory.class);
    }

    @Override
    public List<Catagory> getAllCatagoryByTitlesList(List<String> catagoryTitle) {
        List<Catagory> catagories = new ArrayList<>();
        for (String title : catagoryTitle) {
            catagories.add(this.catagoryRepository.findByTitleIgnoreCase(title));
        }
        return catagories;
    }

    @Override
    public PaginationApiResponse getAllCatagoryByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Catagory> catagories = this.catagoryRepository.findAll(pageable);
        if (!catagories.hasContent()) {
            LOGGER.error("No catagories found");
            throw new ResourceNotFoundException(CATAGORY, "page", page);
        }
        List<Object> catagoryList = catagories.getContent().stream()
                .map(this::convertCatagoryToCatagaoryDto).collect(Collectors.toList());
        PaginationApiResponse paginationApiResponse = new PaginationApiResponse();
        paginationApiResponse.setTotalPages(catagories.getTotalPages());
        paginationApiResponse.setTotalElements(catagories.getTotalElements());
        paginationApiResponse.setPage(catagories.getNumber());
        paginationApiResponse.setSize(catagories.getSize());
        paginationApiResponse.setLastPage(catagories.isLast());
        paginationApiResponse.setContent(catagoryList);
        return paginationApiResponse;
    }

}
