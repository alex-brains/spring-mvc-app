package by.alex.spring.utils;

import by.alex.spring.dao.PersonDAO;
import by.alex.spring.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        // 1. Защита от null и пустых строк (базовую валидацию обычно делает @NotEmpty/@Email над полем)
        if (person.getEmail() == null || person.getEmail().isBlank()) {
            return;
        }

        Optional<Person> personInDb = personDAO.personByEmail(person.getEmail());

        // 2. Проверяем, существует ли уже человек с таким email
        if (personInDb.isPresent()) {
            // Если мы создаем нового (id == 0) ИЛИ если редактируем существующего,
            // но найденный в БД person принадлежит ДРУГОМУ id:
            if (person.getId() == 0 || personInDb.get().getId() != person.getId()) {
                errors.rejectValue("email", "", "This email is already in use");
            }
        }
    }
}
