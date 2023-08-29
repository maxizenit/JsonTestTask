package ru.maxizenit;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Main {

  private static final String FILE_NAME = "tickets.json";

  private static final String VVO = "VVO";

  private static final String TLV = "TLV";

  private static Map<String, Duration> getMinFlightTimeForCarriers(List<Ticket> tickets) {
    Map<String, Duration> result = new HashMap<>();

    tickets.forEach(
        ticket -> {
          String carrier = ticket.getCarrier();
          Duration currentFlightTime = ticket.getFlightTime();

          if (!result.containsKey(carrier)) {
            result.put(carrier, currentFlightTime);
          } else {
            Duration flightTime = result.get(carrier);
            if (flightTime.compareTo(currentFlightTime) > 0) {
              result.put(carrier, currentFlightTime);
            }
          }
        });

    return result;
  }

  private static double getAveragePrice(List<Ticket> tickets) {
    return tickets.stream().mapToInt(Ticket::getPrice).average().getAsDouble();
  }

  private static double getMedianPrice(List<Ticket> tickets) {
    List<Integer> ticketPrices =
        tickets.stream().mapToInt(Ticket::getPrice).sorted().boxed().toList();
    int ticketPricesCount = ticketPrices.size();

    if (ticketPricesCount % 2 != 0) {
      return ticketPrices.get(ticketPricesCount / 2);
    } else {
      return ((double) ticketPrices.get(ticketPricesCount / 2 - 1)
              + (double) ticketPrices.get(ticketPricesCount / 2))
          / 2;
    }
  }

  public static void main(String[] args) throws IOException {
    File file =
        new File(
            Objects.requireNonNull(Main.class.getClassLoader().getResource(FILE_NAME)).getFile());

    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    List<Ticket> tickets =
        objectMapper.readValue(file, Tickets.class).getTickets().stream()
            .filter(
                ticket ->
                    (VVO.equals(ticket.getOrigin()) && TLV.equals(ticket.getDestination()))
                        || (TLV.equals(ticket.getOrigin()) && VVO.equals(ticket.getDestination())))
            .toList();

    getMinFlightTimeForCarriers(tickets)
        .forEach(
            (carrier, duration) -> {
              long hours = duration.toHours();
              long minutes = duration.toMinutes() % 60;
              System.out.printf(
                  "Минимальное время полёта для %s: %s ч %s мин%n", carrier, hours, minutes);
            });

    double averagePrice = getAveragePrice(tickets);
    double medianPrice = getMedianPrice(tickets);

    System.out.printf("Средняя цена билета: %f%n", averagePrice);
    System.out.printf("Медианная цена билета: %f%n", medianPrice);
    System.out.printf(
        "Разница между средней и медианной ценами: %f%n", Math.abs(averagePrice - medianPrice));
  }
}
