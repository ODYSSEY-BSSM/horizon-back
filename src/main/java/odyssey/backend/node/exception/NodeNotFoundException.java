package odyssey.backend.node.exception;

import odyssey.backend.global.exception.GlobalException;

public class NodeNotFoundException extends GlobalException {
    public NodeNotFoundException(){
        super(NodeExceptionProperty.NODE_NOT_FOUND);
    }
}
