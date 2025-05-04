package com.denissudak.notifications.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobDetails {
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mma");

    private LocalDate jobDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String address;

    @Override
    public String toString() {
        return jobDate + ", "
                + startTime.format(timeFormatter) + " - "
                + endTime.format(timeFormatter) + ", "
                + address;
    }
}
