package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class MemoryVacancyRepository implements VacancyRepository {

    private final AtomicInteger nextId = new AtomicInteger(0);

    private final ConcurrentMap<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "Internship for Java Developer", LocalDateTime.now(), true, 0));
        save(new Vacancy(0, "Junior Java Developer", "Junior position for Java Developer", LocalDateTime.now(), true, 0));
        save(new Vacancy(0, "Junior+ Java Developer", "Junior+ position for Java Developer", LocalDateTime.now(), true, 0));
        save(new Vacancy(0, "Middle Java Developer", "Middle position for Java Developer", LocalDateTime.now(), true, 0));
        save(new Vacancy(0, "Middle+ Java Developer", "Middle+ position for Java Developer", LocalDateTime.now(), true, 0));
        save(new Vacancy(0, "Senior Java Developer", "Senior position for Java Developer", LocalDateTime.now(), true, 0));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId.incrementAndGet());
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(),
                (id, oldVacancy) -> new Vacancy(oldVacancy.getId(),
                        vacancy.getTitle(),
                        vacancy.getDescription(),
                        vacancy.getCreationDate(),
                        vacancy.getVisible(),
                        vacancy.getCityId())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}
