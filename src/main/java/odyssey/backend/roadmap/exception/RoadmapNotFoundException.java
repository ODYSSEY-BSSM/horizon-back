package odyssey.backend.roadmap.exception;

import odyssey.backend.global.exception.GlobalException;

public class RoadmapNotFoundException extends GlobalException {
    public RoadmapNotFoundException(){
        super(RoadmapExceptionProperty.ROADMAP_NOT_FOUND);
    }
}
