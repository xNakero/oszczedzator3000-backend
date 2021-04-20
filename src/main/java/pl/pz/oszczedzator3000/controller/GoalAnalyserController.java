package pl.pz.oszczedzator3000.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pz.oszczedzator3000.dto.goalanalyser.GoalAnalyserRequestDto;
import pl.pz.oszczedzator3000.dto.goalanalyser.GoalAnalyserResponseDto;
import pl.pz.oszczedzator3000.service.GoalAnalyserService;

@RestController
@RequestMapping("api/v1")
public class GoalAnalyserController {

    private GoalAnalyserService goalAnalyserService;

    @Autowired
    public GoalAnalyserController(GoalAnalyserService goalAnalyserService) {
        this.goalAnalyserService = goalAnalyserService;
    }

    @RequestMapping("analyser")
    public ResponseEntity<GoalAnalyserResponseDto> getGoalAnalyzerResult(@RequestParam Long userId,
                                                                         @RequestParam Long goalId,
                                                                         @RequestBody GoalAnalyserRequestDto goalAnalyserRequestDto) {
        return new ResponseEntity<>(goalAnalyserService.getGoalAnalyserResult(userId, goalId, goalAnalyserRequestDto),
                HttpStatus.OK);
    }
}
