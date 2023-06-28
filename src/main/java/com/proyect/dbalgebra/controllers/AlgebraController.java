package com.proyect.dbalgebra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.proyect.dbalgebra.entities.AlgebraRequest;
import com.proyect.dbalgebra.entities.AlgebraRespose;
import com.proyect.dbalgebra.services.ConvertService;

@Controller
public class AlgebraController {

    @Autowired
    private ConvertService convertService;

    @GetMapping("/")
    public String transform(Model model) {
        return "converter";
    }

    @PostMapping("/")
    public ResponseEntity<AlgebraRespose> test(@RequestBody AlgebraRequest algebraInput) throws Exception {

        AlgebraRespose res = convertService.convert(algebraInput.getAlgebra());
        try {
            if (res.isError()) {
                return new ResponseEntity<>(
                        res,
                        HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(
                    res,
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    res,
                    HttpStatus.BAD_REQUEST);
        }
    }
}
