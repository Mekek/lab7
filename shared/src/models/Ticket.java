package models;

//import exceptions.NullArgumentException;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * @author Mikhail Kadilov
 * The 'Ticket' class created to work with tickets
 */
public class Ticket implements Comparable<Ticket>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer id; //ѕоле не может быть null, «начение пол€ должно быть больше 0, «начение этого пол€ должно быть уникальным, «начение этого пол€ должно генерироватьс€ автоматически
    private String name; //ѕоле не может быть null, —трока не может быть пустой
    private Coordinates coordinates; //ѕоле не может быть null
    private Date creationDate; //ѕоле не может быть null, «начение этого пол€ должно генерироватьс€ автоматически
    private Float price; //ѕоле не может быть null, «начение пол€ должно быть больше 0
    private TicketType type; //ѕоле не может быть null
    private Event event; //ѕоле не может быть null

    public Ticket(Integer id, String name, Coordinates coordinates, Date  creationDate, Float price, TicketType type, Event event) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.type = type;
        this.event = event;
    }

    public Ticket() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Date  getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Float getPrice() {
        if (price != null) {
            return price;
        }
        return (float) 0;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", price=" + price +
                ", type=" + type +
                ", event=" + event +
                '}';
    }
    @Override
    public int compareTo(Ticket o) {
        if ((float) price != o.price) {
            return Float.compare(price, o.price);
        }
        if (creationDate.compareTo(o.creationDate) != 0) {
            return creationDate.compareTo(o.creationDate);
        }
        return Long.compare(id, o.id);
    }

//    public void checkTicket(){
//        if (getId() == null) throw new NullArgumentException("id не может быть null");
//        if (getId() <= 0) throw new IllegalArgumentException("id не положительный");
//        if (Objects.equals(getName(),"null") || getName() == null || getName().isEmpty() || getName().isBlank()) throw new NullArgumentException("им€ пустое");
//        if (getCoordinates() == null) throw  new NullArgumentException("пустые координаты");
//        if (getCreationDate() == null)throw new NullArgumentException("нулевое врем€");
//        if (getPrice() <= 0) throw new IllegalArgumentException("стоимость <= 0");
//        if (getType() == null) throw new NullArgumentException("некорректный тип");
//        if (getEvent().getId() <= 0) throw new IllegalArgumentException("id событи€ не положительный " + getEvent().getName());
//        if (getEvent().getName().length() > 1847) throw  new IllegalArgumentException("некорректный формат полного имени");
//        if (getEvent().getDate() == null) throw new NullArgumentException("нулевое врем€");
//        if (getEvent().getMinAge() <= 0) throw  new IllegalArgumentException("минимальный возраст <= 0");
//    }
}


