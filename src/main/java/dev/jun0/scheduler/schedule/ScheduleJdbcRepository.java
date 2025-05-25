package dev.jun0.scheduler.schedule;

import dev.jun0.scheduler.schedule.dto.ScheduleAuthorNameResponse;
import dev.jun0.scheduler.schedule.dto.ScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ScheduleJdbcRepository implements ScheduleRepository {
    private final JdbcTemplate jdbc;

    private final RowMapper<ScheduleResponse> scheduleMapper = (rs, rowNum) -> {
        ByteBuffer byteBuffer = ByteBuffer.wrap(rs.getBytes("author"));
        long mostSigBits = byteBuffer.getLong();
        long leastSigBits = byteBuffer.getLong();
        UUID uuid = new UUID(mostSigBits, leastSigBits);

        return new ScheduleResponse(
                rs.getLong("id"),
                rs.getString("task"),
                uuid.toString(),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        );
    };

    private final RowMapper<ScheduleAuthorNameResponse> scheduleAuthorNameMapper = (rs, rowNum) ->
            new ScheduleAuthorNameResponse(
                    rs.getLong("id"),
                    rs.getString("task"),
                    rs.getString("name"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at").toLocalDateTime()
            );

    @Override
    public ScheduleResponse save(String task, String authorUuid, String password) {
        String sql = "INSERT INTO schedules(task, author, password, created_at, updated_at) VALUES (?, (UUID_TO_BIN(?)), ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, task);
            ps.setString(2, authorUuid);
            ps.setString(3, password);
            ps.setTimestamp(4, Timestamp.valueOf(now));
            ps.setTimestamp(5, Timestamp.valueOf(now));
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();
        return new ScheduleResponse(generatedId, task, authorUuid, now, now);
    }

    @Override
    public ScheduleResponse update(Long id, String task, String authorUuid) {
        String sql = "UPDATE schedules SET ";
        List<Object> params = new ArrayList<>();

        if (task != null) {
            sql += "task = ?, ";
            params.add(task);
        }

        if (authorUuid != null && !authorUuid.isEmpty()) {
            sql += "author = (UUID_TO_BIN(?)), ";
            params.add(authorUuid);
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
    public List<ScheduleAuthorNameResponse> findAll(LocalDate updatedDate, String authorUuid, Pageable pageable) {
        String sql = "SELECT schedules.*, users.name FROM schedules JOIN users ON schedules.author = users.uuid WHERE 1=1";

        List<Object> params = new ArrayList<>();
        if (updatedDate != null) {
            sql += " AND DATE(schedules.updated_at) = ?";
            params.add(updatedDate);
        }
        if (authorUuid != null && !authorUuid.isEmpty()) {
            sql += " AND schedules.author = UUID_TO_BIN(?)";
            params.add(authorUuid);
        }
        sql += " ORDER BY schedules.updated_at DESC";

        sql += " LIMIT ? OFFSET ?";
        params.add(pageable.getPageSize());
        params.add(pageable.getPageNumber() * pageable.getPageSize());

        return jdbc.query(sql, scheduleAuthorNameMapper, params.toArray());
    }

}
