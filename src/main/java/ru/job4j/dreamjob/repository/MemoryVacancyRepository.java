package ru.job4j.dreamjob.repository;

import ru.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryVacancyRepository implements VacancyRepository {

    private static final MemoryVacancyRepository INSTANCE = new MemoryVacancyRepository();

    private int nextId = 1;

    private final Map<Integer, Vacancy> vacancies = new HashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "Writing well designed, testable, efficient code",
                LocalDateTime.parse("2023-11-27T10:15:30")));
        save(new Vacancy(0, "Junior Java Developer", "Contributing in all phases of the development lifecycle",
                LocalDateTime.parse("2023-11-27T10:15:30")));
        save(new Vacancy(0, "Junior+ Java Developer", "Writing autotests junit5",
                LocalDateTime.parse("2023-11-28T00:15:30")));
        save(new Vacancy(0, "Middle Java Developer", "a lot of work for a lot of money",
                LocalDateTime.parse("2023-11-28T00:15:30")));
        save(new Vacancy(0, "Middle+ Java Developer", "a lot of work for little money",
                LocalDateTime.parse("2023-10-28T00:15:30")));
        save(new Vacancy(0, "Senior Java Developer", "Designing and developing high-volume"
                + "low-latency applications for mission-critical systems and delivering high-availability and performance",
                LocalDateTime.parse("2023-11-30T22:30:30")));
    }

    public static MemoryVacancyRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId++);
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public void deleteById(int id) {
        vacancies.remove(id);
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(),
                (id, oldVacancy) -> new Vacancy(oldVacancy.getId(), vacancy.getTitle(), vacancy.getDescription(),
                        vacancy.getCreationDate())) != null;
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