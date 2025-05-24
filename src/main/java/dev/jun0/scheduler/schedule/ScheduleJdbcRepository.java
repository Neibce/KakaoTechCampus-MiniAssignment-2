package dev.jun0.scheduler.schedule;

import dev.jun0.scheduler.schedule.dto.ScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleJdbcRepository implements ScheduleRepository {
    private final JdbcTemplate jdbc;

    private final RowMapper<ScheduleResponse> scheduleMapper = (rs, rowNum) -> new ScheduleResponse(
            rs.getLong("id"),
            rs.getString("task"),
            rs.getString("author"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime()
    );

    @Override
    public ScheduleResponse save(String task, String author, String password) {
        String sql = "INSERT INTO schedules(task, author, password, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, task);
            ps.setString(2, author);
            ps.setString(3, password);
            ps.setTimestamp(4, Timestamp.valueOf(now));
            ps.setTimestamp(5, Timestamp.valueOf(now));
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();
        return new ScheduleResponse(generatedId, task, author, now, now);
    }

    @Override
    public ScheduleResponse update(Long id, String task, String author) {
        String sql = "UPDATE schedules SET ";
        List<Object> params = new ArrayList<>();

        if (task != null) {
            sql += "task = ?, ";
            params.add(task);
        }

        if (author != null) {
            sql += "author = ?, ";
            params.add(author);
        }
        sql += "updated_at = ? WHERE id = ?";
        params.add(LocalDateTime.now());
        params.add(id);

        jdbc.update(sql, params.toArray());

        String selectSql = "SELECT * FROM schedules WHERE id = ?";
        return jdbc.queryForObject(selectSql, scheduleMapper, id);
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM schedules WHERE id = ?";
        jdbc.update(sql, id);
    }

    @Override
    public boolean isPasswordValid(Long id, String password) {
        String sql = "SELECT COUNT(*) FROM schedules WHERE id = ? AND password = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, id, password);
        return count != null && count > 0;
    }

    @Override
    public Optional<ScheduleResponse> findById(Long id) {
        String sql = "SELECT * FROM schedules WHERE id = ?";
        List<ScheduleResponse> results = jdbc.query(sql, scheduleMapper, id);
        return results.stream().findFirst();
    }

    @Override
    public List<ScheduleResponse> findAll(LocalDate updatedDate, String author) {
        String sql = "SELECT * FROM schedules WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (updatedDate != null) {
            sql += " AND DATE(updated_at) = ?";
            params.add(updatedDate);
        }
        if (author != null && !author.isEmpty()) {
            sql += " AND author = ?";
            params.add(author);
        }
        sql += " ORDER BY updated_at DESC";

        if (!params.isEmpty())
            return jdbc.query(sql, scheduleMapper, params.toArray());
        else
            return jdbc.query(sql, scheduleMapper);
    }

}
