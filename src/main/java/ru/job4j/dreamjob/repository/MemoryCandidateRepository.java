package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import javax.annotation.concurrent.ThreadSafe;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/*
*@Repository
 */
@ThreadSafe
public class MemoryCandidateRepository implements CandidateRepository {

    private final AtomicInteger id = new AtomicInteger(-1);

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Ivanov Ivan Ivanovich", "Junior Java Developer",
                LocalDateTime.parse("2020-11-27T10:15:30"), 3, 0));
        save(new Candidate(0, "Petrov Petr Petrovich", "Middle Java Developer",
                LocalDateTime.parse("2022-11-27T10:15:30"), 3, 0));
        save(new Candidate(0, "Sidorov Igor Igorevich", "Senior Java Developer",
                LocalDateTime.parse("2023-11-28T00:15:30"), 3, 0));
    }

    @Override
    public Candidate save(Candidate candidate) {
        int nextId = id.incrementAndGet();
        candidate.setId(nextId);
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
       return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(), (id, oldCandidate) -> {
            return new Candidate(oldCandidate.getId(), candidate.getName(), candidate.getDescription(),
                    candidate.getCreationDate(), candidate.getCityId(), candidate.getFileId());
        }) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
