package ru.maxizenit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Data;

@Data
public class Ticket {

  private String origin;

  @JsonProperty("origin_name")
  private String originName;

  private String destination;

  @JsonProperty("destination_name")
  private String destinationName;

  @JsonProperty("departure_date")
  @JsonFormat(pattern = "dd.MM.yy")
  private LocalDate departureDate;

  @JsonProperty("departure_time")
  @JsonFormat(pattern = "H:mm")
  private LocalTime departureTime;

  @JsonProperty("arrival_date")
  @JsonFormat(pattern = "dd.MM.yy")
  private LocalDate arrivalDate;

  @JsonProperty("arrival_time")
  @JsonFormat(pattern = "H:mm")
  private LocalTime arrivalTime;

  private String carrier;

  private Integer stops;

  private Integer price;

  public LocalDateTime getDepartureDateTime() {
    return LocalDateTime.of(departureDate, departureTime);
  }

  public LocalDateTime getArrivalDateTime() {
    return LocalDateTime.of(arrivalDate, arrivalTime);
  }

  public Duration getFlightTime() {
    return Duration.between(getDepartureDateTime(), getArrivalDateTime());
  }
}
