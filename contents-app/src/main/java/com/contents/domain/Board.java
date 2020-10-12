package com.contents.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Board.
 */
@Entity
@Table(name = "board")
public class Board implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "category_id", nullable = false)
    private Long CategoryId;

    @NotNull
    @Size(max = 50)
    @Column(name = "board_type", length = 50, nullable = false)
    private String boardType;

    @Size(max = 20)
    @Column(name = "nickname", length = 20)
    private String nickname;

    @Size(max = 200)
    @Column(name = "profile_image_url", length = 200)
    private String profileImageUrl;

    @NotNull
    @Size(max = 200)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Size(max = 2000)
    @Column(name = "content", length = 2000)
    private String content;

    @Column(name = "count_view")
    private Integer countView;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @OneToMany(mappedBy = "board")
    private Set<BoardComment> comments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public Board userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCategoryId() {
        return CategoryId;
    }

    public Board CategoryId(Long CategoryId) {
        this.CategoryId = CategoryId;
        return this;
    }

    public void setCategoryId(Long CategoryId) {
        this.CategoryId = CategoryId;
    }

    public String getBoardType() {
        return boardType;
    }

    public Board boardType(String boardType) {
        this.boardType = boardType;
        return this;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }

    public String getNickname() {
        return nickname;
    }

    public Board nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public Board profileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        return this;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public Board title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public Board content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCountView() {
        return countView;
    }

    public Board countView(Integer countView) {
        this.countView = countView;
        return this;
    }

    public void setCountView(Integer countView) {
        this.countView = countView;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Board createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Board lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<BoardComment> getComments() {
        return comments;
    }

    public Board comments(Set<BoardComment> boardComments) {
        this.comments = boardComments;
        return this;
    }

    public Board addComment(BoardComment boardComment) {
        this.comments.add(boardComment);
        boardComment.setBoard(this);
        return this;
    }

    public Board removeComment(BoardComment boardComment) {
        this.comments.remove(boardComment);
        boardComment.setBoard(null);
        return this;
    }

    public void setComments(Set<BoardComment> boardComments) {
        this.comments = boardComments;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Board)) {
            return false;
        }
        return id != null && id.equals(((Board) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Board{" +
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
