package by.alex.spring.dao;

import by.alex.spring.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
    private static int PEOPLE_COUNT;

    /*private static final String URL = "jdbc:postgresql://localhost:5432/test_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    private static Connection connection;
    static
    {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    /*private List<Person> people;

    {
        people = new ArrayList<>()
        {{
            add(new Person(++PEOPLE_COUNT, "Tom", 35, "adfa@mail.ru"));
            add(new Person(++PEOPLE_COUNT, "Jack", 27, "jgldaj@gmail.com"));
            add(new Person(++PEOPLE_COUNT, "Marry", 19, "sd;kjf@mail.ru"));
            add(new Person(++PEOPLE_COUNT, "Anna", 22, "iweurtq@gmail.com"));
            add(new Person(++PEOPLE_COUNT, "Margo", 24, "ad;f@mail.ru"));
        }};
    }*/

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> allPeople() {
        //return people;

        /*List<Person> people = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM person");

            while(resultSet.next()) {
                Person person = new Person();

                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));

                people.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return people;*/

        return jdbcTemplate.query("SELECT * FROM person",
                new BeanPropertyRowMapper<>(Person.class)/* (or) new PersonMapper()*/);
    }

    public Optional<Person> personByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM person WHERE email=?", new Object[]{email},
                        new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }

    public Person personById(int id) {
        /*return people.stream()
                .filter(person -> person.getId() == id)
                .findAny()
                .orElse(null);*/

        /*Person person = null;
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM person WHERE id=?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            person = new Person();
            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setAge(resultSet.getInt("age"));
            person.setEmail(resultSet.getString("email"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;*/

        return jdbcTemplate.query("SELECT * FROM person WHERE id=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(Person.class) /* (or) new PersonMapper()*/)
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        /*person.setId(++PEOPLE_COUNT);
        people.add(person);*/

        /*try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO person VALUES (" + 1 + ", '" + person.getName() +
                    "', " + person.getAge() + ", '" + person.getEmail() + "')");
            //INSERT INTO person VALUES (1, 'Tom', 35, 'asdfas@mail.ru')
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

       /* try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO person VALUES (1, ?, ?, ?)");

            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, person.getAge());
            preparedStatement.setString(3, person.getEmail());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        jdbcTemplate.update("INSERT INTO person(name, age, email, address) VALUES (?, ?, ?, ?)",
                person.getName(), person.getAge(), person.getEmail(), person.getAddress());
    }

    public void update(int id, Person updatedPerson) {
        /*people.stream()
                .filter(oldPerson -> oldPerson.getId() == id)
                .findAny()
                .ifPresent(oldPerson -> oldPerson.setName(updatedPerson.getName()));*/

        /*Person oldPerson = personById(id);
        oldPerson.setName(updatedPerson.getName());
        oldPerson.setAge(updatedPerson.getAge());
        oldPerson.setEmail(updatedPerson.getEmail());*/

        /*try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE person SET name=?, age=?, email=? WHERE id=?");
            preparedStatement.setString(1, updatedPerson.getName());
            preparedStatement.setInt(2, updatedPerson.getAge());
            preparedStatement.setString(3, updatedPerson.getEmail());
            preparedStatement.setInt(4, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        jdbcTemplate.update("UPDATE person SET name=?, age=?, email=?, address=? WHERE id=?",
                updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(), updatedPerson.getAddress(), id);
    }

    public void delete(int id) {
        //people.removeIf(person -> person.getId() == id);

        /*try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("DELETE FROM person WHERE id=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
    }

    public void makeAdmin(int id) {
        jdbcTemplate.update("UPDATE person SET is_admin=? WHERE id=?", true, id);
    }

    ///////////////////////////
    /////Test batch update/////
    ///////////////////////////

    public void multipleUpdate() {
        List<Person> people = create1000People();

        long before = System.currentTimeMillis();

        for (Person person : people) {
            jdbcTemplate.update("INSERT INTO person(name, age, email, address) VALUES (?, ?, ?, ?)",
                    person.getId(), person.getName(), person.getAge(), person.getEmail(), person.getAddress());
        }


        long after = System.currentTimeMillis();
        System.out.println("Time: " + (after - before));
    }

    public void batchUpdate() {
        List<Person> people = create1000People();

        long before = System.currentTimeMillis();

        jdbcTemplate.batchUpdate("INSERT INTO person(name, age, email, address) VALUES (?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, people.get(i).getId());
                        ps.setString(2, people.get(i).getName());
                        ps.setInt(3, people.get(i).getAge());
                        ps.setString(4, people.get(i).getEmail());
                        ps.setString(5, people.get(i).getAddress());
                    }

                    @Override
                    public int getBatchSize() {
                        return people.size();
                    }
                });

        long after = System.currentTimeMillis();
        System.out.println("Time: " + (after - before));
    }

    private List<Person> create1000People() {
        List<Person> people = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            people.add(new Person(i, "Name" + i, 30, "test" + i + "@mail.ru", "some" + i + "address"));
        }

        return people;
    }
}
