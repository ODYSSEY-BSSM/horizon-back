package odyssey.backend.roadmap.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.roadmap.dto.RoadmapRequest;
import odyssey.backend.roadmap.dto.RoadmapResponse;
import odyssey.backend.roadmap.service.RoadmapService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roadmap")
public class RoadmapController {

    private final RoadmapService roadmapService;

    @GetMapping("/all")
    public List<RoadmapResponse> getAllRoadmaps() {
        return roadmapService.findAllRoadmaps();
    }

    @PostMapping("/create")
    public RoadmapResponse createRoadmap( @Valid RoadmapRequest request) {
        return roadmapService.save(request);
    }

    @PutMapping("/update/{id}")
    public RoadmapResponse updateRoadmap(@PathVariable Long id, @RequestBody @Valid RoadmapRequest request) {
        return roadmapService.update(id, request);
    }


    @DeleteMapping("/delete/{id}")
    public void deleteRoadmap(@PathVariable Long id) {
        roadmapService.deleteRoadmapById(id);
    }

}
