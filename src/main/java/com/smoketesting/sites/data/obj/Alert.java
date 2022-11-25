package com.smoketesting.sites.data.obj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Alert {
    private Date date;
    private String message;

    public Alert(String message) {
        this.date = new Date();
        this.message = message;
    }
}
