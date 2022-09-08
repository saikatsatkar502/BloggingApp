package com.blogapp.backend.service.catagory;

import java.util.List;

import com.blogapp.backend.model.Catagory;
import com.blogapp.backend.payloads.CatagoryDto;
import com.blogapp.backend.payloads.PaginationApiResponse;

public interface CatagoryServiceInterface {

    public CatagoryDto saveCatagory(CatagoryDto catagory);

    public CatagoryDto getCatagoryById(int id);

    public CatagoryDto getCatagoryByTitle(String title);

    public CatagoryDto deleteCatagoryById(int id);

    public CatagoryDto updateCatagoryById(int id, CatagoryDto catagory);

    public List<CatagoryDto> getAllCatagory();

    public List<Catagory> getAllCatagoryByTitlesList(List<String> catagoryTitle);

    public CatagoryDto convertCatagoryToCatagaoryDto(Catagory catagory);

    public Catagory convertCatagoryDtoToCatagory(CatagoryDto catagoryDto);

    public PaginationApiResponse getAllCatagoryByPage(int page, int size, String sortBy, String direction);
}
