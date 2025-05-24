package dev.jun0.scheduler.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleJdbcRepository implements ScheduleRepository {
    private final JdbcTemplate jdbc;

    private final RowMapper<Schedule> scheduleMapper = (rs, rowNum) -> new Schedule(
            rs.getLong("id"),
            rs.getString("task"),
            rs.getString("author"),
            rs.getString("password"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime()
    );

    @Override
    public void save(Schedule schedule) {
        String sql = "INSERT INTO schedules(task, author, password, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, schedule.getTask());
            ps.setString(2, schedule.getAuthor());
            ps.setString(3, schedule.getPassword());
            ps.setTimestamp(4, Timestamp.valueOf(schedule.getCreatedAt()));
            ps.setTimestamp(5, Timestamp.valueOf(schedule.getUpdatedAt()));
            return ps;
        });
    }

    @Override
    public Optional<Schedule> findById(Long id) {
        String sql = "SELECT * FROM schedules WHERE id = ?";
        List<Schedule> results = jdbc.query(sql, scheduleMapper, id);
        return results.stream().findFirst();
    }

    @Override
    public List<Schedule> findAll(LocalDate updatedDate, String author) {
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
