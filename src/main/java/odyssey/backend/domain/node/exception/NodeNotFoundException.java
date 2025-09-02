package odyssey.backend.domain.node.exception;

import odyssey.backend.domain.node.exception.error.NodeExceptionProperty;
import odyssey.backend.shared.exception.GlobalException;

public class NodeNotFoundException extends GlobalException {
    public NodeNotFoundException(){
        super(NodeExceptionProperty.NODE_NOT_FOUND);
    }
}
