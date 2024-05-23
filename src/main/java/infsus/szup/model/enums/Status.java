package infsus.szup.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Status {
    NOT_STARTED(1),
    IN_PROGRESS(2),
    IN_REVIEW(3),
    CLOSED(4);

    private final int id;
}
