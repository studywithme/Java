package com.contents.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.contents.domain.BoardComment} entity.
 */
public class BoardCommentDTO implements Serializable {

    private Long id;

    @NotNull
    private Long userId;

    @Size(max = 20)
    private String nickname;

    @Size(max = 200)
    private String profileImageUrl;

    @Size(max = 2000)
    private String content;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

        BoardCommentDTO boardCommentDTO = (BoardCommentDTO) o;
        if (boardCommentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), boardCommentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BoardCommentDTO{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", nickname='" + getNickname() + "'" +
            ", profileImageUrl='" + getProfileImageUrl() + "'" +
            ", content='" + getContent() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", board=" + getBoardId() +
            "}";
    }
}
