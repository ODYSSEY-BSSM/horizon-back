package odyssey.backend.domain.roadmap.exception;

import odyssey.backend.domain.roadmap.exception.error.RoadmapExceptionProperty;
import odyssey.backend.shared.exception.GlobalException;

public class RoadmapNotFoundException extends GlobalException {
    public RoadmapNotFoundException(){
        super(RoadmapExceptionProperty.ROADMAP_NOT_FOUND);
    }
}
