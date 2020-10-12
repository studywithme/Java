package com.contents.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.contents.domain.BoardLike} entity.
 */
public class BoardLikeDTO implements Serializable {

    private Long id;

    @NotNull
    private Long userId;

    private Instant createdDate;


    private Long boardId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BoardLikeDTO boardLikeDTO = (BoardLikeDTO) o;
        if (boardLikeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), boardLikeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BoardLikeDTO{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", board=" + getBoardId() +
            "}";
    }
}
