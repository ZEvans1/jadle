package models;

import java.time.LocalDateTime;

public class Review {

    private String writtenBy;
    private String content;
    private int rating;
    private int id;
    private int restaurantId;

    public Review(String writtenBy, String content,int rating,int restaurantId) {
        this.writtenBy = writtenBy;
        this.content = content;
        this.rating = rating;
        this.restaurantId = restaurantId;
    }

    public String getWrittenBy() {
        return writtenBy;
    }

    public void setWrittenBy(String writtenBy) {
        this.writtenBy = writtenBy;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (rating != review.rating) return false;
        if (id != review.id) return false;
        if (restaurantId != review.restaurantId) return false;
        if (!writtenBy.equals(review.writtenBy)) return false;
        return content.equals(review.content);
    }

    @Override
    public int hashCode() {
        int result = writtenBy.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + rating;
        result = 31 * result + id;
        result = 31 * result + restaurantId;
        return result;
    }
}