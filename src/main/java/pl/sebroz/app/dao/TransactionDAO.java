package pl.sebroz.app.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.sebroz.app.model.Transaction;

import java.util.List;

@Repository
@Transactional
public class TransactionDAO {
    private JdbcTemplate jdbcTemplate;

    public TransactionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Transaction> list(String firstKeyword, String secondKeyword) {
        if ((firstKeyword == null || firstKeyword.isEmpty()) && (secondKeyword == null || secondKeyword.isEmpty())) {
            return listAll();
        } else if (secondKeyword == null) {
            return listByType(firstKeyword);
        } else if (firstKeyword == null) {
            return listByType(secondKeyword);
        } else {
            return listAll();
        }
    }

    public List<Transaction> listAll() {
        String sql = "SELECT * FROM transactions";

        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Transaction.class));
    }

    public List<Transaction> listByType(String keyword) {
        String sql = "SELECT * FROM transactions WHERE type=" + keyword;

        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Transaction.class));
    }

    public void save(Transaction transaction) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("transactions").usingColumns("type", "description", "amount", "date");
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(transaction);

        jdbcInsert.execute(param);
    }

    public Transaction get(int id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        Object[] args = {id};

        return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Transaction.class), args);
    }

    public void update(Transaction transaction) {
        String sql = "UPDATE transactions SET type=:type, description=:description, amount=:amount, date=:date WHERE id=:id";
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(transaction);

        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
        template.update(sql, param);
    }

    public void delete(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

