package collectionStorageManager;

import clientLogic.ClientHandler;
import clientLogic.PasswordHandler;
import models.*;
import models.handlers.TicketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class PostgreSQLManager implements DatabaseManager {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab7.PostgreSQLManager");

    @Override
    public ArrayList<Ticket> getCollectionFromDatabase() {
        ArrayList<Ticket> data = new ArrayList<>();

        try {
            Properties info = new Properties();
            info.load(this.getClass().getResourceAsStream("/db.cfg"));
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", info);
            Statement statement = connection.createStatement();

            String query = "SELECT t.*, e.name AS event_name, co.x, co.y " +
                    "FROM Ticket t " +
                    "JOIN Event e ON t.event_id = e.id " +
                    "JOIN Coordinates co ON t.coordinates_id = co.id";

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Coordinates coordinates = new Coordinates(resultSet.getDouble("x"), resultSet.getFloat("y"));
                Date creationDate = resultSet.getDate("creation_date");
                Float price = resultSet.getFloat("price");
                TicketType ticketType = TicketType.valueOf(resultSet.getString("ticket_type"));
                Event event = new Event(resultSet.getInt("id"), resultSet.getString("event_name"), resultSet.getDate("event_date"), resultSet.getInt("min_age"));

                Ticket ticket = new Ticket(id, name, coordinates, creationDate, price, ticketType, event);
                data.add(ticket);
            }
            return data;

        } catch (SQLException | IOException e) {
            logger.error("something went wrong during i/o " + ClientHandler.getUserId());
            e.printStackTrace();
        }
        return data;
    }


    @Override
    public void writeCollectionToDatabase() {
        try {
            Properties info = new Properties();
            info.load(this.getClass().getResourceAsStream("/db.cfg"));
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", info);
            connection.setAutoCommit(false);

            // Retrieve all existing ticket IDs from the database
            Set<Long> existingTicketIds = new HashSet<>();
            String getTicketIdsQuery = "SELECT id FROM Ticket";
            PreparedStatement getTicketIdsStatement = connection.prepareStatement(getTicketIdsQuery);
            ResultSet ticketIdsResultSet = getTicketIdsStatement.executeQuery();
            while (ticketIdsResultSet.next()) {
                existingTicketIds.add(ticketIdsResultSet.getLong("id"));
            }

            for (Ticket ticket : TicketHandler.getInstance().getCollection()) {
                if (!existingTicketIds.contains(ticket.getId())) {
                    ticket.setId(addElementToDatabase(ticket, connection));
                }
            }
            connection.commit();
        } catch (SQLException | IOException e) {
            logger.error("something went wrong during i/o ", e);
        }
    }


    public int writeObjectToDatabase(Ticket ticket) {
        int generatedId = -1;
        try {
            Properties info = new Properties();
            info.load(this.getClass().getResourceAsStream("/db.cfg"));
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", info);
            connection.setAutoCommit(false);

            generatedId = addElementToDatabase(ticket, connection);
            connection.commit();
        } catch (SQLException | IOException e) {
            logger.error("something went wrong during i/o ");
        }
        return generatedId;
    }


    public int addElementToDatabase(Ticket ticket, Connection connection) {
        int generatedId = -1;

        try {
            // Insert Coordinates
            String insertCoordinatesQuery = "INSERT INTO Coordinates (x, y) VALUES (?, ?) RETURNING id";
            PreparedStatement insertCoordinatesStatement = connection.prepareStatement(insertCoordinatesQuery);
            insertCoordinatesStatement.setDouble(1, ticket.getCoordinates().getX());
            insertCoordinatesStatement.setFloat(2, ticket.getCoordinates().getY());
            ResultSet coordinatesResultSet = insertCoordinatesStatement.executeQuery();
            int coordinatesId = -1;
            if (coordinatesResultSet.next()) {
                coordinatesId = coordinatesResultSet.getInt(1);
            }

            // Insert Event (name, date, minage)
            String insertEventQuery = "INSERT INTO Event (name, date, minAge) VALUES (?, ?, ?) RETURNING id";
            PreparedStatement insertEventStatement = connection.prepareStatement(insertEventQuery);
//            insertEventStatement.setInt(1, ticket.getEvent().getId());
            insertEventStatement.setString(1, ticket.getEvent().getName());
            insertEventStatement.setDate(2, new java.sql.Date(ticket.getCreationDate().getTime()));
            insertEventStatement.setInt(3, ticket.getEvent().getMinAge());

            ResultSet eventResultSet = insertEventStatement.executeQuery();
            int eventId = -1;
            if (eventResultSet.next()) {
                eventId = eventResultSet.getInt(1);
            }

            // Insert Ticket
            String insertTicketQuery = "INSERT INTO Ticket (name, coordinates_id, creation_date, price, ticket_type, event_id) " +
                    "VALUES (?, ?, ?, ?, CAST(? AS ticket_type_enum), ?) RETURNING id";
            PreparedStatement insertTicketStatement = connection.prepareStatement(insertTicketQuery);
            insertTicketStatement.setString(1, ticket.getName());
            insertTicketStatement.setInt(2, coordinatesId);
            insertTicketStatement.setDate(3, new java.sql.Date(ticket.getCreationDate().getTime()));
            insertTicketStatement.setFloat(4, ticket.getPrice());
            insertTicketStatement.setString(5, ticket.getType().toString());
            insertTicketStatement.setInt(6, eventId);
            ResultSet ticketResultSet = insertTicketStatement.executeQuery();
            if (ticketResultSet.next()) {
                generatedId = ticketResultSet.getInt(1);
            }

            // Insert relationship between the Ticket and the User (Creator)
            String upsertCreatorQuery = "INSERT INTO Creator (ticket_id, user_id) VALUES (?, ?) ON CONFLICT (ticket_id) DO NOTHING";
            PreparedStatement upsertCreatorStatement = connection.prepareStatement(upsertCreatorQuery);
            upsertCreatorStatement.setInt(1, generatedId);
            upsertCreatorStatement.setLong(2, ClientHandler.getUserId());
            upsertCreatorStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error adding element to database", e);
        }
        return generatedId;
    }


    public boolean removeTicketById(Integer ticketId) {
        try {
            Properties info = new Properties();
            info.load(this.getClass().getResourceAsStream("/db.cfg"));
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", info);

            String deleteTicketQuery = "DELETE FROM Ticket WHERE id = ? AND id IN (SELECT ticket_id FROM Creator WHERE user_id = ?)";
            PreparedStatement deleteTicketStatement = connection.prepareStatement(deleteTicketQuery);
            deleteTicketStatement.setInt(1, ticketId);
            deleteTicketStatement.setLong(2, ClientHandler.getUserId());
            int rowsAffected = deleteTicketStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public long authUser(String name, char[] passwd) {
        try {
            Properties info = new Properties();
            info.load(this.getClass().getResourceAsStream("/db.cfg"));
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", info);

            String selectUserQuery = "SELECT id, passwd_hash, passwd_salt FROM \"User\" WHERE name = ?";
            PreparedStatement selectUserStatement = connection.prepareStatement(selectUserQuery);
            selectUserStatement.setString(1, name);
            ResultSet resultSet = selectUserStatement.executeQuery();

            if (resultSet.next()) {
                String passwdHash = resultSet.getString("passwd_hash");
                String passwdSalt = resultSet.getString("passwd_salt");
                String inputPasswdHash = PasswordHandler.hashPassword(passwd, passwdSalt);

                if (passwdHash.equals(inputPasswdHash)) {
                    return resultSet.getLong("id");
                }
            }
        } catch (SQLException | IOException e) {
            logger.error("something went wrong during i/o ");
        }
        return -1;
    }

    public long regUser(String name, char[] passwd) {
        try {
            Properties info = new Properties();
            info.load(this.getClass().getResourceAsStream("/db.cfg"));
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", info);

            // Check if a user with the provided name already exists
            String selectUserQuery = "SELECT COUNT(*) FROM \"User\" WHERE name = ?";
            PreparedStatement selectUserStatement = connection.prepareStatement(selectUserQuery);
            selectUserStatement.setString(1, name);
            ResultSet resultSet = selectUserStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return -1; // A user with the provided name already exists
            }

            // Generate a unique salt for the new user
            SecureRandom random = new SecureRandom();
            byte[] saltBytes = new byte[16];
            random.nextBytes(saltBytes);
            String salt = Base64.getEncoder().encodeToString(saltBytes);

            // Hash the provided password with the generated salt
            String passwdHash = PasswordHandler.hashPassword(passwd, salt);

            // Insert the new user into the "User" table
            String insertUserQuery = "INSERT INTO \"User\" (name, passwd_hash, passwd_salt) VALUES (?, ?, ?)";
            PreparedStatement insertUserStatement = connection.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, name);
            insertUserStatement.setString(2, passwdHash);
            insertUserStatement.setString(3, salt);

            int rowsAffected = insertUserStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = insertUserStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }
        } catch (SQLException | IOException e) {
            logger.error("something went wrong during i/o ");
        }
        return -1;
    }

    public List<Integer> clearTicketsForUser() {
        long userId = ClientHandler.getUserId();
        List<Integer> deletedTicketIds = new ArrayList<>();
        try {
            Properties info = new Properties();
            info.load(this.getClass().getResourceAsStream("/db.cfg"));
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", info);

            // Get ticket IDs that belong to the current user
            String selectTicketIdsQuery = "SELECT ticket_id FROM Creator WHERE user_id = ?";
            PreparedStatement selectTicketIdsStatement = connection.prepareStatement(selectTicketIdsQuery);
            selectTicketIdsStatement.setLong(1, userId);
            ResultSet ticketIdsResultSet = selectTicketIdsStatement.executeQuery();
            while (ticketIdsResultSet.next()) {
                deletedTicketIds.add(ticketIdsResultSet.getInt("ticket_id"));
            }

            // Delete tickets that belong to the current user
            String deleteTicketsQuery = "DELETE FROM Ticket WHERE id IN (SELECT ticket_id FROM Creator WHERE user_id = ?)";
            PreparedStatement deleteTicketsStatement = connection.prepareStatement(deleteTicketsQuery);
            deleteTicketsStatement.setLong(1, userId);
            deleteTicketsStatement.executeUpdate();

        } catch (SQLException | IOException e) {
            logger.error("something went wrong during i/o ");
        }
        return deletedTicketIds;
    }

    public boolean isTicketOwnedByUser(int ticketId) {
        try {
            Properties info = new Properties();
            info.load(this.getClass().getResourceAsStream("/db.cfg"));
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", info);

            String checkOwnershipQuery = "SELECT COUNT(*) FROM Creator WHERE ticket_id = ? AND user_id = ?";
            PreparedStatement checkOwnershipStatement = connection.prepareStatement(checkOwnershipQuery);
            checkOwnershipStatement.setInt(1, ticketId);
            checkOwnershipStatement.setLong(2, ClientHandler.getUserId());
            ResultSet resultSet = checkOwnershipStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count <= 0;
            }
        } catch (SQLException | IOException e) {
            logger.error("something went wrong during i/o ");
        }
        return true;
    }

    public boolean updateTicket(Ticket obj) {
        try {
            Properties info = new Properties();
            info.load(this.getClass().getResourceAsStream("/db.cfg"));
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", info);
            connection.setAutoCommit(false);

            // Update the Event table
            String updateEventQuery = "UPDATE Event SET name = ? FROM Ticket WHERE Ticket.event_id = Event.id AND Ticket.id = ?";
            PreparedStatement updateEventStatement = connection.prepareStatement(updateEventQuery);
            updateEventStatement.setString(1, obj.getEvent().getName());
            updateEventStatement.setLong(2, obj.getId());
            updateEventStatement.executeUpdate();

            // Update the Coordinates table
            String updateCoordinatesQuery = "UPDATE Coordinates SET x = ?, y = ? FROM Ticket WHERE Ticket.coordinates_id = Coordinates.id AND Ticket.id = ?";
            PreparedStatement updateCoordinatesStatement = connection.prepareStatement(updateCoordinatesQuery);
            updateCoordinatesStatement.setDouble(1, obj.getCoordinates().getX());
            updateCoordinatesStatement.setFloat(2, obj.getCoordinates().getY());
            updateCoordinatesStatement.setInt(3, obj.getId());
            updateCoordinatesStatement.executeUpdate();

            // Update the Ticket table
            String updateTicketQuery = "UPDATE Ticket SET name = ?, creation_date = ?, price = ?, ticket_type = CAST(? AS ticket_type_enum) WHERE id = ?";
            PreparedStatement updateTicketStatement = connection.prepareStatement(updateTicketQuery);
            updateTicketStatement.setString(1, obj.getName());
            updateTicketStatement.setDate(2, new java.sql.Date(obj.getCreationDate().getTime()));
            updateTicketStatement.setFloat(3, obj.getPrice());
            updateTicketStatement.setString(4, obj.getType().toString());
            updateTicketStatement.setLong(5, obj.getId());
            updateTicketStatement.executeUpdate();

            connection.commit();
            return true;
        } catch (SQLException | IOException e) {
            logger.error("something went wrong during i/o ");
        }
        return false;
    }
}
