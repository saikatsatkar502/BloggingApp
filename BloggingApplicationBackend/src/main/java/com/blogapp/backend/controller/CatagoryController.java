package com.blogapp.backend.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogapp.backend.exception.ResourceNotFoundException;
import com.blogapp.backend.payloads.CatagoryDto;
import com.blogapp.backend.payloads.PaginationApiResponse;
import com.blogapp.backend.service.catagory.CatagoryServiceImpl;

@RestController
@RequestMapping("/catagory")
public class CatagoryController {

    private static final Logger LOGGER = LogManager.getLogger(CatagoryController.class);

    @Autowired
    private CatagoryServiceImpl catagoryService;

    @GetMapping("/get-all")
    public ResponseEntity<List<CatagoryDto>> getAllCatagory() {
        LOGGER.info("Getting all catagory");
        List<CatagoryDto> catagoryList = this.catagoryService.getAllCatagory();
        if (!catagoryList.isEmpty()) {
            return ResponseEntity.ok(catagoryList);
        }
        throw new ResourceNotFoundException("No catagories found");
    }

    @GetMapping("/get-by-title/{title}")
    public ResponseEntity<CatagoryDto> getCatagoryByTitle(@PathVariable String title) {
        LOGGER.info("Getting catagory by title");
        return ResponseEntity.ok(this.catagoryService.getCatagoryByTitle(title));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<CatagoryDto> getCatagoryById(@PathVariable int id) {
        LOGGER.info("Getting catagory by id");
        return ResponseEntity.ok(this.catagoryService.getCatagoryById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<CatagoryDto> saveCatagory(@Valid @RequestBody CatagoryDto catagory) {
        LOGGER.info("Saving catagory");
        return ResponseEntity.ok(this.catagoryService.saveCatagory(catagory));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CatagoryDto> updateCatagory(@PathVariable int id, @Valid @RequestBody CatagoryDto catagory) {
        LOGGER.info("Updating catagory");
        return ResponseEntity.ok(this.catagoryService.updateCatagoryById(id, catagory));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CatagoryDto> deleteCatagory(@PathVariable int id) {
        LOGGER.info("Deleting catagory");
        return ResponseEntity.ok(this.catagoryService.deleteCatagoryById(id));
    }

    @GetMapping("/get-by-page")
    public ResponseEntity<PaginationApiResponse> getAllCatagoryByPage(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo) {
        LOGGER.info("Getting all catagory by page");
        return ResponseEntity.ok(this.catagoryService.getAllCatagoryByPage(pageNo, pageSize));
    }

}
