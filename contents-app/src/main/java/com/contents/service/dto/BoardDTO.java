package com.contents.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.contents.domain.Board} entity.
 */
public class BoardDTO implements Serializable {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long CategoryId;

    @NotNull
    @Size(max = 50)
    private String boardType;

    @Size(max = 20)
    private String nickname;

    @Size(max = 200)
    private String profileImageUrl;

    @NotNull
    @Size(max = 200)
    private String title;

    @Size(max = 2000)
    private String content;

    private Integer countView;

    private Instant createdDate;

    private Instant lastModifiedDate;


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

    public Long getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(Long CategoryId) {
        this.CategoryId = CategoryId;
    }

    public String getBoardType() {
        return boardType;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCountView() {
        return countView;
    }

    public void setCountView(Integer countView) {
        this.countView = countView;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BoardDTO boardDTO = (BoardDTO) o;
        if (boardDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), boardDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BoardDTO{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", CategoryId=" + getCategoryId() +
            ", boardType='" + getBoardType() + "'" +
            ", nickname='" + getNickname() + "'" +
            ", profileImageUrl='" + getProfileImageUrl() + "'" +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", countView=" + getCountView() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
