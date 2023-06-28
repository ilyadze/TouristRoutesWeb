package ru.alishev.springcourse.FirstSecurityApp.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    private String content;

    public Message() {}

    public Message(Long id, Person person, Topic topic, String content) {
        this.id = id;
        this.person = person;
        this.topic = topic;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
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
        Message message = (Message) o;
        return Objects.equals(id, message.id) && Objects.equals(person, message.person) && Objects.equals(topic, message.topic) && Objects.equals(content, message.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person, topic, content);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", person=" + person +
                ", topic=" + topic +
                ", content='" + content + '\'' +
                '}';
    }

    // Другие поля сообщения

    // Геттеры, сеттеры и другие методы
}
