package com.mikkaeru.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class Problem {

    private final String title;
    private final Integer status;
    private List<Field> fields;
    private final LocalDateTime dateTime;

    public Problem(String title, int status, LocalDateTime dateTime) {
        this.title = title;
        this.status = status;
        this.dateTime = dateTime;
    }

    public Problem(String title, int status, LocalDateTime dateTime, ArrayList<Field> fields) {
        this.title = title;
        this.status = status;
        this.dateTime = dateTime;
        this.fields = fields;
    }

    @JsonInclude(NON_NULL)
    public static class Field {
        private String name;
        private final String message;

        public Field(String name, String message) {
            this.name = name;
            this.message = message;
        }

        public Field(String message) {
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public String getMessage() {
            return message;
        }
    }

    public String getTitle() {
        return title;
    }

    public Integer getStatus() {
        return status;
    }

    public List<Field> getFields() {
        return fields;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
