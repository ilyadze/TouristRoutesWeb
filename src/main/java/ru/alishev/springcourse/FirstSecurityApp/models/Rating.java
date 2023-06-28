package ru.alishev.springcourse.FirstSecurityApp.models;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Table(name = "Rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    private Comment comment;

    private int value;

    public Rating(int value) {
        this.value = value;
    }

    public Rating(int id, Comment comment, int value) {
        this.id = id;
        this.comment = comment;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Rating(int id, Comment comment) {
        this.id = id;
        this.comment = comment;
    }

    public Rating() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
