package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class MemoryVacancyRepository implements VacancyRepository {

    private final AtomicInteger id = new AtomicInteger(-1);

    private final Map<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();


    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "Writing well designed, testable, efficient code",
                LocalDateTime.parse("2023-11-27T10:15:30"), true));
        save(new Vacancy(0, "Junior Java Developer", "Contributing in all phases of the development lifecycle",
                LocalDateTime.parse("2023-11-27T10:15:30"), true));
        save(new Vacancy(0, "Junior+ Java Developer", "Writing autotests junit5",
                LocalDateTime.parse("2023-11-28T00:15:30"), true));
        save(new Vacancy(0, "Middle Java Developer", "a lot of work for a lot of money",
                LocalDateTime.parse("2023-11-28T00:15:30"), true));
        save(new Vacancy(0, "Middle+ Java Developer", "a lot of work for little money",
                LocalDateTime.parse("2023-10-28T00:15:30"), true));
        save(new Vacancy(0, "Senior Java Developer", "Designing and developing high-volume"
                + "low-latency applications for mission-critical systems and delivering high-availability and performance",
                LocalDateTime.parse("2023-11-30T22:30:30"), true));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        int nextId = id.incrementAndGet();
        vacancy.setId(nextId);
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(), (id, oldVacancy) -> {
            return new Vacancy(oldVacancy.getId(), vacancy.getTitle(), vacancy.getDescription(), vacancy.getCreationDate(), vacancy.getVisible());
        }) != null;
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