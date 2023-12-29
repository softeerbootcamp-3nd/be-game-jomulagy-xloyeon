package com.example.begame.controller;

import com.example.begame.domain.JsonRequest;
import com.example.begame.service.OmokService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class OmokController {

    private final OmokService omokService;

    @GetMapping("")
    public String main(){
        omokService.initBoard();
        return "index";
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity<Map<String,Boolean>> check(@RequestBody JsonRequest jsonRequest){
        int player = jsonRequest.getPlayer();
        int other = jsonRequest.getOther();
        int xpos = jsonRequest.getX();
        int ypos = jsonRequest.getY();
        omokService.updateBoard(player,xpos,ypos);
        Map<String, Boolean> result = new HashMap<>();
        result.put("is_forbidden",omokService.is_Forbidden(xpos,ypos,player));
        result.put("is_end",omokService.fiveStone(player,xpos,ypos));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
